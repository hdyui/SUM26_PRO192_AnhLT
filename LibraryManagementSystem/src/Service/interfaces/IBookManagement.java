package service.interfaces;

import model.Book;
import java.util.List;

public interface IBookManagement {

    // Tao DAU SACH MOI hoan toan + sinh quantity ban sao (serial) dau tien cho no
    boolean addNewBookTitle(String bookId, String title, String author, String genre,
                             int year, int quantity);

    // Dau sach DA TON TAI -> chi sinh them "quantity" ban sao moi, dung lai metadata cu
    boolean addCopiesToExistingBook(String bookId, int quantity);

    // Kiem tra dau sach (bookId) da ton tai chua (co it nhat 1 ban sao)
    boolean bookTitleExists(String bookId);

    // Lay TOAN BO cac ban sao thuoc 1 dau sach (dung cho search mode 1)
    List<Book> getCopiesByBookId(String bookId);

    // Lay DUY NHAT 1 ban sao theo serialNumber (dung cho search mode 2)
    Book getCopyBySerial(String serialNumber);

    // Tim ban sao DAU TIEN dang AVAILABLE cua 1 dau sach -> dung cho auto-assign khi muon
    Book findFirstAvailableCopy(String bookId);

    int countAvailable(String bookId);

    int countTotal(String bookId);

    // Toan bo ban sao trong he thong (khong gop nhom)
    List<Book> getAllBooks();

    // Danh sach cac ma dau sach (khong trung lap)
    List<String> getAllBookIds();

    // Tim theo tu khoa (title/author/genre/bookId) - fallback khi khong khop serial/bookId
    List<Book> searchBooks(String keyword);

    // Xoa 1 ban sao cu the (chi cho phep neu dang AVAILABLE)
    boolean removeCopy(String serialNumber);

    // Xoa toan bo dau sach (chi cho phep neu KHONG co ban nao dang BORROWED)
    boolean removeBookTitle(String bookId);

    // Cap nhat metadata, ap dung cho TAT CA ban sao cung bookId. Truyen null/-1 de bo qua field.
    boolean updateBookInfo(String bookId, String title, String author, String genre, int year);

    void save();
}
