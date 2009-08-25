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
 * @author thomas.mortagne
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
     * WikiParameters.
     * 
     * @param chunks the array of chunks
     * @return the parameters
     */
    protected WikiParameters getParameters(String[] chunks) {
        return WikiParameters.newWikiParameters(chunks.length > 2 ? chunks[2]
            .trim() : null);
    }

    /**
     * @see org.wikimodel.wem.IWikiReferenceParser#parse(java.lang.String)
     */
    public WikiReference parse(String str) {
        if (str == null) {
            return null;
        }

        String[] chunks = splitToChunks(str);
        if (chunks.length == 0) {
            return null;
        }

        String link = getLink(chunks);
        String label = getLabel(chunks);
        WikiParameters parameters = getParameters(chunks);

        return new WikiReference(link, label, parameters);
    }

    /**
     * Returns the given string split to individual segments
     * 
     * @param str the string to split
     * @return the given string split to individual segments
     */
    protected String[] splitToChunks(String str) {
        String delimiter = "[|>]";
        String[] chunks = str.split(delimiter);
        return chunks;
    }

}