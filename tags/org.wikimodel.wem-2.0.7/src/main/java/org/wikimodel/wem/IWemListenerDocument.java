package org.wikimodel.wem;

/**
 * Instances of this type are used to notify about parsed documents. This
 * listener is called for top-level documents as well as for "embedded"
 * documents found in the main document.
 * 
 * <pre>
 * 
 * Examples A: - a very simple document
 * -------------------------------+--------------------------------------------
 * Source                         |  Result
 * -------------------------------+--------------------------------------------
 *  = First Header =              |+ section0
 *  First paragraph.              |  + document 
 *                                |  + seciontContent0
 *                                |    + section1
 *                                |      + header1 = First Header
 *                                |      + sectionContent1
 *                                |        + paragraph = First paragraph.
 * -------------------------------+--------------------------------------------
 * 
 * Examples B: - document with two headers (one section into another)
 * -------------------------------+--------------------------------------------
 * Source                         |  Result
 * -------------------------------+--------------------------------------------
 *  = First Header =              |+ section0
 *  First paragraph.              |  + document 
 *  == Second Header ==           |  + sectionContent0
 *  Second paragraph.             |    + section1
 *                                |      + header1 = First Header
 *                                |      + sectionContent1
 *                                |        + paragraph = First paragraph.
 *                                |        + section2
 *                                |          + header2 = Second Header
 *                                |          + sectionContent2
 *                                |            + paragraph = Second paragraph.
 * -------------------------------+--------------------------------------------
 *                                
 * Examples C: - first section contains two sections of the second level
 * -------------------------------+--------------------------------------------
 * Source                         |  Result
 * -------------------------------+--------------------------------------------
 *  = First Header =              |+ section0
 *  First paragraph.              |  + document 
 *  == Second Header ==           |  + sectionContent0
 *  Second paragraph.             |    + section1
 *  == Third Header ==            |      + header1 = First Header
 *  Third paragraph               |      + sectionContent1
 *                                |        + paragraph = First paragraph.
 *                                |        + section2
 *                                |          + header2 = Second Header
 *                                |          + sectionContent2
 *                                |            + paragraph = Second paragraph.
 *                                |        + section2
 *                                |          + header2 = Third Header
 *                                |          + sectionContent2
 *                                |            + paragraph = Third paragraph.
 * -------------------------------+--------------------------------------------
 * 
 * Examples D: - Two sections of the first level
 * -------------------------------+--------------------------------------------
 * Source                         |  Result
 * -------------------------------+--------------------------------------------
 *  = First Header =              |+ section0
 *  First paragraph.              |  + document 
 *  == Second Header ==           |  + sectionContent0
 *  Second paragraph.             |    + section1
 *  == Third Header ==            |      + header1 = First Header
 *  Third paragraph               |      + sectionContent1
 *  = Fourth Header =             |        + paragraph = First paragraph.
 *  Fourth paragr                 |        + section2
 *                                |          + header2 = Second Header
 *                                |          + sectionContent2
 *                                |            + paragraph = Second paragraph.
 *                                |        + section2
 *                                |          + header2 = Third Header
 *                                |          + sectionContent2
 *                                |            + paragraph = Third paragraph.
 *                                |    + section1
 *                                |      + header1 = Fourth Header
 *                                |      + sectionContent1
 *                                |        + paragraph = Fourth paragraph.
 * -------------------------------+--------------------------------------------
 * </pre>
 * 
 * @author kotelnikov
 */
public interface IWemListenerDocument {
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
     * @param headerLevel the level of the found header; valid values: 1-6
     * @param params wiki parameters associated with the
     */
    void beginHeader(int headerLevel, WikiParameters params);

    /**
     * This method is used to notify about the beginning of a section. Document
     * sections delimits a set of structural elements at the same "level". A new
     * level starts with a new document or a new header.
     * 
     * @param docLevel the level (depth) of the document containing this section
     * @param headerLevel the level of the header defining this section
     * @param params parameters parameters of the document or the header
     */
    void beginSection(int docLevel, int headerLevel, WikiParameters params);

    /**
     * This method is used to notify about the beginning of a section. Document
     * sections delimits a set of structural elements at the same "level". A new
     * level starts with a new document or a new header.
     * 
     * @param docLevel the level (depth) of the document containing this section
     * @param headerLevel the level of the header defining this section
     * @param params parameters parameters of the document or the header
     */
    void beginSectionContent(
        int docLevel,
        int headerLevel,
        WikiParameters params);

    /**
     * This method is used to notify about the end of a top-level or an internal
     * document.
     */
    void endDocument(WikiParameters params);

    /**
     * This method is called to notify about the end of a section-level header.
     * 
     * @param headerLevel the level of the header
     * @param params wiki parameters of the header
     */
    void endHeader(int headerLevel, WikiParameters params);

    /**
     * This method is used to notify about the end of a document section.
     * 
     * @param docLevel the level (depth) of the document containing this section
     * @param headerLevel the level of the header defining this section
     * @param params parameters parameters of the document or the header
     */
    void endSection(int docLevel, int headerLevel, WikiParameters params);

    /**
     * This method is used to notify about the end of a document section.
     * 
     * @param docLevel the level (depth) of the document containing this section
     * @param headerLevel the level of the header defining this section
     * @param params parameters parameters of the document or the header
     */
    void endSectionContent(int docLevel, int headerLevel, WikiParameters params);

}
