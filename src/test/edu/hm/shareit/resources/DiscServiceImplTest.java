// CHECKSTYLE:OFF
//No reason to java doc test methods
package edu.hm.shareit.resources;

import edu.hm.fachklassen.Disc;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/*
*ShareIt
* Date: 27.04.2017
* java version 1.8.0_73
* Windows 10 (1607)
* Intel(R) Core(TM) i5-4200U CPU @ 1.60GHz 2.30 GHz
* @Author Sebastian Heunke, heunke@hm.edu
*/
public class DiscServiceImplTest {

    private DiscServiceImpl sut; //System under Test

    @Before
    public void reset() {
        DiscServiceImpl.DISCS.clear();
        sut = new DiscServiceImpl();
    }

    //Tests for the BookServiceImplementation
    @Test
    public void addingValidDisc() {
        Disc disc = new Disc("Disc", "1234567890123", 0, "Director");

        DiscServiceResult result = sut.addDisc(disc);
        Assert.assertEquals(DiscServiceResult.AllRight, result);
    }

    @Test
    public void noEmptyFieldsAccepted() {
        //No barcode
        Disc disc = new Disc("Dangerous Disc", "", 18, "H4ck0r");
        DiscServiceResult result = sut.addDisc(disc);
        Assert.assertEquals(DiscServiceResult.MissingParamBarcode, result);
        //No director
        disc = new Disc("Wikipedia", "1111111111111", 42, "");
        result = sut.addDisc(disc);
        Assert.assertEquals(DiscServiceResult.MissingParamDirector, result);
        //No titel
        disc = new Disc("", "0000000000001", 0, "Super Mario");
        result = sut.addDisc(disc);
        Assert.assertEquals(DiscServiceResult.MissingParamTitle, result);
        //Negative fsk
        disc = new Disc("How to get born", "1010101010101", -1, "Baby");
        result = sut.addDisc(disc);
        Assert.assertEquals(DiscServiceResult.NegativeFSK, result);
        //Duplicate Barcode
        disc = new Disc("OUTPUT_DISC.disc", "0000000011111", 10010, "Mr Roboto");
        sut.addDisc(disc);
        result = sut.addDisc(disc);
        Assert.assertEquals(DiscServiceResult.BarcodeAlreadyExists, result);
        //Invalid Barcode Length
        disc = new Disc("My first disc", "1", 1, "Me");
        result = sut.addDisc(disc);
        Assert.assertEquals(DiscServiceResult.BarcodeInvalidLength, result);
    }

    @Test
    public void getDiscWorks() {

        Disc disc = new Disc("Tetris", "0987654321321", 8, "Blocks");
        sut.addDisc(disc);
        Disc result = sut.getDisc("0987654321321");
        Assert.assertEquals(disc, result);

        result = sut.getDisc("Hello Disc Service!");
        Assert.assertEquals(null, result);
    }

    @Test
    public void getDiscsWorks() {
        Disc[] expected = new Disc[2];
        Disc disc = new Disc("My First Disc", "1111111111111", 1, "#1");
        expected[0] = disc;
        sut.addDisc(disc);

        disc = new Disc("My Second Disc", "2222222222222", 2, "#2");
        sut.addDisc(disc);
        expected[1] = disc;
        Disc[] result = sut.getDiscs();
        //Manually sort result array, there is no guaranteed order.
        if (result[0] != expected[0]) {
            Disc tmp = result[0];
            result[0] = result[1];
            result[1] = tmp;
        }
        Assert.assertArrayEquals(expected, result);

    }

    @Test
    public void updateDiscWorks() {
        Disc disc = new Disc("Heol World!", "9999999999999", 9, "Java");
        sut.addDisc(disc);
        disc = new Disc("Hello World!", "9999999999999", 8, "Java");
        DiscServiceResult result = sut.updateDisc(disc);
        Assert.assertEquals(DiscServiceResult.AllRight, result);

        Disc updatedDisc = sut.getDisc("9999999999999");
        Assert.assertEquals(disc, updatedDisc);

        result = sut.updateDisc(new Disc("Goodbye.", "666666666666", 5, "?"));
        Assert.assertEquals(DiscServiceResult.NoDiscWithBarcodeFound, result);
    }
}
