/*******************************************************************************
 * Copyright (c) 2005,2008 Cognium Systems SA and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Contributors:
 *     Cognium Systems SA - initial API and implementation
 *******************************************************************************/
package org.wikimodel.wem.xwiki;

public class XWikiScannerUtil {
    public static String unescapeVerbatim(String content) {
        StringBuffer unescapedContent = new StringBuffer();

        boolean escaped = false;
        char[] buff = content.toCharArray();
        for (int i = 0; i < buff.length; ++i) {
            if (!escaped) {
                if (buff[i] == '~') {
                    escaped = true;
                    continue;
                }
            } else {
                if (i < (i = matchVerbatimSyntax(buff, i, '{'))) {
                    unescapedContent.append("{{{");
                    escaped = false;
                    continue;
                } else if (i < (i = matchVerbatimSyntax(buff, i, '}'))) {
                    unescapedContent.append("}}}");
                    escaped = false;
                    continue;
                } else {
                    unescapedContent.append('~');
                }

                escaped = false;
            }

            unescapedContent.append(buff[i]);
        }

        return unescapedContent.toString();
    }

    private static int matchVerbatimSyntax(
        char buff[],
        int currentIndex,
        char syntax) {

        int i = currentIndex;
        boolean escaped = true;
        for (int j = 0; i < buff.length && j < 3; ++i) {
            if (!escaped) {
                if (buff[i] == syntax) {
                    if (++j == 3) {
                        return i;
                    }
                } else if (buff[i] == '~') {
                    escaped = true;
                }
            } else {
                if (buff[i] == syntax) {
                    if (++j == 3) {
                        return i;
                    }
                } else {
                    break;
                }

                escaped = false;
            }
        }

        return currentIndex;
    }
}
