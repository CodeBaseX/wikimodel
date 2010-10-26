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

import org.wikimodel.wem.IWikiParser;
import org.wikimodel.wem.xwiki.xwiki21.XWikiParser;

/**
 * @author MikhailKotelnikov
 * @author thomas.mortagne
 */
public class XWiki21ParserTest extends XWiki20ParserTest {

    /**
     * @param name
     */
    public XWiki21ParserTest(String name) {
        super(name);
    }

    @Override
    protected IWikiParser newWikiParser() {
        return new XWikiParser();
    }
}
