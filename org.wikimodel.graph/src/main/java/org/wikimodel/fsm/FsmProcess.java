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

    private INodeWalkerListener<FsmState, RuntimeException> fListener = new INodeWalkerListener<FsmState, RuntimeException>() {

        public void beginNode(FsmState parent, FsmState node) {
            try {
                fProcessListener.beginState(node);
            } catch (Throwable t) {
                fProcessListener.handleError(node, t);
            }
        }

        public void endNode(FsmState parent, FsmState node) {
            try {
                fProcessListener.endState(node);
            } catch (Throwable t) {
                fProcessListener.handleError(node, t);
            }
        }

    };

    protected IFsmProcessListener fProcessListener;

    private FsmState fRootState;

    private boolean fTerminated;

    private AbstractNodeWalker<FsmState, RuntimeException> fWalker = new AbstractNodeWalker<FsmState, RuntimeException>(
        new INodeWalkerSource<FsmState, RuntimeException>() {

            public FsmState getFirstSubnode(FsmState node) {
                if (isTerminated())
                    return null;
                return getNextState(node, null, fEvent);
            }

            private FsmState getNextState(
                FsmState parent,
                FsmState state,
                IFsmEvent event) {
                if (parent == null)
                    return null;
                IFsmStateDescriptor descriptor = parent.getDescriptor();
                return descriptor.getNextState(parent, state, event);
            }

            public FsmState getNextSubnode(FsmState parent, FsmState node) {
                if (isTerminated())
                    return null;
                return getNextState(parent, node, fEvent);
            }

        },
        null) {

        protected FsmState fPeek;

        protected FsmState getPeekNode() {
            return fPeek;
        }

        protected FsmState popNode() {
            FsmState result = fPeek;
            fPeek = fPeek != null ? fPeek.getParentState() : null;
            return result;
        }

        protected void pushNode(FsmState currentNode) {
            fPeek = currentNode;
        }

    };

    public FsmProcess(FsmState rootState, IFsmProcessListener processListener) {
        super();
        fProcessListener = processListener;
        fRootState = rootState;
        fWalker.setNextNode(fRootState);
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @SuppressWarnings("unchecked")
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
        return fWalker.getCurrentNode();
    }

    public IFsmEvent getEvent() {
        return fEvent;
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

    /**
     * @param mask the "debugging" mask
     * @return <code>true</code> if the next step was successfully performed
     */
    public boolean nextStep(AbstractNodeWalker.Mode mode) {
        fTerminated = !fWalker.shift(fListener, mode);
        return !fTerminated;
    }

    public void setEvent(IFsmEvent event) {
        fEvent = event;
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
