package org.wikimodel.wem.xwiki;

import org.wikimodel.wem.IWikiPrinter;
import org.wikimodel.wem.PrintTextListener;
import org.wikimodel.wem.WikiParameters;

public class XWikiSerializer extends PrintTextListener {

    public XWikiSerializer(IWikiPrinter printer) {
        super(printer);
    }

    public void beginHeader(int level, WikiParameters params) {
        println(getEol());
        print("1");
        for (int i = 0; i < level - 1; i++) {
            print(".1");
        }
        print(" ");

    }

    public void beginListItem() {
        print("* ");

    }

    public void beginTable(WikiParameters params) {
        println("{table}");

    }

    public void beginTableRow(WikiParameters params) {
        println("");

    }

    public void endHeader(int level, WikiParameters params) {
        println("");
        println("");

    }

    public void endListItem() {
        println("");

    }

    public void endTable(WikiParameters params) {
        println("{table}");

    }

    public void endTableCell(boolean tableHead, WikiParameters params) {
        println("|");

    }

    protected String getEol() {
        return "\n";
    }

    public void onHorizontalLine() {
        println("----");

    }

    public void onLineBreak() {
        println("");
        println("");

    }

    public void onNewLine() {
        println("");

    }

    public void onReference(String ref, boolean explicitLink) {
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

    public void onSpace(String str) {
        print(str);

    }

    public void onSpecialSymbol(String str) {
        print(str);

    }

    public void onTableCaption(String str) {
        println(str);

    }

    public void onVerbatimInline(String str) {
        println("{code}");
        println(str);
        println("{code}");

    }

    public void onWord(String str) {
        print(str);

    }

}
