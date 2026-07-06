package model;

public class RegularMember extends Member {

    public static final int BORROW_LIMIT = 3;     // gioi han 3 cuon (Guideline 3.4)
    public static final int FINE_PER_DAY = 5000;  // phat 5000 VND/ngay

    public RegularMember(String memberId, String name, String phone, String email) {
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
        return "REGULAR";
    }
}
