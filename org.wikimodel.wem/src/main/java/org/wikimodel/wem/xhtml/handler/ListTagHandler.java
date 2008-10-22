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
public class ListTagHandler extends TagHandler {

	public ListTagHandler() {
		super(false, true, false);
	}

    @Override
    protected void begin(TagContext context) {
        sendEmptyLines(context);
        // We only send a new list event if we're not already inside a list.
        StringBuffer listStyles = (StringBuffer) context.getTagStack().getStackParameter("listStyles");
        if (listStyles.length() == 0) {
            context.getTagStack().setStackParameter("insideBlockElement", true);
            context.getScannerContext().beginList(context.getParams());
        }
    }

    @Override
    protected void end(TagContext context) {
        // We only need to close the list if we're on the last list item. 
    	// Note that we need to close the list explicitely and not wait for the next element to close it
    	// since the next element could be an implicit paragraph.
    	// For example: <html><ul><li>item</li></ul>a</html>
        StringBuffer listStyles = (StringBuffer) context.getTagStack().getStackParameter("listStyles");
        if (listStyles.length() == 0) {
        	context.getScannerContext().endList();
            context.getTagStack().setStackParameter("insideBlockElement", false);
        }
    }

}
