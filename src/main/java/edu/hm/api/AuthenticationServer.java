package edu.hm.api;
/*
 * (C) 2017, Lukas, l.marckmiller@hm.edu on 21.05.2017. 
 * Java 1.8.0_121, Windows 10 Pro 64bit
 * Intel Core i5-6600K CPU/3.50GHz overclocked 4.1GHz, 4 cores, 16000 MByte RAM)
 * with IntelliJ IDEA 2017.1.1
 */

/**
 * A authentication server.
 *
 * The server checks if a user is registered to
 * the service and therefore has the rights to use it.
 *
 * @author Hauser Oliver, Heunke Sebastian, Marckmiller Lukas
 * @version 1.2
 */
interface AuthenticationServer<T> {

    /**
     * Create new User with username and password. No pattern for password, accept each.
     *
     * @param userToCreate User Object with username and password
     * @return Returns an AuthenticationServerResult with certain Status code.
     */
    T createUser(User userToCreate);

    /**
     * Create Token for user given as param. Use a JWT Token with Expire Date and Secret symmetric algorithm.
     *
     * @param userToAccess User Object with username and password
     * @return Returns an AuthenticationServerResult with certain Status code and Token in Payload if successfully created.
     */
    T createToken(User userToAccess);

    /**
     * Try to validate token. Check if user owns this token and validate.
     *
     * @param token the JWT Token as String
     * @return Returns an AuthenticationServerResult with certain Status code.
     */
    T validateToken(String token);

    /**
     * Invalidate the current Token, the token is gonna be deleted from the database.
     *
     * @param token the JWT Token as String
     * @return Returns an AuthenticationServerResult with certain Status code.
     */
    T invalidateToken(String token);
}
