/*******************************************************************************
 * Copyright (c) 2005,2007 Cognium Systems SA and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Contributors:
 *     Cognium Systems SA - initial API and implementation
 *******************************************************************************/
package org.wikimodel.wem.test;

import java.io.StringReader;

import org.wikimodel.wem.IWemListener;
import org.wikimodel.wem.IWikiParser;
import org.wikimodel.wem.IWikiPrinter;
import org.wikimodel.wem.WikiParserException;
import org.wikimodel.wem.xhtml.PrintListener;
import org.wikimodel.wem.xwiki.XWikiParser;

/**
 * @author MikhailKotelnikov
 */
public class XWikiParserTest extends AbstractWikiParserTest {

    /**
     * @param name
     */
    public XWikiParserTest(String name) {
        super(name);
    }

    /**
     * @param string
     * @param control
     * @throws WikiParserException
     */
    private void doTest(String string, String control)
        throws WikiParserException {
        println("==================================================");
        StringReader reader = new StringReader(string);
        IWikiParser parser = new XWikiParser();
        final StringBuffer buf = new StringBuffer();

        IWikiPrinter printer = newPrinter(buf);
        IWemListener listener = new PrintListener(printer) {
            @Override
            public void onSpace(String str) {
                print("[" + str + "]");
            }

            @Override
            public void onWord(String str) {
                print("{" + str + "}");
            }
        };
        parser.parse(reader, listener);
        String test = buf.toString();
        println(test);
        checkResults(control, test);
    }

    @Override
    protected IWikiParser newWikiParser() {
        return new XWikiParser();
    }

    public void test() throws Exception {
        test(
            "before **bold** after",
            "<p>before <strong>bold</strong> after</p>");

        doTest(
            "before **bold** after",
            "<p>{before}[ ]<strong>{bold}</strong>[ ]{after}</p>");
        doTest("before \n* bold after", "<p>{before}[ ]</p>\n"
            + "<ul>\n"
            + "  <li>{bold}[ ]{after}</li>\n"
            + "</ul>"
            + "");
    }

    /**
     * @throws WikiParserException
     */
    public void testDefinitionLists() throws WikiParserException {
        test("; term: definition", "<dl>\n  <dt>term: definition</dt>\n</dl>");
        test(";: just definition", "<dl>\n  <dd>just definition</dd>\n</dl>");
        test("; just term", "<dl>\n  <dt>just term</dt>\n</dl>");
        test(";: ", "<dl>\n  <dd></dd>\n</dl>");

        test(";not: definition", "<p>;not: definition</p>");
        test(
            "; this:is_not_a_term : it is an uri",
            "<dl>\n"
                + "  <dt><a href='this:is_not_a_term'>this:is_not_a_term</a> : it is an uri</dt>\n"
                + "</dl>");

        test("; term one\n: definition one\n"
            + "; term two\n: definition two\n"
            + "; term three\n: definition three", "<dl>\n"
            + "  <dt>term one</dt>\n"
            + "  <dd>definition one</dd>\n"
            + "  <dt>term two</dt>\n"
            + "  <dd>definition two</dd>\n"
            + "  <dt>term three</dt>\n"
            + "  <dd>definition three</dd>\n"
            + "</dl>");

        test(
            "; One,\ntwo,\nbucle my shoes...\n: "
                + "...Three\nfour,\nClose the door\n"
                + "; Five,\nSix\n: Pick up\n sticks\n\ntam-tam, pam-pam...",
            "<dl>\n"
                + "  <dt>One,\n"
                + "two,\n"
                + "bucle my shoes...</dt>\n"
                + "  <dd>...Three\n"
                + "four,\n"
                + "Close the door</dd>\n"
                + "  <dt>Five,\n"
                + "Six</dt>\n"
                + "  <dd>Pick up\n"
                + " sticks</dd>\n"
                + "</dl>\n"
                + "<p>tam-tam, pam-pam...</p>");
    }

