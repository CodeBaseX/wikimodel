package org.wikimodel.fsm.handle;

import java.util.Hashtable;
import java.util.Map;

import org.wikimodel.fsm.FsmProcess;
import org.wikimodel.fsm.FsmState;
import org.wikimodel.fsm.IFsmEvent;
import org.wikimodel.fsm.IFsmProcessListener;
import org.wikimodel.fsm.IFsmStateDescriptor;

/**
 * @author kotelnikov
 */
public class FsmProcessor implements IFsmProcessListener {

    private Map fStateBeanFactoryMap = new Hashtable();

    /**
     * 
     */
    public FsmProcessor() {
        super();
    }

    static class InternalFsmState extends FsmState {

        public InternalFsmState(
            FsmProcess process,
            String key,
            IFsmStateDescriptor descriptor,
            FsmState parent) {
            super(process, key, descriptor, parent);
        }

        public IFsmStateBean getBean() {
            // TODO Auto-generated method stub
            return null;
        }

    }

    public void beginState(FsmState node) throws Exception {
        InternalFsmState state = (InternalFsmState) node;
        IFsmStateBean bean = state.getBean();
        if (bean != null) {
            bean.activate();
        }
    }

    public void endState(FsmState node) throws Exception {
        InternalFsmState state = (InternalFsmState) node;
        IFsmStateBean bean = state.getBean();
        if (bean != null) {
            bean.deactivate();
        }
    }

    public void handleError(FsmState node, Throwable t) throws Exception {
        InternalFsmState state = (InternalFsmState) node;
        IFsmStateBean bean = state.getBean();
        if (bean != null) {
            bean.handleError(t);
        }
    }

    public FsmState newState(
        FsmProcess process,
        FsmState parent,
        String stateKey,
        IFsmStateDescriptor descriptor) {
        return new InternalFsmState(process, stateKey, descriptor, parent);
    }

}
