package com.sportmatch.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Màn hình 4: Xác nhận đặt sân
 * Trình độ sinh viên năm 3: Kiểm tra thông tin đặt sân, nhập thông tin khách hàng và hoàn tất.
 */
public class BookingConfirmActivity extends AppCompatActivity {

    private String venueName = "Sân bóng Bình Thạnh";
    private String courtName = "Sân số 1 (5 người)";
    private String selectedDate = "Hôm nay, 18/10/2026";
    private String selectedTime = "17:00 – 18:00";
    private int price = 220000;

    private EditText edtCustomerName;
    private EditText edtCustomerPhone;
    private EditText edtBookingNote;

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
            price = getIntent().getIntExtra("PRICE", 220000);
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

        edtCustomerName = findViewById(R.id.edt_customer_name);
        edtCustomerPhone = findViewById(R.id.edt_customer_phone);
        edtBookingNote = findViewById(R.id.edt_booking_note);

        // 4. Nút bấm gọi hotline hỗ trợ khách hàng
        View btnSupport = findViewById(R.id.btn_support_call);
        if (btnSupport != null) {
            btnSupport.setOnClickListener(v -> {
                Toast.makeText(this, "Đang kết nối hotline chủ sân: 0901 234 567", Toast.LENGTH_SHORT).show();
            });
        }

        // 5. Nút "Xác nhận đặt sân" -> Chuyển sang Màn hình 6 (Chi tiết lịch đặt thành công)
        MaterialButton btnConfirm = findViewById(R.id.btn_confirm_booking);
        if (btnConfirm != null) {
            btnConfirm.setOnClickListener(v -> {
                String name = edtCustomerName != null ? edtCustomerName.getText().toString().trim() : "";
                String phone = edtCustomerPhone != null ? edtCustomerPhone.getText().toString().trim() : "";

                if (name.isEmpty()) {
                    Toast.makeText(this, "Vui lòng nhập họ tên người đặt sân!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (phone.isEmpty()) {
                    Toast.makeText(this, "Vui lòng nhập số điện thoại liên hệ!", Toast.LENGTH_SHORT).show();
                    return;
                }

                Toast.makeText(BookingConfirmActivity.this, "Đặt sân thành công!", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(BookingConfirmActivity.this, BookingDetailActivity.class);
                intent.putExtra("VENUE_NAME", venueName);
                intent.putExtra("COURT_NAME", courtName);
                intent.putExtra("SELECTED_DATE", selectedDate);
                intent.putExtra("SELECTED_TIME", selectedTime);
                intent.putExtra("CUSTOMER_NAME", name);
                intent.putExtra("CUSTOMER_PHONE", phone);
                intent.putExtra("PRICE", price);
                intent.putExtra("BOOKING_CODE", "SM-2026-" + (1000 + (int)(Math.random() * 8999)));
                startActivity(intent);
            });
        }
    }
}
