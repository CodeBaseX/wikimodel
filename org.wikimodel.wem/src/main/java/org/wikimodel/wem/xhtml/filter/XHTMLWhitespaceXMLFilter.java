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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.AttributesImpl;

/**
 * Removes non-semantic whitespaces in XML elements. See
 * http://www.w3.org/TR/html4/struct/text.html#h-9.1 for more details. Possible
 * use cases:
 * <p/>
 * <ul>
 * <li><b>UC1</b>: Any white spaces group is removed if it's before a non inline
 * (see INLINE_ELEMENTS) element or at the begining of the document.</li>
 * <li><b>UC2</b>: Any white spaces group is removed if it's after a non inline
 * (see INLINE_ELEMENTS) element or at the end of the document.</li>
 * <li><b>UC3</b>: Inside inline content any white spaces group become a single
 * space.</li>
 * <li><b>UC5</b>: Non visible element (comments, CDATA and NONVISIBLE_ELEMENTS)
 * are invisibles and does not cut a white space group.
 * <code>text(sp)<!--comment-->(sp)text</code> becomes
 * <code>text(sp)<!--comment-->text</code></li>
 * <li><b>UC5</b>: Visible empty element like img count as text when grouping
 * white spaces</li>
 * <li><b>UC6</b>: Semantic comment count as text when grouping white spaces</li>
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

    private static final Set<String> NONINLINE_ELEMENTS = new HashSet<String>(
        Arrays.asList("address", "blockquote", "div", "dl", "dt", "dd",
            "fieldset", "form", "h1", "h2", "h3", "h4", "h5", "h6", "hr",
            "noscript", "ol", "p", "pre", "script", "table", "ul", "html",
            "body", "td", "tr", "th", "tbody", "head", "li", "thead", "tfoot",
            "caption", "col", "colgroup", "legend", "base", "link", "meta",
            "style", "title"));

    /**
     * Non visible elements behave like CDATA and comments: it's part of the
     * white space group.
     */
    private static final Set<String> NONVISIBLE_ELEMENTS = new HashSet<String>(
        Arrays.asList("script"));

    /**
     * Visible elements like images count in the inline text to clean white
     * spaces.
     */
    private static final Set<String> EMPTYVISIBLE_ELEMENTS = new HashSet<String>(
        Arrays.asList("img"));

    /**
     * State indicating if the white spaces has to be cleaned. It's an int to
     * support &lt;pre&gt;pre&lt;/pre&gt; inside &lt;tt
     * class=&quot;wikimodel-verbatim&quot;&gt;pre&lt;/tt&gt;.
     */
    private int fNoCleanUpLevel = 0;

    /**
     * Content to clean.
     */
    private StringBuffer fContent = new StringBuffer();

    /**
     * Bufferized current inline text. It contains only text (and no inline
     * start/end element, comment or CDATA) to be able know if a leading space
     * has to be remove because the previous text ends with it or if there is no
     * previous text.
     */
    private StringBuffer fPreviousInlineText = new StringBuffer();

    /**
     * The previous content to send. Buffurized waiting to know if its trailing
     * space has to be removed when it's the last text of inline content.
     */
    private String fPreviousContent = null;

    /**
     * Previous inline elements. These are the elements before the previous
     * content. It's buffurized to support space group cleaning betwen different
     * inline elements.
     */
    private List<Event> fPreviousElements = new ArrayList<Event>();

    private Stack<Attributes> fAttributes = new Stack<Attributes>();

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
    public void startElement(String uri, String localName, String qName,
            Attributes atts) throws SAXException {
        Attributes clonedAtts = fAttributes.push(new AttributesImpl(atts));

        if (NONVISIBLE_ELEMENTS.contains(localName)) {
            startNonVisibleElement();

            // send start element event
            super.startElement(uri, localName, qName, atts);
        } else {
            if (NONINLINE_ELEMENTS.contains(localName)) {
                // Flush previous content and print current one
                flushContent();

                // white spaces inside pre element are not cleaned
                if ("pre".equalsIgnoreCase(localName)) {
                    ++fNoCleanUpLevel;
                }

                // send start element event
                super.startElement(uri, localName, qName, atts);
            } else if (EMPTYVISIBLE_ELEMENTS.contains(localName)) {
                startEmptyVisibleElement();

                super.startElement(uri, localName, qName, atts);
            } else if (preservedInlineContent(localName, atts)) {
                // Flush previous content and print current one
                flushContent();

                ++fNoCleanUpLevel;

                // send start element event
                super.startElement(uri, localName, qName, atts);
            } else {
                appendInlineEvent(new Event(uri, localName, qName, clonedAtts));
            }
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        if (NONVISIBLE_ELEMENTS.contains(localName)) {
            endNonVisibleElement();

            super.endElement(uri, localName, qName);

            --fNoCleanUpLevel;
        } else {
            if (NONINLINE_ELEMENTS.contains(localName)) {
                // Flush previous content and print current one
                flushContent();

                // white spaces inside pre element are not cleaned
                if ("pre".equalsIgnoreCase(localName)) {
                    --fNoCleanUpLevel;
                }

                super.endElement(uri, localName, qName);
            } else if (EMPTYVISIBLE_ELEMENTS.contains(localName)) {
                endEmptyVisibleElement();

                super.endElement(uri, localName, qName);
            } else if (preservedInlineContent(localName, fAttributes.peek())) {
                // Flush previous content and print current one
                flushContent();

                --fNoCleanUpLevel;

                super.endElement(uri, localName, qName);
            } else {
                appendInlineEvent(new Event(uri, localName, qName));
            }
        }

        fAttributes.pop();
    }

    private boolean preservedInlineContent(String localName, Attributes atts) {
        boolean preserved = false;

        if ("tt".equalsIgnoreCase(localName)) {
            String value = atts.getValue("class");

            if (value != null) {
                preserved = Arrays.asList(value.split(" ")).contains(
                    "wikimodel-verbatim");
            }
        }

        return preserved;
    }

    @Override
    public void startCDATA() throws SAXException {
        startNonVisibleElement();

        super.startCDATA();
    }

    @Override
    public void endCDATA() throws SAXException {
        endNonVisibleElement();

        super.endCDATA();

        --fNoCleanUpLevel;
    }

    @Override
    public void comment(char[] ch, int start, int length) throws SAXException {
        if (shouldRemoveWhiteSpaces()) {
            String comment = new String(ch, start, length);

            if (isSemanticComment(comment)) {
                // UC6: Semantic comment count as text when grouping white
                // spaces
                startEmptyVisibleElement();

                super.comment(ch, start, length);
            } else {
                appendInlineEvent(new Event(comment));
            }
        } else {
            super.comment(ch, start, length);
        }
    }

    @Override
    public void endDocument() throws SAXException {
        // Flush previous content and print current one
        flushContent();

        super.endDocument();
    }

    protected boolean shouldRemoveWhiteSpaces() {
        return fNoCleanUpLevel == 0;
    }

    protected void sendPreviousContent(boolean trimTrailing)
            throws SAXException {
        if (fPreviousContent != null && fPreviousContent.length() > 0) {
            if (trimTrailing) {
                fPreviousContent = trimTrailingWhiteSpaces(fPreviousContent);
            }

            sendCharacters(fPreviousContent.toCharArray());
            fPreviousContent = null;
        }

        for (Event event : fPreviousElements) {
            sendInlineEvent(event);
        }
        fPreviousElements.clear();
    }

    protected void sendInlineEvent(Event event) throws SAXException {
        if (event.type == Event.Type.BEGIN_ELEMENT) {
            super.startElement(event.uri, event.localName, event.qName,
                event.atts);
        } else if (event.type == Event.Type.END_ELEMENT) {
            super.endElement(event.uri, event.localName, event.qName);
        } else if (event.type == Event.Type.COMMENT) {
            super.comment(event.content.toCharArray(), 0, event.content
                    .length());
        }
    }

    /**
     * Flush previous content and print current one.
     */
    protected void flushContent() throws SAXException {
        cleanContentLeadingSpaces();
        cleanContentExtraWhiteSpaces();

        // UC2: Any white spaces group is removed if it's after a non inline
        // (see INLINE_ELEMENTS) element.
        trimTrailingWhiteSpaces();

        // Send previous content
        sendPreviousContent(getContent().length() == 0);

        // Send current content
        if (getContent().length() > 0) {
            sendCharacters(getContent().toString().toCharArray());
            getContent().setLength(0);
        }

        // Reinit inline text buffer
        fPreviousInlineText.setLength(0);
    }

    /**
     * Append an inline element. Inline elements ending with a space are stacked
     * waiting for a non space character or the end of the inline content.
     */
    protected void appendInlineEvent(Event event) throws SAXException {
        cleanContentLeadingSpaces();
        cleanContentExtraWhiteSpaces();

        if (getContent().length() > 0) {
            sendPreviousContent(false);

            fPreviousInlineText.append(getContent());

            if (getContent().charAt(getContent().length() - 1) == ' ') {
                fPreviousContent = getContent().toString();
                fPreviousElements.add(event);
            } else {
                sendCharacters(getContent().toString().toCharArray());
                sendInlineEvent(event);
            }

            getContent().setLength(0);
        } else {
            if (fPreviousInlineText.length() == 0) {
                // There is no inline text before this inline element
                sendInlineEvent(event);
            } else {
                // The last inline text ends with a space
                fPreviousElements.add(event);
            }
        }
    }

    protected void startEmptyVisibleElement() throws SAXException {
        cleanContentLeadingSpaces();
        cleanContentExtraWhiteSpaces();

        // Send previous content
        sendPreviousContent(false);

        // Send content
        sendCharacters(getContent().toString().toCharArray());
        fPreviousInlineText.append(getContent());

        // Add visible element as part of the inline text
        fPreviousInlineText.append("EmptyVisibleElement");

        getContent().setLength(0);
    }

    protected void endEmptyVisibleElement() throws SAXException {
        // Send current content
        if (getContent().length() > 0) {
            sendCharacters(getContent().toString().toCharArray());
            getContent().setLength(0);
        }
    }

    /**
     * Append an non visible element.
     */
    protected void startNonVisibleElement() throws SAXException {
        if (shouldRemoveWhiteSpaces()) {
            cleanContentLeadingSpaces();
            cleanContentExtraWhiteSpaces();

            if (getContent().length() > 0) {
                sendPreviousContent(false);

                fPreviousInlineText.append(getContent());

                if (getContent().charAt(getContent().length() - 1) == ' ') {
                    fPreviousContent = getContent().toString();
                } else {
                    sendCharacters(getContent().toString().toCharArray());
                }
            }

            // The is some text ending with a space before the non visible
            // element. The space will move after the element if it's needed (if
            // the element is followed by inline text);
            if (fPreviousContent != null) {
                sendCharacters(fPreviousContent.toCharArray(), 0,
                    fPreviousContent.length() - 1);
                fPreviousContent = " ";
            }
        } else {
            // Send current content
            sendCharacters(getContent().toString().toCharArray());
        }

        getContent().setLength(0);

        // Do not clean white spaces when in non visible element
        ++fNoCleanUpLevel;
    }

    /**
     * Flush previous content and print current one.
     */
    protected void endNonVisibleElement() throws SAXException {
        // Send current content
        if (getContent().length() > 0) {
            sendCharacters(getContent().toString().toCharArray());
            getContent().setLength(0);
        }
    }

    protected void sendCharacters(char ch[]) throws SAXException {
        sendCharacters(ch, 0, ch.length);
    }

    protected void sendCharacters(char ch[], int start, int length)
            throws SAXException {
        if (length > 0) {
            super.characters(ch, start, length);
        }
    }

    /**
     * UC1: Any white spaces group is removed if it's before a non inline
     * element or at the begining of the document.
     * <p>
     * UC3: Remove leading spaces of content if previous inline text already
     * ends with a space.
     */
    private void cleanContentLeadingSpaces() {
        if (getContent().length() > 0) {
            if (fPreviousInlineText.length() == 0
                    || fPreviousInlineText
                            .charAt(fPreviousInlineText.length() - 1) == ' ') {
                trimLeadingWhiteSpaces();
            }
        }
    }

    /**
     * UC3: Replace group of white spaces by a single space.
     */
    protected void cleanContentExtraWhiteSpaces() {
        if (getContent().length() > 0) {
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
    // when in CDATA or PRE elements).
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

        if (shouldRemoveWhiteSpaces() && content.length() > 0) {
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

    private static class Event {
        public enum Type {
            BEGIN_ELEMENT, END_ELEMENT, COMMENT
        }

        public Type type;

        public String uri;

        public String localName;

        public String qName;

        public Attributes atts;

        String content;

        public Event(String uri, String localName, String qName, Attributes atts) {
            this.type = Type.BEGIN_ELEMENT;
            this.uri = uri;
            this.localName = localName;
            this.qName = qName;
            this.atts = atts;
        }

        public Event(String uri, String localName, String qName) {
            this.type = Type.END_ELEMENT;
            this.uri = uri;
            this.localName = localName;
            this.qName = qName;
        }

        public Event(String content) {
            this.type = Type.COMMENT;
            this.content = content;
        }
    }
}
