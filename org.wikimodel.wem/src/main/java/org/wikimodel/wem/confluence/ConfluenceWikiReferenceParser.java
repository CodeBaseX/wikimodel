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
package org.wikimodel.wem.confluence;

import org.wikimodel.wem.IWikiReferenceParser;
import org.wikimodel.wem.WikiParameters;
import org.wikimodel.wem.WikiReference;

/**
 * @see WikiReference
 * @author kotelnikov
 */
public class ConfluenceWikiReferenceParser implements IWikiReferenceParser {

    public WikiReference parse(String str) {
        str = str.trim();
        String[] array = str.split("[|]");
        String link;
        String label;
        String tip;
        if (array.length == 1) {
            link = str;
            label = null;
            tip = null;
        } else {
            label = array[0].trim();
            link = array[1].trim();
            tip = (array.length > 2) ? array[2].trim() : null;
        }
        WikiParameters params = WikiParameters.EMPTY;
        if (tip != null)
            params = params.addParameter("title", tip);
        WikiReference ref = new WikiReference(link, label, params);
        return ref;
    }

}