package model;

public class PremiumMember extends Member {

    private static final int    BORROW_LIMIT  = 5;
    private static final double FINE_PER_DAY  = 3000.0;
    
    public PremiumMember(String memberId, String name, String phone, String email) {
        super(memberId, name, phone, email, "Premium");
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
        System.out.println("Fine rate: 3,000 VND/day (discounted)");
        System.out.println("=========================");
    }
}
