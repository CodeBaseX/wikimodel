package org.wikimodel.wem.util;

import junit.framework.TestCase;

/**
 * @author MikhailKotelnikov
 */
public class ListBuilderTest extends TestCase {

    /**
     * @param name
     */
    public ListBuilderTest(String name) {
        super(name);
    }

    public void testTwo() throws Exception {
        testTwo("a", "<A><a></a></A>");
        testTwo("a\na", "<A><a></a><a></a></A>");
        testTwo("a\n a", "<A><a><A><a></a></A></a></A>");
        testTwo("a\n a\n a", "<A><a><A><a></a><a></a></A></a></A>");
        testTwo("a\n a\n a\n a", "<A><a>"
            + "<A>"
            + "<a></a>"
            + "<a></a>"
            + "<a></a>"
            + "</A>"
            + "</a></A>");
        testTwo("a\n b\n b\n b", "<A><a>"
            + "<B>"
            + "<b></b>"
            + "<b></b>"
            + "<b></b>"
            + "</B>"
            + "</a></A>");
        testTwo("a\n b\n b\n b\na", "<A>"
            + "<a>"
            + "<B>"
            + "<b></b>"
            + "<b></b>"
            + "<b></b>"
            + "</B>"
            + "</a>"
            + "<a></a>"
            + "</A>");
        testTwo("a\nab\nabc\nabcd", "<A><a>"
            + "<B><b>"
            + "<C><c>"
            + "<D><d></d></D>"
            + "</c></C>"
            + "</b></B>"
            + "</a></A>");
        testTwo("a\nab\nabc", "<A><a>"
            + "<B><b>"
            + "<C><c></c></C>"
            + "</b></B>"
            + "</a></A>");
        testTwo(""
            + "            a\n"
            + "       a   b\n"
            + "  a c\n"
            + " a c  d\n"
            + " e"
            + ""
            + ""
            + ""
            + ""
            + "", "<A><a>"
            + "<B><b></b></B>"
            + "<C><c>"
            + "<D><d></d></D>"
            + "</c></C>"
            + "</a></A>"
            + "<E><e></e></E>");

        testTwo("a\n b", "<A><a><B><b></b></B></a></A>");
        testTwo("a\nab", "<A><a><B><b></b></B></a></A>");
        testTwo("a\n         ", "<A><a></a></A>");
        testTwo(" ", "");
        testTwo("", "");
        testTwo("              ", "");
        testTwo("         a", "<A><a></a></A>");
        testTwo("     a     \n         ", "<A><a></a></A>");
        testTwo(""
            + " a\n"
            + "  b\n"
            + "  c\n"
            + "   d\n"
            + " e"
            + ""
            + ""
            + ""
            + ""
            + "", "<A><a>"
            + "<B><b></b></B>"
            + "<C><c>"
            + "<D><d></d></D>"
            + "</c></C>"
            + "</a></A>"
            + "<E><e></e></E>");
        testTwo(""
            + "a\n"
            + "ab\n"
            + "ac\n"
            + "acd\n"
            + "e"
            + ""
            + ""
            + ""
            + ""
            + "", "<A><a>"
            + "<B><b></b></B>"
            + "<C><c>"
            + "<D><d></d></D>"
            + "</c></C>"
            + "</a></A>"
            + "<E><e></e></E>");
        testTwo(""
            + "            a\n"
            + "       a   b\n"
            + "  a c\n"
            + " a c  d\n"
            + " e"
            + ""
            + ""
            + ""
            + ""
            + "", "<A><a>"
            + "<B><b></b></B>"
            + "<C><c>"
            + "<D><d></d></D>"
            + "</c></C>"
            + "</a></A>"
            + "<E><e></e></E>");

        testTwo("" + "    a\n" + "  b\n" + " c\n" + "cd\n" + "", ""
            + "<A><a></a></A>"
            + "<B><b></b></B>"
            + "<C><c>"
            + "<D><d></d></D>"
            + "</c></C>");
    }

    private void testTwo(String string, String control) {
        final StringBuffer buf = new StringBuffer();
        IListListener listener = new IListListener() {

            public void beginRow(char treeType, char rowType) {
                openTag(rowType);
            }

            public void beginTree(char type) {
                openTag(Character.toUpperCase(type));
            }

            private void closeTag(char ch) {
                buf.append("</").append(ch).append(">");
            }

            public void endRow(char treeType, char rowType) {
                closeTag(rowType);
            }

            public void endTree(char type) {
                closeTag(Character.toUpperCase(type));
            }

            private void openTag(char str) {
                buf.append("<").append(str).append(">");
            }
        };
        ListBuilder builder = new ListBuilder(listener);
        String[] lines = string.split("\n");
        for (String s : lines) {
            builder.alignContext(s);
        }
        builder.alignContext("");
        // builder.finish();
        assertEquals(control, buf.toString());
    }

}
