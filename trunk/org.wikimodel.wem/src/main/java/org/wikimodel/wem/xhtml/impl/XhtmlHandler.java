/*******************************************************************************
 * Copyright (c) 2005,2006 Cognium Systems SA and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Cognium Systems SA - initial API and implementation
 *******************************************************************************/
package org.wikimodel.wem.xhtml.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.wikimodel.wem.WikiPageUtil;
import org.wikimodel.wem.WikiParameter;
import org.wikimodel.wem.WikiParameters;
import org.wikimodel.wem.impl.WikiScannerContext;
import org.wikimodel.wem.xhtml.XhtmlCharacter;
import org.wikimodel.wem.xhtml.XhtmlCharacterType;
import org.wikimodel.wem.xhtml.XhtmlEscapeHandler;
import org.wikimodel.wem.xhtml.handler.BoldTagHandler;
import org.wikimodel.wem.xhtml.handler.CommentHandler;
import org.wikimodel.wem.xhtml.handler.DefinitionDescriptionTagHandler;
import org.wikimodel.wem.xhtml.handler.DefinitionListTagHandler;
import org.wikimodel.wem.xhtml.handler.DefinitionTermTagHandler;
import org.wikimodel.wem.xhtml.handler.DivisionTagHandler;
import org.wikimodel.wem.xhtml.handler.HeaderTagHandler;
import org.wikimodel.wem.xhtml.handler.HorizontalLineTagHandler;
import org.wikimodel.wem.xhtml.handler.ItalicTagHandler;
import org.wikimodel.wem.xhtml.handler.LineBreakTagHandler;
import org.wikimodel.wem.xhtml.handler.ListItemTagHandler;
import org.wikimodel.wem.xhtml.handler.ListTagHandler;
import org.wikimodel.wem.xhtml.handler.ParagraphTagHandler;
import org.wikimodel.wem.xhtml.handler.PreserveTagHandler;
import org.wikimodel.wem.xhtml.handler.QuoteTagHandler;
import org.wikimodel.wem.xhtml.handler.ReferenceTagHandler;
import org.wikimodel.wem.xhtml.handler.SpanTagHandler;
import org.wikimodel.wem.xhtml.handler.StrikedOutTagHandler;
import org.wikimodel.wem.xhtml.handler.SubScriptTagHandler;
import org.wikimodel.wem.xhtml.handler.SuperScriptTagHandler;
import org.wikimodel.wem.xhtml.handler.TableDataTagHandler;
import org.wikimodel.wem.xhtml.handler.TableRowTagHandler;
import org.wikimodel.wem.xhtml.handler.TableTagHandler;
import org.wikimodel.wem.xhtml.handler.TagHandler;
import org.wikimodel.wem.xhtml.handler.TeletypeTagHandler;
import org.wikimodel.wem.xhtml.handler.UnderlineTagHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.ext.Attributes2;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author kotelnikov
 * @author vmassol
 */
public class XhtmlHandler extends DefaultHandler implements LexicalHandler {

    public static class TagStack {

        public class TagContext {

            private final WikiParameters fParameters;

            private String fName;
            
            private StringBuffer fContent;

            public TagHandler fHandler;

            private final TagContext fParent;

            TagStack fTagStack;
            
            public TagContext(
                TagContext parent,
                String name,
                WikiParameters params,
                TagStack tagStack) {
                fName = name;
                fParent = parent;
                fParameters = params;
                fTagStack = tagStack; 
            }

            public boolean appendContent(char[] array, int start, int length) {
                if (fHandler == null || !fHandler.isAccumulateContent())
                    return false;
                if (fContent == null) {
                    fContent = new StringBuffer();
                }
                fContent.append(array, start, length);
                return true;
            }

            public void beginElement(TagHandler handler) {
                if (fParent == null) {
                    fScannerContext.beginDocument();
                }
                fHandler = handler;
                if (fHandler != null) {
                    fHandler.beginElement(this);
                }
            }

            public void endElement() {
                if (fHandler != null) {
                    fHandler.endElement(this);
                }
                if (fParent == null) {
                    fScannerContext.endDocument();
                }
            }

            public String getContent() {
                return fContent != null ? WikiPageUtil.escapeXmlString(fContent
                    .toString()) : "";
            }

            public String getName() {
                return fName;
            }
            
            public WikiParameters getParams() {
                return fParameters;
            }

            public TagContext getParent() {
                return fParent;
            }

            public WikiScannerContext getScannerContext() {
                return fScannerContext;
            }

            public TagStack getTagStack() {
                return fTagStack;
            }
            
            public boolean isContentContainer() {
                return fHandler == null || fHandler.isContentContainer();
            }

