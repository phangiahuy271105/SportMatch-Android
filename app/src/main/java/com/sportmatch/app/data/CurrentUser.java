package com.sportmatch.app.data;

import android.content.Context;

import com.sportmatch.app.utils.SharedPrefManager;

/** Lấy cùng một mã người dùng cho các màn đặt sân. */
public final class CurrentUser {
    private CurrentUser() { }

    public static String getId() {
        return "demo_user_1";
    }

    public static String getId(Context context) {
        SharedPrefManager pref = SharedPrefManager.getInstance(context.getApplicationContext());
        String email = pref.getCurrentUserEmail();
        // Giữ cách lưu theo email hiện tại để đọc được các lịch đã đặt trước đó.
        if (email != null && !email.isEmpty()) return email;
        return getId();
    }
}
