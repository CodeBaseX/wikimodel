/**
 * 
 */
package org.wikimodel.wem.xml;

import java.util.Map;

import org.wikimodel.wem.IWemListenerDocument;
import org.wikimodel.wem.WikiParameters;

/**
 * @author kotelnikov
 */
public class WemDocumentTagNotifier extends AbstractTagNotifier
    implements
    IWemListenerDocument {

    private int fSectionDepth;

    /**
     * @param listener
     */
    public WemDocumentTagNotifier(ITagListener listener) {
        super(listener);
    }

    public void beginDocument(WikiParameters params) {
        fListener.beginTag(DOCUMENT, tagParams(), userParams(params));
    }

    public void beginHeader(int headerLevel, WikiParameters params) {
        fListener.beginTag(
            HEADER,
            tagParams(HEADER_LEVEL, "" + headerLevel),
            userParams(params));
    }

    public void beginSection(
        int docLevel,
        int headerLevel,
        WikiParameters params) {
        fSectionDepth++;
        fListener.beginTag(
            SECTION,
            getSectionParams(docLevel, headerLevel),
            userParams(params));
    }

    public void beginSectionContent(
        int docLevel,
        int headerLevel,
        WikiParameters params) {
        fListener.beginTag(SECTION_CONTENT, getSectionParams(
            docLevel,
            headerLevel), userParams(params));
    }

    public void endDocument(WikiParameters params) {
        fListener.endTag(DOCUMENT, tagParams(), userParams(params));
    }

    public void endHeader(int headerLevel, WikiParameters params) {
        fListener.endTag(
            HEADER,
            tagParams(HEADER_LEVEL, "" + headerLevel),
            userParams(params));
    }

    public void endSection(int docLevel, int headerLevel, WikiParameters params) {
        fListener.endTag(
            SECTION,
            getSectionParams(docLevel, headerLevel),
            userParams(params));
        fSectionDepth--;
    }

    public void endSectionContent(
        int docLevel,
        int headerLevel,
        WikiParameters params) {
        fListener.endTag(SECTION_CONTENT, getSectionParams(
            docLevel,
            headerLevel), userParams(params));
    }

    /**
     * @param docLevel
     * @param headerLevel
     * @return
     */
    private Map<String, String> getSectionParams(int docLevel, int headerLevel) {
        return tagParams(
            SECTION_LEVEL,
            "" + fSectionDepth,
            SECTION_DOC_LEVEL,
            "" + docLevel,
            SECTION_HEADER_LEVEL,
            "" + headerLevel);
    }

}
