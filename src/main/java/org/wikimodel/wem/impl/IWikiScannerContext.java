package org.wikimodel.wem.impl;

import org.wikimodel.wem.IWemConstants;
import org.wikimodel.wem.WikiParameters;
import org.wikimodel.wem.WikiReference;
import org.wikimodel.wem.WikiStyle;

/**
 * @author MikhailKotelnikov
 * @author thomas.mortagne
 */
public interface IWikiScannerContext extends IWemConstants {

    void beginDocument();

    void beginDocument(WikiParameters params);

    void beginHeader(int level);

    void beginHeader(int level, WikiParameters params);

    void beginInfo(String type, WikiParameters params);

    void beginList();

    void beginList(WikiParameters params);

    void beginListItem(String item);

    void beginListItem(String item, WikiParameters params);

    void beginParagraph();

    void beginParagraph(WikiParameters params);

    void beginPropertyBlock(String property, boolean doc);

    void beginPropertyInline(String str);

    void beginQuot();

    void beginQuot(WikiParameters params);

    void beginQuotLine(int depth);

    void beginTable();

    void beginTable(WikiParameters params);

    void beginTableCell(boolean headCell);

    void beginTableCell(boolean headCell, WikiParameters params);

    /**
     * Starts a new table row and adds the first cell to the table.
     * 
     * @param headCell if this parameter is <code>true</code> then this method
     *        starts the header cell at the beginning of the line
     */
    void beginTableRow(boolean headCell);

    /**
     * Starts a new table row and adds the first cell to the table.
     * 
     * @param head if this parameter is <code>true</code> then this method
     *        starts the header cell at the beginning of the line
     * @param rowParams parameters of the row
     * @param cellParams parameters of the first row cell
     */
    void beginTableRow(
        boolean head,
        WikiParameters rowParams,
        WikiParameters cellParams);

    /**
     * Starts a new table row (but doesn't add a cell).
     */
    void beginTableRow(WikiParameters rowParams);

    boolean canApplyDefintionSplitter();

    boolean checkFormatStyle(WikiStyle style);

    void closeBlock();

    void endDocument();

    void endHeader();

    void endInfo();

    void endList();

    void endListItem();

    void endParagraph();

    void endPropertyBlock();

    void endPropertyInline();

    void endQuot();

    void endQuotLine();

    void endTable();

    void endTableCell();

    void endTableExplicit();

    void endTableRow();

    InlineState getInlineState();

    int getTableCellCounter();

    int getTableRowCounter();

    boolean isInDefinitionList();

    boolean isInDefinitionTerm();

    boolean isInHeader();

    boolean isInInlineProperty();

    boolean isInList();

    boolean isInTable();

    boolean isInTableCell();

    boolean isInTableRow();

    void onDefinitionListItemSplit();

    void onEmptyLines(int count);

    void onEscape(String str);

    void onExtensionBlock(String extensionName, WikiParameters params);

    void onExtensionInline(String extensionName, WikiParameters params);

    void onFormat(WikiParameters params);

    void onFormat(WikiStyle wikiStyle);

    void beginFormat(WikiParameters params);

    void beginFormat(WikiStyle wikiStyle);

    void endFormat(WikiParameters params);

    void endFormat(WikiStyle wikiStyle);

    /**
     * @see org.wikimodel.wem.impl.WikiScannerContext#onFormat(org.wikimodel.wem.WikiStyle,
     *      boolean)
     */
    void onFormat(WikiStyle wikiStyle, boolean forceClose);

    void onHorizontalLine();

    void onHorizontalLine(WikiParameters params);

    void onImage(String ref);

    void onImage(WikiReference ref);

    void onLineBreak();

    void onMacro(String name, WikiParameters params, String content);

    void onMacro(
        String macroName,
        WikiParameters params,
        String content,
        boolean inline);

    void onMacroBlock(String macroName, WikiParameters params, String content);

    void onMacroInline(String macroName, WikiParameters params, String content);

    void onNewLine();

    void onQuotLine(int depth);

    void onReference(String ref);

    void onReference(WikiReference ref);

    void onSpace(String str);

    void onSpecialSymbol(String str);

    void onTableCaption(String str);

    void onTableCell(boolean headCell);

    void onTableCell(boolean head, WikiParameters cellParams);

    /**
     * Explicitly starts a new table row. This method should not create a new
     * cell at the beginning of the line. To automatically create the first row
     * cell the methods {@link #beginTableCell(boolean)} or
     * {@link #beginTableRow(boolean, WikiParameters, WikiParameters)} should be
     * used.
     * 
     * @param rowParameters
     */
    void onTableRow(WikiParameters params);

    void onVerbatim(String str, WikiParameters params);

    /**
     * @see org.wikimodel.wem.impl.WikiScannerContext#onVerbatim(java.lang.String,
     *      boolean)
     */
    void onVerbatim(String str, boolean inline);

    void onVerbatim(String str, boolean inline, WikiParameters params);

    void onWord(String str);

}
