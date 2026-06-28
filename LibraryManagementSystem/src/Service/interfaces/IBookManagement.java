package service.interfaces;

import model.Book;
import java.util.List;


public interface IBookManagement {

    boolean addBook(Book book);

    boolean removeBook(String bookId);

    Book getBookById(String bookId);

    List<Book> getAllBooks();

    List<Book> searchBooks(String keyword);

    void save();
}
