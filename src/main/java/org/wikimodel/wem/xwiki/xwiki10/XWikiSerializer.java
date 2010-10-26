package org.wikimodel.wem.xwiki.xwiki10;

import org.wikimodel.wem.IWikiPrinter;
import org.wikimodel.wem.PrintTextListener;
import org.wikimodel.wem.WikiParameters;

/**
 * Serializing XWiki 1.0 syntax ...
 * @see {@link org.wikimodel.wem.xwiki.xwiki20.XWikiSerializer2}
 */
public class XWikiSerializer extends PrintTextListener {

    private boolean fNewTableRow;

    public XWikiSerializer(IWikiPrinter printer) {
        super(printer);
    }

    @Override
    public void beginHeader(int headerLevel, WikiParameters params) {
        print("1");
        for (int i = 0; i < headerLevel - 1; i++) {
            print(".1");
        }
        print(" ");
    }

    @Override
    public void beginListItem() {
        print("* ");
    }

    @Override
    public void beginTable(WikiParameters params) {
        println("{table}");
    }

    @Override
    public void beginTableCell(boolean tableHead, WikiParameters params) {
        if (!fNewTableRow) {
            print("|");
        }
        fNewTableRow = false;
    }

    @Override
    public void beginTableRow(WikiParameters params) {
        fNewTableRow = true;
    }

    @Override
    public void endHeader(int headerLevel, WikiParameters params) {
        println();
        println();
    }

    @Override
    public void endList(WikiParameters params, boolean ordered) {
        println();
    }

    @Override
    public void endListItem() {
        println();
    }

    @Override
    public void endParagraph(WikiParameters params) {
        println();
        println();
    }

    @Override
    public void endTable(WikiParameters params) {
        println("{table}");
        println();
    }

    @Override
    public void endTableRow(WikiParameters params) {
        println();
    }

    protected String getEol() {
        return "\n";
    }

    public void onHorizontalLine() {
        println("----");

    }

    @Override
    public void onLineBreak() {
        println();
        println();
    }

    @Override
    public void onReference(String ref) {
        if (ref.indexOf("Image") == 0)
            print("{image" + ref.substring(5) + "}");
        else {
            int index = ref.indexOf("|");
            if (index > 0) {
                String label = ref.substring(index + 1);
                String link = ref.substring(0, index);
                link = link.replaceAll(" ", "_");
                print("[" + label + ">" + link + "]");
            } else {
                ref = ref.replaceAll(" ", "_");
                print("[" + ref + "]");
            }

        }
    }

    @Override
    public void onSpecialSymbol(String str) {
        print(str);
    }

    @Override
    public void onTableCaption(String str) {
        println(str);
    }

    @Override
    public void onVerbatimInline(String str, WikiParameters params) {
        println("{code}");
        println(str);
        println("{code}");

    }

}
