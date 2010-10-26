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
public class ListItemTagHandler extends TagHandler {

    public ListItemTagHandler() {
        super(true, false, true);
    }

    public ListItemTagHandler(
        boolean documentContainer,
        boolean requiresDocument,
        boolean contentContainer) {
        super(documentContainer, requiresDocument, contentContainer);
    }

    @Override
    public void begin(TagContext context) {
        String markup = context.getParent().getName().equals("ol") ? "#" : "*";
        begin(markup, context);
    }

    protected void begin(String markup, TagContext context) {
        StringBuffer listStyles = (StringBuffer) context
            .getTagStack()
            .getStackParameter("listStyles");
        listStyles.append(markup);
        context.getScannerContext().beginListItem(listStyles.toString());
    }

    @Override
    public void end(TagContext context) {
        StringBuffer listStyles = (StringBuffer) context
            .getTagStack()
            .getStackParameter("listStyles");
        // We should always have a length greater than 0 but we handle
        // the case where the user has entered some badly formed HTML
        if (listStyles.length() > 0) {
            listStyles.setLength(listStyles.length() - 1);
        }
        // Note: Do not generate an endListItem() event since it'll be generated
        // automatically by the next element.
    }
}
