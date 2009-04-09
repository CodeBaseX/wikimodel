/*******************************************************************************
 * Copyright (c) 2005,2009 Cognium Systems SA and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Contributors:
 *     Cognium Systems SA - initial API and implementation
 *******************************************************************************/
package org.wikimodel.wem.test;

import org.wikimodel.wem.IWikiParser;
import org.wikimodel.wem.WikiParserException;
import org.wikimodel.wem.confluence.ConfluenceWikiParser;

/**
 * @author MikhailKotelnikov
 */
public class ConfluenceWikiParserTest extends AbstractWikiParserTest {

    /**
     * @param name
     */
    public ConfluenceWikiParserTest(String name) {
        super(name);
    }

    @Override
    protected IWikiParser newWikiParser() {
        return new ConfluenceWikiParser();
    }

    public void testConfluenceHeadings() throws WikiParserException {
        test("h1. Biggest heading", "<h1>Biggest heading</h1>");
        test("h2. Bigger heading", "<h2>Bigger heading</h2>");
        test("h3. Big Heading", "<h3>Big Heading</h3>");
        test("h4. Normal Heading", "<h4>Normal Heading</h4>");
        test("h5. Small Heading", "<h5>Small Heading</h5>");
        test("h6. Smallest Heading", "<h6>Smallest Heading</h6>");
    }

    /**
     * http://confluence.atlassian.com/renderer/notationhelp.action?section=
     * breaks
     * 
     * @throws WikiParserException
     */
    public void testConfluenceTextBreaks() throws WikiParserException {
        test("first\n\nsecond", "<p>first</p>\n<p>second</p>");
        test("first\\\\second", "<p>first<br />second</p>");
        test("first\n----second", "<p>first</p>\n<hr />\n<p>second</p>");
        test("first --- second", "<p>first&#160;&mdash; second</p>");
        test("first -- second", "<p>first&#160;&ndash; second</p>");
    }

    public void testConfluenceTextEffects() throws WikiParserException {
        test(
            "Makes text *strong*.",
            "<p>Makes text <strong>strong</strong>.</p>");
        test("Makes text _emphasis_.", "<p>Makes text <em>emphasis</em>.</p>");
        test(
            "Makes text ??citation??.",
            "<p>Makes text <cite>citation</cite>.</p>");
        test(
            "Makes text -strikethrough-.",
            "<p>Makes text <strike>strikethrough</strike>.</p>");

        // Underlined was replaced by "tt": I consider the underline formatting
        // harmful - this pure visual tag mocks up a very important logical tag
        // - "a". So it is not a "bug", it is a "feature".
        // (mkotelnikov)
        test(
            "Makes text +underlined+.",
            "<p>Makes text <tt>underlined</tt>.</p>");
        test(
            "Makes text ^superscript^.",
            "<p>Makes text <sup>superscript</sup>.</p>");
        test(
            "Makes text ~subscript~.",
            "<p>Makes text <sub>subscript</sub>.</p>");
        test(
            "Makes text as {{code text}}.",
            "<p>Makes text as <code>code text</code>.</p>");
        test(
            "bq. Some block quoted text.",
            "<blockquote>\n Some block quoted text.\n</blockquote>");
        test(
            "{quote}Some block quoted text.\n{quote}",
            "<blockquote>\nSome block quoted text.\n</blockquote>");

        // {color:red}
        // look ma, red text!
        // {color} Changes the color of a block of text.
        //
        // Example: look ma, red text!
    }

    /**
     * @throws WikiParserException
     */
    public void testEscape() throws WikiParserException {
        // "a" and "b" symbols are just escaped
        test("not\\a\\break", "<p>not"
            + "<span class='wikimodel-escaped'>a</span>"
            + "<span class='wikimodel-escaped'>b</span>"
            + "reak</p>");
    }

