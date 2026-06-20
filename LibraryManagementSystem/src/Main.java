import model.Member;
import model.PremiumMember;
import model.RegularMember;
import utils.FileUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Member> members = new ArrayList<>();

        try {
            if (!FileUtils.fileExists("members.txt")) {
                FileUtils.generateSampleMembersFile();
                System.out.println("Generated sample members.txt in src/resources.");
            }
            members = FileUtils.loadMembers();
        } catch (IOException e) {
            System.err.println("Failed to load members from file: " + e.getMessage());
        }

        if (members.isEmpty()) {
            members.add(new RegularMember("M001", "Nguyen Van A", "0901234567", "a@gmail.com"));
            members.add(new PremiumMember("M002", "Tran Thi B", "0912345678", "b@gmail.com"));
            members.add(new RegularMember("M003", "Le Van C", "0923456789", "c@gmail.com"));
            System.out.println("Using fallback member list.");
        }

        System.out.println("\n===== MEMBERS LOADED =====");
        for (Member m : members) {
            System.out.printf("%s | %s | %s | %s%n", m.getMemberId(), m.getName(), m.getPhone(), m.getMemberType());
        }

        System.out.println("\n===== POLYMORPHISM DEMO: displayInfo() =====");
        for (Member m : members) {
            m.displayInfo();
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
