package com.sportmatch.app.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.sportmatch.app.model.User;

public class SharedPrefManager {
    private static final String SHARED_PREF_NAME = "sportmatch_pref";
    private static SharedPrefManager instance;
    private Context context;

    private SharedPrefManager(Context context) {
        this.context = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPrefManager(context);
        }
        return instance;
    }

    public void saveUser(User user) {
        SharedPreferences sp = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        editor.putString("current_email", user.getEmail().toLowerCase().trim());

        String prefix = "user_" + user.getEmail().toLowerCase().trim() + "_";
        editor.putString(prefix + "id", user.getId());
        editor.putString(prefix + "email", user.getEmail().toLowerCase().trim());
        editor.putString(prefix + "password", user.getPassword());
        editor.putString(prefix + "fullName", user.getFullName());
        editor.putString(prefix + "phoneNumber", user.getPhoneNumber());
        editor.putString(prefix + "favoriteSport", user.getFavoriteSport());
        editor.putString(prefix + "gender", user.getGender()); // LƯU GIỚI TÍNH

        editor.commit();
    }

    public void setCurrentUser(User user) {
        saveUser(user);
        SharedPreferences sp = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        sp.edit().putString("current_email", user.getEmail().toLowerCase().trim()).commit();
    }

    public boolean isLoggedIn() {
        SharedPreferences sp = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sp.getString("current_email", null) != null;
    }

    public User getUser() {
        SharedPreferences sp = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String currentEmail = sp.getString("current_email", null);
        if (currentEmail == null) return null;

        return getUserByEmail(currentEmail);
    }

    public User getUserByEmail(String email) {
        SharedPreferences sp = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String prefix = "user_" + email.toLowerCase().trim() + "_";

        if (!sp.contains(prefix + "email")) {
            return null;
        }

        User user = new User();
        user.setId(sp.getString(prefix + "id", ""));
        user.setEmail(sp.getString(prefix + "email", ""));
        user.setPassword(sp.getString(prefix + "password", ""));
        user.setFullName(sp.getString(prefix + "fullName", ""));
        user.setPhoneNumber(sp.getString(prefix + "phoneNumber", ""));
        user.setFavoriteSport(sp.getString(prefix + "favoriteSport", ""));
        user.setGender(sp.getString(prefix + "gender", "Nam")); // ĐỌC GIỚI TÍNH
        return user;
    }

    public User checkLogin(String email, String password) {
        if (email.equalsIgnoreCase("khoa@sportmatch.vn") && password.equals("123456")) {
            User khoaUser = getUserByEmail("khoa@sportmatch.vn");
            if (khoaUser == null) {
                khoaUser = new User("u01", "khoa@sportmatch.vn", "Triệu Hoàng Đăng Khoa", "0912345678", "Pickleball", "Nam");
                khoaUser.setPassword("123456");
                saveUser(khoaUser);
            }
            return khoaUser;
        }

        User registeredUser = getUserByEmail(email);
        if (registeredUser != null) {
            if (registeredUser.getPassword() != null && registeredUser.getPassword().equals(password)) {
                return registeredUser;
            }
        }

        return null;
    }

    public void logout() {
        SharedPreferences sp = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        sp.edit().remove("current_email").commit();

    }
    // Lấy email của người đang đăng nhập
    public String getCurrentUserEmail() {
        SharedPreferences sp = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sp.getString("current_email", null);
    }

    // Lấy thông tin đầy đủ của người đang đăng nhập
    public User getCurrentUser() {
        String email = getCurrentUserEmail();
        if (email == null) return null;
        return getUserByEmail(email);
    }
}