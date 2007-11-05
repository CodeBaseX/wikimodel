/**
 * 
 */
package org.wikimodel.fsm;

import org.wikimodel.graph.AbstractNodeWalker;
import org.wikimodel.graph.INodeWalkerListener;
import org.wikimodel.graph.INodeWalkerSource;

/**
 * @author kotelnikov
 */
public class FsmProcess implements IFsmProcessConst {

    private IFsmEvent fEvent;

    private INodeWalkerListener fListener = new INodeWalkerListener() {

        public void beginNode(Object parent, Object node) throws Exception {
            FsmState state = (FsmState) node;
            try {
                fProcessListener.beginState(state);
            } catch (Throwable t) {
                fProcessListener.handleError(state, t);
            }
        }

        public void endNode(Object parent, Object node) throws Exception {
            FsmState state = (FsmState) node;
            try {
                fProcessListener.endState(state);
            } catch (Throwable t) {
                fProcessListener.handleError(state, t);
            }
        }

    };

    protected IFsmProcessListener fProcessListener;

    private FsmState fRootState;

    private boolean fTerminated;

    private AbstractNodeWalker fWalker = new AbstractNodeWalker(
        new INodeWalkerSource() {

            public Object getFirstSubnode(Object node) throws Exception {
                if (isTerminated())
                    return null;
                return getNextState((FsmState) node, null, fEvent);
            }

            public Object getNextSubnode(Object parent, Object node)
                throws Exception {
                if (isTerminated())
                    return null;
                return getNextState((FsmState) parent, (FsmState) node, fEvent);
            }

        },
        null) {

        protected FsmState fPeek;

        protected Object getPeekNode() {
            return fPeek;
        }

        protected Object popNode() {
            FsmState result = fPeek;
            fPeek = fPeek != null ? fPeek.fParent : null;
            return result;
        }

        protected void pushNode(Object currentNode) {
            fPeek = (FsmState) currentNode;
        }

    };

    public FsmProcess(
        IFsmStateDescriptor rootStateDescriptor,
        String key,
        IFsmProcessListener processListener) {
        super();
        fProcessListener = processListener;
        fRootState = newState(null, key, rootStateDescriptor);
        fWalker.setNextNode(fRootState);
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof FsmProcess))
            return false;
        FsmProcess process = (FsmProcess) obj;
        return fRootState.equals(process.fRootState);
    }

    /**
     * Returns the current active state; an active state is a state for which
     * the {@link IFsmProcessListener#beginState(FsmState)} was called.
     * 
     * @return the current active state
     */
    public FsmState getCurrentState() {
        return (FsmState) fWalker.getCurrentNode();
    }

    public IFsmEvent getEvent() {
        return fEvent;
    }

    /**
     * Returns the next sub-state of the specified parent; This method loads the
     * description of the transition for the specified sub-state and the given
     * event in the parent's descriptor.
     * 
     * @param parent the parent state; the descriptor of this state is used to
     *        get the definition of transition from the specified sub-state and
     *        for the given event
     * @param state the sub-state of the specified parent
     * @param event the event initializing the transition from the given
     *        sub-state
     * @return the next state corresponding to the specified transition
     */
    protected FsmState getNextState(
        FsmState parent,
        FsmState state,
        IFsmEvent event) {
        if (parent == null)
            return null;
        String key = state != null ? state.fKey : STATE_INITIAL;
        String eventKey = event != null ? event.getKey() : "";
        String to = getTransitionTargetKey(parent, key, eventKey);
        if (STATE_INITIAL.equals(to)) {
            to = getTransitionTargetKey(parent, STATE_INITIAL, eventKey);
        }
        if (to == null || STATE_FINAL.equals(to))
            return null;
        IFsmStateDescriptor descriptor = getSubstateDescriptor(parent, to);
        return newState(parent, to, descriptor);
    }

    /**
     * Returns a sub-state descriptor corresponding to the given sub-state key.
     * By default this method tries to load a sub-state descriptor in the given
     * state and if there is nothing was found then it recursively repeats the
     * same operation for state's parents.
     * 
     * @param parent the parent state which is used to find a sub-state
     *        descriptor for the given key
     * @param substateKey the key of a sub-state; for this key the corresponding
     *        descriptor will be returned.
     * @return a descriptor of a sub-state corresponding to the specified state
     *         key
     */
    protected IFsmStateDescriptor getSubstateDescriptor(
        FsmState parent,
        String substateKey) {
        IFsmStateDescriptor descriptor = null;
        while (parent != null && descriptor == null) {
            IFsmStateDescriptor parentDescriptor = parent.fDescriptor;
            descriptor = parentDescriptor.getSubstateDescriptor(substateKey);
            parent = parent.fParent;
        }
        return descriptor != null
            ? descriptor
            : FsmStateDescriptor.NULL_DESCRIPTOR;
    }

    /**
     * Returns the key of the transition target. By default this method tries to
     * load a transition for the specified sub-state key and the event and if
     * there is no descriptor was found then it trims the event key to the last
     * "." symbol and repeat the same operation until the event key becomes
     * empty (""). This method can return <code>null</code> if there
     * transition was found. In this case the FSM will closes the specified
     * parent state: it considers that there is no sub-states of this state and
     * it can be deactivated.
     * 
     * @param parent the parent state; this method returns a target key for a
     *        transition between sub-states of this state
     * @param substateKey the key of a initial state of the transition
     * @param eventKey the key of the event activating the transition
     * @return the key of the target state
     */
    protected String getTransitionTargetKey(
        FsmState parent,
        String substateKey,
        String eventKey) {
        if (eventKey == null)
            eventKey = "";
        IFsmStateDescriptor parentDescriptor = parent.fDescriptor;
        String result = null;
        while (true) {
            result = parentDescriptor.getNextSubstate(substateKey, eventKey);
            if (result != null || "".equals(eventKey))
                break;
            result = parentDescriptor.getNextSubstate(STATE_ANY, eventKey);
            if (result != null)
                break;
            int idx = eventKey.lastIndexOf(".");
            if (idx < 0)
                eventKey = "";
            else
                eventKey = eventKey.substring(0, idx);
        }
        return result;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return fRootState.hashCode();
    }

    public boolean isTerminated() {
        return fTerminated;
    }

    protected FsmState newState(
        FsmState parent,
        String stateKey,
        IFsmStateDescriptor descriptor) {
        return descriptor != null ? fProcessListener.newState(
            this,
            parent,
            stateKey,
            descriptor) : null;
    }

    /**
     * @param mask the "debugging" mask
     * @return <code>true</code> if the next step was successfully performed
     * @throws Exception
     */
    public boolean nextStep(int mask) throws Exception {
        int status = 0;
        boolean result;
        while (result = !fWalker.isFinished()) {
            status = fWalker.shift(fListener) ? STEP_DOWN : STEP_UP;
            status |= fWalker.getNextNode() != null ? STEP_NEXT : STEP_NO_NEXT;
            if ((status & mask) == mask) {
                break;
            }
        }
        fTerminated |= !result;
        return result;
    }

    public void setEvent(IFsmEvent event) {
        fEvent = event;
    }

    public void setEvent(String key) {
        setEvent(new FsmEvent(key));
    }

    public void setTerminated(boolean terminated) {
        fTerminated = terminated;
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return "Process[" + fRootState + "]";
    }
}
