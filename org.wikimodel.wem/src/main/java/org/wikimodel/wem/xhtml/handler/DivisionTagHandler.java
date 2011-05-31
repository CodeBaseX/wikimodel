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
import java.util.List;

import org.wikimodel.wem.WikiParameter;
import org.wikimodel.wem.xhtml.impl.XhtmlHandler.TagStack.TagContext;

/**
 * @author kotelnikov
 * @author vmassol
 * @author thomas.mortagne
 */
public class DivisionTagHandler extends TagHandler {
    public DivisionTagHandler() {
        super(true, false, true);
    }

    @Override
    public boolean isBlockHandler(TagContext context)
    {
        WikiParameter param = context.getParams().getParameter("class");
        if (param != null) {
            List<String> classes = Arrays.asList(param.getValue().split(" "));
            
            if (classes.contains("wikimodel-emptyline")) {
                return true;
            }
        }

        return false;
    }

    /**
     * @return the class used to indicate the division block is an embedded
     *         document. Note that use a method instead of a static private
     *         String field so that user code can override the class name.
     */
    protected String getDocumentClass() {
        return "wikimodel-document";
    }

    @Override
    protected void begin(TagContext context) {
        WikiParameter param = context.getParams().getParameter("class");
        if (param != null) {
            List<String> classes = Arrays.asList(param.getValue().split(" "));

            // Check if we have a div meaning an empty line between block
            if (classes.contains("wikimodel-emptyline")) {
                int value = (Integer) context.getTagStack().getStackParameter(
                    "emptyLinesCount");
                value++;
                context.getTagStack().setStackParameter(
                    "emptyLinesCount",
                    value);
            } else {
                // Consider that we're inside an embedded document
                beginDocument(context, context.getParams());
            }
        } else {
            // Consider that we're inside an embedded document
            beginDocument(context, context.getParams());
        }
    }

    @Override
    protected void end(TagContext context) {
        WikiParameter param = context.getParams().getParameter("class");
        if (param != null) {
            List<String> classes = Arrays.asList(param.getValue().split(" "));

            if (!classes.contains("wikimodel-emptyline")) {
                endDocument(context);
            }
        } else {
            endDocument(context);
        }
    }
}
