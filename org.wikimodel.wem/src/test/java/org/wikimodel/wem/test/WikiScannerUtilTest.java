package org.wikimodel.wem.test;

import junit.framework.TestCase;

import org.wikimodel.wem.WikiParameter;
import org.wikimodel.wem.WikiParameters;
import org.wikimodel.wem.impl.WikiScannerUtil;

/**
 * @author MikhailKotelnikov
 * @author thomas.mortagne
 */
public class WikiScannerUtilTest extends TestCase {

    /**
     * @param name
     */
    public WikiScannerUtilTest(String name) {
        super(name);
    }

    public void testParams() {
        testParams("a b c d", " ", "a", null, "b", null, "c", null, "d", null);
        testParams("a=b c=d", " ", "a", "b", "c", "d");
        testParams("a=b", "|", "a", "b");
        testParams(" a = b ", "|", "a", "b");
        testParams(" a = b c ", "|", "a", "b c");
        testParams("a=b|c=d", "|", "a", "b", "c", "d");
        testParams("a b c d", "|", "a b c d", null);
        testParams("x=b d e | y= f g h ", "|", "x", "b d e", "y", "f g h");
        testParams(" x = b d e | y = f g h ", "|", "x", "b d e", "y", "f g h");
        testParams(
            " x = ' b d e ' | y = ' f g h ' ",
            "|",
            "x",
            " b d e ",
            "y",
            " f g h ");
        testParams(
            "   x    =    ' b d e '     y =    ' f g h '    ",
            " ",
            "x",
            " b d e ",
            "y",
            " f g h ");
    }

    private void testParams(String str, String delim, String... pairs) {
        WikiParameters params = new WikiParameters(str, delim);
        int size = params.getSize();
        assertEquals(pairs.length / 2, size);
        for (int i = 0; i < size; i++) {
            String key = pairs[i * 2];
            String value = pairs[i * 2 + 1];
            WikiParameter pair = params.getParameter(i);
            assertNotNull(pair);
            assertEquals(key, pair.getKey());
            assertEquals(value, pair.getValue());
        }
    }

    /**
     * 
     */
    public void testSubstringExtract() {
        testSubstringExtract1("123", "");
        testSubstringExtract1("123()", "");
        testSubstringExtract1("()", "");
        testSubstringExtract1("(abc)", "abc");
        testSubstringExtract1("123(abc)456", "abc");
        testSubstringExtract1("123(a\\(b\\)c)456", "a(b)c");
        testSubstringExtract1("123(a\\(b\\)c)456", "a\\(b\\)c", false);

        testSubstringExtract2("123{{}}", "");
        testSubstringExtract2("{{}}", "");
        testSubstringExtract2("{{abc}}", "abc");
        testSubstringExtract2("123{{abc}}456", "abc");
        testSubstringExtract2("123{{a\\(b\\)c}}456", "a(b)c");
        testSubstringExtract2("123{{a\\(b\\)c}}456", "a\\(b\\)c", false);
        testSubstringExtract2("123{{a\\{{b\\}}c}}456", "a{{b}}c");
        testSubstringExtract2("123{{a\\{{b\\}}c}}456", "a\\{{b\\}}c", false);
    }

    private void testSubstringExtract1(String str, String result) {
        String test = WikiScannerUtil.extractSubstring(str, "(", ")", '\\');
        assertEquals(result, test);
    }

    private void testSubstringExtract1(
        String str,
        String result,
        boolean cleanEscape) {
        String test = WikiScannerUtil.extractSubstring(
            str,
            "(",
            ")",
            '\\',
            cleanEscape);
        assertEquals(result, test);
    }

    private void testSubstringExtract2(String str, String result) {
        String test = WikiScannerUtil.extractSubstring(str, "{{", "}}", '\\');
        assertEquals(result, test);
    }

    private void testSubstringExtract2(
        String str,
        String result,
        boolean cleanEscape) {
        String test = WikiScannerUtil.extractSubstring(
            str,
            "{{",
            "}}",
            '\\',
            cleanEscape);
        assertEquals(result, test);
    }
}
