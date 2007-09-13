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

import org.wikimodel.wem.WikiParameter;
import org.wikimodel.wem.WikiParameters;
import org.wikimodel.wem.impl.WikiScannerContext;
import org.wikimodel.wem.xhtml.impl.XhtmlHandler.TagStack.TagContext;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author kotelnikov
 */
public class XhtmlHandler extends DefaultHandler {

    // TODO: add management of embedded block elements.
    public static class TagHandler {

        public void begin(TagContext context) {
        }

        public void end(TagContext context) {
        }
    }

    protected static class TagStack {

        public class TagContext {

            private Attributes fAttributes;

            public TagHandler fHandler;

            String fLocalName;

            private TagContext fParent;

            String fQName;

            String fUri;

            public TagContext(
                TagContext parent,
                String uri,
                String localName,
                String qName,
                Attributes attributes) {
                fUri = uri;
                fLocalName = localName;
                fQName = qName;
                fParent = parent;
                fAttributes = attributes;
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

            public boolean isTag(String string) {
                return string.equals(fLocalName.toLowerCase());
            }

        }

        private static Map<String, TagHandler> fMap = new HashMap<String, TagHandler>();

        public static void add(String tag, TagHandler handler) {
            fMap.put(tag, handler);
        }

        private TagContext fPeek;

        WikiScannerContext fScannerContext;

        public TagStack(WikiScannerContext context) {
            fScannerContext = context;
        }

        public void beginElement(
            String uri,
            String localName,
            String qName,
            Attributes attributes) {
            fPeek = new TagContext(fPeek, uri, localName, qName, attributes);
            localName = fPeek.getName();
            TagHandler handler = fMap.get(localName);
            if (handler != null) {
                handler.begin(fPeek);
                fPeek.fHandler = handler;
            }
        }

        public void endElement() {
            if (fPeek.fHandler != null)
                fPeek.fHandler.end(fPeek);
            fPeek = fPeek.fParent;
        }

        public WikiScannerContext getScannerContext() {
            return fScannerContext;
        }

        public void onCharacters(char[] array, int start, int length) {
            StringBuffer buf = new StringBuffer();
            boolean spaces = false;
            for (int i = 0; i < length; i++) {
                char ch = array[start + i];
                if (Character.isSpaceChar(ch) != spaces) {
                    if (buf.length() > 0) {
                        String str = buf.toString();
                        if (spaces)
                            fScannerContext.onSpace(str);
                        else
                            fScannerContext.onWord(str);
                    }
                    spaces = !spaces;
                    buf.delete(0, buf.length());
                }
                buf.append(ch);
            }
            if (buf.length() > 0) {
                String str = buf.toString();
                if (spaces)
                    fScannerContext.onSpace(str);
                else
                    fScannerContext.onWord(str);
            }
        }
    }

    static {
        TagStack.add("html", new TagHandler() {
            public void begin(TagContext context) {
                context.getScannerContext().beginDocument();
            }

            public void end(TagContext context) {
                context.getScannerContext().endDocument();
            }
        });

        // Simple block elements (p, pre, quotation...)
        TagStack.add("p", new TagHandler() {
            public void begin(TagContext context) {
                context.getScannerContext().beginParagraph(context.getParams());
            }

            public void end(TagContext context) {
                context.getScannerContext().endParagraph();
            }
        });

        // Tables
        TagStack.add("table", new TagHandler() {
            public void begin(TagContext context) {
                context.getScannerContext().beginTable(context.getParams());
            }

            public void end(TagContext context) {
                context.getScannerContext().endTable();
            }
        });
        TagStack.add("tr", new TagHandler() {
            public void begin(TagContext context) {
                context.getScannerContext().beginTableRow(false);
            }

            public void end(TagContext context) {
                context.getScannerContext().endTableRow();
            }
        });
        TagHandler handler = new TagHandler() {
            public void begin(TagContext context) {
                context.getScannerContext().beginTableCell(context.isTag("th"));
            }

            public void end(TagContext context) {
                context.getScannerContext().endTableRow();
            }
        };
        TagStack.add("td", handler);
        TagStack.add("th", handler);

        // Lists
        handler = new TagHandler() {
            public void begin(TagContext context) {
                context.getScannerContext().beginList();
            }

            public void end(TagContext context) {
                context.getScannerContext().endList();
            }
        };
        TagStack.add("ul", handler);
        TagStack.add("ol", handler);
        TagStack.add("li", new TagHandler() {
            public void begin(TagContext context) {
                context.getScannerContext().beginListItem("*");
            }

            public void end(TagContext context) {
                context.getScannerContext().endListItem();
            }
        });

        TagStack.add("dl", new TagHandler() {
            public void begin(TagContext context) {
                context.getScannerContext().beginList();
            }

            public void end(TagContext context) {
                context.getScannerContext().endList();
            }
        });
        TagStack.add("dt", new TagHandler() {
            public void begin(TagContext context) {
                context.getScannerContext().beginListItem(";");
            }

            public void end(TagContext context) {
                context.getScannerContext().endListItem();
            }
        });
        TagStack.add("dd", new TagHandler() {
            public void begin(TagContext context) {
                context.getScannerContext().beginListItem(":");
            }

            public void end(TagContext context) {
                context.getScannerContext().endListItem();
            }
        });

        // Headers
        handler = new TagHandler() {
            public void begin(TagContext context) {
                String tag = context.getName();
                int level = Integer.parseInt(tag.substring(1, 2));
                context.getScannerContext().beginHeader(level);
            }

            public void end(TagContext context) {
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
        TagStack.add("hr", new TagHandler() {
            public void begin(TagContext context) {
                context.getScannerContext().onHorizontalLine();
            }
        });
    }

    protected String fDocumentSectionUri;

    protected String fDocumentUri;

    protected String fDocumentWikiProperties;

    TagStack fStack;

    /**
     * @param context
     */
    public XhtmlHandler(WikiScannerContext context) {
        fStack = new TagStack(context);
    }

    /**
     * @see org.xml.sax.helpers.DefaultHandler#characters(char[], int, int)
     */
    public void characters(char[] array, int start, int length)
        throws SAXException {
        fStack.onCharacters(array, start, length);
    }

    /**
     * @see org.xml.sax.helpers.DefaultHandler#endDocument()
     */
    public void endDocument() throws SAXException {
        fStack.endElement();
    }

    /**
     * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String,
     *      java.lang.String, java.lang.String)
     */
    public void endElement(String uri, String localName, String qName)
        throws SAXException {
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
    public void startDocument() throws SAXException {
        fStack.beginElement(null, null, null, null);
    }

    /**
     * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String,
     *      java.lang.String, java.lang.String, org.xml.sax.Attributes)
     */
    public void startElement(
        String uri,
        String localName,
        String qName,
        Attributes attributes) throws SAXException {
        fStack.beginElement(uri, localName, qName, attributes);
    }

}
