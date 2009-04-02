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

import java.util.Stack;

import org.wikimodel.wem.xhtml.impl.XhtmlHandler.TagStack;
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

        Stack<Boolean> insideBlockElementsStack = 
            (Stack<Boolean>) context.getTagStack().getStackParameter("insideBlockElement");

        if (isBlockHandler(context)) {

            // If we're starting a block tag and we're in inline mode (ie inside a block element) then start a nested document 
            // and save the parent tag, see endElement().
            if (!insideBlockElementsStack.isEmpty() && insideBlockElementsStack.peek()) {
                context.getScannerContext().beginDocument();
                insideBlockElementsStack.push(false);
                Stack<String> tagNames = (Stack<String>) context.getTagStack().getStackParameter("tagNameBeforeNestedDocument");
                tagNames.push(context.getParent().getName());
            }
            
            insideBlockElementsStack.push(true);
        }
        
        begin(context);
    }

    protected void end(TagContext context) {
    }

    public final void endElement(TagContext context) {
        end(context);

        Stack<Boolean> insideBlockElementsStack = 
            (Stack<Boolean>) context.getTagStack().getStackParameter("insideBlockElement");

        if (isBlockHandler(context)) {
            insideBlockElementsStack.pop();
        }

        // Verify if we need to close a nested document that would have been opened.
        // To verify this we check the current tag being closed and verify if it's
        // the one saved when the nested document was opened.
        Stack<String> tagNames = (Stack<String>) context.getTagStack().getStackParameter("tagNameBeforeNestedDocument");
        if (!tagNames.isEmpty() && context.getName().equals(tagNames.peek())) {
            context.getScannerContext().endDocument();
            tagNames.pop();
            insideBlockElementsStack.pop();
        }
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
    
    public void initialize(TagStack stack) {
        // Nothing to do by default. Override in children classes if need be. 
    }
    
    /**
     * @return true if the current handler handles block tags (paragraphs, lists, tables, headers, etc) 
     */
    public boolean isBlockHandler(TagContext context) {
        return false;
    }
}