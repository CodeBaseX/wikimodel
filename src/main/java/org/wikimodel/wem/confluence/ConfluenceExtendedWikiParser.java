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
import java.io.StringReader;
import java.util.Stack;

import org.wikimodel.wem.CompositeListener;
import org.wikimodel.wem.IWemListener;
import org.wikimodel.wem.WikiParameters;
import org.wikimodel.wem.WikiParserException;

/**
 * <pre>
 * http://confluence.atlassian.com/renderer/notationhelp.action?section=all
 * </pre>
 * 
 * @author MikhailKotelnikov
 */
public class ConfluenceExtendedWikiParser extends ConfluenceWikiParser {

    public static class EnhancedListener extends CompositeListener {

        private Stack<Boolean> fSkipDocument = new Stack<Boolean>();

        public EnhancedListener(IWemListener... listeners) {
            super(listeners);
        }

        @Override
        public void beginDocument(WikiParameters params) {
            if (!skipDocument()) {
                super.beginDocument(params);
            }
        }

        @Override
        public void beginSection(
            int docLevel,
            int headerLevel,
            WikiParameters params) {
            if (!skipDocument()) {
                super.beginSection(docLevel, headerLevel, params);
            }
        }

        @Override
        public void beginSectionContent(
            int docLevel,
            int headerLevel,
            WikiParameters params) {
            if (!skipDocument()) {
                super.beginSectionContent(docLevel, headerLevel, params);
            }
            push(false);
        }

        @Override
        public void endDocument(WikiParameters params) {
            if (!skipDocument()) {
                super.endDocument(params);
            }
        }

        @Override
        public void endSection(
            int docLevel,
            int headerLevel,
            WikiParameters params) {
            if (!skipDocument()) {
                super.endSection(docLevel, headerLevel, params);
            }
        }

        @Override
        public void endSectionContent(
            int docLevel,
            int headerLevel,
            WikiParameters params) {
            pop();
            if (!skipDocument()) {
                super.endSectionContent(docLevel, headerLevel, params);
            }
        }

        @Override
        public void onMacroBlock(
            String macroName,
            WikiParameters params,
            String content) {
            String type = null;
            if ("note".equals(macroName)) {
                type = "N";
            } else if ("tip".equals(macroName)) {
                type = "T";
            }
            if (type != null) {
                beginInfoBlock(type, params);
                parseContent(content);
                endInfoBlock(type, params);
            } else {
                super.onMacroBlock(macroName, params, content);
            }
        }

        /**
         * @param content
         * @throws WikiParserException
         */
        private void parseContent(String content) {
            try {
                push(true);
                StringReader reader = new StringReader(content);
                ConfluenceWikiParser parser = new ConfluenceWikiParser();
                parser.parse(reader, this);
                pop();
            } catch (WikiParserException e) {
                throw new RuntimeException(e);
            }
        }

        private void pop() {
            fSkipDocument.pop();
        }

        private void push(boolean b) {
            fSkipDocument.push(b);
        }

        private boolean skipDocument() {
            if (fSkipDocument.isEmpty())
                return false;
            Boolean peek = fSkipDocument.peek();
            return peek;
        }

    }

    /**
     * 
     */
    public ConfluenceExtendedWikiParser() {
        super();
    }

    /**
     * @see org.wikimodel.wem.IWikiParser#parse(java.io.Reader,
     *      org.wikimodel.wem.IWemListener)
     */
    @Override
    public void parse(Reader reader, IWemListener listener)
        throws WikiParserException {
        IWemListener composite = new EnhancedListener(listener);
        super.parse(reader, composite);
    }
}
