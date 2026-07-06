package model;

public class Book {

    public static final String STATUS_AVAILABLE = "AVAILABLE";
    public static final String STATUS_BORROWED = "BORROWED";

    private String bookId;         // Ma dau sach dung chung cho nhieu ban (VD: B001)
    private String serialNumber;   // Ma rieng cho tung cuon vat ly (VD: B001395821)
    private String title;
    private String author;
    private String genre;
    private int publicationYear;
    private String status;         // Trang thai cua RIENG cuon nay: AVAILABLE / BORROWED

    // Dung khi tao ban sao MOI (mac dinh AVAILABLE)
    public Book(String bookId, String serialNumber, String title, String author,
                String genre, int publicationYear) {
        this(bookId, serialNumber, title, author, genre, publicationYear, STATUS_AVAILABLE);
    }

    // Dung khi load tu file (da biet status)
    public Book(String bookId, String serialNumber, String title, String author,
                String genre, int publicationYear, String status) {
        this.bookId = bookId;
        this.serialNumber = serialNumber;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.publicationYear = publicationYear;
        this.status = status;
    }

    // ===== Getter =====
    public String getBookId()         { return bookId; }
    public String getSerialNumber()   { return serialNumber; }
    public String getTitle()          { return title; }
    public String getAuthor()         { return author; }
    public String getGenre()          { return genre; }
    public int getPublicationYear()   { return publicationYear; }
    public String getStatus()         { return status; }

    // ===== Setter co validate (dung khi cascade update theo bookId) =====
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

    // ===== Hanh vi cua chinh 1 ban sao vat ly =====
    public boolean isAvailable() {
        return status.equals(STATUS_AVAILABLE);
    }

    public void markBorrowed() {
        this.status = STATUS_BORROWED;
    }

    public void markAvailable() {
        this.status = STATUS_AVAILABLE;
    }

    public void displayBookInfo() {
        System.out.printf("%-6s | %-12s | %-28s | %-20s | %-12s | %4d | %-9s%n",
                bookId, serialNumber, title, author, genre, publicationYear, status);
    }
}
