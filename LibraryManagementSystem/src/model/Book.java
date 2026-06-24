package model;

public class Book {
    private String bookId;
    private String title;
    private String author;
    private String genre;
    private int publicationYear;
    private int quantity;
    private String status;
    private int timesBorrowed;


    public Book(String bookId, String title, String author, String genre, int publicationYear, int quantity, String status, int timesBorrowed) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.publicationYear = publicationYear;
        this.quantity = quantity;
        this.status = status;
        this.timesBorrowed = 0;
        
        if (quantity > 0) {
            this.status = "AVAILABLE";
        } else {
            this.status = "BORROWED";
        }
        
    }

    public String getBookId() {
        return bookId;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getGenre() {
        return genre;
    }
    

    public int getPublicationYear() {
        return publicationYear;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getStatus() {
        return status;
    }

    public int getTimesBorrowed() {
        return timesBorrowed;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

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
        this.quantity = quantity;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setTimesBorrowed(int timesBorrowed) {
        this.timesBorrowed = timesBorrowed;
    }
    
    
    
    
}
