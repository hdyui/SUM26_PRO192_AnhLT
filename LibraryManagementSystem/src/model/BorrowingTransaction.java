package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class BorrowingTransaction {
    
    public static final String STATUS_BORROWING = "BORROWING";
    public static final String STATUS_RETURNED = "RETURNED";\
    public static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    
    private String transactionId;
    private String bookId;
    private String memberId;
    
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    
    private double fineAmount;
    private String status;

    public BorrowingTransaction(String transactionId, String bookId, String memberId, LocalDate borrowDate) {
        this.transactionId = transactionId;
        this.bookId = bookId;
        this.memberId = memberId;
        this.borrowDate = borrowDate;
        
        //Khong input vao ma dung logic: han tra là 14 ngay sau ngay muon
        this.dueDate = borrowDate.plusDays(14);
        
        
        //Gia tri mac dinh khi moi muon
        this.returnDate = null;
        this.fineAmount = 0.0;
        this.status = STATUS_BORROWING;
    }

    
    //All Getter
    public String getTransactionId()    {return transactionId;}
    public String getBookId()           {return bookId;}
    public String getMemberId()         {return memberId;}
    public LocalDate getBorrowDate()    {return borrowDate;}
    public LocalDate getDueDate()       {return dueDate;}
    public LocalDate getReturnDate()    {return returnDate;}
    public double getFineAmount()       {return fineAmount;}
    public String getStatus()           {return status;}
    

    //Methods Basic in Model
    //Method dung de check xem da qua han chua (tra ve true - false)
    public boolean isOverdue(LocalDate checkDate) {     //checkDate thong so truyen vao se la ngay check (co the la hom nay)
        if(status.equals(STATUS_RETURNED)) {
            return returnDate.isAfter(dueDate);         //Neu da tra thi logic check xem returnDate (ngayTra) co qua han voi dueDate (hanTra) hay khong
        } else {
            return checkDate.isAfter(dueDate);          //Neu chua tra thi check xem ngay check (hom nay) co qua han luon chua
        }
    }
    
    //Tinh so ngay qua han -> Dung cho tinh tien phat
    public int calOverdue(LocalDate checkDate) {
        if(!isOverdue(checkDate)) {
            return 0;
        } else {
            LocalDate end = status.equals(STATUS_RETURNED) ? returnDate : checkDate; // Da tra thi dem den ngay tra, chua tra thi dem den ngay check
            return (int) ChronoUnit.DAYS.between(dueDate, end);
        }
    }
    
    //Method nay dung de danh dau giao dich da tra va cap nhat trang thai
    public void markReturned(LocalDate returnDate, double fineAmount) {
        this.returnDate = returnDate;
        this.fineAmount = fineAmount;
        this.status = STATUS_RETURNED;
    }
    
    public void displayTransactionInfo() {
        String returnStr;
        if (returnDate == null) {
            returnStr = "null"; // Neu chua tra sach thi ngay tra la rong = null
        } else {
            returnStr = "returnDate.format(FMT)"; // Neu da tra thi gan chuan format dd/MM/yyyy
        }

        System.out.printf("%-5s | %-5s | %-5s | %s | %s | %-10s | %,.0f VND | %s%n",
                transactionId, bookId, memberId,
                borrowDate.format(FMT), dueDate.format(FMT), returnStr,
                fineAmount, status);
    }
    
}
