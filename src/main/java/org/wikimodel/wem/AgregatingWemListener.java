/**
 * 
 */
package org.wikimodel.wem;

/**
 * @author kotelnikov
 */
public class AgregatingWemListener implements IWemListener {

    protected IWemListenerSimpleBlocks fBlockListener;

    protected IWemListenerDocument fDocumentListener;

    protected IWemListenerInline fInlineListener;

    protected IWemListenerList fListListener;

    protected IWemListenerProgramming fProgrammingListener;

    protected IWemListenerSemantic fSemanticListener;

    protected IWemListenerTable fTableListener;

    /**
     * 
     */
    public AgregatingWemListener() {
        // TODO Auto-generated constructor stub
    }

    /**
     * @see org.wikimodel.wem.IWemListenerList#beginDefinitionDescription()
     */
    public void beginDefinitionDescription() {
        if (fListListener != null) {
            fListListener.beginDefinitionDescription();
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListenerList#beginDefinitionList(org.wikimodel.wem.WikiParameters)
     */
    public void beginDefinitionList(WikiParameters params) {
        if (fListListener != null) {
            fListListener.beginDefinitionList(params);
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListenerList#beginDefinitionTerm()
     */
    public void beginDefinitionTerm() {
        if (fListListener != null) {
            fListListener.beginDefinitionTerm();
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListenerDocument#beginDocument(org.wikimodel.wem.WikiParameters)
     */
    public void beginDocument(WikiParameters params) {
        if (fDocumentListener != null) {
            fDocumentListener.beginDocument(params);
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListenerInline#beginFormat(org.wikimodel.wem.WikiFormat)
     */
    public void beginFormat(WikiFormat format) {
        if (fInlineListener != null) {
            fInlineListener.beginFormat(format);
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListenerDocument#beginHeader(int,
     *      org.wikimodel.wem.WikiParameters)
     */
    public void beginHeader(int headerLevel, WikiParameters params) {
        if (fDocumentListener != null) {
            fDocumentListener.beginHeader(headerLevel, params);
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListenerSimpleBlocks#beginInfoBlock(java.lang.String,
     *      org.wikimodel.wem.WikiParameters)
     */
    public void beginInfoBlock(String infoType, WikiParameters params) {
        if (fBlockListener != null) {
            fBlockListener.beginInfoBlock(infoType, params);
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListenerList#beginList(org.wikimodel.wem.WikiParameters,
     *      boolean)
     */
    public void beginList(WikiParameters params, boolean ordered) {
        if (fListListener != null) {
            fListListener.beginList(params, ordered);
        }

    }

    /**
     * @see org.wikimodel.wem.IWemListenerList#beginListItem()
     */
    public void beginListItem() {
        if (fListListener != null) {
            fListListener.beginListItem();
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListenerSimpleBlocks#beginParagraph(org.wikimodel.wem.WikiParameters)
     */
    public void beginParagraph(WikiParameters params) {
        if (fBlockListener != null) {
            fBlockListener.beginParagraph(params);
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListenerSemantic#beginPropertyBlock(java.lang.String,
     *      boolean)
     */
    public void beginPropertyBlock(String propertyUri, boolean doc) {
        if (fSemanticListener != null) {
            fSemanticListener.beginPropertyBlock(propertyUri, doc);
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListenerSemantic#beginPropertyInline(java.lang.String)
     */
    public void beginPropertyInline(String propertyUri) {
        if (fSemanticListener != null) {
            fSemanticListener.beginPropertyInline(propertyUri);
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListenerList#beginQuotation(org.wikimodel.wem.WikiParameters)
     */
    public void beginQuotation(WikiParameters params) {
        if (fListListener != null) {
            fListListener.beginQuotation(params);
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListenerList#beginQuotationLine()
     */
    public void beginQuotationLine() {
        if (fListListener != null) {
            fListListener.beginQuotationLine();
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListenerDocument#beginSection(int, int,
     *      org.wikimodel.wem.WikiParameters)
     */
    public void beginSection(
        int docLevel,
        int headerLevel,
        WikiParameters params) {
        if (fDocumentListener != null) {
            fDocumentListener.beginSection(docLevel, headerLevel, params);
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListenerDocument#beginSectionContent(int, int,
     *      org.wikimodel.wem.WikiParameters)
     */
    public void beginSectionContent(
        int docLevel,
        int headerLevel,
        WikiParameters params) {
        if (fDocumentListener != null) {
            fDocumentListener.beginSectionContent(
                docLevel,
                headerLevel,
                params);
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListenerTable#beginTable(org.wikimodel.wem.WikiParameters)
     */
    public void beginTable(WikiParameters params) {
        if (fTableListener != null) {
            fTableListener.beginTable(params);
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListenerTable#beginTableCell(boolean,
     *      org.wikimodel.wem.WikiParameters)
     */
    public void beginTableCell(boolean tableHead, WikiParameters params) {
        if (fTableListener != null) {
            fTableListener.beginTableCell(tableHead, params);
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListenerTable#beginTableRow(org.wikimodel.wem.WikiParameters)
     */
    public void beginTableRow(WikiParameters params) {
        if (fTableListener != null) {
            fTableListener.beginTableRow(params);
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListenerList#endDefinitionDescription()
     */
    public void endDefinitionDescription() {
        if (fListListener != null) {
            fListListener.endDefinitionDescription();
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListenerList#endDefinitionList(org.wikimodel.wem.WikiParameters)
     */
    public void endDefinitionList(WikiParameters params) {
        if (fListListener != null) {
            fListListener.beginDefinitionList(params);
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListenerList#endDefinitionTerm()
     */
    public void endDefinitionTerm() {
        if (fListListener != null) {
            fListListener.endDefinitionTerm();
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListenerDocument#endDocument(org.wikimodel.wem.WikiParameters)
     */
    public void endDocument(WikiParameters params) {
        if (fDocumentListener != null) {
            fDocumentListener.endDocument(params);
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListenerInline#endFormat(org.wikimodel.wem.WikiFormat)
     */
    public void endFormat(WikiFormat format) {
        if (fInlineListener != null) {
            fInlineListener.endFormat(format);
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListenerDocument#endHeader(int,
     *      org.wikimodel.wem.WikiParameters)
     */
    public void endHeader(int headerLevel, WikiParameters params) {
        if (fDocumentListener != null) {
            fDocumentListener.endHeader(headerLevel, params);
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListenerSimpleBlocks#endInfoBlock(java.lang.String,
     *      org.wikimodel.wem.WikiParameters)
     */
    public void endInfoBlock(String infoType, WikiParameters params) {
        if (fBlockListener != null) {
            fBlockListener.endInfoBlock(infoType, params);
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListenerList#endList(org.wikimodel.wem.WikiParameters,
     *      boolean)
     */
    public void endList(WikiParameters params, boolean ordered) {
        if (fListListener != null) {
            fListListener.endList(params, ordered);
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListenerList#endListItem()
     */
    public void endListItem() {
        if (fListListener != null) {
            fListListener.endListItem();
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListenerSimpleBlocks#endParagraph(org.wikimodel.wem.WikiParameters)
     */
    public void endParagraph(WikiParameters params) {
        if (fBlockListener != null) {
            fBlockListener.endParagraph(params);
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListenerSemantic#endPropertyBlock(java.lang.String,
     *      boolean)
     */
    public void endPropertyBlock(String propertyUri, boolean doc) {
        if (fSemanticListener != null) {
            fSemanticListener.endPropertyBlock(propertyUri, doc);
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListenerSemantic#endPropertyInline(java.lang.String)
     */
    public void endPropertyInline(String propertyUri) {
        if (fSemanticListener != null) {
            fSemanticListener.endPropertyInline(propertyUri);
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListenerList#endQuotation(org.wikimodel.wem.WikiParameters)
     */
    public void endQuotation(WikiParameters params) {
        if (fListListener != null) {
            fListListener.endQuotation(params);
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListenerList#endQuotationLine()
     */
    public void endQuotationLine() {
        if (fListListener != null) {
            fListListener.endQuotationLine();
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListenerDocument#endSection(int, int,
     *      org.wikimodel.wem.WikiParameters)
     */
    public void endSection(int docLevel, int headerLevel, WikiParameters params) {
        if (fDocumentListener != null) {
            fDocumentListener.endSection(docLevel, headerLevel, params);
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListenerDocument#endSectionContent(int, int,
     *      org.wikimodel.wem.WikiParameters)
     */
    public void endSectionContent(
        int docLevel,
        int headerLevel,
        WikiParameters params) {
        if (fDocumentListener != null) {
            fDocumentListener.endSectionContent(docLevel, headerLevel, params);
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListenerTable#endTable(org.wikimodel.wem.WikiParameters)
     */
    public void endTable(WikiParameters params) {
        if (fTableListener != null) {
            fTableListener.endTable(params);
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListenerTable#endTableCell(boolean,
     *      org.wikimodel.wem.WikiParameters)
     */
    public void endTableCell(boolean tableHead, WikiParameters params) {
        if (fTableListener != null) {
            fTableListener.endTableCell(tableHead, params);
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListenerTable#endTableRow(org.wikimodel.wem.WikiParameters)
     */
    public void endTableRow(WikiParameters params) {
        if (fTableListener != null) {
            fTableListener.endTableRow(params);
        }
    }

    /**
     * @return the blockListener
     */
    public IWemListenerSimpleBlocks getBlockListener() {
        return fBlockListener;
    }

    /**
     * @return the documentListener
     */
    public IWemListenerDocument getDocumentListener() {
        return fDocumentListener;
    }

    /**
     * @return the inlineListener
     */
    public IWemListenerInline getInlineListener() {
        return fInlineListener;
    }

    /**
     * @return the listListener
     */
    public IWemListenerList getListListener() {
        return fListListener;
    }

    /**
     * @return the programmingListener
     */
    public IWemListenerProgramming getProgrammingListener() {
        return fProgrammingListener;
    }

    /**
     * @return the semanticListener
     */
    public IWemListenerSemantic getSemanticListener() {
        return fSemanticListener;
    }

    /**
     * @return the tableListener
     */
    public IWemListenerTable getTableListener() {
        return fTableListener;
    }

    /**
     * @see org.wikimodel.wem.IWemListenerSimpleBlocks#onEmptyLines(int)
     */
    public void onEmptyLines(int count) {
        if (fBlockListener != null) {
            fBlockListener.onEmptyLines(count);
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListenerInline#onEscape(java.lang.String)
     */
    public void onEscape(String str) {
        if (fInlineListener != null) {
            fInlineListener.onEscape(str);
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListenerProgramming#onExtensionBlock(java.lang.String,
     *      org.wikimodel.wem.WikiParameters)
     */
    public void onExtensionBlock(String extensionName, WikiParameters params) {
        if (fProgrammingListener != null) {
            fProgrammingListener.onExtensionBlock(extensionName, params);
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListenerProgramming#onExtensionInline(java.lang.String,
     *      org.wikimodel.wem.WikiParameters)
     */
    public void onExtensionInline(String extensionName, WikiParameters params) {
        if (fProgrammingListener != null) {
            fProgrammingListener.onExtensionInline(extensionName, params);
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListenerSimpleBlocks#onHorizontalLine(org.wikimodel.wem.WikiParameters)
     */
    public void onHorizontalLine(WikiParameters params) {
        if (fBlockListener != null) {
            fBlockListener.onHorizontalLine(params);
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListenerInline#onImage(java.lang.String)
     */
    public void onImage(String ref) {
        if (fInlineListener != null) {
            fInlineListener.onImage(ref);
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListenerInline#onImage(org.wikimodel.wem.WikiReference)
     */
    public void onImage(WikiReference ref) {
        if (fInlineListener != null) {
            fInlineListener.onImage(ref);
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListenerInline#onLineBreak()
     */
    public void onLineBreak() {
        if (fInlineListener != null) {
            fInlineListener.onLineBreak();
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListenerProgramming#onMacroBlock(java.lang.String,
     *      org.wikimodel.wem.WikiParameters, java.lang.String)
     */
    public void onMacroBlock(
        String macroName,
        WikiParameters params,
        String content) {
        if (fProgrammingListener != null) {
            fProgrammingListener.onMacroBlock(macroName, params, content);
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListenerProgramming#onMacroInline(java.lang.String,
     *      org.wikimodel.wem.WikiParameters, java.lang.String)
     */
    public void onMacroInline(
        String macroName,
        WikiParameters params,
        String content) {
        if (fProgrammingListener != null) {
            fProgrammingListener.onMacroInline(macroName, params, content);
        }
    }

    /* èèèèèèèèèèèèèèèèèèèèèèèèèèèèèèèèèèèèèèèèèèèèèèèèèèèèèèèèèèèèèèèèèèèèèèèè */

    /**
     * @see org.wikimodel.wem.IWemListenerInline#onNewLine()
     */
    public void onNewLine() {
        if (fInlineListener != null) {
            fInlineListener.onNewLine();
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListenerInline#onReference(java.lang.String)
     */
    public void onReference(String ref) {
        if (fInlineListener != null) {
            fInlineListener.onReference(ref);
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListenerInline#onReference(org.wikimodel.wem.WikiReference)
     */
    public void onReference(WikiReference ref) {
        if (fInlineListener != null) {
            fInlineListener.onReference(ref);
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListenerInline#onSpace(java.lang.String)
     */
    public void onSpace(String str) {
        if (fInlineListener != null) {
            fInlineListener.onSpace(str);
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListenerInline#onSpecialSymbol(java.lang.String)
     */
    public void onSpecialSymbol(String str) {
        if (fInlineListener != null) {
            fInlineListener.onSpecialSymbol(str);
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListenerTable#onTableCaption(java.lang.String)
     */
    public void onTableCaption(String str) {
        if (fTableListener != null) {
            fTableListener.onTableCaption(str);
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListenerSimpleBlocks#onVerbatimBlock(java.lang.String,
     *      org.wikimodel.wem.WikiParameters)
     */
    public void onVerbatimBlock(String str, WikiParameters params) {
        if (fBlockListener != null) {
            fBlockListener.onVerbatimBlock(str, params);
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListenerInline#onVerbatimInline(java.lang.String,
     *      org.wikimodel.wem.WikiParameters)
     */
    public void onVerbatimInline(String str, WikiParameters params) {
        if (fInlineListener != null) {
            fInlineListener.onVerbatimInline(str, params);
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListenerInline#onWord(java.lang.String)
     */
    public void onWord(String str) {
        if (fInlineListener != null) {
            fInlineListener.onWord(str);
        }
    }

    /**
     * @param blockListener the blockListener to set
     */
    public void setBlockListener(IWemListenerSimpleBlocks blockListener) {
        fBlockListener = blockListener;
    }

    /**
     * @param documentListener the documentListener to set
     */
    public void setDocumentListener(IWemListenerDocument documentListener) {
        fDocumentListener = documentListener;
    }

    /**
     * @param inlineListener the inlineListener to set
     */
    public void setInlineListener(IWemListenerInline inlineListener) {
        fInlineListener = inlineListener;
    }

    /**
     * @param listListener the listListener to set
     */
    public void setListListener(IWemListenerList listListener) {
        fListListener = listListener;
    }

    /**
     * @param programmingListener the programmingListener to set
     */
    public void setProgrammingListener(
        IWemListenerProgramming programmingListener) {
        fProgrammingListener = programmingListener;
    }

    /**
     * @param semanticListener the semanticListener to set
     */
    public void setSemanticListener(IWemListenerSemantic semanticListener) {
        fSemanticListener = semanticListener;
    }

    /**
     * @param tableListener the tableListener to set
     */
    public void setTableListener(IWemListenerTable tableListener) {
        fTableListener = tableListener;
    }

}
