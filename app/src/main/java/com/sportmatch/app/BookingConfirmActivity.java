package com.sportmatch.app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Màn hình 4: Xác nhận đặt sân
 * Trình độ sinh viên năm 3: Kiểm tra thông tin đặt sân và xác nhận hoàn tất.
 */
public class BookingConfirmActivity extends AppCompatActivity {

    private String venueName = "Sân bóng đá Chảo Lửa";
    private String courtName = "Sân số 1 (5 người)";
    private String selectedDate = "Hôm nay, 18/10/2026";
    private String selectedTime = "18:00 – 19:00";
    private int price = 180000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_confirm);

        // 1. Nút quay lại
        ImageButton btnBack = findViewById(R.id.btn_back);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        // 2. Nhận dữ liệu truyền từ Màn 3
        if (getIntent().hasExtra("VENUE_NAME")) {
            venueName = getIntent().getStringExtra("VENUE_NAME");
        }
        if (getIntent().hasExtra("COURT_NAME")) {
            courtName = getIntent().getStringExtra("COURT_NAME");
        }
        if (getIntent().hasExtra("SELECTED_DATE")) {
            selectedDate = getIntent().getStringExtra("SELECTED_DATE");
        }
        if (getIntent().hasExtra("SELECTED_TIME")) {
            selectedTime = getIntent().getStringExtra("SELECTED_TIME");
        }
        if (getIntent().hasExtra("PRICE")) {
            price = getIntent().getIntExtra("PRICE", 180000);
        }

        // 3. Đổ dữ liệu lên các View
        TextView tvVenue = findViewById(R.id.tv_confirm_venue_name);
        if (tvVenue != null) tvVenue.setText(venueName);

        TextView tvCourt = findViewById(R.id.tv_confirm_court_name);
        if (tvCourt != null) tvCourt.setText(courtName);

        TextView tvDate = findViewById(R.id.tv_confirm_date);
        if (tvDate != null) tvDate.setText(selectedDate);

        TextView tvTime = findViewById(R.id.tv_confirm_time);
        if (tvTime != null) tvTime.setText(selectedTime);

        String priceStr = NumberFormat.getNumberInstance(Locale.forLanguageTag("vi-VN")).format(price) + " đ";
        TextView tvCourtFee = findViewById(R.id.tv_confirm_court_fee);
        if (tvCourtFee != null) tvCourtFee.setText(priceStr);

        TextView tvTotal = findViewById(R.id.tv_confirm_total_price);
        if (tvTotal != null) tvTotal.setText(priceStr);

        // 4. Nút "Xác nhận đặt sân" -> Chuyển sang Màn hình 6 (Chi tiết lịch đặt thành công)
        MaterialButton btnConfirm = findViewById(R.id.btn_confirm_booking);
        if (btnConfirm != null) {
            btnConfirm.setOnClickListener(v -> {
                Toast.makeText(BookingConfirmActivity.this, "Đặt sân thành công!", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(BookingConfirmActivity.this, BookingDetailActivity.class);
                intent.putExtra("VENUE_NAME", venueName);
                intent.putExtra("COURT_NAME", courtName);
                intent.putExtra("SELECTED_DATE", selectedDate);
                intent.putExtra("SELECTED_TIME", selectedTime);
                intent.putExtra("PRICE", price);
                intent.putExtra("BOOKING_CODE", "SM-2026-" + (1000 + (int)(Math.random() * 8999)));
                startActivity(intent);
            });
        }
    }
}
