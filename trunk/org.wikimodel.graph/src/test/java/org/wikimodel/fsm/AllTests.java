package org.wikimodel.fsm;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.wikimodel.fsm.grammar.GrammarTest;
import org.wikimodel.graph.NodeWalkerTest;
import org.wikimodel.graph.TreeBuilderTest;
import org.wikimodel.iterator.MergeIteratorTest;

public class AllTests {

    public static Test suite() {
        TestSuite suite = new TestSuite("Test for org.wikimodel.fsm");
        // $JUnit-BEGIN$
        suite.addTestSuite(FsmProcessTest.class);
        suite.addTestSuite(GrammarTest.class);
        suite.addTestSuite(NodeWalkerTest.class);
        suite.addTestSuite(TreeBuilderTest.class);
        suite.addTestSuite(MergeIteratorTest.class);
        // $JUnit-END$
        return suite;
    }

}
