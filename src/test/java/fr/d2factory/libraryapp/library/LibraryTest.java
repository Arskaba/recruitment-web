package fr.d2factory.libraryapp.library;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.d2factory.libraryapp.book.Book;
import fr.d2factory.libraryapp.book.BookRepository;
import fr.d2factory.libraryapp.book.ISBN;
import fr.d2factory.libraryapp.member.FirstYearStudent;
import fr.d2factory.libraryapp.member.Resident;
import fr.d2factory.libraryapp.member.Student;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LibraryTest {
    private Library library;
    private BookRepository bookRepository = new BookRepository();
    private FirstYearStudent firstYearStudent = new FirstYearStudent();
    private Student student = new Student();
    private Resident resident = new Resident();

    @Before
    public void setup() throws IOException {
        InputStream is = LibraryTest.class.getResourceAsStream("/books.json");
        String jsonBookArray = IOUtils.toString(is);
        ObjectMapper mapper = new ObjectMapper();
        List<Book> books = mapper.readValue(jsonBookArray, new TypeReference<List<Book>>(){});
        bookRepository.addBooks(books);

        library = new LibraryImpl(bookRepository);
        firstYearStudent.setWallet(20.00f);
        student.setWallet(20.00f);
        resident.setWallet(20.00f);
    }

    @Test
    public void member_can_borrow_a_book_if_book_is_available() {
        Book book = library.borrowBook(3326456467846L, firstYearStudent, LocalDate.now());
        Assert.assertTrue(book != null);
    }

    @Test(expected = IsAlreadyBorrowedException.class)
    public void borrowed_book_is_no_longer_available() {
        Book book = library.borrowBook(3326456467846L, firstYearStudent, LocalDate.now());
        Book book2 = library.borrowBook(3326456467846L, student, LocalDate.now());
    }

    @Test
    public void residents_are_taxed_10cents_for_each_day_they_keep_a_book() {
        Book book = library.borrowBook(3326456467846L, resident, LocalDate.now().minusDays(30));
        library.returnBook(book, resident);
        Assert.assertTrue(resident.getWallet() == 17.00f);
    }

    @Test
    public void students_pay_10_cents_the_first_30days() {
        Book book = library.borrowBook(3326456467846L, student, LocalDate.now().minusDays(10));
        library.returnBook(book, student);
        Assert.assertTrue(student.getWallet() == 19.00f);
    }

    @Test
    public void students_in_1st_year_are_not_taxed_for_the_first_15days() {
        Book book = library.borrowBook(3326456467846L, firstYearStudent, LocalDate.now().minusDays(15));
        library.returnBook(book, firstYearStudent);
        Assert.assertTrue(firstYearStudent.getWallet() == 20.00f);
    }

    @Test
    public void students_in_1st_year_are_taxed_if_they_keep_for_more_than_15days() {
        Book book = library.borrowBook(3326456467846L, firstYearStudent, LocalDate.now().minusDays(30));
        library.returnBook(book, firstYearStudent);
        Assert.assertTrue(firstYearStudent.getWallet() == 18.50f);
    }

    @Test
    public void students_pay_15cents_for_each_day_they_keep_a_book_after_the_initial_30days() {
        Book book = library.borrowBook(3326456467846L, student, LocalDate.now().minusDays(40));
        library.returnBook(book, student);
        Assert.assertTrue(student.getWallet() == 15.50f);
    }

    @Test
    public void residents_pay_20cents_for_each_day_they_keep_a_book_after_the_initial_60days() {
        Book book = library.borrowBook(3326456467846L, resident, LocalDate.now().minusDays(70));
        library.returnBook(book, resident);
        Assert.assertTrue(resident.getWallet() == 12.50f);
    }

    @Test(expected = HasLateBooksException.class)
    public void members_cannot_borrow_book_if_they_have_late_books() {
        Book book = library.borrowBook(3326456467846L, resident, LocalDate.now().minusDays(70));
        Book bok2 = library.borrowBook(46578964513L, resident, LocalDate.now());
    }
}
