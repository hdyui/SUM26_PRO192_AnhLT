package service.implement;

import model.BorrowingTransaction;
import model.Book;
import model.Member;
import service.interfaces.IBorrowingManagement;
import service.interfaces.IBookManagement;
import service.interfaces.IMemberManagement;
import utils.FileUtils;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;


public class BorrowingService implements IBorrowingManagement {

    //Danh sach luu tru giao dich
    private List<BorrowingTransaction> transactionList;
    
    //Cac service de doi chieu du lieu
    private IBookManagement bookService;
    private IMemberManagement memberService;
    
    private final String FILE_PATH = "resources/transactions.txt";
    
    
    //Constructor de nap du lieu khoi dong chuong trinh
    public BorrowingService(IBookManagement bookService, IMemberManagement memberService) {
        this.bookService = bookService;
        this.memberService = memberService;
        
        // Đọc dữ liệu từ file khi khởi động chương trình
        try {
            this.transactionList = FileUtils.loadTransactionsFromFile(FILE_PATH);
        } catch (IOException ex) {
            System.out.println("Failed to load transactions: " + ex.getMessage());
            this.transactionList = new ArrayList<>();
        }
        if (this.transactionList == null) {
            this.transactionList = new ArrayList<>();
        }

//        this.transactionList = new ArrayList<>(); //Tam thoi dung de test voi data gia, sau se xoa
    }
    
    
    //METHOD
    @Override
    public void borrowBook(String memberId, String bookId, LocalDate borrowDate) {
        //Check du lieu input
        if (memberId == null || memberId.trim().isEmpty() || 
            bookId == null || bookId.trim().isEmpty() || 
            borrowDate == null) {
            System.out.println("Error: Invalid input data!");
            return;
        }

        // Tim member va book xem co ton tai khong
        Member member = memberService.getMemberById(memberId); 
        Book book = bookService.getBookById(bookId);           

        if (member == null) {
            System.out.println("Error: No member with " + memberId);
            return;
        }
        if (book == null) {
            System.out.println("Error: No book with " + bookId);
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
        FileUtils.saveTransactionsToFile(this.transactionList, FILE_PATH);

        System.out.println("\n---------------------------------------------------");
        System.out.println("SUCCESS: Book borrowing transaction created!");
        System.out.println("---------------------------------------------------\n");
    }

    
    @Override
    public void returnBook(String memberId, String bookId, LocalDate returnDate) {
        if (memberId == null || memberId.trim().isEmpty() || 
            bookId == null || bookId.trim().isEmpty() || 
            returnDate == null) {
            System.out.println("Error: Invalid input data!");
            return;
        }

        BorrowingTransaction currentTx = findBorrowingTransaction(memberId, bookId);
        if (currentTx == null) {
            System.out.println("Error: No borrowing transaction matching this data was found!");
            return;
        }

        if (returnDate.isBefore(currentTx.getBorrowDate())) {
            System.out.println("Error: The book return date cannot be before the borrowing date!");
            return;
        }

        Book book = bookService.getBookById(bookId);
        Member member = memberService.getMemberById(memberId);
        if (book == null || member == null) {
            System.out.println("Error: System error, original data not found.");
            return;
        }

        long overdueDays = currentTx.calOverdue(returnDate);
        double fineAmount = FileUtils.calculateFine(overdueDays);

        currentTx.markReturned(returnDate, fineAmount);

        book.increaseQuantity();
        member.returnBook(); 

        FileUtils.saveTransactionsToFile(this.transactionList, FILE_PATH);

        System.out.println("\n---------------------------------------------------");
        if (fineAmount > 0) {
            System.out.println("BOOK RETURNED: Book '" + book.getTitle() + "' returned by '" + member.getName() + "'.");
            System.out.println("OVERDUE      : " + overdueDays + " ngày.");
            System.out.println("FINE         : " + String.format("%,.0f", fineAmount) + " VND");
        } else {
            System.out.println("BOOK RETURNED: Book '" + book.getTitle() + "' returned by '" + member.getName() + "'.");
            System.out.println("There are no late payment penalties.");
        }
        System.out.println("---------------------------------------------------\n");
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
    
    
    private BorrowingTransaction findBorrowingTransaction(String memberId, String bookId) {
    for (BorrowingTransaction tx : transactionList) {
        if (tx.getMemberId().equals(memberId)
                && tx.getBookId().equals(bookId)
                && tx.getStatus().equals(BorrowingTransaction.STATUS_BORROWING)) { // bắt buộc
            return tx;
        }
    }
    return null;
}
    
}