    /**
     * @throws WikiParserException
     */
    public void testEscape() throws WikiParserException {
        test("[a reference]");
        test("[[not a reference]");

        test("~First letter is escaped");
        test("~[not a reference]");
        test("~~escaped tilda");
        test("~ just a tilda because there is an espace after this tilda...");

        test("!Heading\n~!Not a heading\n!Heading again!");
    }

    public void testFormat() throws Exception {
        // test("**bold**", "<p><strong>bold</strong></p>");
        test(
            "before **bold** after",
            "<p>before <strong>bold</strong> after</p>");
        test("before //italic// after", "<p>before <em>italic</em> after</p>");
        test(
            "before --strike-- after",
            "<p>before <strike>strike</strike> after</p>");
        test(
            "before __underline__ after",
            "<p>before <ins>underline</ins> after</p>");
        test("before ^^sup^^ after", "<p>before <sup>sup</sup> after</p>");
        test("before ,,sub,, after", "<p>before <sub>sub</sub> after</p>");
        test("before ##mono## after", "<p>before <mono>mono</mono> after</p>");
    }

    /**
     * @throws WikiParserException
     */
    public void testFormats() throws WikiParserException {
        test("(% param1='value1' param2='value2' %)");
        test("xxx (% param1='value1' param2='value2' %) xxx ");
        // (% param3="value3" %)hello(%%) world
        //
        // (% param3="valueA" %)hello (% param3="valueB" %)world

        test("**bold**", "<p><strong>bold</strong></p>");
        test("//italic//", "<p><em>italic</em></p>");
        test("--strike--", "<p><strike>strike</strike></p>");
        test("^^sup^^", "<p><sup>sup</sup></p>");
        test(",,sub,,", "<p><sub>sub</sub></p>");
        test("##mono##", "<p><mono>mono</mono></p>");
    }

    /**
     * @throws WikiParserException
     */
    public void testHeaders() throws WikiParserException {
        test("= Heading 1", "<h1>Heading 1</h1>");
        test("== Heading 2", "<h2>Heading 2</h2>");
        test("=== Heading 3", "<h3>Heading 3</h3>");
        test("==== Heading 4", "<h4>Heading 4</h4>");
        test("===== Heading 5", "<h5>Heading 5</h5>");
        test("====== Heading 6", "<h6>Heading 6</h6>");
        test("= Heading 1 =some", "<h1>Heading 1</h1>\n<p>some</p>");
    }

    /**
     * @throws WikiParserException
     */
    public void testHorLine() throws WikiParserException {
        test("----");
        test("-------");
        test("-----------");
        test(" -----------");
        test("----abc");
        test("(%a=b%)\n----", "<hr a='b' />");
    }

    /**
     * @throws WikiParserException
     */
    public void testLineBreak() throws WikiParserException {
        test("abc\\\\def");
    }

    /**
     * @throws WikiParserException
     */
    public void testLists() throws WikiParserException {
        test("* first", "<ul>\n" + "  <li>first</li>\n" + "</ul>");
        test("** second", "<ul>\n"
            + "  <li><ul>\n"
            + "  <li>second</li>\n"
            + "</ul>\n"
            + "</li>\n"
            + "</ul>");
        test("  ** second", "<ul>\n"
            + "  <li><ul>\n"
            + "  <li>second</li>\n"
            + "</ul>\n"
            + "</li>\n"
            + "</ul>");
        test("* first\n** second", "<ul>\n"
            + "  <li>first<ul>\n"
            + "  <li>second</li>\n"
            + "</ul>\n"
            + "</li>\n"
            + "</ul>");
        test("*1. second", "<ul>\n"
            + "  <li><ol>\n"
            + "  <li>second</li>\n"
            + "</ol>\n"
            + "</li>\n"
            + "</ul>");
        test("  11. second", "<ol>\n"
            + "  <li><ol>\n"
            + "  <li>second</li>\n"
            + "</ol>\n"
            + "</li>\n"
            + "</ol>");

        test("* item one\n"
            + "* item two\n"
            + "*1. item three\n"
            + "*1. item four\n"
            + "* item five - first line\n"
            + "   item five - second line\n"
            + "* item six\n"
            + "  is on multiple\n"
            + " lines", "<ul>\n"
            + "  <li>item one</li>\n"
            + "  <li>item two<ol>\n"
            + "  <li>item three</li>\n"
            + "  <li>item four</li>\n"
            + "</ol>\n"
            + "</li>\n"
            + "  <li>item five - first line\n"
            + "   item five - second line</li>\n"
            + "  <li>item six\n"
            + "  is on multiple\n"
            + " lines</li>\n"
            + "</ul>");

        test("* item one", "<ul>\n  <li>item one</li>\n</ul>");
        test("*item one", "<p>*item one</p>");

    }