    /**
     * @throws WikiParserException
     */
    public void testFormats() throws WikiParserException {
        test("a-b", "<p>a-b</p>");
        test("-a-", "<p><strike>a</strike></p>");

        // STRONG
        test("*bold*", "<p><strong>bold</strong></p>");
        test(
            "*bold* *bold* *bold*",
            "<p><strong>bold</strong> <strong>bold</strong> <strong>bold</strong></p>");
        // EM
        test("_italic_", "<p><em>italic</em></p>");
        test(
            "_italic_ _italic_ _italic_",
            "<p><em>italic</em> <em>italic</em> <em>italic</em></p>");
        // CITE
        test("??citation??", "<p><cite>citation</cite></p>");
        test(
            "??citation?? ??citation?? ??citation??",
            "<p><cite>citation</cite> <cite>citation</cite> <cite>citation</cite></p>");

        // STRIKE
        test("-abc-", "<p><strike>abc</strike></p>");
        test(" -abc-", "<p> <strike>abc</strike></p>");
        test("abc -cde- efg", "<p>abc <strike>cde</strike> efg</p>");
        test(
            "abc -cde- -efg-",
            "<p>abc <strike>cde</strike> <strike>efg</strike></p>");
        // not a STRIKE
        test("abc - cde", "<p>abc - cde</p>");

        // TT
        test("+abc+", "<p><tt>abc</tt></p>");
        test(" +abc+", "<p> <tt>abc</tt></p>");
        test("abc +cde+ efg", "<p>abc <tt>cde</tt> efg</p>");
        test("abc +cde+ +efg+", "<p>abc <tt>cde</tt> <tt>efg</tt></p>");
        // not a TT
        test("abc + cde", "<p>abc + cde</p>");

        // SUB
        test("~abc~", "<p><sub>abc</sub></p>");
        test(" ~abc~", "<p> <sub>abc</sub></p>");
        test("abc ~cde~ efg", "<p>abc <sub>cde</sub> efg</p>");
        test("abc ~cde~ ~efg~", "<p>abc <sub>cde</sub> <sub>efg</sub></p>");

        // SUP
        test("^abc^", "<p><sup>abc</sup></p>");
        test(" ^abc^", "<p> <sup>abc</sup></p>");
        test("abc ^cde^ efg", "<p>abc <sup>cde</sup> efg</p>");
        test("abc ^cde^ ^efg^", "<p>abc <sup>cde</sup> <sup>efg</sup></p>");

        test("before{{{inside}}}after", "<p>before<code>inside</code>after</p>");

        // Mixed styles
        test(
            "normal*bold_bold-italic*italic_normal",
            "<p>normal<strong>bold</strong><strong><em>bold-italic</em></strong><em>italic</em>normal</p>");

        // Not formatting
        test(
            "http://www.foo.bar",
            "<p><a href='http://www.foo.bar'>http://www.foo.bar</a></p>");

    }

    /**
     * @throws WikiParserException
     */
    public void testHeaders() throws WikiParserException {
        // Headers without spaces after the header symbol
        test("h1.Header1", "<h1>Header1</h1>");
        test("h2.Header2", "<h2>Header2</h2>");
        test("h3.Header3", "<h3>Header3</h3>");
        test("h4.Header4", "<h4>Header4</h4>");
        test("h5.Header5", "<h5>Header5</h5>");
        test("h6.Header6", "<h6>Header6</h6>");
        test("h7.Not a header", "<p>h7.Not a header</p>");
        // Headers: ignoring spaces just after the header sign
        test("h1. Header1", "<h1>Header1</h1>");
        test("h2. Header2", "<h2>Header2</h2>");
        test("h3. Header3", "<h3>Header3</h3>");
        test("h4. Header4", "<h4>Header4</h4>");
        test("h5. Header5", "<h5>Header5</h5>");
        test("h6. Header6", "<h6>Header6</h6>");
        test("h7. Not a header", "<p>h7. Not a header</p>");

        test(
            "\nh1.Header\n* list item",
            "<h1>Header</h1>\n<ul>\n  <li>list item</li>\n</ul>");
        test(
            "before\nh3. Header \nafter",
            "<p>before</p>\n<h3>Header </h3>\n<p>after</p>");
    }

    /**
     * @throws WikiParserException
     */
    public void testLineBreaks() throws WikiParserException {
        test("line\\\\break", "<p>line<br />break</p>");
        test("not\\a\\break", "<p>not"
            + "<span class='wikimodel-escaped'>a</span>"
            + "<span class='wikimodel-escaped'>b</span>"
            + "reak</p>");
    }

