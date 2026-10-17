package com.sportmatch.app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Màn hình 6: Chi tiết lịch đặt sân (Vé đặt thành công)
 * Trình độ sinh viên năm 3: Hiển thị mã vé check-in, thông tin sân và cho quay về trang chủ.
 */
public class BookingDetailActivity extends AppCompatActivity {

    private String venueName = "Sân bóng đá Chảo Lửa";
    private String courtName = "Sân số 1 (5 người)";
    private String selectedDate = "Thứ Bảy, 18/10/2026";
    private String selectedTime = "18:00 – 19:00";
    private int price = 180000;
    private String bookingCode = "SM-2026-8899";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_detail);

        // 1. Nút quay lại
        ImageButton btnBack = findViewById(R.id.btn_back);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        // 2. Nhận dữ liệu truyền sang
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
        if (getIntent().hasExtra("BOOKING_CODE")) {
            bookingCode = getIntent().getStringExtra("BOOKING_CODE");
        }

        // 3. Đổ dữ liệu lên giao diện
        TextView tvCode = findViewById(R.id.tv_booking_detail_code);
        if (tvCode != null) tvCode.setText(bookingCode);

        TextView tvVenue = findViewById(R.id.tv_detail_venue_name);
        if (tvVenue != null) tvVenue.setText(venueName);

        TextView tvCourt = findViewById(R.id.tv_detail_court_name);
        if (tvCourt != null) tvCourt.setText(courtName);

        TextView tvDate = findViewById(R.id.tv_detail_booking_date);
        if (tvDate != null) tvDate.setText(selectedDate);

        TextView tvTime = findViewById(R.id.tv_detail_booking_time);
        if (tvTime != null) tvTime.setText(selectedTime);

        String priceStr = NumberFormat.getNumberInstance(Locale.forLanguageTag("vi-VN")).format(price) + " đ";
        TextView tvFee = findViewById(R.id.tv_detail_court_fee);
        if (tvFee != null) tvFee.setText(priceStr);

        TextView tvTotal = findViewById(R.id.tv_detail_total_amount);
        if (tvTotal != null) tvTotal.setText(priceStr);

        // Hiển thị thông tin khách hàng đã nhập
        String customerName = getIntent().getStringExtra("CUSTOMER_NAME");
        String customerPhone = getIntent().getStringExtra("CUSTOMER_PHONE");
        TextView tvCustName = findViewById(R.id.tv_detail_customer_name);
        if (tvCustName != null && customerName != null) tvCustName.setText(customerName);

        TextView tvCustPhone = findViewById(R.id.tv_detail_customer_phone);
        if (tvCustPhone != null && customerPhone != null) tvCustPhone.setText(customerPhone);

        // 4. Nút Gọi điện hỗ trợ sân
        MaterialButton btnContact = findViewById(R.id.btn_contact_venue);
        if (btnContact != null) {
            btnContact.setOnClickListener(v -> {
                Toast.makeText(this, "Hotline sân: 0901 234 567", Toast.LENGTH_SHORT).show();
            });
        }

        // 5. Nút "Về trang chủ tìm sân" -> Quay về MainActivity
        MaterialButton btnBackToHome = findViewById(R.id.btn_back_to_home);
        if (btnBackToHome != null) {
            btnBackToHome.setOnClickListener(v -> {
                Intent intent = new Intent(BookingDetailActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            });
        }
    }
}
