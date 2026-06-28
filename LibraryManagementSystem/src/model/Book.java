package model;


public class Book {

    // Các giá trị trạng thái 
    public static final String STATUS_AVAILABLE = "AVAILABLE";
    public static final String STATUS_BORROWED = "BORROWED";

    private String bookId;
    private String title;
    private String author;
    private String genre;
    private int publicationYear;
    private int quantity;
    private String status;
    private int timesBorrowed;

    // Constructor dùng khi thêm sách mới
    public Book(String bookId, String title, String author, String genre,
                int publicationYear, int quantity) {
        this(bookId, title, author, genre, publicationYear, quantity, 0);
    }

    // Constructor đầy đủ, dùng khi load từ file
    public Book(String bookId, String title, String author, String genre,
                int publicationYear, int quantity, int timesBorrowed) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.publicationYear = publicationYear;
        this.quantity = quantity;
        this.timesBorrowed = timesBorrowed;
        updateStatus();
    }

    // Cập nhật status theo quantity: còn sách -> AVAILABLE, hết sách -> BORROWED
    private void updateStatus() {
        this.status = (quantity > 0) ? STATUS_AVAILABLE : STATUS_BORROWED;
    }

    // Getter
    public String getBookId()       { return bookId; }
    public String getTitle()        { return title; }
    public String getAuthor()       { return author; }
    public String getGenre()        { return genre; }
    public int getPublicationYear() { return publicationYear; }
    public int getQuantity()        { return quantity; }
    public String getStatus()       { return status; }
    public int getTimesBorrowed()   { return timesBorrowed; }

    // Setter
    public void setTitle(String title) {
        if (title != null && !title.trim().isEmpty()) {
            this.title = title;
        }
    }

    public void setAuthor(String author) {
        if (author != null && !author.trim().isEmpty()) {
            this.author = author;
        }
    }

    public void setGenre(String genre) {
        if (genre != null && !genre.trim().isEmpty()) {
            this.genre = genre;
        }
    }

    public void setPublicationYear(int publicationYear) {
        if (publicationYear > 0) {
            this.publicationYear = publicationYear;
        }
    }

    public void setQuantity(int quantity) {
        if (quantity >= 0) {
            this.quantity = quantity;
            updateStatus();
        }
    }

    // Kiểm tra sách còn để mượn hay không 
    public boolean isAvailable() {
        return quantity > 0;
    }
    
    // Giảm số lượng sách khi có người mượn và cập nhật trạng thái
    public void decreaseQuantity() {
        if (quantity > 0) {
            quantity--;
            updateStatus();
        }
    }
    
    // Tăng số lượng sách khi có người trả và cập nhật trạng thái
    public void increaseQuantity() {
        quantity++;
        updateStatus();
    }

    // Tăng số lần sách được mượn
    public void increaseTimesBorrowed() {
        timesBorrowed++;
    }

    //  Hiển thị đầy đủ thông tin sách
    public void displayBookInfo() {
        System.out.printf("%-6s | %-28s | %-22s | %-12s | %4d | qty:%-2d | %-9s | borrowed:%d%n",
                bookId, title, author, genre, publicationYear, quantity, status, timesBorrowed);
    }
}
