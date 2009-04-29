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
package org.wikimodel.wem.xhtml.filter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/**
 * Removes non-semantic whitespaces in XML elements. See
 * http://www.w3.org/TR/html4/struct/text.html#h-9.1 for more details.
 * 
 * Possible use cases:
 * <p/>
 * <ul>
 * <li><b>UC1</b>:
 * <code>&lt;tag1&gt;(sp)(sp)one(sp)(sp)two(sp)(sp)&lt;/tag1&gt;</code> becomes
 * <code>&lt;tag1&gt;one(sp)two&lt;/tag1&gt;</code></li>
 * <li><b>UC2</b>:
 * <code>&lt;tag1&gt;(sp)(sp)one(sp)(sp)two(sp)(sp)&lt;tag2&gt;three&lt;/tag2&gt;&lt;/tag1&gt;</code>
 * becomes
 * <code>&lt;tag1&gt;one(sp)two(sp)&lt;tag2&gt;three&lt;/tag2&gt;&lt;/tag1&gt;</code>
 * </li>
 * <li><b>UC3</b>:
 * <code>&lt;tag1&gt;(\n\r\t)one(\n\r\t)two(\n\r\t)&lt;/tag1&gt;</code> becomes
 * <code>&lt;tag1&gt;one(sp)two&lt;/tag1&gt;</code></li>
 * <li><b>UC4</b>: <code>&lt;/tag1&gt;(sp)(sp)(\n\r\t)&lt;tag2&gt;</code> (where
 * tag1 and tag2 are not both block elements) becomes
 * <code>&lt;/tag1&gt; &lt;tag2&gt;</code>. If tag1 and tag2 are block elements
 * all spaces are removed</li>
 * <li><b>UC5</b>:
 * <code>&lt;![CDATA[(\n\r\t)(sp)(sp)one(sp)(sp)(\n\r\t)]]&gt;</code> is left
 * untouched</code></li>
 * <li><b>UC6</b>:
 * <code>&lt;pre&gt;(\n\r\t)(sp)(sp)one(sp)(sp)(\n\r\t)&lt;/pre&gt;</code> is
 * left untouched</code></li>
 * <li><b>UC7</b>:
 * <code>&lt;tag1&gt;(sp)(sp)one(sp)(sp)&lt;!--comment--&gt;(sp)(sp)two(sp)(sp)&lt;/tag1&gt;</code>
 * becomes <code>&lt;tag1&gt;one&lt;!--comment--&gt;two&lt;/tag1&gt;</code>
 * <li><b>UC8</b>: <code>&lt;/tag1&gt;(sp)(sp)one(sp)(sp)&lt;/tag2&gt;</code>
 * becomes <code>&lt;/tag1&gt;(sp)one&lt;/tag2&gt;</code></li>
 * <li><b>UC9</b>: Comments which have a meaning for the XHTML parser do not
 * have spaces removed in the content preceding them
 * </ul>
 * 
 * @author vmassol
 */
public class XHTMLWhitespaceXMLFilter extends DefaultXMLFilter {
    private static final Pattern HTML_WHITESPACE_DUPLICATES_PATTERN = Pattern
            .compile("\\s{2,}|[\\t\\n\\x0B\\f\\r]+");

    private static final Pattern HTML_WHITESPACE_HEAD_PATTERN = Pattern
            .compile("^\\s+");

    private static final Pattern HTML_WHITESPACE_TAIL_PATTERN = Pattern
            .compile("\\s+$");

    /*
     * private static final List<String> NONINLINE_ELEMENTS = Arrays.asList(
     * "html", "head", "body", "p", "table", "ul", "ol", "li", "hr", "td", "tr",
     * "th", "div", "tbody", "thead", "pre", "h1", "h2", "h3", "h4", "h5", "h6",
     * "dl", "dt", "dd", "blockquote");
     */

    private static final List<String> INLINECONTAINER_ELEMENTS = Arrays.asList(
            "p", "li", "hr", "td", "th", "div", "thead", "pre", "h1", "h2",
            "h3", "h4", "h5", "h6", "dl", "dt", "dd", "blockquote");

    private boolean fRemoveWhitespaces = true;

    private int fInlineDepth = 0;

    /**
     * Content to clean.
     */
    private StringBuffer fContent = new StringBuffer();

    private StringBuffer fPreviousInlineContent = new StringBuffer();
    private String fPreviousContent = null;

    private List<String[]> fEndingInlineElements = new ArrayList<String[]>();

    public XHTMLWhitespaceXMLFilter() {

    }

    public XHTMLWhitespaceXMLFilter(XMLReader reader) {
        super(reader);
    }

    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException {
        getContent().append(ch, start, length);
    }

