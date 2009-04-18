package org.wikimodel.wem.impl;

import org.wikimodel.wem.IWemListener;
import org.wikimodel.wem.WikiFormat;
import org.wikimodel.wem.WikiParameters;
import org.wikimodel.wem.WikiReference;
import org.wikimodel.wem.WikiStyle;
import org.wikimodel.wem.util.IListListener;
import org.wikimodel.wem.util.ListBuilder;

/**
 * @author MikhailKotelnikov
 */
class InternalWikiScannerContext implements IWikiScannerContext {

    private interface IBlockTypes {

        int HEADER = 1 << 1;

        int INFO = 1 << 2;

        int LIST = 1 << 3;

        int LIST_DL = (1 << 4) | LIST;

        int LIST_DL_DD = (1 << 5) | LIST_DL;

        int LIST_DL_DT = (1 << 6) | LIST_DL;

        int LIST_LI = (1 << 7) | LIST;

        int NONE = 0;

        int PARAGRAPH = 1 << 8;

        int QUOT = 1 << 9;

        int QUOT_LI = (1 << 10) | QUOT;

        int TABLE = 1 << 11;

        int TABLE_ROW = (1 << 12) | TABLE;

        int TABLE_ROW_CELL = (1 << 13) | TABLE_ROW;
    }

    private int fBlockType = IBlockTypes.NONE;

    private WikiFormat fFormat;

    private int fHeaderLevel;

    private WikiParameters fHeaderParams;

    private WikiParameters fInfoParams;

    private char fInfoType;

    private String fInlineProperty;

    private InlineState fInlineState = new InlineState();

    private ListBuilder fListBuilder;

    private final IWemListener fListener;

    private WikiParameters fListParams;

    private WikiFormat fNewFormat = WikiFormat.EMPTY;

    private WikiParameters fParagraphParams;

    private String fProperty;

    private boolean fPropertyDoc;

    private ListBuilder fQuotBuilder;

    private int fQuoteDepth = 0;

    private WikiParameters fQuotParams;

    private int fTableCellCounter = -1;

    private WikiParameters fTableCellParams = WikiParameters.EMPTY;

    private boolean fTableHead;

    private WikiParameters fTableParams;

    private int fTableRowCounter = -1;

    private WikiParameters fTableRowParams;

    public InternalWikiScannerContext(IWemListener listener) {
        fListener = listener;
    }

    public void beginDocument() {
        fInlineState.set(InlineState.BEGIN_FORMAT);
        fListener.beginDocument();
    }

    public void beginHeader(int level) {
        beginHeader(level, WikiParameters.EMPTY);
    }

    public void beginHeader(int level, WikiParameters params) {
        if (level < 1)
            level = 1;
        else if (level > 6)
            level = 6;
        if (fBlockType != IBlockTypes.HEADER) {
            closeBlock();
            fBlockType = IBlockTypes.HEADER;
            fHeaderLevel = level;
            fHeaderParams = params;
            fListener.beginHeader(level, fHeaderParams);
        }
        beginStyleContainer();
    }

    public void beginInfo(char type, WikiParameters params) {
        if ((fBlockType & IBlockTypes.INFO) != IBlockTypes.INFO) {
            closeBlock();
            fInfoType = type;
            fInfoParams = params != null ? params : WikiParameters.EMPTY;
            fListener.beginInfoBlock(fInfoType, fInfoParams);
            fBlockType = IBlockTypes.INFO;
        }
        beginStyleContainer();
    }

