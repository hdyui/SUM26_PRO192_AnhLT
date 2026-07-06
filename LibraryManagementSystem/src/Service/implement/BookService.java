package service.implement;

import model.Book;
import service.interfaces.IBookManagement;
import utils.FileUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BookService implements IBookManagement {

    private List<Book> bookList;
    private static final String FILE_NAME = "books.txt";
    private static final Random RANDOM = new Random();

    public BookService() {
        this.bookList = FileUtils.loadBooks(FILE_NAME);
    }

    // Sinh 1 serialNumber duy nhat cho 1 bookId: [bookId] + [6 so ngau nhien]
    // Lap lai neu trung (xac suat rat thap nhung van kiem tra cho chac chan).
    private String generateSerialNumber(String bookId) {
        String serial;
        do {
            int randomPart = RANDOM.nextInt(1_000_000); // 0 -> 999999
            serial = bookId + String.format("%06d", randomPart);
        } while (getCopyBySerial(serial) != null);
        return serial;
    }

    @Override
    public boolean addNewBookTitle(String bookId, String title, String author, String genre,
                                    int year, int quantity) {
        if (bookId == null || bookId.trim().isEmpty() || quantity < 1) {
            return false;
        }
        // Dau sach phai la MOI hoan toan (chua co ban sao nao)
        if (bookTitleExists(bookId)) {
            return false;
        }
        for (int i = 0; i < quantity; i++) {
            String serial = generateSerialNumber(bookId);
            bookList.add(new Book(bookId, serial, title, author, genre, year));
        }
        save();
        return true;
    }

    @Override
    public boolean addCopiesToExistingBook(String bookId, int quantity) {
        if (quantity < 1) {
            return false;
        }
        List<Book> existing = getCopiesByBookId(bookId);
        if (existing.isEmpty()) {
            return false;
        }
        // Dung lai metadata cua ban sao dau tien lam mau
        Book sample = existing.get(0);
        for (int i = 0; i < quantity; i++) {
            String serial = generateSerialNumber(bookId);
            bookList.add(new Book(bookId, serial, sample.getTitle(), sample.getAuthor(),
                    sample.getGenre(), sample.getPublicationYear()));
        }
        save();
        return true;
    }

    @Override
    public boolean bookTitleExists(String bookId) {
        return !getCopiesByBookId(bookId).isEmpty();
    }

    @Override
    public String generateNextBookId() {
    // Lay so lon nhat trong cac Book ID hien co (dang B + 3 chu so), roi +1.
    // Dung max thay vi dem so luong dau sach de tranh trung ID neu co dau sach
    // o giua da bi xoa (remove) truoc do.
    int maxNum = 0;
    for (String id : getAllBookIds()) {
        try {
            int num = Integer.parseInt(id.substring(1));
            if (num > maxNum) {
                maxNum = num;
            }
        } catch (Exception e) {
            // Bo qua neu ID cu khong dung dinh dang chuan B + 3 so
        }
    }
    return String.format("B%03d", maxNum + 1);
    }
    
    @Override
    public List<Book> getCopiesByBookId(String bookId) {
        List<Book> result = new ArrayList<>();
        if (bookId == null) {
            return result;
        }
        for (Book b : bookList) {
            if (b.getBookId().equalsIgnoreCase(bookId)) {
                result.add(b);
            }
        }
        return result;
    }

    @Override
    public Book getCopyBySerial(String serialNumber) {
        if (serialNumber == null) {
            return null;
        }
        for (Book b : bookList) {
            if (b.getSerialNumber().equalsIgnoreCase(serialNumber)) {
                return b;
            }
        }
        return null;
    }

    @Override
    public Book findFirstAvailableCopy(String bookId) {
        for (Book b : bookList) {
            if (b.getBookId().equalsIgnoreCase(bookId) && b.isAvailable()) {
                return b;
            }
        }
        return null;
    }

    @Override
    public int countAvailable(String bookId) {
        int count = 0;
        for (Book b : getCopiesByBookId(bookId)) {
            if (b.isAvailable()) {
                count++;
            }
        }
        return count;
    }

    @Override
    public int countTotal(String bookId) {
        return getCopiesByBookId(bookId).size();
    }

    @Override
    public List<Book> getAllBooks() {
        return bookList;
    }

    @Override
    public List<String> getAllBookIds() {
        List<String> ids = new ArrayList<>();
        for (Book b : bookList) {
            if (!ids.contains(b.getBookId())) {
                ids.add(b.getBookId());
            }
        }
        return ids;
    }

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

    @Override
    public boolean removeCopy(String serialNumber) {
        Book book = getCopyBySerial(serialNumber);
        if (book == null || book.getStatus().equals(Book.STATUS_BORROWED)) {
            return false;
        }
        bookList.remove(book);
        save();
        return true;
    }

    @Override
    public boolean removeBookTitle(String bookId) {
        List<Book> copies = getCopiesByBookId(bookId);
        if (copies.isEmpty()) {
            return false;
        }
        // Khong cho xoa neu con BAT KY ban nao dang duoc muon
        for (Book b : copies) {
            if (b.getStatus().equals(Book.STATUS_BORROWED)) {
                return false;
            }
        }
        bookList.removeAll(copies);
        save();
        return true;
    }

    @Override
    public boolean updateBookInfo(String bookId, String title, String author, String genre, int year) {
        List<Book> copies = getCopiesByBookId(bookId);
        if (copies.isEmpty()) {
            return false;
        }
        // Ap dung cho TAT CA ban sao cung bookId (metadata dung chung 1 dau sach)
        for (Book b : copies) {
            if (title != null) b.setTitle(title);
            if (author != null) b.setAuthor(author);
            if (genre != null) b.setGenre(genre);
            if (year > 0) b.setPublicationYear(year);
        }
        save();
        return true;
    }

    @Override
    public void save() {
        FileUtils.saveBooks(bookList, FILE_NAME);
    }
}
