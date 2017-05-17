// CHECKSTYLE:OFF
//No reason to java doc test methods
package edu.hm.shareit.resources;

import edu.hm.fachklassen.Disc;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.core.Response;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/*
*ShareIt
* Date: 23.04.2017
* java version 1.8.0_73
* Windows 10 (1607)
* Intel(R) Core(TM) i5-4200U CPU @ 1.60GHz 2.30 GHz
* @Author Sebastian Heunke, heunke@hm.edu
*/

public class DiscResourceTest {
    private DiscResource sut = new DiscResource();
    private DiscService serviceMock;

    @Before
    public void setUp() {
        serviceMock = mock(DiscService.class);
        sut = new DiscResource(serviceMock);
    }

    @Test
    public void postNewDisc() {
        Disc disc = new Disc("Okay Disc", "OKOKOKOKOKOK.", 0, "Voll Okay.");
        when(serviceMock.addDisc(disc)).thenReturn(DiscServiceResult.AllRight);
        Response result = sut.createDisc(disc);
        Assert.assertEquals(Response.Status.OK.getStatusCode(), result.getStatus());

        disc = new Disc("", "aaaaaaaaaaaaa", 0, "No Titel.");
        when(serviceMock.addDisc(disc)).thenReturn(DiscServiceResult.MissingParamTitle);
        result = sut.createDisc(disc);
        Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), result.getStatus());

        disc = new Disc("The Disc", "", 42, "Directör");
        when(serviceMock.addDisc(disc)).thenReturn(DiscServiceResult.MissingParamBarcode);
        result = sut.createDisc(disc);
        Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), result.getStatus());

        disc = new Disc("Dupplicate disc", "OKOKOKOKOKOK.", 9000, "Copyyyy");
        when(serviceMock.addDisc(disc)).thenReturn(DiscServiceResult.BarcodeAlreadyExists);
        result = sut.createDisc(disc);
        Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), result.getStatus());
    }

    @Test
    public void getPostedDiscs() {
        Disc disc = new Disc("Get Me!", "abcdefghijklm", 0, "???");
        when(serviceMock.addDisc(disc)).thenReturn(DiscServiceResult.AllRight);
        when(serviceMock.getDisc("abcdefghijklm")).thenReturn(disc);
        sut.createDisc(disc);
        Response result = sut.getDisc("abcdefghijklm");
        Assert.assertEquals(disc, result.getEntity());

        Response nullDisc = sut.getDisc("NOT A BARCODE");
        when(serviceMock.getDisc("NOT A BARCODE")).thenReturn(null);
        Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), nullDisc.getStatus());

        when(serviceMock.getDiscs()).thenReturn(new Disc[]{disc});
        result = sut.getDiscs();
        Assert.assertArrayEquals(new Disc[]{disc}, (Disc[]) result.getEntity());//temporary test
    }


    @Test
    public void updateDiscs() {
        Disc disc = new Disc("dvd", "0000000000013", 13, "Hans");
        when(serviceMock.addDisc(disc)).thenReturn(DiscServiceResult.AllRight);
        when(serviceMock.updateDisc(new Disc("something", "0000000000444", 4, "Peter"))).thenReturn(DiscServiceResult.NoDiscWithBarcodeFound);
        when(serviceMock.getDisc("0000000000013")).thenReturn(disc);
        sut.createDisc(disc);
        Response result = sut.updateDisc("000000000013", new Disc("something", "0000000000444", 4, "Peter"));

        Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), result.getStatus());
        Assert.assertEquals(disc, sut.getDisc("0000000000013").getEntity());

        disc = new Disc("cd", "0000000000013", 13, "Hans");
        when(serviceMock.updateDisc(disc)).thenReturn(DiscServiceResult.AllRight);
        when(serviceMock.getDisc("0000000000013")).thenReturn(disc);
        result = sut.updateDisc("0000000000013", disc);
        Assert.assertEquals(Response.Status.OK.getStatusCode(), result.getStatus());
        Assert.assertEquals(disc, sut.getDisc("0000000000013").getEntity());

        disc = new Disc("New disc!", "discdiscdisc!", 1111111, "Mr. Disc");
        when(serviceMock.addDisc(disc)).thenReturn(DiscServiceResult.AllRight);
        sut.createDisc(disc);
        disc = new Disc("Old disc!", "", 222222, "Mrs. Disc");
        when(serviceMock.updateDisc(new Disc("Old disc!", "discdiscdisc!", 222222, "Mrs. Disc"))).thenReturn(DiscServiceResult.AllRight);
        result = sut.updateDisc("discdiscdisc!", disc);
        Assert.assertEquals(Response.Status.OK.getStatusCode(), result.getStatus());
    }


}