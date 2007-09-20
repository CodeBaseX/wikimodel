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
import org.wikimodel.wem.common.CommonWikiParser;

/**
 * @author MikhailKotelnikov
 */
public class CommonWikiParserTest extends AbstractWikiParserTest {

    /**
     * @param name
     */
    public CommonWikiParserTest(String name) {
        super(name);
    }

    @Override
    protected IWikiParser newWikiParser() {
        return new CommonWikiParser();
    }

    public void test() throws WikiParserException {
        test("----------------------------------------------\r\n"
            + "= Example1 =\r\n"
            + "\r\n"
            + "The table below contains an embedded document.\r\n"
            + "Using such embedded documents you can insert table\r\n"
            + "in a list or a list in a table. And embedded documents\r\n"
            + "can contain their own embedded documents!!!\r\n"
            + "\r\n"
            + "!! Header 1.1 !! Header 1.2\r\n"
            + ":: Cell 2.1 :: Cell 2.2 with an embedded document: (((\r\n"
            + "== This is an embedded document! ==\r\n"
            + "* list item one\r\n"
            + "* list item two\r\n"
            + "  * sub-item A\r\n"
            + "  * sub-item B\r\n"
            + "* list item three\r\n"
            + ")))\r\n"
            + ":: Cell 3.1 :: Cell 3.2\r\n"
            + "\r\n"
            + "This is a paragraphs after the table...\r\n"
            + "----------------------------------------------\r\n"
            + "");
    }

    /**
     * @throws WikiParserException
     */
    public void testDocuments() throws WikiParserException {
        test("before ((( inside ))) after ");
        test("before inside ))) after ");
        test("before (((\ninside ))) after ");
        test("before (((\n inside ))) after ");
        test("| Line One | First doc: (((\n inside ))) after \n"
            + "|Line Two | Second doc: (((lkjlj))) skdjg");
        test("| This is a table: | (((* item one\n"
            + "* item two\n"
            + " * subitem 1\n"
            + " * subitem 2\n"
            + "* item three))) ");

        test("before ((( opened and not closed");
        test("before ((( one ((( two ((( three ");
    }

    /**
     * @throws WikiParserException
     */
    public void testEscape() throws WikiParserException {
        test("[a reference]");
        test("\\[not a reference]");

        test("\\First letter is escaped");
        test("\\[not a reference]");
        test("\\\\escaped backslash");
        test("\\ a line break because it is followed by a space");

        test("= Heading =\n\\= Not a heading =\n= Heading again! =");
    }

    /**
     * @throws WikiParserException
     */
    public void testExtensions() throws WikiParserException {
        test("abc $abc after");
        test("before\n$abc after");

        test("abc $abc(hello)");
        test("before\n$abc(hello)after");

        test("before $inlineExtension after");
        test("before$inlineExtension()after");
        test(" $inlineExtension  ");

        test("$blockExtension()after");
    }

    /**
     * @throws WikiParserException
     */
    public void testFormats() throws WikiParserException {
        test("*bold* ", "<p><strong>bold</strong> </p>");
        test(" **bold** ", "<p> <strong>bold</strong> </p>");
        test("__italic__", "<p><em>italic</em></p>");

        test("*strong*", "<p><strong>strong</strong></p>");
        test(" *strong*", "<p> <strong>strong</strong></p>");
        test("__em__", "<p><em>em</em></p>");
        test("$$code$$", "<p><code>code</code></p>");
        test("^^sup^^", "<p><sup>sup</sup></p>");
        test("~~sub~~", "<p><sub>sub</sub></p>");

        // These special symbols ("--" and "++") at the begining of the line are
        // interpreted as list markers (see {@link #testLists()} method)
        test("before++big++after", "<p>before<big>big</big>after</p>");
        test("before--small--after", "<p>before<small>small</small>after</p>");

        test("@@ins@@", "<p><ins>ins</ins></p>");
        test("##del##", "<p><del>del</del></p>");

        test(""
            + "before"
            + "*bold*"
            + "__italic__"
            + "^^superscript^^"
            + "~~subscript~~"
            + "value after", "<p>"
            + "before"
            + "<strong>bold</strong>"
            + "<em>italic</em>"
            + "<sup>superscript</sup>"
            + "<sub>subscript</sub>"
            + "value after"
            + "</p>");

        // "Bad-formed" formatting
        test("normal*bold__bold-italic*italic__normal", "<p>"
            + "normal<strong>bold</strong>"
            + "<strong><em>bold-italic</em></strong>"
            + "<em>italic</em>normal"
            + "</p>");

        // Auto-closing (non used) style formatting at the end of lines.
        test("not a bold__", "<p>not a bold</p>");
        test("not an italic__", "<p>not an italic</p>");

        test("text*", "<p>text</p>");
        test("text**", "<p>text</p>");
        test("text__", "<p>text</p>");
        test("text$$", "<p>text</p>");
        test("text^^", "<p>text</p>");
        test("text~~", "<p>text</p>");

    }

