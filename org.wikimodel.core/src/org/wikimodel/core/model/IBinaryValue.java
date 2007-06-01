package org.wikimodel.core.model;

import java.io.InputStream;

/**
 * A common interface for all implementations of binary content.
 * 
 * @author StéphaneLaurière
 * @author MikhailKotelnikov
 */
public interface IBinaryValue extends IValue {

    /**
     * Returns an input stream providing access to the underlying binary content
     * 
     * @return an input stream providing access to the underlying binary content
     */
    InputStream getContent();

    /**
     * @return the MIME-type of this binary value
     */
    String getMimeType();

}
