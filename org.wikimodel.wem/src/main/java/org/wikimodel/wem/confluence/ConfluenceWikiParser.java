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
package org.wikimodel.wem.confluence;

import java.io.Reader;

import org.wikimodel.wem.IWemListener;
import org.wikimodel.wem.IWikiParser;
import org.wikimodel.wem.WikiParserException;
import org.wikimodel.wem.confluence.javacc.ConfluenceWikiScanner;
import org.wikimodel.wem.confluence.javacc.ParseException;
import org.wikimodel.wem.impl.WikiScannerContext;

/**
 * <pre>
 * http://confluence.atlassian.com/renderer/notationhelp.action?section=all
 * </pre>
 * 
 * @author MikhailKotelnikov
 */
public class ConfluenceWikiParser implements IWikiParser {
    /**
     * Indicate if {noformat} macro should be seen as a macro or a verbatim
     * block.
     */
    private boolean fNoformatAsMacro = true;

    public ConfluenceWikiParser() {
    }

    /**
     * 
     */
    public ConfluenceWikiParser(boolean noformatAsMacro) {
        fNoformatAsMacro = noformatAsMacro;
    }

    /**
     * @see org.wikimodel.wem.IWikiParser#parse(java.io.Reader,
     *      org.wikimodel.wem.IWemListener)
     */
    public void parse(Reader reader, IWemListener listener) throws WikiParserException {
        try {
            ConfluenceWikiScanner scanner = new ConfluenceWikiScanner(reader);
            scanner.setNoformatAsMacro(fNoformatAsMacro);
            ConfluenceWikiScannerContext context = new ConfluenceWikiScannerContext(listener);
            scanner.parse(context);
        } catch (ParseException e) {
            throw new WikiParserException(e);
        }
    }
}
