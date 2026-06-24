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
    }
    
    
    //METHOD
    @Override
    public void borrowBook(String memberId, String bookId, LocalDate borrowDate) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void returnBook(String memberId, String bookId, LocalDate returnDate) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<BorrowingTransaction> getCurrentlyBorrowedBooks() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<BorrowingTransaction> getBorrowingHistoryByMember(String memberId) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    
    
}
