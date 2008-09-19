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

import org.wikimodel.wem.IWemConstants;
import org.wikimodel.wem.WikiPageUtil;
import org.wikimodel.wem.WikiParameter;
import org.wikimodel.wem.WikiParameters;
import org.wikimodel.wem.WikiReference;
import org.wikimodel.wem.impl.WikiScannerContext;
import org.wikimodel.wem.xhtml.XhtmlCharacter;
import org.wikimodel.wem.xhtml.XhtmlCharacterType;
import org.wikimodel.wem.xhtml.XhtmlEscapeHandler;
import org.wikimodel.wem.xhtml.impl.XhtmlHandler.TagStack.TagContext;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author kotelnikov
 * @author vmassol
 */
public class XhtmlHandler extends DefaultHandler {

    // TODO: add management of embedded block elements.
    public static class TagHandler {

        /**
         * @param context
         * @return <code>true</code> if the current tag represented by the given
         *         context requires a parent document
         */
        private static boolean requiresParentDocument(TagContext context) {
            if (context == null)
                return true;
            if (context.fHandler == null
                || !context.fHandler.requiresDocument())
                return false;
            boolean inContainer = false;
            TagContext parent = context.fParent;
            while (parent != null) {
                if (parent.fHandler != null) {
                    inContainer = parent.fHandler.isDocumentContainer();
                    break;
                }
                parent = parent.fParent;
            }
            return inContainer;
        }

        public boolean fAccumulateContent;

        /**
         * This flag is <code>true</code> if the current tag can have a text
         * content
         */
        private final boolean fContentContainer;

        /**
         * This flag shows if the current tag can be used as a container for
         * embedded documents.
         */
        private final boolean fDocumentContainer;

        /**
         * This flag shows if the current tag should be created as a direct
         * child of a document.
         */
        private final boolean fRequiresDocument;

        /**
         * @param documentContainer
         * @param requiresDocument
         * @param contentContainer
         */
        public TagHandler(
            boolean documentContainer,
            boolean requiresDocument,
            boolean contentContainer) {
            fDocumentContainer = documentContainer;
            fRequiresDocument = requiresDocument;
            fContentContainer = contentContainer;
        }

        protected void begin(TagContext context) {
        }

        public void beginElement(TagContext context) {
            begin(context);
        }

        protected void end(TagContext context) {
        }

        public final void endElement(TagContext context) {
            end(context);
        }

        public boolean isContentContainer() {
            return fContentContainer;
        }

        public boolean isDocumentContainer() {
            return fDocumentContainer;
        }

        public boolean requiresDocument() {
            return fRequiresDocument;
        }
    }

    protected static class TagStack {

        public class TagContext {

            private final Attributes fAttributes;

            private StringBuffer fContent;

            public TagHandler fHandler;

            String fLocalName;

            private final TagContext fParent;

            String fQName;

            String fUri;

            TagStack fTagStack;
            
            public TagContext(
                TagContext parent,
                String uri,
                String localName,
                String qName,
                Attributes attributes,
                TagStack tagStack) {
                fUri = uri;
                fLocalName = localName;
                fQName = qName;
                fParent = parent;
                fAttributes = attributes;
                fTagStack = tagStack; 
            }

