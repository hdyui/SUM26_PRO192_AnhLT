package utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Input {

    private static final Scanner sc = new Scanner(System.in);

    // Dinh dang ngay dung chung toan project: dd/MM/yyyy
    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // Doc mot dong, cat khoang trang thua
    public static String readString(String prompt) {
        System.out.print(prompt);
        return sc.nextLine().trim();
    }

    // Doc chuoi bat buoc khong rong
    public static String readNonEmptyString(String prompt) {
        String value;
        do {
            value = readString(prompt);
            if (value.isEmpty()) {
                System.out.println("Input cannot be empty. Please try again.");
            }
        } while (value.isEmpty());
        return value;
    }

    // Doc so nguyen, lap lai neu nhap sai (xu ly exception)
    public static int readInt(String prompt) {
        while (true) {
            String raw = readString(prompt);
            try {
                return Integer.parseInt(raw);
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Please enter an integer.");
            }
        }
    }

    // Doc ngay theo dd/MM/yyyy, lap lai neu nhap sai
    public static LocalDate readDate(String prompt) {
        while (true) {
            String raw = readString(prompt);
            try {
                return LocalDate.parse(raw, DATE_FORMAT);
            } catch (Exception e) {
                System.out.println("Invalid date. Please use format dd/MM/yyyy.");
            }
        }
    }
}
