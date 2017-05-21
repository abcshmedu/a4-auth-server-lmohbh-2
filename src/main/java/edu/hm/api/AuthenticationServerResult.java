package edu.hm.api;/*
 * (C) 2017, Lukas, l.marckmiller@hm.edu on 21.05.2017. 
 * Java 1.8.0_121, Windows 10 Pro 64bit
 * Intel Core i5-6600K CPU/3.50GHz overclocked 4.1GHz, 4 cores, 16000 MByte RAM)
 * with IntelliJ IDEA 2017.1.1
 *
 */

import javax.ws.rs.core.Response;

public enum AuthenticationServerResult {
    AllRight("Your request was correct. Like that!", Response.Status.OK);

    private final String message;
    private final Response.Status status;

    /**
     * Creates new Result with a message and status.
     * @param message message describing why this response is appearing
     * @param status status of the response
     */
    AuthenticationServerResult(String message, Response.Status status) {
        this.message = message;
        this.status = status;
    }

    /**
     * Reponse status of the Result.
     * @return Response status fitting the result
     */
    public Response.Status getStatus() {
        return status;
    }

    /**
     * String message of the reason why this response occurred.
     * @return User readable string
     */
    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return this.message;
    }
}
