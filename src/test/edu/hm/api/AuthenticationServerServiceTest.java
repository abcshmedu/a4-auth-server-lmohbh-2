// CHECKSTYLE:OFF
//No reason to java doc test methods

package edu.hm.api;

import org.junit.Test;
import static org.junit.Assert.*;

/* (C) 2017, O. Hauser, ohauser@hm.edu
 * Munich University of Applied Sciences, Department 07, Computer Science
 * Java 1.8.0_131, Linux x86_64 4.4.0-66-generic
 * Dell (Intel Core i7-5500U CPU @ 2.40GHz, 4 cores, 8000 MByte RAM)
 **/
public class AuthenticationServerServiceTest {


    //createUser
    @Test(timeout = 1000)
    public void createUserUsernameIsNotEmpty()
    {
        final AuthenticationServerService aSS = new AuthenticationServerService();
        final User user = new User("","password");
        assertEquals(AuthenticationServerResult.UserOrPasswordMissing,aSS.createUser(user));
    }

    @Test(timeout = 1000)
    public void createUserPasswordIsNotEmpty()
    {
        final AuthenticationServerService aSS = new AuthenticationServerService();
        final User user = new User("user","");
        assertEquals(AuthenticationServerResult.UserOrPasswordMissing,aSS.createUser(user));
    }

    @Test(timeout = 1000)
    public void createUserIsSuccessful()
    {
        final AuthenticationServerService aSS = new AuthenticationServerService();
        final User user = new User("user","password");
        assertEquals(AuthenticationServerResult.AllRight,aSS.createUser(user));
    }

    @Test(timeout = 1000)
    public void createUserUsernameAlreadyExists()
    {
        final AuthenticationServerService aSS = new AuthenticationServerService();
        final User user = new User("user","password");
        assertEquals(AuthenticationServerResult.UserAlreadyExists,aSS.createUser(user));
    }


    //createToken
    @Test(timeout = 1000)
    public void createTokenUsernameIsNotEmpty()
    {
        final AuthenticationServerService aSS = new AuthenticationServerService();
        final User user = new User("","password");
        assertEquals(AuthenticationServerResult.UserOrPasswordMissing,aSS.createToken(user));
    }

    @Test(timeout = 1000)
    public void createTokenPasswordIsNotEmpty()
    {
        final AuthenticationServerService aSS = new AuthenticationServerService();
        final User user = new User("user","");
        assertEquals(AuthenticationServerResult.UserOrPasswordMissing,aSS.createToken(user));
    }

    @Test(timeout = 1000)
    public void createTokenUsernameExistsInDatabase()
    {
        final AuthenticationServerService aSS = new AuthenticationServerService();
        final User user = new User("resu","drowssap");
        assertEquals(AuthenticationServerResult.UserNotExisting,aSS.createToken(user));
    }

    @Test(timeout = 2000)
    public void createTokenIsSuccessful()
    {
        final AuthenticationServerService aSS = new AuthenticationServerService();
        final User user = new User("user","password");
        assertEquals(AuthenticationServerResult.TokenCreated,aSS.createToken(user));
    }

    @Test(timeout = 1000)
    public void createTokenPasswordIsValid()
    {
        final AuthenticationServerService aSS = new AuthenticationServerService();
        final User user = new User("user","drowssap");
        assertEquals(AuthenticationServerResult.InvalidPassword,aSS.createToken(user));
    }


    //validateToken
    @Test(timeout = 1000)
    public void validateTokenTokenIsNotNull()
    {
        final AuthenticationServerService aSS = new AuthenticationServerService();
        assertEquals(AuthenticationServerResult.EmptyToken,aSS.validateToken(null));
    }

    @Test(timeout = 1000)
    public void validateTokenTokenIsNotInDatabase()
    {
        final AuthenticationServerService aSS = new AuthenticationServerService();
        assertEquals(AuthenticationServerResult.NoValidToken,aSS.validateToken("WannaCry"));
    }

    @Test(timeout = 1000)
    public void validateTokenTokenIsValidated()
    {
        final AuthenticationServerService aSS = new AuthenticationServerService();
        final User user = new User("user","password");
        assertEquals(AuthenticationServerResult.Validated,aSS.validateToken(aSS.createToken(user).getPayload()));
    }


    //invalidateToken
    @Test(timeout = 1000)
    public void invalidateTokenTokenIsNotNull()
    {
        final AuthenticationServerService aSS = new AuthenticationServerService();
        assertEquals(AuthenticationServerResult.EmptyToken,aSS.invalidateToken(null));
    }

    @Test(timeout = 1000)
    public void invalidateTokenTokenIsNotInDatabase()
    {
        final AuthenticationServerService aSS = new AuthenticationServerService();
        assertEquals(AuthenticationServerResult.NoValidToken,aSS.invalidateToken("WannaCry"));
    }

    @Test(timeout = 1000)
    public void invalidateTokenTokenIsInvalidated()
    {
        final AuthenticationServerService aSS = new AuthenticationServerService();
        final User user = new User("user","password");
        assertEquals(AuthenticationServerResult.TokenInvalidated,aSS.invalidateToken(aSS.createToken(user).getPayload()));
    }



}