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
package org.wikimodel.wem;

/**
 * This class is used as a common parser of references. It is used to transform
 * references found in wiki documents into corresponding structured objects -
 * {@link WikiReference}. Methods of this class should be overloaded to parse
 * correctly wiki-specific references.
 * 
 * @see WikiReference
 * @author kotelnikov
 */
public class WikiReferenceParser implements IWikiReferenceParser {

    /**
     * Extracts the label from the array of chunks and returns it.
     * 
     * @param chunks the array of chunks
     * @return a label extracted from the given array of chunks
     */
    protected String getLabel(String[] chunks) {
        return chunks.length > 1 ? chunks[1].trim() : null;
    }

    /**
     * Extracts the link from the array of chunks and returns it.
     * 
     * @param chunks the array of chunks
     * @return a link extracted from the given array of chunks
     */
    protected String getLink(String[] chunks) {
        return chunks[0].trim();
    }

    /**
     * Extracts parameters part of the original reference and returns it as a
     * unique string.
     * 
     * @param chunks the array of chunks
     * @return a part of the parsed reference containing parameters
     */
    protected String getParameters(String[] chunks) {
        return chunks.length > 2 ? chunks[2].trim() : null;
    }

    /**
     * @see org.wikimodel.wem.IWikiReferenceParser#parse(java.lang.String)
     */
    public WikiReference parse(String str) {
        if (str == null || "".equals(str.trim()))
            return null;
        String[] chunks = splitToChunks(str);
        if (chunks.length == 0)
            return null;
        String link = getLink(chunks);
        String label = getLabel(chunks);
        WikiParameters params = WikiParameters
            .newWikiParameters(getParameters(chunks));
        WikiReference reference = new WikiReference(link, label, params);
        return reference;
    }

    /**
     * Returns the given string splitted to individual segments
     * 
     * @param str the string to split
     * @return the given string splitted to individual segments
     */
    protected String[] splitToChunks(String str) {
        String delimiter = "[|>]";
        String[] chunks = str.split(delimiter);
        return chunks;
    }

}