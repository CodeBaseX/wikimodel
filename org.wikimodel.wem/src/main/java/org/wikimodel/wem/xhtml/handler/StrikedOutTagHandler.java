/*******************************************************************************
 * Copyright (c) 2005,2006 Cognium Systems SA and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Cognium Systems SA - initial API and implementation
 *******************************************************************************/
package org.wikimodel.wem.xhtml.handler;

import org.wikimodel.wem.IWemConstants;

/**
 * @author kotelnikov
 * @author vmassol
 * @author thomass.mortagne
 */
public class StrikedOutTagHandler extends AbstractFormatTagHandler {
    public StrikedOutTagHandler() {
        super(IWemConstants.STRIKE);
    }
}
