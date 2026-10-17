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
import com.google.android.material.chip.ChipGroup;

import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Màn hình 3: Chọn ngày và khung giờ
 * Trình độ sinh viên năm 3:
 * - Tự động so sánh với giờ hiện tại của hệ thống (Calendar): Khung giờ đã qua trong ngày hôm nay sẽ không cho đặt.
 * - Cho phép chọn Ngày mai hoặc Ngày khác qua DatePickerDialog để mở lại các khung giờ hợp lệ.
 */
public class SelectTimeSlotActivity extends AppCompatActivity {

    private String venueName = "Sân bóng Bình Thạnh";
    private String courtName = "Sân số 1 (Sân 5 người)";
    private int price = 220000;
    private String selectedTime = "";
    private String selectedDate = "";

    private TextView tvSummary;
    private ViewGroup currentSelectedSlot = null;

    // Các trạng thái của một ô khung giờ
    private static final int STATE_AVAILABLE = 1; // Còn trống
    private static final int STATE_SELECTED = 2;  // Đang chọn
    private static final int STATE_BOOKED = 3;    // Hết chỗ (đã có người đặt)
    private static final int STATE_PAST = 4;      // Đã qua giờ (trong ngày hôm nay)

    // Danh sách 6 khung giờ thi đấu phổ biến
    private ViewGroup[] slotViews;
    private final int[] startHours = {16, 17, 18, 19, 20, 21};
    private final String[] timeTexts = {
            "16:00 – 17:00",
            "17:00 – 18:00",
            "18:00 – 19:00",
            "19:00 – 20:00",
            "20:00 – 21:00",
            "21:00 – 22:00"
    };

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

        // Khởi tạo danh sách các ô khung giờ trên giao diện
        initSlotViews();

        // 3. Cài đặt chọn ngày thi đấu (Hôm nay, Ngày mai, Ngày khác)
        setupDateChips();

