package com.sportmatch.app;
import com.sportmatch.app.R;
import com.sportmatch.app.model.User;
import com.sportmatch.app.utils.SharedPrefManager;
import com.sportmatch.app.utils.ValidationUtils;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    private ImageView imgAvatar;
    private TextView tvProfileNameHeader, tvProfileEmailHeader, tvProfilePhoneHeader;
    private EditText etProfileName, etProfilePhone;
    private RadioGroup rgGender;
    private RadioButton rbMale, rbFemale;
    private Spinner spProfileSport;
    private Button btnUpdateProfile, btnLogout;

    private final String[] sportsList = {"Bóng đá mini", "Cầu lông"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_profile);

        initViews();
        setupSpinner();
        loadUserData();
        setupEvents();
    }

    private void initViews() {
        imgAvatar = findViewById(R.id.imgAvatar);
        tvProfileNameHeader = findViewById(R.id.tvProfileNameHeader);
        tvProfileEmailHeader = findViewById(R.id.tvProfileEmailHeader);
        tvProfilePhoneHeader = findViewById(R.id.tvProfilePhoneHeader);
        etProfileName = findViewById(R.id.etProfileName);
        etProfilePhone = findViewById(R.id.etProfilePhone);
        rgGender = findViewById(R.id.rgGender);
        rbMale = findViewById(R.id.rbMale);
        rbFemale = findViewById(R.id.rbFemale);
        spProfileSport = findViewById(R.id.spProfileSport);
        btnUpdateProfile = findViewById(R.id.btnUpdateProfile);
        btnLogout = findViewById(R.id.btnLogout);
    }

    private void setupSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, sportsList);
        spProfileSport.setAdapter(adapter);
    }

    private void loadUserData() {
        User user = SharedPrefManager.getInstance(this).getUser();
        if (user != null) {
            tvProfileNameHeader.setText(user.getFullName());
            tvProfileEmailHeader.setText(user.getEmail());
            tvProfilePhoneHeader.setText("SĐT: " + (user.getPhoneNumber() != null ? user.getPhoneNumber() : "Chưa cập nhật"));

            etProfileName.setText(user.getFullName());
            etProfilePhone.setText(user.getPhoneNumber());

            // 1. CẬP NHẬT AVATAR VÀ GIỚI TÍNH THEO DỮ LIỆU
            if ("Nữ".equalsIgnoreCase(user.getGender())) {
                rbFemale.setChecked(true);
                imgAvatar.setImageResource(R.drawable.avatar_female);
            } else {
                rbMale.setChecked(true);
                imgAvatar.setImageResource(R.drawable.avatar_male);
            }

            // 2. Môn thể thao
            if (user.getFavoriteSport() != null) {
                for (int i = 0; i < sportsList.length; i++) {
                    if (sportsList[i].equalsIgnoreCase(user.getFavoriteSport())) {
                        spProfileSport.setSelection(i);
                        break;
                    }
                }
            }
        }
    }

    private void setupEvents() {
        // Đổi avatar xem trước ngay khi người dùng tích chọn Nam/Nữ
        rgGender.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rbFemale) {
                imgAvatar.setImageResource(R.drawable.avatar_female);
            } else {
                imgAvatar.setImageResource(R.drawable.avatar_male);
            }
        });

        // Nút Lưu cập nhật
        btnUpdateProfile.setOnClickListener(v -> handleUpdateProfile());

        // Nút Đăng xuất
        btnLogout.setOnClickListener(v -> {
            SharedPrefManager.getInstance(this).logout();
            Toast.makeText(this, "Đã đăng xuất!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    private void handleUpdateProfile() {
        String name = etProfileName.getText() != null ? etProfileName.getText().toString().trim() : "";
        String phone = etProfilePhone.getText() != null ? etProfilePhone.getText().toString().trim() : "";
        String sport = spProfileSport.getSelectedItem().toString();
        String gender = rbFemale.isChecked() ? "Nữ" : "Nam";

        // Kiểm tra hợp lệ
        if (!ValidationUtils.isValidFullName(name)) {
            etProfileName.setError("Họ tên phải gồm ít nhất 2 từ và không chứa số!");
            etProfileName.requestFocus();
            return;
        }

        if (!ValidationUtils.isValidVietnamesePhone(phone)) {
            etProfilePhone.setError("SĐT không hợp lệ (10 số, bắt đầu bằng 03, 05, 07, 08, 09)!");
            etProfilePhone.requestFocus();
            return;
        }

        // Lưu thông tin gồm cả GIỚI TÍNH vào hệ thống
        User user = SharedPrefManager.getInstance(this).getUser();
        user.setFullName(name);
        user.setPhoneNumber(phone);
        user.setFavoriteSport(sport);
        user.setGender(gender);
        SharedPrefManager.getInstance(this).saveUser(user);

        // Cập nhật lên Header
        tvProfileNameHeader.setText(name);
        tvProfilePhoneHeader.setText("SĐT: " + phone);

        // Đảm bảo hiển thị đúng avatar
        if ("Nữ".equals(gender)) {
            imgAvatar.setImageResource(R.drawable.avatar_female);
        } else {
            imgAvatar.setImageResource(R.drawable.avatar_male);
        }

        Toast.makeText(this, "Cập nhật hồ sơ thành công!", Toast.LENGTH_SHORT).show();
    }
}