            public boolean isTag(String string) {
                return string.equals(fName.toLowerCase());
            }
        }

        private static final int CHARACTER = 0;

        private Map<String, TagHandler> fMap = new HashMap<String, TagHandler>();

        private CommentHandler fCommentHandler;
        
        private static final int NEW_LINE = 3;

        private static final char SPACE = 1;

        private static final int SPECIAL_SYMBOL = 2;

        /**
         * Allow saving parameters. 
         * For example we save the number of br elements if we're outside 
         * of a block element so that we can emit an onEmptyLines event.
         */
        private Map<String, Object> fStackParameters = new HashMap<String, Object>(); 
        
        public void add(String tag, TagHandler handler) {
            fMap.put(tag, handler);
        }
        
        public void addAll(Map<String, TagHandler> handlers) {
        	fMap.putAll(handlers);
        }

        public void setCommentHandler(CommentHandler handler) {
            fCommentHandler = handler;
        }
        
        private TagContext fPeek;

        WikiScannerContext fScannerContext;

        XhtmlEscapeHandler fEscapeHandler;

        public TagStack(WikiScannerContext context, XhtmlEscapeHandler escapeHandler) {
            fScannerContext = context;
            fEscapeHandler = escapeHandler;
            fCommentHandler = new CommentHandler();
            
            // Pre-initialize stack parameters for performance reason 
            // (so that we don't have to check all the time if they're initialized or not)
            setStackParameter("ignoreElements", false);
            setStackParameter("emptyLinesCount", 0);
            setStackParameter("listStyles", new StringBuffer());
            setStackParameter("quoteDepth", new Integer(0));
            setStackParameter("insideBlockElement", false);
        }

        public void beginElement(
            String name,
            WikiParameters params) {
            fPeek = new TagContext(fPeek, name, params, this);
            name = fPeek.getName();
            TagHandler handler = fMap.get(name);
            boolean ignoreElements = (Boolean) getStackParameter("ignoreElements");
            if (!ignoreElements) {
                fPeek.beginElement(handler);
            }
        }

        public void endElement() {
            boolean ignoreElements = (Boolean) getStackParameter("ignoreElements");
            if (!ignoreElements) {
                fPeek.endElement();
            }
            fPeek = fPeek.fParent;
        }

        private XhtmlCharacterType getCharacterType(char ch) {
            XhtmlCharacterType type = XhtmlCharacterType.CHARACTER;
            switch (ch) {
                case '!':
                case '\'':
                case '#':
                case '$':
                case '%':
                case '&':
                case '(':
                case ')':
                case '*':
                case '+':
                case ',':
                case '-':
                case '.':
                case '/':
                case ':':
                case ';':
                case '<':
                case '=':
                case '>':
                case '?':
                case '@':
                case '[':
                case '\\':
                case ']':
                case '^':
                case '_':
                case '`':
                case '{':
                case '|':
                case '}':
                case '~':
                case '\"':
                    type = XhtmlCharacterType.SPECIAL_SYMBOL;
                    break;
                case ' ':
                case '\t':
                    type = XhtmlCharacterType.SPACE;
                    break;
                case '\n':
                case '\r':
                    type = XhtmlCharacterType.NEW_LINE;
                    break;
                default:
                    break;
            }
            return type;
        }

        public WikiScannerContext getScannerContext() {
            return fScannerContext;
        }
        
        public void setScannerContext(WikiScannerContext context) {
            fScannerContext = context;
        }

        private void flushStack(Stack<XhtmlCharacter> stack) {
            while (stack.size() > 0) {
                XhtmlCharacter character = stack.remove(0);
                switch (character.getType()) {
                    case ESCAPED:
                        fScannerContext.onEscape("" + character.getCharacter());
                        break;
                    case NEW_LINE:
                        fScannerContext.onNewLine();
                        break;
                    case SPECIAL_SYMBOL:
                        fScannerContext.onSpecialSymbol("" + character.getCharacter());
                        break;
                    case SPACE:
                        StringBuffer spaceBuffer = new StringBuffer(" ");
                        while ((stack.size() > 0) && (stack.firstElement().getType() == XhtmlCharacterType.SPACE)) {
                            stack.remove(0);
                            spaceBuffer.append(' ');
                        }
                        fScannerContext.onSpace(spaceBuffer.toString());
                        break;
                    default:
                        StringBuffer charBuffer = new StringBuffer();
                        charBuffer.append(character.getCharacter());
                        while ((stack.size() > 0) && (stack.firstElement().getType() == XhtmlCharacterType.CHARACTER)) {
                            charBuffer.append(stack.firstElement().getCharacter());
                            stack.remove(0);
                        }
                        fScannerContext.onWord(WikiPageUtil.escapeXmlString(charBuffer.toString()));
                }
            }            
        }
        