    public void testMacro() throws WikiParserException {
        test("{{macro/}}{{macro/}}", ""
            + "<pre class='macro' macroName='macro'><![CDATA[]]></pre>\n"
            + "<pre class='macro' macroName='macro'><![CDATA[]]></pre>");
        test("{{toto1}}a{{/toto1}}{{toto2/}}", ""
            + "<pre class='macro' macroName='toto1'><![CDATA[a]]></pre>\n"
            + "<pre class='macro' macroName='toto2'><![CDATA[]]></pre>");
        test("{{toto}}a{{/toto}}{{toto}}{{/toto}}", ""
            + "<pre class='macro' macroName='toto'><![CDATA[a]]></pre>\n"
            + "<pre class='macro' macroName='toto'><![CDATA[]]></pre>");
        test(
            "{{toto}}a{{/toto}}",
            "<pre class='macro' macroName='toto'><![CDATA[a]]></pre>");
        test(
            "{{x:toto y:param=value1 z:param2='value two'}}a{{/x:toto}}",
            "<pre class='macro' macroName='x:toto' y:param='value1' z:param2='value two'><![CDATA[a]]></pre>");
        test(
            "{{toto}}a{{toto}}b{{/toto}}c{{/toto}}",
            "<pre class='macro' macroName='toto'><![CDATA[a{{toto}}b{{/toto}}c]]></pre>");
        test(
            "{{toto}}a{{tata}}b{{/tata}}c{{/toto}}",
            "<pre class='macro' macroName='toto'><![CDATA[a{{tata}}b{{/tata}}c]]></pre>");
        test("before\n{{toto}}a{{/toto}}\nafter", ""
            + "<p>before</p>\n"
            + "<pre class='macro' macroName='toto'><![CDATA[a]]></pre>\n"
            + "<p>after</p>");
        test("before\n{{toto}}a{{/toto}}after", ""
            + "<p>before</p>\n"
            + "<pre class='macro' macroName='toto'><![CDATA[a]]></pre>\n"
            + "<p>after</p>");

        // URIs as macro names
        test(
            "{{x:toto}}a{{/x:toto}}",
            "<pre class='macro' macroName='x:toto'><![CDATA[a]]></pre>");
        test(
            "{{x:toto}}a{{x:toto}}b{{/x:toto}}c{{/x:toto}}",
            "<pre class='macro' macroName='x:toto'><![CDATA[a{{x:toto}}b{{/x:toto}}c]]></pre>");
        test(
            "{{x:toto}}a{{tata}}b{{/tata}}c{{/x:toto}}",
            "<pre class='macro' macroName='x:toto'><![CDATA[a{{tata}}b{{/tata}}c]]></pre>");
        test("before\n{{x:toto}}a{{/x:toto}}\nafter", ""
            + "<p>before</p>\n"
            + "<pre class='macro' macroName='x:toto'><![CDATA[a]]></pre>\n"
            + "<p>after</p>");
        test("before\n{{x:toto}}a{{/x:toto}}after", ""
            + "<p>before</p>\n"
            + "<pre class='macro' macroName='x:toto'><![CDATA[a]]></pre>\n"
            + "<p>after</p>");

        // Empty macros
        test(
            "{{x:toto /}}",
            "<pre class='macro' macroName='x:toto'><![CDATA[]]></pre>");
        test(
            "{{x:toto a=b c=d /}}",
            "<pre class='macro' macroName='x:toto' a='b' c='d'><![CDATA[]]></pre>");
        test(
            "before\n{{x:toto  a=b c=d/}}\nafter",
            ""
                + "<p>before</p>\n"
                + "<pre class='macro' macroName='x:toto' a='b' c='d'><![CDATA[]]></pre>\n"
                + "<p>after</p>");
        test(
            "before\n{{x:toto  a='b' c='d'/}}after",
            ""
                + "<p>before</p>\n"
                + "<pre class='macro' macroName='x:toto' a='b' c='d'><![CDATA[]]></pre>\n"
                + "<p>after</p>");
        test(
            "before{{x:toto /}}after",
            "<p>before<span class='macro' macroName='x:toto'><![CDATA[]]></span>after</p>");

        // Bad-formed block macros (not-closed)
        test(
            "{{toto}}",
            "<pre class='macro' macroName='toto'><![CDATA[]]></pre>");
        test(
            "{{toto}}a{{toto}}",
            "<pre class='macro' macroName='toto'><![CDATA[a{{toto}}]]></pre>");
        test("{{/x}}", "<p>{{/x}}</p>");
        test(
            "before{{a}}x{{b}}y{{c}}z\n" + "new line in the same  macro",
            ""
                + "<p>before<span class='macro' macroName='a'><![CDATA[x{{b}}y{{c}}z\n"
                + "new line in the same  macro]]></span></p>");
        test(
            "before{{a}}x{{b}}y{{c}}z{{/a}}after",
            ""
                + "<p>before<span class='macro' macroName='a'><![CDATA[x{{b}}y{{c}}z]]></span>after</p>");

        // 
        test(
            "{{toto}}a{{/toto}}",
            "<pre class='macro' macroName='toto'><![CDATA[a]]></pre>");
        test(
            "before{{toto}}macro{{/toto}}after",
            "<p>before<span class='macro' macroName='toto'><![CDATA[macro]]></span>after</p>");

        test("before{{toto a=b c=d}}toto macro tata {{/toto}}after", ""
            + "<p>before<span class='macro' macroName='toto' a='b' c='d'>"
            + "<![CDATA[toto macro tata ]]>"
            + "</span>after</p>");

        test(
            "before{{toto a=b c=d}}toto {{x qsdk}} macro {{sd}} tata {{/toto}}after",
            ""
                + "<p>before<span class='macro' macroName='toto' a='b' c='d'>"
                + "<![CDATA[toto {{x qsdk}} macro {{sd}} tata ]]>"
                + "</span>after</p>");

        // Not a macro
        test("{{ toto a=b c=d}}", "<p>{{ toto a=b c=d}}</p>");

        test(
            "This is a macro: {{toto x:a=b x:c=d}}\n"
                + "<table>\n"
                + "#foreach ($x in $table)\n"
                + "  <tr>hello, $x</tr>\n"
                + "#end\n"
                + "</table>\n"
                + "{{/toto}}",
            "<p>This is a macro: <span class='macro' macroName='toto' x:a='b' x:c='d'><![CDATA[\n"
                + "<table>\n"
                + "#foreach ($x in $table)\n"
                + "  <tr>hello, $x</tr>\n"
                + "#end\n"
                + "</table>\n"
                + "]]></span></p>");
        test(
            ""
                + "* item one\n"
                + "* item two\n"
                + "  {{code}} this is a code{{/code}} \n"
                + "  the same item (continuation)\n"
                + "* item three",
            ""
                + "<ul>\n"
                + "  <li>item one</li>\n"
                + "  <li>item two\n"
                + "  <span class='macro' macroName='code'><![CDATA[ this is a code]]></span> \n"
                + "  the same item (continuation)</li>\n"
                + "  <li>item three</li>\n"
                + "</ul>");

        // Macros with URIs as names
        test(
            "{{x:y a=b c=d}}",
            "<pre class='macro' macroName='x:y' a='b' c='d'><![CDATA[]]></pre>");
        test(
            "before{{x:y a=b c=d}}macro content",
            "<p>before<span class='macro' macroName='x:y' a='b' c='d'><![CDATA[macro content]]></span></p>");
        test(
            "before\n{{x:y a=b c=d}}macro content",
            ""
                + "<p>before</p>\n"
                + "<pre class='macro' macroName='x:y' a='b' c='d'><![CDATA[macro content]]></pre>");
        test(
            "before\n{{x:y a=b c=d/}}\nafter",
            ""
                + "<p>before</p>\n"
                + "<pre class='macro' macroName='x:y' a='b' c='d'><![CDATA[]]></pre>\n"
                + "<p>after</p>");

        // Not closed and bad-formed macros
        test(
            "a{{a}}{{b}}",
            "<p>a<span class='macro' macroName='a'><![CDATA[{{b}}]]></span></p>");
        test(
            "a{{a}}{{b}}{",
            "<p>a<span class='macro' macroName='a'><![CDATA[{{b}}{]]></span></p>");
    }

