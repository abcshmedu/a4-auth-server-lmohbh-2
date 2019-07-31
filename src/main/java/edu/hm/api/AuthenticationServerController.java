package edu.hm.api; /*
 * (C) 2017, Lukas Marckmiller, l.marckmiller@hm.edu on 18.05.2017.
 * Java 1.8.0_121, Windows 10 Pro 64bit
 * Intel Core i5-6600K CPU/3.50GHz overclocked 4.1GHz, 4 cores, 16000 MByte RAM)
 * with IntelliJ IDEA 2017.1.1
 */

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Implements the REST API for the Authentication Server.
 *
 * @author Hauser Oliver, Heunke Sebastian, Marckmiller Lukas
 * @version 1.2
 */
@Path("a4")
public class AuthenticationServerController implements AuthenticationServer<Response> {

    private static final int OK = 200;

    private final AuthenticationServer<AuthenticationServerResult> authService;

    /**
     * Default Constructor.
     */
    public AuthenticationServerController() {
        authService = new AuthenticationServerService();
    }

    /**
     * Constructor.
     * @param authService AuthenticationServerService to work with
     */
    public AuthenticationServerController(AuthenticationServer<AuthenticationServerResult> authService) {
        this.authService = authService;
    }

    /**
     * Create new User with username and password. No pattern for password, accept each.
     *
     * @param userToCreate User Object with username and password
     * @return Returns an AuthenticationServerResult with certain Status code.
     */
    @POST
    @Path("user")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createUser(User userToCreate) {
        AuthenticationServerResult result = authService.createUser(userToCreate);
        return Response.status(result.getStatus()).entity(result.toJSONString()).build();
    }

    /**
     * Create Token for user given as param. Use a JWT Token with Expire Date and Secret symmetric algorithm.
     *
     * @param userToAccess User Object with username and password
     * @return Returns an AuthenticationServerResult with certain Status code and Token in Payload if successfully created.
     */
    @POST
    @Path("token")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createToken(User userToAccess) {
        AuthenticationServerResult result = authService.createToken(userToAccess);
        return Response.status(result.getStatus()).entity(result.toJSONString()).build();
    }

    /**
     * Try to validate token. Check if user owns this token and validate.
     *
     * @param token the JWT Token as String
     * @return Returns an AuthenticationServerResult with certain Status code.
     */
    @GET
    @Path("token")
    public Response validateToken(@HeaderParam("Authorization") String token) {
        AuthenticationServerResult result = authService.validateToken(token);
        return Response.status(OK).entity(result.toJSONString()).build();
    }

    /**
     * Invalidate the current Token, the token is gonna be deleted from the database.
     *
     * @param token the JWT Token as String
     * @return Returns an AuthenticationServerResult with certain Status code.
     */
    @PUT
    @Path("token")
    public Response invalidateToken(@HeaderParam("Authorization") String token) {
        AuthenticationServerResult result = authService.invalidateToken(token);
        return Response.status(result.getStatus()).entity(result.toJSONString()).build();
    }
}
