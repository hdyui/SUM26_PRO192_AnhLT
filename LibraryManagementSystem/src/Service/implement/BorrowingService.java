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
    
    private final String FILE_NAME = "transactions.txt";
    
    
    //Constructor de nap du lieu khoi dong chuong trinh
    public BorrowingService(IBookManagement bookService, IMemberManagement memberService) {
        this.bookService = bookService;
        this.memberService = memberService;
        this.transactionList = FileUtils.loadTransactions(FILE_NAME);
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
    
    // Sinh transactionId tang tien dua tren so luong giao dich hien co: T001, T002, ...
    private String generateTransactionId() {
        return String.format("T%03d", transactionList.size() + 1);
    }
    
    //METHOD
    @Override
    public void borrowBooks(String memberId, List<String> bookIds, LocalDate borrowDate) {
        if (memberId == null || memberId.trim().isEmpty()
                || bookIds == null || bookIds.isEmpty()
                || borrowDate == null) {
            System.out.println("Error: Invalid input data!");
            return;
        }

        Member member = memberService.getMemberById(memberId);
        if (member == null) {
            System.out.println("Error: No member with ID " + memberId);
            return;
        }

        // Sinh Receipt ID DUY NHAT cho CA LAN muon nay (gom nhom cac transaction nho)
        String receiptId = "REC" + System.currentTimeMillis();

        int successCount = 0;
        for (String bookId : bookIds) {
            if (bookId == null || bookId.trim().isEmpty()) {
                continue;
            }

            // Kiem tra gioi han moi luot, vi member co the vua dat gioi han
            // ngay giua vong lap (sau vai cuon da muon thanh cong o tren).
            if (!member.canBorrowBook()) {
                System.out.println("Error: Member " + memberId + " reached borrowing limit ("
                        + member.getBorrowingLimit() + "). Stop borrowing further books.");
                break;
            }

            // Thuat toan Auto-assign: tim ban sao AVAILABLE dau tien cua dau sach nay
            Book copy = bookService.findFirstAvailableCopy(bookId);
            if (copy == null) {
                System.out.println("Error: Book '" + bookId + "' is out of stock (no available copy). Skipped.");
                continue;
            }

            String transactionId = generateTransactionId();
            BorrowingTransaction tx = new BorrowingTransaction(
                    receiptId, transactionId, copy.getSerialNumber(), bookId, memberId, borrowDate);
            transactionList.add(tx);

            copy.markBorrowed();
            member.borrowBook();
            successCount++;

            System.out.println("SUCCESS: Borrowed '" + copy.getTitle()
                    + "' - Serial: " + copy.getSerialNumber());
        }

        bookService.save();
        memberService.save();
        FileUtils.saveTransactions(transactionList, FILE_NAME);

        System.out.println("Receipt " + receiptId + ": " + successCount + "/" + bookIds.size()
                + " book(s) borrowed successfully.");
    }

    
    @Override
    public void returnBook(String memberId, String bookId, LocalDate returnDate) {
        if (memberId == null || memberId.trim().isEmpty()
                || bookId == null || bookId.trim().isEmpty()
                || returnDate == null) {
            System.out.println("Error: Invalid input data!");
            return;
        }

        // Tim ban sao DAU TIEN dang duoc member nay muon khop bookId
        BorrowingTransaction tx = findBorrowingTransaction(memberId, bookId);
        if (tx == null) {
            System.out.println("Error: No active borrowing transaction found for this data!");
            return;
        }

        if (returnDate.isBefore(tx.getBorrowDate())) {
            System.out.println("Error: Return date cannot be before borrow date!");
            return;
        }

        Book copy = bookService.getCopyBySerial(tx.getSerialNumber());
        Member member = memberService.getMemberById(memberId);
        if (copy == null || member == null) {
            System.out.println("Error: System error, original data not found.");
            return;
        }

        int overdueDays = tx.calOverdue(returnDate);
        double fineAmount = overdueDays * member.getFinePerDay();

        tx.markReturned(returnDate, fineAmount);

        copy.markAvailable();
        member.returnBook();

        bookService.save();
        memberService.save();
        FileUtils.saveTransactions(transactionList, FILE_NAME);

        if (fineAmount > 0) {
            System.out.printf("BOOK RETURNED: '%s' (Serial: %s) by '%s'. Overdue %d day(s). Fine: %,.0f VND.%n",
                    copy.getTitle(), copy.getSerialNumber(), member.getName(), overdueDays, fineAmount);
        } else {
            System.out.printf("BOOK RETURNED: '%s' (Serial: %s) by '%s'. No overdue fine.%n",
                    copy.getTitle(), copy.getSerialNumber(), member.getName());
        }
    }
    
    
    @Override
    public BorrowingTransaction findActiveTransactionBySerial(String serialNumber) {
        for (BorrowingTransaction tx : transactionList) {
            if (tx.getSerialNumber().equalsIgnoreCase(serialNumber)
                    && tx.getStatus().equals(BorrowingTransaction.STATUS_BORROWING)) {
                return tx;
            }
        }
        return null;
    }

    
    @Override
    public List<BorrowingTransaction> getCurrentlyBorrowedBooks() {
        List<BorrowingTransaction> result = new ArrayList<>();
        for (BorrowingTransaction tx : transactionList) {
            if (tx.getStatus().equals(BorrowingTransaction.STATUS_BORROWING)) {
                result.add(tx);
            }
        }
        return result;
    }

    
    @Override
    public List<BorrowingTransaction> getBorrowingHistoryByMember(String memberId) {
        List<BorrowingTransaction> history = new ArrayList<>();
        for (BorrowingTransaction tx : transactionList) {
            if (tx.getMemberId().equalsIgnoreCase(memberId)) {
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