        public void onCharacters(char[] array, int start, int length) {
            
            if (!fPeek.isContentContainer())
                return;
            boolean ignoreElements = (Boolean) getStackParameter("ignoreElements");
            if (ignoreElements)
                return;

            if (!fPeek.appendContent(array, start, length)) {
                Stack<XhtmlCharacter> stack = new Stack<XhtmlCharacter>();
                Map<String, Object> characterContext = new HashMap<String, Object>();
                if (fEscapeHandler != null) {
                	fEscapeHandler.initialize(characterContext);
                }
                for (int i = 0; i < length; i++) {
                    XhtmlCharacter current = new XhtmlCharacter(array[start + i], getCharacterType(array[start + i]));
                    XhtmlCharacter result = current;
                    if (fEscapeHandler != null) {
                        result = fEscapeHandler.handleCharacter(current, stack, fPeek, characterContext);
                    }
                    stack.push(result);
                }
                
                // Now send the events.
                flushStack(stack);
            }
        }

        public void onComment(char[] array, int start, int length) {
            fCommentHandler.onComment(new String(array, start, length), this);
        }
        
        public void setStackParameter(String name, Object data) {
        	fStackParameters.put(name, data);
        }
        
        public Object getStackParameter(String name) {
        	return fStackParameters.get(name);
        }

    }

    protected String fDocumentSectionUri;

    protected String fDocumentUri;

    protected String fDocumentWikiProperties;

    TagStack fStack;

    /**
     * SAX parsers are allowed to call the characters() method several times in a row.
     * Some parsers have a buffer of 8K (Crimson), others of 16K (Xerces) and others can
     * even call onCharacters() for every single characters! Thus we need to accumulate
     * the characters in a buffer before we process them.
     */
    private StringBuffer fAccumulationBuffer;

    /**
     * If true then the parser is still processing the DTD and thus we should ignore all Comments/CDATA sections.
     */
    private boolean fIsInDTD;
    
    public XhtmlHandler(WikiScannerContext context, Map<String, TagHandler> extraHandlers, XhtmlEscapeHandler escapeHandler) {
        this(context, extraHandlers, escapeHandler, new CommentHandler());
    }

    /**
     * @param context
     */
    public XhtmlHandler(WikiScannerContext context, Map<String, TagHandler> extraHandlers, 
        XhtmlEscapeHandler escapeHandler, CommentHandler commentHandler) {
        fStack = new TagStack(context, escapeHandler);
        fStack.setCommentHandler(commentHandler);
        
        // Register default handlers
        fStack.add("p", new ParagraphTagHandler());
        fStack.add("table", new TableTagHandler());
        fStack.add("tr", new TableRowTagHandler());
        TagHandler handler = new TableDataTagHandler();
        fStack.add("td", handler);
        fStack.add("th", handler);
        handler = new ListTagHandler();
        fStack.add("ul", handler);
        fStack.add("ol", handler);
        fStack.add("li", new ListItemTagHandler());
        fStack.add("dl", new DefinitionListTagHandler());
        fStack.add("dt", new DefinitionTermTagHandler());
        fStack.add("dd", new DefinitionDescriptionTagHandler());
        handler = new HeaderTagHandler();
        fStack.add("h1", handler);
        fStack.add("h2", handler);
        fStack.add("h3", handler);
        fStack.add("h4", handler);
        fStack.add("h5", handler);
        fStack.add("h6", handler);
        fStack.add("hr", new HorizontalLineTagHandler());
        fStack.add("pre", new PreserveTagHandler());
        handler = new ReferenceTagHandler();
        fStack.add("a", handler);
        fStack.add("img", handler);
        handler = new BoldTagHandler();
        fStack.add("strong", handler);
        fStack.add("b", handler);
        handler = new UnderlineTagHandler();
        fStack.add("ins", handler);
        fStack.add("u", handler);
        handler = new StrikedOutTagHandler();
        fStack.add("del", handler);
        fStack.add("strike", handler);
        fStack.add("s", handler);
        handler = new ItalicTagHandler();
        fStack.add("em", handler);
        fStack.add("i", handler);
        fStack.add("sup", new SuperScriptTagHandler());
        fStack.add("sub", new SubScriptTagHandler());
        fStack.add("tt", new TeletypeTagHandler());
        fStack.add("br", new LineBreakTagHandler());
        fStack.add("div", new DivisionTagHandler());
        handler = new QuoteTagHandler();
        fStack.add("blockquote", handler);
        fStack.add("quote", handler);
        fStack.add("span", new SpanTagHandler());

        // Register extra handlers
        fStack.addAll(extraHandlers);
        
        // Allow each handler to have some initialization
        for (TagHandler tagElementHandler: fStack.fMap.values()) {
            tagElementHandler.initialize(fStack);
        }
    }

