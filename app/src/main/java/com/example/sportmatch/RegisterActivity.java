package com.example.sportmatch;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import models.User;
import utils.SharedPrefManager;
import utils.ValidationUtils;

public class RegisterActivity extends AppCompatActivity {

    private TextInputLayout tilFullName, tilEmail, tilPhone, tilPassword, tilConfirmPassword;
    private TextInputEditText etFullName, etEmail, etPhone, etPassword, etConfirmPassword;
    private Spinner spFavoriteSport;
    private Button btnRegister;
    private TextView tvBackToLogin;

    private final String[] sportsList = {"Bóng đá", "Cầu lông", "Tennis", "Bóng rổ", "Bóng chuyền", "Pickleball"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initViews();
        setupSpinner();
        setupEvents();
    }

    private void initViews() {
        tilFullName = findViewById(R.id.tilFullName);
        tilEmail = findViewById(R.id.tilEmail);
        tilPhone = findViewById(R.id.tilPhone);
        tilPassword = findViewById(R.id.tilPassword);
        tilConfirmPassword = findViewById(R.id.tilConfirmPassword);

        etFullName = findViewById(R.id.etFullName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);

        spFavoriteSport = findViewById(R.id.spFavoriteSport);
        btnRegister = findViewById(R.id.btnRegister);
        tvBackToLogin = findViewById(R.id.tvBackToLogin);
    }

    private void setupSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, sportsList);
        spFavoriteSport.setAdapter(adapter);
    }

    private void setupEvents() {
        btnRegister.setOnClickListener(v -> handleRegister());
        tvBackToLogin.setOnClickListener(v -> finish());
    }

    private void handleRegister() {
        String fullName = etFullName.getText() != null ? etFullName.getText().toString().trim() : "";
        String email = etEmail.getText() != null ? etEmail.getText().toString().trim() : "";
        String phone = etPhone.getText() != null ? etPhone.getText().toString().trim() : "";
        String password = etPassword.getText() != null ? etPassword.getText().toString().trim() : "";
        String confirmPassword = etConfirmPassword.getText() != null ? etConfirmPassword.getText().toString().trim() : "";
        String favoriteSport = spFavoriteSport.getSelectedItem().toString();

        tilFullName.setError(null);
        tilEmail.setError(null);
        tilPhone.setError(null);
        tilPassword.setError(null);
        tilConfirmPassword.setError(null);

        // 1. Kiểm tra Họ tên
        if (fullName.isEmpty()) {
            tilFullName.setError("Vui lòng nhập họ và tên!");
            etFullName.requestFocus();
            return;
        }
        if (!ValidationUtils.isValidFullName(fullName)) {
            tilFullName.setError("Họ tên không hợp lệ (cần ít nhất 2 từ và không chứa số)!");
            etFullName.requestFocus();
            return;
        }

        // 2. Kiểm tra Email
        if (email.isEmpty()) {
            tilEmail.setError("Vui lòng nhập email!");
            etEmail.requestFocus();
            return;
        }
        if (!ValidationUtils.isValidEmail(email)) {
            tilEmail.setError("Định dạng email không hợp lệ (Ví dụ: ten@gmail.com)!");
            etEmail.requestFocus();
            return;
        }
        if (SharedPrefManager.getInstance(this).getUserByEmail(email) != null) {
            tilEmail.setError("Email này đã được đăng ký tài khoản SportMatch!");
            etEmail.requestFocus();
            return;
        }

        // 3. Kiểm tra SĐT chuẩn nhà mạng VN
        if (phone.isEmpty()) {
            tilPhone.setError("Vui lòng nhập số điện thoại!");
            etPhone.requestFocus();
            return;
        }
        if (!ValidationUtils.isValidVietnamesePhone(phone)) {
            tilPhone.setError("Số điện thoại không đúng chuẩn VN (10 số, đầu 03, 05, 07, 08, 09)!");
            etPhone.requestFocus();
            return;
        }

        // 4. Kiểm tra Mật khẩu an toàn
        if (password.isEmpty()) {
            tilPassword.setError("Vui lòng nhập mật khẩu!");
            etPassword.requestFocus();
            return;
        }
        if (!ValidationUtils.isValidPassword(password)) {
            tilPassword.setError("Mật khẩu phải từ 6 ký tự, gồm cả chữ và số (Ví dụ: Khoa123)!");
            etPassword.requestFocus();
            return;
        }

        // 5. Xác nhận mật khẩu
        if (!password.equals(confirmPassword)) {
            tilConfirmPassword.setError("Mật khẩu xác nhận không trùng khớp!");
            etConfirmPassword.requestFocus();
            return;
        }

        // Lưu tài khoản hợp lệ
        String newId = "user_" + System.currentTimeMillis();
        User newUser = new User(newId, email, fullName, phone, favoriteSport);
        newUser.setPassword(password);
        SharedPrefManager.getInstance(this).saveUser(newUser);

        Toast.makeText(this, "Đăng ký thành công tài khoản: " + fullName, Toast.LENGTH_LONG).show();
        finish();
    }
}