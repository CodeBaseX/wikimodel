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

    @Override
    protected IWikiParser newWikiParser() {
        return new XWikiParser();
    }

    /**
     * @throws WikiParserException
     */
    public void testDefinitionLists() throws WikiParserException {
        test(";term: definition");
        test(";:just definition");
        test(";just term");
        test(";:");

        test(";this:is_not_a_term : it is an uri");

        test(";term one: definition one\n"
            + ";term two: definition two\n"
            + ";term three: definition three");

        test(";One,\ntwo,\nbucle my shoes...:\n"
            + "...Three\nfour,\nClose the door\n"
            + ";Five,\nSix:Pick up\n sticks\n\ntam-tam, pam-pam...");

        test(";__term__:''definition''");

        test("this is not a definition --\n"
            + " ;__not__ a term: ''not'' a definition\n"
            + "----toto");
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

    /**
     * @throws WikiParserException
     */
    public void testFormats() throws WikiParserException {
        test("*bold*");
        test("__bold__");
        test("~~italic~~");
        test("--strike--");
    }

    /**
     * @throws WikiParserException
     */
    public void testHeaders() throws WikiParserException {
        test("1 Heading  1");
        test("1.1 Heading 2");
        test("1.1.1 Heading 3");
        test("1.1.1.1 Heading 4");
        test("1.1.1.1.1.1 Heading 5");
        test("1.1.1.1.1.1 Heading 6");
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
        test("* first");
        test("** second");
        test("* first\n** second");
        test("*1. second");
        test("*item one\n"
            + "* item two\n"
            + "*1. item three\n"
            + "*1. item four\n"
            + "* item five - first line\n"
            + "   item five - second line\n"
            + "* item six\n"
            + "  is on multiple\n"
            + " lines");
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
    public void testQuot() throws WikiParserException {
        test("This is a paragraph\n\n and this is a quotations\n the second line");
    }

    /**
     * @throws WikiParserException
     */
    public void testReferences() throws WikiParserException {
        test("before http://www.foo.bar/com after");
        test("before [toto] after");
        test("before wiki:Hello after");
        test("before wiki~:Hello after");
        test("before [#local ancor] after");

        test("not [[a reference] at all!");
    }

    /**
     * @throws WikiParserException
     */
    public void testTables() throws WikiParserException {
        test("{table}First line\nSecond line\nThird line");

        test("|This is not a table");
        test("{table}This | is a | table");
        test("{table}This | is a | table\n{table}");
        test("before\n{table}This is \na table\n{table}after");
        test("{table}This is a table");
        test("{table}First line\nSecond line\nThird line");
    }

    /**
     * @throws WikiParserException
     */
    public void testVerbatimeInline() throws WikiParserException {
        test(
            "before{code}code{code}after",
            "<p>before<code>code</code>after</p>");
    }

    /**
     * @throws WikiParserException
     */
    public void testVerbatimeBlocks() throws WikiParserException {
        test("before\n" + "{code}code{code}" + "after", ""
            + "<p>before</p>\n"
            + "<pre>code</pre>\n"
            + "<p>after</p>");

        test("before\n"
            + "{code}\n"
            + "1 Not a header\n\n"
            + "* Not a list\n"
            + "{code}"
            + "after\n"
            + "{code}code again{code}", ""
            + "<p>before</p>\n"
            + "<pre>\n"
            + "1 Not a header\n\n"
            + "* Not a list\n"
            + "</pre>\n"
            + "<p>after</p>\n"
            + "<pre>code again</pre>");

        test("abc \n{code}123\nCDE\n345{code}efg", ""
            + "<p>abc </p>\n"
            + "<pre>123\n"
            + "CDE\n"
            + "345"
            + "</pre>\n"
            + "<p>efg</p>");
        test("abc\n{{{\n {{{ 123 \n}\\}} \n}}} efg");
        test("inline{{verbatime}}block");
        test("{{just like this...");
    }
}
