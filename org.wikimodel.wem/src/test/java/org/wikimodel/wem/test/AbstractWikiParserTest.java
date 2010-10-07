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
package org.wikimodel.wem.test;

import java.io.StringReader;

import junit.framework.TestCase;

import org.wikimodel.wem.IWemListener;
import org.wikimodel.wem.IWikiParser;
import org.wikimodel.wem.IWikiPrinter;
import org.wikimodel.wem.WikiParameters;
import org.wikimodel.wem.WikiParserException;
import org.wikimodel.wem.xhtml.PrintListener;

/**
 * @author MikhailKotelnikov
 */
public abstract class AbstractWikiParserTest extends TestCase {

    private boolean fOutputEnabled;

    private boolean fShowSections;

    private boolean supportImage;

    private boolean supportDownload;

    /**
     * @param name
     */
    public AbstractWikiParserTest(String name) {
        super(name);
    }
    
    public AbstractWikiParserTest(String name, boolean supportImage, boolean supportDownload) {
        super(name);
        
        this.supportImage = supportImage;
        this.supportDownload = supportDownload;
    }

    /**
     * @param control
     * @param test
     */
    protected void checkResults(String control, String test) {
        if (control != null) {
            control = "<div class='wikimodel-document'>\n" + control
                    + "\n</div>\n";
            assertEquals(control, test);
        }
    }

    protected void enableOutput(boolean enable) {
        fOutputEnabled = enable;
    }

    /**
     * @param buf
     * @return
     */
    protected IWemListener newParserListener(final StringBuffer buf) {
        IWikiPrinter printer = newPrinter(buf);
        IWemListener listener;
        if (!fShowSections) {
            listener = new PrintListener(printer, this.supportImage, this.supportDownload);
        } else {
            listener = new PrintListener(printer, this.supportImage, this.supportDownload) {
                @Override
                public void beginSection(int docLevel, int headerLevel,
                        WikiParameters params) {
                    println("<section-" + docLevel + "-" + headerLevel + params
                            + ">");
                }

                @Override
                public void beginSectionContent(int docLevel, int headerLevel,
                        WikiParameters params) {
                    println("<sectionContent-" + docLevel + "-" + headerLevel
                            + params + ">");
                }

                @Override
                public void endSection(int docLevel, int headerLevel,
                        WikiParameters params) {
                    println("</section-" + docLevel + "-" + headerLevel + ">");
                }

                @Override
                public void endSectionContent(int docLevel, int headerLevel,
                        WikiParameters params) {
                    println("</sectionContent-" + docLevel + "-" + headerLevel
                            + ">");
                }
            };
        }
        return listener;
    }

    /**
     * @param buf
     * @return
     */
    protected IWikiPrinter newPrinter(final StringBuffer buf) {
        IWikiPrinter printer = new IWikiPrinter() {

            public void print(String str) {
                buf.append(str);
            }

            public void println(String str) {
                buf.append(str);
                buf.append("\n");
            }
        };
        return printer;
    }

    protected abstract IWikiParser newWikiParser();

    protected void println(String str) {
        if (fOutputEnabled)
            System.out.println(str);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        enableOutput(true);
    }

    protected void showSections(boolean b) {
        fShowSections = b;
    }

    /**
     * @param string
     * @throws WikiParserException
     */
    protected void test(String string) throws WikiParserException {
        test(string, null);
    }

    /**
     * @param string
     * @throws WikiParserException
     */
    protected void test(String string, String control)
            throws WikiParserException {
        println("==================================================");
        StringReader reader = new StringReader(string);
        IWikiParser parser = newWikiParser();
        final StringBuffer buf = new StringBuffer();
        IWemListener listener = newParserListener(buf);
        parser.parse(reader, listener);
        String test = buf.toString();
        println(test);
        checkResults(control, test);
    }

}
