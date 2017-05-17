package edu.hm.shareit.resources;

import edu.hm.fachklassen.Book;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/*
 * (C) 2017, Lukas, l.marckmiller@hm.edu on 12.04.2017.
 * Java 1.8.0_111, Windows 10 Pro 64bit
 * Intel Core i5-6600K CPU/3.50GHz overclocked 4.1GHz, 4 cores, 16000 MByte RAM)
 * with IntelliJ IDEA 2017.1.1
 *
 * test: Chrome Plugin Postman for POST and PUT otherwise just write localhost:8082/shareit/media/books or for Discs localhost:8082/shareit/media/DISCS
  * to get a certain Book or Disc write localhost:8082/shareit/media/books/1234      with 1234 is the isbn number
 */

/**
 * BookResource Rest service handler.
 */
@Path("/media/books")
public class BookResource {

    private final BookService bookService;

    /**
     * Default ctor with defaul BookService used.
     */
    public BookResource() {
        this(new BookServiceImpl());
    }

    /**
     * Constructor to set other BookServices to be used.
     * Mainly used to inject Mock objects.
     * @param data service to be used
     */
    BookResource(BookService data) {
        bookService = data;
    }

    /**
     * Returns currently used bookService.
     * @return current bookService in use
     */
    private BookService getBookService() {
        return bookService;
    }

    /**
     * Rest request creating a new Book on the service data.
     * @param book to be created
     * @return information about the success
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createBook(Book book) {
        final BookServiceResult result = bookService.addBook(book);
        return Response
                .status(result.getStatus())
                .entity(getJsonFromServiceResult(result))
                .build();
    }

    /**
     * Rest request to update a existing book.
     * @param isbn of the book to be updated.
     * @param book updated book information
     * @return response with info about the success
     */
    @PUT
    @Path("{isbn}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateBook(@PathParam("isbn") String isbn, Book book) {
        //isbn in uri differs from isbn in book object. I do that because updateBook only gets a book object,
        //there's no possibility to check in service for the local var isbn because the json defined isbn is given to
        //the service
        if (!book.getIsbn().equals("") && !isbn.equals(book.getIsbn())) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new JSONObject().put("Message", "Isbn differs from isbn specified in payload.").toString())
                    .build();
        }
        //no isbn in request json body
        else if (book.getIsbn().equals("")) {
            book = new Book(book.getTitle(), book.getAuthor(), isbn);
        }

        //find and replace book object
        final BookServiceResult result = bookService.updateBook(book);
        return Response.status(result.getStatus().getStatusCode())
                .entity(getJsonFromServiceResult(result))
                .build();
    }

    /**
     * Rest request to get all the books in an array.
     * @return Result containg all books in an array.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBooks() {
        return Response
                .status(Response.Status.OK)
                .entity(getBookService().getBooks())
                .build();
    }

    /**
     * Rest request to get a book by its isbn.
     * @param isbn the book should have
     * @return Result containing either the book or info why not.
     */
    @GET
    @Path("{isbn}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBook(@PathParam("isbn") String isbn) {
        final Book book = getBookService().getBook(isbn);
        return Response
                .status(book == null ? Response.Status.BAD_REQUEST : Response.Status.OK)
                .entity(book == null ? new JSONObject()
                        .put("Status", Response.Status.BAD_REQUEST.getStatusCode())
                        .put("Message", "No book with given isbn found.").toString()
                        : book
                ).build();
    }


    /**
     * Transforms bookServiceResult to JsonObject
     * @param bookServiceResult to be transformed
     * @return a jsonObject
     */
    private String getJsonFromServiceResult(BookServiceResult bookServiceResult) {
        final JSONObject returnJsonObject = new JSONObject();
        returnJsonObject.put("Status", bookServiceResult.getStatus().getStatusCode());
        returnJsonObject.put("Message", bookServiceResult.getMessage());
        return returnJsonObject.toString();
    }
}
