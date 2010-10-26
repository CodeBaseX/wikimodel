package org.wikimodel.wem;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

    public static Test suite() {
        TestSuite suite = new TestSuite("Test for org.wikimodel.wem.test.xhtml");
        // $JUnit-BEGIN$
        suite.addTest(org.wikimodel.wem.test.AllTests.suite());
        suite.addTest(org.wikimodel.wem.test.xhtml.AllTests.suite());
        // $JUnit-END$
        return suite;
    }

}
