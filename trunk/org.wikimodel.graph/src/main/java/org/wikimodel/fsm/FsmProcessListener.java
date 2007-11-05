/**
 * 
 */
package org.wikimodel.fsm;

/**
 * @author kotelnikov
 */
public class FsmProcessListener implements IFsmProcessListener {

    /**
     * 
     */
    public FsmProcessListener() {
        super();
    }

    /**
     * @see org.wikimodel.fsm.IFsmProcessListener#beginState(org.wikimodel.fsm.FsmState)
     */
    public void beginState(FsmState node) throws Exception {
        //
    }

    /**
     * @see org.wikimodel.fsm.IFsmProcessListener#endState(org.wikimodel.fsm.FsmState)
     */
    public void endState(FsmState node) throws Exception {
        //
    }

    /**
     * @see org.wikimodel.fsm.IFsmProcessListener#handleError(org.wikimodel.fsm.FsmState,
     *      java.lang.Throwable)
     */
    public void handleError(FsmState state, Throwable t) throws Exception {
        //
    }

    /**
     * @see org.wikimodel.fsm.IFsmProcessListener#newState(org.wikimodel.fsm.FsmState,
     *      java.lang.String, org.wikimodel.fsm.IFsmStateDescriptor)
     */
    public FsmState newState(
        FsmProcess process,
        FsmState parent,
        String stateKey,
        IFsmStateDescriptor descriptor) {
        return new FsmState(process, stateKey, descriptor, parent);
    }

}
