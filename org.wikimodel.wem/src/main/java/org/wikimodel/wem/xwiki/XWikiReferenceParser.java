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

import java.util.regex.Pattern;

import org.wikimodel.wem.WikiReferenceParser;

/**
 * @author kotelnikov
 * @author vmassol
 * @author thomas.mortagne
 */
public class XWikiReferenceParser extends WikiReferenceParser {

    Pattern BEGINLINK_PATTERN = Pattern.compile("[~]?\\[\\[");

    Pattern ENDLINK_PATTERN = Pattern.compile("[~]?\\]\\]");

    @Override
    protected String getLabel(String[] chunks) {
        return chunks[0];
    }

    @Override
    protected String getLink(String[] chunks) {
        return chunks[1];
    }

    @Override
    protected String getParameters(String[] chunks) {
        return chunks[2];
    }

    @Override
    protected String[] splitToChunks(String str) {
        String[] chunks = new String[3];

        char[] array = str.toCharArray();

        StringBuffer label = new StringBuffer();
        StringBuffer reference = new StringBuffer();
        StringBuffer parameters = new StringBuffer();

        boolean foundReference = false;
        int i = 0;
        int nb;
        for (boolean escaped = false; i < array.length; ++i) {
            char c = array[i];

            if (!escaped) {
                if (array[i] == '~') {
                    escaped = true;
                } else if ((nb = countFirstChar(array, i, '>')) >= 2) {
                    for (; nb > 2; --nb) {
                        label.append(array[i++]);
                    }
                    foundReference = true;
                    i += 2;
                    parseReference(array, i, reference, parameters);
                    break;
                } else if ((nb = countFirstChar(array, i, '|')) >= 2) {
                    for (; nb > 2; --nb) {
                        label.append(array[i++]);
                    }
                    i += 2;
                    parameters.append(array, i, array.length - i);
                    break;
                } else if (c == '[' && i + 1 < array.length
                        && array[i + 1] == '[') {
                    int endLink = findEndLink(array, i + 2);
                    if (endLink != -1) {
                        // If we find an internal link we skip it
                        label.append(array, i, endLink - i);
                        i = endLink - 1;
                    } else {
                        label.append("[[");
                        ++i;
                    }
                } else {
                    label.append(c);
                }
            } else {
                label.append(c);
                escaped = false;
            }
        }

        if (!foundReference) {
            chunks[1] = label.toString();
        } else {
            chunks[0] = label.toString();
            chunks[1] = reference.toString();
        }

        if (parameters.length() > 0) {
            chunks[2] = parameters.toString();
        }

        return chunks;
    }

    private int findEndLink(char[] array, int i) {
        int linkdepth = 1;
        int endLink = -1;

        for (boolean escaped = false; i < array.length; ++i) {
            char c = array[i];

            if (!escaped) {
                if (array[i] == '~') {
                    escaped = true;
                } else if (c == '[' && i + 1 < array.length
                        && array[i + 1] == '[') {
                    ++linkdepth;
                    ++i;
                } else if (c == ']' && i + 1 < array.length
                        && array[i + 1] == ']') {
                    --linkdepth;
                    ++i;
                    endLink = i + 1;
                    if (linkdepth == 0) {
                        break;
                    }
                }
            } else {
                escaped = false;
            }
        }

        return endLink;
    }

    /**
     * Extract the link and the parameters.
     * 
     * @param array the array to extract information from
     * @param i the current position in the array
     * @param reference the link buffer to fill
     * @param parameters the parameters buffer to fill
     */
    private void parseReference(
        char[] array,
        int i,
        StringBuffer reference,
        StringBuffer parameters) {
        int nb;

        for (boolean escaped = false; i < array.length; ++i) {
            char c = array[i];

            if (!escaped) {
                if (array[i] == '~' && !escaped) {
                    escaped = true;
                } else if ((nb = countFirstChar(array, i, '|')) >= 2) {
                    for (; nb > 2; --nb) {
                        reference.append(array[i++]);
                    }
                    i += 2;
                    parameters.append(array, i, array.length - i);
                    break;
                } else {
                    reference.append(c);
                }
            } else {
                reference.append(c);
                escaped = false;
            }
        }
    }

    private int countFirstChar(char[] array, int i, char c) {
        int nb = 0;
        for (; i < array.length && array[i] == c; ++i) {
            ++nb;
        }

        return nb;
    }
}
