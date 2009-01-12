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
import org.wikimodel.wem.xhtml.impl.XhtmlHandler.TagStack;

/**
 * Handle Macro definitions in comments (we store macro definitions in a comment since it
 * wouldn't be possible at all to reconstruct them from the result of their execution).
 * 
 * @author vmassol
 */
public class CommentHandler {

    public void onComment(String content, TagStack stack)
    {
        // Format of a macro definition in comment:
        //   <!--startmacro:velocity|-||-|
        //   Some **content**
        //   --><p>Some <strong>content</strong></p><!--stopmacro-->
        if (content.startsWith("startmacro:")) {
            stack.setStackParameter("ignoreElements", true);

            // Parse the comment to extract the macro name, parameters and content
            String[] tokens = content.substring("startmacro:".length()).split("\\|\\-\\|");
            String macroName = tokens[0];
            // Tokens will be of length 1 only when there's no content and no parameters defined.
            WikiParameters macroParams = WikiParameters.EMPTY;
            String macroContent = "";
            if (tokens.length > 1) {
                macroParams = WikiParameters.newWikiParameters(tokens[1]);
                if (tokens.length > 0) {
                    macroContent = tokens[2];
                }
            }
            
            // If we're inside a block element then issue an inline macro event otherwise issue a block macro event
            boolean insideBlockElement = (Boolean) stack.getStackParameter("insideBlockElement");
            if (insideBlockElement) {
                stack.getScannerContext().onMacroInline(macroName, macroParams, macroContent);
            } else {
                stack.getScannerContext().onMacroBlock(macroName, macroParams, macroContent);
            }
        } else if (content.startsWith("stopmacro")) {
            stack.setStackParameter("ignoreElements", false);
        }
    }
    
    
}
