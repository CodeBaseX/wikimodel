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
package org.wikimodel.wem.xwiki.xwiki20;

import org.wikimodel.wem.WikiParameters;

/**
 * @author thomas.mortagne
 */
public class XWikiWikiParameters extends WikiParameters {
    public static WikiParameters newWikiParameters(String str) {
        return WikiParameters.newWikiParameters(str,
            XWikiScannerUtil.ESCAPECHAR);
    }

    public XWikiWikiParameters(String str) {
        super(str, XWikiScannerUtil.ESCAPECHAR);
    }
}
