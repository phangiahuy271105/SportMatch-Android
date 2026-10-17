package com.sportmatch.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Màn hình 3: Chọn ngày và khung giờ
 * Trình độ sinh viên năm 3: Chọn giờ thi đấu và chuyển sang Màn hình 4.
 */
public class SelectTimeSlotActivity extends AppCompatActivity {

    private String venueName = "Sân bóng đá Chảo Lửa";
    private String courtName = "Sân số 1 (5 người)";
    private int price = 180000;
    private String selectedTime = "18:00 – 19:00";
    private String selectedDate = "Hôm nay, 18/10/2026";

    private TextView tvSummary;
    private View selectedSlotView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_time_slot);

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
        if (getIntent().hasExtra("PRICE")) {
            price = getIntent().getIntExtra("PRICE", 180000);
        }

        TextView tvVenue = findViewById(R.id.tv_venue_name);
        if (tvVenue != null) tvVenue.setText(venueName);

        TextView tvCourt = findViewById(R.id.tv_court_name);
        if (tvCourt != null) tvCourt.setText(courtName);

        TextView tvCourtPrice = findViewById(R.id.tv_court_price_rate);
        if (tvCourtPrice != null) {
            String formatted = NumberFormat.getNumberInstance(Locale.forLanguageTag("vi-VN")).format(price) + " đ/giờ";
            tvCourtPrice.setText(formatted);
        }

        tvSummary = findViewById(R.id.tv_selection_summary);
        updateSummaryText();

        // 3. Gắn sự kiện click cho các ô khung giờ
        setupSlotClick(R.id.slot_16_17, "16:00 – 17:00");
        setupSlotClick(R.id.slot_17_18, "17:00 – 18:00");
        setupSlotClick(R.id.slot_18_19, "18:00 – 19:00");
        setupSlotClick(R.id.slot_19_20, "19:00 – 20:00");
        setupSlotClick(R.id.slot_20_21, "20:00 – 21:00");
        setupSlotClick(R.id.slot_21_22, "21:00 – 22:00");

        // 4. Nút "Tiếp tục" -> Chuyển sang Màn hình 4 (Xác nhận đặt sân)
        MaterialButton btnContinue = findViewById(R.id.btn_continue);
        if (btnContinue != null) {
            btnContinue.setOnClickListener(v -> {
                Intent intent = new Intent(SelectTimeSlotActivity.this, BookingConfirmActivity.class);
                intent.putExtra("VENUE_NAME", venueName);
                intent.putExtra("COURT_NAME", courtName);
                intent.putExtra("SELECTED_DATE", selectedDate);
                intent.putExtra("SELECTED_TIME", selectedTime);
                intent.putExtra("PRICE", price);
                startActivity(intent);
            });
        }
    }

    private void setupSlotClick(int slotId, String timeText) {
        View slot = findViewById(slotId);
        if (slot != null) {
            slot.setOnClickListener(v -> {
                // Đổi trạng thái hiển thị
                if (selectedSlotView != null && selectedSlotView != slot) {
                    selectedSlotView.setBackgroundResource(R.drawable.bg_slot_available);
                }
                selectedSlotView = slot;
                slot.setBackgroundResource(R.drawable.bg_slot_selected);
                selectedTime = timeText;
                updateSummaryText();
            });
        }
    }

    private void updateSummaryText() {
        if (tvSummary != null) {
            tvSummary.setText("Đã chọn: " + selectedTime + " | " + selectedDate);
        }
    }
}