    /**
     * @throws WikiParserException
     */
    public void testParagraphs() throws WikiParserException {
        test("First paragraph.\n"
            + "Second line of the same paragraph.\n"
            + "\n"
            + "The second paragraph");
        test(
            "(% a='b' %)\nparagraph1\n\nparagraph2",
            "<p a='b'>paragraph1</p>\n<p>paragraph2</p>");
    }

    /**
     * @throws WikiParserException
     */
    public void testQuot() throws WikiParserException {
        test("This is a paragraph"
            + "\n"
            + "\n"
            + ">and this is a quotations\n"
            + "the second line", "<p>This is a paragraph</p>\n"
            + "<blockquote>\n"
            + "and this is a quotations\n"
            + "the second line\n"
            + "</blockquote>");
    }

    /**
     * @throws WikiParserException
     */
    public void testReferences() throws WikiParserException {
        test(
            "before http://www.foo.bar/com after",
            "<p>before <a href='http://www.foo.bar/com'>http://www.foo.bar/com</a> after</p>");
        test(
            "before [[toto]] after",
            "<p>before <a href='toto'>toto</a> after</p>");
        test(
            "before [[ [toto] [tata] ]] after",
            "<p>before <a href='[toto] [tata]'>[toto] [tata]</a> after</p>");
        test(
            "before wiki:Hello after",
            "<p>before <a href='wiki:Hello'>wiki:Hello</a> after</p>");
        test(
            "before wiki~:Hello after",
            "<p>before wiki<span class='escaped'>:</span>Hello after</p>");

        // Not a reference
        test("before [toto] after", "<p>before [toto] after</p>");
        test("not [[a reference] at all!", "<p>not [[a reference] at all!</p>");
        test(
            "before [#local ancor] after",
            "<p>before [#local ancor] after</p>");
    }

