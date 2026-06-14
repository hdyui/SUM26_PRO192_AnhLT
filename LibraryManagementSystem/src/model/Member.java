package model;

import java.util.ArrayList;
import java.util.List;

public abstract class Member {

    private String memberId;
    private String name;
    private String phone;
    private String email;
    private List<String> borrowedBookIds;


    public Member(String memberId, String name, String phone, String email) {
        this.memberId       = memberId;
        this.name           = name;
        this.phone          = phone;
        this.email          = email;
        this.borrowedBookIds = new ArrayList<>();
    }

    public abstract int getBorrowingLimit();
    public abstract double calculateFine(int overdueDays);
    public abstract String getMemberType();

    // ===== Method được override ở child để demo đa hình =====
    public void displayInfo() {
        System.out.println("====== MEMBER INFO ======");
        System.out.println("ID       : " + memberId);
        System.out.println("Name     : " + name);
        System.out.println("Phone    : " + phone);
        System.out.println("Email    : " + email);
        System.out.println("Type     : " + getMemberType());
        System.out.println("Limit    : " + getBorrowingLimit() + " books");
        System.out.println("Borrowed : " + borrowedBookIds.size() + " book(s)");
    }

    // ===== Getters & Setters =====
    public String getMemberId() { return memberId; }
    public String getName()     { return name; }
    public String getPhone()    { return phone; }
    public String getEmail()    { return email; }
    public List<String> getBorrowedBookIds() { return borrowedBookIds; }

    public void setName(String name) {
        if (name != null && !name.trim().isEmpty()) this.name = name;
    }
    public void setPhone(String phone) { this.phone = phone; }
    public void setEmail(String email) { this.email = email; }

    public boolean canBorrow() {
        return borrowedBookIds.size() < getBorrowingLimit();
    }

    public void borrowBook(String bookId)  { borrowedBookIds.add(bookId); }
    public void returnBook(String bookId)  { borrowedBookIds.remove(bookId); }
}
