package model;

public class PremiumMember extends Member {

    public static final int BORROW_LIMIT = 5;     // gioi han 5 cuon (Guideline 3.4)
    public static final int FINE_PER_DAY = 3000;  // phat 3000 VND/ngay (re hon Regular)

    public PremiumMember(String memberId, String name, String phone, String email) {
        super(memberId, name, phone, email);
    }

    @Override
    public int getBorrowingLimit() {
        return BORROW_LIMIT;
    }

    @Override
    public int getFinePerDay() {
        return FINE_PER_DAY;
    }

    @Override
    public String getMemberType() {
        return "PREMIUM";
    }
}