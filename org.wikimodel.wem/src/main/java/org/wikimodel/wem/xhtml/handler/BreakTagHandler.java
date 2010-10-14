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
 * @author kotelnikov
 * @author vmassol
 * @author thomass.mortagne
 */
public class BreakTagHandler extends TagHandler {

    public BreakTagHandler() {
        super(false, false, false);
    }

    @Override
    protected void begin(TagContext context) {
        // If we're inside a quotation line then a BR must close the current
        // quotation line and start a new quotation line.
        int quoteDepth = (Integer) context.getTagStack().getStackParameter(
            QuoteTagHandler.QUOTEDEPTH);
        if (quoteDepth > 0) {
            context.getScannerContext().beginQuotLine(quoteDepth);

            // duplicate span on several quotation lines
            WikiParameters spanParameters;
            for (int i = 0; (spanParameters = (WikiParameters) context
                    .getTagStack().getStackParameter(
                        SpanTagHandler.SPANPARAMETERS, i)) != null; ++i) {
                context.getScannerContext().beginFormat(spanParameters);
            }
        } else {
            context.getScannerContext().onNewLine();
        }
    }
}
