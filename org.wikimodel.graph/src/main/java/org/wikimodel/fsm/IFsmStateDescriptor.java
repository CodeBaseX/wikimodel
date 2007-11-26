package org.wikimodel.fsm;

public interface IFsmStateDescriptor {

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
    FsmState getNextState(FsmState parent, FsmState state, IFsmEvent event);
}