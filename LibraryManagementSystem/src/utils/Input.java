package utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

<<<<<<< HEAD
=======

>>>>>>> Huy
public class Input {

    private static final Scanner sc = new Scanner(System.in);

<<<<<<< HEAD
    // Dinh dang ngay dung chung toan project: dd/MM/yyyy
    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // Doc mot dong, cat khoang trang thua
    public static String readString(String prompt) {
        System.out.print(prompt);
        return sc.nextLine().trim();
    }

    // Doc chuoi bat buoc khong rong
=======
    // Format ngày chuẩn toàn hệ thống
    // dd/MM/yyyy (ví dụ: 28/06/2026)
    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    
    // Đọc chuỗi
    public static String readString(String prompt) {
        System.out.print(prompt);
        return sc.nextLine().trim(); // đọc + xóa khoảng trắng đầu/cuối
    }

    // Đọc chuỗi không rỗng
>>>>>>> Huy
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

<<<<<<< HEAD
    // Doc so nguyen, lap lai neu nhap sai (xu ly exception)
=======
    // Đọc số nguyên
>>>>>>> Huy
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

<<<<<<< HEAD
    // Doc ngay theo dd/MM/yyyy, lap lai neu nhap sai
=======
    // Đọc ngày theo dd/MM/yyyy
>>>>>>> Huy
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
