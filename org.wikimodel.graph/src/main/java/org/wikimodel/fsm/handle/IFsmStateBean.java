/**
 * 
 */
package org.wikimodel.fsm.handle;

/**
 * @author kotelnikov
 */
public interface IFsmStateBean {

    void activate() throws Exception;

    void deactivate() throws Exception;

    void handleError(Throwable t) throws Exception;

}
