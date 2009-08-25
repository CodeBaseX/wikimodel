/**
 * 
 */
package org.wikimodel.wem.util.tmp;

import junit.framework.TestCase;

import org.wikimodel.wem.util.tmp.TreeBuilder1.ITreeBuilderListener;

/**
 * @author kotelnikov
 */
public class TreeBuilder1Test extends TestCase {

    final StringBuffer fBuf = new StringBuffer();

    TreeBuilder1<String> fBuilder;

    /**
     * 
     */
    public TreeBuilder1Test() {
    }

    /**
     * @param name
     */
    public TreeBuilder1Test(String name) {
        super(name);
    }

    /**
     * @param control
     */
    private void check(String control) {
        assertEquals(control, fBuf.toString());
        fBuf.delete(0, fBuf.length());
    }

    @Override
    protected void setUp() throws Exception {
        ITreeBuilderListener<String> listener = new ITreeBuilderListener<String>() {
            public void beginItem(int depth, String data) {
                fBuf.append("<" + data + ">");
            }

            public void beginLevel(int depth, String prevBegin) {
                fBuf.append("<level>");
            }

            public void endItem(int depth, String data) {
                fBuf.append("</" + data + ">");
            }

            public void endLevel(int i, String prevBegin) {
                fBuf.append("</level>");
            }

        };
        fBuilder = new TreeBuilder1<String>(listener);
    }

    public void test() throws Exception {
        test(0, "a", "<level><a>");
        test(30, "b", "<level><b>");
        test(10, "c", "</b><c>");

        test(30, "d", "<level><d>");
        test(15, "e", "</d><e>");
        test(8, "f", "</e></level></c><f>");
        test(8, "g", "</f><g>");

        fBuilder.trim(10);
        check("");
        fBuilder.trim(8, false);
        check("");
        fBuilder.finish();
        check("</g></level></a></level>");
    }

    /**
     * @param value
     * @param control
     * @param string
     */
    private void test(int value, String data, String control) {
        fBuilder.align(value, data);
        check(control);
    }

}
