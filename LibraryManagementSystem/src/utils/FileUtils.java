package utils;

import model.Book;
import model.Member;
import model.RegularMember;
import model.PremiumMember;
import model.BorrowingTransaction;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {

    public static final String RESOURCE_FOLDER = "src/resources";
    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private static final String SPLIT_REGEX = "\\|";
    private static final String JOIN_SEP = "|";

    // ===== Cac ham doc/ghi co ban =====
    private static Path resolvePath(String fileName) {
        return Paths.get(RESOURCE_FOLDER, fileName);
    }

    private static List<String> readLines(String fileName) {
        Path path = resolvePath(fileName);
        try {
            if (Files.notExists(path)) {
                return new ArrayList<>();
            }
            return Files.readAllLines(path, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("Error reading file: " + fileName);
            return new ArrayList<>();
        }
    }

    private static void writeLines(String fileName, List<String> lines) {
        Path path = resolvePath(fileName);
        try {
            if (path.getParent() != null) {
                Files.createDirectories(path.getParent());
            }
            Files.write(path, lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("Error writing file: " + fileName);
        }
    }

    // ===== BOOK =====
    // Format: bookId|serialNumber|title|author|genre|publicationYear|status
    // (Khong con quantity/timesBorrowed: so luong = dem so dong cung bookId,
    //  timesBorrowed tinh dong tu transactions.txt)
    public static List<Book> loadBooks(String fileName) {
        List<Book> books = new ArrayList<>();
        for (String line : readLines(fileName)) {
            if (line.trim().isEmpty() || line.startsWith("#")) {
                continue;
            }
            String[] p = line.split(SPLIT_REGEX);
            if (p.length < 7) {
                continue;
            }
            try {
                String bookId = p[0].trim();
                String serialNumber = p[1].trim();
                String title = p[2].trim();
                String author = p[3].trim();
                String genre = p[4].trim();
                int year = Integer.parseInt(p[5].trim());
                String status = p[6].trim();
                books.add(new Book(bookId, serialNumber, title, author, genre, year, status));
            } catch (NumberFormatException e) {
                System.out.println("Skip invalid book line: " + line);
            }
        }
        return books;
    }

    public static void saveBooks(List<Book> books, String fileName) {
        List<String> lines = new ArrayList<>();
        for (Book b : books) {
            lines.add(String.join(JOIN_SEP,
                    b.getBookId(),
                    b.getSerialNumber(),
                    b.getTitle(),
                    b.getAuthor(),
                    b.getGenre(),
                    String.valueOf(b.getPublicationYear()),
                    b.getStatus()));
        }
        writeLines(fileName, lines);
    }

    // ===== MEMBER =====
    // Format: memberId|name|phone|email|memberType|totalBorrowings
    // (memberId = "M" + phone, sinh tu dong luc tao, van luu phone rieng de tien hien thi)
    public static List<Member> loadMembers(String fileName) {
        List<Member> members = new ArrayList<>();
        for (String line : readLines(fileName)) {
            if (line.trim().isEmpty() || line.startsWith("#")) {
                continue;
            }
            String[] p = line.split(SPLIT_REGEX);
            if (p.length < 6) {
                continue;
            }
            try {
                String memberId = p[0].trim();
                String name = p[1].trim();
                String phone = p[2].trim();
                String email = p[3].trim();
                String type = p[4].trim();
                int total = Integer.parseInt(p[5].trim());

                Member m;
                if (type.equalsIgnoreCase("PREMIUM")) {
                    m = new PremiumMember(memberId, name, phone, email);
                } else {
                    m = new RegularMember(memberId, name, phone, email);
                }
                m.setTotalBorrowings(total);
                members.add(m);
            } catch (NumberFormatException e) {
                System.out.println("Skip invalid member line: " + line);
            }
        }
        return members;
    }

    public static void saveMembers(List<Member> members, String fileName) {
        List<String> lines = new ArrayList<>();
        for (Member m : members) {
            lines.add(String.join(JOIN_SEP,
                    m.getMemberId(),
                    m.getName(),
                    m.getPhone(),
                    m.getEmail(),
                    m.getMemberType(),
                    String.valueOf(m.getTotalBorrowings())));
        }
        writeLines(fileName, lines);
    }

    // ===== TRANSACTION =====
    // Format: receiptId|transactionId|serialNumber|bookId|memberId|borrowDate|dueDate|returnDate|fineAmount|status
    public static List<BorrowingTransaction> loadTransactions(String fileName) {
        List<BorrowingTransaction> list = new ArrayList<>();
        for (String line : readLines(fileName)) {
            if (line.trim().isEmpty() || line.startsWith("#")) {
                continue;
            }
            String[] p = line.split(SPLIT_REGEX);
            if (p.length < 10) {
                continue;
            }
            try {
                String receiptId = p[0].trim();
                String txId = p[1].trim();
                String serialNumber = p[2].trim();
                String bookId = p[3].trim();
                String memberId = p[4].trim();
                LocalDate borrowDate = LocalDate.parse(p[5].trim(), DATE_FORMAT);
                String returnRaw = p[7].trim();
                double fine = Double.parseDouble(p[8].trim());
                String status = p[9].trim();

                BorrowingTransaction tx = new BorrowingTransaction(
                        receiptId, txId, serialNumber, bookId, memberId, borrowDate);
                if (status.equalsIgnoreCase(BorrowingTransaction.STATUS_RETURNED)
                        && !returnRaw.equalsIgnoreCase("null")) {
                    LocalDate returnDate = LocalDate.parse(returnRaw, DATE_FORMAT);
                    tx.markReturned(returnDate, fine);
                }
                list.add(tx);
            } catch (Exception e) {
                System.out.println("Skip invalid transaction line: " + line);
            }
        }
        return list;
    }

    public static void saveTransactions(List<BorrowingTransaction> list, String fileName) {
        List<String> lines = new ArrayList<>();
        for (BorrowingTransaction tx : list) {
            String returnStr = (tx.getReturnDate() == null)
                    ? "null"
                    : tx.getReturnDate().format(DATE_FORMAT);
            lines.add(String.join(JOIN_SEP,
                    tx.getReceiptId(),
                    tx.getTransactionId(),
                    tx.getSerialNumber(),
                    tx.getBookId(),
                    tx.getMemberId(),
                    tx.getBorrowDate().format(DATE_FORMAT),
                    tx.getDueDate().format(DATE_FORMAT),
                    returnStr,
                    String.format("%.0f", tx.getFineAmount()),
                    tx.getStatus()));
        }
        writeLines(fileName, lines);
    }
}