/**
 * 
 */
package org.wikimodel.wem.xml;

import org.wikimodel.wem.AgregatingWemListener;

/**
 * @author kotelnikov
 */
public class WemTagNotifier extends AgregatingWemListener {

    /**
     * 
     */
    public WemTagNotifier(ITagListener listener) {
        super();
        setInlineListener(new WemInlineTagNotifier(listener));
        setBlockListener(new WemSimpleBlockTagNotifier(listener));
        setDocumentListener(new WemDocumentTagNotifier(listener));
        setSemanticListener(new WemSemanticTagNotifier(listener));
        setProgrammingListener(new WemProgrammingTagNotifier(listener));
        setTableListener(new WemTableTagNotifier(listener));
        setListListener(new WemListTagNotifier(listener));
    }

}
