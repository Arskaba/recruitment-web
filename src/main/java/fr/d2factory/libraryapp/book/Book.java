package fr.d2factory.libraryapp.book;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A simple representation of a book
 */
public class Book {

    @JsonProperty("title")
    String title;
    @JsonProperty("author")
    String author;
    @JsonProperty("isbn")
    ISBN isbn;

    public Book(){
        super();
    }

    public Book(String title, String author, ISBN isbn) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
    }
}
