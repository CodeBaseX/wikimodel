/**
 * 
 */
package org.wikimodel.wem.xml;

import java.io.StringReader;
import java.io.StringWriter;

import org.wikimodel.wem.IWikiParser;
import org.wikimodel.wem.common.CommonWikiParser;
import org.wikimodel.wem.test.AbstractWikiParserTest;
import org.wikimodel.wem.xml.sax.WemReader;

/**
 * @author kotelnikov
 */
public class WemTagNotifierTest extends AbstractWikiParserTest {

    /**
     * @param name
     */
    public WemTagNotifierTest(String name) {
        super(name);
    }

    @Override
    protected IWikiParser newWikiParser() {
        return new CommonWikiParser();
    }

    public void test() throws Exception {
        showSections(true);
        String str = "{{a=b c=d}}\n=Header=\n* item __one__\n* item two";
        StringReader reader = new StringReader(str);

        IWikiParser parser = newWikiParser();
        WemReader xmlReader = new WemReader(parser);
        StringWriter writer = new StringWriter();
        XmlUtil.write(reader, xmlReader, writer);
        String control = "<w:document xmlns:w=\"http://www.wikimodel.org/ns/wem#\" xmlns:u=\"http://www.wikimodel.org/ns/user-defined-params#\">\n"
            + "    <w:section w:absLevel=\"1\" w:docLevel=\"1\" w:headerLevel=\"0\">\n"
            + "        <w:content w:absLevel=\"1\" w:docLevel=\"1\" w:headerLevel=\"0\">\n"
            + "            <w:section w:absLevel=\"2\" w:docLevel=\"1\" w:headerLevel=\"1\" u:a=\"b\" u:c=\"d\">\n"
            + "                <w:header w:level=\"1\" u:a=\"b\" u:c=\"d\">\n"
            + "                    <w:format>Header</w:format>\n"
            + "                </w:header>\n"
            + "                <w:content w:absLevel=\"2\" w:docLevel=\"1\" w:headerLevel=\"1\" u:a=\"b\" u:c=\"d\">\n"
            + "                    <w:ul>\n"
            + "                        <w:li>\n"
            + "                            <w:format>item </w:format>\n"
            + "                            <w:format w:styles=\"em\">one</w:format>\n"
            + "                        </w:li>\n"
            + "                        <w:li>\n"
            + "                            <w:format>item two</w:format>\n"
            + "                        </w:li>\n"
            + "                    </w:ul>\n"
            + "                </w:content>\n"
            + "            </w:section>\n"
            + "        </w:content>\n"
            + "    </w:section>\n"
            + "</w:document>\n"
            + "";
        String result = writer.toString();
        assertEquals(control, result);
        System.out.println(writer);
    }

}