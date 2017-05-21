package edu.hm.api;/*
 * (C) 2017, Lukas, l.marckmiller@hm.edu on 21.05.2017. 
 * Java 1.8.0_121, Windows 10 Pro 64bit
 * Intel Core i5-6600K CPU/3.50GHz overclocked 4.1GHz, 4 cores, 16000 MByte RAM)
 * with IntelliJ IDEA 2017.1.1
 *
 */

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import edu.hm.entities.User;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AuthenticationServerService implements AuthenticationServer {
    public static Map<User,String> DATA_STORAGE = new HashMap<>();
    private static String SECRET = "LMOHBH20171527";

    @Override
    public AuthenticationServerResult createUser(User userToCreate) {
        //TODO: check for valid user (user name not given, username and password not empty...)
        DATA_STORAGE.put(userToCreate,null);
        return null;
    }

    @Override
    public String createToken(User userToAccess) {
        //https://github.com/auth0/java-jwt
        Algorithm algorithm = null;

        try {
            algorithm = Algorithm.HMAC256(SECRET);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        return JWT.create()
                .withExpiresAt(new Date(System.currentTimeMillis() + 120 * 1000))
                .sign(algorithm);
    }

    @Override
    public AuthenticationServerResult validateToken(String token) {
        return null;
    }

    @Override
    public AuthenticationServerResult invalidateToken(String token) {
        return null;
    }
}
