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

import org.wikimodel.wem.EmptyWemListener;
import org.wikimodel.wem.impl.WikiScannerContext;
import org.wikimodel.wem.xhtml.impl.XhtmlHandler.TagStack.TagContext;

/**
 * @author kotelnikov
 * @author vmassol
 * @author thomas.mortagne
 */
public class PreserveTagHandler extends TagHandler {

    public PreserveTagHandler() {
        super(false, true, true);
    }

    @Override
    protected void begin(TagContext context) {
        // filter content of the <pre> element
        context.getTagStack().pushScannerContext(
            new WikiScannerContext(new PreserverListener()));
        context.getScannerContext().beginDocument();
    }

    @Override
    protected void end(TagContext context) {
        context.getScannerContext().endDocument();
        PreserverListener preserverListener = (PreserverListener) context
                .getTagStack().popScannerContext().getfListener();
        sendEmptyLines(context);

        context.getScannerContext().onVerbatim(preserverListener.toString(),
            false, context.getParams());
    }
}

class PreserverListener extends EmptyWemListener {
    StringBuffer buffer = new StringBuffer();

    @Override
    public String toString() {
        return this.buffer.toString();
    }

    @Override
    public void onWord(String str) {
        this.buffer.append(str);
    }

    @Override
    public void onSpecialSymbol(String str) {
        this.buffer.append(str);
    }

    @Override
    public void onSpace(String str) {
        this.buffer.append(str);
    }

    @Override
    public void onLineBreak() {
        this.buffer.append("\n");
    }

    @Override
    public void onNewLine() {
        this.buffer.append("\n");
    }
}
