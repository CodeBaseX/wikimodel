/**
 * 
 */
package org.wikimodel.fsm;

import java.util.HashMap;
import java.util.Map;

/**
 * @author kotelnikov
 */
public class FsmStateDescriptor
    implements
    IFsmStateDescriptor,
    IFsmProcessConst {

    private Map<String, IFsmStateDescriptor> fSubstateDescriptors = new HashMap<String, IFsmStateDescriptor>();

    private Map<String, String> fTransitionMap = new HashMap<String, String>();

    /**
     * 
     */
    public FsmStateDescriptor() {
        super();
    }

    /**
     * @param substateKey
     * @param descriptor
     */
    public void addSubstateDescriptor(
        String substateKey,
        IFsmStateDescriptor descriptor) {
        fSubstateDescriptors.put(substateKey, descriptor);
    }

    /**
     * @param descriptor
     */
    public void addTransition(String from, String event, String to) {
        String transitionKey = getTransitionKey(from, event);
        if (to == null)
            to = IFsmProcessConst.STATE_FINAL;
        fTransitionMap.put(transitionKey, to);
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof FsmStateDescriptor))
            return false;
        FsmStateDescriptor descriptor = (FsmStateDescriptor) obj;
        return fTransitionMap.equals(descriptor.fTransitionMap)
            && fSubstateDescriptors.equals(descriptor.fSubstateDescriptors);
    }

    /**
     * Returns the next sub-state of the specified parent; This method loads the
     * description of the transition for the specified sub-state and the given
     * event in the parent's descriptor.
     * 
     * @param parent the parent state; the descriptor of this state is used to
     *        get the definition of transition from the spec /**
     * @see org.wikimodel.fsm.IFsmStateDescriptor#newStateified sub-state and
     *      for the given event
     * @param state the sub-state of the specified parent
     * @param event the event initializing the transition from the given
     *        sub-state
     * @return the next state corresponding to the specified transition
     */
    public FsmState getNextState(
        FsmState parent,
        FsmState state,
        IFsmEvent event) {
        if (parent == null)
            return null;
        String key = state != null ? state.getKey() : STATE_INITIAL;
        if (event == null)
            event = IFsmEvent.NULL;
        String to = getTransitionTargetKey(parent, key, event);
        if (STATE_INITIAL.equals(to)) {
            to = getTransitionTargetKey(parent, STATE_INITIAL, event);
        }
        if (to == null || STATE_FINAL.equals(to))
            return null;
        FsmStateDescriptor descriptor = (FsmStateDescriptor) getSubstateDescriptor(
            parent,
            to);
        return descriptor.newState(parent, to);
    }

    /**
     * Returns the target state of the transition
     * 
     * @param from the start transition state
     * @param event the event activating transition
     * @return a key of the transition destination state
     */
    public String getNextSubstate(String from, String event) {
        String transitionKey = getTransitionKey(from, event);
        return fTransitionMap.get(transitionKey);
    }

    @SuppressWarnings("unchecked")
    private FsmStateDescriptor getStateDescriptor(FsmState parent) {
        return (FsmStateDescriptor) parent.getDescriptor();
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
            FsmStateDescriptor parentDescriptor = getStateDescriptor(parent);
            descriptor = parentDescriptor.getSubstateDescriptor(substateKey);
            parent = parent.getParentState();
        }
        return descriptor != null ? descriptor : new FsmStateDescriptor();
    }

    /**
     * Returns a descriptor of a sub-state corresponding to the specified key
     * 
     * @param substateKey the key of the substate for which the corresponding
     *        descriptor should be returned
     * @return a substate descriptor corresponding to the specified state key
     */
    public IFsmStateDescriptor getSubstateDescriptor(String substateKey) {
        return fSubstateDescriptors.get(substateKey);
    }

    /**
     * @param stateKey
     * @param eventKey
     * @return a transition key corresponding to the transition form a state
     *         with the given key the specified event is occurred
     */
    protected String getTransitionKey(String stateKey, String eventKey) {
        if (stateKey == null) {
            stateKey = IFsmProcessConst.STATE_INITIAL;
        }
        if (eventKey == null) {
            eventKey = "";
        }
        return stateKey + "#" + eventKey;
    }

    /**
     * Returns the key of the transition target. By default this method tries to
     * load a transition for the specified sub-state key and the event and if
     * there is no descriptor was found then it trims the event key to the last
     * "." symbol and repeat the same operation until the event key becomes
     * empty (""). This method can return <code>null</code> if there transition
     * was found. In this case the FSM will closes the specified parent state:
     * it considers that there is no sub-states of this state and it can be
     * deactivated.
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
        IFsmEvent event) {
        FsmStateDescriptor parentDescriptor = getStateDescriptor(parent);
        String result = null;
        while (event != null) {
            String eventKey = event.getKey();
            result = parentDescriptor.getNextSubstate(substateKey, eventKey);
            if (result != null)
                break;
            result = parentDescriptor.getNextSubstate(STATE_ANY, eventKey);
            if (result != null)
                break;
            event = event.getParentEvent();
        }
        return result;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        int a = fSubstateDescriptors.hashCode();
        int b = fTransitionMap.hashCode();
        return (a ^ (b >>> 32));
    }

    /**
     * @param fsmProcess the process for which this state should be created
     * @param parent the parent state of the state to create
     * @param stateKey the key of the state
     * @return a new state corresponding to this descriptor
     */
    public FsmState newState(FsmState parent, String stateKey) {
        return new FsmState(parent, stateKey, this);
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return fTransitionMap.toString();
    }

}
