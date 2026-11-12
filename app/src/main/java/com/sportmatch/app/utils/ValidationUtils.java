package com.sportmatch.app.utils;

import java.util.regex.Pattern;

public class ValidationUtils {

    // Regex chuẩn Email quốc tế với tên miền hợp lệ
    private static final String EMAIL_REGEX =
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    // Regex chuẩn 10 số điện thoại di động các nhà mạng Việt Nam (Viettel, Vina, Mobi, Vietnamobile)
    private static final String VIETNAM_PHONE_REGEX =
            "^(0)(3[2-9]|5[6|8|9]|7[0|6-9]|8[1-9]|9[0-9])[0-9]{7}$";

    // 1. Kiểm tra Email hợp lệ
    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) return false;
        return Pattern.compile(EMAIL_REGEX).matcher(email.trim()).matches();
    }

    // 2. Kiểm tra Số điện thoại Việt Nam chuẩn nhà mạng
    public static boolean isValidVietnamesePhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) return false;
        return Pattern.compile(VIETNAM_PHONE_REGEX).matcher(phone.trim()).matches();
    }

    // 3. Kiểm tra Họ tên: tối thiểu 2 từ, không chứa số hay ký tự đặc biệt
    public static boolean isValidFullName(String name) {
        if (name == null || name.trim().isEmpty()) return false;
        String cleanName = name.trim();

        // Tên phải có ít nhất 2 từ (cách nhau bởi dấu cách)
        String[] words = cleanName.split("\\s+");
        if (words.length < 2) return false;

        // Chỉ chứa chữ cái tiếng Việt và khoảng trắng, không chứa số
        return cleanName.matches("^[\\p{L}\\s]+$");
    }

    // 4. Kiểm tra Mật khẩu an toàn: tối thiểu 6 ký tự, gồm cả chữ và số
    public static boolean isValidPassword(String password) {
        if (password == null || password.length() < 6) return false;
        // Bắt buộc có cả chữ và số (ví dụ khoa123)
        boolean hasLetter = false;
        boolean hasDigit = false;
        for (char c : password.toCharArray()) {
            if (Character.isLetter(c)) hasLetter = true;
            if (Character.isDigit(c)) hasDigit = true;
        }
        return hasLetter && hasDigit;
    }
}