    public void beginList() {
        if ((fBlockType & IBlockTypes.LIST) != IBlockTypes.LIST) {
            closeBlock();
            if (fListParams == null)
                fListParams = WikiParameters.EMPTY;
            IListListener listener = new IListListener() {

                public void beginRow(char treeType, char rowType) {
                    if (rowType == ':') {
                        fBlockType = IBlockTypes.LIST_DL_DD;
                        fListener.beginDefinitionDescription();
                    } else if (rowType == ';') {
                        fBlockType = IBlockTypes.LIST_DL_DT;
                        fListener.beginDefinitionTerm();
                    } else {
                        fBlockType = IBlockTypes.LIST_LI;
                        fListener.beginListItem();
                    }
                    beginStyleContainer();
                }

                public void beginTree(char type) {
                    closeFormat();
                    switch (type) {
                        case '#':
                            fListener.beginList(fListParams, true);
                            fBlockType = IBlockTypes.LIST;
                            break;
                        case 'd':
                            fListener.beginDefinitionList(fListParams);
                            fBlockType = IBlockTypes.LIST_DL;
                            break;
                        default:
                            fListener.beginList(fListParams, false);
                            fBlockType = IBlockTypes.LIST;
                            break;
                    }
                }

                public void endRow(char treeType, char rowType) {
                    closeFormat();
                    endStyleContainer();
                    if (rowType == ':') {
                        fListener.endDefinitionDescription();
                        fBlockType = IBlockTypes.LIST_DL;
                    } else if (rowType == ';') {
                        if ((fBlockType & IBlockTypes.LIST_DL_DT) == IBlockTypes.LIST_DL_DT)
                            fListener.endDefinitionTerm();
                        else
                            fListener.endDefinitionDescription();
                        fBlockType = IBlockTypes.LIST_DL;
                    } else {
                        fListener.endListItem();
                        fBlockType = IBlockTypes.LIST;
                    }
                }

                public void endTree(char type) {
                    switch (type) {
                        case '#':
                            fListener.endList(fListParams, true);
                            fBlockType = IBlockTypes.LIST;
                            break;
                        case 'd':
                            fListener.endDefinitionList(fListParams);
                            fBlockType = IBlockTypes.LIST;
                            break;
                        default:
                            fListener.endList(fListParams, false);
                            fBlockType = IBlockTypes.LIST;
                            break;
                    }
                }

            };
            fListBuilder = new ListBuilder(listener) {

                @Override
                protected char getTreeType(char rowType) {
                    if (rowType == ';' || rowType == ':')
                        return 'd';
                    return rowType;
                }
            };
            fBlockType = IBlockTypes.LIST;
        }
    }

    public void beginList(WikiParameters params) {
        fListParams = params;
        beginList();
    }

    public void beginListItem(String item) {
        beginListItem(item, WikiParameters.EMPTY);
    }
    
    public void beginListItem(String item, WikiParameters params) {
        beginList();
        
        if (fListParams == WikiParameters.EMPTY) {
            fListParams = params;
        }
        
        item = trimLineBreaks(item);
        item = replace(item, ";:", ":");
        // Definitions can not have subitems...
        int idx = item.indexOf(';');
        if (idx >= 0)
            item = item.substring(0, idx + 1);
        fListBuilder.alignContext(item);
        fListParams = WikiParameters.EMPTY;
    }

    public void beginParagraph() {
        if (fBlockType != IBlockTypes.PARAGRAPH) {
            closeBlock();
            if (fParagraphParams == null)
                fParagraphParams = WikiParameters.EMPTY;
            fListener.beginParagraph(fParagraphParams);
            fBlockType = IBlockTypes.PARAGRAPH;
            beginStyleContainer();
        }
    }

    public void beginParagraph(WikiParameters params) {
        fParagraphParams = params;
        beginParagraph();
    }

    public void beginPropertyBlock(String property, boolean doc) {
        closeBlock();
        fProperty = property;
        fPropertyDoc = doc;
        fListener.beginPropertyBlock(fProperty, fPropertyDoc);
    }

    public void beginPropertyInline(String str) {
        closeFormat();
        beginStyleContainer();
        fInlineProperty = str;
        fListener.beginPropertyInline(fInlineProperty);
    }

    public void beginQuot() {
        if ((fBlockType & IBlockTypes.QUOT) != IBlockTypes.QUOT) {
            closeBlock();
            if (fQuotParams == null)
                fQuotParams = WikiParameters.EMPTY;
            IListListener listener = new IListListener() {

                public void beginRow(char treeType, char rowType) {
                    fBlockType = IBlockTypes.QUOT_LI;
                    fListener.beginQuotationLine();
                    beginStyleContainer();
                }

                public void beginTree(char type) {
                    closeFormat();
                    fListener.beginQuotation(fQuotParams);
                    fBlockType = IBlockTypes.QUOT;
                }

                public void endRow(char treeType, char rowType) {
                    closeFormat();
                    endStyleContainer();
                    fListener.endQuotationLine();
                    fBlockType = IBlockTypes.QUOT;
                }

                public void endTree(char type) {
                    closeFormat();
                    fListener.endQuotation(fQuotParams);
                    fBlockType = IBlockTypes.QUOT;
                }

            };
            fQuotBuilder = new ListBuilder(listener);
            fBlockType = IBlockTypes.QUOT;
        }
    }

