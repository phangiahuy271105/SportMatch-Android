package com.sportmatch.app;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class CreatePostActivity extends AppCompatActivity {

    private EditText edtSport, edtLocation, edtTime, edtMaxPlayers, edtPhone, edtDescription;
    private Button btnSubmitPost;
    private ImageView imgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        // Ánh xạ nút Quay lại
        imgBack = findViewById(R.id.imgBack);

        // Sự kiện bấm nút Quay lại -> Đóng màn hình hiện tại
        imgBack.setOnClickListener(v -> finish());

        // Ánh xạ các ô nhập liệu
        edtSport = findViewById(R.id.edtSport);
        edtLocation = findViewById(R.id.edtLocation);
        edtTime = findViewById(R.id.edtTime);
        edtMaxPlayers = findViewById(R.id.edtMaxPlayers);
        edtPhone = findViewById(R.id.edtPhone);
        edtDescription = findViewById(R.id.edtDescription);
        btnSubmitPost = findViewById(R.id.btnSubmitPost);

        // Xử lý sự kiện bấm nút Đăng bài
        btnSubmitPost.setOnClickListener(v -> {
            String sport = edtSport.getText().toString().trim();
            String location = edtLocation.getText().toString().trim();
            String time = edtTime.getText().toString().trim();
            String maxPlayersStr = edtMaxPlayers.getText().toString().trim();
            String phone = edtPhone.getText().toString().trim();

            if (TextUtils.isEmpty(sport) || TextUtils.isEmpty(location) ||
                    TextUtils.isEmpty(time) || TextUtils.isEmpty(maxPlayersStr) ||
                    TextUtils.isEmpty(phone)) {

                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin và Số điện thoại liên hệ!", Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(this, "Đăng bài thành công!", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}