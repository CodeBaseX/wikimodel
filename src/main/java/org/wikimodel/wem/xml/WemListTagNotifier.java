/**
 * 
 */
package org.wikimodel.wem.xml;

import org.wikimodel.wem.IWemListenerList;
import org.wikimodel.wem.WikiParameters;

/**
 * @author kotelnikov
 */
public class WemListTagNotifier extends AbstractTagNotifier
    implements
    IWemListenerList {

    /**
     * @param listener
     */
    public WemListTagNotifier(ITagListener listener) {
        super(listener);
    }

    public void beginDefinitionDescription() {
        fListener.beginTag(DEFINITION_DESCRIPTION, EMPTY_MAP, EMPTY_MAP);
    }

    public void beginDefinitionList(WikiParameters params) {
        fListener.beginTag(DEFINITION_LIST, EMPTY_MAP, EMPTY_MAP);
    }

    public void beginDefinitionTerm() {
        fListener.beginTag(DEFINITION_TERM, EMPTY_MAP, EMPTY_MAP);
    }

    public void beginList(WikiParameters params, boolean ordered) {
        String tagName = ordered ? LIST_ORDERED : LIST_UNORDERED;
        fListener.beginTag(tagName, EMPTY_MAP, userParams(params));
    }

    public void beginListItem() {
        fListener.beginTag(LIST_ITEM, EMPTY_MAP, EMPTY_MAP);
    }

    public void beginQuotation(WikiParameters params) {
        fListener.beginTag(QUOTATION, EMPTY_MAP, userParams(params));
    }

    public void beginQuotationLine() {
        fListener.beginTag(QUOTATION_LINE, EMPTY_MAP, EMPTY_MAP);
    }

    public void endDefinitionDescription() {
        fListener.endTag(DEFINITION_DESCRIPTION, EMPTY_MAP, EMPTY_MAP);
    }

    public void endDefinitionList(WikiParameters params) {
        fListener.endTag(DEFINITION_LIST, EMPTY_MAP, userParams(params));
    }

    public void endDefinitionTerm() {
        fListener.endTag(DEFINITION_TERM, EMPTY_MAP, EMPTY_MAP);
    }

    public void endList(WikiParameters params, boolean ordered) {
        String tagName = ordered ? LIST_ORDERED : LIST_UNORDERED;
        fListener.endTag(tagName, EMPTY_MAP, userParams(params));
    }

    public void endListItem() {
        fListener.endTag(LIST_ITEM, EMPTY_MAP, EMPTY_MAP);
    }

    public void endQuotation(WikiParameters params) {
        fListener.endTag(QUOTATION, EMPTY_MAP, userParams(params));
    }

    public void endQuotationLine() {
        fListener.endTag(QUOTATION_LINE, EMPTY_MAP, EMPTY_MAP);
    }

}
