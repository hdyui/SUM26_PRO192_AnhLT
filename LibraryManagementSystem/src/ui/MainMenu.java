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
import java.util.ArrayList;
import java.util.List;


public class MainMenu {
    private IBookManagement bookService;
    private IMemberManagement memberService;
    private IBorrowingManagement borrowingService;
    private IReportManagement reportService;
    
    
    // Thu tu khoi tao quan trong: book & member truoc, roi moi den borrowing
    // vi borrowing can doi chieu du lieu book/member ngay luc reconcile.
    public MainMenu() {
        this.bookService = new BookService();
        this.memberService = new MemberService();
        this.borrowingService = new BorrowingService(bookService, memberService);
        this.reportService = new ReportService(bookService, memberService, borrowingService);
    }
    
    
    //Menu tong hop
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
                case 5: running = false; System.out.println("Goodbye!"); break;
                default: System.out.println("Invalid option.");
            }
        }
    }
    
    
    //CAC THUOC TINH MENU CUA BOOK
    //Menu cua book
    private void bookMenu() {
        boolean back = false;
        while (!back) {
            System.out.println();
            System.out.println("----------- BOOK MANAGEMENT -----------");
            System.out.println("1. Add book");
            System.out.println("2. Update book");
            System.out.println("3. Remove book");
            System.out.println("4. View all books");
            System.out.println("5. Search books (by Book ID / Serial / keyword)");
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
        }
    }
    
    
    private void addBook() {
        System.out.println("----------- ADD BOOK -----------");
        String id = Input.readNonEmptyString("Book ID (Bxxx): ");
        if (!Validations.isValidId(id, "B")) {
            System.out.println("Invalid Book ID format (expected B + 3 digits).");
            return;
        }

        // Neu dau sach DA TON TAI -> chi hoi so luong ban muon them, KHONG hoi lai metadata.
        // Day chinh la cach tranh phai "Add Book" nhieu lan: chi 1 lan nhap quantity,
        // he thong tu chay vong lap sinh N serial ben trong addCopiesToExistingBook/addNewBookTitle.
        if (bookService.bookTitleExists(id)) {
            List<Book> existing = bookService.getCopiesByBookId(id);
            Book sample = existing.get(0);
            System.out.println("Book ID already exists: '" + sample.getTitle()
                    + "' (currently " + existing.size() + " copies, "
                    + bookService.countAvailable(id) + " available).");
            int qty = Input.readInt("Enter quantity of NEW copies to add: ");
            if (!Validations.isValidQuantity(qty)) {
                System.out.println("Quantity must be >= 1.");
                return;
            }
            if (bookService.addCopiesToExistingBook(id, qty)) {
                System.out.println("Added " + qty + " new copies to existing book '" + sample.getTitle() + "'.");
            } else {
                System.out.println("Failed to add copies.");
            }
            return;
        }

        // Dau sach hoan toan moi -> nhap day du metadata + quantity ban dau
        String title = Input.readNonEmptyString("Title: ");
        String author = Input.readNonEmptyString("Author: ");
        String genre = Input.readNonEmptyString("Genre: ");
        int year = Input.readInt("Publication Year: ");
        if (!Validations.isValidYear(year)) {
            System.out.println("Invalid year.");
            return;
        }
        int qty = Input.readInt("Quantity (number of copies to create): ");
        if (!Validations.isValidQuantity(qty)) {
            System.out.println("Quantity must be >= 1.");
            return;
        }

        if (bookService.addNewBookTitle(id, title, author, genre, year, qty)) {
            System.out.println("New book title '" + title + "' added with " + qty + " physical copies.");
        } else {
            System.out.println("Failed to add book.");
        }
    }
        
    
    
    private void updateBook() {
        System.out.println("----------- UPDATE BOOK -----------");
        String id = Input.readNonEmptyString("Enter Book ID: ");
        if (!bookService.bookTitleExists(id)) {
            System.out.println("Book not found.");
            return;
        }
        List<Book> copies = bookService.getCopiesByBookId(id);
        System.out.println("Current info (applies to all " + copies.size() + " copies of this title):");
        copies.get(0).displayBookInfo();

        String title = Input.readString("New Title (blank to skip): ");
        String author = Input.readString("New Author (blank to skip): ");
        String genre = Input.readString("New Genre (blank to skip): ");
        String yearRaw = Input.readString("New Publication Year (blank to skip): ");

        int year = -1;
        if (!yearRaw.isEmpty()) {
            try {
                year = Integer.parseInt(yearRaw);
            } catch (NumberFormatException e) {
                System.out.println("Invalid year, skipped.");
                year = -1;
            }
        }

        bookService.updateBookInfo(id,
                title.isEmpty() ? null : title,
                author.isEmpty() ? null : author,
                genre.isEmpty() ? null : genre,
                year);
        System.out.println("Book updated successfully.");
    }

    
    private void removeBook() {
        System.out.println("----------- REMOVE BOOK -----------");
        System.out.println("1. Remove ONE specific copy (by Serial Number)");
        System.out.println("2. Remove ENTIRE book title (all copies)");
        int c = Input.readInt("Choose: ");
        if (c == 1) {
            String serial = Input.readNonEmptyString("Enter Serial Number: ");
            if (bookService.removeCopy(serial)) {
                System.out.println("Copy " + serial + " removed successfully.");
            } else {
                System.out.println("Cannot remove: copy not found, or it is currently borrowed.");
            }
        } else if (c == 2) {
            String id = Input.readNonEmptyString("Enter Book ID: ");
            if (bookService.removeBookTitle(id)) {
                System.out.println("Book title " + id + " (all copies) removed successfully.");
            } else {
                System.out.println("Cannot remove: title not found, or some copies are currently borrowed.");
            }
        } else {
            System.out.println("Invalid option.");
        }
    }

    
