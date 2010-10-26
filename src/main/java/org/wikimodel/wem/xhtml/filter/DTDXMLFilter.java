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

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/**
 * Skip all callbacks when parsing the DTD.
 * 
 * @author vmassol
 */
public class DTDXMLFilter extends DefaultXMLFilter {
    /**
     * We want to accumulate characters only when not parsing the DTD.
     */
    private boolean fIsInDTD;

    public DTDXMLFilter() {
        super();
    }

    public DTDXMLFilter(XMLReader reader) {
        super(reader);
    }

    /**
     * @see org.xml.sax.helpers.DefaultHandler#characters(char[], int, int)
     */
    @Override
    public void characters(char[] array, int start, int length)
        throws SAXException {
        if (!fIsInDTD) {
            super.characters(array, start, length);
        }
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
        if (!fIsInDTD) {
            super.startElement(uri, localName, qName, attributes);
        }
    }

    /**
     * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String,
     *      java.lang.String, java.lang.String)
     */
    @Override
    public void endElement(String uri, String localName, String qName)
        throws SAXException {
        if (!fIsInDTD) {
            super.endElement(uri, localName, qName);
        }
    }

    @Override
    public void comment(char[] array, int start, int length)
        throws SAXException {
        if (!fIsInDTD) {
            super.comment(array, start, length);
        }
    }

    @Override
    public void startCDATA() throws SAXException {
        if (!fIsInDTD) {
            super.startCDATA();
        }
    }

    @Override
    public void endCDATA() throws SAXException {
        if (!fIsInDTD) {
            super.endCDATA();
        }
    }

    @Override
    public void startDTD(String name, String publicId, String systemId)
        throws SAXException {
        fIsInDTD = true;
        super.startDTD(name, publicId, systemId);
    }

    @Override
    public void endDTD() throws SAXException {
        fIsInDTD = false;
        super.endDTD();
    }
}