    /**
     * @throws WikiParserException
     */
    public void testTables() throws WikiParserException {
        // "!!" and "::" markup
        test("!! Header :: Cell ", ""
            + "<table><tbody>\n"
            + "  <tr><th> Header </th><td> Cell </td></tr>\n"
            + "</tbody></table>");
        test("!!   Header    ::    Cell    ", ""
            + "<table><tbody>\n"
            + "  <tr><th>   Header    </th><td>    Cell    </td></tr>\n"
            + "</tbody></table>");

        test("::Cell 1 :: Cell 2", "<table><tbody>\n"
            + "  <tr><td>Cell 1 </td><td> Cell 2</td></tr>\n"
            + "</tbody></table>");
        test("Not a Header :: Not a Cell", "<p>Not a Header :: Not a Cell</p>");
        test("Not a Header::Not a Cell", "<p>Not a Header::Not a Cell</p>");

        // "||" and "|" markup
        test("|| Header | Cell ", ""
            + "<table><tbody>\n"
            + "  <tr><th> Header </th><td> Cell </td></tr>\n"
            + "</tbody></table>");
        test("||   Header    |    Cell    ", ""
            + "<table><tbody>\n"
            + "  <tr><th>   Header    </th><td>    Cell    </td></tr>\n"
            + "</tbody></table>");

        test("|Cell 1 | Cell 2", "<table><tbody>\n"
            + "  <tr><td>Cell 1 </td><td> Cell 2</td></tr>\n"
            + "</tbody></table>");
        test("Not a Header | Not a Cell", "<p>Not a Header | Not a Cell</p>");
        test("Not a Header|Not a Cell", "<p>Not a Header|Not a Cell</p>");

        test("|| cell 1.1 || cell 1.2\n" + "|| cell 2.1|| cell 2.2", ""
            + "<table><tbody>\n"
            + "  <tr><th> cell 1.1 </th><th> cell 1.2</th></tr>\n"
            + "  <tr><th> cell 2.1</th><th> cell 2.2</th></tr>\n"
            + "</tbody></table>");
        test("|| Head 1.1 || Head 1.2\n" + "| cell 2.1| cell 2.2", ""
            + "<table><tbody>\n"
            + "  <tr><th> Head 1.1 </th><th> Head 1.2</th></tr>\n"
            + "  <tr><td> cell 2.1</td><td> cell 2.2</td></tr>\n"
            + "</tbody></table>");
        test("|| Multi \nline  \nheader \n"
            + "| Multi\nline\ncell\n"
            + "\n"
            + "One,two,three", ""
            + "<table><tbody>\n"
            + "  <tr><th> Multi \nline  \nheader </th></tr>\n"
            + "  <tr><td> Multi\nline\ncell</td></tr>\n"
            + "</tbody></table>\n"
            + "<p>One,two,three</p>");
        test("this is not || a table", "<p>this is not || a table</p>");
        test("this is not | a table", "<p>this is not | a table</p>");

        test(
            "|| //Italic header// || **Bold header**\n"
                + "| //Italic cell// | **Bold cell**\n",
            ""
                + "<table><tbody>\n"
                + "  <tr><th> <em>Italic header</em> </th><th> <strong>Bold header</strong></th></tr>\n"
                + "  <tr><td> <em>Italic cell</em> </td><td> <strong>Bold cell</strong></td></tr>\n"
                + "</tbody></table>");
        test(
            "|| //Italic header || **Bold header \n"
                + "| //Italic cell | **Bold cell \n",
            ""
                + "<table><tbody>\n"
                + "  <tr><th> <em>Italic header </em></th><th> <strong>Bold header </strong></th></tr>\n"
                + "  <tr><td> <em>Italic cell </em></td><td> <strong>Bold cell </strong></td></tr>\n"
                + "</tbody></table>");

        // Table parameters
        test("(%a=b%)\n|| Header ", ""
            + "<table a='b'><tbody>\n"
            + "  <tr><th> Header </th></tr>\n"
            + "</tbody></table>");
        test("(%a=b%)\n!! Header ", ""
            + "<table a='b'><tbody>\n"
            + "  <tr><th> Header </th></tr>\n"
            + "</tbody></table>");
        test("(%a=b%)\n| cell ", ""
            + "<table a='b'><tbody>\n"
            + "  <tr><td> cell </td></tr>\n"
            + "</tbody></table>");
        test("(%a=b%)\n| cell ", ""
            + "<table a='b'><tbody>\n"
            + "  <tr><td> cell </td></tr>\n"
            + "</tbody></table>");

        // Row parameters
        test("(%a=b%)||cell");
        test("(%a=b%)::cell1\n(%c=d%)::cell2");

        test("(%a=b%)\n(%c=d%)||(%e=f%) cell");
        test("(%a=b%)\n(%c=d%)::(%e=f%) cell ::(%g=h%)");

    }

    public void testVerbatim() throws WikiParserException {
        test("{{{abc}}}", "<pre>abc</pre>");
        test("{{{abc}}}{{{cde}}}", "<pre>abc</pre>\n<pre>cde</pre>");
        test(
            "before\n{{{abc}}}after",
            "<p>before</p>\n<pre>abc</pre>\n<p>after</p>");
        test("{{{{{{abc}}}}}}", "<pre>{{{abc}}}</pre>");

        // Inline verbatim
        // test(" {{{abc}}}", "<p> <code>abc</code></p>");
        test("before{{{abc}}}after", "<p>before<code>abc</code>after</p>");
        test(
            "before{{{{{{abc}}}}}}after",
            "<p>before<code>{{{abc}}}</code>after</p>");

    }

}
