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
package org.wikimodel.wem.test.xhtml;

import org.wikimodel.wem.IWikiParser;
import org.wikimodel.wem.WikiParserException;
import org.wikimodel.wem.test.AbstractWikiParserTest;
import org.wikimodel.wem.xhtml.XhtmlParser;

/**
 * @author MikhailKotelnikov
 * @author vmassol
 * @author thomas.mortagne
 */
public class XHtmlParserTest extends AbstractWikiParserTest {

    /**
     * @param name
     */
    public XHtmlParserTest(String name) {
        super(name);
    }

    @Override
    protected IWikiParser newWikiParser() {
        return new XhtmlParser();
    }

    /**
     * @throws WikiParserException
     */
    public void testDocuments() throws WikiParserException {
        test("<html><p>before</p>\n" +
            "<div class='doc'>\n" +
            "<p>inside</p>\n" +
            "</div>\n" +
            "<p>after</p></html>",
            "<p>before</p>\n" +
            "<div class='doc'>\n" +
            "<p>inside</p>\n" +
            "</div>\n" +
            "<p>after</p>");
        test("<html><p>before</p>\n" +
            "<div class='doc'>\n" +
            "<p>inside</p>\n" +
            "</div>\n" +
            "<p>after</p></html>",
            "<p>before</p>\n" +
            "<div class='doc'>\n" +
            "<p>inside</p>\n" +
            "</div>\n" +
            "<p>after</p>");
        test("<html><table><tbody>\n" +
            " <tr><td> Line One </td><td> First doc:<div class='doc'>\n" +
            "<p>inside</p>\n" +
            "</div>\n" +
            "after</td></tr>\n" +
            "   <tr><td>Line Two</td><td>Second doc:<div class='doc'>\n" +
            "<p>lkjlj</p>\n" +
            "</div>\n" +
            "skdjg</td></tr>\n" +
            "</tbody></table></html>",
            "<table><tbody>\n" +
            "  <tr><td>Line One</td><td>First doc:<div class='doc'>\n" +
            "<p>inside</p>\n" +
            "</div>\n" +
            "after</td></tr>\n" +
            "  <tr><td>Line Two</td><td>Second doc:<div class='doc'>\n" +
            "<p>lkjlj</p>\n" +
            "</div>\n" +
            "skdjg</td></tr>\n" +
            "</tbody></table>");
        test("<html><table><tbody>\n" +
            "  <tr><td>This is a table:</td><td><div class='doc'>\n" +
            "<ul>\n" +
            "  <li>item one</li>\n" +
            "  <li>item two</li>\n" +
            "  <li>subitem 1</li>\n" +
            "  <li>subitem 2</li>\n" +
            "  <li>item three</li>\n" +
            "</ul>\n" +
            "</div>\n" +
            "</td></tr>\n" +
            "</tbody></table></html>",
            "<table><tbody>\n" +
            "  <tr><td>This is a table:</td><td><div class='doc'>\n" +
            "<ul>\n" +
            "  <li>item one</li>\n" +
            "  <li>item two</li>\n" +
            "  <li>subitem 1</li>\n" +
            "  <li>subitem 2</li>\n" +
            "  <li>item three</li>\n" +
            "</ul>\n" +
            "</div>\n" +
            "</td></tr>\n" +
            "</tbody></table>");

        test("<html><p>before</p>\n" +
            "<div class='doc'>\n" +
            "<p>opened and not closed</p>\n" +
            "</div></html>",
            "<p>before</p>\n" +
            "<div class='doc'>\n" +
            "<p>opened and not closed</p>\n" +
            "</div>");
        test("<html><p>before</p>\n" +
            "<div class='doc'>\n" +
            "<p>one</p>\n" +
            "<div class='doc'>\n" +
            "<p>two</p>\n" +
            "<div class='doc'>\n" +
            "<p>three</p>\n" +
            "</div>\n" +
            "</div>\n" +
            "</div></html>",
            "<p>before</p>\n" +
            "<div class='doc'>\n" +
            "<p>one</p>\n" +
            "<div class='doc'>\n" +
            "<p>two</p>\n" +
            "<div class='doc'>\n" +
            "<p>three</p>\n" +
            "</div>\n" +
            "</div>\n" +
            "</div>");
    }
    
    /**
     * @throws WikiParserException
     */
    public void testDefinitionLists() throws WikiParserException {
        test("<html><dl><dt>term</dt><dd>definition</dd></dl></html>", ""
            + "<dl>\n"
            + "  <dt>term</dt>\n"
            + "  <dd>definition</dd>\n"
            + "</dl>");
        test(
            "<html><dl><dt>term1</dt><dt>term2</dt><dd>definition</dd></dl></html>",
            ""
                + "<dl>\n"
                + "  <dt>term1</dt>\n"
                + "  <dt>term2</dt>\n"
                + "  <dd>definition</dd>\n"
                + "</dl>");
        // FIXME: this test generates an invalid structure (should be an
        // embedded document with an internal definition list);
        test("<html>"
            + "<dl>"
            + "<dt>term</dt>"
            + "<dd>definition"
            + "<dl>"
            + "<dt>term</dt>"
            + "<dd>definition</dd>"
            + "</dl>"
            + "</dd>"
            + "</dl></html>");
    }

    /**
     * @throws WikiParserException
     */
    public void testEscape() throws WikiParserException {
    }

