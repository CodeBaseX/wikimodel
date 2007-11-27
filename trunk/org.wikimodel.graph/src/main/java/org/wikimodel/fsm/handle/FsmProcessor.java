package org.wikimodel.fsm.handle;

import org.wikimodel.fsm.FsmState;
import org.wikimodel.fsm.IFsmProcessListener;
import org.wikimodel.fsm.IFsmStateDescriptor;

/**
 * @author kotelnikov
 */
public class FsmProcessor implements IFsmProcessListener {

    /**
     * 
     */
    public FsmProcessor() {
        super();
    }

    static class InternalFsmState extends FsmState {

        public InternalFsmState(
            FsmState parent,
            String key,
            IFsmStateDescriptor descriptor) {
            super(parent, key, descriptor);
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

    public void handleError(FsmState node, Throwable t) {
        InternalFsmState state = (InternalFsmState) node;
        IFsmStateBean bean = state.getBean();
        if (bean != null) {
            try {
                bean.handleError(t);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

}
