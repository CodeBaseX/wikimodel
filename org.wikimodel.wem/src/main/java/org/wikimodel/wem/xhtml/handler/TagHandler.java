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

import org.wikimodel.wem.WikiParameters;
import org.wikimodel.wem.xhtml.impl.XhtmlHandler;
import org.wikimodel.wem.xhtml.impl.XhtmlHandler.TagStack;
import org.wikimodel.wem.xhtml.impl.XhtmlHandler.TagStack.TagContext;

/**
 * @author kotelnikov
 * @author vmassol
 * @author thomas.mortagne
 */
public class TagHandler {
    private boolean fAccumulateContent;

    /**
     * This flag is <code>true</code> if the current tag can have a text content
     */
    private final boolean fContentContainer;

    /**
     * This flag shows if the current tag can be used as a container for
     * embedded documents.
     */
    private final boolean fDocumentContainer;

    /**
     * This flag shows if the current tag should be created as a direct child of
     * a document.
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

        Stack<Boolean> insideBlockElementsStack = (Stack<Boolean>) context
            .getTagStack()
            .getStackParameter("insideBlockElement");

        if (isBlockHandler(context)) {
            // If we're starting a block tag and we're in inline mode (ie inside
            // a block element) then start a nested document
            // and save the parent tag, see endElement().
            if (!insideBlockElementsStack.isEmpty()
                && insideBlockElementsStack.peek()) {
                beginDocument(context);

                context.getTagStack().setStackParameter(
                    "documentParent",
                    context.getParent());

                // Get the new inside block element state
                insideBlockElementsStack = (Stack<Boolean>) context
                    .getTagStack()
                    .getStackParameter("insideBlockElement");
            }

            insideBlockElementsStack.push(true);
        }

        begin(context);
    }

    protected void end(TagContext context) {
    }

    public final void endElement(TagContext context) {
        // Verify if we need to close a nested document that would have been
        // opened.
        // To verify this we check the current tag being closed and verify if
        // it's the one saved when the nested document was opened.
        TagContext docParent = (TagContext) context
            .getTagStack()
            .getStackParameter("documentParent");
        if (context == docParent) {
            endDocument(context);
        }

        end(context);

        Stack<Boolean> insideBlockElementsStack = (Stack<Boolean>) context
            .getTagStack()
            .getStackParameter("insideBlockElement");

        if (isBlockHandler(context)) {
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
    public static void sendEmptyLines(XhtmlHandler.TagStack.TagContext context) {
        sendEmptyLines(context.getTagStack());
    }

    public static void sendEmptyLines(TagStack stack) {
        int lineCount = (Integer) stack.getStackParameter("emptyLinesCount");
        if (lineCount > 0) {
            stack.getScannerContext().onEmptyLines(lineCount);
            stack.setStackParameter("emptyLinesCount", 0);
        }
    }

    public void initialize(TagStack stack) {
        // Nothing to do by default. Override in children classes if need be.
    }

    /**
     * @return true if the current handler handles block tags (paragraphs,
     *         lists, tables, headers, etc)
     */
    public boolean isBlockHandler(TagContext context) {
        return false;
    }

    protected void beginDocument(TagContext context) {
        beginDocument(context, null);
    }

    protected void beginDocument(TagContext context, WikiParameters params) {
        sendEmptyLines(context);
        if (params == null) {
            context.getScannerContext().beginDocument();
        } else {
            context.getScannerContext().beginDocument(params);
        }

        Object ignoreElements = context.getTagStack().getStackParameter(
            "ignoreElements");

        // Stack context parameters since we enter in a new document
        context.getTagStack().pushStackParameters();

        // ignoreElements apply on embedded document
        context.getTagStack().setStackParameter(
            "ignoreElements",
            ignoreElements);
    }

    protected void endDocument(TagContext context) {
        context.getTagStack().popStackParameters();

        context.getScannerContext().endDocument();
    }
}