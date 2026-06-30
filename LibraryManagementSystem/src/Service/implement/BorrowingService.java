package service.implement;

import model.BorrowingTransaction;
import model.Book;
import model.Member;
import service.interfaces.IBorrowingManagement;
import service.interfaces.IBookManagement;
import service.interfaces.IMemberManagement;
import utils.FileUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class BorrowingService implements IBorrowingManagement {

    //Danh sach luu tru giao dich
    private List<BorrowingTransaction> transactionList;
    
    //Cac service de doi chieu du lieu
    private IBookManagement bookService;
    private IMemberManagement memberService;
    
    private final String FILE_PATH = "transactions.txt";
    
    
    //Constructor de nap du lieu khoi dong chuong trinh
    public BorrowingService(IBookManagement bookService, IMemberManagement memberService) {
        this.bookService = bookService;
        this.memberService = memberService;
        this.transactionList = FileUtils.loadTransactions(FILE_PATH);
        reconcileCurrentBorrowed();

    }
    
    
    private void reconcileCurrentBorrowed() {
        for (Member m : memberService.getAllMembers()) {
            int active = 0;
            for (BorrowingTransaction tx : transactionList) {
                if (tx.getMemberId().equalsIgnoreCase(m.getMemberId())
                        && tx.getStatus().equals(BorrowingTransaction.STATUS_BORROWING)) {
                    active++;
                }
            }
            m.setCurrentBorrowed(active);
        }
    }
    
    
    //METHOD
    @Override
    public void borrowBook(String memberId, String bookId, LocalDate borrowDate) {
        //Check du lieu input
        if (memberId == null || memberId.trim().isEmpty()
                || bookId == null || bookId.trim().isEmpty()
                || borrowDate == null) {
            System.out.println("Error: Invalid input data!");
            return;
        }

        // Tim member va book xem co ton tai khong
        Member member = memberService.getMemberById(memberId); 
        Book book = bookService.getBookById(bookId);           

        if (member == null) {
            System.out.println("Error: No member with ID " + memberId);
            return;
        }
        if (book == null) {
            System.out.println("Error: No book with ID " + bookId);
            return;
        }

        // Check dieu kien muon sach
        if (!member.canBorrowBook()) {
            System.out.println("Error: Member has reached the book borrowing limit!");
            return;
        }
        if (!book.isAvailable()) {
            System.out.println("Error: This book is currently out of stock!");
            return;
        }

        // Neu duyet het thi create giao dich muon
        String transactionId = String.format("T%03d", transactionList.size() + 1); 
        BorrowingTransaction newTransaction = new BorrowingTransaction(transactionId, bookId, memberId, borrowDate);

        // Them vao list
        transactionList.add(newTransaction);

        //Update du lieu
        book.decreaseQuantity();
        book.increaseTimesBorrowed();
        member.borrowBook(); 

        //Luu du lieu
        bookService.save();
        memberService.save();
        FileUtils.saveTransactions(transactionList, FILE_PATH);

        System.out.println("SUCCESS: Book '" + book.getTitle()
                + "' borrowed by '" + member.getName() + "'.");
    }

    
    @Override
    public void returnBook(String memberId, String bookId, LocalDate returnDate) {
        if (memberId == null || memberId.trim().isEmpty()
                || bookId == null || bookId.trim().isEmpty()
                || returnDate == null) {
            System.out.println("Error: Invalid input data!");
            return;
        }

        BorrowingTransaction tx = findBorrowingTransaction(memberId, bookId);
        if (tx == null) {
            System.out.println("Error: No active borrowing transaction found for this data!");
            return;
        }

        if (returnDate.isBefore(tx.getBorrowDate())) {
            System.out.println("Error: Return date cannot be before borrow date!");
            return;
        }

        Book book = bookService.getBookById(bookId);
        Member member = memberService.getMemberById(memberId);
        if (book == null || member == null) {
            System.out.println("Error: System error, original data not found.");
            return;
        }

        int overdueDays = tx.calOverdue(returnDate);
        double fineAmount = overdueDays * member.getFinePerDay();

        tx.markReturned(returnDate, fineAmount);

        book.increaseQuantity();
        member.returnBook(); 

        bookService.save();
        memberService.save();
        FileUtils.saveTransactions(transactionList, FILE_PATH);

        if (fineAmount > 0) {
            System.out.printf("BOOK RETURNED: '%s' by '%s'. Overdue %d day(s). Fine: %,.0f VND.%n",
                    book.getTitle(), member.getName(), overdueDays, fineAmount);
        } else {
            System.out.printf("BOOK RETURNED: '%s' by '%s'. No overdue fine.%n",
                    book.getTitle(), member.getName());
        }
    }

    
    @Override
    public List<BorrowingTransaction> getCurrentlyBorrowedBooks() {
        List<BorrowingTransaction> result = new ArrayList<>();
        for (int i = 0; i < transactionList.size(); i++) {
            BorrowingTransaction tx = transactionList.get(i);
            
            if (tx.getStatus().equals(BorrowingTransaction.STATUS_BORROWING)) {
                result.add(tx);
            }
        }
        return result;
    }

    @Override
    public List<BorrowingTransaction> getBorrowingHistoryByMember(String memberId) {
        List<BorrowingTransaction> history = new ArrayList<>();
        for (int i = 0; i < transactionList.size(); i++) {
            BorrowingTransaction tx = transactionList.get(i);
            
            if (tx.getMemberId().equals(memberId)) {
                history.add(tx);
            }
        }
        return history;
    }
    
    @Override
    public List<BorrowingTransaction> getAllTransactions() {
        return transactionList;
    }
    
    
    // Tim giao dich DANG MUON khop member + book.
    // Bat buoc loc theo STATUS_BORROWING de tranh tim trung giao dich da tra cu.
    private BorrowingTransaction findBorrowingTransaction(String memberId, String bookId) {
        for (BorrowingTransaction tx : transactionList) {
            if (tx.getMemberId().equalsIgnoreCase(memberId)
                    && tx.getBookId().equalsIgnoreCase(bookId)
                    && tx.getStatus().equals(BorrowingTransaction.STATUS_BORROWING)) {
                return tx;
            }
        }
        return null;
    }
    
}
