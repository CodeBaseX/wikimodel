/**
 * 
 */
package org.wikimodel.wem.util;

import junit.framework.TestCase;

/**
 * @author kotelnikov
 */
public class SectionBuilderTest extends TestCase {

    protected static class PrintListener implements ISectionListener<String> {

        final StringBuffer fBuf = new StringBuffer();

        public void beginDocument(IPos<String> pos) {
            fBuf.append("<doc" + n(pos) + ">");
        }

        public void beginSection(IPos<String> pos) {
            fBuf.append("<s" + n(pos) + ">");
        }

        public void beginSectionContent(IPos<String> pos) {
            fBuf.append("<c" + n(pos) + ">");
        }

        public void beginSectionHeader(IPos<String> pos) {
            fBuf.append("<h" + n(pos) + ">");
        }

        public void endDocument(IPos<String> pos) {
            fBuf.append("</doc" + n(pos) + ">");
        }

        public void endSection(IPos<String> pos) {
            fBuf.append("</s" + n(pos) + ">");
        }

        public void endSectionContent(IPos<String> pos) {
            fBuf.append("</c" + n(pos) + ">");
        }

        public void endSectionHeader(IPos<String> pos) {
            fBuf.append("</h" + n(pos) + ">");
        }

        private String n(IPos<String> pos) {
            return "[" + pos.getData() + "]" + "-" + pos.getDocumentLevel()
                    + "-" + pos.getHeaderLevel();
        }

        @Override
        public String toString() {
            return fBuf.toString();
        }

    }

    SectionBuilder<String> fBuilder;

    private PrintListener fListener;

    /**
     * @param name
     */
    public SectionBuilderTest(String name) {
        super(name);
    }

    private String check(String prev, String delta) {
        String test = fListener.toString();
        assertEquals(prev + delta, test);
        return test;
    }

    @Override
    protected void setUp() throws Exception {
        fListener = new PrintListener();
        fBuilder = new SectionBuilder<String>(fListener);
    }

    public void testDocumentLevels() {
        String prev = "";

        fBuilder.beginDocument("X");
        prev = check(prev, "<doc[X]-1-0>");
        fBuilder.beginDocument("Y");
        prev = check(prev, "<doc[Y]-2-0>");
        fBuilder.endDocument();
        prev = check(prev, "</doc[Y]-2-0>");
        fBuilder.endDocument();
        prev = check(prev, "</doc[X]-1-0>");
    }

    public void testHeaderLevels() {

        fBuilder.beginDocument("X");
        String prev = "";
        prev = check(prev, "<doc[X]-1-0>");

        fBuilder.beginHeader(1, "A");
        prev = check(prev, "<s[A]-1-1><h[A]-1-1>");
        fBuilder.endHeader();
        prev = check(prev, "</h[A]-1-1><c[A]-1-1>");

        fBuilder.beginHeader(3, "B");
        prev = check(prev, "<s[B]-1-2><c[B]-1-2><s[B]-1-3><h[B]-1-3>");
        fBuilder.endHeader();
        prev = check(prev, "</h[B]-1-3><c[B]-1-3>");

        fBuilder.beginHeader(3, "C");
        prev = check(prev, "</c[B]-1-3></s[B]-1-3><s[C]-1-3><h[C]-1-3>");
        fBuilder.endHeader();
        prev = check(prev, "</h[C]-1-3><c[C]-1-3>");

        fBuilder.beginHeader(2, "D");
        prev = check(prev,
            "</c[C]-1-3></s[C]-1-3></c[B]-1-2></s[B]-1-2><s[D]-1-2><h[D]-1-2>");
        fBuilder.endHeader();
        prev = check(prev, "</h[D]-1-2><c[D]-1-2>");

        fBuilder.beginHeader(3, "E");
        prev = check(prev, "<s[E]-1-3><h[E]-1-3>");
        fBuilder.endHeader();
        prev = check(prev, "</h[E]-1-3><c[E]-1-3>");

        fBuilder.endDocument();
        prev = check(prev, "</c[E]-1-3></s[E]-1-3>" + "</c[D]-1-2></s[D]-1-2>"
                + "</c[A]-1-1></s[A]-1-1>" + "</doc[X]-1-0>");
    }

    public void testWithoutLevels() {
        fBuilder.beginDocument("X");
        String prev = "";
        prev = check(prev, "<doc[X]-1-0>");

        fBuilder.beginHeader(1, "A");
        prev = check(prev, "<s[A]-1-1><h[A]-1-1>");
        fBuilder.endHeader();
        prev = check(prev, "</h[A]-1-1><c[A]-1-1>");

        fBuilder.beginHeader(3, "B");
        prev = check(prev, "<s[B]-1-2><c[B]-1-2><s[B]-1-3><h[B]-1-3>");
        fBuilder.endHeader();
        prev = check(prev, "</h[B]-1-3><c[B]-1-3>");

        fBuilder.beginHeader(3, "C");
        prev = check(prev, "</c[B]-1-3></s[B]-1-3><s[C]-1-3><h[C]-1-3>");
        fBuilder.endHeader();
        prev = check(prev, "</h[C]-1-3><c[C]-1-3>");

        fBuilder.beginHeader(2, "D");
        prev = check(prev,
            "</c[C]-1-3></s[C]-1-3></c[B]-1-2></s[B]-1-2><s[D]-1-2><h[D]-1-2>");
        fBuilder.endHeader();
        prev = check(prev, "</h[D]-1-2><c[D]-1-2>");

        fBuilder.beginHeader(3, "E");
        prev = check(prev, "<s[E]-1-3><h[E]-1-3>");
        fBuilder.endHeader();
        prev = check(prev, "</h[E]-1-3><c[E]-1-3>");

        fBuilder.endDocument();
        prev = check(prev, "</c[E]-1-3></s[E]-1-3>" + "</c[D]-1-2></s[D]-1-2>"
                + "</c[A]-1-1></s[A]-1-1>" + "</doc[X]-1-0>");

    }

    public void testInSubDocument() {
        String prev = "";
        fBuilder.beginDocument("X");
        prev = check(prev, "<doc[X]-1-0>");

        fBuilder.beginHeader(1, "A");
        prev = check(prev, "<s[A]-1-1><h[A]-1-1>");
        fBuilder.endHeader();
        prev = check(prev, "</h[A]-1-1><c[A]-1-1>");

        // BEGIN DOCUMENT Y [

        fBuilder.beginDocument("Y");
        prev = check(prev, "<doc[Y]-2-0>");

        fBuilder.beginHeader(1, "YA");
        prev = check(prev, "<s[YA]-2-1><h[YA]-2-1>");
        fBuilder.endHeader();
        prev = check(prev, "</h[YA]-2-1><c[YA]-2-1>");

        fBuilder.endDocument();
        prev = check(prev, "</c[YA]-2-1></s[YA]-2-1></doc[Y]-2-0>");

        // ] END DOCUMENT Y

        fBuilder.beginHeader(2, "B");
        prev = check(prev, "<s[B]-1-2><h[B]-1-2>");
        fBuilder.endHeader();
        prev = check(prev, "</h[B]-1-2><c[B]-1-2>");

        fBuilder.endDocument();
        prev = check(prev, "</c[B]-1-2></s[B]-1-2>" + "</c[A]-1-1></s[A]-1-1>"
                + "</doc[X]-1-0>");
    }
}
