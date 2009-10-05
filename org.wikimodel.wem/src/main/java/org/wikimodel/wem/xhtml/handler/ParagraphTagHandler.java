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
 * @author thomas.mortagne
 */
public class ParagraphTagHandler extends TagHandler {

    public ParagraphTagHandler() {
        super(false, true, true);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.wikimodel.wem.xhtml.handler.TagHandler#isBlockHandler(org.wikimodel.wem.xhtml.impl.XhtmlHandler.TagStack.TagContext)
     */
    @Override
    public boolean isBlockHandler(TagContext context) {
        int quoteDepth = (Integer) context.getTagStack().getStackParameter(
            "quoteDepth");

        return quoteDepth == 0;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.wikimodel.wem.xhtml.handler.TagHandler#begin(org.wikimodel.wem.xhtml.impl.XhtmlHandler.TagStack.TagContext)
     */
    @Override
    protected void begin(TagContext context) {
        if (isBlockHandler(context)) {
            sendEmptyLines(context);
            context.getScannerContext().beginParagraph(context.getParams());
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.wikimodel.wem.xhtml.handler.TagHandler#end(org.wikimodel.wem.xhtml.impl.XhtmlHandler.TagStack.TagContext)
     */
    @Override
    protected void end(TagContext context) {
        if (isBlockHandler(context)) {
            context.getScannerContext().endParagraph();
        }
    }
}
