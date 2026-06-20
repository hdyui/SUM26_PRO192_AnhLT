
import model.Member;
import model.PremiumMember;
import model.RegularMember;

public class Main {
    public static void main(String[] args) {
        Member[] members = {
            new RegularMember("M001", "Nguyen Van A", "0901234567", "a@gmail.com"), 
            new PremiumMember("M002", "Tran Thi B",  "0912345678", "b@gmail.com"),
            new RegularMember("M003", "Le Van C",    "0923456789", "c@gmail.com")
        };

        System.out.println("\n===== POLYMORPHISM DEMO: displayInfo() =====");
        for (Member m : members) {
            m.displayInfo(); 
            // Java tự gọi đúng version của từng class con là tính Dynamic dispatch
            System.out.println();
        }

        System.out.println("===== POLYMORPHISM DEMO: calculateFine() =====");
        int overdueDays = 5;
        for (Member m : members) {
            double fine = m.calculateFine(overdueDays); 
            System.out.printf("[%s] %s — Fine for %d days: %,.0f VND%n",
                    m.getMemberType(), m.getName(), overdueDays, fine);
        }

        System.out.println("\n===== BORROW LIMIT CHECK =====");
        for (Member m : members) {
            System.out.printf("[%s] %s — Limit: %d | Can borrow: %s%n",
                    m.getMemberType(), m.getName(),
                    m.getBorrowingLimit(), m.canBorrow());
        }
    }
}
