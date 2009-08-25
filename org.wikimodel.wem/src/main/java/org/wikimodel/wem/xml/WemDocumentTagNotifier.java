/**
 * 
 */
package org.wikimodel.wem.xml;

import org.wikimodel.wem.IWemListenerDocument;
import org.wikimodel.wem.WikiParameters;

/**
 * @author kotelnikov
 */
public class WemDocumentTagNotifier extends AbstractTagNotifier
    implements
    IWemListenerDocument {

    /**
     * @param listener
     */
    public WemDocumentTagNotifier(ITagListener listener) {
        super(listener);
    }

    public void beginDocument(WikiParameters params) {
        fListener.beginTag(DOCUMENT, tagParams(), userParams(params));
    }

    public void beginHeader(int level, WikiParameters params) {
        fListener.beginTag(
            HEADER,
            tagParams(HEADER_LEVEL, "" + level),
            userParams(params));
    }

    public void beginSection(
        int docLevel,
        int sectionLevel,
        WikiParameters params) {
        fListener.beginTag(SECTION, tagParams(
            SECTION_DOC_LEVEL,
            "" + docLevel,
            SECTION_LEVEL,
            "" + sectionLevel), userParams(params));
    }

    public void beginSectionContent(
        int docLevel,
        int sectionLevel,
        WikiParameters params) {
        fListener.beginTag(SECTION_CONTENT, tagParams(SECTION_DOC_LEVEL, ""
            + docLevel, SECTION_LEVEL, "" + sectionLevel), userParams(params));
    }

    public void endDocument(WikiParameters params) {
        fListener.endTag(DOCUMENT, tagParams(), userParams(params));
    }

    public void endHeader(int level, WikiParameters params) {
        fListener.endTag(
            HEADER,
            tagParams(HEADER_LEVEL, "" + level),
            userParams(params));
    }

    public void endSection(int docLevel, int sectionLevel, WikiParameters params) {
        fListener.endTag(SECTION, tagParams(
            SECTION_DOC_LEVEL,
            "" + docLevel,
            SECTION_LEVEL,
            "" + sectionLevel), userParams(params));
    }

    public void endSectionContent(
        int docLevel,
        int sectionLevel,
        WikiParameters params) {
        fListener.endTag(SECTION_CONTENT, tagParams(SECTION_DOC_LEVEL, ""
            + docLevel, SECTION_LEVEL, "" + sectionLevel), userParams(params));
    }

}
