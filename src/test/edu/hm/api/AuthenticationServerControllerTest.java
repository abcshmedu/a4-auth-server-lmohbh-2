// CHECKSTYLE:OFF
//No reason to java doc test methods
package edu.hm.api;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import javax.ws.rs.core.Response;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/* (C) 2017, O. Hauser, ohauser@hm.edu
 * Munich University of Applied Sciences, Department 07, Computer Science
 * Java 1.8.0_131, Linux x86_64 4.4.0-66-generic
 * Dell (Intel Core i7-5500U CPU @ 2.40GHz, 4 cores, 8000 MByte RAM)
 **/

@RunWith(MockitoJUnitRunner.class)
public class AuthenticationServerControllerTest {

    private AuthenticationServerController sut = new AuthenticationServerController();
    @Mock
    private AuthenticationServer<AuthenticationServerResult> serviceMock;
    private final AuthenticationServerService aSS = new AuthenticationServerService();

    @Before
    public void setUp() {
        serviceMock = (AuthenticationServer<AuthenticationServerResult>) Mockito.mock(AuthenticationServer.class);
        sut = new AuthenticationServerController(serviceMock);
    }

    //createUser
    @Test(timeout = 1000)
    public void createUserIsSuccessful(){
        User user = new User("user", "password");
        when(serviceMock.createUser(user)).thenReturn(AuthenticationServerResult.AllRight);
        Response result = sut.createUser(user);
        Assert.assertEquals(Response.Status.OK.getStatusCode(), result.getStatus());
    }

    @Test(timeout = 4000)
    public void createUserPasswordIsNotEmpty() {
        User user = new User("", "password");
        when(serviceMock.createUser(user)).thenReturn(AuthenticationServerResult.UserOrPasswordMissing);
        Response result = sut.createUser(user);
        Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), result.getStatus());
    }

    @Test(timeout = 1000)
    public void createUserUsernameIsNotEmpty() {
        User user = new User("user", "");
        when(serviceMock.createUser(user)).thenReturn(AuthenticationServerResult.UserOrPasswordMissing);
        Response result = sut.createUser(user);
        Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), result.getStatus());
    }

    @Test(timeout = 1000)
    public void createUserUsernameAlreadyExists() {
        User user = new User("user", "password");
        when(serviceMock.createUser(user)).thenReturn(AuthenticationServerResult.UserAlreadyExists);
        Response result = sut.createUser(user);
        Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), result.getStatus());
    }

    //createToken
    @Test(timeout = 1000)
    public void createTokenUsernameIsNotEmpty()
    {
        User user = new User("", "password");
        when(serviceMock.createToken(user)).thenReturn(AuthenticationServerResult.UserOrPasswordMissing);
        Response result = sut.createToken(user);
        Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), result.getStatus());
    }

    @Test(timeout = 1000)
    public void createTokenPasswordIsNotEmpty()
    {
        User user = new User("user", "");
        when(serviceMock.createToken(user)).thenReturn(AuthenticationServerResult.UserOrPasswordMissing);
        Response result = sut.createToken(user);
        Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), result.getStatus());
    }

    @Test(timeout = 1000)
    public void createTokenUsernameExistsInDatabase()
    {
        User user = new User("resu", "drowssap");
        when(serviceMock.createToken(user)).thenReturn(AuthenticationServerResult.UserNotExisting);
        Response result = sut.createToken(user);
        Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), result.getStatus());
    }

    @Test(timeout = 1000)
    public void createTokenIsSuccessful()
    {
        User user = new User("user", "password");
        when(serviceMock.createToken(user)).thenReturn(AuthenticationServerResult.TokenCreated);
        Response result = sut.createToken(user);
        Assert.assertEquals(Response.Status.CREATED.getStatusCode(), result.getStatus());
    }

    @Test(timeout = 1000)
    public void createTokenPasswordIsValid()
    {
        User user = new User("user", "drowssap");
        when(serviceMock.createToken(user)).thenReturn(AuthenticationServerResult.InvalidPassword);
        Response result = sut.createToken(user);
        Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), result.getStatus());
    }

    //validate token
    @Test(timeout = 1000)
    public void validateTokenTokenIsValid()
    {
        User user = new User("user","password");
        String token = aSS.createToken(user).getPayload();
        when(serviceMock.validateToken(token)).thenReturn(AuthenticationServerResult.Validated);
        Response result = sut.validateToken(token);
        Assert.assertEquals(Response.Status.OK.getStatusCode(), result.getStatus());
    }

    //invalidate token
    @Test(timeout = 1000)
    public void invalidateTokenTokenIsNotInDatabase()
    {
        when(serviceMock.invalidateToken("WannaCry")).thenReturn(AuthenticationServerResult.NoValidToken);
        Response result = sut.invalidateToken("WannaCry");
        Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), result.getStatus());
    }

    @Test(timeout = 1000)
    public void invalidateTokenTokenIsInvalidated()
    {
        User user = new User("user","password");
        String token = aSS.createToken(user).getPayload();
        when(serviceMock.invalidateToken(token)).thenReturn(AuthenticationServerResult.TokenInvalidated);
        Response result = sut.invalidateToken(token);
        Assert.assertEquals(Response.Status.OK.getStatusCode(), result.getStatus());
    }
}