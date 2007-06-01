package org.wikimodel.core.model;

/**
 * Cursors provides sequncial access to all values returned by a search
 * operation.
 * 
 * @param <T> the type of the row returned by this cursor
 * @author MikhailKotelnikov
 * @author StéphaneLaurière
 */
public interface ICursor<T> {

    /**
     * Closes this query result and liberate all associated resources
     */
    void close();

    /**
     * Returns an currently loaded row.
     * 
     * @return the currently loaded row
     */
    T getCurrentRow();

    /**
     * Loads a next result row and returns <code>true</code> if it was
     * successfully loaded.
     * 
     * @return <code>true</code> if a next result row was successfully loaded
     */
    boolean loadNextRow();

}