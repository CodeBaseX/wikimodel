/*
 * Created on 17 ao�t 2004
 * @author kotelnikov
 */
package org.wikimodel.fsm.xml;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Stack;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * This is a common superclass for all readers loading configurations from xml
 * files. It contains some utility methods like {@link #parse(InputStream)},
 * {@link #parseResource(ClassLoader, String)} or {@link #parseResource(String)}.
 * 
 * @author kotelnikov
 */
public class ConfigParser {

    protected final static Logger log = Logger.getLogger(ConfigParser.class
        .getName());

    /**
     * 
     */
    public static class NodeHandler {

        /**
         * @param uri
         * @param localName
         * @param name
         * @param attributes
         * @throws Exception
         */
        public void beginNode(
            String uri,
            String localName,
            String name,
            Attributes attributes) throws Exception {
            //
        }

        /**
         * @param uri
         * @param localName
         * @param name
         * @param attributes
         * @throws Exception
         */
        public void endNode(
            String uri,
            String localName,
            String name,
            Attributes attributes) throws Exception {
            //
        }

    }

    /**
     * @author kotelnikov
     */
    static class NodeInfo {
        /**
         *
         */
        public Attributes fAttributes;

        /**
         *
         */
        public String fLocalName;

        /**
         *
         */
        protected NodeInfo fParent;

        /**
         *
         */
        private String fPath;

        /**
         *
         */
        public String fQName;

        /**
         *
         */
        public String fUri;

        /**
         * @param parent
         * @param uri
         * @param localName
         * @param qName
         * @param attributes
         */
        public NodeInfo(
            NodeInfo parent,
            String uri,
            String localName,
            String qName,
            Attributes attributes) {
            this.fParent = parent;
            this.fUri = uri;
            this.fLocalName = localName;
            this.fQName = qName;
            this.fAttributes = attributes;
            this.fPath = parent != null ? parent.fPath + "/" : "";
            this.fPath += qName;
        }

        /**
         * @return the path to this node
         */
        public String getPath() {
            return fPath;
        }

    }

    class XmlNodeHandler extends DefaultHandler {

        private Stack fHandlerStack = new Stack();

        private NodeInfo fRoot;

        /**
         * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String,
         *      java.lang.String, java.lang.String)
         */
        public void endElement(String uri, String localName, String qName)
            throws SAXException {
            NodeHandler handler = (NodeHandler) fHandlerStack.pop();
            if (handler != null) {
                try {
                    handler.endNode(
                        fRoot.fUri,
                        fRoot.fLocalName,
                        fRoot.fQName,
                        fRoot.fAttributes);
                } catch (Exception e) {
                    log.log(Level.SEVERE, "Can not handle end of the tag", e);
                    throw new SAXException(e);
                }
            }
            fRoot = fRoot.fParent;
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
            fRoot = new NodeInfo(fRoot, uri, localName, qName, attributes);
            String path = fRoot.getPath();
            NodeHandler handler = (NodeHandler) fHandlerMap.get(path);
            if (handler == null) {
                for (Iterator iterator = fHandlerMap.entrySet().iterator(); iterator
                    .hasNext();) {
                    Map.Entry entry = (Entry) iterator.next();
                    String mask = (String) entry.getKey();
                    Pattern regexp = Pattern.compile(mask);
                    Matcher matcher = regexp.matcher(path);
                    if (matcher.matches())
                        handler = (NodeHandler) entry.getValue();
                    if (handler != null)
                        break;
                }
            }
            if (handler != null) {
                try {
                    handler.beginNode(
                        fRoot.fUri,
                        fRoot.fLocalName,
                        fRoot.fQName,
                        fRoot.fAttributes);
                } catch (Exception e) {
                    log.log(
                        Level.SEVERE,
                        "Can not handle begining of the tag",
                        e);
                    throw new SAXException(e);
                }
            }
            fHandlerStack.push(handler);
        }
    }

    Map fHandlerMap = new LinkedHashMap();

    /**
     *
     */
    XmlNodeHandler fXmlHandler = new XmlNodeHandler();

    /**
     * This method parse the given input stream as the xml-stream. This method
     * puts this class as the default event handler for the sax-parser.
     * 
     * @param input input stream to parse
     * @throws Exception
     */
    public void parse(InputStream input) throws Exception {
        parse(new InputStreamReader(input, "UTF-8"));
    }

    /**
     * This method parse the given input and uses this class as the default
     * event handler for the sax-parser.
     * 
     * @param reader
     * @throws Exception
     */
    public void parse(Reader reader) throws Exception {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        InputSource source = new InputSource(reader);
        SAXParser saxParser = factory.newSAXParser();
        saxParser.parse(source, fXmlHandler);
    }

    /**
     * This methods loads a resource with the given resourceName and tries to
     * parse it. To load the resource it uses the given class loader.
     * 
     * @param classLoader a class loader which will be used to load a resource
     *        with the given name containing configuration to read.
     * @param resourceName the name of resource to parse
     * @throws Exception
     */
    public void parseResource(ClassLoader classLoader, String resourceName)
        throws Exception {
        InputStream input = classLoader.getResourceAsStream(resourceName);
        parse(input);
    }

    /**
     * This methods loads a resource with the given resourceName and tries to
     * parse it. To load the resource it uses the default class loader (the same
     * as was used to load this class).
     * 
     * @param resourceName the name of resource to parse
     * @throws Exception
     */
    public void parseResource(String resourceName) throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        parseResource(classLoader, resourceName);
    }

    /**
     * @param pathMask
     * @param handler
     */
    public void registerHandler(String pathMask, NodeHandler handler) {
        fHandlerMap.put(pathMask, handler);
    }

}