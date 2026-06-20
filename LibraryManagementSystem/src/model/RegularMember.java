package model;

public class RegularMember extends Member {

    private static final int    BORROW_LIMIT  = 3;
    private static final double FINE_PER_DAY  = 5000.0; 

    public RegularMember(String memberId, String name, String phone, String email) {
        super(memberId, name, phone, email, "Regular");
    }

    @Override
    public int getBorrowingLimit() {
        return BORROW_LIMIT;
    }

    @Override
    public double calculateFine(int overdueDays) {
        if (overdueDays <= 0) return 0;
        return overdueDays * FINE_PER_DAY;
    }


    @Override
    public void displayInfo() {
        super.displayInfo(); // gọi method cha
        System.out.println("Fine rate: 5,000 VND/day");
        System.out.println("=========================");
    }
}
