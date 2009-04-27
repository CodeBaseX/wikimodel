/**
 * 
 */
package org.wikimodel.wem.examples;

import java.io.Reader;
import java.io.StringReader;

import org.wikimodel.wem.IWemListener;
import org.wikimodel.wem.IWikiParser;
import org.wikimodel.wem.IWikiPrinter;
import org.wikimodel.wem.WikiPageUtil;
import org.wikimodel.wem.WikiParameters;
import org.wikimodel.wem.WikiParserException;
import org.wikimodel.wem.WikiReference;
import org.wikimodel.wem.confluence.ConfluenceWikiParser;
import org.wikimodel.wem.xhtml.PrintListener;

/**
 * @author kotelnikov
 */
public class XHTMLSerializationExample {

    public static void main(String[] args) throws WikiParserException {
        Reader reader = new StringReader(""
            + "h1. Hello, world\n"
            + "* list item 1\n"
            + "* list item 2\n"
            + "\n"
            + "||Table header|Table cell\n"
            + "||Table header|Table cell\n"
            + "\n"
            + "Paragraph with a [reference|http://www.google.com]...");

        // You can instantiate here any parser you want, like
        // CommonSyntaxParser, JspWikiParser, XWikiParser, ...
        IWikiParser parser = new ConfluenceWikiParser();
        IWikiPrinter printer = new IWikiPrinter() {
            public void print(String str) {
                System.out.print(str);
            }

            public void println(String str) {
                System.out.println(str);
            }
        };
        // Default XHTML print listener
        IWemListener serializer = new PrintListener(printer) {
            @Override
            public void onReference(WikiReference ref) {
                String link = ref.getLink();
                String label = ref.getLabel();
                if (label == null)
                    label = link;
                WikiParameters params = ref.getParameters();
                print("<A HREF='"
                    + WikiPageUtil.escapeXmlAttribute(link)
                    + "'"
                    + params
                    + ">"
                    + WikiPageUtil.escapeXmlString(label)
                    + "</A>");
            }
        };
        parser.parse(reader, serializer);
    }

}
