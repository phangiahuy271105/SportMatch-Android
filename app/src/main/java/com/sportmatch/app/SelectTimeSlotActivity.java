package com.sportmatch.app;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;

import java.text.NumberFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Màn hình 3: Chọn ngày và khung giờ
 * Trình độ sinh viên năm 3: Chọn ngày thi đấu (kèm DatePickerDialog), chọn giờ rõ ràng không bị lỗi màu.
 */
public class SelectTimeSlotActivity extends AppCompatActivity {

    private String venueName = "Sân bóng Bình Thạnh";
    private String courtName = "Sân số 1 (Sân 5 người)";
    private int price = 220000;
    private String selectedTime = "17:00 – 18:00";
    private String selectedDate = "Hôm nay, 18/10/2026";

    private TextView tvSummary;
    private ViewGroup currentSelectedSlot = null;

    private static final int STATE_AVAILABLE = 1;
    private static final int STATE_SELECTED = 2;
    private static final int STATE_BOOKED = 3;

    private final Map<ViewGroup, String> availableSlotsMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_time_slot);

        // 1. Nút quay lại
        ImageButton btnBack = findViewById(R.id.btn_back);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        // 2. Nhận dữ liệu truyền sang từ Màn 2
        if (getIntent().hasExtra("VENUE_NAME")) {
            venueName = getIntent().getStringExtra("VENUE_NAME");
        }
        if (getIntent().hasExtra("COURT_NAME")) {
            courtName = getIntent().getStringExtra("COURT_NAME");
        }
        if (getIntent().hasExtra("PRICE")) {
            price = getIntent().getIntExtra("PRICE", 220000);
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

        // 3. Cài đặt chọn ngày thi đấu (Hôm nay, Ngày mai, Ngày khác)
        setupDateChips();

        // 4. Cài đặt các khung giờ thi đấu (Còn trống, Đang chọn, Hết chỗ)
        setupTimeSlots();

        // 5. Nút "Tiếp tục" -> Chuyển sang Màn hình 4 (Xác nhận đặt sân)
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

    private void setupDateChips() {
        Chip chipToday = findViewById(R.id.chip_today);
        Chip chipTomorrow = findViewById(R.id.chip_tomorrow);
        Chip chipOther = findViewById(R.id.chip_other_date);

        if (chipToday != null) {
            chipToday.setOnClickListener(v -> {
                selectedDate = "Hôm nay, 18/10/2026";
                updateSummaryText();
            });
        }

        if (chipTomorrow != null) {
            chipTomorrow.setOnClickListener(v -> {
                selectedDate = "Ngày mai, 19/10/2026";
                updateSummaryText();
            });
        }

        if (chipOther != null) {
            chipOther.setOnClickListener(v -> {
                // Mở hộp thoại chọn ngày chuẩn của Android
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog datePicker = new DatePickerDialog(this,
                        (view, year, month, dayOfMonth) -> {
                            selectedDate = String.format(Locale.getDefault(), "%02d/%02d/%d", dayOfMonth, month + 1, year);
                            chipOther.setText(selectedDate);
                            chipOther.setChecked(true);
                            updateSummaryText();
                        },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
                datePicker.getDatePicker().setMinDate(System.currentTimeMillis());
                datePicker.show();
            });
        }
    }

    private void setupTimeSlots() {
        ViewGroup slot1 = findViewById(R.id.slot_16_17);
        ViewGroup slot2 = findViewById(R.id.slot_17_18);
        ViewGroup slot3 = findViewById(R.id.slot_18_19);
        ViewGroup slot4 = findViewById(R.id.slot_19_20);
        ViewGroup slot5 = findViewById(R.id.slot_20_21);
        ViewGroup slot6 = findViewById(R.id.slot_21_22);

        // Khung giờ 1: 16:00 - 17:00 (Còn trống)
        setSlotDisplay(slot1, "16:00 – 17:00", STATE_AVAILABLE);
        registerAvailableSlot(slot1, "16:00 – 17:00");

        // Khung giờ 2: 17:00 - 18:00 (Đang chọn mặc định)
        setSlotDisplay(slot2, "17:00 – 18:00", STATE_SELECTED);
        registerAvailableSlot(slot2, "17:00 – 18:00");
        currentSelectedSlot = slot2;

        // Khung giờ 3 & 4: Hết chỗ (Không cho chọn)
        setSlotDisplay(slot3, "18:00 – 19:00", STATE_BOOKED);
        if (slot3 != null) {
            slot3.setOnClickListener(v -> Toast.makeText(this, "Khung giờ 18:00 – 19:00 đã hết chỗ!", Toast.LENGTH_SHORT).show());
        }

        setSlotDisplay(slot4, "19:00 – 20:00", STATE_BOOKED);
        if (slot4 != null) {
            slot4.setOnClickListener(v -> Toast.makeText(this, "Khung giờ 19:00 – 20:00 đã hết chỗ!", Toast.LENGTH_SHORT).show());
        }

        // Khung giờ 5: 20:00 - 21:00 (Còn trống)
        setSlotDisplay(slot5, "20:00 – 21:00", STATE_AVAILABLE);
        registerAvailableSlot(slot5, "20:00 – 21:00");

        // Khung giờ 6: 21:00 - 22:00 (Còn trống)
        setSlotDisplay(slot6, "21:00 – 22:00", STATE_AVAILABLE);
        registerAvailableSlot(slot6, "21:00 – 22:00");
    }

    private void registerAvailableSlot(ViewGroup slot, String timeText) {
        if (slot == null) return;
        availableSlotsMap.put(slot, timeText);

        slot.setOnClickListener(v -> {
            // Đưa ô đang chọn trước đó về trạng thái còn trống
            if (currentSelectedSlot != null && currentSelectedSlot != slot) {
                String prevTime = availableSlotsMap.get(currentSelectedSlot);
                setSlotDisplay(currentSelectedSlot, prevTime != null ? prevTime : "", STATE_AVAILABLE);
            }

            // Đặt ô vừa bấm thành ô đang chọn
            currentSelectedSlot = slot;
            selectedTime = timeText;
            setSlotDisplay(slot, timeText, STATE_SELECTED);
            updateSummaryText();
        });
    }

    private void setSlotDisplay(ViewGroup slot, String time, int state) {
        if (slot == null || slot.getChildCount() < 2) return;
        TextView tvTime = (TextView) slot.getChildAt(0);
        TextView tvStatus = (TextView) slot.getChildAt(1);

        tvTime.setText(time);

        if (state == STATE_SELECTED) {
            slot.setBackgroundResource(R.drawable.bg_slot_selected);
            tvTime.setTextColor(ContextCompat.getColor(this, R.color.white));
            tvStatus.setText("✓ Đang chọn");
            tvStatus.setTextColor(Color.parseColor("#D9EAFD"));
        } else if (state == STATE_AVAILABLE) {
            slot.setBackgroundResource(R.drawable.bg_slot_available);
            tvTime.setTextColor(ContextCompat.getColor(this, R.color.sportmatch_text_primary));
            tvStatus.setText("Còn trống");
            tvStatus.setTextColor(ContextCompat.getColor(this, R.color.sportmatch_success));
        } else { // STATE_BOOKED
            slot.setBackgroundResource(R.drawable.bg_slot_booked);
            tvTime.setTextColor(ContextCompat.getColor(this, R.color.sportmatch_text_tertiary));
            tvStatus.setText("Hết chỗ");
            tvStatus.setTextColor(ContextCompat.getColor(this, R.color.sportmatch_text_tertiary));
        }
    }

    private void updateSummaryText() {
        if (tvSummary != null) {
            tvSummary.setText("Đã chọn: " + selectedTime + " | " + selectedDate);
        }
    }
}
