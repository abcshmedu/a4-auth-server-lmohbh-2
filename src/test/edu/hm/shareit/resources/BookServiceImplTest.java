// CHECKSTYLE:OFF
//No reason to java doc test methods
package edu.hm.shareit.resources;

import edu.hm.fachklassen.Book;
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
public class BookServiceImplTest {

    private BookServiceImpl sut; //System under Test

    @Before
    public void reset() {
        BookServiceImpl.BOOKS_SET.clear();
        sut = new BookServiceImpl();
    }

    //Tests for the BookServiceImplementation
    @Test
    public void addingValidBook() {
        Book book = new Book("Mobby Dick", "Author", "123456");
        BookServiceResult result = sut.addBook(book);
        Assert.assertEquals(BookServiceResult.AllRight, result);
    }

    @Test
    public void noEmptyFieldsAccepted() {
        //No Titel
        Book book = new Book("", "Author", "123456");
        BookServiceResult result = sut.addBook(book);
        Assert.assertEquals(BookServiceResult.MissingParamTitle, result);
        //No Author
        book = new Book("Java for Dummies", "", "42");
        result = sut.addBook(book);
        Assert.assertEquals(BookServiceResult.MissingParamAuthor, result);
        //No Isbn
        book = new Book("Isbn Book", "Santa Claus", "");
        result = sut.addBook(book);
        Assert.assertEquals(BookServiceResult.MissingParamIsbn, result);

        book = new Book("11000101", "Robot", "#FF");
        sut.addBook(book);
        book = new Book("11000110", "Robot-2.0", "#FF");
        result = sut.addBook(book);
        Assert.assertEquals(BookServiceResult.BookWithIsbnExistsAlready, result);
    }

    @Test
    public void getBooksWorks() {
        Book[] expected = new Book[2];
        Book book = new Book("My First Book", "Someone", "#1");
        expected[0] = book;
        sut.addBook(book);

        book = new Book("My Second Book", "Someone", "#2");
        sut.addBook(book);
        expected[1] = book;
        Book[] result = sut.getBooks();
        //Manually sort result array, there is no guaranteed order.
        if (result[0] != expected[0]) {
            Book tmp = result[0];
            result[0] = result[1];
            result[1] = tmp;
        }
        Assert.assertArrayEquals(expected, result);
    }


    @Test
    public void getBookWorks() {


        Book book = new Book("booooook", "authoooor", "isbn");
        sut.addBook(book);
        Book result = sut.getBook("isbn");
        Assert.assertEquals(book, result);

        result = sut.getBook("Hello Book Service!");
        Assert.assertEquals(null, result);
    }

    @Test
    public void updateBookWorks() {
        Book book = new Book("HeloWorld", "Me", "0");
        sut.addBook(book);
        book = new Book("Hello World!", "Not Me", "0");
        BookServiceResult result = sut.updateBook(book);
        Assert.assertEquals(BookServiceResult.AllRight, result);

        Book updatedBook = sut.getBook("0");
        Assert.assertEquals(book, updatedBook);

        result = sut.updateBook(new Book("Goodbye.", "?", "42"));
        Assert.assertEquals(BookServiceResult.NoBookWithIsbnFound, result);
    }
}
