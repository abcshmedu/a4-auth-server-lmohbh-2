/*
 * (C) 2017, Lukas, l.marckmiller@hm.edu on 21.05.2017.
 * Java 1.8.0_121, Windows 10 Pro 64bit
 * Intel Core i5-6600K CPU/3.50GHz overclocked 4.1GHz, 4 cores, 16000 MByte RAM)
 * with IntelliJ IDEA 2017.1.1
 */
package edu.hm.api;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Implementation of Authentication Server.
 *
 * @author Hauser Oliver, Heunke Sebastian, Marckmiller Lukas
 * @version 1.2
 * @see edu.hm.api.AuthenticationServer
 */
public class AuthenticationServerService implements AuthenticationServer<AuthenticationServerResult> {

    private static final int SECOND = 1000;
    private static final int EXPIRE_TIME = 120;
    private static final int LEEWAY = 5;

    //Current Data Storage
    private static final Map<User, String> DATA_STORAGE = new HashMap<>();
    //Secret for the JWT Creation Alogrithm. Only used in this class.
    private static final String SECRET = "LMOHBH20171527";

    @Override
    public AuthenticationServerResult createUser(User userToCreate) {
        final String emptyPassword = User.sha256HashValue("");
        //Check if username and password not missing
        if (userToCreate.getUsername().equals("") || userToCreate.getPassword().equals(emptyPassword)) {
            return AuthenticationServerResult.UserOrPasswordMissing;
        }

        //check if username already exists
        if (DATA_STORAGE.keySet().stream()
                .map(User::getUsername)
                .filter(un -> un.equals(userToCreate.getUsername()))
                .count() != 0) {
            return AuthenticationServerResult.UserAlreadyExists;
        }

        //insert to data structure
        DATA_STORAGE.put(userToCreate, null);
        return AuthenticationServerResult.AllRight;
    }

    @Override
    public AuthenticationServerResult createToken(User userToAccess) {
        //Check if username and password not missing
        final String emptyPassword = User.sha256HashValue("");
        if (userToAccess.getUsername().equals("") | userToAccess.getPassword().equals(emptyPassword)) {
            return AuthenticationServerResult.UserOrPasswordMissing;
        }

        //check if given user exists in data storage
        if (DATA_STORAGE.keySet().stream()
                .filter(u -> u.getUsername().equals(userToAccess.getUsername())).count() == 0) {
            return AuthenticationServerResult.UserNotExisting;
        }

        //check if given password equals password for user in database
        if (DATA_STORAGE.keySet().stream()
                .filter(u -> u.getUsername().equals(userToAccess.getUsername()))
                .filter(u -> u.equalsHashedPassword(userToAccess.getPassword()))
                .count() == 0) {
            return AuthenticationServerResult.InvalidPassword;
        }

        //define algorithm for jwt creation
        Algorithm algorithm = null;
        try {
            algorithm = Algorithm.HMAC256(SECRET);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        //create jwt token, lifetime = 120 sec
        String token = JWT.create()
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRE_TIME * SECOND))
                .sign(algorithm);
        //set token for user in datastorage, this is needed either for invalidation of the token or to guarantee exactly one token per user.
        DATA_STORAGE.replace(userToAccess, token);

        return AuthenticationServerResult.TokenCreated.setPayload(token);
    }

    @Override
    public AuthenticationServerResult validateToken(String token) {
        //check if token is empty
        if (token == null) {
            return AuthenticationServerResult.EmptyToken;
        }

        //check if token exists in Database, to avoid self created tokens or usage of none expired old tokens.
        if (DATA_STORAGE.entrySet().stream().filter(es -> token.equals(es.getValue())).count() == 0) {
            return AuthenticationServerResult.NoValidToken;
        }

        Algorithm algorithm = null;
        try {
            algorithm = Algorithm.HMAC256(SECRET);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        //verify token with leeway of 5 sec
        JWTVerifier jwtVerifier = JWT.require(algorithm).acceptLeeway(LEEWAY).build();
        try {
            DecodedJWT decodedJWT = jwtVerifier.verify(token);
            System.out.println(decodedJWT.getSignature());

        } catch (TokenExpiredException teE) {
            return AuthenticationServerResult.TokenExpired;

        } catch (JWTVerificationException jve) {
            return AuthenticationServerResult.NoValidToken;
        }

        //valid signature
        return AuthenticationServerResult.Validated;
    }

    @Override
    public AuthenticationServerResult invalidateToken(String token) {
        //check if token is empty
        if (token == null) {
            return AuthenticationServerResult.EmptyToken;
        }

        //check if token exists in data storage
        final Set<Map.Entry<User, String>> dataStorageSet =
                DATA_STORAGE.entrySet().stream()
                        .filter(ue -> token.equals(ue.getValue()))
                        .collect(Collectors.toSet());

        if (dataStorageSet.size() == 0) {
            return AuthenticationServerResult.NoValidToken;
        } else if (dataStorageSet.size() > 1) {
            throw new RuntimeException("Token " + token + " exists more than once in data storage, critical exception!");
        } //invalidate token
        else {
            DATA_STORAGE.replace(dataStorageSet.stream().map(Map.Entry::getKey).findFirst().get(), null);
            return AuthenticationServerResult.TokenInvalidated;
        }
    }
}
