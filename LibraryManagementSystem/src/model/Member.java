package model;

public abstract class Member {

    protected String memberId;
    protected String name;
    protected String phone;
    protected String email;

    // So sach DANG muon (chi giu trong bo nho, dung de check gioi han)
    protected int currentBorrowed;
    
    // Tong so luot da muon (luu file, dung cho report thanh vien tich cuc)
    protected int totalBorrowings;

    public Member(String memberId, String name, String phone, String email) {
        this.memberId = memberId;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.currentBorrowed = 0;
        this.totalBorrowings = 0;
    }

    // ===== Getter =====
    public String getMemberId()     { return memberId; }
    public String getName()         { return name; }
    public String getPhone()        { return phone; }
    public String getEmail()        { return email; }
    public int getCurrentBorrowed() { return currentBorrowed; }
    public int getTotalBorrowings() { return totalBorrowings; }

    // ===== Setter co validate =====
    public void setName(String name) {
        if (name != null && !name.trim().isEmpty()) {
            this.name = name;
        }
    }

    public void setPhone(String phone) {
        if (phone != null && !phone.trim().isEmpty()) {
            this.phone = phone;
        }
    }

    public void setEmail(String email) {
        if (email != null && !email.trim().isEmpty()) {
            this.email = email;
        }
    }

    // Dung khi load du lieu tu file
    public void setTotalBorrowings(int totalBorrowings) {
        if (totalBorrowings >= 0) {
            this.totalBorrowings = totalBorrowings;
        }
    }

    // Dung khi load: khoi phuc so sach dang muon tu cac giao dich BORROWING
    public void setCurrentBorrowed(int currentBorrowed) {
        if (currentBorrowed >= 0) {
            this.currentBorrowed = currentBorrowed;
        }
    }

    // ===== Hanh vi CHUNG cho moi loai member =====
    public boolean canBorrowBook() {
        // goi getBorrowingLimit() abstract -> tuy loai member ma gioi han khac nhau
        return currentBorrowed < getBorrowingLimit();
    }

    public void borrowBook() {
        currentBorrowed++;
        totalBorrowings++;
    }

    public void returnBook() {
        if (currentBorrowed > 0) {
            currentBorrowed--;
        }
    }

    // ===== Hanh vi KHAC NHAU theo loai -> abstract =====
    public abstract int getBorrowingLimit();
    public abstract int getFinePerDay();
    public abstract String getMemberType();

    // Dung chung nhung goi cac method abstract -> the hien dynamic dispatch
    public void displayInfo() {
        System.out.printf("%-6s | %-18s | %-12s | %-22s | %-8s | limit:%d | borrowing:%d | total:%d%n",
                memberId, name, phone, email, getMemberType(),
                getBorrowingLimit(), currentBorrowed, totalBorrowings);
    }
}