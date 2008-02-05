package org.wikimodel.fsm;

public class FsmEvent implements IFsmEvent {

    public static IFsmEvent newEvent(String eventKey) {
        char[] array = eventKey != null ? eventKey.toCharArray() : null;
        StringBuffer buf = new StringBuffer();
        int len = array != null ? array.length : 0;
        IFsmEvent e = IFsmEvent.NULL;
        int i;
        for (i = 0; i < len; i++) {
            char ch = array[i];
            if (ch == '.') {
                if (i > 0) {
                    e = new FsmEvent(e, buf.toString());
                }
            }
            buf.append(ch);
        }
        if (i > 0) {
            e = new FsmEvent(e, buf.toString());
        }
        return e;
    }

    private String fKey;

    private IFsmEvent fParent;

    public FsmEvent(IFsmEvent parent, String key) {
        assert key != null : "The event key can not be null";
        fParent = parent;
        fKey = key;
    }

    public FsmEvent(String key) {
        this(null, key);
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof IFsmEvent))
            return false;
        IFsmEvent e = (IFsmEvent) obj;
        String key = e.getKey();
        if (!fKey.equals(key))
            return false;
        IFsmEvent first = e.getParentEvent();
        IFsmEvent second = e.getParentEvent();
        return first == null || second == null ? first == second : first
            .equals(second);
    }

    /**
     * @return the key of this event
     */
    public String getKey() {
        return fKey;
    }

    public IFsmEvent getParentEvent() {
        return fParent;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return fKey != null ? fKey.hashCode() : 0;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return fKey;
    }

}
