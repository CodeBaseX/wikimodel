package org.wikimodel.fsm;

public class FsmEvent implements IFsmEvent {

    private String fKey;

    public FsmEvent(String key) {
        fKey = key;
    }

    /**
     * @return the key of this event
     */
    public String getKey() {
        return fKey;
    }

}
