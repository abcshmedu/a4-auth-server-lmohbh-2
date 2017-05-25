package edu.hm.api;/*
 * (C) 2017, Lukas, l.marckmiller@hm.edu on 21.05.2017. 
 * Java 1.8.0_121, Windows 10 Pro 64bit
 * Intel Core i5-6600K CPU/3.50GHz overclocked 4.1GHz, 4 cores, 16000 MByte RAM)
 * with IntelliJ IDEA 2017.1.1
 *
 */

import org.json.JSONObject;
import javax.ws.rs.core.Response;

/**
 * Implementation of Results for Authentication Server Exceptions.
 * @author Hauser Oliver, Heunke Sebastian, Marckmiller Lukas
 * @version 1.2
 */
public enum AuthenticationServerResult {
    //Status Messages for Authentication REST API
    AllRight("Your request was correct. Like that!", Response.Status.OK),
    TokenExpired("Token expired",Response.Status.BAD_REQUEST),
    NoValidToken("No valid token.",Response.Status.BAD_REQUEST),
    Validated("Validated",Response.Status.OK),
    UserAlreadyExists("User already exists",Response.Status.BAD_REQUEST),
    UserOrPasswordMissing("Username or password missing", Response.Status.BAD_REQUEST),
    UserNotExisting("User with username not existing",Response.Status.BAD_REQUEST),
    InvalidPassword("Invalid Password",Response.Status.BAD_REQUEST),
    EmptyToken("No Token in Header, expected Header field: Authorization: <Token>",Response.Status.BAD_REQUEST),
    TokenInvalidated("Successfully invalidated",Response.Status.OK),
    TokenCreated("Token successfuly created",Response.Status.CREATED);

    private final String message;
    private final Response.Status status;
    private String payload;
    /**
     * Creates new Result with a message and status.
     * @param message message describing why this response is appearing
     * @param status status of the response
     */
    AuthenticationServerResult(String message, Response.Status status) {
        this.message = message;
        this.status = status;
        this.payload = "";
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
    private String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return this.message;
    }

    /**
     * Method can be used to parse the result object to a JSONString.
     * @return result as JSON String.
     */
    public String toJSONString() {
        return new JSONObject()
                .put("Status",this.getStatus().getStatusCode())
                .put("Message",this.getMessage())
                .put("Payload",this.getPayload()).toString();
    }

    private String getPayload() {
        return payload;
    }

    public AuthenticationServerResult setPayload(String payload) {
        this.payload = payload;
        return this;
    }
}
