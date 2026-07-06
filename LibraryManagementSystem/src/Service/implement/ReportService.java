package service.implement;

import model.Book;
import model.Member;
import model.BorrowingTransaction;
import service.interfaces.IReportManagement;
import service.interfaces.IBookManagement;
import service.interfaces.IMemberManagement;
import service.interfaces.IBorrowingManagement;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ReportService implements IReportManagement {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private IBookManagement bookService;
    private IMemberManagement memberService;
    private IBorrowingManagement borrowingService;

    public ReportService(IBookManagement bookService,
                         IMemberManagement memberService,
                         IBorrowingManagement borrowingService) {
        this.bookService = bookService;
        this.memberService = memberService;
        this.borrowingService = borrowingService;
    }

    @Override
    public void generateCurrentlyBorrowedReport() {
        System.out.println("----------- CURRENTLY BORROWED BOOKS -----------");
        List<BorrowingTransaction> list = borrowingService.getCurrentlyBorrowedBooks();
        if (list.isEmpty()) {
            System.out.println("No book is currently borrowed.");
            return;
        }
        for (BorrowingTransaction tx : list) {
            tx.displayTransactionInfo();
        }
    }

    @Override
    public void generateOverdueReport(LocalDate checkDate) {
        System.out.println("----------- OVERDUE BOOKS (as of " + checkDate.format(FMT) + ") -----------");
        boolean found = false;
        for (BorrowingTransaction tx : borrowingService.getCurrentlyBorrowedBooks()) {
            if (tx.isOverdue(checkDate)) {
                int days = tx.calOverdue(checkDate);
                Book copy = bookService.getCopyBySerial(tx.getSerialNumber());
                Member member = memberService.getMemberById(tx.getMemberId());
                String title = (copy != null) ? copy.getTitle() : "?";
                String name = (member != null) ? member.getName() : "?";
                System.out.printf("%-12s | %-25s | %-14s | %-15s | due:%s | overdue:%d day(s)%n",
                        tx.getSerialNumber(), title, tx.getMemberId(), name,
                        tx.getDueDate().format(FMT), days);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No overdue books.");
        }
    }

    @Override
    public void generatePopularBooksReport() {
        System.out.println("----------- MOST POPULAR BOOKS -----------");
        // Khong con luu timesBorrowed tren Book -> tinh dong bang cach dem transaction
        // co cung bookId, de tranh du lieu bi lech giua Book va Transaction.
        List<String> ids = new ArrayList<>(bookService.getAllBookIds());
        List<Integer> counts = new ArrayList<>();
        for (String id : ids) {
            int cnt = 0;
            for (BorrowingTransaction tx : borrowingService.getAllTransactions()) {
                if (tx.getBookId().equalsIgnoreCase(id)) {
                    cnt++;
                }
            }
            counts.add(cnt);
        }

        // Sap xep song song (ids, counts) giam dan theo counts bang selection sort
        for (int i = 0; i < ids.size() - 1; i++) {
            int maxIndex = i;
            for (int j = i + 1; j < ids.size(); j++) {
                if (counts.get(j) > counts.get(maxIndex)) {
                    maxIndex = j;
                }
            }
            String tmpId = ids.get(i);
            ids.set(i, ids.get(maxIndex));
            ids.set(maxIndex, tmpId);

            int tmpCount = counts.get(i);
            counts.set(i, counts.get(maxIndex));
            counts.set(maxIndex, tmpCount);
        }

        for (int i = 0; i < ids.size(); i++) {
            List<Book> copies = bookService.getCopiesByBookId(ids.get(i));
            String title = copies.isEmpty() ? "?" : copies.get(0).getTitle();
            System.out.printf("%-6s | %-28s | times borrowed: %d%n", ids.get(i), title, counts.get(i));
        }
    }

    @Override
    public void generateTopMembersReport() {
        System.out.println("----------- MOST ACTIVE MEMBERS -----------");
        List<Member> members = new ArrayList<>(memberService.getAllMembers());
        for (int i = 0; i < members.size() - 1; i++) {
            int maxIndex = i;
            for (int j = i + 1; j < members.size(); j++) {
                if (members.get(j).getTotalBorrowings() > members.get(maxIndex).getTotalBorrowings()) {
                    maxIndex = j;
                }
            }
            Member tmp = members.get(i);
            members.set(i, members.get(maxIndex));
            members.set(maxIndex, tmp);
        }
        for (Member m : members) {
            System.out.printf("%-13s | %-18s | %-8s | total borrowings: %d%n",
                    m.getMemberId(), m.getName(), m.getMemberType(), m.getTotalBorrowings());
        }
    }
}
