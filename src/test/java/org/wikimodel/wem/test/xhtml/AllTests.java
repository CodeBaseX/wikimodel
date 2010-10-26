package org.wikimodel.wem.test.xhtml;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

    public static Test suite() {
        TestSuite suite = new TestSuite("Test for org.wikimodel.wem.test.xhtml");
        // $JUnit-BEGIN$
        suite.addTestSuite(XHTMLWhitespaceXMLFilterTest.class);
        suite.addTestSuite(XHtmlParserTest.class);
        // $JUnit-END$
        return suite;
    }

}
