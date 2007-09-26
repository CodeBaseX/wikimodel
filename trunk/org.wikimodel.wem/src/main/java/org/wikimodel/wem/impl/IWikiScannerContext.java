package org.wikimodel.wem.impl;

import org.wikimodel.wem.IWemConstants;
import org.wikimodel.wem.WikiParameters;
import org.wikimodel.wem.WikiStyle;

/**
 * @author MikhailKotelnikov
 */
public interface IWikiScannerContext extends IWemConstants {

    void beginDocument();

    void beginHeader(int level);

    void beginHeader(int level, WikiParameters params);

    void beginInfo(char type, WikiParameters params);

    void beginList();

    void beginList(WikiParameters params);

    void beginListItem(String item);

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

    void beginTableExplicit();

    void beginTableRow(boolean headCell);

    void beginTableRow(boolean headCell, WikiParameters params);

    void beginTableRow(
        boolean head,
        WikiParameters rowParams,
        WikiParameters cellParams);

    boolean canApplyDefintionSplitter();

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

    int getTableCellCounter();

    int getTableRowCounter();

    boolean inInlineProperty();

    boolean isInDefinitionList();

    boolean isInDefinitionTerm();

    boolean isInHeader();

    boolean isInList();

    boolean isInTable();

    boolean isInTableCell();

    boolean isInTableRow();

    void onDefinitionListItemSplit();

    void onEscape(String str);

    void onExtensionBlock(String extensionName, WikiParameters params);

    void onExtensionInline(String extensionName, WikiParameters params);

    void onFormat(WikiStyle wikiStyle);

    /**
     * @see org.wikimodel.wem.impl.WikiScannerContext#onFormat(org.wikimodel.wem.WikiStyle,
     *      boolean)
     */
    void onFormat(WikiStyle wikiStyle, boolean forceClose);

    void onHorizontalLine();

    void onLineBreak();

    void onMacro(String macroName, WikiParameters params, String content);

    void onNewLine();

    void onQuotLine(int depth);

    void onReference(String ref);

    void onSpace(String str);

    void onSpecialSymbol(String str);

    void onTableCaption(String str);

    void onTableCell(boolean headCell);

    void onTableCell(boolean head, WikiParameters cellParams);

    void onTableRow(boolean header);

    /**
     * @see org.wikimodel.wem.impl.WikiScannerContext#onTableRow(boolean,
     *      org.wikimodel.wem.WikiParameters)
     */
    void onTableRow(boolean header, WikiParameters params);

    /**
     * @see org.wikimodel.wem.impl.WikiScannerContext#onVerbatim(java.lang.String,
     *      boolean)
     */
    void onVerbatim(String str, boolean inline);

    void onWord(String str);

}
