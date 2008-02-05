/**
 * 
 */
package org.wikimodel.fsm;

/**
 * @author kotelnikov
 */
public interface IFsmEvent {

    IFsmEvent NULL = new FsmEvent("");

    /**
     * @return the key of this event
     */
    String getKey();

    /**
     * @return the parent event or null if this is a root event
     */
    IFsmEvent getParentEvent();

}
