package org.wikimodel.wem;

/**
 * A composite listener which delegates each listener method call to multiple
 * listeners registered in this composite listener.
 * 
 * @author MikhailKotelnikov
 */
public class CompositeListener implements IWemListener {

    /**
     * An internal list of listeners to which each method call will be
     * delegated.
     */
    private IWemListener[] fListeners;

    /**
     * @param listeners an array of listeners to which all method calls will be
     *        delegated
     */
    public CompositeListener(IWemListener[] listeners) {
        fListeners = listeners;
    }

    /**
     * @see org.wikimodel.wem.IWemListener#beginDefinitionDescription()
     */
    public void beginDefinitionDescription() {
        for (int i = 0; i < fListeners.length; i++) {
            fListeners[i].beginDefinitionDescription();
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListener#beginDefinitionList(org.wikimodel.wem.WikiParameters)
     */
    public void beginDefinitionList(WikiParameters params) {
        for (int i = 0; i < fListeners.length; i++) {
            fListeners[i].beginDefinitionList(params);
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListener#beginDefinitionTerm()
     */
    public void beginDefinitionTerm() {
        for (int i = 0; i < fListeners.length; i++) {
            fListeners[i].beginDefinitionTerm();
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListener#beginDocument()
     */
    public void beginDocument() {
        for (int i = 0; i < fListeners.length; i++) {
            fListeners[i].beginDocument();
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListener#beginFormat(org.wikimodel.wem.WikiFormat)
     */
    public void beginFormat(WikiFormat format) {
        for (int i = 0; i < fListeners.length; i++) {
            fListeners[i].beginFormat(format);
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListener#beginHeader(int, WikiParameters)
     */
    public void beginHeader(int level, WikiParameters params) {
        for (int i = 0; i < fListeners.length; i++) {
            fListeners[i].beginHeader(level, params);
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListener#beginInfoBlock(char,
     *      org.wikimodel.wem.WikiParameters)
     */
    public void beginInfoBlock(char infoType, WikiParameters params) {
        for (int i = 0; i < fListeners.length; i++) {
            fListeners[i].beginInfoBlock(infoType, params);
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListener#beginList(org.wikimodel.wem.WikiParameters,
     *      boolean)
     */
    public void beginList(WikiParameters params, boolean ordered) {
        for (int i = 0; i < fListeners.length; i++) {
            fListeners[i].beginList(params, ordered);
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListener#beginListItem()
     */
    public void beginListItem() {
        for (int i = 0; i < fListeners.length; i++) {
            fListeners[i].beginListItem();
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListener#beginParagraph(org.wikimodel.wem.WikiParameters)
     */
    public void beginParagraph(WikiParameters params) {
        for (int i = 0; i < fListeners.length; i++) {
            fListeners[i].beginParagraph(params);
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListener#beginPropertyBlock(java.lang.String,
     *      boolean)
     */
    public void beginPropertyBlock(String propertyUri, boolean doc) {
        for (int i = 0; i < fListeners.length; i++) {
            fListeners[i].beginPropertyBlock(propertyUri, doc);
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListener#beginPropertyInline(java.lang.String)
     */
    public void beginPropertyInline(String str) {
        for (int i = 0; i < fListeners.length; i++) {
            fListeners[i].beginPropertyInline(str);
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListener#beginQuotation(org.wikimodel.wem.WikiParameters)
     */
    public void beginQuotation(WikiParameters params) {
        for (int i = 0; i < fListeners.length; i++) {
            fListeners[i].beginQuotation(params);
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListener#beginQuotationLine()
     */
    public void beginQuotationLine() {
        for (int i = 0; i < fListeners.length; i++) {
            fListeners[i].beginQuotationLine();
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListener#beginTable(org.wikimodel.wem.WikiParameters)
     */
    public void beginTable(WikiParameters params) {
        for (int i = 0; i < fListeners.length; i++) {
            fListeners[i].beginTable(params);
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListener#beginTableCell(boolean,
     *      WikiParameters)
     */
    public void beginTableCell(boolean tableHead, WikiParameters params) {
        for (int i = 0; i < fListeners.length; i++) {
            fListeners[i].beginTableCell(tableHead, params);
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListener#beginTableRow(org.wikimodel.wem.WikiParameters)
     */
    public void beginTableRow(WikiParameters params) {
        for (int i = 0; i < fListeners.length; i++) {
            fListeners[i].beginTableRow(params);
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListener#endDefinitionDescription()
     */
    public void endDefinitionDescription() {
        for (int i = 0; i < fListeners.length; i++) {
            fListeners[i].endDefinitionDescription();
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListener#endDefinitionList(org.wikimodel.wem.WikiParameters)
     */
    public void endDefinitionList(WikiParameters params) {
        for (int i = 0; i < fListeners.length; i++) {
            fListeners[i].endDefinitionList(params);
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListener#endDefinitionTerm()
     */
    public void endDefinitionTerm() {
        for (int i = 0; i < fListeners.length; i++) {
            fListeners[i].endDefinitionTerm();
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListener#endDocument()
     */
    public void endDocument() {
        for (int i = 0; i < fListeners.length; i++) {
            fListeners[i].endDocument();
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListener#endFormat(org.wikimodel.wem.WikiFormat)
     */
    public void endFormat(WikiFormat format) {
        for (int i = 0; i < fListeners.length; i++) {
            fListeners[i].endFormat(format);
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListener#endHeader(int, WikiParameters)
     */
    public void endHeader(int level, WikiParameters params) {
        for (int i = 0; i < fListeners.length; i++) {
            fListeners[i].endHeader(level, params);
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListener#endInfoBlock(char,
     *      org.wikimodel.wem.WikiParameters)
     */
    public void endInfoBlock(char infoType, WikiParameters params) {
        for (int i = 0; i < fListeners.length; i++) {
            fListeners[i].endInfoBlock(infoType, params);
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListener#endList(org.wikimodel.wem.WikiParameters,
     *      boolean)
     */
    public void endList(WikiParameters params, boolean ordered) {
        for (int i = 0; i < fListeners.length; i++) {
            fListeners[i].endList(params, ordered);
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListener#endListItem()
     */
    public void endListItem() {
        for (int i = 0; i < fListeners.length; i++) {
            fListeners[i].endListItem();
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListener#endParagraph(org.wikimodel.wem.WikiParameters)
     */
    public void endParagraph(WikiParameters params) {
        for (int i = 0; i < fListeners.length; i++) {
            fListeners[i].endParagraph(params);
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListener#endPropertyBlock(java.lang.String,
     *      boolean)
     */
    public void endPropertyBlock(String propertyUri, boolean doc) {
        for (int i = 0; i < fListeners.length; i++) {
            fListeners[i].endPropertyBlock(propertyUri, doc);
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListener#endPropertyInline(java.lang.String)
     */
    public void endPropertyInline(String inlineProperty) {
        for (int i = 0; i < fListeners.length; i++) {
            fListeners[i].endPropertyInline(inlineProperty);
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListener#endQuotation(org.wikimodel.wem.WikiParameters)
     */
    public void endQuotation(WikiParameters params) {
        for (int i = 0; i < fListeners.length; i++) {
            fListeners[i].endQuotation(params);
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListener#endQuotationLine()
     */
    public void endQuotationLine() {
        for (int i = 0; i < fListeners.length; i++) {
            fListeners[i].endQuotationLine();
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListener#endTable(org.wikimodel.wem.WikiParameters)
     */
    public void endTable(WikiParameters params) {
        for (int i = 0; i < fListeners.length; i++) {
            fListeners[i].endTable(params);
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListener#endTableCell(boolean, WikiParameters)
     */
    public void endTableCell(boolean tableHead, WikiParameters params) {
        for (int i = 0; i < fListeners.length; i++) {
            fListeners[i].endTableCell(tableHead, params);
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListener#endTableRow(org.wikimodel.wem.WikiParameters)
     */
    public void endTableRow(WikiParameters params) {
        for (int i = 0; i < fListeners.length; i++) {
            fListeners[i].endTableRow(params);
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListener#onEscape(java.lang.String)
     */
    public void onEscape(String str) {
        for (int i = 0; i < fListeners.length; i++) {
            fListeners[i].onEscape(str);
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListener#onExtensionBlock(java.lang.String,
     *      org.wikimodel.wem.WikiParameters)
     */
    public void onExtensionBlock(String extensionName, WikiParameters params) {
        for (int i = 0; i < fListeners.length; i++) {
            fListeners[i].onExtensionBlock(extensionName, params);
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListener#onExtensionInline(java.lang.String,
     *      org.wikimodel.wem.WikiParameters)
     */
    public void onExtensionInline(String extensionName, WikiParameters params) {
        for (int i = 0; i < fListeners.length; i++) {
            fListeners[i].onExtensionInline(extensionName, params);
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListener#onHorizontalLine()
     */
    public void onHorizontalLine() {
        for (int i = 0; i < fListeners.length; i++) {
            fListeners[i].onHorizontalLine();
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListener#onLineBreak()
     */
    public void onLineBreak() {
        for (int i = 0; i < fListeners.length; i++) {
            fListeners[i].onLineBreak();
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListener#onNewLine()
     */
    public void onNewLine() {
        for (int i = 0; i < fListeners.length; i++) {
            fListeners[i].onNewLine();
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListener#onReference(java.lang.String)
     */
    public void onReference(String ref) {
        for (int i = 0; i < fListeners.length; i++) {
            fListeners[i].onReference(ref);
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListener#onSpace(java.lang.String)
     */
    public void onSpace(String str) {
        for (int i = 0; i < fListeners.length; i++) {
            fListeners[i].onSpace(str);
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListener#onSpecialSymbol(java.lang.String)
     */
    public void onSpecialSymbol(String str) {
        for (int i = 0; i < fListeners.length; i++) {
            fListeners[i].onSpecialSymbol(str);
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListener#onTableCaption(java.lang.String)
     */
    public void onTableCaption(String str) {
        for (int i = 0; i < fListeners.length; i++) {
            fListeners[i].onTableCaption(str);
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListener#onVerbatimBlock(java.lang.String)
     */
    public void onVerbatimBlock(String str) {
        for (int i = 0; i < fListeners.length; i++) {
            fListeners[i].onVerbatimBlock(str);
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListener#onVerbatimInline(java.lang.String)
     */
    public void onVerbatimInline(String str) {
        for (int i = 0; i < fListeners.length; i++) {
            fListeners[i].onVerbatimInline(str);
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListener#onWord(java.lang.String)
     */
    public void onWord(String str) {
        for (int i = 0; i < fListeners.length; i++) {
            fListeners[i].onWord(str);
        }
    }

    public void onMacroBlock(
        String macroName,
        WikiParameters params,
        String content) {
        for (int i = 0; i < fListeners.length; i++) {
            fListeners[i].onMacroBlock(macroName, params, content);
        }
    }

    public void onMacroInline(
        String macroName,
        WikiParameters params,
        String content) {
        for (int i = 0; i < fListeners.length; i++) {
            fListeners[i].onMacroInline(macroName, params, content);
        }
    }

    public void onEmptyLines(int count) {
        for (int i = 0; i < fListeners.length; i++) {
            fListeners[i].onEmptyLines(count);
        }
    }

}
