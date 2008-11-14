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

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/**
 * Removes non-semantic whitespaces in XML elements. 
 * 
 * Possible use cases:
 * <p/>
 * <ul>
 *   <li><b>UC1</b>: <code>&lt;tag1&gt;(sp)(sp)one(sp)(sp)two(sp)(sp)&lt;/tag1&gt;</code> becomes <code>&lt;tag1&gt;one(sp)two&lt;/tag1&gt;</code></li>
 *   <li><b>UC2</b>: <code>&lt;tag1&gt;(sp)(sp)one(sp)(sp)two(sp)(sp)&lt;tag2&gt;three&lt;/tag2&gt;&lt;/tag1&gt;</code> becomes <code>&lt;tag1&gt;one(sp)two(sp)&lt;tag2&gt;three&lt;/tag2&gt;&lt;/tag1&gt;</code></li>
 *   <li><b>UC3</b>: <code>&lt;tag1&gt;(\n\r\t)one(\n\r\t)two(\n\r\t)&lt;/tag1&gt;</code> becomes <code>&lt;tag1&gt;one(sp)two&lt;/tag1&gt;</code></li>
 *   <li><b>UC4</b>: <code>&lt;/tag1&gt;(sp)(sp)(\n\r\t)&lt;tag2&gt;</code> (where tag1 and tag2 are not both block elements) becomes <code>&lt;/tag1&gt; &lt;tag2&gt;</code>. If tag1 and tag2 are block elements all spaces are removed</li>
 *   <li><b>UC5</b>: <code>&lt;![CDATA[(\n\r\t)(sp)(sp)one(sp)(sp)(\n\r\t)]]&gt;</code> is left untouched</code></li>
 *   <li><b>UC6</b>: <code>&lt;pre&gt;(\n\r\t)(sp)(sp)one(sp)(sp)(\n\r\t)&lt;/pre&gt;</code> is left untouched</code></li>
 *   <li><b>UC7</b>: <code>&lt;tag1&gt;(sp)(sp)one(sp)(sp)&lt;!--comment--&gt;(sp)(sp)two(sp)(sp)&lt;/tag1&gt;</code> becomes <code>&lt;tag1&gt;one&lt;!--comment--&gt;two&lt;/tag1&gt;</code>
 *   <li><b>UC8</b>: <code>&lt;/tag1&gt;(sp)(sp)one(sp)(sp)&lt;/tag2&gt;</code> becomes <code>&lt;/tag1&gt;(sp)one&lt;/tag2&gt;</code></li>
 *   <li><b>UC9</b>: Comments which have a meaning for the XHTML parser do not have spaces removed in the content preceding them
 * </ul>  
 * 
 * @author vmassol
 */
public class XHTMLWhitespaceXMLFilter extends DefaultXMLFilter
{
    private static final Pattern HTML_WHITESPACE_DUPLICATES_PATTERN = 
        Pattern.compile("\\s{2,}|[\\t\\n\\x0B\\f\\r]+");
    
    private static final Pattern HTML_WHITESPACE_HEAD_PATTERN = Pattern.compile("^\\s+");
    
    private static final Pattern HTML_WHITESPACE_TAIL_PATTERN = Pattern.compile("\\s+$");

    private static final List<String> BLOCK_ELEMENTS = Arrays.asList(
        "p", "table", "ul", "ol", "li", "hr", "td", "tr", "th", "div", "tbody", "thead", 
        "pre", "h1", "h2", "h3", "h4", "h5", "h6", "dl", "dt", "dd", "blockquote");
    
    private boolean fRemoveWhitespaces = true;
    
    private String fLastClosedElement;
    
    private String fStartedElement;
    
    /**
     * Content to clean.
     */
    private StringBuffer fContent = new StringBuffer();

    private StringBuffer fContentBeforeComment;

    public XHTMLWhitespaceXMLFilter()
    {
        super();
    }
    
