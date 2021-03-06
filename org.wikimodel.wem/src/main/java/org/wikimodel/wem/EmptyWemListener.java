/**
 * 
 */
package org.wikimodel.wem;

/**
 * @author kotelnikov
 */
public class EmptyWemListener implements IWemListener {

    /**
     * 
     */
    public EmptyWemListener() {
        //
    }

    /**
     * @see org.wikimodel.wem.IWemListener#beginDefinitionDescription()
     */
    public void beginDefinitionDescription() {
        //
    }

    /**
     * @see org.wikimodel.wem.IWemListener#beginDefinitionList(org.wikimodel.wem.WikiParameters)
     */
    public void beginDefinitionList(WikiParameters params) {
        //
    }

    /**
     * @see org.wikimodel.wem.IWemListener#beginDefinitionTerm()
     */
    public void beginDefinitionTerm() {
        //
    }

    /**
     * @see org.wikimodel.wem.IWemListener#beginDocument()
     */
    public void beginDocument() {
        //
    }

    /**
     * @see org.wikimodel.wem.IWemListenerDocument#beginDocument(org.wikimodel.wem.WikiParameters)
     */
    public void beginDocument(WikiParameters params) {
        beginDocument();
    }

    /**
     * @see org.wikimodel.wem.IWemListener#beginFormat(org.wikimodel.wem.WikiFormat)
     */
    public void beginFormat(WikiFormat format) {
        //
    }

    /**
     * @see org.wikimodel.wem.IWemListener#beginHeader(int,
     *      org.wikimodel.wem.WikiParameters)
     */
    public void beginHeader(int headerLevel, WikiParameters params) {
        //
    }

    /**
     * @see org.wikimodel.wem.IWemListener#beginInfoBlock(String,
     *      org.wikimodel.wem.WikiParameters)
     */
    public void beginInfoBlock(String infoType, WikiParameters params) {
        //
    }

    /**
     * @see org.wikimodel.wem.IWemListener#beginList(org.wikimodel.wem.WikiParameters,
     *      boolean)
     */
    public void beginList(WikiParameters params, boolean ordered) {
        //
    }

    /**
     * @see org.wikimodel.wem.IWemListener#beginListItem()
     */
    public void beginListItem() {
        //
    }

    /**
     * @see org.wikimodel.wem.IWemListener#beginParagraph(org.wikimodel.wem.WikiParameters)
     */
    public void beginParagraph(WikiParameters params) {
        //
    }

    /**
     * @see org.wikimodel.wem.IWemListener#beginPropertyBlock(java.lang.String,
     *      boolean)
     */
    public void beginPropertyBlock(String propertyUri, boolean doc) {
        //
    }

    /**
     * @see org.wikimodel.wem.IWemListener#beginPropertyInline(java.lang.String)
     */
    public void beginPropertyInline(String str) {
        //
    }

    /**
     * @see org.wikimodel.wem.IWemListener#beginQuotation(org.wikimodel.wem.WikiParameters)
     */
    public void beginQuotation(WikiParameters params) {
        //
    }

    /**
     * @see org.wikimodel.wem.IWemListener#beginQuotationLine()
     */
    public void beginQuotationLine() {
        //
    }

    /**
     * @see org.wikimodel.wem.IWemListenerDocument#beginSection(int, int,
     *      WikiParameters)
     */
    public void beginSection(
        int docLevel,
        int headerLevel,
        WikiParameters params) {
        // 
    }

    /**
     * @see org.wikimodel.wem.IWemListenerDocument#beginSectionContent(int, int,
     *      WikiParameters)
     */
    public void beginSectionContent(
        int docLevel,
        int headerLevel,
        WikiParameters params) {
        // 
    }

    /**
     * @see org.wikimodel.wem.IWemListener#beginTable(org.wikimodel.wem.WikiParameters)
     */
    public void beginTable(WikiParameters params) {
        //
    }

