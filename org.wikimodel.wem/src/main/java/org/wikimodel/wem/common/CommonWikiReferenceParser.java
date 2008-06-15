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
package org.wikimodel.wem.common;

import org.wikimodel.wem.IWikiReferenceParser;
import org.wikimodel.wem.WikiParameters;
import org.wikimodel.wem.WikiReference;

/**
 * @author kotelnikov
 */
public class CommonWikiReferenceParser implements IWikiReferenceParser {

    public WikiReference parse(String str) {
        str = str.trim();
        // Params
        int idx = str.indexOf('>');
        if (idx < 0)
            idx = str.indexOf('|');
        String params = null;
        if (idx >= 0) {
            params = str.substring(idx + 1);
            str = str.substring(0, idx);
        }

        // Label
        idx = str.indexOf(' ');
        String label = null;
        if (idx > 0) {
            label = str.substring(idx + 1);
            str = str.substring(0, idx);
        }
        WikiParameters parameters = new WikiParameters(params);
        return new WikiReference(str, label, parameters);
    }
}