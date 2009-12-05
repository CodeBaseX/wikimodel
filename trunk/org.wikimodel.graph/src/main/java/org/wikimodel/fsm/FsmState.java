/**
 * 
 */
package org.wikimodel.fsm;

/**
 * @author kotelnikov
 */
public class FsmState {

    private IFsmStateDescriptor fDescriptor;

    private String fKey;

    private FsmState fParent;

    public FsmState(FsmState parent, String key, IFsmStateDescriptor descriptor) {
        fKey = key;
        fParent = parent;
        fDescriptor = descriptor;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof FsmState))
            return false;
        FsmState state = (FsmState) obj;
        return fKey.equals(state.fKey)
            && fDescriptor.equals(state.fDescriptor)
            && (fParent == null || state.fParent == null
                ? fParent == state.fParent
                : fParent.equals(state.fParent));
    }

    /**
     * @return the descriptor corresponding to this state
     */
    public IFsmStateDescriptor getDescriptor() {
        return fDescriptor;
    }

    /**
     * @return the key of this state
     */
    public String getKey() {
        return fKey;
    }

    /**
     * @return the parent of this state
     */
    public FsmState getParentState() {
        return fParent;
    }

    public String getPath() {
        StringBuffer buf = new StringBuffer();
        FsmState state = this;
        while (state != null) {
            if (buf.length() > 0)
                buf.insert(0, "/");
            buf.insert(0, state.getKey());
            state = state.fParent;
        }
        return buf.toString();
    }

    @Override
    public int hashCode() {
        return fDescriptor.hashCode();
    }

    @Override
    public String toString() {
        return getPath();
    }

}
