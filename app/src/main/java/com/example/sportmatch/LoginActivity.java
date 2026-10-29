package com.example.sportmatch;

import android.content.Intent;
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

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout tilEmail, tilPassword;
    private TextInputEditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvForgotPassword, tvGoToRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Nếu đã đăng nhập trước đó thì vào thẳng Profile
        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            startActivity(new Intent(LoginActivity.this, ProfileActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_login);

        initViews();
        setupEvents();
    }

    private void initViews() {
        tilEmail = findViewById(R.id.tilEmail);
        tilPassword = findViewById(R.id.tilPassword);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        tvGoToRegister = findViewById(R.id.tvGoToRegister);
    }

    private void setupEvents() {
        btnLogin.setOnClickListener(v -> handleLogin());

        // Chuyển sang màn hình Đăng ký
        tvGoToRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        // Chuyển sang màn hình Quên mật khẩu
        tvForgotPassword.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
            startActivity(intent);
        });
    }

    private void handleLogin() {
        String email = etEmail.getText() != null ? etEmail.getText().toString().trim() : "";
        String password = etPassword.getText() != null ? etPassword.getText().toString().trim() : "";

        tilEmail.setError(null);
        tilPassword.setError(null);

        // 1. Kiểm tra Email
        if (email.isEmpty()) {
            tilEmail.setError("Vui lòng nhập email!");
            etEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            tilEmail.setError("Định dạng email không hợp lệ!");
            etEmail.requestFocus();
            return;
        }

        // 2. Kiểm tra Mật khẩu
        if (password.isEmpty()) {
            tilPassword.setError("Vui lòng nhập mật khẩu!");
            etPassword.requestFocus();
            return;
        }
        if (password.length() < 6) {
            tilPassword.setError("Mật khẩu phải từ 6 ký tự trở lên!");
            etPassword.requestFocus();
            return;
        }

        // 3. Xử lý Đăng nhập qua sổ tay SharedPrefManager
        User user = SharedPrefManager.getInstance(this).checkLogin(email, password);

        if (user != null) {
            // Đúng Email và Mật khẩu -> Đặt làm người dùng hiện tại
            SharedPrefManager.getInstance(this).setCurrentUser(user);

            Toast.makeText(this, "Đăng nhập thành công! Xin chào " + user.getFullName(), Toast.LENGTH_LONG).show();

            // Chuyển sang màn hình Hồ sơ cá nhân
            Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Email hoặc mật khẩu không chính xác!", Toast.LENGTH_LONG).show();
        }
    }
}