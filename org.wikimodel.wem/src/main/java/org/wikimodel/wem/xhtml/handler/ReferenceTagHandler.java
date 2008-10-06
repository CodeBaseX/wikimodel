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

import org.wikimodel.wem.WikiParameter;
import org.wikimodel.wem.WikiReference;
import org.wikimodel.wem.xhtml.impl.XhtmlHandler.TagStack.TagContext;

/**
 * @author kotelnikov
 * @author vmassol
 */
public class ReferenceTagHandler extends TagHandler {

	public ReferenceTagHandler() {
		super(false, false, true);
	}

    {
        setAccumulateContent(true);
    }

    @Override
    protected void begin(TagContext context) {
    }

    @Override
    protected void end(TagContext context) {
        // TODO: it should be replaced by a normal parameters
        WikiParameter ref = context.getParams().getParameter("href");
        // Check if there's a class attribute with a "wikimodel-freestanding" value.
        // If so it means we have a free standing link.
        WikiParameter classParam = context.getParams().getParameter("class");
        if (ref != null) {
            if ((classParam != null) && classParam.getValue().equalsIgnoreCase("wikimodel-freestanding")) {
                context.getScannerContext().onReference(ref.getValue());
            } else {
                String content = context.getContent();
                WikiReference reference = new WikiReference(
                    ref.getValue(),
                    content);
                context.getScannerContext().onReference(reference);
            }
        }
    }
}
