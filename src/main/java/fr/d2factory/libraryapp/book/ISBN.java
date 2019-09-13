package fr.d2factory.libraryapp.book;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ISBN {
    @JsonProperty("isbnCode")
    long isbnCode;

    public ISBN(){
        super();
    }

    public ISBN(long isbnCode) {
        this.isbnCode = isbnCode;
    }
}
