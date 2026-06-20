package utils;

import model.Book;
import model.BorrowingTransaction;
import model.Member;
import model.PremiumMember;
import model.RegularMember;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FileUtils {

    public static final String RESOURCE_FOLDER = "src/resources";
    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE;

    public static List<String> readAllLines(String filePath) throws IOException {
        Path path = resolveResourcePath(filePath);
        if (Files.notExists(path)) {
            return new ArrayList<>();
        }
        return Files.readAllLines(path, StandardCharsets.UTF_8);
    }

    public static String readAllText(String filePath) throws IOException {
        Path path = resolveResourcePath(filePath);
        if (Files.notExists(path)) {
            return "";
        }
        return Files.readString(path, StandardCharsets.UTF_8);
    }

    public static void writeAllLines(String filePath, List<String> lines) throws IOException {
        Path path = createPath(resolveResourcePath(filePath));
        Files.write(path, lines, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    public static void writeAllText(String filePath, String content) throws IOException {
        Path path = createPath(resolveResourcePath(filePath));
        Files.writeString(path, content, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    public static void appendLines(String filePath, List<String> lines) throws IOException {
        Path path = createPath(resolveResourcePath(filePath));
        Files.write(path, lines, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }

    public static void appendLine(String filePath, String line) throws IOException {
        appendLines(filePath, Collections.singletonList(line));
    }

    public static boolean fileExists(String filePath) {
        return Files.exists(resolveResourcePath(filePath));
    }

    public static void generateSampleMembersFile(String resourceFileName) throws IOException {
        List<String> sampleLines = Arrays.asList(
            "# memberId | name | phone | email | type",
            "M001|Nguyen Van A|0901234567|a@gmail.com|Regular",
            "M002|Tran Thi B|0912345678|b@gmail.com|Premium",
            "M003|Le Van C|0923456789|c@gmail.com|Regular"
        );
        writeAllLines(resourceFileName, sampleLines);
    }

    public static List<Member> loadMembers() throws IOException {
        return loadMembers("members.txt");
    }

    public static void generateSampleMembersFile() throws IOException {
        generateSampleMembersFile("members.txt");
    }

    public static List<Book> loadBooks(String resourceFileName) throws IOException {
        List<Book> books = new ArrayList<>();
        for (String line : readAllLines(resourceFileName)) {
            String trimmed = line.trim();
            if (trimmed.isEmpty() || trimmed.startsWith("#")) {
                continue;
            }
            Book book = parseBook(trimmed);
            if (book != null) {
                books.add(book);
            }
        }
        return books;
    }

    public static List<Member> loadMembers(String resourceFileName) throws IOException {
        List<Member> members = new ArrayList<>();
        for (String line : readAllLines(resourceFileName)) {
            String trimmed = line.trim();
            if (trimmed.isEmpty() || trimmed.startsWith("#")) {
                continue;
            }
            Member member = parseMember(trimmed);
            if (member != null) {
                members.add(member);
            }
        }
        return members;
    }

    public static List<BorrowingTransaction> loadTransactions(String resourceFileName) throws IOException {
        List<BorrowingTransaction> transactions = new ArrayList<>();
        for (String line : readAllLines(resourceFileName)) {
            String trimmed = line.trim();
            if (trimmed.isEmpty() || trimmed.startsWith("#")) {
                continue;
            }
            BorrowingTransaction transaction = parseTransaction(trimmed);
            if (transaction != null) {
                transactions.add(transaction);
            }
        }
        return transactions;
    }

    public static Path resolveResourcePath(String resourceFileName) {
        Path direct = Paths.get(resourceFileName);
        if (Files.exists(direct)) {
            return direct;
        }

        Path resourcePath = Paths.get(RESOURCE_FOLDER, resourceFileName);
        if (Files.exists(resourcePath)) {
            return resourcePath;
        }

        Path fallback = Paths.get("resources", resourceFileName);
        if (Files.exists(fallback)) {
            return fallback;
        }

        return resourcePath;
    }

    private static Path createPath(Path path) throws IOException {
        Path parent = path.getParent();
        if (parent != null && Files.notExists(parent)) {
            Files.createDirectories(parent);
        }
        if (Files.notExists(path)) {
            Files.createFile(path);
        }
        return path;
    }

    private static Book parseBook(String line) {
        String[] parts = line.split("[|,]");
        if (parts.length < 3) {
            return null;
        }
        String bookId = parts[0].trim();
        String title = parts[1].trim();
        String author = parts.length > 2 ? parts[2].trim() : "";
        String publisher = parts.length > 3 ? parts[3].trim() : "";
        int year = parts.length > 4 ? parseInt(parts[4].trim()) : 0;
        int availableCopies = parts.length > 5 ? parseInt(parts[5].trim()) : 0;
        return new Book(bookId, title, author, publisher, year, availableCopies);
    }

    private static Member parseMember(String line) {
        String[] parts = line.split("[|,]");
        if (parts.length < 4) {
            return null;
        }
        String memberId = parts[0].trim();
        String name = parts[1].trim();
        String phone = parts[2].trim();
        String email = parts[3].trim();
        String memberType = parts.length > 4 ? parts[4].trim() : "Regular";

        if (memberType.equalsIgnoreCase("Premium")) {
            return new PremiumMember(memberId, name, phone, email);
        }
        return new RegularMember(memberId, name, phone, email);
    }

    private static BorrowingTransaction parseTransaction(String line) {
        String[] parts = line.split("[|,]");
        if (parts.length < 5) {
            return null;
        }
        String transactionId = parts[0].trim();
        String bookId = parts[1].trim();
        String memberId = parts[2].trim();
        LocalDate borrowDate = parseDate(parts[3].trim());
        LocalDate dueDate = parts.length > 4 ? parseDate(parts[4].trim()) : null;
        LocalDate returnDate = parts.length > 5 ? parseDate(parts[5].trim()) : null;
        return new BorrowingTransaction(transactionId, bookId, memberId, borrowDate, dueDate, returnDate);
    }

    private static int parseInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException ex) {
            return 0;
        }
    }

    private static LocalDate parseDate(String value) {
        try {
            return LocalDate.parse(value, DATE_FORMAT);
        } catch (Exception ex) {
            return null;
        }
    }
}

