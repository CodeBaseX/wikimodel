package org.wikimodel.wem;

/**
 * @author MikhailKotelnikov
 * @author thomas.mortagne
 */
public class PrintTextListener implements IWemListener {

    private final IWikiPrinter fPrinter;

    protected ReferenceHandler fRefHandler;
    
    private boolean supportImage;

    private boolean supportDownload;

    /**
     * @param printer
     */
    public PrintTextListener(IWikiPrinter printer) {
        this(printer, false, false);
    }
    
    public PrintTextListener(IWikiPrinter printer, boolean supportImage, boolean supportDownload) {
        this.supportImage = supportImage;
        this.supportDownload = supportDownload;
        
        fPrinter = printer;
        fRefHandler = newReferenceHandler();
    }

    public boolean isSupportImage() {
        return supportImage;
    }
    
    public boolean isSupportDownload() {
        return supportDownload;
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
     * @see org.wikimodel.wem.IWemListenerDocument#beginDocument(org.wikimodel.wem.WikiParameters)
     */
    public void beginDocument(WikiParameters params) {
    }

    /**
     * @see org.wikimodel.wem.IWemListener#beginFormat(org.wikimodel.wem.WikiFormat)
     */
    public void beginFormat(WikiFormat format) {
    }

    /**
     * @see org.wikimodel.wem.IWemListener#beginHeader(int, WikiParameters)
     */
    public void beginHeader(int headerLevel, WikiParameters params) {
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
     * This method is called at the end of each block element. It can be
     * overloaded in subclasses.
     */
    protected void endBlock() {
        //
    }

    /**
     * @see org.wikimodel.wem.IWemListener#endDefinitionDescription()
     */
    public void endDefinitionDescription() {
        endBlock();
    }

    /**
     * @see org.wikimodel.wem.IWemListener#endDefinitionList(org.wikimodel.wem.WikiParameters)
     */
    public void endDefinitionList(WikiParameters params) {
        endBlock();
    }

    /**
     * @see org.wikimodel.wem.IWemListener#endDefinitionTerm()
     */
    public void endDefinitionTerm() {
    }

    /**
     * @see org.wikimodel.wem.IWemListenerDocument#endDocument(org.wikimodel.wem.WikiParameters)
     */
    public void endDocument(WikiParameters params) {
        endBlock();
    }

    /**
     * @see org.wikimodel.wem.IWemListener#endFormat(org.wikimodel.wem.WikiFormat)
     */
    public void endFormat(WikiFormat format) {
    }

    /**
     * @see org.wikimodel.wem.IWemListener#endHeader(int, WikiParameters)
     */
    public void endHeader(int headerLevel, WikiParameters params) {
        endBlock();
    }

    /**
     * @see org.wikimodel.wem.IWemListener#endInfoBlock(String,
     *      org.wikimodel.wem.WikiParameters)
     */
    public void endInfoBlock(String infoType, WikiParameters params) {
        endBlock();
    }

    /**
     * @see org.wikimodel.wem.IWemListener#endList(org.wikimodel.wem.WikiParameters,
     *      boolean)
     */
    public void endList(WikiParameters params, boolean ordered) {
        endBlock();
    }

    /**
     * @see org.wikimodel.wem.IWemListener#endListItem()
     */
    public void endListItem() {
        endBlock();
    }

    /**
     * @see org.wikimodel.wem.IWemListener#endParagraph(org.wikimodel.wem.WikiParameters)
     */
    public void endParagraph(WikiParameters params) {
        endBlock();
    }

    /**
     * @see org.wikimodel.wem.IWemListener#endPropertyBlock(java.lang.String,
     *      boolean)
     */
    public void endPropertyBlock(String propertyUri, boolean doc) {
        endBlock();
    }

    /**
     * @see org.wikimodel.wem.IWemListener#endPropertyInline(java.lang.String)
     */
    public void endPropertyInline(String inlineProperty) {
    }

    /**
     * @see org.wikimodel.wem.IWemListener#endQuotation(org.wikimodel.wem.WikiParameters)
     */
    public void endQuotation(WikiParameters params) {
        endBlock();
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
        endBlock();
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

    protected ReferenceHandler newReferenceHandler() {
        return new ReferenceHandler(supportImage, supportDownload) {

            @Override
            protected void handleImage(
                String ref,
                String label,
                WikiParameters params) {
                handleReference(ref, label, params);
            }

            @Override
            protected void handleReference(
                String ref,
                String label,
                WikiParameters params) {
                print(label);
                print("<" + ref + ">");
            }

        };
    }

    public void onEmptyLines(int count) {
        //
    }

    /**
     * @see org.wikimodel.wem.IWemListener#onEscape(java.lang.String)
     */
    public void onEscape(String str) {
        print(str);
    }

    public void onExtensionBlock(String extensionName, WikiParameters params) {
        //
    }

    public void onExtensionInline(String extensionName, WikiParameters params) {
    }

    /**
     * @see org.wikimodel.wem.IWemListener#onHorizontalLine(WikiParameters
     *      params)
     */
    public void onHorizontalLine(WikiParameters params) {
    }

    public void onImage(String ref) {
        print("<img");
        print(" src='" + ref + "'");
        print(" class='wikimodel-freestanding'/>");
    }

    public void onImage(WikiReference ref) {
        print("<img");
        String link = ref.getLink();
        link = WikiPageUtil.escapeXmlAttribute(link);
        print(" src='" + link + "'");
        WikiParameters params = ref.getParameters();
        String label = ref.getLabel();
        if (label != null) {
            if (params.getParameter("title") == null) {
                params = params.addParameter("title", label);
            }
        }
        print(params + "/>");
    }

    /**
     * @see org.wikimodel.wem.IWemListener#onLineBreak()
     */

    public void onLineBreak() {
        println("");
    }

    public void onMacroBlock(
        String macroName,
        WikiParameters params,
        String content) {
    }

    public void onMacroInline(
        String macroName,
        WikiParameters params,
        String content) {
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
        WikiReference reference = new WikiReference(ref);
        onReference(reference);
    }

    public void onReference(WikiReference ref) {
        fRefHandler.handle(ref);
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
     * @see org.wikimodel.wem.IWemListener#onVerbatimBlock(String,
     *      WikiParameters)
     */
    public void onVerbatimBlock(String str, WikiParameters params) {
        print(str);
    }

    /**
     * @see org.wikimodel.wem.IWemListener#onVerbatimInline(java.lang.String,
     *      WikiParameters)
     */
    public void onVerbatimInline(String str, WikiParameters params) {
        print(str);
    }

    /**
     * @see org.wikimodel.wem.IWemListener#onWord(java.lang.String)
     */

    public void onWord(String str) {
        print(str);
    }

    protected void print(String str) {
        fPrinter.print(str);
    }

    protected void println() {
        fPrinter.println("");
    }

    protected void println(String str) {
        fPrinter.println(str);
    }
}
