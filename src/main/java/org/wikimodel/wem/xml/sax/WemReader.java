/**
 * 
 */
package org.wikimodel.wem.xml.sax;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.wikimodel.wem.IWikiParser;
import org.wikimodel.wem.WikiParserException;
import org.wikimodel.wem.xml.WemTagNotifier;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;

/**
 * @author kotelnikov
 */
public class WemReader implements XMLReader {

    private ContentHandler fContentHandler;

    private DTDHandler fDTDHandler;

    private EntityResolver fEntityResolver;

    private ErrorHandler fErrorHandler;

    private IWikiParser fParser;

    protected Map<String, Object> fProperties = new HashMap<String, Object>();

    /**
     * 
     */
    public WemReader(IWikiParser parser) {
        fParser = parser;
    }

    /**
     * @see org.xml.sax.XMLReader#getContentHandler()
     */
    public ContentHandler getContentHandler() {
        return fContentHandler;
    }

    /**
     * @see org.xml.sax.XMLReader#getDTDHandler()
     */
    public DTDHandler getDTDHandler() {
        return fDTDHandler;
    }

    /**
     * @see org.xml.sax.XMLReader#getEntityResolver()
     */
    public EntityResolver getEntityResolver() {
        return fEntityResolver;
    }

    /**
     * @see org.xml.sax.XMLReader#getErrorHandler()
     */
    public ErrorHandler getErrorHandler() {
        return fErrorHandler;
    }

    /**
     * @see org.xml.sax.XMLReader#getFeature(java.lang.String)
     */
    public boolean getFeature(String name)
        throws SAXNotRecognizedException,
        SAXNotSupportedException {
        // throw new SAXNotSupportedException();
        return false;
    }

    /**
     * @see org.xml.sax.XMLReader#getProperty(java.lang.String)
     */
    public Object getProperty(String name)
        throws SAXNotRecognizedException,
        SAXNotSupportedException {
        return fProperties.get(name);
    }

    /**
     * @see org.xml.sax.XMLReader#parse(org.xml.sax.InputSource)
     */
    public void parse(InputSource input) throws IOException, SAXException {
        Reader reader = input.getCharacterStream();
        parse(reader);
    }

    /**
     * @param reader
     * @throws SAXException
     * @throws IOException
     */
    private void parse(Reader reader) throws SAXException, IOException {
        try {
            WemToSax handler = new WemToSax(fContentHandler);
            WemTagNotifier tagNotifier = new WemTagNotifier(handler);
            fParser.parse(reader, tagNotifier);
        } catch (WikiParserException e) {
            throw new SAXException(e);
        } finally {
            reader.close();
        }
    }

    /**
     * @see org.xml.sax.XMLReader#parse(java.lang.String)
     */
    public void parse(String systemId) throws IOException, SAXException {
        URL url = new URL(systemId);
        InputStream input = url.openStream();
        try {
            InputStreamReader reader = new InputStreamReader(input, "UTF-8");
            parse(reader);
        } finally {
            input.close();
        }
    }

    /**
     * @see org.xml.sax.XMLReader#setContentHandler(org.xml.sax.ContentHandler)
     */
    public void setContentHandler(ContentHandler handler) {
        fContentHandler = handler;
    }

    /**
     * @see org.xml.sax.XMLReader#setDTDHandler(org.xml.sax.DTDHandler)
     */
    public void setDTDHandler(DTDHandler handler) {
        fDTDHandler = handler;
    }

    /**
     * @see org.xml.sax.XMLReader#setEntityResolver(org.xml.sax.EntityResolver)
     */
    public void setEntityResolver(EntityResolver resolver) {
        fEntityResolver = resolver;
    }

    /**
     * @see org.xml.sax.XMLReader#setErrorHandler(org.xml.sax.ErrorHandler)
     */
    public void setErrorHandler(ErrorHandler handler) {
        fErrorHandler = handler;
    }

    /**
     * @see org.xml.sax.XMLReader#setFeature(java.lang.String, boolean)
     */
    public void setFeature(String name, boolean value)
        throws SAXNotRecognizedException,
        SAXNotSupportedException {
        // FIXME:
    }

    /**
     * @see org.xml.sax.XMLReader#setProperty(java.lang.String,
     *      java.lang.Object)
     */
    public void setProperty(String name, Object value)
        throws SAXNotRecognizedException,
        SAXNotSupportedException {
        fProperties.put(name, value);
    }

}
