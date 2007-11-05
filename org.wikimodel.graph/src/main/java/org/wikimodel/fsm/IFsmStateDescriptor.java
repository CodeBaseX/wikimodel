package org.wikimodel.fsm;

public interface IFsmStateDescriptor {

    /**
     * Returns the target state of the transition
     * 
     * @param from the start transition state
     * @param event the event activating transition
     * @return a key of the transition destination state
     */
    String getNextSubstate(String from, String event);

    /**
     * Returns a descriptor of a sub-state corresponding to the specified key
     * 
     * @param substateKey the key of the substate for which the corresponding
     *        descriptor should be returned
     * @return a substate descriptor corresponding to the specified state key
     */
    IFsmStateDescriptor getSubstateDescriptor(String substateKey);

}