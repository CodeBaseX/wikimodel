package org.wikimodel.wem;

import org.wikimodel.wem.util.WikiEntityUtil;

/**
 * @author MikhailKotelnikov
 */
public class PrintInlineListener extends PrintTextListener {

    /**
     * 
     */
    public PrintInlineListener(IWikiPrinter printer) {
        super(printer);
    }

    /**
     * @see org.wikimodel.wem.IWemListener#beginFormat(org.wikimodel.wem.WikiFormat)
     */
    public void beginFormat(WikiFormat format) {
        print(format.getTags(true));
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
     * @see org.wikimodel.wem.IWemListener#endFormat(org.wikimodel.wem.WikiFormat)
     */
    public void endFormat(WikiFormat format) {
        print(format.getTags(false));
    }

    /**
     * @see org.wikimodel.wem.IWemListener#endPropertyInline(java.lang.String)
     */
    public void endPropertyInline(String inlineProperty) {
        print("</span>");
    }

    /**
     * @see org.wikimodel.wem.IWemListener#onEscape(java.lang.String)
     */
    public void onEscape(String str) {
        print("<span class='escaped'>"
            + WikiPageUtil.escapeXmlString(str)
            + "</span>");
    }

    public void onExtensionInline(String extensionName, WikiParameters params) {
        print("<span class='extension' extension='"
            + extensionName
            + "' "
            + params
            + " />");
    }

    /**
     * @see org.wikimodel.wem.IWemListener#onLineBreak()
     */

    public void onLineBreak() {
        print("<br />");
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
     * @see org.wikimodel.wem.IWemListener#onSpecialSymbol(java.lang.String)
     */
    public void onSpecialSymbol(String str) {
        String entity = WikiEntityUtil.getHtmlSymbol(str);
        if (entity != null) {
            entity = "&" + entity + ";";
            if (str.startsWith(" --")) {
                entity = "&nbsp;" + entity + " ";
            }
        } else {
            entity = WikiPageUtil.escapeXmlString(str);
        }
        print(entity);
    }

    /**
     * @see org.wikimodel.wem.IWemListener#onVerbatimInline(java.lang.String)
     */
    public void onVerbatimInline(String str) {
        print("<code>" + WikiPageUtil.escapeXmlString(str) + "</code>");
    }

}
