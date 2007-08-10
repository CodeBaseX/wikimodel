package org.wikimodel.wem;

/**
 * @author MikhailKotelnikov
 */
public class PrintInlineListener implements IWemListener {

    private StringBuffer fBuffer;

    /**
     * 
     */
    public PrintInlineListener() {
    }

    /**
     * 
     */
    public PrintInlineListener(StringBuffer buf) {
        fBuffer = buf;
    }

    /**
     * @see org.wikimodel.wem.IWemListener#beginDefinitionDescription()
     */
    public void beginDefinitionDescription() {
    }

    /**
     * @see org.wikimodel.wem.IWemListener#beginDefinitionList(org.wikimodel.wem.WikiParameters)
     */
    public void beginDefinitionList(WikiParameters params) {
    }

    /**
     * @see org.wikimodel.wem.IWemListener#beginDefinitionTerm()
     */
    public void beginDefinitionTerm() {
    }

    /**
     * @see org.wikimodel.wem.IWemListener#beginDocument()
     */
    public void beginDocument() {
    }

    /**
     * @see org.wikimodel.wem.IWemListener#beginFormat(org.wikimodel.wem.WikiFormat)
     */
    public void beginFormat(WikiFormat format) {
        print(format.getTags(true));
    }

    /**
     * @see org.wikimodel.wem.IWemListener#beginHeader(int, WikiParameters)
     */
    public void beginHeader(int level, WikiParameters params) {
    }

    /**
     * @see org.wikimodel.wem.IWemListener#beginInfoBlock(char,
     *      org.wikimodel.wem.WikiParameters)
     */
    public void beginInfoBlock(char infoType, WikiParameters params) {
        //
    }

    /**
     * @see org.wikimodel.wem.IWemListener#beginList(org.wikimodel.wem.WikiParameters,
     *      boolean)
     */
    public void beginList(WikiParameters params, boolean ordered) {
    }

    /**
     * @see org.wikimodel.wem.IWemListener#beginListItem()
     */
    public void beginListItem() {
    }

    /**
     * @see org.wikimodel.wem.IWemListener#beginParagraph(org.wikimodel.wem.WikiParameters)
     */
    public void beginParagraph(WikiParameters params) {
    }

    /**
     * @see org.wikimodel.wem.IWemListener#beginPropertyBlock(java.lang.String,
     *      boolean)
     */
    public void beginPropertyBlock(String propertyUri, boolean doc) {
    }

    /**
     * @see org.wikimodel.wem.IWemListener#beginPropertyInline(java.lang.String)
     */
    public void beginPropertyInline(String str) {
        print("<span class='property' url='"
            + WikiPageUtil.escapeXmlAttribute(str)
            + "'>");
    }

    /**
     * @see org.wikimodel.wem.IWemListener#beginQuotation(org.wikimodel.wem.WikiParameters)
     */
    public void beginQuotation(WikiParameters params) {
    }

    /**
     * @see org.wikimodel.wem.IWemListener#beginQuotationLine()
     */
    public void beginQuotationLine() {
        //
    }

    /**
     * @see org.wikimodel.wem.IWemListener#beginTable(org.wikimodel.wem.WikiParameters)
     */
    public void beginTable(WikiParameters params) {
    }

    /**
     * @see org.wikimodel.wem.IWemListener#beginTableCell(boolean,
     *      WikiParameters)
     */
    public void beginTableCell(boolean tableHead, WikiParameters params) {
    }

    /**
     * @see org.wikimodel.wem.IWemListener#beginTableRow(org.wikimodel.wem.WikiParameters)
     */
    public void beginTableRow(WikiParameters params) {
    }

    /**
     * @see org.wikimodel.wem.IWemListener#endDefinitionDescription()
     */
    public void endDefinitionDescription() {
    }

    /**
     * @see org.wikimodel.wem.IWemListener#endDefinitionList(org.wikimodel.wem.WikiParameters)
     */
    public void endDefinitionList(WikiParameters params) {
    }

    /**
     * @see org.wikimodel.wem.IWemListener#endDefinitionTerm()
     */
    public void endDefinitionTerm() {
    }

    /**
     * @see org.wikimodel.wem.IWemListener#endDocument()
     */
    public void endDocument() {
    }

    /**
     * @see org.wikimodel.wem.IWemListener#endFormat(org.wikimodel.wem.WikiFormat)
     */
    public void endFormat(WikiFormat format) {
        print(format.getTags(false));
    }

    /**
     * @see org.wikimodel.wem.IWemListener#endHeader(int, WikiParameters)
     */
    public void endHeader(int level, WikiParameters params) {
    }

