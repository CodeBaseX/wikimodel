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

import org.wikimodel.wem.IWikiParser;
import org.wikimodel.wem.WikiParserException;
import org.wikimodel.wem.xhtml.XhtmlParser;

/**
 * @author MikhailKotelnikov
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
    public void testDefinitionLists() throws WikiParserException {
        test("<html><dl><dt>term</dt><dd>definition</dd></dl></html>");
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
        test("<html>before<hr />after</html>");
    }

    /**
     * @throws WikiParserException
     */
    public void testLineBreak() throws WikiParserException {
    }

    /**
     * @throws WikiParserException
     */
    public void testLists() throws WikiParserException {
        // TODO: add management of embedded block elements.
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
    }

    /**
     * @throws WikiParserException
     */
    public void testParagraphs() throws WikiParserException {
    }

    /**
     * @throws WikiParserException
     */
    public void testQuot() throws WikiParserException {
    }

    /**
     * @throws WikiParserException
     */
    public void testReferences() throws WikiParserException {
    }

    /**
     * @throws WikiParserException
     */
    public void testTables() throws WikiParserException {
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
