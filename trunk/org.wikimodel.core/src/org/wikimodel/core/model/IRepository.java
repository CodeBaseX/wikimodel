package org.wikimodel.core.model;

/**
 * Repository is the main access point for the storage functionality.
 * 
 * @author MikhailKotelnikov
 * @author StéphaneLaurière
 */
public interface IRepository {

    /**
     * Connects a user specified by the given login and password to the
     * underlying storage and returns a new user session.
     * 
     * @param login the user login
     * @param password the user password
     * @return a new user's session
     */
    ISession login(String login, String password);

}
