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
import org.wikimodel.wem.WikiParameters;
import org.wikimodel.wem.WikiReference;
import org.wikimodel.wem.xhtml.impl.XhtmlHandler.TagStack.TagContext;

/**
 * @author thomas.mortagne
 */
public class ImgTagHandler extends ReferenceTagHandler {
    @Override
    protected void end(TagContext context) {
	WikiParameter src = context.getParams().getParameter("src");

	if (src != null) {
	    // Remove src and alt parameters for event parameters since it
	    // already consumed as reference and label
	    WikiParameters parameters = context.getParams().remove("src");

	    if (isFreeStandingReference(context)) {
		context.getScannerContext().onImage(src.getValue());
	    } else {
		WikiParameter alt = parameters.getParameter("alt");
		parameters = parameters.remove("alt");

		WikiReference reference = new WikiReference(src.getValue(),
			alt != null ? alt.getValue() : null, parameters);

		context.getScannerContext().onImage(reference);
	    }
	}
    }
}
