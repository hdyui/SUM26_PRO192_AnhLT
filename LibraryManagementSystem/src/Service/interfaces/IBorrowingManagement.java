package service.interfaces;

import model.BorrowingTransaction;
import java.time.LocalDate;
import java.util.List;

public interface IBorrowingManagement {

    // Muon NHIEU sach cung luc cho 1 memberId, chung 1 receiptId tu sinh (Master-Detail).
    // Voi moi bookId trong danh sach, he thong tu tim serial AVAILABLE dau tien de gan.
    void borrowBooks(String memberId, List<String> bookIds, LocalDate borrowDate);

    void returnBook(String memberId, String bookId, LocalDate returnDate);

    // Tim giao dich DANG MUON (BORROWING) theo serialNumber -> dung cho search sach mode 2
    BorrowingTransaction findActiveTransactionBySerial(String serialNumber);

    List<BorrowingTransaction> getCurrentlyBorrowedBooks();

    List<BorrowingTransaction> getBorrowingHistoryByMember(String memberId);

    List<BorrowingTransaction> getAllTransactions();
}