//    private boolean isBookCurrentlyBorrowed(String bookId) {
//        for (BorrowingTransaction tx : borrowingService.getCurrentlyBorrowedBooks()) {
//            if (tx.getBookId().equalsIgnoreCase(bookId)) {
//                return true;
//            }
//        }
//        return false;
//    }

    
    private void viewAllBooks() {
        System.out.println("----------- BOOK LIST (grouped by title) -----------");
        List<String> ids = bookService.getAllBookIds();
        if (ids.isEmpty()) {
            System.out.println("No books.");
            return;
        }
        for (String id : ids) {
            List<Book> copies = bookService.getCopiesByBookId(id);
            Book sample = copies.get(0);
            System.out.printf("%n%s | %s | %s | %s | %d | total:%d | available:%d%n",
                    id, sample.getTitle(), sample.getAuthor(), sample.getGenre(),
                    sample.getPublicationYear(), copies.size(), bookService.countAvailable(id));
            for (Book b : copies) {
                System.out.println("    - Serial: " + b.getSerialNumber() + " | Status: " + b.getStatus());
            }
        }
    }

    
    // Ho tro 2 che do tim kiem theo yeu cau:
    // 1. Nhap Book ID (VD B001)      -> liet ke TOAN BO ban sao cua dau sach + trang thai.
    // 2. Nhap Serial Number chinh xac -> chi 1 ban sao, cross-check ai dang muon neu BORROWED.
    // 3. Fallback: neu khong khop ca 2 mode tren, tim theo tu khoa title/author/genre.
    private void searchBooks() {
        String kw = Input.readNonEmptyString("Enter Book ID / Serial Number / keyword: ");

        Book exactCopy = bookService.getCopyBySerial(kw);
        if (exactCopy != null) {
            System.out.println("Found 1 physical copy:");
            exactCopy.displayBookInfo();
            if (exactCopy.getStatus().equals(Book.STATUS_BORROWED)) {
                BorrowingTransaction tx = borrowingService.findActiveTransactionBySerial(exactCopy.getSerialNumber());
                if (tx != null) {
                    Member m = memberService.getMemberById(tx.getMemberId());
                    String name = (m != null) ? m.getName() : "?";
                    System.out.println("Currently borrowed by: " + tx.getMemberId() + " - " + name
                            + " | Due date: " + tx.getDueDate());
                }
            }
            return;
        }

        List<Book> copiesOfTitle = bookService.getCopiesByBookId(kw);
        if (!copiesOfTitle.isEmpty()) {
            System.out.println("Book title '" + kw + "' has " + copiesOfTitle.size() + " copy(ies):");
            for (Book b : copiesOfTitle) {
                b.displayBookInfo();
            }
            return;
        }

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
        // Member ID KHONG con nhap tay: sinh tu dong tu SDT theo dinh dang M[SDT]
        String phone = Input.readNonEmptyString("Phone number: ");
        if (!Validations.isValidPhone(phone)) {
            System.out.println("Invalid phone number.");
            return;
        }
        if (memberService.phoneExists(phone)) {
            System.out.println("This phone number is already registered to another member.");
            return;
        }

        String id = memberService.generateMemberId(phone);

        String name = Input.readNonEmptyString("Name: ");
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
            System.out.println("Member added successfully. Member ID: " + id);
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

        // Luu y: KHONG cho sua Phone tai day, vi Phone la nguon sinh ra Member ID
        // -> neu sua Phone se lam sai lech ID da sinh truoc do.
        String name = Input.readString("New Name (blank to skip): ");
        if (!name.isEmpty()) m.setName(name);
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
            System.out.println("1. Borrow book(s) - single receipt");
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

    
    // Luong muon NHIEU sach cung luc, chung 1 Receipt, chi nhap Member ID 1 lan.
    // Thu thu chi nhap Book ID (dau sach) cho tung cuon - KHONG nhap Serial Number thu cong,
    // he thong tu dong tim ban sao AVAILABLE dau tien va gan vao (auto-assign).
    private void doBorrow() {
        System.out.println("----------- BORROW BOOK(S) -----------");
        String memberId = Input.readNonEmptyString("Member ID: ");
        if (memberService.getMemberById(memberId) == null) {
            System.out.println("Member not found.");
            return;
        }

        int qty = Input.readInt("How many books do you want to borrow: ");
        if (qty < 1) {
            System.out.println("Quantity must be >= 1.");
            return;
        }

        List<String> bookIds = new ArrayList<>();
        for (int i = 1; i <= qty; i++) {
            String bookId = Input.readNonEmptyString("Enter Book ID for book #" + i + ": ");
            bookIds.add(bookId);
        }

        LocalDate borrowDate = Input.readDate("Borrow Date (dd/MM/yyyy): ");
        borrowingService.borrowBooks(memberId, bookIds, borrowDate);
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
        }
    }
    
}