    /**
     * @see org.wikimodel.wem.IWemListener#endInfoBlock(char,
     *      org.wikimodel.wem.WikiParameters)
     */
    public void endInfoBlock(char infoType, WikiParameters params) {
        //
    }

    /**
     * @see org.wikimodel.wem.IWemListener#endList(org.wikimodel.wem.WikiParameters,
     *      boolean)
     */
    public void endList(WikiParameters params, boolean ordered) {
    }

    /**
     * @see org.wikimodel.wem.IWemListener#endListItem()
     */
    public void endListItem() {
    }

    /**
     * @see org.wikimodel.wem.IWemListener#endParagraph(org.wikimodel.wem.WikiParameters)
     */
    public void endParagraph(WikiParameters params) {
    }

    /**
     * @see org.wikimodel.wem.IWemListener#endPropertyBlock(java.lang.String,
     *      boolean)
     */
    public void endPropertyBlock(String propertyUri, boolean doc) {
    }

    /**
     * @see org.wikimodel.wem.IWemListener#endPropertyInline(java.lang.String)
     */
    public void endPropertyInline(String inlineProperty) {
        print("</span>");
    }

    /**
     * @see org.wikimodel.wem.IWemListener#endQuotation(org.wikimodel.wem.WikiParameters)
     */
    public void endQuotation(WikiParameters params) {
    }

    /**
     * @see org.wikimodel.wem.IWemListener#endQuotationLine()
     */
    public void endQuotationLine() {
        //
    }

    /**
     * @see org.wikimodel.wem.IWemListener#endTable(org.wikimodel.wem.WikiParameters)
     */
    public void endTable(WikiParameters params) {
    }

    /**
     * @see org.wikimodel.wem.IWemListener#endTableCell(boolean, WikiParameters)
     */
    public void endTableCell(boolean tableHead, WikiParameters params) {
    }

    /**
     * @see org.wikimodel.wem.IWemListener#endTableRow(org.wikimodel.wem.WikiParameters)
     */
    public void endTableRow(WikiParameters params) {
    }

    /**
     * @see org.wikimodel.wem.IWemListener#onEscape(java.lang.String)
     */
    public void onEscape(String str) {
        print("<span class='escaped'>"
            + WikiPageUtil.escapeXmlString(str)
            + "</span>");
    }

    public void onExtensionBlock(String extensionName, WikiParameters params) {
        //
    }

    public void onExtensionInline(String extensionName, WikiParameters params) {
        print("<span class='extension' extension='"
            + extensionName
            + "' "
            + params
            + " />");
    }

    /**
     * @see org.wikimodel.wem.IWemListener#onHorizontalLine()
     */
    public void onHorizontalLine() {
    }

    /**
     * @see org.wikimodel.wem.IWemListener#onLineBreak()
     */

    public void onLineBreak() {
        print("<br />");
    }

    /**
     * @see org.wikimodel.wem.IWemListener#onNewLine()
     */
    public void onNewLine() {
        println("");
    }

    /**
     * @see org.wikimodel.wem.IWemListener#onReference(java.lang.String)
     */

    public void onReference(String ref) {
        print("<a href='"
            + WikiPageUtil.escapeXmlAttribute(ref)
            + "'>"
            + WikiPageUtil.escapeXmlString(ref)
            + "</a>");
    }

    /**
     * @see org.wikimodel.wem.IWemListener#onSpace(java.lang.String)
     */
    public void onSpace(String str) {
        print(str);
    }

    /**
     * @see org.wikimodel.wem.IWemListener#onSpecialSymbol(java.lang.String)
     */
    public void onSpecialSymbol(String str) {
        print(str);
    }

    /**
     * @see org.wikimodel.wem.IWemListener#onTableCaption(java.lang.String)
     */
    public void onTableCaption(String str) {
    }

    /**
     * @see org.wikimodel.wem.IWemListener#onVerbatimBlock(java.lang.String)
     */
    public void onVerbatimBlock(String str) {
    }

    /**
     * @see org.wikimodel.wem.IWemListener#onVerbatimInline(java.lang.String)
     */
    public void onVerbatimInline(String str) {
        print("<code>" + WikiPageUtil.escapeXmlString(str) + "</code>");
    }

    /**
     * @see org.wikimodel.wem.IWemListener#onWord(java.lang.String)
     */

    public void onWord(String str) {
        print(str);
    }

    protected void print(String str) {
        if (fBuffer != null) {
            fBuffer.append(str);
        } else {
            System.out.print(str);
        }
    }

    protected void println(String str) {
        if (fBuffer != null) {
            fBuffer.append(str);
            fBuffer.append("\n");
        } else {
            System.out.println(str);
        }
    }

}
