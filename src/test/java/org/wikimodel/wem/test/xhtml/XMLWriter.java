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
package org.wikimodel.wem.test.xhtml;

import org.wikimodel.wem.xhtml.filter.DefaultXMLFilter;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * @author vmassol
 * @author thomas.mortagne
 */
public class XMLWriter extends DefaultXMLFilter {
    StringBuffer fBuffer = new StringBuffer();

    public String getBuffer() {
        return fBuffer.toString();
    }

    public void reset() {
        fBuffer.setLength(0);
    }

    @Override
    public void characters(char[] array, int start, int length)
            throws SAXException {
        fBuffer.append(array, start, length);
    }

    @Override
    public void startElement(String uri, String localName, String qName,
            Attributes atts) throws SAXException {
        fBuffer.append("<" + localName + ">");
    }

    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        fBuffer.append("</" + localName + ">");
    }

    @Override
    public void startCDATA() throws SAXException {
        fBuffer.append("<![CDATA[");
    }

    @Override
    public void endCDATA() throws SAXException {
        fBuffer.append("]]>");
    }

    @Override
    public void comment(char[] ch, int start, int length) throws SAXException {
        fBuffer.append("<!--");
        fBuffer.append(ch, start, length);
        fBuffer.append("-->");
    }
}
