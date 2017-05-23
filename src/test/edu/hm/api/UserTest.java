package edu.hm.api;

import org.junit.Test;

import static org.junit.Assert.*;

/*
* (C) 2017, Lukas Marckmiller, l.marckmiller@hm.edu on 23.05.2017.
* Java 1.8.0_121, Windows 10 Pro 64bit
* Intel Core i5-6600K CPU/3.50GHz overclocked 4.1GHz, 4 cores, 16000 MByte RAM)
* with IntelliJ IDEA 2017.1.1
*/
public class UserTest {
    @Test(timeout = 300)
    public void testGetUsername() {
        fail();
    }

    @Test(timeout = 300)
    public void testGetPassword() {
        fail();
    }

    @Test
    public void testSha256HashValue()
    {
        final String testpassword123Hashed = "b55c8792d1ce458e279308835f8a97b580263503e76e1998e279703e35ad0c2e";
        assertEquals(User.sha256HashValue("testpassword123"),testpassword123Hashed);
    }

    @Test(timeout = 300)
    public void testEqualsHashedPasswordOverNonEmptyConstructor() {
        User user = new User("testuser","testpassword123");
        assertTrue(user.equalsHashedPassword("testpassword123"));
    }

    @Test(timeout = 300)
    public void testEqualsHashedPasswordOverEmptyConstructor() {
        User user = new User();
        user.setPassword("testpassword123");
        assertTrue(user.equalsHashedPassword("testpassword123"));
    }
}