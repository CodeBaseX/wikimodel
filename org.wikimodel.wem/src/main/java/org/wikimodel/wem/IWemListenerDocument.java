package org.wikimodel.wem;

/**
 * Instances of this type are used to notify about parsed documents. This
 * listener is called for top-level documents as well as for "embedded"
 * documents found in the main document.
 * 
 * @author kotelnikov
 */
public interface IWemListenerDocument {

    /**
     * This method is called to notify about the beginning of the top-level
     * parsed document or about the beginning of an embedded document (contained
     * in the main one).
     */
    void beginDocument();

    /**
     * This method is called to notify about the beginning of the top-level
     * parsed document or about the beginning of an embedded document (contained
     * in the main one).
     */
    void beginDocument(WikiParameters params);

    /**
     * This method is called to notify about a new section header found in the
     * document.
     * 
     * @param level the level of the found header; valid values: 1-6
     * @param params wiki parameters associated with the
     */
    void beginHeader(int level, WikiParameters params);

    /**
     * This method is used to notify about the end of a top-level or an internal
     * document.
     */
    void endDocument();

    /**
     * This method is used to notify about the end of a top-level or an internal
     * document.
     */
    void endDocument(WikiParameters params);

    /**
     * This method is called to notify about the end of a section-level header.
     * 
     * @param level the level of the header
     * @param params wiki parameters of the header
     */
    void endHeader(int level, WikiParameters params);

}
