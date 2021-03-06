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
    private final IWemListener[] fListeners;

    /**
     * @param listeners an array of listeners to which all method calls will be
     *        delegated
     */
    public CompositeListener(IWemListener... listeners) {
        fListeners = listeners;
    }

    /**
     * @see org.wikimodel.wem.IWemListener#beginDefinitionDescription()
     */
    public void beginDefinitionDescription() {
        for (IWemListener listener : fListeners) {
            listener.beginDefinitionDescription();
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListener#beginDefinitionList(org.wikimodel.wem.WikiParameters)
     */
    public void beginDefinitionList(WikiParameters params) {
        for (IWemListener listener : fListeners) {
            listener.beginDefinitionList(params);
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListener#beginDefinitionTerm()
     */
    public void beginDefinitionTerm() {
        for (IWemListener listener : fListeners) {
            listener.beginDefinitionTerm();
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListenerDocument#beginDocument(org.wikimodel.wem.WikiParameters)
     */
    public void beginDocument(WikiParameters params) {
        for (IWemListener listener : fListeners) {
            listener.beginDocument(params);
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListener#beginFormat(org.wikimodel.wem.WikiFormat)
     */
    public void beginFormat(WikiFormat format) {
        for (IWemListener listener : fListeners) {
            listener.beginFormat(format);
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListener#beginHeader(int, WikiParameters)
     */
    public void beginHeader(int headerLevel, WikiParameters params) {
        for (IWemListener listener : fListeners) {
            listener.beginHeader(headerLevel, params);
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListener#beginInfoBlock(String,
     *      org.wikimodel.wem.WikiParameters)
     */
    public void beginInfoBlock(String infoType, WikiParameters params) {
        for (IWemListener listener : fListeners) {
            listener.beginInfoBlock(infoType, params);
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListener#beginList(org.wikimodel.wem.WikiParameters,
     *      boolean)
     */
    public void beginList(WikiParameters params, boolean ordered) {
        for (IWemListener listener : fListeners) {
            listener.beginList(params, ordered);
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListener#beginListItem()
     */
    public void beginListItem() {
        for (IWemListener listener : fListeners) {
            listener.beginListItem();
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListener#beginParagraph(org.wikimodel.wem.WikiParameters)
     */
    public void beginParagraph(WikiParameters params) {
        for (IWemListener listener : fListeners) {
            listener.beginParagraph(params);
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListener#beginPropertyBlock(java.lang.String,
     *      boolean)
     */
    public void beginPropertyBlock(String propertyUri, boolean doc) {
        for (IWemListener listener : fListeners) {
            listener.beginPropertyBlock(propertyUri, doc);
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListener#beginPropertyInline(java.lang.String)
     */
    public void beginPropertyInline(String str) {
        for (IWemListener listener : fListeners) {
            listener.beginPropertyInline(str);
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListener#beginQuotation(org.wikimodel.wem.WikiParameters)
     */
    public void beginQuotation(WikiParameters params) {
        for (IWemListener listener : fListeners) {
            listener.beginQuotation(params);
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListener#beginQuotationLine()
     */
    public void beginQuotationLine() {
        for (IWemListener listener : fListeners) {
            listener.beginQuotationLine();
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListenerDocument#beginSection(int, int,
     *      WikiParameters)
     */
    public void beginSection(
        int docLevel,
        int headerLevel,
        WikiParameters params) {
        for (IWemListener listener : fListeners) {
            listener.beginSection(docLevel, headerLevel, params);
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListenerDocument#beginSectionContent(int, int,
     *      WikiParameters)
     */
    public void beginSectionContent(
        int docLevel,
        int headerLevel,
        WikiParameters params) {
        for (IWemListener listener : fListeners) {
            listener.beginSectionContent(docLevel, headerLevel, params);
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListener#beginTable(org.wikimodel.wem.WikiParameters)
     */
    public void beginTable(WikiParameters params) {
        for (IWemListener listener : fListeners) {
            listener.beginTable(params);
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListener#beginTableCell(boolean,
     *      WikiParameters)
     */
    public void beginTableCell(boolean tableHead, WikiParameters params) {
        for (IWemListener listener : fListeners) {
            listener.beginTableCell(tableHead, params);
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListener#beginTableRow(org.wikimodel.wem.WikiParameters)
     */
    public void beginTableRow(WikiParameters params) {
        for (IWemListener listener : fListeners) {
            listener.beginTableRow(params);
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListener#endDefinitionDescription()
     */
    public void endDefinitionDescription() {
        for (IWemListener listener : fListeners) {
            listener.endDefinitionDescription();
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListener#endDefinitionList(org.wikimodel.wem.WikiParameters)
     */
    public void endDefinitionList(WikiParameters params) {
        for (IWemListener listener : fListeners) {
            listener.endDefinitionList(params);
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListener#endDefinitionTerm()
     */
    public void endDefinitionTerm() {
        for (IWemListener listener : fListeners) {
            listener.endDefinitionTerm();
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListenerDocument#endDocument(org.wikimodel.wem.WikiParameters)
     */
    public void endDocument(WikiParameters params) {
        for (IWemListener listener : fListeners) {
            listener.endDocument(params);
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListener#endFormat(org.wikimodel.wem.WikiFormat)
     */
    public void endFormat(WikiFormat format) {
        for (IWemListener listener : fListeners) {
            listener.endFormat(format);
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListener#endHeader(int, WikiParameters)
     */
    public void endHeader(int headerLevel, WikiParameters params) {
        for (IWemListener listener : fListeners) {
            listener.endHeader(headerLevel, params);
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListener#endInfoBlock(String,
     *      org.wikimodel.wem.WikiParameters)
     */
    public void endInfoBlock(String infoType, WikiParameters params) {
        for (IWemListener listener : fListeners) {
            listener.endInfoBlock(infoType, params);
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListener#endList(org.wikimodel.wem.WikiParameters,
     *      boolean)
     */
    public void endList(WikiParameters params, boolean ordered) {
        for (IWemListener listener : fListeners) {
            listener.endList(params, ordered);
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListener#endListItem()
     */
    public void endListItem() {
        for (IWemListener listener : fListeners) {
            listener.endListItem();
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListener#endParagraph(org.wikimodel.wem.WikiParameters)
     */
    public void endParagraph(WikiParameters params) {
        for (IWemListener listener : fListeners) {
            listener.endParagraph(params);
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListener#endPropertyBlock(java.lang.String,
     *      boolean)
     */
    public void endPropertyBlock(String propertyUri, boolean doc) {
        for (IWemListener listener : fListeners) {
            listener.endPropertyBlock(propertyUri, doc);
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListener#endPropertyInline(java.lang.String)
     */
    public void endPropertyInline(String inlineProperty) {
        for (IWemListener listener : fListeners) {
            listener.endPropertyInline(inlineProperty);
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListener#endQuotation(org.wikimodel.wem.WikiParameters)
     */
    public void endQuotation(WikiParameters params) {
        for (IWemListener listener : fListeners) {
            listener.endQuotation(params);
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListener#endQuotationLine()
     */
    public void endQuotationLine() {
        for (IWemListener listener : fListeners) {
            listener.endQuotationLine();
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListenerDocument#endSection(int, int,
     *      WikiParameters)
     */
    public void endSection(int docLevel, int headerLevel, WikiParameters params) {
        for (IWemListener listener : fListeners) {
            listener.endSection(docLevel, headerLevel, params);
        }

    }

    /**
     * @see org.wikimodel.wem.IWemListenerDocument#endSectionContent(int, int,
     *      WikiParameters)
     */
    public void endSectionContent(
        int docLevel,
        int headerLevel,
        WikiParameters params) {
        for (IWemListener listener : fListeners) {
            listener.endSectionContent(docLevel, headerLevel, params);
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListener#endTable(org.wikimodel.wem.WikiParameters)
     */
    public void endTable(WikiParameters params) {
        for (IWemListener listener : fListeners) {
            listener.endTable(params);
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListener#endTableCell(boolean, WikiParameters)
     */
    public void endTableCell(boolean tableHead, WikiParameters params) {
        for (IWemListener listener : fListeners) {
            listener.endTableCell(tableHead, params);
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListener#endTableRow(org.wikimodel.wem.WikiParameters)
     */
    public void endTableRow(WikiParameters params) {
        for (IWemListener listener : fListeners) {
            listener.endTableRow(params);
        }
    }

    public void onEmptyLines(int count) {
        for (IWemListener listener : fListeners) {
            listener.onEmptyLines(count);
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListener#onEscape(java.lang.String)
     */
    public void onEscape(String str) {
        for (IWemListener listener : fListeners) {
            listener.onEscape(str);
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListener#onExtensionBlock(java.lang.String,
     *      org.wikimodel.wem.WikiParameters)
     */
    public void onExtensionBlock(String extensionName, WikiParameters params) {
        for (IWemListener listener : fListeners) {
            listener.onExtensionBlock(extensionName, params);
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListener#onExtensionInline(java.lang.String,
     *      org.wikimodel.wem.WikiParameters)
     */
    public void onExtensionInline(String extensionName, WikiParameters params) {
        for (IWemListener listener : fListeners) {
            listener.onExtensionInline(extensionName, params);
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListener#onHorizontalLine(WikiParameters
     *      params)
     */
    public void onHorizontalLine(WikiParameters params) {
        for (IWemListener listener : fListeners) {
            listener.onHorizontalLine(params);
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListenerInline#onImage(java.lang.String)
     */
    public void onImage(String ref) {
        for (IWemListener listener : fListeners) {
            listener.onImage(ref);
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListenerInline#onImage(org.wikimodel.wem.WikiReference)
     */
    public void onImage(WikiReference ref) {
        for (IWemListener listener : fListeners) {
            listener.onImage(ref);
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListener#onLineBreak()
     */
    public void onLineBreak() {
        for (IWemListener listener : fListeners) {
            listener.onLineBreak();
        }
    }

    public void onMacroBlock(
        String macroName,
        WikiParameters params,
        String content) {
        for (IWemListener listener : fListeners) {
            listener.onMacroBlock(macroName, params, content);
        }
    }

    public void onMacroInline(
        String macroName,
        WikiParameters params,
        String content) {
        for (IWemListener listener : fListeners) {
            listener.onMacroInline(macroName, params, content);
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListener#onNewLine()
     */
    public void onNewLine() {
        for (IWemListener listener : fListeners) {
            listener.onNewLine();
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListener#onReference(java.lang.String)
     */
    public void onReference(String ref) {
        for (IWemListener listener : fListeners) {
            listener.onReference(ref);
        }
    }

    public void onReference(WikiReference ref) {
        for (IWemListener listener : fListeners) {
            listener.onReference(ref);
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListener#onSpace(java.lang.String)
     */
    public void onSpace(String str) {
        for (IWemListener listener : fListeners) {
            listener.onSpace(str);
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListener#onSpecialSymbol(java.lang.String)
     */
    public void onSpecialSymbol(String str) {
        for (IWemListener listener : fListeners) {
            listener.onSpecialSymbol(str);
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListener#onTableCaption(java.lang.String)
     */
    public void onTableCaption(String str) {
        for (IWemListener listener : fListeners) {
            listener.onTableCaption(str);
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListener#onVerbatimBlock(String,
     *      WikiParameters)
     */
    public void onVerbatimBlock(String str, WikiParameters params) {
        for (IWemListener listener : fListeners) {
            listener.onVerbatimBlock(str, params);
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListener#onVerbatimInline(java.lang.String,
     *      WikiParameters)
     */
    public void onVerbatimInline(String str, WikiParameters params) {
        for (IWemListener listener : fListeners) {
            listener.onVerbatimInline(str, params);
        }
    }

    /**
     * @see org.wikimodel.wem.IWemListener#onWord(java.lang.String)
     */
    public void onWord(String str) {
        for (IWemListener listener : fListeners) {
            listener.onWord(str);
        }
    }

}
