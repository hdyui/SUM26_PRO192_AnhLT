package ui;

import model.Book;
import model.Member;
import model.RegularMember;
import model.PremiumMember;
import model.BorrowingTransaction;
import service.implement.BookService;
import service.implement.MemberService;
import service.implement.BorrowingService;
import service.implement.ReportService;
import service.interfaces.IBookManagement;
import service.interfaces.IMemberManagement;
import service.interfaces.IBorrowingManagement;
import service.interfaces.IReportManagement;
import utils.Input;
import utils.Validations;

import java.time.LocalDate;
import java.util.List;


public class MainMenu {
<<<<<<< HEAD
=======
    
    // Service layer (xử lý nghiệp vụ)
>>>>>>> Huy
    private IBookManagement bookService;
    private IMemberManagement memberService;
    private IBorrowingManagement borrowingService;
    private IReportManagement reportService;
<<<<<<< HEAD
    
    
    // Thu tu khoi tao quan trong: book & member truoc, roi moi den borrowing
    // vi borrowing can doi chieu du lieu book/member ngay luc reconcile.
    public MainMenu() {
=======

    public MainMenu() {
        // Khởi tạo service
>>>>>>> Huy
        this.bookService = new BookService();
        this.memberService = new MemberService();
        this.borrowingService = new BorrowingService(bookService, memberService);
        this.reportService = new ReportService(bookService, memberService, borrowingService);
    }
    
<<<<<<< HEAD
    
    //Menu tong hop
=======
    // MENU CHÍNH
>>>>>>> Huy
    public void run() {
        boolean running = true;
        while (running) {
            System.out.println();
            System.out.println("======================================");
            System.out.println("       LIBRARY MANAGEMENT SYSTEM");
            System.out.println("======================================");
            System.out.println("1. Manage Books");
            System.out.println("2. Manage Members");
            System.out.println("3. Borrowing / Returning");
            System.out.println("4. Reports");
            System.out.println("5. Exit");
            System.out.println("--------------------------------------");
            int choice = Input.readInt("Choose an option: ");
            switch (choice) {
                case 1: bookMenu(); break;
                case 2: memberMenu(); break;
                case 3: borrowingMenu(); break;
                case 4: reportMenu(); break;
<<<<<<< HEAD
                case 5: running = false; System.out.println("Goodbye, thanks for working!"); break;
                default: System.out.println("Invalid option.");
            }
        } 
    }
    
    
    //CAC THUOC TINH MENU CUA BOOK
    //Menu cua book
=======
                case 5: running = false; System.out.println("Goodbye!"); break;
                default: System.out.println("Invalid option.");
            }
        }
    }

    // ==================== BOOK ====================
