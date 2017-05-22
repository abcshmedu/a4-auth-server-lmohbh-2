package edu.hm.api;/*
 * (C) 2017, Lukas, l.marckmiller@hm.edu on 21.05.2017. 
 * Java 1.8.0_121, Windows 10 Pro 64bit
 * Intel Core i5-6600K CPU/3.50GHz overclocked 4.1GHz, 4 cores, 16000 MByte RAM)
 * with IntelliJ IDEA 2017.1.1
 *
 */
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;

import javax.xml.crypto.Data;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AuthenticationServerService implements AuthenticationServer {
    public static Map<User,String> DATA_STORAGE = new HashMap<>();
    private static String SECRET = "LMOHBH20171527";

    @Override
    public AuthenticationServerResult createUser(User userToCreate) {
        if (userToCreate.getUsername().equals("") | userToCreate.getUsername().equals(""))
        {
            return AuthenticationServerResult.UserOrPasswordMissing;
        }

        if (DATA_STORAGE.keySet().stream()
                .map(User::getUsername)
                .filter(un -> un.equals(userToCreate.getUsername()))
                .count() != 0)
        {
            return AuthenticationServerResult.UserAlreadyExists;
        }

        DATA_STORAGE.put(userToCreate,null);
        return AuthenticationServerResult.AllRight;
    }

    @Override
    public AuthenticationServerResult createToken(User userToAccess) {
        //https://github.com/auth0/java-jwt
        if (userToAccess.getUsername().equals("") | userToAccess.getUsername().equals(""))
        {
            return AuthenticationServerResult.UserOrPasswordMissing;
        }

        if (DATA_STORAGE.keySet().stream()
                .filter(u -> u.getUsername().equals(userToAccess.getUsername())).count() == 0)
            return AuthenticationServerResult.UserNotExisting;

        if(DATA_STORAGE.keySet().stream()
                .filter(u -> u.getUsername().equals(userToAccess.getUsername()))
                .map(User::getPassword)
                .filter(u -> u.equals(userToAccess.getPassword()))
                .count() == 0)
            return AuthenticationServerResult.InvalidPassword;

        Algorithm algorithm = null;
        try {
            algorithm = Algorithm.HMAC256(SECRET);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        String token = JWT.create()
                .withExpiresAt(new Date(System.currentTimeMillis() + 30 * 1000))
                .sign(algorithm);
        DATA_STORAGE.replace(userToAccess,token);

        return AuthenticationServerResult.Validated.setPayload(token);
    }

    @Override
    public AuthenticationServerResult validateToken(String token) {
        if (token == null)
            return AuthenticationServerResult.EmptyToken;

        if (DATA_STORAGE.entrySet().stream().filter(es -> es.getValue().equals(token)).count() == 0)
            return AuthenticationServerResult.NoValidToken;

        Algorithm algorithm = null;
        try {
            algorithm = Algorithm.HMAC256(SECRET);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        JWTVerifier jwtVerifier = JWT.require(algorithm).acceptLeeway(5).build();
        try {
            DecodedJWT decodedJWT = jwtVerifier.verify(token);
            System.out.println(decodedJWT.getSignature());

        } catch(TokenExpiredException teE)
        {
            return AuthenticationServerResult.TokenExpired;

        } catch(JWTVerificationException jve)
        {
            return AuthenticationServerResult.NoValidToken;
        }

        //valid signiture
        return AuthenticationServerResult.Validated;
    }

    @Override
    public AuthenticationServerResult invalidateToken(String token)
    {
        if (token == null)
            return AuthenticationServerResult.EmptyToken;

        final Set<Map.Entry<User, String>> dataStorageSet =
                DATA_STORAGE.entrySet().stream()
                        .filter(ue -> token.equals(ue.getValue()))
                        .collect(Collectors.toSet());

        if (dataStorageSet.size() == 0)
            return AuthenticationServerResult.NoValidToken;
        else {
            DATA_STORAGE.replace(dataStorageSet.stream().map(ue -> ue.getKey()).findFirst().get(), null);
            return AuthenticationServerResult.TokenInvalidated;
        }

    }
}