    public XHTMLWhitespaceXMLFilter(XMLReader reader)
    {
        super(reader);
    }
    
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        getContent().append(ch, start, length);
    }
    
    @Override
    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException
    {
        cleanWhiteSpacesBeforeElement();
        cleanExtraWhiteSpaces();
        
        // UC4: Remove all spaces between elements only if they are block elements
        if (getContent().length() == 1 && getContent().charAt(0) == ' ' && fLastClosedElement != null
            && BLOCK_ELEMENTS.contains(fLastClosedElement) && BLOCK_ELEMENTS.contains(localName)) {
            getContent().setLength(0);
        } else {
            sendCharacters();
        }
        
        fLastClosedElement = null;
        fContentBeforeComment = null;
        fStartedElement = localName;
        super.startElement(uri, localName, qName, atts);
        
        // UC6: Do not clean white spaces when in PRE element
        if ("pre".equalsIgnoreCase(localName)) {
            fRemoveWhitespaces = false;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {

        cleanWhiteSpacesAfterElement(localName);
        cleanExtraWhiteSpaces();        
        sendCharacters();
        fLastClosedElement = localName;
        fStartedElement = null;
        fRemoveWhitespaces = true;
        fContentBeforeComment = null;
        super.endElement(uri, localName, qName);
    }
    
    @Override
    public void startCDATA() throws SAXException
    {
        cleanWhiteSpacesBeforeElement();
        cleanExtraWhiteSpaces();        
        sendCharacters();

        // UC5: Do not clean white spaces when in CDATA section
        fRemoveWhitespaces = false;
        
        fContentBeforeComment = null;
        super.startCDATA();
    }

    @Override
    public void endCDATA() throws SAXException
    {
        if (getContent().length() > 0) {
            sendCharacters();
        }
        super.endCDATA();
        fRemoveWhitespaces = true;
    }

    @Override
    public void comment(char[] ch, int start, int length) throws SAXException
    {
        // UC7: Clean white spaces when there's a non semantic comment
        // UC9: Don't clean white spaces for semantic comments
        if (!isSemanticComment(new String(ch, start, length))) {
            cleanWhiteSpacesAfterElement(fStartedElement);
            cleanExtraWhiteSpaces();
            fContentBeforeComment = new StringBuffer(fContent);
        } else {
            fStartedElement = "semanticcomment";
            cleanExtraWhiteSpaces();
        }
        sendCharacters();
        super.comment(ch, start, length);
    }

    protected boolean shouldRemoveWhiteSpaces()
    {
        return fRemoveWhitespaces;
    }
    
    protected void sendCharacters() throws SAXException
    {
        if (getContent().length() > 0) {
            super.characters(getContent().toString().toCharArray(), 0, getContent().length());
            getContent().setLength(0);
        }
    }

    private void cleanWhiteSpacesBeforeElement() throws SAXException
    {
        if (getContent().length() > 0) {

            // UC2: A new element is started when the previous one isn't closed yet.
            // UC7: Remove lead spaces if we're after a non semantic comment and there was no content before the comment
            if (fStartedElement != null) {
                // UC7 & UC9: Don't remove lead whitespaces if we're on a semantic comment or if we're on a non semantic 
                // comment but with an empty text before the comment.
                if (!fStartedElement.equalsIgnoreCase("semanticcomment") && (fContentBeforeComment == null || fContentBeforeComment.length() == 0)) {  
                    trimLeadingWhiteSpaces();
                }
            } 
        }
    }
    
    private void cleanWhiteSpacesAfterElement(String tagName) throws SAXException
    {
        if (getContent().length() > 0) {

            if (fStartedElement == null) {
                // UC8: Previous tag was closed and we're closing another tag.
                trimTrailingWhiteSpaces();
            } else if (fStartedElement.equalsIgnoreCase(tagName)) {
                // UC1: started element is the same as ending element
                // UC7
                if (fContentBeforeComment == null || fContentBeforeComment.length() == 0) {
                    trimLeadingWhiteSpaces();
                }
                trimTrailingWhiteSpaces();
            } else if (fStartedElement.equalsIgnoreCase("semanticcomment")) {
                // UC9
                trimTrailingWhiteSpaces();
            }
        }
    }
    
    protected void cleanExtraWhiteSpaces()
    {
        if (getContent().length() > 0) {
            // UC3: Remove non whitespace chars (/n, /r, /t, etc)
            if (shouldRemoveWhiteSpaces()) {
                Matcher matcher = HTML_WHITESPACE_DUPLICATES_PATTERN.matcher(getContent());
                String result = matcher.replaceAll(" ");
                getContent().setLength(0);
                getContent().append(result);
            }
        }
    }    
    
    // Trim white spaces and new lines since they are ignored in XHTML (except when
    // in CDATA or PRE elements).
    protected void trimLeadingWhiteSpaces()
    {
        if (shouldRemoveWhiteSpaces()) {
            Matcher matcher = HTML_WHITESPACE_HEAD_PATTERN.matcher(getContent());
            String result = matcher.replaceAll("");
            getContent().setLength(0);
            getContent().append(result);
        }
    }
    
    protected void trimTrailingWhiteSpaces()
    {
        if (shouldRemoveWhiteSpaces()) {
            Matcher matcher = HTML_WHITESPACE_TAIL_PATTERN.matcher(getContent());
            String result = matcher.replaceAll("");
            getContent().setLength(0);
            getContent().append(result);
        }
    }
    
    protected StringBuffer getContent()
    {
        return fContent;
    }
    
    /**
     * We remove spaces around non semantic comments.
     * 
     * @param comment the comment to evaluate
     * @return true if the comment is a semantic one
     */
    protected boolean isSemanticComment(String comment)
    {
        return comment.startsWith("startmacro:") || comment.startsWith("stopmacro"); 
    }
}
