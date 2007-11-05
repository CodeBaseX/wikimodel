/**
 * 
 */
package org.wikimodel.fsm;

/**
 * @author kotelnikov
 */
public class FsmState {

    protected IFsmStateDescriptor fDescriptor;

    protected String fKey;

    protected FsmState fParent;

    private FsmProcess fProcess;

    public FsmState(
        FsmProcess process,
        String key,
        IFsmStateDescriptor descriptor,
        FsmState parent) {
        fProcess = process;
        fKey = key;
        fParent = parent;
        fDescriptor = descriptor;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
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

    public FsmProcess getProcess() {
        return fProcess;
    }

    public int hashCode() {
        return fDescriptor.hashCode();
    }

    public String toString() {
        return getPath();
    }

}