    /**
     * @throws WikiParserException
     */
    public void testLists() throws WikiParserException {
        test("# Item 1\n"
            + "## Item 2\n"
            + "##* Item 3\n"
            + "## Item 4\n"
            + "# Item 5\n", ""
            + "<ol>\n"
            + "  <li>Item 1<ol>\n"
            + "  <li>Item 2<ul>\n"
            + "  <li>Item 3</li>\n"
            + "</ul>\n"
            + "</li>\n"
            + "  <li>Item 4</li>\n"
            + "</ol>\n"
            + "</li>\n"
            + "  <li>Item 5</li>\n"
            + "</ol>");
        test("* item one", "<ul>\n  <li>item one</li>\n</ul>");
        test("* item one\n** item two", ""
            + "<ul>\n"
            + "  <li>item one<ul>\n"
            + "  <li>item two</li>\n"
            + "</ul>\n"
            + "</li>\n"
            + "</ul>");
        test("- item one\n-- item two", ""
            + "<ul>\n"
            + "  <li>item one<ul>\n"
            + "  <li>item two</li>\n"
            + "</ul>\n"
            + "</li>\n"
            + "</ul>");
        test("** item one", ""
            + "<ul>\n"
            + "  <li><ul>\n"
            + "  <li>item one</li>\n"
            + "</ul>\n"
            + "</li>\n"
            + "</ul>");
        test("*** item one", ""
            + "<ul>\n"
            + "  <li><ul>\n"
            + "  <li><ul>\n"
            + "  <li>item one</li>\n"
            + "</ul>\n"
            + "</li>\n"
            + "</ul>\n"
            + "</li>\n"
            + "</ul>");
        test("*## item one", ""
            + "<ul>\n"
            + "  <li><ol>\n"
            + "  <li><ol>\n"
            + "  <li>item one</li>\n"
            + "</ol>\n"
            + "</li>\n"
            + "</ol>\n"
            + "</li>\n"
            + "</ul>");
        test("*## item one", ""
            + "<ul>\n"
            + "  <li><ol>\n"
            + "  <li><ol>\n"
            + "  <li>item one</li>\n"
            + "</ol>\n"
            + "</li>\n"
            + "</ol>\n"
            + "</li>\n"
            + "</ul>");
        test("# Item 1\n"
            + "## Item 2\n"
            + "##* Item 3\n"
            + "## Item 4\n"
            + "# Item 5\n"
            + "* Item 1\n"
            + "** Item 2\n"
            + "*** Item 3\n"
            + "** Item 4\n"
            + "* Item 5\n"
            + "* Item 6", ""
            + "<ol>\n"
            + "  <li>Item 1<ol>\n"
            + "  <li>Item 2<ul>\n"
            + "  <li>Item 3</li>\n"
            + "</ul>\n"
            + "</li>\n"
            + "  <li>Item 4</li>\n"
            + "</ol>\n"
            + "</li>\n"
            + "  <li>Item 5</li>\n"
            + "</ol>\n"
            + "<ul>\n"
            + "  <li>Item 1<ul>\n"
            + "  <li>Item 2<ul>\n"
            + "  <li>Item 3</li>\n"
            + "</ul>\n"
            + "</li>\n"
            + "  <li>Item 4</li>\n"
            + "</ul>\n"
            + "</li>\n"
            + "  <li>Item 5</li>\n"
            + "  <li>Item 6</li>\n"
            + "</ul>");
    }

    /**
     * @throws WikiParserException
     */
    public void testParagraphs() throws WikiParserException {
        test("First paragraph.\n"
            + "Second line of the same paragraph.\n"
            + "\n"
            + "The second paragraph");
    }

    /**
     * @throws WikiParserException
     */
    public void testProperties() throws WikiParserException {
        test("#toto hello  world\n123");
        test("#prop1 value1\n#prop2 value2");
    }

    /**
     */
    public void testQuot() throws WikiParserException {
        test(
            "first\nbq.second\n\nthird",
            "<p>first</p>\n<blockquote>\nsecond\n</blockquote>\n<p>third</p>");
        test(
            "bq. Some block quoted text.",
            "<blockquote>\n Some block quoted text.\n</blockquote>");
        test(
            "first\n{quote}second\n{quote}\nthird",
            "<p>first</p>\n<blockquote>\nsecond\n</blockquote>\n<p>third</p>");
        test(
            "{quote}Some block quoted text.\n{quote}",
            "<blockquote>\nSome block quoted text.\n</blockquote>");
    }

