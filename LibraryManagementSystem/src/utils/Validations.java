package utils;

public class Validations {

    // Kiem tra chuoi rong hoac null
    public static boolean isNullOrEmpty(String s) {
        return s == null || s.trim().isEmpty();
    }

    // Kiem tra dinh dang ID: prefix + 3 chu so. Vi du B001.
    // Chi con dung cho Book ID (Member ID gio sinh tu dong tu SDT nen khong can nhap/kiem tra dang nay).
    public static boolean isValidId(String id, String prefix) {
        if (isNullOrEmpty(id)) {
            return false;
        }
        return id.matches("^" + prefix + "\\d{3}$");
    }

    // Nam xuat ban phai duong va khong vuot qua 2100
    public static boolean isValidYear(int year) {
        return year > 0 && year <= 2100;
    }

    // So luong ban sach (khi them moi / them ban sao) phai >= 1
    public static boolean isValidQuantity(int quantity) {
        return quantity >= 1;
    }

    // Email don gian: co @ va co dau .
    public static boolean isValidEmail(String email) {
        if (isNullOrEmpty(email)) {
            return false;
        }
        return email.contains("@") && email.contains(".");
    }

    // So dien thoai: bat dau bang 0, tong 10 hoac 11 chu so
    public static boolean isValidPhone(String phone) {
        if (isNullOrEmpty(phone)) {
            return false;
        }
        return phone.matches("^0\\d{9,10}$");
    }
}
