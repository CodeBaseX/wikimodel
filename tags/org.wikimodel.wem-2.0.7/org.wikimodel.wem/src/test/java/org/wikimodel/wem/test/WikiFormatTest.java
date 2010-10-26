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

import java.util.List;

import org.wikimodel.wem.IWemConstants;
import org.wikimodel.wem.WikiFormat;
import org.wikimodel.wem.WikiStyle;

import junit.framework.TestCase;

public class WikiFormatTest extends TestCase {
    public void testOrder() {
        WikiFormat format = new WikiFormat(IWemConstants.STRONG);
        format = format.addStyle(IWemConstants.EM);

        List<WikiStyle> styles = format.getStyles();
        assertEquals(IWemConstants.STRONG, styles.get(0));
        assertEquals(IWemConstants.EM, styles.get(1));

        format = new WikiFormat(IWemConstants.EM);
        format = format.addStyle(IWemConstants.STRONG);

        styles = format.getStyles();
        assertEquals(IWemConstants.EM, styles.get(0));
        assertEquals(IWemConstants.STRONG, styles.get(1));
    }
}
