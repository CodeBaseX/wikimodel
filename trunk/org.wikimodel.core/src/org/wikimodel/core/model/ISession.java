package org.wikimodel.core.model;

import java.io.InputStream;

/**
 * The user's session is the main access point to all functionalities provided
 * by the storage.
 * 
 * @author MikhailKotelnikov
 */
public interface ISession {

    /**
     * Returns a node corresponding to the given URI
     * 
     * @param uri the URI of the page to return
     * @return a node corresponding to the given uri
     */
    INode getNode(String uri);

    /**
     * Loads all nodes associated with URIs started with the specified
     * URI-prefix and returns them in the form of the cursor.
     * 
     * @param uriPrefix the URI-prefix of the returned nodes.
     * @return a cursor over all nodes associated with URIs starting with the
     *         specified prefix
     */
    ICursor<INode> getNodes(String uriPrefix);

    /**
     * @param input the input content stream
     * @param mimeType the mime type of the value
     * @return a newly created binary value
     */
    IBinaryValue newBinaryValue(InputStream input, String mimeType);

    /**
     * Creates and returns a new node
     * 
     * @return a new node
     */
    INode newNode();

    /**
     * Returns a newly created literal instance with the given string value and
     * language
     * 
     * @param value the literal value
     * @param language the language of the given literal value
     * @return a new literal instance with the given string value and language @
     */
    IStringValue newStringValue(String value, String language);

    /**
     * Returns a newly created typed value
     * 
     * @param value the internal object represenation of the typed value
     * @param dataType the URI of the value type
     * @return a newly created typed value
     */
    ITypedValue newTypedValue(Object value, String dataType);

    /**
     * Returns a newly created typed value
     * 
     * @param value the serialized form of a typed object
     * @param dataType the URI of the value type
     * @return a newly created typed value @
     */
    IValue newTypedValueFromString(String value, String dataType);

}