>>>>>>> Huy
    private void bookMenu() {
        boolean back = false;
        while (!back) {
            System.out.println();
            System.out.println("----------- BOOK MANAGEMENT -----------");
            System.out.println("1. Add book");
            System.out.println("2. Update book");
            System.out.println("3. Remove book");
            System.out.println("4. View all books");
            System.out.println("5. Search books");
            System.out.println("0. Back");
            int c = Input.readInt("Choose: ");
            switch (c) {
                case 1: addBook(); break;
                case 2: updateBook(); break;
                case 3: removeBook(); break;
                case 4: viewAllBooks(); break;
                case 5: searchBooks(); break;
                case 0: back = true; break;
                default: System.out.println("Invalid option.");
            }
<<<<<<< HEAD
=======
        }
    }

    private void addBook() {
        System.out.println("----------- ADD BOOK -----------");
        String id = Input.readNonEmptyString("Book ID (Bxxx): ");
        // Check format ID
        if (!Validations.isValidId(id, "B")) {
            System.out.println("Invalid Book ID format (expected B + 3 digits).");
            return;
        }
        // Check trùng ID
        if (bookService.getBookById(id) != null) {
            System.out.println("Book ID already exists.");
            return;
        }
        String title = Input.readNonEmptyString("Title: ");
        String author = Input.readNonEmptyString("Author: ");
        String genre = Input.readNonEmptyString("Genre: ");
        int year = Input.readInt("Publication Year: ");
        if (!Validations.isValidYear(year)) {
            System.out.println("Invalid year.");
            return;
        }
        int qty = Input.readInt("Quantity: ");
        if (!Validations.isValidQuantity(qty)) {
            System.out.println("Quantity cannot be negative.");
            return;
        }
        Book book = new Book(id, title, author, genre, year, qty);
        if (bookService.addBook(book)) {
            System.out.println("Book added successfully.");
        } else {
            System.out.println("Failed to add book.");
        }
    }

    private void updateBook() {
        System.out.println("----------- UPDATE BOOK -----------");
        String id = Input.readNonEmptyString("Enter Book ID: ");
        Book book = bookService.getBookById(id);
        if (book == null) {
            System.out.println("Book not found.");
            return;
        }
        System.out.println("Current info:");
        book.displayBookInfo();

        // Bỏ trống để giữ nguyên giá trị cũ
        String title = Input.readString("New Title (blank to skip): ");
        if (!title.isEmpty()) book.setTitle(title);
        String author = Input.readString("New Author (blank to skip): ");
        if (!author.isEmpty()) book.setAuthor(author);
        String genre = Input.readString("New Genre (blank to skip): ");
        if (!genre.isEmpty()) book.setGenre(genre);
        String qtyRaw = Input.readString("New Quantity (blank to skip): ");
        if (!qtyRaw.isEmpty()) {
            try {
                book.setQuantity(Integer.parseInt(qtyRaw));
            } catch (NumberFormatException e) {
                System.out.println("Invalid quantity, skipped.");
            }
        }
        bookService.save();
        System.out.println("Book updated successfully.");
    }

    private void removeBook() {
        System.out.println("----------- REMOVE BOOK -----------");
        String id = Input.readNonEmptyString("Enter Book ID: ");
        Book book = bookService.getBookById(id);
        if (book == null) {
            System.out.println("Book not found.");
            return;
        }
        // Không cho xóa nếu đang được mượn
        if (isBookCurrentlyBorrowed(id)) {
            System.out.println("Cannot remove: this book is currently borrowed.");
            return;
        }
        if (bookService.removeBook(id)) {
            System.out.println("Book removed successfully.");
        } else {
            System.out.println("Failed to remove book.");
        }
    }
    
    // Kiểm tra sách đang được mượn
    private boolean isBookCurrentlyBorrowed(String bookId) {
        for (BorrowingTransaction tx : borrowingService.getCurrentlyBorrowedBooks()) {
            if (tx.getBookId().equalsIgnoreCase(bookId)) {
                return true;
            }
        }
        return false;
    }

    private void viewAllBooks() {
        System.out.println("----------- BOOK LIST -----------");
        List<Book> books = bookService.getAllBooks();
        if (books.isEmpty()) {
            System.out.println("No books.");
            return;
        }
        for (Book b : books) {
            b.displayBookInfo();
        }
    }

    private void searchBooks() {
        String kw = Input.readNonEmptyString("Search keyword: ");
        List<Book> result = bookService.searchBooks(kw);
        if (result.isEmpty()) {
            System.out.println("No book found.");
            return;
        }
        for (Book b : result) {
            b.displayBookInfo();
        }
    }

    // ==================== MEMBER ====================
    private void memberMenu() {
        boolean back = false;
        while (!back) {
            System.out.println();
            System.out.println("----------- MEMBER MANAGEMENT -----------");
            System.out.println("1. Add member");
            System.out.println("2. Update member");
            System.out.println("3. Remove member");
            System.out.println("4. View all members");
            System.out.println("5. Search members");
            System.out.println("0. Back");
            int c = Input.readInt("Choose: ");
            switch (c) {
                case 1: addMember(); break;
                case 2: updateMember(); break;
                case 3: removeMember(); break;
                case 4: viewAllMembers(); break;
                case 5: searchMembers(); break;
                case 0: back = true; break;
                default: System.out.println("Invalid option.");
            }
        }
    }

    private void addMember() {
        System.out.println("----------- ADD MEMBER -----------");
        String id = Input.readNonEmptyString("Member ID (Mxxx): ");
        // Check format ID
        if (!Validations.isValidId(id, "M")) {
            System.out.println("Invalid Member ID format (expected M + 3 digits).");
            return;
        }
        // Check trùng ID
        if (memberService.getMemberById(id) != null) {
            System.out.println("Member ID already exists.");
            return;
        }
        String name = Input.readNonEmptyString("Name: ");
        String phone = Input.readNonEmptyString("Phone: ");
        if (!Validations.isValidPhone(phone)) {
            System.out.println("Invalid phone number.");
            return;
        }
        String email = Input.readNonEmptyString("Email: ");
        if (!Validations.isValidEmail(email)) {
            System.out.println("Invalid email.");
            return;
        }
        System.out.println("Type: 1. REGULAR   2. PREMIUM");
        int t = Input.readInt("Choose type: ");
        Member member;
        if (t == 2) {
            member = new PremiumMember(id, name, phone, email);
        } else {
            member = new RegularMember(id, name, phone, email);
        }
        if (memberService.addMember(member)) {
            System.out.println("Member added successfully.");
        } else {
            System.out.println("Failed to add member.");
        }
    }

    private void updateMember() {
        System.out.println("----------- UPDATE MEMBER -----------");
        String id = Input.readNonEmptyString("Enter Member ID: ");
        Member m = memberService.getMemberById(id);
        if (m == null) {
            System.out.println("Member not found.");
            return;
        }
        System.out.println("Current info:");
        m.displayInfo();

        String name = Input.readString("New Name (blank to skip): ");
        if (!name.isEmpty()) m.setName(name);
        String phone = Input.readString("New Phone (blank to skip): ");
        if (!phone.isEmpty()) m.setPhone(phone);
        String email = Input.readString("New Email (blank to skip): ");
        if (!email.isEmpty()) m.setEmail(email);

        memberService.save();
        System.out.println("Member updated successfully.");
    }

    private void removeMember() {
        System.out.println("----------- REMOVE MEMBER -----------");
        String id = Input.readNonEmptyString("Enter Member ID: ");
        Member m = memberService.getMemberById(id);
        if (m == null) {
            System.out.println("Member not found.");
            return;
        }
        // Không cho xóa nếu còn đang mượn sách
        if (m.getCurrentBorrowed() > 0) {
            System.out.println("Cannot remove: member still has borrowed books.");
            return;
        }
        if (memberService.removeMember(id)) {
            System.out.println("Member removed successfully.");
        } else {
            System.out.println("Failed to remove member.");
        }
    }

    private void viewAllMembers() {
        System.out.println("----------- MEMBER LIST -----------");
        List<Member> members = memberService.getAllMembers();
        if (members.isEmpty()) {
            System.out.println("No members.");
            return;
        }
        for (Member m : members) {
            m.displayInfo();
        }
    }

    private void searchMembers() {
        String kw = Input.readNonEmptyString("Search keyword: ");
        List<Member> result = memberService.searchMembers(kw);
        if (result.isEmpty()) {
            System.out.println("No member found.");
            return;
        }
        for (Member m : result) {
            m.displayInfo();
        }
    }

    // ==================== BORROWING ====================
    private void borrowingMenu() {
        boolean back = false;
        while (!back) {
            System.out.println();
            System.out.println("----------- BORROWING / RETURNING -----------");
            System.out.println("1. Borrow a book");
            System.out.println("2. Return a book");
            System.out.println("3. View currently borrowed books");
            System.out.println("4. View borrowing history by member");
            System.out.println("0. Back");
            int c = Input.readInt("Choose: ");
            switch (c) {
                case 1: doBorrow(); break;
                case 2: doReturn(); break;
                case 3: viewCurrentlyBorrowed(); break;
                case 4: viewHistory(); break;
                case 0: back = true; break;
                default: System.out.println("Invalid option.");
            }
        }
    }

    private void doBorrow() {
        System.out.println("----------- BORROW BOOK -----------");
        String memberId = Input.readNonEmptyString("Member ID: ");
        String bookId = Input.readNonEmptyString("Book ID: ");
        LocalDate borrowDate = Input.readDate("Borrow Date (dd/MM/yyyy): ");
        // Gọi service xử lý logic mượn sách
        borrowingService.borrowBook(memberId, bookId, borrowDate);
    }

    private void doReturn() {
        System.out.println("----------- RETURN BOOK -----------");
        String memberId = Input.readNonEmptyString("Member ID: ");
        String bookId = Input.readNonEmptyString("Book ID: ");
        LocalDate returnDate = Input.readDate("Return Date (dd/MM/yyyy): ");
        // Gọi service xử lý trả sách
        borrowingService.returnBook(memberId, bookId, returnDate);
    }

    private void viewCurrentlyBorrowed() {
        System.out.println("----------- CURRENTLY BORROWED -----------");
        List<BorrowingTransaction> list = borrowingService.getCurrentlyBorrowedBooks();
        if (list.isEmpty()) {
            System.out.println("None.");
            return;
        }
        for (BorrowingTransaction tx : list) {
            tx.displayTransactionInfo();
        }
    }

    private void viewHistory() {
        String memberId = Input.readNonEmptyString("Member ID: ");
        List<BorrowingTransaction> history = borrowingService.getBorrowingHistoryByMember(memberId);
        if (history.isEmpty()) {
            System.out.println("No history.");
            return;
        }
        for (BorrowingTransaction tx : history) {
            tx.displayTransactionInfo();
        }
    }

    // ==================== REPORTS ====================
    private void reportMenu() {
        boolean back = false;
        while (!back) {
            System.out.println();
            System.out.println("----------- REPORTS -----------");
            System.out.println("1. Currently borrowed books");
            System.out.println("2. Overdue books");
            System.out.println("3. Most popular books");
            System.out.println("4. Most active members");
            System.out.println("0. Back");
            int c = Input.readInt("Choose: ");
            switch (c) {
                case 1:
                    reportService.generateCurrentlyBorrowedReport();
                    break;
                case 2:
                    LocalDate checkDate = Input.readDate("Check date (dd/MM/yyyy): ");
                    reportService.generateOverdueReport(checkDate);
                    break;
                case 3:
                    reportService.generatePopularBooksReport();
                    break;
                case 4:
                    reportService.generateTopMembersReport();
                    break;
                case 0:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid option.");
            }
>>>>>>> Huy
        }
    }
    
    
    private void addBook() {
        System.out.println("----------- ADD BOOK -----------");
        String id = Input.readNonEmptyString("Book ID (Bxxx): ");
        if (!Validations.isValidId(id, "B")) {
            System.out.println("Invalid Book ID format (expected B + 3 digits).");
            return;
        }
        if (bookService.getBookById(id) != null) {
            System.out.println("Book ID already exists.");
            return;
        }
        String title = Input.readNonEmptyString("Title: ");
        String author = Input.readNonEmptyString("Author: ");
        String genre = Input.readNonEmptyString("Genre: ");
        int year = Input.readInt("Publication Year: ");
        if (!Validations.isValidYear(year)) {
            System.out.println("Invalid year.");
            return;
        }
        int qty = Input.readInt("Quantity: ");
        if (!Validations.isValidQuantity(qty)) {
            System.out.println("Quantity cannot be negative.");
            return;
        }
        Book book = new Book(id, title, author, genre, year, qty);
        if (bookService.addBook(book)) {
            System.out.println("Book added successfully.");
        } else {
            System.out.println("Failed to add book.");
        }
    }
    
    
    private void updateBook() {
        System.out.println("----------- UPDATE BOOK -----------");
        String id = Input.readNonEmptyString("Enter Book ID: ");
        Book book = bookService.getBookById(id);
        if (book == null) {
            System.out.println("Book not found.");
            return;
        }
        System.out.println("Current info:");
        book.displayBookInfo();

        // Bo trong de giu nguyen gia tri cu
        String title = Input.readString("New Title (blank to skip): ");
        if (!title.isEmpty()) book.setTitle(title);
        String author = Input.readString("New Author (blank to skip): ");
        if (!author.isEmpty()) book.setAuthor(author);
        String genre = Input.readString("New Genre (blank to skip): ");
        if (!genre.isEmpty()) book.setGenre(genre);
        String qtyRaw = Input.readString("New Quantity (blank to skip): ");
        if (!qtyRaw.isEmpty()) {
            try {
                book.setQuantity(Integer.parseInt(qtyRaw));
            } catch (NumberFormatException e) {
                System.out.println("Invalid quantity, skipped.");
            }
        }
        bookService.save();
        System.out.println("Book updated successfully.");
    }

    
    private void removeBook() {
        System.out.println("----------- REMOVE BOOK -----------");
        String id = Input.readNonEmptyString("Enter Book ID: ");
        Book book = bookService.getBookById(id);
        if (book == null) {
            System.out.println("Book not found.");
            return;
        }
        // BR: chi xoa khi khong co ban nao dang bi muon
        if (isBookCurrentlyBorrowed(id)) {
            System.out.println("Cannot remove: this book is currently borrowed.");
            return;
        }
        if (bookService.removeBook(id)) {
            System.out.println("Book removed successfully.");
        } else {
            System.out.println("Failed to remove book.");
        }
    }

    
    private boolean isBookCurrentlyBorrowed(String bookId) {
        for (BorrowingTransaction tx : borrowingService.getCurrentlyBorrowedBooks()) {
            if (tx.getBookId().equalsIgnoreCase(bookId)) {
                return true;
            }
        }
        return false;
    }

    
    private void viewAllBooks() {
        System.out.println("----------- BOOK LIST -----------");
        List<Book> books = bookService.getAllBooks();
        if (books.isEmpty()) {
            System.out.println("No books.");
            return;
        }
        for (Book b : books) {
            b.displayBookInfo();
        }
    }

    
    private void searchBooks() {
        String kw = Input.readNonEmptyString("Search keyword: ");
        List<Book> result = bookService.searchBooks(kw);
        if (result.isEmpty()) {
            System.out.println("No book found.");
            return;
        }
        for (Book b : result) {
            b.displayBookInfo();
        }
    }
    
    
    //CAC THUOC TINH MENU CUA MEMBER
    //Menu cua Member
    private void memberMenu() {
        boolean back = false;
        while (!back) {
            System.out.println();
            System.out.println("----------- MEMBER MANAGEMENT -----------");
            System.out.println("1. Add member");
            System.out.println("2. Update member");
            System.out.println("3. Remove member");
            System.out.println("4. View all members");
            System.out.println("5. Search members");
            System.out.println("0. Back");
            int c = Input.readInt("Choose: ");
            switch (c) {
                case 1: addMember(); break;
                case 2: updateMember(); break;
                case 3: removeMember(); break;
                case 4: viewAllMembers(); break;
                case 5: searchMembers(); break;
                case 0: back = true; break;
                default: System.out.println("Invalid option.");
            }
        }
    }

    
    private void addMember() {
        System.out.println("----------- ADD MEMBER -----------");
        String id = Input.readNonEmptyString("Member ID (Mxxx): ");
        if (!Validations.isValidId(id, "M")) {
            System.out.println("Invalid Member ID format (expected M + 3 digits).");
            return;
        }
        if (memberService.getMemberById(id) != null) {
            System.out.println("Member ID already exists.");
            return;
        }
        String name = Input.readNonEmptyString("Name: ");
        String phone = Input.readNonEmptyString("Phone: ");
        if (!Validations.isValidPhone(phone)) {
            System.out.println("Invalid phone number.");
            return;
        }
        String email = Input.readNonEmptyString("Email: ");
        if (!Validations.isValidEmail(email)) {
            System.out.println("Invalid email.");
            return;
        }
        System.out.println("Type: 1. REGULAR   2. PREMIUM");
        int t = Input.readInt("Choose type: ");
        Member member;
        if (t == 2) {
            member = new PremiumMember(id, name, phone, email);
        } else {
            member = new RegularMember(id, name, phone, email);
        }
        if (memberService.addMember(member)) {
            System.out.println("Member added successfully.");
        } else {
            System.out.println("Failed to add member.");
        }
    }

    
    private void updateMember() {
        System.out.println("----------- UPDATE MEMBER -----------");
        String id = Input.readNonEmptyString("Enter Member ID: ");
        Member m = memberService.getMemberById(id);
        if (m == null) {
            System.out.println("Member not found.");
            return;
        }
        System.out.println("Current info:");
        m.displayInfo();

        String name = Input.readString("New Name (blank to skip): ");
        if (!name.isEmpty()) m.setName(name);
        String phone = Input.readString("New Phone (blank to skip): ");
        if (!phone.isEmpty()) m.setPhone(phone);
        String email = Input.readString("New Email (blank to skip): ");
        if (!email.isEmpty()) m.setEmail(email);

        memberService.save();
        System.out.println("Member updated successfully.");
    }

    
    private void removeMember() {
        System.out.println("----------- REMOVE MEMBER -----------");
        String id = Input.readNonEmptyString("Enter Member ID: ");
        Member m = memberService.getMemberById(id);
        if (m == null) {
            System.out.println("Member not found.");
            return;
        }
        if (m.getCurrentBorrowed() > 0) {
            System.out.println("Cannot remove: member still has borrowed books.");
            return;
        }
        if (memberService.removeMember(id)) {
            System.out.println("Member removed successfully.");
        } else {
            System.out.println("Failed to remove member.");
        }
    }

    
    private void viewAllMembers() {
        System.out.println("----------- MEMBER LIST -----------");
        List<Member> members = memberService.getAllMembers();
        if (members.isEmpty()) {
            System.out.println("No members.");
            return;
        }
        for (Member m : members) {
            m.displayInfo();
        }
    }

    
    private void searchMembers() {
        String kw = Input.readNonEmptyString("Search keyword: ");
        List<Member> result = memberService.searchMembers(kw);
        if (result.isEmpty()) {
            System.out.println("No member found.");
            return;
        }
        for (Member m : result) {
            m.displayInfo();
        }
    }
    
    
    //CAC THUOC TINH MENU CUA BORROWING
    //Menu cua Borrowing
    private void borrowingMenu() {
        boolean back = false;
        while (!back) {
            System.out.println();
            System.out.println("----------- BORROWING / RETURNING -----------");
            System.out.println("1. Borrow a book");
            System.out.println("2. Return a book");
            System.out.println("3. View currently borrowed books");
            System.out.println("4. View borrowing history by member");
            System.out.println("0. Back");
            int c = Input.readInt("Choose: ");
            switch (c) {
                case 1: doBorrow(); break;
                case 2: doReturn(); break;
                case 3: viewCurrentlyBorrowed(); break;
                case 4: viewHistory(); break;
                case 0: back = true; break;
                default: System.out.println("Invalid option.");
            }
        }
    }

    
    private void doBorrow() {
        System.out.println("----------- BORROW BOOK -----------");
        String memberId = Input.readNonEmptyString("Member ID: ");
        String bookId = Input.readNonEmptyString("Book ID: ");
        LocalDate borrowDate = Input.readDate("Borrow Date (dd/MM/yyyy): ");
        borrowingService.borrowBook(memberId, bookId, borrowDate);
    }

    
    private void doReturn() {
        System.out.println("----------- RETURN BOOK -----------");
        String memberId = Input.readNonEmptyString("Member ID: ");
        String bookId = Input.readNonEmptyString("Book ID: ");
        LocalDate returnDate = Input.readDate("Return Date (dd/MM/yyyy): ");
        borrowingService.returnBook(memberId, bookId, returnDate);
    }

    
    private void viewCurrentlyBorrowed() {
        System.out.println("----------- CURRENTLY BORROWED -----------");
        List<BorrowingTransaction> list = borrowingService.getCurrentlyBorrowedBooks();
        if (list.isEmpty()) {
            System.out.println("None.");
            return;
        }
        for (BorrowingTransaction tx : list) {
            tx.displayTransactionInfo();
        }
    }

    
    private void viewHistory() {
        String memberId = Input.readNonEmptyString("Member ID: ");
        List<BorrowingTransaction> history = borrowingService.getBorrowingHistoryByMember(memberId);
        if (history.isEmpty()) {
            System.out.println("No history.");
            return;
        }
        for (BorrowingTransaction tx : history) {
            tx.displayTransactionInfo();
        }
    }
    
    
    //MENU BAO CAO CAC SO LIEU QUAN TRONG
    public void reportMenu() {
        boolean back = false;
        while (!back) {
            System.out.println();
            System.out.println("----------- REPORTS -----------");
            System.out.println("1. Currently borrowed books");
            System.out.println("2. Overdue books");
            System.out.println("3. Most popular books");
            System.out.println("4. Most active members");
            System.out.println("0. Back");
            int c = Input.readInt("Choose: ");
            switch (c) {
                case 1:
                    reportService.generateCurrentlyBorrowedReport();
                    break;
                case 2:
                    LocalDate checkDate = Input.readDate("Check date (dd/MM/yyyy): ");
                    reportService.generateOverdueReport(checkDate);
                    break;
                case 3:
                    reportService.generatePopularBooksReport();
                    break;
                case 4:
                    reportService.generateTopMembersReport();
                    break;
                case 0:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }
    
}