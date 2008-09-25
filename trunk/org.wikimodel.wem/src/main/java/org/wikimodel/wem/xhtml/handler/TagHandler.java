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
 */
public class TagHandler {

    /**
     * @param context
     * @return <code>true</code> if the current tag represented by the given
     *         context requires a parent document
     */
    private static boolean requiresParentDocument(TagContext context) {
        if (context == null)
            return true;
        if (context.fHandler == null
            || !context.fHandler.requiresDocument())
            return false;
        boolean inContainer = false;
        TagContext parent = context.getParent();
        while (parent != null) {
            if (parent.fHandler != null) {
                inContainer = parent.fHandler.isDocumentContainer();
                break;
            }
            parent = parent.getParent();
        }
        return inContainer;
    }

    private boolean fAccumulateContent;

    /**
     * This flag is <code>true</code> if the current tag can have a text
     * content
     */
    private final boolean fContentContainer;

    /**
     * This flag shows if the current tag can be used as a container for
     * embedded documents.
     */
    private final boolean fDocumentContainer;

    /**
     * This flag shows if the current tag should be created as a direct
     * child of a document.
     */
    private final boolean fRequiresDocument;

    /**
     * @param documentContainer
     * @param requiresDocument
     * @param contentContainer
     */
    public TagHandler(
        boolean documentContainer,
        boolean requiresDocument,
        boolean contentContainer) {
        fDocumentContainer = documentContainer;
        fRequiresDocument = requiresDocument;
        fContentContainer = contentContainer;
    }

    protected void begin(TagContext context) {
    }

    public void beginElement(TagContext context) {
        begin(context);
    }

    protected void end(TagContext context) {
    }

    public final void endElement(TagContext context) {
        end(context);
    }

    public boolean isContentContainer() {
        return fContentContainer;
    }

    public boolean isDocumentContainer() {
        return fDocumentContainer;
    }

    public boolean requiresDocument() {
        return fRequiresDocument;
    }
    
    public void setAccumulateContent(boolean accumulateContent) {
    	fAccumulateContent = accumulateContent;
    }
    
    public boolean isAccumulateContent() {
    	return fAccumulateContent;
    }
    
    /**
     * Check if we need to emit an onEmptyLines() event.
     */
    public static void sendEmptyLines(TagContext context) {
        int lineCount = (Integer) context.getTagStack().getStackParameter("emptyLinesCount"); 
        if (lineCount > 0) {
            context.getScannerContext().onEmptyLines(lineCount);
            context.getTagStack().setStackParameter("emptyLinesCount", 0);
        }
    }
    
}