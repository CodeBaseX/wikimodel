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

import java.io.IOException;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.XMLFilterImpl;

/**
 * @author vmassol
 */
public class DefaultXMLFilter extends XMLFilterImpl implements LexicalHandler {
    public static final String SAX_LEXICAL_HANDLER_PROPERTY = "http://xml.org/sax/properties/lexical-handler";

    private LexicalHandler lexicalHandler;

    public DefaultXMLFilter() {
        super();
    }

    public DefaultXMLFilter(XMLReader reader) {
        super(reader);
    }

    @Override
    public void parse(InputSource input) throws SAXException, IOException {
        if (getParent() != null) {
            getParent().setProperty(SAX_LEXICAL_HANDLER_PROPERTY, this);
        }
        super.parse(input);
    }

    @Override
    public void setProperty(String name, Object value)
        throws SAXNotRecognizedException,
        SAXNotSupportedException {
        // We save the lexical handler so that we can use it in the
        // implementation of the LexicalHandler interface methods.
        if (SAX_LEXICAL_HANDLER_PROPERTY.equals(name)) {
            this.lexicalHandler = (LexicalHandler) value;
        } else {
            super.setProperty(name, value);
        }
    }

    @Override
    public Object getProperty(String name)
        throws SAXNotRecognizedException,
        SAXNotSupportedException {
        if (SAX_LEXICAL_HANDLER_PROPERTY.equals(name)) {
            return this.lexicalHandler;
        } else {
            return super.getProperty(name);
        }
    }

    public void comment(char[] ch, int start, int length) throws SAXException {
        if (this.lexicalHandler != null) {
            this.lexicalHandler.comment(ch, start, length);
        }
    }

    public void endCDATA() throws SAXException {
        if (this.lexicalHandler != null) {
            this.lexicalHandler.endCDATA();
        }
    }

    public void endDTD() throws SAXException {
        if (this.lexicalHandler != null) {
            this.lexicalHandler.endDTD();
        }
    }

    public void endEntity(String name) throws SAXException {
        if (this.lexicalHandler != null) {
            this.lexicalHandler.endEntity(name);
        }
    }

    public void startCDATA() throws SAXException {
        if (this.lexicalHandler != null) {
            this.lexicalHandler.startCDATA();
        }
    }

    public void startDTD(String name, String publicId, String systemId)
        throws SAXException {
        if (this.lexicalHandler != null) {
            this.lexicalHandler.startDTD(name, publicId, systemId);
        }
    }

    public void startEntity(String name) throws SAXException {
        if (this.lexicalHandler != null) {
            this.lexicalHandler.startEntity(name);
        }
    }
}
