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
import org.wikimodel.wem.creole.CreoleWikiParser;
import org.wikimodel.wem.creole.javacc.ParseException;

/**
 * @author MikhailKotelnikov
 */
public class CreoleWikiParserTest extends AbstractWikiParserTest {

    /**
     * @param name
     */
    public CreoleWikiParserTest(String name) {
        super(name);
    }

    @Override
    protected IWikiParser newWikiParser() {
        return new CreoleWikiParser();
    }

    /**
     * @throws WikiParserException
     */
    public void testFormats() throws WikiParserException {
        test("**bold**");
        test("//italic//");
        test("normal**bold//bold-italic**italic//normal");
        test("*nothing special/");
        test("_nothing special_");
        test("not/an/italic/at/all");
    }

    /**
     * @throws WikiParserException
     */
    public void testHeaders() throws WikiParserException {
        test("=Header1=");
        test("==Header2==");
        test("===Header3===");
        test("====Header4====");
        test("=====Header5====");
        test("======Header6======");
        test("=======Header6(?)=======");

        test("\n===Header===\n * list item");
        test("before\n=== Header ===\nafter");
        test("before\n=== Header \nafter");
        test("This is not a header: ===");
        test("== Header**bold** //italic// ==");
    }

    /**
     * @throws WikiParserException
     */
    public void testLineBreaks() throws WikiParserException {
        test("line\\\\break");
        test("not\\a\\break");
    }

    /**
     * @throws WikiParserException
     */
    public void testLists() throws WikiParserException {
        test(" * item one\n"
            + " * item two\n"
            + "   # item three\n"
            + "   # item four\n"
            + " * item five - first line\n"
            + "   item five - second line\n"
            + " * item six\n"
            + "   is on multiple\n"
            + "   lines");
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
     * @throws ParseException
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
    public void testTables() throws WikiParserException {
        test("|= Header 1.1 |= Header 1.2\n"
            + "| Cell 2.1 | Cell 2.2\n"
            + "| Cell 3.1 |= Head 3.2");
        test("abc || cde");
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
