package fr.d2factory.libraryapp.library;

/**
 * This exception is thrown when a book is already borrowed
 */
public class IsAlreadyBorrowedException extends RuntimeException {
    public IsAlreadyBorrowedException(String errorMessage) {
        super(errorMessage);
    }
}
