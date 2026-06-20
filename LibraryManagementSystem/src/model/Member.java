package model;

import java.util.ArrayList;
import java.util.List;

public abstract class Member {

    private String memberId;
    private String name;
    private String phone;
    private String email;
    private String memberType;
    private List<Book> borrowedBooks;
    private List<Book> borrowingHistory;

    public Member(String memberId, String name, String phone, String email, String memberType) {
        this.memberId = memberId;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.memberType = memberType;
        this.borrowedBooks = new ArrayList<>();
        this.borrowingHistory = new ArrayList<>();
    }

    public abstract int getBorrowingLimit();
    public abstract double calculateFine(int overdueDays);

    public void displayInfo() {
        System.out.println("====== MEMBER INFO ======");
        System.out.println("ID          : " + memberId);
        System.out.println("Name        : " + name);
        System.out.println("Phone       : " + phone);
        System.out.println("Email       : " + email);
        System.out.println("Type        : " + getMemberType());
        System.out.println("Limit       : " + getBorrowingLimit() + " books");
        System.out.println("Borrowed    : " + borrowedBooks.size() + " book(s)");
    }

    public String getMemberId() {
        return memberId;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getMemberType() {
        return memberType;
    }

    public int getTotalBorrowing() {
        return borrowedBooks.size();
    }

    public List<Book> getBorrowedBookIds() {
        return borrowedBooks;
    }

    public void setName(String name) {
        if (name != null && !name.trim().isEmpty()) {
            this.name = name;
        }
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean canBorrow() {
        return borrowedBooks.size() < getBorrowingLimit();
    }

    public void borrowBook(Book book) {
        if (book != null) {
            borrowedBooks.add(book);
            borrowingHistory.add(book);
        }
    }

    public void returnBook(Book book) {
        if (book != null) {
            borrowedBooks.remove(book);
        }
    }
}
