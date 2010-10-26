package org.wikimodel.wem;

/**
 * This listener is used to notify about semantic elements defined in the
 * document. Possible semantic elements are inline and block properties. Each
 * block property can have a document or a simple paragraph as its value. Each
 * inline element contains only formatted inline elements.
 * 
 * @author kotelnikov
 */
public interface IWemListenerSemantic {

    /**
     * This method is called to notify about the beginning of a new property
     * found in the parsed document.
     * 
     * @param propertyUri the URI of the semantic block property found in the
     *        document
     * @param doc this flag is <code>true</code> if the found property contains
     *        a whole document; if this flag is <code>false</code> then expected
     *        property value contains a paragraph
     */
    void beginPropertyBlock(String propertyUri, boolean doc);

    /**
     * This method is called to notify about the beginning of a new inline
     * property found in the text of the parsed document.
     * 
     * @param propertyUri the URI of the semantic inline property found in the
     *        document
     */
    void beginPropertyInline(String propertyUri);

    /**
     * This method is called to notify about the end of a block property found
     * in the parsed document.
     * 
     * @param propertyUri the URI of the semantic block property found in the
     *        document
     * @param doc this flag is <code>true</code> if the found property contains
     *        a whole document; otherwise (if this flag is <code>false</code>)
     *        the value of the property is a simple paragraph
     */
    void endPropertyBlock(String propertyUri, boolean doc);

    /**
     * This method is called to notify about the end of an inline property found
     * in the text of the parsed document.
     * 
     * @param propertyUri the URI of the semantic inline property found in the
     *        document
     */
    void endPropertyInline(String propertyUri);

}
