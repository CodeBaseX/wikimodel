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
        System.out.println(writer);
    }

}