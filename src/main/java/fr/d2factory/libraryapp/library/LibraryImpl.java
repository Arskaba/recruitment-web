package fr.d2factory.libraryapp.library;

import fr.d2factory.libraryapp.book.Book;
import fr.d2factory.libraryapp.book.BookRepository;
import fr.d2factory.libraryapp.member.Member;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class LibraryImpl implements Library {

    private BookRepository bookRepository;

    public LibraryImpl(BookRepository bookRepository){
        this.bookRepository = bookRepository;
    }

    @Override
    public Book borrowBook(long isbnCode, Member member, LocalDate borrowedAt) throws HasLateBooksException, IsAlreadyBorrowedException {
        Book bookToBorrow = bookRepository.findBook(isbnCode);
        if (bookToBorrow == null) {
            throw new IsAlreadyBorrowedException("This book is already borrowed");
        }
        if(member.borrowedBookList != null) {
            for (Book bookAlreadyBorrowedByMember : member.borrowedBookList) {
                LocalDate date = bookRepository.findBorrowedBookDate(bookAlreadyBorrowedByMember);
                if (member.isLate(date)) {
                    throw new HasLateBooksException("This member is late to return a book !");
                }
            }
        }
        bookRepository.saveBookBorrow(bookToBorrow, borrowedAt);
        member.borrowedBookList.add(bookToBorrow);
        return bookToBorrow;
    }

    @Override
    public void returnBook(Book book, Member member) {
        LocalDate now = LocalDate.now();
        LocalDate startingDate = bookRepository.findBorrowedBookDate(book);
        Long numberOfDays = ChronoUnit.DAYS.between(startingDate, now);
        member.payBook(numberOfDays.intValue());
        member.borrowedBookList.remove(book);
    }
}
