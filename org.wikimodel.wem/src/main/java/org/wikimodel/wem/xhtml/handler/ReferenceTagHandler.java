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
 * Handles both A and IMG tags (since WikiModel handles images as references).
 * 
 * @author kotelnikov
 * @author vmassol
 */
public class ReferenceTagHandler extends TagHandler {

	public ReferenceTagHandler() {
		super(false, false, true);
	}
	
    @Override
    protected void begin(TagContext context) {
        setAccumulateContent(true);
    }

    @Override
    protected void end(TagContext context) {
        // TODO: it should be replaced by a normal parameters
        WikiParameter ref = null;
        if (context.getLocalName().equals("img")) {
            ref = context.getParams().getParameter("src");
        } else {
            ref = context.getParams().getParameter("href");
        }

        if (ref != null) {
            // Check if there's a class attribute with a "wikimodel-freestanding" value.
            // If so it means we have a free standing link.
            if (isFreeStandingReference(context)) {
                context.getScannerContext().onReference(ref.getValue());
            } else {
                String content = context.getContent();
                WikiReference reference = new WikiReference(ref.getValue(),
                    content, context.getParams());
                context.getScannerContext().onReference(reference);
            }
        }
    }
    
    protected boolean isFreeStandingReference(TagContext context)
    {
        // Check if there's a class attribute with a "wikimodel-freestanding" value.
        // If so it means we have a free standing link.
        WikiParameter classParam = context.getParams().getParameter("class");
        return ((classParam != null) && classParam.getValue().equalsIgnoreCase("wikimodel-freestanding"));
    }
}
