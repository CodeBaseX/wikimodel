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

import java.io.IOException;
import java.io.StringReader;

import org.wikimodel.wem.IWemListener;
import org.wikimodel.wem.IWikiParser;
import org.wikimodel.wem.IWikiPrinter;
import org.wikimodel.wem.PrintListener;
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

	/**
	 * @throws WikiParserException
	 */
	public void testInfo() throws WikiParserException {
		test("/i\\ item {{{formatted block}}} {macro}123{/macro} after");
		test("before\n" + "/i\\Information block:\n" + "{{{pre\n"
				+ "  formatted\n" + " block}}} sdlkgj\n" + "qsdg\n\n" + "after");
		test("/!\\");
		test("/i\\info");
		test("/i\\Information block:\n" + "first line\n" + "second line\n"
				+ "third  line");
		test("{{a=b}}\n/!\\");
		test("{{a=b}}\n/i\\info");
	}

	public void test() throws WikiParserException {
		test("%rdf:type toto:Document\r\n" + "\r\n" + "%title Hello World\r\n"
				+ "\r\n" + "%summary This is a short description\r\n"
				+ "%locatedIn (((\r\n" + "    %type [City]\r\n"
				+ "    %name [Paris]\r\n" + "    %address (((\r\n"
				+ "      %building 10\r\n" + "      %street Cité Nollez\r\n"
				+ "    ))) \r\n" + ")))\r\n" + "= Hello World =\r\n" + "\r\n"
				+ "* item one\r\n" + "  * sub-item a\r\n"
				+ "  * sub-item b\r\n" + "    + ordered X \r\n"
				+ "    + ordered Y\r\n" + "  * sub-item c\r\n"
				+ "* item two\r\n" + "\r\n" + "\r\n"
				+ "The table below contains \r\n"
				+ "an %seeAlso(embedded document). \r\n"
				+ "It can contain the same formatting \r\n"
				+ "elements as the root document.\r\n" + "\r\n" + "\r\n"
				+ "!! Table Header 1.1 !! Table Header 1.2\r\n"
				+ ":: Cell 2.1 :: Cell 2.2 (((\r\n"
				+ "== Embedded document ==\r\n"
				+ "This is an embedded document:\r\n" + "* item X\r\n"
				+ "* item Y\r\n" + "))) The text goes after the embedded\r\n"
				+ " document\r\n" + ":: Cell 3.1 :: Cell 3.2");
		test("----------------------------------------------\r\n"
				+ "= Example1 =\r\n" + "\r\n"
				+ "The table below contains an embedded document.\r\n"
				+ "Using such embedded documents you can insert table\r\n"
				+ "in a list or a list in a table. And embedded documents\r\n"
				+ "can contain their own embedded documents!!!\r\n" + "\r\n"
				+ "!! Header 1.1 !! Header 1.2\r\n"
				+ ":: Cell 2.1 :: Cell 2.2 with an embedded document: (((\r\n"
				+ "== This is an embedded document! ==\r\n"
				+ "* list item one\r\n" + "* list item two\r\n"
				+ "  * sub-item A\r\n" + "  * sub-item B\r\n"
				+ "* list item three\r\n" + ")))\r\n"
				+ ":: Cell 3.1 :: Cell 3.2\r\n" + "\r\n"
				+ "This is a paragraphs after the table...\r\n"
				+ "----------------------------------------------\r\n" + "");
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
		test("| This is a table: | (((* item one\n" + "* item two\n"
				+ " * subitem 1\n" + " * subitem 2\n" + "* item three))) ");

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

		test("" + "before" + "*bold*" + "__italic__" + "^^superscript^^"
				+ "~~subscript~~" + "value after", "<p>" + "before"
				+ "<strong>bold</strong>" + "<em>italic</em>"
				+ "<sup>superscript</sup>" + "<sub>subscript</sub>"
				+ "value after" + "</p>");

		// "Bad-formed" formatting
		test("normal*bold__bold-italic*italic__normal", "<p>"
				+ "normal<strong>bold</strong>"
				+ "<strong><em>bold-italic</em></strong>"
				+ "<em>italic</em>normal" + "</p>");

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
				+ "<h1>Header </h1>\n" + "<p>after</p>");

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
		test("*this is a bold, and not a list",
				"<p><strong>this is a bold, and not a list</strong></p>");
		test("**bold**", "<p><strong>bold</strong></p>");

		test("* first", "<ul>\n  <li>first</li>\n</ul>");
		test("** second",
				"<ul>\n  <li><ul>\n  <li>second</li>\n</ul>\n</li>\n</ul>");

		test("* item one\n" + "* item two\n" + "*+item three\n"
				+ "*+ item four\n" + "* item five - first line\n"
				+ "   item five - second line\n" + "* item six\n"
				+ "  is on multiple\n" + " lines");

		test(
				"* item {{{formatted block}}} {macro}123{/macro} after",
				"<ul>\n"
						+ "  <li>item <pre>formatted block</pre>\n"
						+ " <span class='macro' macroName='macro'><![CDATA[123]]></span> after</li>\n</ul>");

		test("? term:  definition");
		test("?just term");
		test(":just definition");
		test(";:just definition");
		test(":just definition");
		test(";:");
		test(": Indenting is stripped out.\r\n"
				+ " : Includes double indenting");

		test(";term one: definition one\n" + ";term two: definition two\n"
				+ ";term three: definition three");

		test(";One,\ntwo,\nbucle my shoes...:\n"
				+ "...Three\nfour,\nClose the door\n"
				+ ";Five,\nSix: Pick up\n sticks\n\ntam-tam, pam-pam...");

		test(";__term__: *definition*");

		test("this is not a definition --\n"
				+ " ;__not__ a term: ''not'' a definition\n" + "----toto");

		test("{{a='b'}}\n* item one");
	}

	public void testMacro() throws WikiParserException {
		test("{toto}a{toto}b{/toto}c{/toto}");

		test("{toto}");
		test("{toto}a{/toto}");
		test("before{toto}macro{/toto}after");
		test("before{toto a=b c=d}toto macro tata {/toto}after");
		test("before{toto a=b c=d}toto {x qsdk} macro {sd} tata {/toto}after");

		test("{toto}a{toto}");
		test("- before\n" + "{code}this is a code{/code} \n"
				+ " this is afer the code...");

		// Not macros
		test("{ toto a=b c=d}");

		// Macro and its usage
		test("This is a macro: {toto x:a=b x:c=d}\n" + "<table>\n"
				+ "#foreach ($x in $table)\n" + "  <tr>hello, $x</tr>\n"
				+ "#end\n" + "</table>\n" + "{/toto}\n\n"
				+ "And this is a usage of this macro: $toto(a=x b=y)");

		test("!!Header:: Cell with a macro: \n"
				+ " {code}this is a code{/code} \n"
				+ " this is afer the code...");
		test("" + "* item one\n" + "* item two\n"
				+ "  * subitem with a macro:\n"
				+ "  {code} this is a code{/code} \n"
				+ "  the same item (continuation)\n" + "  * subitem two\n"
				+ "* item three");
	}

	/**
	 * @throws WikiParserException
	 */
	public void testParagraphs() throws WikiParserException {
		test("{{background='blue'}}", "<p background='blue'></p>");
		test("" + "{{background='blue'}}\n" + "{{background='red'}}\n"
				+ "{{background='green'}}", "" + "<p background='blue'></p>\n"
				+ "<p background='red'></p>\n" + "<p background='green'></p>");
		test("" + "{{background='blue'}}first\n"
				+ "{{background='red'}}second\n"
				+ "{{background='green'}}third", ""
				+ "<p background='blue'>first</p>\n"
				+ "<p background='red'>second</p>\n"
				+ "<p background='green'>third</p>");
		test("" + "{{background='blue'}}\nfirst\n"
				+ "{{background='red'}}\nsecond\n"
				+ "{{background='green'}}\nthird", ""
				+ "<p background='blue'>first</p>\n"
				+ "<p background='red'>second</p>\n"
				+ "<p background='green'>third</p>");

		test("{{background='blue'}}hello", "<p background='blue'>hello</p>");
		test("{{background='blue'}}\n" + "First paragraph\r\n" + "\r\n"
				+ "\r\n" + "\r\n" + "", ""
				+ "<p background='blue'>First paragraph</p>\n"
				+ "<div style='height:3em;'></div>");

		test("First paragraph\r\n" + "\r\n" + "\r\n" + "\r\n" + "");
		test("First paragraph.\n" + "Second line of the same paragraph.\n"
				+ "\n" + "The second paragraph");

		test("\n<toto");
	}

	/**
	 * @throws WikiParserException
	 */
	public void testProperties() throws WikiParserException {
		test("%toto hello  world\n123");
		test("%prop1 value1\n%prop2 value2");
		test("%prop1 (((embedded)))next paragraph\n%prop2 value2");
		test("before\r\n" + "\r\n" + "%company (((\r\n"
				+ "    %name Cognium Systems\r\n" + "    %addr (((\r\n"
				+ "        %country France\r\n" + "        %city Paris\r\n"
				+ "        %street Cité Nollez\r\n"
				+ "        %building 10\r\n"
				+ "        This is just a description...\r\n" + "    )))\r\n"
				+ ")))\r\n" + "\r\n" + "after");
		test("before\r\n" + "\r\n" + "%company (((\r\n"
				+ "    %name Cognium Systems\r\n" + "    %addr (((\r\n"
				+ "        %country France\r\n" + "        %city Paris\r\n"
				+ "        %street Cité Nollez\r\n"
				+ "        %building 10\r\n"
				+ "        This is just a description...\r\n" + "    \r\n"
				+ "\r\n" + "\r\n" + "after");

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

		test("This is a paragraph\n" + ">and this is a quotations\n"
				+ "> the second line");
		test("        This is just a description...\r\n" + "    \r\n" + "\r\n"
				+ "\r\n");
		test("> first\n" + ">> second\n" + ">> third\n" + ">>> subquot1\n"
				+ ">>> subquot2\n" + ">> fourth");
		test("{{a='b'}}\n" + "  first\n" + "  second\n" + "  third\n"
				+ "    subquot1\n" + "    subquot2" + "  fourth");

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

	// /**
	// * @throws WikiParserException
	// */
	// public void testTables() throws WikiParserException {
	// test("!! Header :: Cell ", ""
	// + "<table><tbody>\n"
	// + " <tr><th>Header</th><td>Cell </td></tr>\n"
	// + "</tbody></table>");
	// test("!! Header :: Cell ", ""
	// + "<table><tbody>\n"
	// + " <tr><th>Header</th><td>Cell </td></tr>\n"
	// + "</tbody></table>");
	//
	// test("::Cell 1 :: Cell 2");
	// test("Not a Header :: Not a Cell");
	// test("Not a Header::Not a Cell");
	// test(":Term definition");
	// test(";:Term definition");
	//
	// test("|| cell 1.1 || cell 1.2\n" + "|| cell 2.1|| cell 2.2");
	// test("|| Head 1.1 || Head 1.2\n" + "| cell 2.1| cell 2.2");
	// test("|| Multi \nline \nheader \n"
	// + "| Multi\nline\ncell\n\nOne,two,three...");
	// test("this is not || a table");
	// test("this is not | a table");
	// test("|| ''Italic header'' || __Bold header__\n"
	// + "| ''Italic cell'' | __Bold cell__\n");
	// // Bad formed formatting
	// test("|| ''Italic header || __Bold header \n"
	// + "| ''Italic cell | __Bold cell\n");
	//
	// test("{{a=b}}\n|| cell");
	//
	// // Row parameters
	// test("{{a=b}}||cell");
	// test("{{a=b}}::cell1\n{{c=d}}::cell2");
	//
	// test("{{a=b}}\n{{c=d}}||{{e=f}} cell");
	// test("{{a=b}}\n{{c=d}}::{{e=f}} cell ::{{g=h}}");
	//
	// }

	/**
	 * @throws WikiParserException
	 */
	public void testVerbatimeBlocks() throws WikiParserException {
		test("{{{verbatim}}}", "<pre>verbatim</pre>");
		test("{{{ver\\}}}batim}}}", "<pre>ver}}}batim</pre>");
		test("before{{{verbatim}}}after", "<p>before</p>\n"
				+ "<pre>verbatim</pre>\n" + "<p>after</p>");
		test("{{{verbatim", "<pre>verbatim</pre>");
		test("{{{{{{verbatim", "<pre>{{{verbatim</pre>");
		test("{{{{{{verbatim}}}", "<pre>{{{verbatim</pre>");
		test("{{{{{{verbatim}}}}}}", "<pre>{{{verbatim}}}</pre>");
		test("{{{before{{{verbatim}}}after}}}",
				"<pre>before{{{verbatim}}}after</pre>");

		test("{{{before{{{123{{{verbatim}}}456}}}after}}}",
				"<pre>before{{{123{{{verbatim}}}456}}}after</pre>");
		test(
				"{{{verbatim}}}}}} - the three last symbols should be in a paragraph",
				"<pre>verbatim</pre>\n"
						+ "<p>}}} - the three last symbols should be in a paragraph</p>");

		// Complex formatting
		test("!! Syntax !! Results\n" + ":: {{{\n"
				+ "!! Header 1 !! Header 2\n" + ":: Cell 1 :: Cell 2\n"
				+ "}}} :: (((\n" + "!! Header 1 !! Header 2\n"
				+ ":: Cell 1 :: Cell 2\n" + ")))\n" + ":: {{{\n"
				+ "|| Header 1 || Header 2\n" + "| Cell 1 | Cell 2\n"
				+ "}}} :: (((\n" + "|| Header 1 || Header 2\n"
				+ "| Cell 1 | Cell 2\n" + ")))\n" + "");
	}

	public void testVerbatimInlineElements() throws WikiParserException {
		test("`verbatim`", "<p><code>verbatim</code></p>");
		test("before`verbatim`after", "<p>before<code>verbatim</code>after</p>");

		// Bad formed elements
		test("`verbatim", "<p>`verbatim</p>");
		test("before`after", "<p>before`after</p>");
		test("before`after\nnext line", "<p>before`after\nnext line</p>");
	}

	String[] tokenManagerExceptionCauses = {
			"The link can also be a direct URL starting with {{http:}}, {{ftp:}}, {{mailto:}}, {{https:}}, or {{news:}}, in which case the link points to an external entity. For example, to point at the java.sun.com home page, use {{[[http://java.sun.com]}}, which becomes [http://java.sun.com/] or {{[[Java home page|http://java.sun.com]}}, which becomes [Java home page|http://java.sun.com].",
			"Das anlegen einer neuen Instanz erfolgt mit zope-eigenen Skript {{/export/zope/bin/mkzopeinstance.py}}",
			"Deduktionstheorem: M |= F <=> M u {-F} widerspruchsvoll <=> (M u -F) unerfüllbar",
			" title  = {{OWL} {W}eb {O}ntology {L}anguage {G}uide},",
			"{\"{o}} = ö",
			"<tr><td><code>Include-Resource</code></td><td><a href='#LIST'>LIST</a> of iclause</td><td>The Include-Resource instruction makes it possible to include arbitrary resources; it contains a list of resource paths. The resources will be copied into the target jar file. The iclause can have the following forms:<br />&nbsp;<br /><code>iclause    ::= inline | copy</code><br /><code>copy       ::= '{' process '}' | process</code><br /><code>process    ::= assignment | simple</code><br /><code>assignment ::= PATH '=' PATH</code><br /><code>simple     ::= PATH</code><br /><code>inline     ::= '@' PATH ( '!/' PATH? ('/**' | '/*')? )?</code><br />&nbsp;<br />In the case of <code>assignment</code> or <code>simple</code>, the PATH parameter can point to a file or directory. The <code>simple</code> form will place the resource in the target JAR with only the file name, therefore without any path components. That is, including src/a/b.c will result in a resource b.c in the root of the target JAR. If the PATH points to a directory, the directory name itself is not used in the target JAR path. If the resource must be placed in a subdirectory of the target jar, use the <code>assignment</code> form. The <code>inline</code> requires a ZIP or JAR file, which will be completely expanded in the target JAR, unless followed with a file specification. The file specification can be a specific file in the jar or a directory followed by ** or *. The ** indicates recursively and the * indicates one level. If just a directory name is given, it will mean **.<br />The <code>simple</code> and <code>assigment</code> forms can be encoded with curly braces, like <code>{foo.txt}</code>. This indicates that the file should be preprocessed (or filtered as it is sometimes called). Preprocessed files can use the same variables and macros as defined in the <a href='#macros'>macro section</a>.<br />&nbsp;<br /><code>Include-Resource: @osgi.jar, <br />&nbsp;{LICENSE.txt}, <br />&nbsp;acme/Merge.class=src/acme/Merge.class</code></td></tr>",
			"note ={\\url{http://heikohaller.de/literatur/diplomarbeit/}},",
			"The magical incantation in the {{jspwiki.properties}} file is:",
			"Then set your variable to true or false by \\newcommand{\\condition}{true} or \\newcommand{\\condition}{false}.",
			"* Inpired by the Java types, we also define a {{SortedSet}} as ol with {{class=\"noduplicates\"}}.",
			";:''__Tip__: If you want to insert the page name without the spaces, use {{<wiki:Variable var=\"pagename\" />}}.''",
			"\\hfill {\\it Max V\"{o}lkel} \\",
			"     <td headers=\"matches\">Punctuation: One of <tt>!\"#$%&'()*+,-./:;<=>?@[[\\]^_`{|}~</tt></td></tr>",
			"ein Label \"{{global{global} }}\" gibt",
			"* unsafe: {space}, ?<>#%{}|\\^~[]`" };

	public void testIssue6() throws IOException, WikiParserException {
		for (String in : tokenManagerExceptionCauses) {
			IWikiParser wikiParser = new CommonWikiParser();
			IWemListener listener = new PrintListener(new NullPrinter());
			wikiParser.parse(new StringReader(in), listener);
		}
	}

	class NullPrinter implements IWikiPrinter {
		public void print(String str) {
		}

		public void println(String str) {
		}
	}

}