        // 4. Nút "Tiếp tục" -> Chuyển sang Màn hình 4 (Xác nhận đặt sân)
        MaterialButton btnContinue = findViewById(R.id.btn_continue);
        if (btnContinue != null) {
            btnContinue.setOnClickListener(v -> {
                if (currentSelectedSlot == null || selectedTime.isEmpty() || selectedTime.startsWith("Đã hết")) {
                    Toast.makeText(this, "Vui lòng chọn một khung giờ còn trống!", Toast.LENGTH_SHORT).show();
                    return;
                }
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

    private void initSlotViews() {
        slotViews = new ViewGroup[]{
                findViewById(R.id.slot_16_17),
                findViewById(R.id.slot_17_18),
                findViewById(R.id.slot_18_19),
                findViewById(R.id.slot_19_20),
                findViewById(R.id.slot_20_21),
                findViewById(R.id.slot_21_22)
        };
    }

    private void setupDateChips() {
        Chip chipToday = findViewById(R.id.chip_today);
        Chip chipTomorrow = findViewById(R.id.chip_tomorrow);
        Chip chipOther = findViewById(R.id.chip_other_date);

        // Lấy ngày hiện tại thực tế trên máy
        Calendar now = Calendar.getInstance();
        int day = now.get(Calendar.DAY_OF_MONTH);
        int month = now.get(Calendar.MONTH) + 1;
        int year = now.get(Calendar.YEAR);
        String todayString = String.format(Locale.getDefault(), "Hôm nay, %02d/%02d/%d", day, month, year);

        // Tính ngày mai
        Calendar tmw = Calendar.getInstance();
        tmw.add(Calendar.DAY_OF_YEAR, 1);
        int tmwDay = tmw.get(Calendar.DAY_OF_MONTH);
        int tmwMonth = tmw.get(Calendar.MONTH) + 1;
        int tmwYear = tmw.get(Calendar.YEAR);
        String tomorrowString = String.format(Locale.getDefault(), "Ngày mai, %02d/%02d/%d", tmwDay, tmwMonth, tmwYear);

        // Mặc định ban đầu chọn "Hôm nay"
        selectedDate = todayString;
        updateTimeSlots(true);

        ChipGroup dateChips = findViewById(R.id.date_chips);
        if (dateChips != null) {
            dateChips.setOnCheckedStateChangeListener((group, checkedIds) -> {
                if (checkedIds.isEmpty()) return;
                int id = checkedIds.get(0);
                if (id == R.id.chip_today) {
                    selectedDate = todayString;
                    updateTimeSlots(true);
                } else if (id == R.id.chip_tomorrow) {
                    selectedDate = tomorrowString;
                    updateTimeSlots(false);
                }
            });
        }

        if (chipToday != null) {
            chipToday.setOnClickListener(v -> {
                selectedDate = todayString;
                updateTimeSlots(true);
            });
        }

        if (chipTomorrow != null) {
            chipTomorrow.setOnClickListener(v -> {
                selectedDate = tomorrowString;
                updateTimeSlots(false);
            });
        }

        if (chipOther != null) {
            chipOther.setOnClickListener(v -> {
                // Mở hộp thoại chọn ngày chuẩn của Android
                DatePickerDialog datePicker = new DatePickerDialog(this,
                        (view, y, m, d) -> {
                            boolean isChosenToday = (d == day && (m + 1) == month && y == year);
                            selectedDate = String.format(Locale.getDefault(), "%02d/%02d/%d", d, m + 1, y);
                            chipOther.setText(selectedDate);
                            chipOther.setChecked(true);
                            updateTimeSlots(isChosenToday);
                        },
                        year,
                        month - 1,
                        day);
                datePicker.getDatePicker().setMinDate(System.currentTimeMillis());
                datePicker.show();
            });
        }
    }

    /**
     * Cập nhật trạng thái các khung giờ dựa theo thời gian thực:
     * - isToday = true: So sánh giờ thực tế hiện tại. Ca nào <= giờ hiện tại sẽ thành "Đã qua giờ".
     * - isToday = false: Ca ở ngày tương lai nên các ca trước đó được mở lại bình thường.
     */
    private void updateTimeSlots(boolean isToday) {
        Calendar now = Calendar.getInstance();
        int currentHour = now.get(Calendar.HOUR_OF_DAY);

        boolean isSelectedStillValid = false;
        ViewGroup firstAvailableSlot = null;
        String firstAvailableTime = null;

        for (int i = 0; i < slotViews.length; i++) {
            final ViewGroup slot = slotViews[i];
            final String timeText = timeTexts[i];
            int startHour = startHours[i];

            if (slot == null) continue;

            // 1. Kiểm tra nếu là ngày hôm nay và khung giờ đã qua so với giờ hiện tại
            if (isToday && startHour <= currentHour) {
                setSlotDisplay(slot, timeText, STATE_PAST);
                slot.setOnClickListener(v ->
                        Toast.makeText(this, "Khung giờ " + timeText + " đã qua trong ngày hôm nay!", Toast.LENGTH_SHORT).show()
                );
                if (slot == currentSelectedSlot) {
                    currentSelectedSlot = null;
                }
            }
            // 2. Giả lập một khung giờ đã có người khác đặt trước (ví dụ 19:00 - 20:00)
            else if (startHour == 19) {
                setSlotDisplay(slot, timeText, STATE_BOOKED);
                slot.setOnClickListener(v ->
                        Toast.makeText(this, "Khung giờ " + timeText + " đã có người đặt trước!", Toast.LENGTH_SHORT).show()
                );
                if (slot == currentSelectedSlot) {
                    currentSelectedSlot = null;
                }
            }
            // 3. Khung giờ còn trống và hợp lệ để đặt
            else {
                if (firstAvailableSlot == null) {
                    firstAvailableSlot = slot;
                    firstAvailableTime = timeText;
                }

                // Nếu khung giờ này đang được chọn trước đó và vẫn hợp lệ
                if (timeText.equals(selectedTime)) {
                    isSelectedStillValid = true;
                    currentSelectedSlot = slot;
                    setSlotDisplay(slot, timeText, STATE_SELECTED);
                } else {
                    setSlotDisplay(slot, timeText, STATE_AVAILABLE);
                }

                // Gán sự kiện bấm chọn cho ô còn trống
                slot.setOnClickListener(v -> {
                    // Nếu đang có ô được chọn khác thì trả ô đó về trạng thái còn trống
                    if (currentSelectedSlot != null && currentSelectedSlot != slot) {
                        for (int j = 0; j < slotViews.length; j++) {
                            if (slotViews[j] == currentSelectedSlot) {
                                setSlotDisplay(currentSelectedSlot, timeTexts[j], STATE_AVAILABLE);
                                break;
                            }
                        }
                    }

                    currentSelectedSlot = slot;
                    selectedTime = timeText;
                    setSlotDisplay(slot, timeText, STATE_SELECTED);
                    updateSummaryText();
                });
            }
        }

        // Tự động chọn khung giờ còn trống đầu tiên nếu lựa chọn trước đó không còn hợp lệ
        if (!isSelectedStillValid && firstAvailableSlot != null) {
            currentSelectedSlot = firstAvailableSlot;
            selectedTime = firstAvailableTime;
            setSlotDisplay(firstAvailableSlot, firstAvailableTime, STATE_SELECTED);
        } else if (!isSelectedStillValid) {
            currentSelectedSlot = null;
            selectedTime = "Đã hết giờ nhận đặt trong ngày";
        }

        updateSummaryText();
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
        } else if (state == STATE_PAST) {
            slot.setBackgroundResource(R.drawable.bg_slot_booked);
            tvTime.setTextColor(ContextCompat.getColor(this, R.color.sportmatch_text_tertiary));
            tvStatus.setText("Đã qua giờ");
            tvStatus.setTextColor(ContextCompat.getColor(this, R.color.sportmatch_text_tertiary));
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
