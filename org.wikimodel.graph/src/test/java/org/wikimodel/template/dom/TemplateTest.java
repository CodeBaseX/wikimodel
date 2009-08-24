/**
 * 
 */
package org.wikimodel.template.dom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.w3c.dom.Document;
import org.wikimodel.template.IXmlTemplateListener;
import org.wikimodel.template.PrintTemplateListener;

/**
 * @author kotelnikov
 */
public class TemplateTest extends TestCase {

    /**
     * @param name
     */
    public TemplateTest(String name) {
        super(name);
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> map(String... values) {
        Map<String, Object> map = new HashMap<String, Object>();
        for (int i = 0; i < values.length; i++) {
            String key = values[i++];
            String value = i < values.length ? values[i] : null;
            Object old = map.get(key);
            if (old != null) {
                List<Object> list = null;
                if (old instanceof List<?>) {
                    list = (List<Object>) old;
                } else {
                    list = new ArrayList<Object>();
                    list.add(old);
                    map.put(key, list);
                }
                list.add(value);
            } else {
                map.put(key, value);
            }
        }
        return map;
    }

    /**
     * @param buf
     * @return
     */
    private PrintTemplateListener newPrintListener(final StringBuffer buf) {
        return new PrintTemplateListener() {
            @Override
            protected void print(String string) {
                buf.append(string);
            }
        };
    }

    public void test() throws Exception {
        test(
            "" + "<div><h1 st-value='title'>xyz</h1></div>",
            "<div />",
            "<div><h1></h1></div>");
        test(""
            + "<div>before[<div st-select='items'>xyz</div>]after"
            + "</div>", "<div />", "<div>before[]after</div>");
        test(""
            + "<div>\n<h1 st-value='title'>X</h1>\n"
            + "before[<div st-select='items'></div>]after\n"
            + "</div>", "<div><title>Title</title></div>", "<div>\n"
            + "<h1>Title</h1>\n"
            + "before[]after\n"
            + "</div>");
    }

    /**
     * @param templateStr
     * @param data
     * @param control
     * @throws Exception
     */
    private void test(String templateStr, Object data, String control)
        throws Exception {
        Document template = DOMUtil.readXML(templateStr);
        DomTemplateEngine engine = new DomTemplateEngine();
        final StringBuffer buf = new StringBuffer();
        IXmlTemplateListener listener = newPrintListener(buf);
        engine.process(template.getDocumentElement(), data, listener);
        assertEquals(control, buf.toString());
    }

    /**
     * @param templateStr
     * @param dataStr
     * @throws Exception
     */
    private void test(String templateStr, String dataStr, String control)
        throws Exception {
        Document dataDoc = DOMUtil.readXML(dataStr);
        Object data = dataDoc.getDocumentElement();
        test(templateStr, data, control);
    }

    public void testMapData() throws Exception {
        String templateStr = ""
            + "<div>\n"
            + "     <h1 st-value='title'>This is a <em>test</em> title</h1>\n\n"
            + "     <div st-value='content'>Test content</div>\n\n"
            + "     <div st-iterate='users'>\n"
            + "         <h2 st-value='name'>John Smith</h2>\n"
            + "         <div st-select='mail'>\n"
            + "             <p>Mails</p>\n"
            + "             <ul>\n"
            + "                 <li st-iterate='$this'>[<a st-value='$this'>this.is.a@mail.com</a>]</li>\n"
            + "             </ul>\n"
            + "         </div>\n"
            + "     </div>\n"
            + "</div>";

        Map<String, Object> data = map("titl", "This is the REAL title");
        data.put("title", "This is the REAL title");
        List<Object> list = new ArrayList<Object>();
        list.add(map("name", "Micha"));
        list.add(map(
            "name",
            "Alex",
            "mail",
            "foo@toto.com",
            "mail",
            "bar@toto.com"));
        list.add(map("name", "Renaud", "mail", "foo@titi.com"));
        data.put("users", list);
        data.put("content", "<h1>Hello</h1><p>This is the content</p>");

        String control = "<div>\n"
            + "     <h1>This is the REAL title</h1>\n"
            + "\n"
            + "     <div><h1>Hello</h1><p>This is the content</p></div>\n"
            + "\n"
            + "     <div>\n"
            + "         <h2>Micha</h2>\n"
            + "         \n"
            + "     </div><div>\n"
            + "         <h2>Alex</h2>\n"
            + "         <div>\n"
            + "             <p>Mails</p>\n"
            + "             <ul>\n"
            + "                 <li>[<a>foo@toto.com</a>]</li><li>[<a>bar@toto.com</a>]</li>\n"
            + "             </ul>\n"
            + "         </div>\n"
            + "     </div><div>\n"
            + "         <h2>Renaud</h2>\n"
            + "         <div>\n"
            + "             <p>Mails</p>\n"
            + "             <ul>\n"
            + "                 <li>[<a>foo@titi.com</a>]</li>\n"
            + "             </ul>\n"
            + "         </div>\n"
            + "     </div>\n"
            + "</div>";

        test(templateStr, data, control);

        Document template = DOMUtil.readXML(templateStr);
        DomTemplateEngine engine = new DomTemplateEngine();
        StringBuffer buf = new StringBuffer();
        IXmlTemplateListener listener = newPrintListener(buf);
        engine.process(template.getDocumentElement(), data, listener);
        assertEquals(control, buf.toString());
    }

    public void testXml() throws Exception {
        test(
            "<div><h1 st-value='a'/></div>",
            "<div><a>A</a></div>",
            "<div><h1>A</h1></div>");
        test(
            "<div><h1 st-value='title'/><div st-value='content'/></div>",
            "<div><title>Title</title><content>Content</content></div>",
            "<div><h1>Title</h1><div>Content</div></div>");
        test(""
            + "<div>\n"
            + "<h1 st-value='title'>X</h1>\n"
            + "before[<div st-select='items'></div>]after\n"
            + "</div>", "<div><title>Title</title></div>", "<div>\n"
            + "<h1>Title</h1>\n"
            + "before[]after\n"
            + "</div>");
        String userTemplate = ""
            + "<div>\n"
            + "<h1 st-value='title'></h1>\n"
            + "<div st-value='content'>Content</div>\n"
            + "<div st-select='users'>\n"
            + " <h2>A list of users</h2>\n"
            + " <div st-select='user'>\n"
            + "     <h3 st-value='name'>John Smith</h3>\n"
            + "     <div st-select='email'>\n"
            + "         <h4>Emails</h4>\n"
            + "         <ul>\n"
            + "             <li st-iterate='$this'><a href='' st-attribute='href:$this' st-value='$this'></a></li>\n"
            + "         </ul>\n"
            + "     </div>\n"
            + " </div>\n"
            + "</div>\n"
            + "</div>";

        test(userTemplate, "<div><title>Title</title></div>", "<div>\n"
            + "<h1>Title</h1>\n"
            + "<div></div>\n"
            + "\n"
            + "</div>");
        test(userTemplate, "<div>"
            + "<title>Title</title>"
            + "<content>Content</content>"
            + "<users>"
            + " <user><name>Alex</name></user>"
            + "</users>"
            + "</div>", "<div>\n"
            + "<h1>Title</h1>\n"
            + "<div>Content</div>\n"
            + "<div>\n"
            + " <h2>A list of users</h2>\n"
            + " <div>\n"
            + "     <h3>Alex</h3>\n"
            + "     \n"
            + " </div>\n"
            + "</div>\n"
            + "</div>");
        test(
            userTemplate,
            "<div>"
                + "<title>Title</title>"
                + "<content>Content</content>"
                + "<users>"
                + " <user>"
                + "     <name>Alex</name>"
                + "     <email>alex@test.com</email>"
                + "</user>"
                + "</users>"
                + "</div>",
            "<div>\n"
                + "<h1>Title</h1>\n"
                + "<div>Content</div>\n"
                + "<div>\n"
                + " <h2>A list of users</h2>\n"
                + " <div>\n"
                + "     <h3>Alex</h3>\n"
                + "     <div>\n"
                + "         <h4>Emails</h4>\n"
                + "         <ul>\n"
                + "             <li><a href='alex@test.com'>alex@test.com</a></li>\n"
                + "         </ul>\n"
                + "     </div>\n"
                + " </div>\n"
                + "</div>\n"
                + "</div>");
        test(userTemplate, "<div>"
            + "<title>Title</title>"
            + "<content>Content</content>"
            + "<users>"
            + " <user>"
            + "     <name>Alex</name>"
            + "     <email>alex@test.com</email>"
            + "     <email>toto@titi.com</email>"
            + "</user>"
            + "</users>"
            + "</div>", "<div>\n"
            + "<h1>Title</h1>\n"
            + "<div>Content</div>\n"
            + "<div>\n"
            + " <h2>A list of users</h2>\n"
            + " <div>\n"
            + "     <h3>Alex</h3>\n"
            + "     <div>\n"
            + "         <h4>Emails</h4>\n"
            + "         <ul>\n"
            + "             <li><a href='alex@test.com'>alex@test.com</a></li>"
            + "<li><a href='alex@test.com'>alex@test.com</a></li>\n"
            + "         </ul>\n"
            + "     </div>\n"
            + " </div>\n"
            + "</div>\n"
            + "</div>");
    }
}
