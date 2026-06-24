package model;

import java.time.LocalDate;

public class BorrowingTransaction {
    
    public static final String STATUS_BORROWING = "BORROWING";
    public static final String STATUS_RETURNED = "RETURNED";
    
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
        
        //Khong input vao ma sai logic: han tra là 14 ngay sau ngay muon
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
    //Method dung de check xem da qua han chua
    public boolean isOverdue(LocalDate checkDate) { //checkDate thong so truyen vao se la ngay check (hom nay)
        if(status.equals(STATUS_RETURNED)) {
            //Neu da tra thi logic check xem returnDate (ngayTra) co qua han voi dueDate (hanTra) hay khong
            return returnDate.isAfter(dueDate);
        } else {
            //Neu chua tra thi check xem ngay check (hom nay) co qua han luon chua
            return checkDate.isAfter(dueDate);
        }
    }
    
    
    //Method nay dung de danh dau sach da duoc tra va can thay doi mot so trang thai
    public void markReturned(LocalDate returnDate, double fineAmount) {
        this.returnDate = returnDate;
        this.fineAmount = fineAmount;
        this.status = STATUS_RETURNED;
    }
    
    public void getTransactionInfo() {
        System.out.println("====== TRANSACTION INFO ======");
        System.out.println("Transaction ID : " + transactionId);
        System.out.println("Book ID        : " + bookId);
        System.out.println("Member ID      : " + memberId);
        System.out.println("Borrow Date    : " + borrowDate);
        System.out.println("Due Date       : " + dueDate);
        System.out.println("Return Date    : " + returnDate);
        System.out.println("Fine Amount    : " + fineAmount);
        System.out.println("Status         : " + status);
    }
    
}