            public boolean appendContent(char[] array, int start, int length) {
                if (fHandler == null || !fHandler.fAccumulateContent)
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

            public String getLocalName() {
                return fLocalName;
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

            public String getName() {
                return getLocalName(fUri, fLocalName, fQName, false);
            }

            public WikiParameters getParams() {
                List<WikiParameter> list = new ArrayList<WikiParameter>();
                int len = fAttributes != null ? fAttributes.getLength() : 0;
                for (int i = 0; i < len; i++) {
                    String key = getLocalName(
                        fAttributes.getURI(i),
                        fAttributes.getQName(i),
                        fAttributes.getLocalName(i),
                        false);
                    String value = fAttributes.getValue(i);
                    WikiParameter param = new WikiParameter(key, value);
                    list.add(param);
                }
                WikiParameters params = new WikiParameters(list);
                return params;
            }

            public TagContext getParent() {
                return fParent;
            }

            public String getQName() {
                return fQName;
            }

            public WikiScannerContext getScannerContext() {
                return fScannerContext;
            }

            public String getUri() {
                return fUri;
            }

            public TagStack getTagStack() {
                return fTagStack;
            }
            
            public boolean isContentContainer() {
                return fHandler == null || fHandler.isContentContainer();
            }

            public boolean isTag(String string) {
                return string.equals(fLocalName.toLowerCase());
            }

        }

        private static final int CHARACTER = 0;

        private static Map<String, TagHandler> fMap = new HashMap<String, TagHandler>();

        private static final int NEW_LINE = 3;

        private static final char SPACE = 1;

        private static final int SPECIAL_SYMBOL = 2;

        /**
         * Allow saving parameters. 
         * For example we save the number of br elements if we're outside 
         * of a block element so that we can emit an onEmptyLines event.
         */
        private Map<String, Object> fStackParameters = new HashMap<String, Object>(); 
        
        public static void add(String tag, TagHandler handler) {
            fMap.put(tag, handler);
        }

        private TagContext fPeek;

        WikiScannerContext fScannerContext;

        XhtmlEscapeHandler fEscapeHandler;

        public TagStack(WikiScannerContext context, XhtmlEscapeHandler escapeHandler) {
            fScannerContext = context;
            fEscapeHandler = escapeHandler;
            
            // Pre-initialize stack parameters for performance reason 
            // (so that we don't have to check all the time if they're initialized or not)
            setStackParameter("emptyLinesCount", 0);
            setStackParameter("listStyles", new StringBuffer());
        }

        public void beginElement(
            String uri,
            String localName,
            String qName,
            Attributes attributes) {
            fPeek = new TagContext(fPeek, uri, localName, qName, attributes, this);
            localName = fPeek.getName();
            TagHandler handler = fMap.get(localName);
            fPeek.beginElement(handler);
        }

        public void endElement() {
            fPeek.endElement();
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
            if (!fPeek.appendContent(array, start, length)) {
                Stack<XhtmlCharacter> stack = new Stack<XhtmlCharacter>();
                Map<String, Object> characterContext = new HashMap<String, Object>();
                fEscapeHandler.initialize(characterContext);
                for (int i = 0; i < length; i++) {
                    XhtmlCharacter current = new XhtmlCharacter(array[start + i], getCharacterType(array[start + i]));
                    XhtmlCharacter result = current;
                    if (fEscapeHandler != null) {

                        // In order to find the HTML tag being handled we need to find the first non null handler
                        TagContext context = fPeek;
                        TagHandler handler = context.fHandler;
                        while ((handler == null) && (context.getParent() != null)) {
                            context = context.getParent();
                            handler = context.fHandler;
                        }
                        String tag;
                        if (handler == null) {
                            // We haven't found a handler. It means we're inside the top element and we assume we're on an implicit paragraph.
                            tag = "p";
                        } else {
                            tag = context.getLocalName().toLowerCase();
                        }
                        
                        result = fEscapeHandler.handleCharacter(current, stack, tag, characterContext);
                    }
                    stack.push(result);
                }
                
                // Now send the events.
                flushStack(stack);
            }
        }
        
        public void setStackParameter(String name, Object data) {
        	fStackParameters.put(name, data);
        }
        
        public Object getStackParameter(String name) {
        	return fStackParameters.get(name);
        }

    }