    /**
     * @see org.wikimodel.wem.IWemListener#beginTableCell(boolean,
     *      org.wikimodel.wem.WikiParameters)
     */
    public void beginTableCell(boolean tableHead, WikiParameters params) {
        //
    }

    /**
     * @see org.wikimodel.wem.IWemListener#beginTableRow(org.wikimodel.wem.WikiParameters)
     */
    public void beginTableRow(WikiParameters params) {
        //
    }

    /**
     * @see org.wikimodel.wem.IWemListener#endDefinitionDescription()
     */
    public void endDefinitionDescription() {
        //
    }

    /**
     * @see org.wikimodel.wem.IWemListener#endDefinitionList(org.wikimodel.wem.WikiParameters)
     */
    public void endDefinitionList(WikiParameters params) {
        //
    }

    /**
     * @see org.wikimodel.wem.IWemListener#endDefinitionTerm()
     */
    public void endDefinitionTerm() {
        //
    }

    /**
     * @see org.wikimodel.wem.IWemListener#endDocument()
     */
    public void endDocument() {
        //
    }

    /**
     * @see org.wikimodel.wem.IWemListenerDocument#endDocument(org.wikimodel.wem.WikiParameters)
     */
    public void endDocument(WikiParameters params) {
        endDocument();
    }

    /**
     * @see org.wikimodel.wem.IWemListener#endFormat(org.wikimodel.wem.WikiFormat)
     */
    public void endFormat(WikiFormat format) {
        //
    }

    /**
     * @see org.wikimodel.wem.IWemListener#endHeader(int,
     *      org.wikimodel.wem.WikiParameters)
     */
    public void endHeader(int headerLevel, WikiParameters params) {
        //
    }

    /**
     * @see org.wikimodel.wem.IWemListener#endInfoBlock(String,
     *      org.wikimodel.wem.WikiParameters)
     */
    public void endInfoBlock(String infoType, WikiParameters params) {
        //
    }

    /**
     * @see org.wikimodel.wem.IWemListener#endList(org.wikimodel.wem.WikiParameters,
     *      boolean)
     */
    public void endList(WikiParameters params, boolean ordered) {
        //
    }

    /**
     * @see org.wikimodel.wem.IWemListener#endListItem()
     */
    public void endListItem() {
        //
    }

    /**
     * @see org.wikimodel.wem.IWemListener#endParagraph(org.wikimodel.wem.WikiParameters)
     */
    public void endParagraph(WikiParameters params) {
        //
    }

    /**
     * @see org.wikimodel.wem.IWemListener#endPropertyBlock(java.lang.String,
     *      boolean)
     */
    public void endPropertyBlock(String propertyUri, boolean doc) {
        //
    }

    /**
     * @see org.wikimodel.wem.IWemListener#endPropertyInline(java.lang.String)
     */
    public void endPropertyInline(String inlineProperty) {
        //
    }

    /**
     * @see org.wikimodel.wem.IWemListener#endQuotation(org.wikimodel.wem.WikiParameters)
     */
    public void endQuotation(WikiParameters params) {
        //
    }

    /**
     * @see org.wikimodel.wem.IWemListener#endQuotationLine()
     */
    public void endQuotationLine() {
        //
    }

    /**
     * @see org.wikimodel.wem.IWemListenerDocument#endSection(int, int,
     *      WikiParameters)
     */
    public void endSection(int docLevel, int headerLevel, WikiParameters params) {
        // 
    }

    /**
     * @see org.wikimodel.wem.IWemListenerDocument#endSectionContent(int, int,
     *      WikiParameters)
     */
    public void endSectionContent(
        int docLevel,
        int headerLevel,
        WikiParameters params) {
        // 
    }

    /**
     * @see org.wikimodel.wem.IWemListener#endTable(org.wikimodel.wem.WikiParameters)
     */
    public void endTable(WikiParameters params) {
        //
    }