    /**
     * @throws WikiParserException
     */
    public void testFormats() throws WikiParserException {
        test("<html><b>bold</b></html>", "<p><strong>bold</strong></p>");
        test("<html><strong>bold</strong></html>", "<p><strong>bold</strong></p>");
        
        test("<html><s>strike</s></html>", "<p><strike>strike</strike></p>");
        test("<html><strike>strike</strike></html>", "<p><strike>strike</strike></p>");
        test("<html><del>strike</del></html>", "<p><strike>strike</strike></p>");
        
        test("<html><em>italic</em></html>", "<p><em>italic</em></p>");
        
        test("<html><u>underline</u></html>", "<p><ins>underline</ins></p>");
        test("<html><ins>underline</ins></html>", "<p><ins>underline</ins></p>");
        
        test("<html><sup>sup</sup></html>", "<p><sup>sup</sup></p>");
        test("<html><sub>sub</sub></html>", "<p><sub>sub</sub></p>");
        
        test("<html><tt>mono</tt></html>", "<p><mono>mono</mono></p>");
    }

    /**
     * @throws WikiParserException
     */
    public void testVerbatimInline() throws WikiParserException {
        test("<html><p><tt class=\"wikimodel-verbatim\">verbatim</tt></p></html>", "<p><code>verbatim</code></p>");
    }

    /**
     * @throws WikiParserException
     */
    public void testHeaders() throws WikiParserException {
        test("<html><h1>header1</h1></html>");
        test("<html><h2>header2</h2></html>");
        test("<html><h3>header3</h3></html>");
        test("<html><h4>header4</h4></html>");
        test("<html><h5>header5</h5></html>");
        test("<html><h6>header6</h6></html>");

        test("<html>before<h1>header1</h1>after</html>");
    }

    /**
     * @throws WikiParserException
     */
    public void testHorLine() throws WikiParserException {
        test("<html>before<hr />after</html>", ""
            + "<p>before</p>\n"
            + "<hr />\n"
            + "<p>after</p>");
        test("<html><hr a='b' /></html>", "<hr a='b' />");
    }

    /**
     * @throws WikiParserException
     */
    public void testLineBreak() throws WikiParserException {
        test("<html>before<br />after</html>", "<p>before\nafter</p>");
    }

    /**
     * @throws WikiParserException
     */
    public void testLists() throws WikiParserException {
        // TODO: add management of embedded block elements.
        test("<html><ul><li>a<ul><li>b</li></ul></li><li>c</li></ul></html>", ""
            + "<ul>\n"
            + "  <li>a<ul>\n"
            + "  <li>b</li>\n"
            + "</ul>\n"
            + "</li>\n"
            + "  <li>c</li>\n"
            + "</ul>");

        test("<html><ul>"
            + "<li>item one</li>"
            + "<li>before<hr />after</li>"
            + "</ul></html>");
        test("<html><ul>"
            + "<li>item one</li>"
            + "<li>before"
            + " <ul>"
            + "  <li>item one</li>"
            + " </ul>"
            + "after</li>"
            + "</ul></html>");

        test("<html><ol><li>Item 1<ol><li>Item 2<ul class=\"star\"><li>Item\r\n"
            + "3</li></ul></li><li>Item 4</li></ol></li><li>Item 5</li></ol><ul\r\n"
            + "class=\"star\"><li>Item 1<ul class=\"star\"><li>Item 2<ul class=\"star\"><li>Item\r\n"
            + "3</li></ul></li><li>Item 4</li></ul></li><li>Item 5</li><li>Item\r\n"
            + "6</li></ul></html>\r\n"
            + "");
    }

    /**
     * @throws WikiParserException
     */
    public void testParagraphs() throws WikiParserException {
        test("<html><p>paragraph</p></html>", "<p>paragraph</p>");
        test(
            "<p>hello <em class=\"italic\">beautiful</em> <strong>world</strong></p>",
            "<p>hello <em>beautiful</em> <strong>world</strong></p>");

    }

    /**
     * @throws WikiParserException
     */
    public void testQuot() throws WikiParserException {
    	test("<html><blockquote>quote</blockquote></html>", 
    		"<blockquote>\nquote\n</blockquote>");
    	test("<html><blockquote>line1<blockquote>line2<blockquote>line3</blockquote>"
    	    + "line4</blockquote></blockquote></html>", 
    	    "<blockquote>\nline1<blockquote>\nline2<blockquote>\nline3\n</blockquote>"
    	    + "\n\nline4\n</blockquote>\n\n</blockquote>");
    }

    /**
     * @throws WikiParserException
     */
    public void testReferences() throws WikiParserException {
    }

    public void testImages() throws WikiParserException {
        test("<html><img src=\"target\" alt=\"some description\"/></html>","<p><img src=\"target\" alt=\"some description\"/></p>");
    }
    
    /**
     * @throws WikiParserException
     */
    public void testTables() throws WikiParserException {
        test("<html><table><tr><td>first cell</td><td>second cell</td></tr></table></html>");
        test("<html><table><tr><td>first cell</td></tr></table></html>");
        test("<html><table>"
            + "<tr><th>first header</th><th>second header</th></tr>"
            + "<tr><td>first cell</td><td>second cell</td></tr>"
            + "</table></html>");
        test("<html><table>"
            + "<tr><th>first row</th><td>first cell</td></tr>"
            + "<tr><th>second row</th><td>before <table><tr><td>first cell</td></tr></table> after</td></tr>"
            + "<tr><th>third row</th><td>third cell</td></tr>"
            + "</table></html>");

        // "Bad-formed" tables...

        // The content is completely ignored.
        test("<html><table>first cell</table></html>");

        // A "td" element directly in the table
        test("<html><table><td>first cell</td></table></html>");

        // Not a table at all
        test("<html><td>first cell</td></html>");
    }

    /**
     * @throws WikiParserException
     */
    public void testVerbatimeBlocks() throws WikiParserException {
    }

    /**
     * @throws WikiParserException
     */
    public void testVerbatimeInline() throws WikiParserException {
    }
}