    /**
     * @throws WikiParserException
     */
    public void testHeaders() throws WikiParserException {
        test("=Header1", "<h1>Header1</h1>");
        test("==Header2", "<h2>Header2</h2>");
        test("===Header3", "<h3>Header3</h3>");
        test("====Header4", "<h4>Header4</h4>");
        test("before\n= Header =\nafter", "<p>before</p>\n"
            + "<h1>Header </h1>\n"
            + "<p>after</p>");

        test("This is not a header: ==", "<p>This is not a header: ==</p>");

        test("{{a=b}}\n=Header1", "<h1 a='b'>Header1</h1>");
    }

    /**
     * @throws WikiParserException
     */
    public void testHorLine() throws WikiParserException {
        test("----");
        test("-------");
        test("-----------");
        test(" -----------");
        test("---abc");
    }

    /**
     * @throws WikiParserException
     */
    public void testInfo() throws WikiParserException {
        test("/!\\");
        test("/i\\info");
        test("/i\\Information block:\n"
            + "first line\n"
            + "second line\n"
            + "third  line");
        test("{{a=b}}\n/!\\");
        test("{{a=b}}\n/i\\info");
    }

    /**
     * @throws WikiParserException
     */
    public void testLineBreak() throws WikiParserException {
        test("abc\\\ndef");
        test("abc\\  \ndef");
        test("abc\\ x \ndef");
        test("abc x \ndef");
    }

    /**
     * @throws WikiParserException
     */
    public void testLists() throws WikiParserException {
        test("* first");
        test("*this is a bold, and not a list");
        test("** second");
        test("* item one\n"
            + "* item two\n"
            + "*+item three\n"
            + "*+ item four\n"
            + "* item five - first line\n"
            + "   item five - second line\n"
            + "* item six\n"
            + "  is on multiple\n"
            + " lines");

        test(";term:  definition");
        test(";just term");
        test(":just definition");
        test(";:just definition");
        test(":just definition");
        test(";:");
        test(": Indenting is stripped out.\r\n"
            + " : Includes double indenting");

        test(";term one: definition one\n"
            + ";term two: definition two\n"
            + ";term three: definition three");

        test(";One,\ntwo,\nbucle my shoes...:\n"
            + "...Three\nfour,\nClose the door\n"
            + ";Five,\nSix: Pick up\n sticks\n\ntam-tam, pam-pam...");

        test(";__term__: *definition*");

        test("this is not a definition --\n"
            + " ;__not__ a term: ''not'' a definition\n"
            + "----toto");

        test("{{a='b'}}\n* item one");
    }

    /**
     * @throws WikiParserException
     */
    public void testParagraphs() throws WikiParserException {
        test("{{background='blue'}}");
        test("{{background='blue'}}\n{{background=\'red\'}}\n{{background=\'green\'}}");

        test("{{background='blue'}}hello");
        test("{{background='blue'}}\n"
            + "First paragraph\r\n"
            + "\r\n"
            + "\r\n"
            + "\r\n"
            + "");

        test("First paragraph\r\n" + "\r\n" + "\r\n" + "\r\n" + "");
        test("First paragraph.\n"
            + "Second line of the same paragraph.\n"
            + "\n"
            + "The second paragraph");

        test("\n<toto");
    }

