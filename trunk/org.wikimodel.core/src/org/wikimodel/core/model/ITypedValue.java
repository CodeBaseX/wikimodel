package org.wikimodel.core.model;

/**
 * This is a common interface for all typed values.
 * 
 * @author MikhailKotelnikov
 */
public interface ITypedValue extends IValue {

    /**
     * Returns a string-serialized representation of this value
     * 
     * @return a string-serialized representation of this value
     */
    String getAsString();

    /**
     * Returns the URI of the type of the value
     * 
     * @return the URI of the type of the value
     */
    String getDataType();

    /**
     * Returns the typed value object
     * 
     * @return the typed value object
     */
    Object getValue();
}
