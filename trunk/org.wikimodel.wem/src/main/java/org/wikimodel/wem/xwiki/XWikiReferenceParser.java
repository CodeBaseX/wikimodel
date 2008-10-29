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

import org.wikimodel.wem.WikiReferenceParser;

/**
 * @author kotelnikov
 * @author vmassol
 */
public class XWikiReferenceParser extends WikiReferenceParser {

    @Override
    protected String getLabel(String[] chunks) {
        return chunks[0];
    }

    @Override
    protected String getLink(String[] chunks) {
        return chunks[1];
    }

    @Override
    protected String getParameters(String[] chunks)
    {
        return chunks[2];
    }

    @Override
    protected String[] splitToChunks(String str) {
        String[] chunks = new String[3];

        // Note: It's important to use lastIndexOf() since it's possible to 
        // have ">" characters in the label part. 
        int labelSeparatorPosition = str.lastIndexOf(">>");
        if (labelSeparatorPosition > -1) {
            chunks[0] = str.substring(0, labelSeparatorPosition);
            String buffer = str.substring(labelSeparatorPosition + 2);
            int referenceSeparatorPosition = buffer.indexOf("||");
            if (referenceSeparatorPosition > -1) {
                chunks[1] = buffer.substring(0, referenceSeparatorPosition);
                chunks[2] = buffer.substring(referenceSeparatorPosition + 2);                
            } else {
                chunks[1] = buffer;
            }
        } else {
            // Same as above we want to allow the link to use the | character
            int referenceSeparatorPosition = str.lastIndexOf("||");
            if (referenceSeparatorPosition > -1) {
                chunks[1] = str.substring(0, referenceSeparatorPosition);
                chunks[2] = str.substring(referenceSeparatorPosition + 2);
            } else {
                chunks[1] = str;
            }
        }
        
        return chunks;
    }
}