    /**
     * @throws WikiParserException
     */
    public void testReferences() throws WikiParserException {
        test(
            "before http://www.foo.bar/com after",
            "<p>before <a href='http://www.foo.bar/com'>http://www.foo.bar/com</a> after</p>");
        test(
            "before this+is+a+reference:to_here after",
            "<p>before <a href='this+is+a+reference:to_here'>this+is+a+reference:to_here</a> after</p>");
        test(
            "before [[toto]] after",
            "<p>before <a href='toto'>toto</a> after</p>");

        // Tests from
        // http://wikicreole.org/wiki/Creole1.0#section-Creole1.0-
        // LinksInternalExternalAndInterwiki
        test("[[link]]", "<p><a href='link'>link</a></p>");
        test(
            "[[MyBigPage|Go to my page]]",
            "<p><a href='MyBigPage'>Go to my page</a></p>");
        test(
            "[[http://www.wikicreole.org/]]",
            "<p><a href='http://www.wikicreole.org/'>http://www.wikicreole.org/</a></p>");
        test(
            "http://www.rawlink.org/, http://www.another.rawlink.org",
            "<p><a href='http://www.rawlink.org/'>http://www.rawlink.org/</a>, <a href='http://www.another.rawlink.org'>http://www.another.rawlink.org</a></p>");
        test(
            "[[http://www.wikicreole.org/|Visit the WikiCreole website]]",
            "<p><a href='http://www.wikicreole.org/'>Visit the WikiCreole website</a></p>");
        // test(
        // "[[Weird Stuff|**Weird** // Stuff//]]",
        // "<p><a href='Weird Stuff'>**Weird** // Stuff//</a></p>");
        test("[[Weird Stuff|**Weird** // Stuff//]]");
        test(
            "[[Ohana:WikiFamily]]",
            "<p><a href='Ohana:WikiFamily'>Ohana:WikiFamily</a></p>");

        // Not a reference
        test("before [toto] after", "<p>before [toto] after</p>");

        test("before this+is+a+reference:to_here after");
        test("before this+is+not+a+reference: to_here after");
        test("before|foo:bar|after");
        test("before main:wiki after");
        test("before main:**wiki** after");
        test("before http://www.google.com:8080 after");
        test("before http://www.google.com?q=Hello+World!#fragmenté~~ after");

        test("Bad reference: http://923.43.23.11:8080/toto ");
        test("Bad reference: http:|sdf after");
    }

    /**
     * @throws WikiParserException
     */
    public void testTable() throws WikiParserException {
        test(
            "||Header",
            "<table><tbody>\n  <tr><th>Header</th></tr>\n</tbody></table>");
        test(
            "|Cell",
            "<table><tbody>\n  <tr><td>Cell</td></tr>\n</tbody></table>");
        test(
            "||Header||\n",
            "<table><tbody>\n  <tr><th>Header</th></tr>\n</tbody></table>");
        test(
            "|Cell|\n",
            "<table><tbody>\n  <tr><td>Cell</td></tr>\n</tbody></table>");
        test("||Header1||Header2", ""
            + "<table><tbody>\n"
            + "  <tr><th>Header1</th><th>Header2</th></tr>\n"
            + "</tbody></table>");
        test("||Header1||Header2||", ""
            + "<table><tbody>\n"
            + "  <tr><th>Header1</th><th>Header2</th></tr>\n"
            + "</tbody></table>");

        test("|Cell1|Cell2", ""
            + "<table><tbody>\n"
            + "  <tr><td>Cell1</td><td>Cell2</td></tr>\n"
            + "</tbody></table>");
        test("||Header1\n||Header2", ""
            + "<table><tbody>\n"
            + "  <tr><th>Header1</th></tr>\n"
            + "  <tr><th>Header2</th></tr>\n"
            + "</tbody></table>");
        test("|Cell1\n|Cell2", ""
            + "<table><tbody>\n"
            + "  <tr><td>Cell1</td></tr>\n"
            + "  <tr><td>Cell2</td></tr>\n"
            + "</tbody></table>");
        test("|| Header | Cell", ""
            + "<table><tbody>\n"
            + "  <tr><th> Header </th><td> Cell</td></tr>\n"
            + "</tbody></table>");

        test("|| Header 1.1 || Header 1.2\n"
            + "| Cell 2.1 | Cell 2.2\n"
            + "| Cell 3.1 || Head 3.2", ""
            + "<table><tbody>\n"
            + "  <tr><th> Header 1.1 </th><th> Header 1.2</th></tr>\n"
            + "  <tr><td> Cell 2.1 </td><td> Cell 2.2</td></tr>\n"
            + "  <tr><td> Cell 3.1 </td><th> Head 3.2</th></tr>\n"
            + "</tbody></table>");

        test("||heading 1||heading 2||heading 3||\n"
            + "|col A1|col A2|col A3|\n"
            + "|col B1|col B2|col B3| ");
        // Not a table
        test("abc || cde", "<p>abc || cde</p>");
    }

    /**
     * @throws WikiParserException
     */
    public void testVerbatimeBlocks() throws WikiParserException {
        test("abc \n{{{ 123\n  CDE\n   345 }}} efg");
        test("abc {{{ 123\n  CDE\n   345 }}} efg");
        test("abc\n{{{\n{{{\n 123 \n }}}\n}}} efg");
        test("`verbatime`");
        test("`just like this...");
    }

}