    static {
        
        // TagStack.add("html", new TagHandler(false, false, true) {
        // @Override
        // public void begin(TagContext context) {
        // context.getScannerContext().beginDocument();
        // }
        //
        // @Override
        // public void end(TagContext context) {
        // context.getScannerContext().endDocument();
        // }
        // });

        // Simple block elements (p, pre, quotation...)
        TagStack.add("p", new TagHandler(false, true, true) {
            @Override
            protected void begin(TagContext context) {
                sendEmptyLines(context);
                context.getScannerContext().beginParagraph(context.getParams());
            }

            @Override
            protected void end(TagContext context) {
                context.getScannerContext().endParagraph();
            }
        });

        // Tables
        TagStack.add("table", new TagHandler(false, true, false) {
            @Override
            protected void begin(TagContext context) {
                sendEmptyLines(context);
                context.getScannerContext().beginTable(context.getParams());
            }

            @Override
            protected void end(TagContext context) {
                context.getScannerContext().endTable();
            }
        });
        TagStack.add("tr", new TagHandler(false, false, false) {
            @Override
            protected void begin(TagContext context) {
                context.getScannerContext().beginTableRow(false);
            }

            @Override
            protected void end(TagContext context) {
                context.getScannerContext().endTableRow();
            }
        });
        TagHandler handler = new TagHandler(true, false, true) {
            @Override
            protected void begin(TagContext context) {
                context.getScannerContext().beginTableCell(context.isTag("th"));
            }

            @Override
            protected void end(TagContext context) {
                context.getScannerContext().endTableCell();
            }
        };
        TagStack.add("td", handler);
        TagStack.add("th", handler);

        // Lists
        handler = new TagHandler(false, true, false) {
            @Override
            protected void begin(TagContext context) {
                sendEmptyLines(context);
                // We only send a new list event if we're not already inside a list.
                StringBuffer listStyles = (StringBuffer) context.getTagStack().getStackParameter("listStyles");
                if (listStyles.length() == 0) {
                    context.getScannerContext().beginList(context.getParams());
                }
            }

            @Override
            protected void end(TagContext context) {
                // Note: Do not generate an endList() event since it'll be generated automatically by the next element.
            }
        };
        TagStack.add("ul", handler);
        TagStack.add("ol", handler);
        TagStack.add("li", new TagHandler(true, false, true) {
            @Override
            public void begin(TagContext context) {
                String markup = context.getParent().getName().equals("ol")
                    ? "#"
                    : "*";
                StringBuffer listStyles = (StringBuffer) context.getTagStack().getStackParameter("listStyles");
                listStyles.append(markup);
                context.getScannerContext().beginListItem(listStyles.toString());
            }

            @Override
            protected void end(TagContext context) {
                StringBuffer listStyles = (StringBuffer) context.getTagStack().getStackParameter("listStyles");
                // We should always have a length greater than 0 but we handle 
                // the case where the user has entered some badly formed HTML
                if (listStyles.length() > 0) {
                    listStyles.setLength(listStyles.length() - 1);
                }
                // Note: Do not generate an endListItem() event since it'll be generated automatically by the next element.
            }
        });

        TagStack.add("dl", new TagHandler(false, true, false) {
            @Override
            protected void begin(TagContext context) {
                sendEmptyLines(context);
                context.getScannerContext().beginList();
            }

            @Override
            protected void end(TagContext context) {
                context.getScannerContext().endList();
            }
        });
        TagStack.add("dt", new TagHandler(false, false, true) {
            @Override
            protected void begin(TagContext context) {
                context.getScannerContext().beginListItem(";");
            }

            @Override
            protected void end(TagContext context) {
                context.getScannerContext().endListItem();
            }
        });
        TagStack.add("dd", new TagHandler(true, false, true) {
            @Override
            protected void begin(TagContext context) {
                context.getScannerContext().beginListItem(":");
            }

            @Override
            protected void end(TagContext context) {
                context.getScannerContext().endListItem();
            }
        });

        // Headers
        handler = new TagHandler(false, true, true) {
            @Override
            protected void begin(TagContext context) {
                String tag = context.getName();
                int level = Integer.parseInt(tag.substring(1, 2));
                sendEmptyLines(context);
                context.getScannerContext().beginHeader(level);
            }

            @Override
            protected void end(TagContext context) {
                context.getScannerContext().endHeader();
            }
        };
        TagStack.add("h1", handler);
        TagStack.add("h2", handler);
        TagStack.add("h3", handler);
        TagStack.add("h4", handler);
        TagStack.add("h5", handler);
        TagStack.add("h6", handler);

        // Unique block elements
        TagStack.add("hr", new TagHandler(false, true, false) {
            @Override
            protected void begin(TagContext context) {
                sendEmptyLines(context);
                context.getScannerContext().onHorizontalLine();
            }
        });
        TagStack.add("pre", new TagHandler(false, true, true) {
            {
                fAccumulateContent = true;
            }

            @Override
            protected void end(TagContext context) {
                String str = context.getContent();
                sendEmptyLines(context);
                context.getScannerContext().onVerbatim(str, false);
            }
        });

        // In-line elements
        TagStack.add("a", new TagHandler(false, false, true) {
            {
                fAccumulateContent = true;
            }

            @Override
            protected void begin(TagContext context) {
            }

            @Override
            protected void end(TagContext context) {
                // TODO: it should be replaced by a normal parameters
                WikiParameter ref = context.getParams().getParameter("href");
                // Check if there's a class attribute with a "wikimodel-freestanding" value.
                // If so it means we have a free standing link.
                String classValue = context.fAttributes.getValue("class");
                if (ref != null) {
                    if ((classValue != null) && classValue.equalsIgnoreCase("wikimodel-freestanding")) {
                        context.getScannerContext().onReference(ref.getValue());
                    } else {
                        String content = context.getContent();
                        WikiReference reference = new WikiReference(
                            ref.getValue(),
                            content);
                        context.getScannerContext().onReference(reference);
                    }
                }
            }
        });

        handler = new TagHandler(false, false, true) {
            @Override
            protected void begin(TagContext context) {
                context.getScannerContext().onFormat(IWemConstants.STRONG);
            }

            @Override
            protected void end(TagContext context) {
                context.getScannerContext().onFormat(IWemConstants.STRONG);
            }
        };
        TagStack.add("strong", handler);
        TagStack.add("b", handler);
        
        handler = new TagHandler(false, false, true) {
            @Override
            protected void begin(TagContext context) {
                context.getScannerContext().onFormat(IWemConstants.INS);
            }

            @Override
            protected void end(TagContext context) {
                context.getScannerContext().onFormat(IWemConstants.INS);
            }
        };
        TagStack.add("u", handler);
        TagStack.add("ins", handler);

        handler = new TagHandler(false, false, true) {
            @Override
            protected void begin(TagContext context) {
                context.getScannerContext().onFormat(IWemConstants.STRIKE);
            }

            @Override
            protected void end(TagContext context) {
                context.getScannerContext().onFormat(IWemConstants.STRIKE);
            }
        };
        TagStack.add("del", handler);
        TagStack.add("s", handler);
        TagStack.add("strike", handler);
        
        handler = new TagHandler(false, false, true) {
            @Override
            protected void begin(TagContext context) {
                context.getScannerContext().onFormat(IWemConstants.EM);
            }

            @Override
            protected void end(TagContext context) {
                context.getScannerContext().onFormat(IWemConstants.EM);
            }
        };
        TagStack.add("em", handler);
        TagStack.add("i", handler);
        
        handler = new TagHandler(false, false, true) {
            @Override
            protected void begin(TagContext context) {
                context.getScannerContext().onFormat(IWemConstants.SUP);
            }

            @Override
            protected void end(TagContext context) {
                context.getScannerContext().onFormat(IWemConstants.SUP);
            }
        };
        TagStack.add("sup", handler);
        
        handler = new TagHandler(false, false, true) {
            @Override
            protected void begin(TagContext context) {
                context.getScannerContext().onFormat(IWemConstants.SUB);
            }

            @Override
            protected void end(TagContext context) {
                context.getScannerContext().onFormat(IWemConstants.SUB);
            }
        };
        TagStack.add("sub", handler);

        // There are 2 possible output for <tt>:
        // * If there a class="wikimodel-verbatim" specified then we emit a onVerbatimInline() event
        // * If there no class or a class with another value then we emit a Monospace Format event.
        TagStack.add("tt", new TagHandler(false, false, true) {
            @Override
            protected void begin(TagContext context) {
                String classValue = context.fAttributes.getValue("class");
                if ((classValue != null) && classValue.equalsIgnoreCase("wikimodel-verbatim")) {
                    fAccumulateContent = true;
                } else {
                    context.getScannerContext().onFormat(IWemConstants.MONO);
                }
            }

            @Override
            protected void end(TagContext context) {
                String classValue = context.fAttributes.getValue("class");
                if ((classValue != null) && classValue.equalsIgnoreCase("wikimodel-verbatim")) {
                    String str = context.getContent();
                    context.getScannerContext().onVerbatim(str, true);
                } else {
                    context.getScannerContext().onFormat(IWemConstants.MONO);
                }
            }
        }); 

        TagStack.add("br", new TagHandler(false, false, false) {
            @Override
            protected void begin(TagContext context) {
            	// If the parent is not one of the known tags and if it's 
            	// not the "html" one then we consider we're outside of a block
            	// element and we save the number of <br/> to emit an 
            	// onEmptyLines event.
            	if ((context.getParent() == null) || (context.getParent().isTag("html"))
            	    || (context.getParent().isTag("body"))) {
            		int value = (Integer) context.getTagStack().getStackParameter("emptyLinesCount");
            		value++;
            		context.getTagStack().setStackParameter("emptyLinesCount", value);
            	} else {
            		context.getScannerContext().onLineBreak();
            	}
            }
        });
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
    private StringBuffer accumulationBuffer;

    /**
     * @param context
     */
    public XhtmlHandler(WikiScannerContext context, XhtmlEscapeHandler escapeHandler) {
        fStack = new TagStack(context, escapeHandler);
    }

    /**
     * @see org.xml.sax.helpers.DefaultHandler#characters(char[], int, int)
     */
    @Override
    public void characters(char[] array, int start, int length)
        throws SAXException {
        if (accumulationBuffer != null) {
            accumulationBuffer.append(array, start, length);
        }
    }

    /**
     * @see org.xml.sax.helpers.DefaultHandler#endDocument()
     */
    @Override
    public void endDocument() throws SAXException {
        fStack.endElement();
    }

    /**
     * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String,
     *      java.lang.String, java.lang.String)
     */
    @Override
    public void endElement(String uri, String localName, String qName)
        throws SAXException {
        if (accumulationBuffer != null && accumulationBuffer.length() > 0) {
            fStack.onCharacters(accumulationBuffer.toString().toCharArray(), 0, accumulationBuffer.length());
            accumulationBuffer.setLength(0);
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
        fStack.beginElement(null, null, null, null);
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
        if (accumulationBuffer != null && accumulationBuffer.length() > 0) {
            fStack.onCharacters(accumulationBuffer.toString().toCharArray(), 0, accumulationBuffer.length());
        }
        accumulationBuffer = new StringBuffer();
        fStack.beginElement(uri, localName, qName, attributes);
    }

    /**
     * Check if we need to emit an onEmptyLines() event.
     */
    public static void sendEmptyLines(TagContext context) {
        int lineCount = (Integer) context.getTagStack().getStackParameter("emptyLinesCount"); 
        if (lineCount > 0) {
            context.getScannerContext().onEmptyLines(lineCount - 1);
            context.getTagStack().setStackParameter("emptyLinesCount", null);
        }
    }
    
}
