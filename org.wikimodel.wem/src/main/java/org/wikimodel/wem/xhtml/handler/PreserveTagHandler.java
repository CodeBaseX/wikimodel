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

import org.wikimodel.wem.xhtml.impl.XhtmlHandler.TagStack.TagContext;

/**
 * @author kotelnikov
 * @author vmassol
 */
public class PreserveTagHandler extends TagHandler {

	public PreserveTagHandler() {
		super(false, true, true);
	}
	
    {
        setAccumulateContent(true);
    }

    @Override
    protected void end(TagContext context) {
        String str = context.getContent();
        sendEmptyLines(context);
        context.getScannerContext().onVerbatim(str, false);
    }

}
