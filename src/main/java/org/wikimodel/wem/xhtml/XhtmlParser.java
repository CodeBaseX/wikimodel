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
package org.wikimodel.wem.xhtml;

import java.io.Reader;
import java.util.Collections;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.wikimodel.wem.IWemListener;
import org.wikimodel.wem.IWikiParser;
import org.wikimodel.wem.WikiParserException;
import org.wikimodel.wem.impl.WikiScannerContext;
import org.wikimodel.wem.xhtml.filter.AccumulationXMLFilter;
import org.wikimodel.wem.xhtml.filter.DTDXMLFilter;
import org.wikimodel.wem.xhtml.filter.XHTMLWhitespaceXMLFilter;
import org.wikimodel.wem.xhtml.handler.CommentHandler;
import org.wikimodel.wem.xhtml.handler.TagHandler;
import org.wikimodel.wem.xhtml.impl.LocalEntityResolver;
import org.wikimodel.wem.xhtml.impl.XhtmlHandler;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author MikhailKotelnikov
 */
public class XhtmlParser implements IWikiParser {

    private Map<String, TagHandler> fExtraHandlers;

    private CommentHandler fCommentHandler;

    /**
     * Optional XML Reader that can be specified. This is the solution for
     * setting up custom XML filters.
     */
    private XMLReader fXmlReader;

    public XhtmlParser() {
        fExtraHandlers = Collections.<String, TagHandler> emptyMap();
        fCommentHandler = new CommentHandler();
    }

    public void setExtraHandlers(Map<String, TagHandler> extraHandlers) {
        fExtraHandlers = extraHandlers;
    }

    public void setCommentHandler(CommentHandler commentHandler) {
        fCommentHandler = commentHandler;
    }

    public void setXmlReader(XMLReader xmlReader) {
        fXmlReader = xmlReader;
    }

    /**
     * @param listener the listener object wich will be used to report about all
     *        structural elements on the wiki page.
     * @return a XHTML SAX handler wich can be used to generate well-formed
     *         sequence of WEM events; all events will be reported to the given
     *         listener object.
     */
    public DefaultHandler getHandler(IWemListener listener) {
        WikiScannerContext context = new WikiScannerContext(listener);
        XhtmlHandler handler = new XhtmlHandler(
            context,
            fExtraHandlers,
            fCommentHandler);
        return handler;
    }

    /**
     * @see org.wikimodel.wem.IWikiParser#parse(java.io.Reader,
     *      org.wikimodel.wem.IWemListener)
     */
    public void parse(Reader reader, IWemListener listener)
        throws WikiParserException {
        try {
            XMLReader xmlReader = getXMLReader();

            // The WikiModel-specific handler
            DefaultHandler handler = getHandler(listener);

            xmlReader
                .setFeature("http://xml.org/sax/features/namespaces", true);
            xmlReader.setEntityResolver(new LocalEntityResolver());
            xmlReader.setContentHandler(handler);
            xmlReader.setProperty(
                "http://xml.org/sax/properties/lexical-handler",
                handler);

            InputSource source = new InputSource(reader);
            xmlReader.parse(source);

        } catch (Exception e) {
            throw new WikiParserException(e);
        }
    }

    private XMLReader getXMLReader() throws Exception {
        XMLReader reader;

        if (fXmlReader != null) {
            reader = fXmlReader;
        } else {
            SAXParserFactory parserFactory = SAXParserFactory.newInstance();
            SAXParser parser = parserFactory.newSAXParser();
            XMLReader xmlReader = parser.getXMLReader();

            // Ignore SAX callbacks when the parser parses the DTD
            DTDXMLFilter dtdFilter = new DTDXMLFilter(xmlReader);

            // Add a XML Filter to accumulate onCharacters() calls since SAX
            // parser may call it several times.
            AccumulationXMLFilter accumulationFilter = new AccumulationXMLFilter(
                dtdFilter);

            // Add a XML Filter to remove non-semantic white spaces. We need to
            // do that since all WikiModel
            // events contain only semantic information.
            XHTMLWhitespaceXMLFilter whitespaceFilter = new XHTMLWhitespaceXMLFilter(
                accumulationFilter);

            reader = whitespaceFilter;
        }

        return reader;
    }
}
