package com.example.sportmatch;

import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import models.User;
import utils.SharedPrefManager;

public class ForgotPasswordActivity extends AppCompatActivity {

    private TextInputLayout tilForgotEmail, tilNewPassword, tilConfirmNewPassword;
    private TextInputEditText etForgotEmail, etNewPassword, etConfirmNewPassword;
    private Button btnResetPassword;
    private TextView tvBackToLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        initViews();
        setupEvents();
    }

    private void initViews() {
        tilForgotEmail = findViewById(R.id.tilForgotEmail);
        tilNewPassword = findViewById(R.id.tilNewPassword);
        tilConfirmNewPassword = findViewById(R.id.tilConfirmNewPassword);

        etForgotEmail = findViewById(R.id.etForgotEmail);
        etNewPassword = findViewById(R.id.etNewPassword);
        etConfirmNewPassword = findViewById(R.id.etConfirmNewPassword);

        btnResetPassword = findViewById(R.id.btnResetPassword);
        tvBackToLogin = findViewById(R.id.tvBackToLoginFromForgot);
    }

    private void setupEvents() {
        btnResetPassword.setOnClickListener(v -> handleResetPassword());
        tvBackToLogin.setOnClickListener(v -> finish()); // Đóng màn hình quay về đăng nhập
    }

    private void handleResetPassword() {
        String email = etForgotEmail.getText() != null ? etForgotEmail.getText().toString().trim() : "";
        String newPass = etNewPassword.getText() != null ? etNewPassword.getText().toString().trim() : "";
        String confirmPass = etConfirmNewPassword.getText() != null ? etConfirmNewPassword.getText().toString().trim() : "";

        // Reset lỗi hiển thị
        tilForgotEmail.setError(null);
        tilNewPassword.setError(null);
        tilConfirmNewPassword.setError(null);

        // 1. Kiểm tra Email nhập vào
        if (email.isEmpty()) {
            tilForgotEmail.setError("Vui lòng nhập email tài khoản!");
            etForgotEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            tilForgotEmail.setError("Định dạng email không hợp lệ!");
            etForgotEmail.requestFocus();
            return;
        }

        // 2. Tra cứu xem tài khoản này có tồn tại trong hệ thống không
        User user = SharedPrefManager.getInstance(this).getUserByEmail(email);
        if (user == null && email.equalsIgnoreCase("khoa@sportmatch.vn")) {
            // Khởi tạo tài khoản mẫu nếu chưa từng lưu
            user = new User("u01", "khoa@sportmatch.vn", "Triệu Hoàng Đăng Khoa", "0912345678", "Pickleball");
        }

        if (user == null) {
            tilForgotEmail.setError("Email này chưa đăng ký tài khoản SportMatch!");
            etForgotEmail.requestFocus();
            return;
        }

        // 3. Kiểm tra Mật khẩu mới
        if (newPass.isEmpty()) {
            tilNewPassword.setError("Vui lòng nhập mật khẩu mới!");
            etNewPassword.requestFocus();
            return;
        }
        if (newPass.length() < 6) {
            tilNewPassword.setError("Mật khẩu mới phải từ 6 ký tự trở lên!");
            etNewPassword.requestFocus();
            return;
        }

        // 4. Kiểm tra 2 mật khẩu có khớp nhau không
        if (!newPass.equals(confirmPass)) {
            tilConfirmNewPassword.setError("Mật khẩu xác nhận không trùng khớp!");
            etConfirmNewPassword.requestFocus();
            return;
        }

        // 5. Cập nhật mật khẩu mới vào tài khoản
        user.setPassword(newPass);
        SharedPrefManager.getInstance(this).saveUser(user);

        Toast.makeText(this, "Đổi mật khẩu thành công! Hãy đăng nhập lại bằng mật khẩu mới.", Toast.LENGTH_LONG).show();
        finish(); // Quay lại màn hình đăng nhập
    }
}