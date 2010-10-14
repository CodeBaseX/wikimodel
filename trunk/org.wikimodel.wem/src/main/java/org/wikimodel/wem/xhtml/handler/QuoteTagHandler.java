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
 * @author vmassol
 * @author thomass.mortagne
 */
public class QuoteTagHandler extends TagHandler {
    public static final String QUOTEDEPTH = "quoteDepth";

    public QuoteTagHandler() {
        super(false, false, true);
    }

    @Override
    public boolean isBlockHandler(TagContext context) {
        // A new blockquote is considered a block element only if the parent is
        // not a blockquote item since blockquotes
        // are not new block elements
        return !(context.getParent().getName().equals("blockquote"));
    }

    @Override
    protected void begin(TagContext context) {
        int quoteDepth = (Integer) context.getTagStack().getStackParameter(
            QUOTEDEPTH);
        if (quoteDepth == 0) {
            context.getScannerContext().beginQuot(context.getParams());
        }
        quoteDepth++;
        context.getScannerContext().beginQuotLine(quoteDepth);
        context.getTagStack().setStackParameter(QUOTEDEPTH, quoteDepth);
    }

    @Override
    protected void end(TagContext context) {
        int quoteDepth = (Integer) context.getTagStack().getStackParameter(
            QUOTEDEPTH);
        quoteDepth--;
        if (quoteDepth < 0) {
            quoteDepth = 0;
        }
        context.getScannerContext().endQuotLine();
        if (quoteDepth == 0) {
            context.getScannerContext().endQuot();
        }
        context.getTagStack().setStackParameter(QUOTEDEPTH, quoteDepth);
    }
}
