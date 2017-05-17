package edu.hm.shareit.resources;

import edu.hm.fachklassen.Book;

/**
 * Datastructure for a book service.
 */
interface BookService {
    /**
     * Adds a new book to the structure.
     * @param book to be added
     * @return Result containing information about the success
     */
    BookServiceResult addBook(Book book);

    /**
     * Gathers all the books curerntly in the structure.
     * @return All books in an array.
     */
    Book[] getBooks();

    /**
     * Gets a book from the structure.
     * @param isbn of the book to be returned
     * @return either the book or null
     */
    Book getBook(String isbn);

    /**
     * Updates a book in the structure.
     * @param book to update
     * @return BokServiceResult with information about execution
     */
    BookServiceResult updateBook(Book book);
}