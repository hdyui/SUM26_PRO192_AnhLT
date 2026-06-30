package service.interfaces;

import model.BorrowingTransaction;
import java.util.List;
import java.time.LocalDate;

public interface IBorrowingManagement {
    
    //Method tao giao dich muon sach
    void borrowBook(String memberId, String bookId, LocalDate borrowDate);
    
    //Method tra sach
    void returnBook(String memberId, String bookId, LocalDate returnDate);
    
    //Xem danh sach dang duoc muon
    List<BorrowingTransaction> getCurrentlyBorrowedBooks();
    
    //Xem lich su muon sach cua mot thanh vien
    List<BorrowingTransaction> getBorrowingHistoryByMember(String memberId);
    
    //Lay tat ca du lieu cua hoa don
    List<BorrowingTransaction> getAllTransactions();
}
