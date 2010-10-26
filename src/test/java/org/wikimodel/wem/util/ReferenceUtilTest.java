/**
 * 
 */
package org.wikimodel.wem.util;

import junit.framework.TestCase;

import org.wikimodel.wem.WikiParameters;
import org.wikimodel.wem.WikiReference;
import org.wikimodel.wem.WikiReferenceParser;

/**
 * @author kotelnikov
 */
public class ReferenceUtilTest extends TestCase {

    public ReferenceUtilTest(String name) {
        super(name);
    }

    public void test() {
        test("a", "a");
        test("   a   ", "a");
        test("a|b", "a", "b");
        test("   a |   b   ", "a", "b");
        test("a>b", "a", "b");
        test("   a >   b   ", "a", "b");
        test("   a   >   b | x=y  ", "a", "b", "x=y");
        test("   a   >   b > x=y  ", "a", "b", "x=y");
        test("   a   >   b > x=y > toto titi ", "a", "b", "x=y");
    }

    private void test(String str, String link) {
        WikiReference ref = new WikiReferenceParser().parse(str);
        assertEquals(new WikiReference(link), ref);
    }

    private void test(String str, String link, String label) {
        WikiReference ref = new WikiReferenceParser().parse(str);
        assertEquals(new WikiReference(link, label), ref);
    }

    private void test(String str, String link, String label, String params) {
        WikiReference ref = new WikiReferenceParser().parse(str);
        assertEquals(
            new WikiReference(link, label, new WikiParameters(params)),
            ref);
    }

}
