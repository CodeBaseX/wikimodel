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
public class TableRowTagHandler extends TagHandler {

	public TableRowTagHandler() {
		super(false, false, false);
	}

    @Override
    protected void begin(TagContext context) {
        context.getScannerContext().beginTableRow(context.getParams());
    }

    @Override
    protected void end(TagContext context) {
        context.getScannerContext().endTableRow();
    }
	
}
