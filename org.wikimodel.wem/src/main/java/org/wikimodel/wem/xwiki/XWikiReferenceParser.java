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

import org.wikimodel.wem.WikiReferenceParser;

/**
 * @author kotelnikov
 * @author vmassol
 * @author tmortagne
 */
public class XWikiReferenceParser extends WikiReferenceParser {

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
        StringBuffer link = new StringBuffer();
        StringBuffer parameters = new StringBuffer();

        boolean foundLink = false;
        int i = 0;
        int nb;
        for (boolean escaped = false; i < array.length; ++i) {
            char c = array[i];

            if (!escaped) {
                if (array[i] == '~' && !escaped) {
                    escaped = true;
                } else if ((nb = countFirstChar(array, i, '>')) >= 2) {
                    for (; nb > 2; --nb) {
                        label.append(array[i++]);
                    }
                    foundLink = true;
                    i += 2;
                    parseLink(array, i, link, parameters);
                    break;
                } else if ((nb = countFirstChar(array, i, '|')) >= 2) {
                    for (; nb > 2; --nb) {
                        label.append(array[i++]);
                    }
                    i += 2;
                    parameters.append(array, i, array.length - i);
                    break;
                } else {
                    label.append(c);
                }
            } else {
                label.append(c);
                escaped = false;
            }
        }

        if (!foundLink) {
            chunks[1] = label.toString();
        } else {
            chunks[0] = label.toString();
            chunks[1] = link.toString();
        }

        if (parameters.length() > 0) {
            chunks[2] = parameters.toString();
        }

        return chunks;
    }

    /**
     * Extract the link and the parameters.
     * 
     * @param array the array to extract information from
     * @param i the current position in the array
     * @param link the link buffer to fill
     * @param parameters the parameters buffer to fill
     */
    private void parseLink(
        char[] array,
        int i,
        StringBuffer link,
        StringBuffer parameters) {
        int nb;

        for (boolean escaped = false; i < array.length; ++i) {
            char c = array[i];

            if (!escaped) {
                if (array[i] == '~' && !escaped) {
                    escaped = true;
                } else if ((nb = countFirstChar(array, i, '|')) >= 2) {
                    for (; nb > 2; --nb) {
                        link.append(array[i++]);
                    }
                    i += 2;
                    parameters.append(array, i, array.length - i);
                    break;
                } else {
                    link.append(c);
                }
            } else {
                link.append(c);
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
