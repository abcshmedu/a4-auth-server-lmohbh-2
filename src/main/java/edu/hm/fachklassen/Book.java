package edu.hm.fachklassen;

/*
*ShareIt
* Date: 12.04.2017
* java version 1.8.0_73
* Windows 10 (1607)
* Intel(R) Core(TM) i5-4200U CPU @ 1.60GHz 2.30 GHz
* @Author Sebastian Heunke, heunke@hm.edu
*/

/**
 * Book Represenation of a medium.
 */
public class Book extends Medium {

    private final String author;

    private final String isbn;

    /**
     * Creates new book with empty default values.
     */
    private Book() {
        this("", "", "");
    }

    /**
     * Creates a new book representation.
     * @param title of the book
     * @param author of the book
     * @param isbn unique to this book
     */
    public Book(String title, String author, String isbn) {
        super(title);
        if (author == null || isbn == null) {
            throw new IllegalArgumentException("Invalid author or isbn!");
        }
        this.author = author;
        this.isbn = isbn;
    }


    /**
     * Unique isbn for this book.
     * @return Isbn number.
     */
    public String getIsbn() {
        return isbn;
    }

    /**
     * Author of the book.
     * @return Author.
     */
    public String getAuthor() {
        return author;
    }

    @Override
    public String toString() {
        return "[" + getAuthor() + ": " + getTitle() + " (" + getIsbn() + ")]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Book book = (Book) o;

        if (!super.equals(book)) {
            return false;
        }
        if (!getAuthor().equals(book.getAuthor())) {
            return false;
        }
        return getIsbn().equals(book.getIsbn());
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + getAuthor().hashCode();
        result = 31 * result + getIsbn().hashCode();
        return result;
    }
}
