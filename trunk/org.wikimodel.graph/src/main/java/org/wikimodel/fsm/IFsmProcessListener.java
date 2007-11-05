/**
 * 
 */
package org.wikimodel.fsm;

/**
 * @author kotelnikov
 */
public interface IFsmProcessListener {

    void beginState(FsmState node) throws Exception;

    void endState(FsmState node) throws Exception;

    void handleError(FsmState state, Throwable t) throws Exception;

    FsmState newState(
        FsmProcess process,
        FsmState parent,
        String stateKey,
        IFsmStateDescriptor descriptor);

}