    @Override
    public void startElement(
        String uri,
        String localName,
        String qName,
        Attributes atts) throws SAXException {
        cleanBeforeElement();
        cleanExtraWhiteSpaces();

        sendContent();

        if (INLINECONTAINER_ELEMENTS.contains(localName)) {
            ++fInlineDepth;
            fPreviousInlineContent.setLength(0);
        }
        super.startElement(uri, localName, qName, atts);

        // UC6: Do not clean white spaces when in PRE element
        if ("pre".equalsIgnoreCase(localName)) {
            fRemoveWhitespaces = false;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        cleanInlineContentFirstSpaces();
        cleanExtraWhiteSpaces();

        if (INLINECONTAINER_ELEMENTS.contains(localName)) {
            --fInlineDepth;
        }

        if (fInlineDepth == 0) {
            trimTrailingWhiteSpaces();
            sendContent();
            fPreviousInlineContent.setLength(0);

            fRemoveWhitespaces = true;

            super.endElement(uri, localName, qName);
        } else {
            if (getContent().length() > 0) {
                sendPreviousContent(false);
                fPreviousInlineContent.append(getContent());
                fPreviousContent = getContent().toString();
            }

            getContent().setLength(0);
            fEndingInlineElements.add(new String[] { uri, localName, qName });
        }
    }

    @Override
    public void startCDATA() throws SAXException {
        cleanInlineContentFirstSpaces();
        cleanExtraWhiteSpaces();
        sendContent();

        // UC5: Do not clean white spaces when in CDATA section
        fRemoveWhitespaces = false;

        super.startCDATA();
    }

    @Override
    public void endCDATA() throws SAXException {
        if (getContent().length() > 0) {
            sendContent();
        }
        super.endCDATA();
        fRemoveWhitespaces = true;
    }

    @Override
    public void comment(char[] ch, int start, int length) throws SAXException {
        cleanBeforeElement();
        cleanExtraWhiteSpaces();

        sendContent();

        if (isSemanticComment(new String(ch, start, length))) {
            fPreviousInlineContent.append("semanticcomment");
        }

        super.comment(ch, start, length);
    }

    protected boolean shouldRemoveWhiteSpaces() {
        return fRemoveWhitespaces;
    }

    protected void sendPreviousContent(boolean trimTrailing)
            throws SAXException {
        if (fEndingInlineElements.size() > 0) {
            if (fPreviousContent != null && fPreviousContent.length() > 0) {
                if (trimTrailing) {
                    fPreviousContent = trimTrailingWhiteSpaces(fPreviousContent);
                }

                sendCharacters(fPreviousContent);
            }
            for (String[] element : fEndingInlineElements) {
                super.endElement(element[0], element[1], element[2]);
            }
            fEndingInlineElements.clear();
        }

        fPreviousContent = null;
    }

    protected void sendContent() throws SAXException {
        sendPreviousContent(getContent().length() == 0);

        if (getContent().length() > 0) {
            fPreviousInlineContent.append(getContent());
            sendCharacters(getContent().toString());
            getContent().setLength(0);
        }
    }

    protected void sendCharacters(String content) throws SAXException {
        if (content.length() > 0) {
            super.characters(content.toCharArray(), 0, content.length());
        }
    }

    private void cleanBeforeElement() {
        if (fInlineDepth == 0) {
            trimLeadingWhiteSpaces();
            trimTrailingWhiteSpaces();
        } else {
            cleanInlineContentFirstSpaces();
        }
    }

    private void cleanInlineContentFirstSpaces() {
        if (getContent().length() > 0) {
            if (fPreviousInlineContent.length() == 0
                    || fPreviousInlineContent.charAt(fPreviousInlineContent
                            .length() - 1) == ' ') {
                trimLeadingWhiteSpaces();
            }
        }
    }

    protected void cleanExtraWhiteSpaces() {
        if (getContent().length() > 0) {
            // UC3: Remove non whitespace chars (/n, /r, /t, etc)
            if (shouldRemoveWhiteSpaces()) {
                Matcher matcher = HTML_WHITESPACE_DUPLICATES_PATTERN
                        .matcher(getContent());
                String result = matcher.replaceAll(" ");
                getContent().setLength(0);
                getContent().append(result);
            }
        }
    }

    // Trim white spaces and new lines since they are ignored in XHTML (except
    // when
    // in CDATA or PRE elements).
    protected void trimLeadingWhiteSpaces() {
        if (shouldRemoveWhiteSpaces() && getContent().length() > 0) {
            String result = trimLeadingWhiteSpaces(getContent());
            getContent().setLength(0);
            getContent().append(result);
        }
    }

    protected String trimLeadingWhiteSpaces(CharSequence content) {
        String trimedContent;

        if (shouldRemoveWhiteSpaces() && content.length() > 0) {
            Matcher matcher = HTML_WHITESPACE_HEAD_PATTERN.matcher(content);
            trimedContent = matcher.replaceAll("");
        } else {
            trimedContent = content.toString();
        }

        return trimedContent;
    }

    protected void trimTrailingWhiteSpaces() {
        if (shouldRemoveWhiteSpaces() && getContent().length() > 0) {
            String result = trimTrailingWhiteSpaces(getContent());
            getContent().setLength(0);
            getContent().append(result);
        }
    }

    protected String trimTrailingWhiteSpaces(CharSequence content) {
        String trimedContent;

        if (shouldRemoveWhiteSpaces() && getContent().length() > 0) {
            Matcher matcher = HTML_WHITESPACE_TAIL_PATTERN.matcher(content);
            trimedContent = matcher.replaceAll("");
        } else {
            trimedContent = content.toString();
        }

        return trimedContent;
    }

    protected StringBuffer getContent() {
        return fContent;
    }

    /**
     * We remove spaces around non semantic comments.
     * 
     * @param comment the comment to evaluate
     * @return true if the comment is a semantic one
     */
    protected boolean isSemanticComment(String comment) {
        return comment.startsWith("startmacro:")
                || comment.startsWith("stopmacro");
    }
}
