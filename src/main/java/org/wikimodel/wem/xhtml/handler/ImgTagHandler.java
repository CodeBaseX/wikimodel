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
        WikiParameters parameters = context.getParams();

        WikiParameter src = parameters.getParameter("src");

        if (src != null) {
            if (isFreeStandingReference(context)) {
                context.getScannerContext().onImage(src.getValue());
            } else {
                WikiParameter alt = parameters.getParameter("alt");

                WikiReference reference = new WikiReference(
                    src.getValue(),
                    alt != null ? alt.getValue() : null,
                    removeMeaningfulParameters(parameters));

                context.getScannerContext().onImage(reference);
            }
        }
    }

    protected WikiParameters removeMeaningfulParameters(WikiParameters parameters) {
        return removeFreestanding(parameters).remove("alt").remove("src");
    }
}
