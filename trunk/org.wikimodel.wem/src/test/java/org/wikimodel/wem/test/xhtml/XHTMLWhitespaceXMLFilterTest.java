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
 */
public class XHTMLWhitespaceXMLFilterTest extends TestCase
{
    private XMLWriter writerFilter;
    private XHTMLWhitespaceXMLFilter whitespaceFilter;
    
    protected void setUp() throws Exception
    {
        XMLReader xmlReader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
        whitespaceFilter = new XHTMLWhitespaceXMLFilter(xmlReader);
        writerFilter = new XMLWriter();

        whitespaceFilter.setFeature("http://xml.org/sax/features/namespaces", true);
        whitespaceFilter.setContentHandler(writerFilter);
        whitespaceFilter.setProperty("http://xml.org/sax/properties/lexical-handler", writerFilter);
    }

    public void testWhiteSpaceStripping() throws Exception
    {
        assertCleanedHTML("<p>one two</p>", "<p>  one  two  </p>");
        assertCleanedHTML("<p>one two <b>three</b></p>", "<p>  one  two  <b>three</b></p>");
        assertCleanedHTML("<p>one two</p>", "<p>\n\r\tone\n\r\ttwo\n\r\t</p>");
        assertCleanedHTML("<p>one <b>two</b> <b>three</b></p>", "<p>one <b>two</b>  <b>three</b></p>");
        assertCleanedHTML("<p>one</p> two <p>three</p>", "<p>one</p>  two  <p>three</p>");
        assertCleanedHTML("<![CDATA[\n  one  \n]]>", "<![CDATA[\n  one  \n]]>");
        assertCleanedHTML("<pre>\n  one  \n</pre>", "<pre>\n  one  \n</pre>");
        assertCleanedHTML("<p>one<!--comment--> two</p>", "<p>  one  <!--comment-->  two  </p>");
        assertCleanedHTML("<p><!--comment-->one <b>two</b></p>", "<p><!--comment-->  one  <b>two</b></p>");
        assertCleanedHTML("<p>one <b>two</b> three</p>", "<p>one <b>two</b>  three  </p>");
        assertCleanedHTML("<!--comment-->one <![CDATA[two]]>", "<!--comment-->  one  <![CDATA[two]]>");
        assertCleanedHTML("<p></p>", "<p>  </p>");
        assertCleanedHTML(" <!--startmacro:something--><!--nonsemantic--> <!--stopmacro-->", 
            "  <!--startmacro:something-->  <!--nonsemantic-->  <!--stopmacro-->  ");
        assertCleanedHTML("<p>one <!--startmacro:something--><!--stopmacro--> two</p>", 
            "<p>one  <!--startmacro:something--><!--stopmacro-->  two</p>");
        assertCleanedHTML("<!--comment-->one<![CDATA[two]]>", "<!--comment-->  one<![CDATA[two]]>");
        assertCleanedHTML("<p>one<!--comment--> two<![CDATA[three]]></p>", "<p>one <!--comment-->  two<![CDATA[three]]></p>");
        assertCleanedHTML("<p>one <span>two</span><!--comment--> three <![CDATA[four]]></p>", 
            "<p> one  <span>  two </span><!--comment-->  three  <![CDATA[four]]></p>");
    }
    
    public void testWhiteSpaceStrippingForBlockElements() throws Exception
    {
        assertCleanedHTML("<p></p><p></p>", "<p></p>  \n\r\t<p></p>");
        assertCleanedHTML("<ul><li></li><li></li></ul>", "<ul>  <li></li>  <li></li>  </ul>");
        assertCleanedHTML("<table></table><p></p>", "<table></table> <p></p>");
    }
    
    private void assertCleanedHTML(String expected, String originalContent) throws Exception
    {
        InputSource source = new InputSource(new StringReader("<html>" + originalContent + "</html>"));
        whitespaceFilter.parse(source);
        assertEquals("<html>" + expected + "</html>", writerFilter.getBuffer());
        writerFilter.reset();
    }
}