    public void beginQuot(WikiParameters params) {
        fQuotParams = params;
        beginQuot();
    }

    /**
     * @see org.wikimodel.wem.impl.IWikiScannerContext#beginQuotLine(int)
     */
    public void beginQuotLine(int depth) {
        beginQuot();
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < depth; i++)
            buf.append(" ");
        buf.append("*");
        fQuoteDepth = depth;
        fQuotBuilder.alignContext(buf.toString());
    }

    private void beginStyleContainer() {
        fInlineState.set(InlineState.BEGIN);
    }

    public void beginTable() {
        if ((fBlockType & IBlockTypes.TABLE) != IBlockTypes.TABLE) {
            closeBlock();
            if (fTableParams == null)
                fTableParams = WikiParameters.EMPTY;
            fTableRowCounter = 0;
            fListener.beginTable(fTableParams);
            fBlockType = IBlockTypes.TABLE;
        }
    }

    public void beginTable(WikiParameters params) {
        fTableParams = params;
        beginTable();
    }

    public void beginTableCell(boolean headCell) {
        beginTableCell(headCell, fTableCellParams);
    }

    public void beginTableCell(boolean headCell, WikiParameters params) {
        if ((fBlockType & IBlockTypes.TABLE_ROW_CELL) != IBlockTypes.TABLE_ROW_CELL) {
            beginTableRow(null);
            fTableHead = headCell;
            fTableCellParams = params != null ? params : WikiParameters.EMPTY;
            fListener.beginTableCell(fTableHead, fTableCellParams);
            fBlockType = IBlockTypes.TABLE_ROW_CELL;
            beginStyleContainer();
        }
    }

    public void beginTableRow(boolean headCell) {
        if (beginTableRowInternal((WikiParameters) null)) {
            beginTableCell(headCell, null);
        }
    }

    public void beginTableRow(
        boolean head,
        WikiParameters rowParams,
        WikiParameters cellParams) {
        if (beginTableRowInternal(rowParams)) {
            beginTableCell(head, cellParams);
        }
    }

    public void beginTableRow(WikiParameters rowParams) {
        beginTableRowInternal(rowParams);
    }

    private boolean beginTableRowInternal(WikiParameters rowParams) {
        boolean result = false;
        if ((fBlockType & IBlockTypes.TABLE_ROW) != IBlockTypes.TABLE_ROW) {
            beginTable();
            fBlockType = IBlockTypes.TABLE_ROW;
            fTableCellCounter = 0;
            fTableRowParams = rowParams != null
                ? rowParams
                : WikiParameters.EMPTY;
            fListener.beginTableRow(fTableRowParams);
            result = true;
        }
        return result;
    }

    public boolean canApplyDefintionSplitter() {
        return fBlockType == IBlockTypes.LIST_DL_DT;
    }

    void checkBlockContainer() {
        if (isInTable()) {
            checkTableCell();
        } else if (isInList()) {
            checkListItem();
        } else if (!isInTableCell() && !isInListItem()) {
            closeBlock();
        } else if (isInQuotation()) {
            checkQuotationLine();
        }
    }

    public boolean checkFormatStyle(WikiStyle style) {
        return style != null && fFormat != null && fFormat.hasStyle(style);
    }

    private void checkListItem() {
        if (!isInListItem()) {
            beginListItem("*");
        }
    }

    private void checkParagraph() {
        if (!isInParagraph()) {
            beginParagraph();
        }
    }

    private void checkQuotationLine() {
        if (!isInQuotationLine()) {
            beginQuotLine(fQuoteDepth);
        }
    }

    private void checkStyleOpened() {
        if (isInTable()) {
            checkTableCell();
        } else if (isInList()) {
            checkListItem();
        } else if (isInQuotation()) {
            checkQuotationLine();
        } else if (isNoBlockElements()) {
            checkParagraph();
        }
        openFormat();
    }

    private void checkTableCell() {
        if (!isInTableCell()) {
            beginTableCell(fTableHead);
        }
    }

    public void closeBlock() {
        switch (fBlockType) {
            case IBlockTypes.NONE:
                break;
            case IBlockTypes.HEADER:
                endHeader();
                break;
            case IBlockTypes.PARAGRAPH:
                endParagraph();
                break;
            case IBlockTypes.INFO:
                endInfo();
                break;
            default:
                if ((fBlockType & IBlockTypes.TABLE) != 0) {
                    endTable();
                } else if ((fBlockType & IBlockTypes.LIST) != 0) {
                    endList();
                } else if ((fBlockType & IBlockTypes.QUOT) != 0) {
                    endQuot();
                }
                break;
        }
    }

    public void closeFormat() {
        closeFormat(true);
    }

    private void closeFormat(boolean resetNewStyle) {
        if (fFormat != null) {
            fListener.endFormat(fFormat);
            fFormat = null;
        }
        if (resetNewStyle)
            fNewFormat = WikiFormat.EMPTY;
    }

    public void endDocument() {
        closeBlock();
        fListener.endDocument();
    }

    public void endHeader() {
        if ((fBlockType & IBlockTypes.HEADER) != 0) {
            closeFormat();
            endStyleContainer();
            fListener.endHeader(fHeaderLevel, fHeaderParams);
            fHeaderLevel = -1;
            fBlockType = IBlockTypes.NONE;
        }
    }

    public void endInfo() {
        if ((fBlockType & IBlockTypes.INFO) != 0) {
            closeFormat();
            endStyleContainer();
            fListener.endInfoBlock(fInfoType, fInfoParams);
            fInfoType = '\0';
            fInfoParams = null;
            fBlockType = IBlockTypes.NONE;
        }
    }

    public void endList() {
        if ((fBlockType & IBlockTypes.LIST) != 0) {
            fListBuilder.alignContext("");
            fListBuilder = null;
            fBlockType = IBlockTypes.NONE;
            fListParams =  WikiParameters.EMPTY;
        }
    }

    public void endListItem() {
    }

    public void endParagraph() {
        if ((fBlockType & IBlockTypes.PARAGRAPH) != 0) {
            closeFormat();
            endStyleContainer();
            fListener.endParagraph(fParagraphParams);
            fBlockType = IBlockTypes.NONE;
            fParagraphParams = WikiParameters.EMPTY;
        }
    }

    public void endPropertyBlock() {
        closeBlock();
        fListener.endPropertyBlock(fProperty, fPropertyDoc);
        fProperty = null;
    }

    public void endPropertyInline() {
        closeFormat();
        endStyleContainer();
        fListener.endPropertyInline(fInlineProperty);
        fInlineProperty = null;
    }

    public void endQuot() {
        if ((fBlockType & IBlockTypes.QUOT) != 0) {
            closeFormat();
            fQuotBuilder.alignContext("");
            fQuotBuilder = null;
            fBlockType = IBlockTypes.NONE;
        }
    }

    public void endQuotLine() {
        fQuoteDepth--;
        if (fQuoteDepth < 0) {
            fQuoteDepth = 0;
        }
        fBlockType = IBlockTypes.QUOT;
    }

    private void endStyleContainer() {
        fInlineState.set(InlineState.BEGIN);
    }

    public void endTable() {
        if ((fBlockType & IBlockTypes.TABLE) == IBlockTypes.TABLE) {
            endTableRow();
            fListener.endTable(fTableParams);
            fTableRowCounter = -1;
            fBlockType = IBlockTypes.NONE;
        }
    }

    public void endTableCell() {
        if ((fBlockType & IBlockTypes.TABLE_ROW_CELL) == IBlockTypes.TABLE_ROW_CELL) {
            closeFormat();
            endStyleContainer();
            fListener.endTableCell(fTableHead, fTableCellParams);
            fTableCellCounter++;
            fBlockType = IBlockTypes.TABLE_ROW;
            fTableCellParams = WikiParameters.EMPTY;
        }
    }

    public void endTableExplicit() {
        endTable();
    }

    public void endTableRow() {
        if ((fBlockType & IBlockTypes.TABLE_ROW) == IBlockTypes.TABLE_ROW) {
            if (fTableCellCounter <= 0)
                checkTableCell();
            endTableCell();
            fListener.endTableRow(fTableRowParams);
            fBlockType = IBlockTypes.TABLE;
            fTableHead = false;
            fTableCellCounter = -1;
            fTableRowCounter++;
        }
    }

    public InlineState getInlineState() {
        return fInlineState;
    }

    /**
     * Returns the tableCellCounter.
     * 
     * @return the tableCellCounter.
     */
    public int getTableCellCounter() {
        return fTableCellCounter;
    }

    /**
     * Returns the tableRowCounter.
     * 
     * @return the tableRowCounter.
     */
    public int getTableRowCounter() {
        return fTableRowCounter;
    }

    /**
     * Returns the inDefinitionList.
     * 
     * @return the inDefinitionList.
     */
    public boolean isInDefinitionList() {
        return (fBlockType & IBlockTypes.LIST_DL) == IBlockTypes.LIST_DL;
    }

    public boolean isInDefinitionTerm() {
        return fBlockType == IBlockTypes.LIST_DL_DT;
    }

    /**
     * Returns the inHeader.
     * 
     * @return the inHeader.
     */
    public boolean isInHeader() {
        return fHeaderLevel > 0;
    }

    public boolean isInInlineProperty() {
        return fInlineProperty != null;
    }

    /**
     * Returns the inList.
     * 
     * @return the inList.
     */
    public boolean isInList() {
        return fBlockType == IBlockTypes.LIST;
    }

    private boolean isInListItem() {
        return (fBlockType & IBlockTypes.LIST_LI) == IBlockTypes.LIST_LI;
    }

    private boolean isInParagraph() {
        return fBlockType == IBlockTypes.PARAGRAPH;
    }

    public boolean isInQuotation() {
        return fBlockType == IBlockTypes.QUOT;
    }

    private boolean isInQuotationLine() {
        return (fBlockType & IBlockTypes.QUOT_LI) == IBlockTypes.QUOT_LI;
    }

    public boolean isInTable() {
        return ((fBlockType & IBlockTypes.TABLE) == IBlockTypes.TABLE);
    }

    public boolean isInTableCell() {
        return (fBlockType & IBlockTypes.TABLE_ROW_CELL) == IBlockTypes.TABLE_ROW_CELL;
    }

    public boolean isInTableRow() {
        return (fBlockType & IBlockTypes.TABLE_ROW) == IBlockTypes.TABLE_ROW;
    }

    private boolean isNoBlockElements() {
        return (fBlockType == IBlockTypes.NONE);
    }

    public void onDefinitionListItemSplit() {
        closeFormat();
        if ((fBlockType & IBlockTypes.LIST_DL_DT) == IBlockTypes.LIST_DL_DT) {
            closeFormat();
            endStyleContainer();
            fListener.endDefinitionTerm();
        } else if ((fBlockType & IBlockTypes.LIST_DL_DD) == IBlockTypes.LIST_DL_DD) {
            closeFormat();
            endStyleContainer();
            fListener.endDefinitionDescription();
        }
        fBlockType = IBlockTypes.LIST_DL_DD;
        fListener.beginDefinitionDescription();
        beginStyleContainer();
    }

    public void onEmptyLines(int count) {
        closeBlock();
        fListener.onEmptyLines(count);
    }

    public void onEscape(String str) {
        checkStyleOpened();
        fListener.onEscape(str);
        fInlineState.set(InlineState.ESCAPE);
    }

    public void onExtensionBlock(String extensionName, WikiParameters params) {
        fListener.onExtensionBlock(extensionName, params);
    }

    public void onExtensionInline(String extensionName, WikiParameters params) {
        fListener.onExtensionInline(extensionName, params);
        fInlineState.set(InlineState.EXTENSION);
    }

    public void onFormat(WikiParameters params) {
        closeFormat(false);
        fNewFormat = fNewFormat.setParameters(params.toList());
        fInlineState.set(InlineState.BEGIN_FORMAT);
    }

    public void onFormat(WikiStyle wikiStyle) {
        onFormat(wikiStyle, false);
    }

    public void onFormat(WikiStyle wikiStyle, boolean forceClose) {
        closeFormat(false);
        if (forceClose) {
            fNewFormat = fNewFormat.removeStyle(wikiStyle);
        } else {
            fNewFormat = fNewFormat.switchStyle(wikiStyle);
        }
        fInlineState.set(InlineState.BEGIN_FORMAT);
    }

    public void onHorizontalLine() {
        onHorizontalLine(WikiParameters.EMPTY);
    }

    public void onHorizontalLine(WikiParameters params) {
        closeBlock();
        fListener.onHorizontalLine(params);
    }

    public void onImage(String ref) {
        checkStyleOpened();
        fListener.onImage(ref);
        fInlineState.set(InlineState.IMAGE);
    }

    public void onImage(WikiReference ref) {
        checkStyleOpened();
        fListener.onImage(ref);
        fInlineState.set(InlineState.IMAGE);
    }

    public void onLineBreak() {
        checkStyleOpened();
        fListener.onLineBreak();
        fInlineState.set(InlineState.LINE_BREAK);
    }

    public void onMacroBlock(
        String macroName,
        WikiParameters params,
        String content) {
        checkBlockContainer();
        fListener.onMacroBlock(macroName, params, content);
    }

    public void onMacroInline(
        String macroName,
        WikiParameters params,
        String content) {
        checkStyleOpened();
        fListener.onMacroInline(macroName, params, content);
        fInlineState.set(InlineState.MACRO);
    }

    public void onNewLine() {
        checkStyleOpened();
        fListener.onNewLine();
        fInlineState.set(InlineState.NEW_LINE);
    }

    public void onQuotLine(int depth) {
        endQuotLine();
        beginQuotLine(depth);
    }

    public void onReference(String ref) {
        checkStyleOpened();
        fListener.onReference(ref);
        fInlineState.set(InlineState.REFERENCE);
    }

    public void onReference(WikiReference ref) {
        checkStyleOpened();
        fListener.onReference(ref);
        fInlineState.set(InlineState.REFERENCE);
    }

    public void onSpace(String str) {
        checkStyleOpened();
        fListener.onSpace(str);
        fInlineState.set(InlineState.SPACE);
    }

    public void onSpecialSymbol(String str) {
        checkStyleOpened();
        fListener.onSpecialSymbol(str);
        fInlineState.set(InlineState.SPECIAL_SYMBOL);
    }

    public void onTableCaption(String str) {
        beginTable();
        fListener.onTableCaption(str);
    }

    public void onTableCell(boolean headCell) {
        onTableCell(headCell, null);
    }

    public void onTableCell(boolean head, WikiParameters params) {
        endTableCell();
        fTableHead = head;
        fTableCellParams = params != null ? params : WikiParameters.EMPTY;
        // beginTableCell(head, params);
    }

    public void onTableRow(WikiParameters params) {
        endTableRow();
        beginTableRow(params);
    }

    public void onVerbatim(String str, boolean inline) {
        onVerbatim(str, inline, WikiParameters.EMPTY);
    }

    public void onVerbatim(String str, boolean inline, WikiParameters params) {
        if (!inline) {
            checkBlockContainer();
            fListener.onVerbatimBlock(str, params);
        } else {
            checkStyleOpened();
            fListener.onVerbatimInline(str);
            fInlineState.set(InlineState.VERBATIM);
        }
    }

    public void onWord(String str) {
        checkStyleOpened();
        fListener.onWord(str);
        fInlineState.set(InlineState.WORD);
    }

    private void openFormat() {
        if (!fNewFormat.equals(fFormat)) {
            WikiFormat newFormat = fNewFormat;
            closeFormat();
            fFormat = newFormat;
            fNewFormat = newFormat;
            fListener.beginFormat(fFormat);
        }
        fInlineState.set(InlineState.BEGIN_FORMAT);
    }

    private String replace(String item, String from, String to) {
        int pos = 0;
        int prevPos = pos;
        StringBuffer buf = new StringBuffer();
        while (true) {
            pos = item.indexOf(from, pos);
            if (pos < 0) {
                pos = item.length();
                break;
            }
            buf.append(item.substring(prevPos, pos));
            buf.append(to);
            pos += from.length();
            prevPos = pos;
        }
        buf.append(item.substring(prevPos, pos));
        return buf.toString();
    }

    private String trimLineBreaks(String item) {
        StringBuffer buf = new StringBuffer();
        char[] array = item.toCharArray();
        for (char ch : array) {
            if (ch == '\n' || ch == '\r')
                continue;
            buf.append(ch);
        }
        return buf.toString();
    }

}
