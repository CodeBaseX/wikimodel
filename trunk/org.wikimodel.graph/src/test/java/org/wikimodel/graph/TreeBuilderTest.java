/**
 * 
 */
package org.wikimodel.graph;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

/**
 * This class is used to re-create trees by individual node paths.
 * 
 * @author kotelnikov
 */
public class TreeBuilderTest extends TestCase {

    static class NodeItem {

        public static void addItems(List list, char[] line) {
            int len = 0;
            for (int i = 0; i < line.length; i++) {
                char ch = line[i];
                if (Character.isSpaceChar(ch)) {
                    len++;
                } else {
                    list.add(new NodeItem(len, ch));
                    len = 0;
                }
            }
        }

        final char ch;

        final int len;

        private NodeItem(int len, char ch) {
            this.len = len;
            this.ch = ch;
        }

        public String toString() {
            return "" + ch;
        }

    }

    /**
     * @param name
     */
    public TreeBuilderTest(String name) {
        super(name);
    }

    private List[] getPaths(String str) {
        List pathList = new ArrayList();
        String[] lines = str.split("\n");
        boolean empty = false;
        for (int l = 0; l < lines.length; l++) {
            String line = lines[l];
            char[] array = line.toCharArray();
            List path = new ArrayList();
            for (int i = 0; i < array.length; i++) {
                path.add("" + array[i]);
            }
            pathList.add(path);
            empty = path.isEmpty();
        }
        if (!empty)
            pathList.add(new ArrayList());
        List[] result = new List[pathList.size()];
        result = (List[]) pathList.toArray(result);
        return result;
    }

    private INodeWalkerListener newListener(final StringBuffer buf) {
        final INodeWalkerListener listener = new INodeWalkerListener() {
            public void beginNode(Object parent, Object node) throws Exception {
                buf.append("<" + node + ">");
            }

            public void endNode(Object parent, Object node) throws Exception {
                buf.append("</" + node + ">");
            }
        };
        return listener;
    }

    public void testOne() throws Exception {
        testOne("", "");
        testOne("a", "<a></a>");
        testOne("a\n b", "<a><b></b></a>");
        testOne("a\nb\n c", "<a></a><b><c></c></b>");
        testOne(" a\n b\n  c", "<a></a><b><c></c></b>");
        testOne("  a\n     b", "<a><b></b></a>");
        testOne("  a\n  b", "<a></a><b></b>");
        testOne("     a\n  b", "<a></a><b></b>");
        testOne("  b\n    c", "<b><c></c></b>");
        testOne("  b\n    c\n   d", "<b><c></c><d></d></b>");
        testOne("  b\n    c\n   d\n    c", "<b><c></c><d><c></c></d></b>");
    }

    private void testOne(String str, String control) throws Exception {
        final StringBuffer buf = new StringBuffer();
        final INodeWalkerListener listener = newListener(buf);
        TreeBuilder builder = new TreeBuilder() {

            protected boolean equal(Object first, Object second) {
                NodeItem firstItem = (NodeItem) first;
                NodeItem secondItem = (NodeItem) second;
                return firstItem.ch == secondItem.ch;
            }

            protected int getLength(Object node) {
                return ((NodeItem) node).len;
            }
        };

        String[] lines = str.split("\n");
        for (int l = 0; l < lines.length; l++) {
            List list = new ArrayList();
            String line = lines[l];
            NodeItem.addItems(list, line.toCharArray());
            builder.align(list, listener);
        }

        List list = new ArrayList();
        builder.align(list, listener);
        assertEquals(control, buf.toString());
    }

    public void testTwo() throws Exception {
        testTwo("", "");
        testTwo("a", "<a></a>");
        testTwo("a", "<a></a>");
        testTwo("a\nb", "<a></a><b></b>");
        testTwo("ab", "<a><b></b></a>");
        testTwo("a\nab", "<a><b></b></a>");
        testTwo("abc", "<a><b><c></c></b></a>");
        testTwo("abc\nabc\nabd", "<a><b><c></c><c></c><d></d></b></a>");
        testTwo("abc\nabc", "<a><b><c></c><c></c></b></a>");
        testTwo("c\nc\ncd", "<c></c><c><d></d></c>");
        testTwo(
            "abc\nabc\nabcd\nabc",
            "<a><b><c></c><c><d></d></c><c></c></b></a>");
        testTwo("abc\naXY", "<a><b><c></c></b><X><Y></Y></X></a>");
        testTwo("abc\nabc", "<a><b><c></c><c></c></b></a>");
        testTwo("ab\na", "<a><b></b></a><a></a>");
        testTwo("abc\na", "<a><b><c></c></b></a><a></a>");
    }

    private void testTwo(String str, String control) throws Exception {
        StringBuffer buf = new StringBuffer();
        INodeWalkerListener listener = newListener(buf);
        TreeBuilder builder = new TreeBuilder() {
            protected boolean equal(Object first, Object second) {
                if (" ".equals(first) || " ".equals(second))
                    return true;
                return super.equal(first, second);
            }
        };
        List[] paths = getPaths(str);
        for (int i = 0; i < paths.length; i++) {
            builder.align(paths[i], listener);
        }
        assertEquals(control, buf.toString());
    }

}
