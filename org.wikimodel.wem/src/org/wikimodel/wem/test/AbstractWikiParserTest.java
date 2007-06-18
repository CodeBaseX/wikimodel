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
import org.wikimodel.wem.PrintListener;
import org.wikimodel.wem.WikiParserException;
import org.wikimodel.wem.creole.javacc.ParseException;

/**
 * @author MikhailKotelnikov
 */
public abstract class AbstractWikiParserTest extends TestCase {

    /**
     * @param name
     */
    public AbstractWikiParserTest(String name) {
        super(name);
    }

    protected abstract IWikiParser newWikiParser();

    /**
     * @param string
     * @throws ParseException
     * @throws WikiParserException
     */
    protected void test(String string) throws WikiParserException {
        System.out
            .println("==================================================");
        StringReader reader = new StringReader(string);
        IWikiParser parser = newWikiParser();
        IWemListener listener = new PrintListener() {
            @Override
            protected void print(String str) {
                super.print(str);
            }

            @Override
            protected void println(String string) {
                super.println(string);
            }
        };
        parser.parse(reader, listener);
    }
}
