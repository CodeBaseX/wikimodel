package org.wikimodel.wem;

/**
 * Instances of this type are used to transform references found in wiki
 * documents into corresponding structured objects - {@link WikiReference}.
 * 
 * @see WikiReference
 * @author kotelnikov
 */
public interface IWikiReferenceParser {

    /**
     * Parses the given reference, recognizes individual parts of this reference
     * (link, label, parameters) and returns the corresponding reference object.
     * 
     * @param str the reference to parse
     * @return a wiki reference corresponding to the given link
     */
    WikiReference parse(String str);

}