    /**
     * @throws WikiParserException
     */
    public void testProperties() throws WikiParserException {
        test("%toto hello  world\n123");
        test("%prop1 value1\n%prop2 value2");
        test("%prop1 (((embedded)))next paragraph\n%prop2 value2");
        test("before\r\n"
            + "\r\n"
            + "%company (((\r\n"
            + "    %name Cognium Systems\r\n"
            + "    %addr (((\r\n"
            + "        %country France\r\n"
            + "        %city Paris\r\n"
            + "        %street Cité Nollez\r\n"
            + "        %building 10\r\n"
            + "        This is just a description...\r\n"
            + "    )))\r\n"
            + ")))\r\n"
            + "\r\n"
            + "after");
        test("before\r\n"
            + "\r\n"
            + "%company (((\r\n"
            + "    %name Cognium Systems\r\n"
            + "    %addr (((\r\n"
            + "        %country France\r\n"
            + "        %city Paris\r\n"
            + "        %street Cité Nollez\r\n"
            + "        %building 10\r\n"
            + "        This is just a description...\r\n"
            + "    \r\n"
            + "\r\n"
            + "\r\n"
            + "after");

        test("before %prop(value) after");
        test("before %foo:bar:toto.com/titi/tata?query=x#ancor(value) after");
        test("before %prop(before*bold*__italic__^^superscript^^~~subscript~~value) after");
    }

    /**
     * @throws WikiParserException
     */
    public void testQuot() throws WikiParserException {
        test("Q: Quotation");

        test(">This is a message\n"
            + ">>and this is a response to the message \n"
            + "> This is a continuation of the same message");

        test("This is a paragraph\n"
            + ">and this is a quotations\n"
            + "> the second line");
        test("        This is just a description...\r\n"
            + "    \r\n"
            + "\r\n"
            + "\r\n");
        test("  first\n"
            + "  second\n"
            + "  third\n"
            + "    subquot1\n"
            + "    subquot2"
            + "  fourth");
        test("{{a='b'}}\n"
            + "  first\n"
            + "  second\n"
            + "  third\n"
            + "    subquot1\n"
            + "    subquot2"
            + "  fourth");

    }

    /**
     * @throws WikiParserException
     */
    public void testReferences() throws WikiParserException {
        test("Это (=ссылка=) на внешний документ...");
        test("This is a (=reference=) to an external document...");

        test("before http://www.foo.bar/com after");
        test("before http://www.foo.bar/com?q=abc#ancor after");
        test("before wiki:Hello after");
        test("before wiki\\:Hello after");

        test("before abc:cde#efg after");
        test("before abc:cde#efg after");
        test("before first:second:third:anonymous@hello/path/?query=value#ancor after");

        test("download:MyDoc.pdf");
        test("Reference: download:MyDoc.pdf :not a reference");

        test("http://123.234.245.34/toto/titi/MyDoc.pdf");

        // Not references
        test("download::MyDoc.pdf");
        test("before abc::after");
        test("before abc: after");
        test("before abc# after");
        test("before #abc after");
        test("before abc:#cde after");

        // Explicit references.
        test("before [toto] after");
        test("before (=toto=) after");
        test("before [#local ancor] after");

        test("before (((doc-before(=toto=)doc-after))) after");
        test("before ((((=toto=)))) after");
        test(" ((((=toto=))))");
        test("((((=toto=))))");
        test("((((((toto))))))");
        test("\n(((a(((toto)))b)))");
    }

    /**
     * @throws WikiParserException
     */
    public void testSpecialSymbols() throws WikiParserException {
        test(":)");
    }

