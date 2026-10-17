package com.sportmatch.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Màn hình 2: Chi tiết sân thi đấu
 * Trình độ sinh viên năm 3: Nhận Intent, hiển thị thông tin sân và mở Màn hình 3.
 */
public class VenueDetailActivity extends AppCompatActivity {

    private String venueName = "Sân bóng đá Chảo Lửa";
    private String venueAddress = "30 Phan Thúc Duyện, P.4, Tân Bình";
    private int price = 180000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venue_detail);

        // 1. Nút quay lại
        ImageButton btnBack = findViewById(R.id.btn_back);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        // 2. Nhận dữ liệu truyền từ MainActivity nếu có
        if (getIntent().hasExtra("VENUE_NAME")) {
            venueName = getIntent().getStringExtra("VENUE_NAME");
        }
        if (getIntent().hasExtra("VENUE_ADDRESS")) {
            venueAddress = getIntent().getStringExtra("VENUE_ADDRESS");
        }
        if (getIntent().hasExtra("PRICE")) {
            price = getIntent().getIntExtra("PRICE", 180000);
        }

        // 3. Hiển thị thông tin lên giao diện
        TextView tvName = findViewById(R.id.venue_detail_name);
        if (tvName != null) {
            tvName.setText(venueName);
        }

        TextView tvAddress = findViewById(R.id.venue_detail_address);
        if (tvAddress != null) {
            tvAddress.setText(venueAddress);
        }

        TextView tvBottomPrice = findViewById(R.id.tv_bottom_price);
        if (tvBottomPrice != null) {
            String formattedPrice = NumberFormat.getNumberInstance(Locale.forLanguageTag("vi-VN")).format(price) + "đ/giờ";
            tvBottomPrice.setText(formattedPrice);
        }

        // 4. Thêm sân con mẫu vào danh sách sân
        LinearLayout subCourtsContainer = findViewById(R.id.sub_courts_container);
        if (subCourtsContainer != null) {
            // Sân con 1
            View court1 = getLayoutInflater().inflate(R.layout.item_sub_court, subCourtsContainer, false);
            TextView name1 = court1.findViewById(R.id.tv_court_name);
            TextView price1 = court1.findViewById(R.id.tv_court_price);
            if (name1 != null) name1.setText(R.string.court_label_pitch_1);
            if (price1 != null) price1.setText(NumberFormat.getNumberInstance(Locale.forLanguageTag("vi-VN")).format(price) + "đ/giờ");
            subCourtsContainer.addView(court1);

            // Sân con 2
            View court2 = getLayoutInflater().inflate(R.layout.item_sub_court, subCourtsContainer, false);
            TextView name2 = court2.findViewById(R.id.tv_court_name);
            TextView price2 = court2.findViewById(R.id.tv_court_price);
            if (name2 != null) name2.setText(R.string.court_label_pitch_2);
            if (price2 != null) price2.setText(NumberFormat.getNumberInstance(Locale.forLanguageTag("vi-VN")).format(price + 50000) + "đ/giờ");
            subCourtsContainer.addView(court2);
        }

        // 5. Nút "Chọn ngày và giờ" -> Chuyển sang Màn hình 3
        MaterialButton btnSelectSlot = findViewById(R.id.btn_select_time_slot);
        if (btnSelectSlot != null) {
            btnSelectSlot.setOnClickListener(v -> {
                Intent intent = new Intent(VenueDetailActivity.this, SelectTimeSlotActivity.class);
                intent.putExtra("VENUE_NAME", venueName);
                intent.putExtra("COURT_NAME", "Sân số 1 (5 người)");
                intent.putExtra("PRICE", price);
                startActivity(intent);
            });
        }
    }
}
