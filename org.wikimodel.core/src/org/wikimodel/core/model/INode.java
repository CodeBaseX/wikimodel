package org.wikimodel.core.model;

import java.util.Collection;
import java.util.Set;

/**
 * The node is the main structural element of the storage. Each node is
 * associated with properties and references.
 * 
 * @author MikhailKotelnikov
 * @author StéphaneLaurière
 */
public interface INode extends IValue {

    /**
     * Returns an array of all properties defined for this node.
     * 
     * @return an array of all properties
     */
    INode[] getAllProperties();

    /**
     * Returns a cursor over all nodes referencing this one by at least one
     * property.
     * 
     * @param property the property
     * @return a cursor over all nodes referencing this one by at least one
     *         property
     */
    ICursor<INode> getBackReferences();

    /**
     * Returns a cursor over all nodes referencing this one by the specified
     * property.
     * 
     * @param property the property
     * @return a cursor over all nodes referencing this one by the specified
     *         property
     */
    ICursor<INode> getBackReferences(INode property);

    /**
     * Returns the value of the specified property. If there are many values of
     * this property then this method returns the first one.
     * 
     * @param property for this property the corresponding value will be
     *        returned
     * @return the value of the specified property
     */
    IValue getPropertyValue(INode property);

    /**
     * Returns a cursor over all values of the specified property
     * 
     * @param property for this property the corresponding value will be
     *        returned
     * @return a cursor of all property values
     */
    ICursor<IValue> getPropertyValues(INode property);

    /**
     * Returns an array of URIs identifying this node
     * 
     * @return an array of URIs identifying this node
     */
    Set<String> getURIs();

    /**
     * Removes all values of the specified property.
     * 
     * @param property the property to remove
     * @return <code>true</code> if all values of the specified property was
     *         removed
     */
    boolean removePropertyValues(INode property);

    /**
     * Sets the value of the specified property. All old values will be replaced
     * by the igven value.
     * 
     * @param property the property to set
     * @param value the value to set
     */
    void setPropertyValue(INode property, IValue value);

    /**
     * Sets a new values of the specified property.
     * 
     * @param property the property to set
     * @param values a collection of multiple property values
     */
    void setPropertyValues(INode property, Collection<IValue> values);

    /**
     * Sets a new values of the specified property.
     * 
     * @param property the property to set
     * @param values a cursor over all values to set
     */
    void setPropertyValues(INode property, ICursor<IValue> values);

    /**
     * Associates this node with the given set of URIs
     * 
     * @param uris a new URIs to associate with this node
     */
    void setURIs(Set<String> uris);

}
