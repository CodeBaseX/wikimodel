/**
 * 
 */
package org.wikimodel.wem.examples;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.wikimodel.wem.IWikiParser;
import org.wikimodel.wem.confluence.ConfluenceExtendedWikiParser;
import org.wikimodel.wem.xml.XmlUtil;
import org.wikimodel.wem.xml.sax.WemReader;

/**
 * @author kotelnikov
 */
public class DocBookGenerator {

    private static class Transformation {

        private Document fXslDocument;

        public final File outputFile;

        public final URL xslUrl;

        public Transformation(String xslUrl, String outputFile)
            throws MalformedURLException {
            this(new URL(xslUrl), outputFile != null
                ? new File(outputFile)
                : null);
        }

        public Transformation(URL xslUrl, File outputFile) {
            this.xslUrl = xslUrl;
            this.outputFile = outputFile;
        }

        /**
         * @param xslUrl
         * @return
         * @throws IOException
         * @throws Exception
         */
        private Document getXsl() throws IOException, Exception {
            if (fXslDocument == null) {
                InputStream xslInput = xslUrl.openStream();
                try {
                    fXslDocument = XmlUtil.readXML(xslInput);
                } finally {
                    xslInput.close();
                }
            }
            return fXslDocument;
        }

        /**
         * @param document
         * @param xslDocument
         * @return
         * @throws TransformerFactoryConfigurationError
         * @throws Exception
         * @throws IOException
         */
        public Document run(Document document) throws Exception {
            Document xslDocument = getXsl();
            DOMSource xslSource = new DOMSource(xslDocument);
            DOMSource xmlSource = new DOMSource(document);
            DOMResult result = new DOMResult();
            XmlUtil.formatXML(xmlSource, xslSource, null, result);
            Document doc = (Document) result.getNode();
            writeDocument(doc.getDocumentElement(), outputFile);
            return doc;
        }
    }

    private static String getParam(String[] args, int i, String defaultValue) {
        String str = (i >= 0 && i < args.length) ? args[i] : null;
        return (str != null) ? str : defaultValue;
    }

    private static String getResourceUrl(String fileName)
        throws MalformedURLException {
        return DocBookGenerator.class.getResource(fileName).toString();
    }

    public static Transformation[] getTransformations(int pos, String... args)
        throws MalformedURLException {
        List<Transformation> result = new ArrayList<Transformation>();
        for (int i = 0; i + pos < args.length; i++) {
            String xsl = args[i];
            i++;
            String outFile = (i + pos < args.length) ? args[i] : null;
            result.add(new Transformation(xsl, outFile));
        }
        return result.toArray(new Transformation[result.size()]);
    }

    public static void main(String... args) throws Exception {
        DocBookGenerator generator = new DocBookGenerator();
        String wiki = getParam(args, 0, getResourceUrl("/ConfluenceMarkup.txt"));
        String xml = getParam(args, 1, "./tmp/ConfluenceMarkup.wem.xml");
        Transformation[] transformations;
        if (args.length < 4) {
            transformations = getTransformations(
                0,
                getResourceUrl("/WemToDocbook.xsl"),
                "./tmp/ConfluenceMarkup.dockbook.xml");
        } else {
            transformations = getTransformations(2, args);
        }
        URL wikiUrl = new URL(wiki);
        File xmlFile = new File(xml);
        generator.generate(wikiUrl, xmlFile, transformations);
    }

    /**
     * @param element
     * @param file
     * @throws FileNotFoundException
     * @throws Exception
     * @throws IOException
     */
    private static void writeDocument(Element element, File file)
        throws FileNotFoundException,
        Exception,
        IOException {
        if (file == null)
            return;
        file = file.getCanonicalFile();
        file.getParentFile().mkdirs();
        FileOutputStream out = new FileOutputStream(file);
        try {
            XmlUtil.write(element, out);
        } finally {
            out.close();
        }
    }

    /**
     * 
     */
    public DocBookGenerator() {
    }

    /**
     * @param sourceUrl
     * @param xmlFile
     * @param transformations
     * @throws Exception
     */
    private void generate(
        URL sourceUrl,
        File xmlFile,
        Transformation... transformations) throws Exception {
        Document document = parseWikiDocument(sourceUrl);
        writeDocument(document.getDocumentElement(), xmlFile);
        for (Transformation transformation : transformations) {
            document = transformation.run(document);
        }
    }

    protected IWikiParser newWikiParser() {
        return new ConfluenceExtendedWikiParser();
        // return new ConfluenceWikiParser();
        // return new CommonWikiParser();
    }

    /**
     * Parses the given input stream with a wiki document and transforms it into
     * a XML DOM.
     * 
     * @param sourceUrl
     * @return
     * @throws IOException
     * @throws ParserConfigurationException
     * @throws TransformerConfigurationException
     * @throws TransformerFactoryConfigurationError
     * @throws TransformerException
     */
    private Document parseWikiDocument(URL sourceUrl)
        throws IOException,
        ParserConfigurationException,
        TransformerConfigurationException,
        TransformerFactoryConfigurationError,
        TransformerException {
        InputStream input = sourceUrl.openStream();
        try {
            Reader reader = new InputStreamReader(input);
            IWikiParser parser = newWikiParser();
            Document document = XmlUtil.newDocument();
            DOMResult result = new DOMResult(document);
            WemReader xmlReader = new WemReader(parser);
            XmlUtil.write(reader, xmlReader, result);
            return document;
        } finally {
            input.close();
        }
    }

}
