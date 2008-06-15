/*******************************************************************************
 * Copyright (c) 2005,2008 Cognium Systems SA and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Contributors:
 *     Cognium Systems SA - initial API and implementation
 *******************************************************************************/
package org.wikimodel.wem.jspwiki;

import org.wikimodel.wem.WikiReferenceParser;

/**
 * @author kotelnikov
 */
public class JspWikiReferenceParser extends WikiReferenceParser {

    @Override
    protected String getLabel(String[] chunks) {
        if (chunks.length > 1)
            return chunks[0];
        return null;
    }

    @Override
    protected String getLink(String[] chunks) {
        if (chunks.length > 1)
            return chunks[1];
        return chunks[0];
    }

}