package edu.hm.api; /*
 * (C) 2017, Lukas Marckmiller, l.marckmiller@hm.edu on 18.05.2017.
 * Java 1.8.0_121, Windows 10 Pro 64bit
 * Intel Core i5-6600K CPU/3.50GHz overclocked 4.1GHz, 4 cores, 16000 MByte RAM)
 * with IntelliJ IDEA 2017.1.1
 */

import sun.misc.BASE64Encoder;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Data Structure for user object.
 * @author  Lukas Marckmiller
 * @version 1.2
 */
public class User {
    private final String username;
    private String password;

    public User()
    {
        this("","");
    }

    public User(String username, String password) {
        setPassword(password);
        //TODO: hash password
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    /**
     * Setter for Hashing the password.
     * @param password password as clear String
     * @return SHA256 Hashed password
     */
    public User setPassword(String password)
    {
        this.password = sha256HashValue(password);
        return this;
    }

    //SHA256 Algorithm

    /**
     * Creates a SHA256 value of a String var.
     * @param string some string
     * @return SHA256 hashed String
     */
    public static String sha256HashValue(String string)
    {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e.getCause());
        }
        digest.update(string.getBytes(StandardCharsets.UTF_8));

        return String.format("%064x", new java.math.BigInteger(1, digest.digest()));
    }

    public boolean equalsHashedPassword(String clearPassword)
    {
       return clearPassword.equals(getPassword());
    }

    //Equals and Hashcode needed because User objects stored in Hashmap, identifying the objects in map with equals method
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return (username != null
                ? username.equals(user.username)
                : user.username == null) && (password != null
                ? password.equals(user.password)
                : user.password == null);
    }

    @Override
    public int hashCode() {
        int result = username != null ? username.hashCode() : 0;
        result = 31 * result + (password != null ? password.hashCode() : 0);
        return result;
    }
}