    /**
     * @see org.xml.sax.helpers.DefaultHandler#characters(char[], int, int)
     */
    @Override
    public void characters(char[] array, int start, int length)
        throws SAXException {
        if (fAccumulationBuffer != null) {
            fAccumulationBuffer.append(array, start, length);
        }
    }

    /**
     * @see org.xml.sax.helpers.DefaultHandler#endDocument()
     */
    @Override
    public void endDocument() throws SAXException {
        TagHandler.sendEmptyLines(fStack.fPeek);
        fStack.endElement();
    }

    /**
     * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String,
     *      java.lang.String, java.lang.String)
     */
    @Override
    public void endElement(String uri, String localName, String qName)
        throws SAXException {
        if (fAccumulationBuffer != null && fAccumulationBuffer.length() > 0) {
            fStack.onCharacters(fAccumulationBuffer.toString().toCharArray(), 0, fAccumulationBuffer.length());
            fAccumulationBuffer.setLength(0);
        }
        fStack.endElement();
    }

    protected String getHref(Attributes attributes) {
        String value = attributes.getValue("HREF");
        if (value == null)
            value = attributes.getValue("href");
        if (value == null)
            value = attributes.getValue("src");
        if (value == null)
            value = attributes.getValue("SRC");
        return value;
    }

    /**
     * @see org.xml.sax.helpers.DefaultHandler#startDocument()
     */
    @Override
    public void startDocument() throws SAXException {
        fStack.beginElement(null, null);
    }

    /**
     * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String,
     *      java.lang.String, java.lang.String, org.xml.sax.Attributes)
     */
    @Override
    public void startElement(
        String uri,
        String localName,
        String qName,
        Attributes attributes) throws SAXException {
        if (fAccumulationBuffer != null && fAccumulationBuffer.length() > 0) {
            fStack.onCharacters(fAccumulationBuffer.toString().toCharArray(), 0, fAccumulationBuffer.length());
        }
        fAccumulationBuffer = new StringBuffer();
        fStack.beginElement(getLocalName(uri, localName, qName, false), getParameters(attributes));
    }

    // Lexical handler methods

    public void comment(char[] array, int start, int length) throws SAXException
    {
        if (!fIsInDTD) {
            // If there's any characters not yet handled, handle them now.
            if (fAccumulationBuffer != null && fAccumulationBuffer.length() > 0) {
                fStack.onCharacters(fAccumulationBuffer.toString().toCharArray(), 0, fAccumulationBuffer.length());
            }
            fAccumulationBuffer = new StringBuffer();
            fStack.onComment(array, start, length);
        }
    }

    public void endCDATA() throws SAXException
    {
        // Nothing to do
    }

    public void endDTD() throws SAXException
    {
        fIsInDTD = false;
    }

    public void endEntity(String arg0) throws SAXException
    {
        // Nothing to do
    }

    public void startCDATA() throws SAXException
    {
        // Nothing to do
    }

    public void startDTD(String arg0, String arg1, String arg2) throws SAXException
    {
        fIsInDTD = true;
    }

    public void startEntity(String arg0) throws SAXException
    {
        // Nothing to do
    }
    
    private String getLocalName(
        String uri,
        String localName,
        String name,
        boolean upperCase) {
        String result = (localName != null && !"".equals(localName))
            ? localName
            : name;
        return upperCase ? result.toUpperCase() : result;
    }

    private WikiParameters getParameters(Attributes attributes) {
        List<WikiParameter> params = new ArrayList<WikiParameter>();
        for (int i = 0; i < attributes.getLength(); i++) {
            String key = getLocalName(
                attributes.getURI(i),
                attributes.getQName(i),
                attributes.getLocalName(i),
                false);
            String value = attributes.getValue(i);
            WikiParameter param = new WikiParameter(key, value);

            // The XHTML DTD specifies some default value for some attributes. For example for a TD element
            // it defines colspan=1 and rowspan=1. Thus we'll get a colspan and rowspan attribute passed to
            // the current method even though they are not defined in the source XHTML content.
            // However with SAX2 it's possible to check if an attribute is defined in the source or not using
            // the Attributes2 class.
            // See http://www.saxproject.org/apidoc/org/xml/sax/package-summary.html#package_description
            if (attributes instanceof Attributes2) {
                Attributes2 attributes2 = (Attributes2) attributes;
                // If the attribute is present in the XHTML source file then add it, otherwise skip it.
                if (attributes2.isSpecified(i)) {
                    params.add(param);
                }
            } else {
                params.add(param);
            }
        }
        return new WikiParameters(params);
    }
    
}
