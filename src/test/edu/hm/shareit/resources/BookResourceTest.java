// CHECKSTYLE:OFF
//No reason to java doc test methods
package edu.hm.shareit.resources;

import edu.hm.fachklassen.Book;
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

public class BookResourceTest {
    private BookResource sut = new BookResource();
    private BookService serviceMock;

    @Before
    public void setUp() {
        serviceMock = mock(BookService.class);
        sut = new BookResource(serviceMock);
    }

    @Test
    public void postNewBook() {
        Book book = new Book("TITEL", "AUTHOR", "ISBN");
        when(serviceMock.addBook(book)).thenReturn(BookServiceResult.AllRight);
        Response result = sut.createBook(book);
        Assert.assertEquals(Response.Status.OK.getStatusCode(), result.getStatus());

        book = new Book("", "AUTHOR", "4");
        when(serviceMock.addBook(book)).thenReturn(BookServiceResult.MissingParamTitle);
        result = sut.createBook(book);
        Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), result.getStatus());

        book = new Book("A good Titel", "A medicore Author", "");
        when(serviceMock.addBook(book)).thenReturn(BookServiceResult.MissingParamIsbn);
        result = sut.createBook(book);
        Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), result.getStatus());

        book = new Book("No Book", "", "404");
        when(serviceMock.addBook(book)).thenReturn(BookServiceResult.MissingParamAuthor);
        result = sut.createBook(book);
        Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), result.getStatus());

        book = new Book("Just another Book", "One more Author", "ISBN");
        when(serviceMock.addBook(book)).thenReturn(BookServiceResult.BookWithIsbnExistsAlready);
        result = sut.createBook(book);
        Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), result.getStatus());
    }

    @Test
    public void getPostedBooks() {
        Book book = new Book(".", ",", ";");
        when(serviceMock.addBook(book)).thenReturn(BookServiceResult.AllRight);
        when(serviceMock.getBook(";")).thenReturn(book);
        sut.createBook(book);
        Response result = sut.getBook(";");
        Assert.assertEquals(book, result.getEntity());

        when(serviceMock.getBook("NOT A ISBN")).thenReturn(null);
        Response nullBook = sut.getBook("NOT A ISBN");
        Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), nullBook.getStatus());

        when(serviceMock.getBooks()).thenReturn(new Book[]{book});
        result = sut.getBooks();
        Assert.assertArrayEquals(new Book[]{book}, (Book[]) result.getEntity());//temporary test
    }

    @Test
    public void updateBooks() {
        Book book = new Book("abc", "def", "ghi");
        when(serviceMock.addBook(book)).thenReturn(BookServiceResult.AllRight);
        when(serviceMock.updateBook(new Book("ABC", "DEF", "XYZ"))).thenReturn(BookServiceResult.NoBookWithIsbnFound);
        when(serviceMock.getBook("ghi")).thenReturn(book);
        sut.createBook(book);
        Response result = sut.updateBook("ghi", new Book("ABC", "DEF", "XYZ"));

        Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), result.getStatus());
        Assert.assertEquals(book, sut.getBook("ghi").getEntity());

        book = new Book("ABC", "DEF", "ghi");
        when(serviceMock.updateBook(book)).thenReturn(BookServiceResult.AllRight);
        when(serviceMock.getBook("ghi")).thenReturn(book);
        result = sut.updateBook("ghi", book);
        Assert.assertEquals(Response.Status.OK.getStatusCode(), result.getStatus());
        Assert.assertEquals(book, sut.getBook("ghi").getEntity());

        book = new Book("New", "Book", "!");
        when(serviceMock.addBook(book)).thenReturn(BookServiceResult.AllRight);
        sut.createBook(book);
        book = new Book("Old", "Scroll", "");
        when(serviceMock.updateBook(new Book("Old", "Scroll", "!"))).thenReturn(BookServiceResult.AllRight);
        result = sut.updateBook("!", book);
        Assert.assertEquals(Response.Status.OK.getStatusCode(), result.getStatus());
    }


}