    /**
     * @throws WikiParserException
     */
    public void testTables() throws WikiParserException {
        test("!! Header :: Cell ", ""
            + "<table>\n"
            + "  <tr><th>Header</th><td>Cell </td></tr>\n"
            + "</table>");
        test("!!   Header    ::    Cell    ", ""
            + "<table>\n"
            + "  <tr><th>Header</th><td>Cell    </td></tr>\n"
            + "</table>");

        test("::Cell 1 :: Cell 2");
        test("Not a Header :: Not a Cell");
        test("Not a Header::Not a Cell");
        test(":Term definition");
        test(";:Term definition");

        test("|| cell 1.1 || cell 1.2\n" + "|| cell 2.1|| cell 2.2");
        test("|| Head 1.1 || Head 1.2\n" + "| cell 2.1| cell 2.2");
        test("|| Multi \nline  \nheader \n"
            + "| Multi\nline\ncell\n\nOne,two,three...");
        test("this is not || a table");
        test("this is not | a table");
        test("|| ''Italic header'' || __Bold header__\n"
            + "| ''Italic cell'' | __Bold cell__\n");
        // Bad formed formatting
        test("|| ''Italic header || __Bold header \n"
            + "| ''Italic cell | __Bold cell\n");

        test("{{a=b}}\n|| cell");

        // Row parameters
        test("{{a=b}}||cell");
        test("{{a=b}}::cell1\n{{c=d}}::cell2");

        test("{{a=b}}\n{{c=d}}||{{e=f}} cell");
        test("{{a=b}}\n{{c=d}}::{{e=f}} cell ::{{g=h}}");

    }

    /**
     * @throws WikiParserException
     */
    public void testVerbatimeBlocks() throws WikiParserException {
        test("{{{verbatim}}}", "<pre>verbatim</pre>");
        test("{{{ver\\}}}batim}}}", "<pre>ver}}}batim</pre>");
        test("before{{{verbatim}}}after", "<p>before</p>\n"
            + "<pre>verbatim</pre>\n"
            + "<p>after</p>");
        test("{{{verbatim", "<pre>verbatim</pre>");
        test("{{{{{{verbatim", "<pre>{{{verbatim</pre>");
        test("{{{{{{verbatim}}}", "<pre>{{{verbatim</pre>");
        test("{{{{{{verbatim}}}}}}", "<pre>{{{verbatim}}}</pre>");
        test(
            "{{{before{{{verbatim}}}after}}}",
            "<pre>before{{{verbatim}}}after</pre>");

        test(
            "{{{before{{{123{{{verbatim}}}456}}}after}}}",
            "<pre>before{{{123{{{verbatim}}}456}}}after</pre>");
        test(
            "{{{verbatim}}}}}} - the three last symbols should be in a paragraph",
            "<pre>verbatim</pre>\n"
                + "<p>}}} - the three last symbols should be in a paragraph</p>");

        // Complex formatting
        test("!! Syntax !! Results\n"
            + ":: {{{\n"
            + "!! Header 1 !! Header 2\n"
            + ":: Cell 1 :: Cell 2\n"
            + "}}} :: (((\n"
            + "!! Header 1 !! Header 2\n"
            + ":: Cell 1 :: Cell 2\n"
            + ")))\n"
            + ":: {{{\n"
            + "|| Header 1 || Header 2\n"
            + "| Cell 1 | Cell 2\n"
            + "}}} :: (((\n"
            + "|| Header 1 || Header 2\n"
            + "| Cell 1 | Cell 2\n"
            + ")))\n"
            + "");
    }

    public void testVerbatimInlineElements() throws WikiParserException {
        test("`verbatim`", "<p><code>verbatim</code></p>");
        test("before`verbatim`after", "<p>before<code>verbatim</code>after</p>");

        // Bad formed elements
        test("`verbatim", "<p>`verbatim</p>");
        test("before`after", "<p>before`after</p>");
        test("before`after\nnext line", "<p>before`after\nnext line</p>");
    }

}
