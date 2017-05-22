package edu.hm.api; /*
 * (C) 2017, Lukas Marckmiller, l.marckmiller@hm.edu on 18.05.2017.
 * Java 1.8.0_121, Windows 10 Pro 64bit
 * Intel Core i5-6600K CPU/3.50GHz overclocked 4.1GHz, 4 cores, 16000 MByte RAM)
 * with IntelliJ IDEA 2017.1.1
 */

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("auth")
public class AuthenticationServerController {
    private final AuthenticationServerService authService;

    public AuthenticationServerController() {
         authService = new AuthenticationServerService();
    }

    @POST
    @Path("user")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createUser(User userToCreate)
    {
        AuthenticationServerResult result = authService.createUser(userToCreate);
        return Response.status(result.getStatus()).entity(result.getMessage()).build();
    }

    @POST
    @Path("token")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getToken(User userToAccess)
    {
        AuthenticationServerResult result = authService.createToken(userToAccess);
        return Response.status(result.getStatus()).entity(result.getPayload()).build();
    }

    @GET
    @Path("token")
    public Response validateToken(@HeaderParam("Authorization") String token)
    {
        AuthenticationServerResult result = authService.validateToken(token);
        return Response.status(result.getStatus()).entity(result.getMessage()).build();
    }

    @PUT
    @Path("token")
    public Response invalidateToken(@HeaderParam("Authorization") String token)
    {
        AuthenticationServerResult result = authService.invalidateToken(token);
        return Response.status(result.getStatus()).entity(result.getMessage()).build();
    }
}
