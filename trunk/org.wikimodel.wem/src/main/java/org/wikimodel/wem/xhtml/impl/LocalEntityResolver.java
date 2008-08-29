/*******************************************************************************
 * Copyright (c) 2003-2008 XWiki, under the LGPL 2.1 license. All Rights
 * were granted by the XWiki committers to the WikiModel project to reuse this
 * class.
 *******************************************************************************/
package org.wikimodel.wem.xhtml.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Entity resolver that resolves entities using entity files (like
 * xhtml-symbol.ent, xhtml-special.ent, xhtml-lat1.ent) located on the file
 * system (in the classpath). This allows an XML parser that uses this entity
 * resolver to work even when there's no internet connection. It also speeds up
 * the entity resolution.
 * 
 * @version $Id$
 */
public class LocalEntityResolver implements EntityResolver {

    private final static Logger log = Logger
        .getLogger(LocalEntityResolver.class.getName());

    /**
     * Allow the application to resolve external entities. <p/>
     * <p>
     * The Parser will call this method before opening any external entity
     * except the top-level document entity including the external DTD subset,
     * external entities referenced within the DTD, and external entities
     * referenced within the document element): the application may request that
     * the parser resolve the entity itself, that it use an alternative URI, or
     * that it use an entirely different input source.
     * </p>
     * <p/>
     * <p>
     * Application writers can use this method to redirect external system
     * identifiers to secure and/or local URIs, to look up public identifiers in
     * a catalogue, or to read an entity from a database or other input source
     * (including, for example, a dialog box).
     * </p>
     * <p/>
     * <p>
     * If the system identifier is a URL, the SAX parser must resolve it fully
     * before reporting it to the application.
     * </p>
     * 
     * @param publicId The public identifier of the external entity being
     *        referenced, or null if none was supplied.
     * @param systemId The system identifier of the external entity being
     *        referenced.
     * @return An InputSource object describing the new input source, or null to
     *         request that the parser open a regular URI connection to the
     *         system identifier.
     * @throws org.xml.sax.SAXException Any SAX exception, possibly wrapping
     *         another exception.
     * @throws java.io.IOException A Java-specific IO exception, possibly the
     *         result of creating a new InputStream or Reader for the
     *         InputSource.
     * @see org.xml.sax.InputSource
     */
    public InputSource resolveEntity(String publicId, String systemId)
        throws SAXException,
        IOException {
        InputSource source = null;

        try {
            URI uri = new URI(systemId);

            if ("http".equals(uri.getScheme())
                || "file".equals(uri.getScheme())) {
                String filename = (new File(uri.getPath())).getName();
                InputStream istream = getClass()
                    .getClassLoader()
                    .getResourceAsStream(filename);
                if (istream != null) {
                    source = new InputSource(istream);
                } else {
                    log.warning(String.format(
                        "Failed to load resource [%s] locally. "
                            + "Will try to get it online at [%s]",
                        filename,
                        systemId));
                }
            } else {
                // As there's no scheme we'll assume that it's an already
                // resolved systemId that is
                // passed. This happens when a DTD file uses a relative systemId
                // for dependent
                // entity files. For example the default xhtml1-strict.dtd and
                // xhtml1-transitional.dtd files reference xhtml-lat1.ent,
                // xhtml-special.ent and
                // xhtml1-symbol.ent relatively. Normally these relative
                // declarations generate a
                // URL with a "file" scheme but apparently there are some cases
                // when the raw
                // entity file names is passed to this resolveEntity method...
                log
                    .fine(String
                        .format(
                            "Unknown URI scheme [%s] for entity [%s]. "
                                + "Assuming the entity is already resolved and looking for it in the file system.",
                            uri.getScheme(),
                            systemId));
                InputStream istream = getClass()
                    .getClassLoader()
                    .getResourceAsStream(systemId);
                if (istream != null) {
                    source = new InputSource(istream);
                } else {
                    log.fine("Failed to load resource ["
                        + systemId
                        + "] locally.");
                }
            }
        } catch (URISyntaxException e) {
            log.log(
                Level.WARNING,
                String.format("Invalid URI [%s]", systemId),
                e);
        }
        // Returning null causes the caller to try accessing the entity online
        return source;
    }
}
