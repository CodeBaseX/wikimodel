/**
 * 
 */
package org.wikimodel.wem.xml;

import org.wikimodel.wem.IWemListenerSemantic;

/**
 * @author kotelnikov
 */
public class WemSemanticTagNotifier extends AbstractTagNotifier
    implements
    IWemListenerSemantic {

    /**
     * @param listener
     */
    public WemSemanticTagNotifier(ITagListener listener) {
        super(listener);
    }

    public void beginPropertyBlock(String propertyUri, boolean doc) {
        fListener.beginTag(PROPERTY_BLOCK, tagParams(
            PROPERTY_URL,
            propertyUri,
            PROPERTY_DOC,
            "" + doc), EMPTY_MAP);
    }

    public void beginPropertyInline(String propertyUri) {
        fListener.beginTag(
            PROPERTY_INLINE,
            tagParams(PROPERTY_URL, propertyUri),
            EMPTY_MAP);
    }

    public void endPropertyBlock(String propertyUri, boolean doc) {
        fListener.endTag(PROPERTY_BLOCK, tagParams(
            PROPERTY_URL,
            propertyUri,
            PROPERTY_DOC,
            "" + doc), EMPTY_MAP);
    }

    public void endPropertyInline(String propertyUri) {
        fListener.endTag(
            PROPERTY_INLINE,
            tagParams(PROPERTY_URL, propertyUri),
            EMPTY_MAP);
    }

}