    /**
     * @see org.wikimodel.wem.IWemListener#endTableCell(boolean,
     *      org.wikimodel.wem.WikiParameters)
     */
    public void endTableCell(boolean tableHead, WikiParameters params) {
        //
    }

    /**
     * @see org.wikimodel.wem.IWemListener#endTableRow(org.wikimodel.wem.WikiParameters)
     */
    public void endTableRow(WikiParameters params) {
        //
    }

    /**
     * @see org.wikimodel.wem.IWemListener#onEmptyLines(int)
     */
    public void onEmptyLines(int count) {
        //
    }

    /**
     * @see org.wikimodel.wem.IWemListener#onEscape(java.lang.String)
     */
    public void onEscape(String str) {
        //
    }

    /**
     * @see org.wikimodel.wem.IWemListener#onExtensionBlock(java.lang.String,
     *      org.wikimodel.wem.WikiParameters)
     */
    public void onExtensionBlock(String extensionName, WikiParameters params) {
        //
    }

    /**
     * @see org.wikimodel.wem.IWemListener#onExtensionInline(java.lang.String,
     *      org.wikimodel.wem.WikiParameters)
     */
    public void onExtensionInline(String extensionName, WikiParameters params) {
        //
    }

    /**
     * @see org.wikimodel.wem.IWemListener#onHorizontalLine(WikiParameters
     *      params)
     */
    public void onHorizontalLine(WikiParameters params) {
        //
    }

    /**
     * @see org.wikimodel.wem.IWemListenerInline#onImage(java.lang.String)
     */
    public void onImage(String ref) {
        //   
    }

    /**
     * @see org.wikimodel.wem.IWemListenerInline#onImage(org.wikimodel.wem.WikiReference)
     */
    public void onImage(WikiReference ref) {
        // 
    }

    /**
     * @see org.wikimodel.wem.IWemListener#onLineBreak()
     */
    public void onLineBreak() {
        //
    }

    /**
     * @see org.wikimodel.wem.IWemListener#onMacroBlock(java.lang.String,
     *      org.wikimodel.wem.WikiParameters, java.lang.String)
     */
    public void onMacroBlock(
        String macroName,
        WikiParameters params,
        String content) {
        //
    }

    /**
     * @see org.wikimodel.wem.IWemListener#onMacroInline(java.lang.String,
     *      org.wikimodel.wem.WikiParameters, java.lang.String)
     */
    public void onMacroInline(
        String macroName,
        WikiParameters params,
        String content) {
        //
    }

    /**
     * @see org.wikimodel.wem.IWemListener#onNewLine()
     */
    public void onNewLine() {
        //
    }

    /**
     * @see org.wikimodel.wem.IWemListener#onReference(java.lang.String)
     */
    public void onReference(String ref) {
        //
    }

    public void onReference(WikiReference ref) {
        // 
    }

    /**
     * @see org.wikimodel.wem.IWemListener#onSpace(java.lang.String)
     */
    public void onSpace(String str) {
        //
    }

    /**
     * @see org.wikimodel.wem.IWemListener#onSpecialSymbol(java.lang.String)
     */
    public void onSpecialSymbol(String str) {
        //
    }

    /**
     * @see org.wikimodel.wem.IWemListener#onTableCaption(java.lang.String)
     */
    public void onTableCaption(String str) {
        //
    }

    /**
     * @see org.wikimodel.wem.IWemListener#onVerbatimBlock(String,
     *      WikiParameters)
     */
    public void onVerbatimBlock(String str, WikiParameters params) {
        //
    }

    /**
     * @see org.wikimodel.wem.IWemListener#onVerbatimInline(java.lang.String,
     *      WikiParameters)
     */
    public void onVerbatimInline(String str, WikiParameters params) {
        //
    }

    /**
     * @see org.wikimodel.wem.IWemListener#onWord(java.lang.String)
     */
    public void onWord(String str) {
        //
    }

}
