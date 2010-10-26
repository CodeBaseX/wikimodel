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

import java.util.Arrays;

import org.wikimodel.wem.IWemConstants;
import org.wikimodel.wem.WikiParameter;
import org.wikimodel.wem.impl.WikiScannerContext;
import org.wikimodel.wem.xhtml.impl.XhtmlHandler.TagStack.TagContext;

/**
 * @author kotelnikov
 * @author vmassol
 * @author thomas.mortagne
 */
public class TeletypeTagHandler extends AbstractFormatTagHandler {
    // There are 2 possible output for <tt>:
    // * If there a class="wikimodel-verbatim" specified then we emit a
    // onVerbatimInline() event
    // * If there no class or a class with another value then we emit a
    // Monospace Format event.

    public TeletypeTagHandler() {
        super(IWemConstants.MONO);
    }

    @Override
    protected void begin(TagContext context) {
        WikiParameter param = context.getParams().getParameter("class");
        if ((param != null)
                && Arrays.asList(param.getValue().split(" ")).contains(
                    "wikimodel-verbatim")) {
            beginVerbatim(context);
        } else {
            super.begin(context);
        }
    }

    @Override
    protected void end(TagContext context) {
        WikiParameter param = context.getParams().getParameter("class");
        if ((param != null)
                && Arrays.asList(param.getValue().split(" ")).contains(
                    "wikimodel-verbatim")) {
            endVerbatim(context);
        } else {
            super.end(context);
        }
    }

    private void beginVerbatim(TagContext context) {
        // filter content of the <tt> element
        context.getTagStack().pushScannerContext(
            new WikiScannerContext(new PreserverListener()));
        context.getScannerContext().beginDocument();
    }

    private void endVerbatim(TagContext context) {
        context.getScannerContext().endDocument();
        PreserverListener preserverListener = (PreserverListener) context
                .getTagStack().popScannerContext().getfListener();

        context.getScannerContext().onVerbatim(preserverListener.toString(),
            true);
    }
}
