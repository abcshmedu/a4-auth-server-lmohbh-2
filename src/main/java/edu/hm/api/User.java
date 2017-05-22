package edu.hm.api; /*
 * (C) 2017, Lukas Marckmiller, l.marckmiller@hm.edu on 18.05.2017.
 * Java 1.8.0_121, Windows 10 Pro 64bit
 * Intel Core i5-6600K CPU/3.50GHz overclocked 4.1GHz, 4 cores, 16000 MByte RAM)
 * with IntelliJ IDEA 2017.1.1
 */

public class User {
    private final String username;
    private final String password;

    public User()
    {
        this("","");
    }

    User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    String getUsername() {
        return username;
    }

    String getPassword() {
        return password;
    }
}
