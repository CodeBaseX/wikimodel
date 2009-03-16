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

import java.util.ArrayList;
import java.util.List;

import org.wikimodel.wem.WikiParameter;
import org.wikimodel.wem.WikiParameters;
import org.wikimodel.wem.impl.WikiScannerUtil;
import org.wikimodel.wem.xhtml.impl.XhtmlHandler.TagStack;

/**
 * Handle Macro definitions in comments (we store macro definitions in a comment
 * since it wouldn't be possible at all to reconstruct them from the result of
 * their execution).
 * 
 * @author vmassol
 * @author tmortagne
 */
public class CommentHandler {
    private static final String MACRO_SEPARATOR = "|-|";

    public void onComment(String content, TagStack stack) {
        // Format of a macro definition in comment:
        // <!--startmacro:velocity|-||-|
        // Some **content**
        // --><p>Some <strong>content</strong></p><!--stopmacro-->
        if (content.startsWith("startmacro:")) {
            stack.setStackParameter("ignoreElements", true);

            String macroName;
            WikiParameters macroParams = WikiParameters.EMPTY;
            String macroContent = null;

            String macroString = content.substring("startmacro:".length());

            int index = macroString.indexOf(MACRO_SEPARATOR);

            if (index != -1) {
                // Extract macro name
                macroName = macroString.substring(0, index);

                // Remove macro name part and continue parsing
                macroString = macroString.substring(index
                        + MACRO_SEPARATOR.length());

                index = macroString.indexOf(MACRO_SEPARATOR);
                if (index != -1) {
                    // Extract macro parameters
                    List<WikiParameter> parameters = new ArrayList<WikiParameter>();
                    index = WikiScannerUtil.splitToPairs(macroString,
                            parameters, null, MACRO_SEPARATOR);
                    macroParams = new WikiParameters(parameters);

                    // Extract macro content
                    macroContent = macroString.substring(index
                            + MACRO_SEPARATOR.length());
                } else {
                    // There is only parameters remaining in the string, the
                    // macro does not have content
                    // Extract macro parameters
                    macroParams = WikiParameters.newWikiParameters(macroString);
                }
            } else {
                // There is only macro name, the macro does not have parameters
                // or content
                macroName = macroString;
            }

            // If we're inside a block element then issue an inline macro event
            // otherwise issue a block macro event
            boolean insideBlockElement = (Boolean) stack
                    .getStackParameter("insideBlockElement");
            if (insideBlockElement) {
                stack.getScannerContext().onMacroInline(macroName, macroParams,
                        macroContent);
            } else {
                stack.getScannerContext().onMacroBlock(macroName, macroParams,
                        macroContent);
            }
        } else if (content.startsWith("stopmacro")) {
            stack.setStackParameter("ignoreElements", false);
        }
    }
}
