package service.implement;

import model.Book;
import service.interfaces.IBookManagement;
import utils.FileUtils;

import java.util.ArrayList;
import java.util.List;


public class BookService implements IBookManagement {
    
    //  Danh sách lưu tất cả sách trong hệ thống
    private List<Book> bookList;
    private static final String FILE_NAME = "books.txt";

    // Khởi tạo BookService và đọc dữ liệu từ file
    public BookService() {
        this.bookList = FileUtils.loadBooks(FILE_NAME);
    }
    
     // Thêm sách mới nếu hợp lệ và không trùng mã
    @Override
    public boolean addBook(Book book) {
        if (book == null) {
            return false;
        }
        // ID phai duy nhat (BR1)
        if (getBookById(book.getBookId()) != null) {
            return false;
        }
        bookList.add(book);
        save();
        return true;
    }
    
     // Xóa sách theo mã sách
    @Override
    public boolean removeBook(String bookId) {
        Book book = getBookById(bookId);
        if (book == null) {
            return false;
        }
        bookList.remove(book);
        save();
        return true;
    }
    
    // Tìm sách theo mã sách
    @Override
    public Book getBookById(String bookId) {
        for (Book b : bookList) {
            if (b.getBookId().equalsIgnoreCase(bookId)) {
                return b;
            }
        }
        return null;
    }
    
     // Lấy toàn bộ danh sách sách
    @Override
    public List<Book> getAllBooks() {
        return bookList;
    }

    // Tìm kiếm sách theo từ khóa (mã, tên, tác giả hoặc thể loại)
    @Override
    public List<Book> searchBooks(String keyword) {
        List<Book> result = new ArrayList<>();
        if (keyword == null) {
            return result;
        }
        String k = keyword.trim().toLowerCase();
        for (Book b : bookList) {
            if (b.getBookId().toLowerCase().contains(k)
                    || b.getTitle().toLowerCase().contains(k)
                    || b.getAuthor().toLowerCase().contains(k)
                    || b.getGenre().toLowerCase().contains(k)) {
                result.add(b);
            }
        }
        return result;
    }

    // Lưu danh sách sách xuống file
    @Override
    public void save() {
        FileUtils.saveBooks(bookList, FILE_NAME);
    }
}
