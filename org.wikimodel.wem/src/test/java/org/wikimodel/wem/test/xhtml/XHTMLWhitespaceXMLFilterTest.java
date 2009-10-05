/*******************************************************************************
 * Copyright (c) 2008 Cognium Systems SA and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Contributors:
 *     Cognium Systems SA - initial API and implementation
 *******************************************************************************/
package org.wikimodel.wem.test.xhtml;

import java.io.StringReader;

import javax.xml.parsers.SAXParserFactory;

import org.wikimodel.wem.xhtml.filter.XHTMLWhitespaceXMLFilter;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import junit.framework.TestCase;

/**
 * @author vmassol
 * @author thomas.mortagne
 */
public class XHTMLWhitespaceXMLFilterTest extends TestCase {
    private XMLWriter writerFilter;

    private XHTMLWhitespaceXMLFilter whitespaceFilter;

    /**
     * {@inheritDoc}
     * 
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        XMLReader xmlReader = SAXParserFactory
            .newInstance()
            .newSAXParser()
            .getXMLReader();
        whitespaceFilter = new XHTMLWhitespaceXMLFilter(xmlReader);
        writerFilter = new XMLWriter();

        whitespaceFilter.setFeature(
            "http://xml.org/sax/features/namespaces",
            true);
        whitespaceFilter.setContentHandler(writerFilter);
        whitespaceFilter.setProperty(
            "http://xml.org/sax/properties/lexical-handler",
            writerFilter);
    }

    public void testWhiteSpaceStripping() throws Exception {
        assertCleanedHTML("<p>one two</p>", "<p>  one  two  </p>");
        assertCleanedHTML(
            "<p>one two <b>three</b></p>",
            "<p>  one  two  <b>three</b></p>");
        assertCleanedHTML("<p>one two</p>", "<p>\n\r\tone\n\r\ttwo\n\r\t</p>");
        assertCleanedHTML(
            "<p>one <b>two</b> <b>three</b></p>",
            "<p>one <b>two</b>  <b>three</b></p>");
        assertCleanedHTML(
            "<p>one <b>two</b> <em><b>three</b>a</em></p>",
            "<p>one <b>two</b>  <em><b>three</b>a</em></p>");
        assertCleanedHTML(
            "<p>one</p>two<p>three</p>",
            "<p>one</p>  two  <p>three</p>");
        assertCleanedHTML("<![CDATA[\n  one  \n]]>", "<![CDATA[\n  one  \n]]>");
        assertCleanedHTML("<pre>\n  one  \n</pre>", "<pre>\n  one  \n</pre>");
        assertCleanedHTML(
            "<p>toto <tt>\n  one  \n</tt></p>",
            "<p>toto <tt class=\"wikimodel-verbatim\">\n  one  \n</tt></p>");
        assertCleanedHTML(
            "<p>one <!--comment-->two</p>",
            "<p>  one  <!--comment-->  two  </p>");
        assertCleanedHTML(
            "<p><!--comment-->one <b>two</b></p>",
            "<p><!--comment-->  one  <b>two</b></p>");
        assertCleanedHTML(
            "<p>one <b>two</b> three</p>",
            "<p>one <b>two</b>  three  </p>");
        assertCleanedHTML(
            "<!--comment-->one<![CDATA[two]]>",
            "<!--comment-->  one  <![CDATA[two]]>");
        assertCleanedHTML("<p></p>", "<p>  </p>");
        assertCleanedHTML("<p><b>text</b></p>", "<p> <b> text </b> </p>");
        assertCleanedHTML(
            "<!--startmacro:something--> <!--nonsemantic--><!--stopmacro-->",
            "  <!--startmacro:something-->  <!--nonsemantic-->  <!--stopmacro-->  ");
        assertCleanedHTML(
            "<p>one <!--startmacro:something--><!--stopmacro--></p>",
            "<p>one  <!--startmacro:something--><!--stopmacro--></p>");
        assertCleanedHTML(
            "<p>one <!--startmacro:something--><!--stopmacro--> two</p>",
            "<p>one  <!--startmacro:something--><!--stopmacro-->  two</p>");
        assertCleanedHTML(
            "<!--comment-->one<![CDATA[two]]>",
            "<!--comment-->  one<![CDATA[two]]>");
        assertCleanedHTML(
            "<p>one <!--comment-->two<![CDATA[three]]></p>",
            "<p>one <!--comment-->  two<![CDATA[three]]></p>");
        assertCleanedHTML(
            "<p>one <span>two </span><!--comment-->three<![CDATA[four]]></p>",
            "<p> one  <span>  two </span><!--comment-->  three  <![CDATA[four]]></p>");
        assertCleanedHTML(
            "<p>This<strong> Spore Cheat Sheet</strong></p>",
            "<p>This<strong>\nSpore Cheat Sheet</strong></p>");
        assertCleanedHTML(
            "<table><tbody><tr><td>First doc:<div><p>inside</p></div></td></tr></tbody></table>",
            "<table><tbody>\n<tr><td>First doc:<div>\n<p>inside</p></div></td></tr></tbody></table>");
        assertCleanedHTML(
            "<p>one two three<br></br><br></br>hello</p>",
            "<p>one two three<br/><br/>hello</p>");
        assertCleanedHTML(
            "<p><strong><span>hello</span></strong><span>world</span></p>",
            "<p><strong><span>hello</span></strong><span>world</span></p>");
        assertCleanedHTML(
            "<div><p></p><p></p></div>",
            "<div><p></p> <p></p></div>");
        assertCleanedHTML(
            "<unknow>hello word</unknow>",
            " <unknow> hello  word </unknow> ");
        assertCleanedHTML(
            "<p><unknow>hello word </unknow>text</p>",
            "<p> <unknow> hello  word </unknow> text</p>");
        assertCleanedHTML(
            "one<script> one  two </script> two",
            "one <script> one  two </script> two");
        assertCleanedHTML(
            "<span>one two</span>",
            "<span> one  two </span>",
            false);
        assertCleanedHTML(
            "one<script> one  two </script>",
            "one <script> one  two </script>");
        assertCleanedHTML(
            "one<script>//<![CDATA[\nsome script\n//]]></script>",
            "one<script>//<![CDATA[\nsome script\n//]]></script>");
        assertCleanedHTML("one <img></img> two", "one <img/> two");
    }

    public void testWhiteSpaceStrippingForBlockElements() throws Exception {
        assertCleanedHTML("<p></p><p></p>", "<p></p>  \n\r\t<p></p>");
        assertCleanedHTML(
            "<ul><li></li><li></li></ul>",
            "<ul>  <li></li>  <li></li>  </ul>");
        assertCleanedHTML("<table></table><p></p>", "<table></table> <p></p>");
    }

    private void assertCleanedHTML(String expected, String originalContent)
        throws Exception {
        assertCleanedHTML(expected, originalContent, true);
    }

    private void assertCleanedHTML(
        String expected,
        String originalContent,
        boolean protect) throws Exception {
        if (protect) {
            expected = "<html>" + expected + "</html>";
            originalContent = "<html>" + originalContent + "</html>";
        }

        InputSource source = new InputSource(new StringReader(originalContent));
        whitespaceFilter.parse(source);
        assertEquals(expected, writerFilter.getBuffer());
        writerFilter.reset();
    }
}
