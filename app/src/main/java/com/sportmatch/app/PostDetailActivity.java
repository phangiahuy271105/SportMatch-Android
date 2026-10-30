package com.sportmatch.app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class PostDetailActivity extends AppCompatActivity {

    private String phoneNumber = "0987654321";
    private ImageView imgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        // Nút quay lại
        imgBack = findViewById(R.id.imgBack);
        imgBack.setOnClickListener(v -> finish());

        // Ánh xạ View
        TextView tvSport = findViewById(R.id.tvDetailSport);
        TextView tvLocation = findViewById(R.id.tvDetailLocation);
        TextView tvTime = findViewById(R.id.tvDetailTime);
        TextView tvSlots = findViewById(R.id.tvDetailSlots);
        TextView tvDescription = findViewById(R.id.tvDetailDescription);
        TextView tvAuthorNameDetail = findViewById(R.id.tvAuthorNameDetail);

        Button btnCallNow = findViewById(R.id.btnCallNow);
        Button btnSendRequest = findViewById(R.id.btnSendRequest);

        // Nhận dữ liệu truyền từ Feed sang
        Intent intent = getIntent();
        if (intent != null) {
            String authorName = intent.getStringExtra("authorName");
            if (authorName != null && !authorName.isEmpty()) {
                tvAuthorNameDetail.setText("Người đăng: " + authorName);
            }

            tvSport.setText(intent.getStringExtra("sport"));
            tvLocation.setText("📍 Địa điểm: " + intent.getStringExtra("location"));
            tvTime.setText("⏰ Thời gian: " + intent.getStringExtra("time"));
            int current = intent.getIntExtra("current", 0);
            int max = intent.getIntExtra("max", 0);
            tvSlots.setText("👥 Số lượng: " + current + "/" + max + " người");
            tvDescription.setText("Ghi chú: " + intent.getStringExtra("description"));
        }

        // Bấm Gọi ngay -> Mở màn hình quay số
        btnCallNow.setOnClickListener(v -> {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:" + phoneNumber));
            startActivity(callIntent);
        });

        // Bấm Gửi yêu cầu
        btnSendRequest.setOnClickListener(v -> {
            Toast.makeText(this, "Đã gửi yêu cầu tham gia tới chủ bài đăng!", Toast.LENGTH_SHORT).show();
            btnSendRequest.setText("Đã gửi yêu cầu");
            btnSendRequest.setEnabled(false);
        });
    }
}