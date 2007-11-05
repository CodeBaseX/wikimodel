/**
 * 
 */
package org.wikimodel.fsm;

import java.util.HashMap;
import java.util.Map;

/**
 * @author kotelnikov
 */
public class FsmStateDescriptor implements IFsmStateDescriptor {

    public static final IFsmStateDescriptor NULL_DESCRIPTOR = new FsmStateDescriptor();

    private Map fSubstateDescriptors = new HashMap();

    private Map fTransitionMap = new HashMap();

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
     * @see org.wikimodel.fsm.IFsmStateDescriptor#getNextSubstate(java.lang.String,
     *      java.lang.String)
     */
    public String getNextSubstate(String from, String event) {
        String transitionKey = getTransitionKey(from, event);
        return (String) fTransitionMap.get(transitionKey);
    }

    /**
     * @see org.wikimodel.fsm.IFsmStateDescriptor#getSubstateDescriptor(java.lang.String)
     */
    public IFsmStateDescriptor getSubstateDescriptor(String substateKey) {
        return (IFsmStateDescriptor) fSubstateDescriptors.get(substateKey);
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
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        int a = fSubstateDescriptors.hashCode();
        int b = fTransitionMap.hashCode();
        return (int) (a ^ (b >>> 32));
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return fTransitionMap.toString();
    }
}
