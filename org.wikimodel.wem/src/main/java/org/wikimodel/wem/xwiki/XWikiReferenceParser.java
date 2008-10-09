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
package org.wikimodel.wem.xwiki;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.wikimodel.wem.WikiReferenceParser;

/**
 * @author kotelnikov
 * @author vmassol
 */
public class XWikiReferenceParser extends WikiReferenceParser {

    /**
     * Note that use Pattern.DOTALL since a link can span multiple lines in XWiki syntax.
     */
    private static final Pattern LINK_PATTERN = Pattern.compile("(?:(.*)(?:>>|\\|\\|))?(.*)", Pattern.DOTALL);
    
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

    @Override
    protected String[] splitToChunks(String str) {
        String[] chunks;
        Matcher matcher = LINK_PATTERN.matcher(str);
        if (matcher.matches()) {
            boolean hasLabel = (matcher.group(1) != null);
            boolean hasReference = (matcher.group(2) != null);
            if (hasLabel && hasReference) {
                chunks = new String[2];
                chunks[0] = matcher.group(1);
                chunks[1] = matcher.group(2);
            } else if (hasReference) {
                chunks = new String[1];
                chunks[0] = matcher.group(2);
            } else {
                chunks = new String[0];
            }
        } else {
            chunks = new String[0];
        }
        return chunks;
    }
}
