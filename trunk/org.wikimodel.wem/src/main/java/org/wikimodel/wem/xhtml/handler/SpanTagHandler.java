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

import org.wikimodel.wem.WikiParameters;
import org.wikimodel.wem.xhtml.impl.XhtmlHandler.TagStack.TagContext;

/**
 * @author vmassol
 * @author thomass.mortagne
 */
public class SpanTagHandler extends TagHandler {
    public static final String SPANPARAMETERS = "spanParameters";
    
    public SpanTagHandler() {
        super(false, false, true);
    }

    @Override
    protected void begin(TagContext context) {
        WikiParameters spanParameters = (WikiParameters)context.getTagStack().getStackParameter(SPANPARAMETERS);
        if (spanParameters != null) {
            spanParameters = spanParameters.addParameters(context.getParams());
        } else {
            spanParameters = new WikiParameters(context.getParams());
        }
        context.getTagStack().pushStackParameter(SPANPARAMETERS, spanParameters);
        
        if (context.getParams().getSize() > 0) {
            context.getScannerContext().beginFormat(spanParameters);
        }
    }

    @Override
    protected void end(TagContext context) {
        if (context.getParams().getSize() > 0) {
            context.getScannerContext().endFormat(WikiParameters.EMPTY);
        }
        context.getTagStack().popStackParameter(SPANPARAMETERS);
    }

}
