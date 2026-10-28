package com.sportmatch.app;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.sportmatch.app.data.BookingStore;
import com.sportmatch.app.data.BookingTime;
import com.sportmatch.app.data.SampleData;
import com.sportmatch.app.model.Court;
import com.sportmatch.app.model.Venue;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/** Chọn một ngày và một khung giờ cho sân con đã chọn. */
public class SelectTimeSlotActivity extends AppCompatActivity {
    private final List<Integer> hours = new ArrayList<>();
    private final List<ViewGroup> slots = new ArrayList<>();
    private String selectedDate;
    private int selectedHour = -1;
    private Venue venue;
    private Court court;
    private BookingStore bookingStore;
    private TextView summary;
    private MaterialButton continueButton;
    private Chip todayChip;
    private Chip tomorrowChip;
    private Chip otherDateChip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_time_slot);
        ((ImageButton) findViewById(R.id.btn_back)).setOnClickListener(v -> finish());

        venue = SampleData.getVenueById(getIntent().getStringExtra("VENUE_ID"));
        court = SampleData.getCourtById(getIntent().getStringExtra("COURT_ID"));
        if (venue == null || court == null || !court.getVenueId().equals(venue.getId())) {
            Toast.makeText(this, "Không tìm thấy sân con", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        bookingStore = new BookingStore(this);

        ((TextView) findViewById(R.id.tv_venue_name)).setText(venue.getName());
        ((TextView) findViewById(R.id.tv_court_name)).setText(court.getName());
        ((TextView) findViewById(R.id.tv_court_price_rate))
                .setText(BookingTime.displayPrice(court.getPricePerHour()) + "/giờ");
        ((TextView) findViewById(R.id.tv_opening_hours))
                .setText(getString(R.string.select_slot_opening_hours, venue.getOpeningHours()));
        summary = findViewById(R.id.tv_selection_summary);
        continueButton = findViewById(R.id.btn_continue);
        createSlots();

        todayChip = findViewById(R.id.chip_today);
        tomorrowChip = findViewById(R.id.chip_tomorrow);
        otherDateChip = findViewById(R.id.chip_other_date);
        todayChip.setOnClickListener(v -> chooseDate(BookingTime.today()));
        tomorrowChip.setOnClickListener(v -> chooseDate(BookingTime.tomorrow()));
        otherDateChip.setOnClickListener(v -> showDatePicker());
        chooseDate(BookingTime.today());

        continueButton.setOnClickListener(v -> {
            if (selectedHour < 0) {
                Toast.makeText(this, "Chọn một khung giờ còn trống", Toast.LENGTH_SHORT).show();
                return;
            }
            if (BookingTime.hasStarted(selectedDate, selectedHour)
                    || bookingStore.isSlotBooked(court.getId(), selectedDate, selectedHour)) {
                Toast.makeText(this, "Khung giờ này không còn trống", Toast.LENGTH_SHORT).show();
                selectedHour = -1;
                showSlots();
                return;
            }
            Intent intent = new Intent(this, BookingConfirmActivity.class);
            intent.putExtra("VENUE_ID", venue.getId());
            intent.putExtra("COURT_ID", court.getId());
            intent.putExtra("PLAY_DATE", selectedDate);
            intent.putExtra("START_HOUR", selectedHour);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (bookingStore != null && selectedDate != null) showSlots();
    }

    private void chooseDate(String date) {
        selectedDate = date;
        selectedHour = -1;
        showSlots();
    }

    private void showDatePicker() {
        String previousDate = selectedDate;
        Calendar now = BookingTime.now();
        DatePickerDialog picker = new DatePickerDialog(this, (view, year, month, day) -> {
            Calendar chosen = BookingTime.now();
            chosen.set(year, month, day);
            String date = BookingTime.toIsoDate(chosen);
            chooseDate(date);
            otherDateChip.setText(BookingTime.displayDate(date));
            otherDateChip.setChecked(true);
        }, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));

        Calendar firstDay = BookingTime.now();
        firstDay.set(Calendar.HOUR_OF_DAY, 0);
        firstDay.set(Calendar.MINUTE, 0);
        firstDay.set(Calendar.SECOND, 0);
        picker.getDatePicker().setMinDate(firstDay.getTimeInMillis());
        picker.setOnCancelListener(dialog -> {
            if (previousDate.equals(BookingTime.today())) todayChip.setChecked(true);
            else if (previousDate.equals(BookingTime.tomorrow())) tomorrowChip.setChecked(true);
            else otherDateChip.setChecked(true);
        });
        picker.show();
    }

    private void showSlots() {
        for (int i = 0; i < slots.size(); i++) {
            final int hour = hours.get(i);
            ViewGroup slot = slots.get(i);
            boolean past = BookingTime.hasStarted(selectedDate, hour);
            boolean booked = bookingStore.isSlotBooked(court.getId(), selectedDate, hour);
            if ((past || booked) && selectedHour == hour) selectedHour = -1;

            TextView time = slot.findViewById(R.id.tv_slot_time);
            TextView status = slot.findViewById(R.id.tv_slot_status);
            time.setText(BookingTime.displaySlot(hour));
            slot.setOnClickListener(null);

            if (past || booked) {
                slot.setBackgroundResource(R.drawable.bg_slot_booked);
                time.setTextColor(ContextCompat.getColor(this, R.color.sportmatch_text_tertiary));
                status.setText(past ? R.string.status_past : R.string.status_booked);
                status.setTextColor(ContextCompat.getColor(this, R.color.sportmatch_text_tertiary));
            } else if (selectedHour == hour) {
                slot.setBackgroundResource(R.drawable.bg_slot_selected);
                time.setTextColor(Color.WHITE);
                status.setText(R.string.status_selected);
                status.setTextColor(ContextCompat.getColor(this, R.color.sportmatch_primary_container));
            } else {
                slot.setBackgroundResource(R.drawable.bg_slot_available);
                time.setTextColor(ContextCompat.getColor(this, R.color.sportmatch_text_primary));
                status.setText(R.string.status_available);
                status.setTextColor(ContextCompat.getColor(this, R.color.sportmatch_success));
            }

            if (!past && !booked) {
                slot.setOnClickListener(v -> {
                    selectedHour = hour;
                    showSlots();
                });
            }
            slot.setContentDescription(BookingTime.displaySlot(hour) + ", " + status.getText());
        }

        if (selectedHour < 0) {
            summary.setText(R.string.slot_summary_default);
        } else {
            summary.setText(BookingTime.displaySlot(selectedHour) + " | "
                    + BookingTime.displayDate(selectedDate));
        }
        boolean hasSelection = selectedHour >= 0;
        continueButton.setEnabled(hasSelection);
        continueButton.setAlpha(hasSelection ? 1f : 0.5f);
    }

    private void createSlots() {
        LinearLayout container = findViewById(R.id.slots_container);
        container.removeAllViews();
        LinearLayout row = null;

        for (int hour = venue.getOpeningHour(); hour < venue.getClosingHour(); hour++) {
            int position = hour - venue.getOpeningHour();
            if (position % 2 == 0) {
                row = new LinearLayout(this);
                row.setOrientation(LinearLayout.HORIZONTAL);
                LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                if (position > 0) rowParams.topMargin = dp(10);
                container.addView(row, rowParams);
            }

            ViewGroup slot = (ViewGroup) getLayoutInflater()
                    .inflate(R.layout.item_time_slot, row, false);
            LinearLayout.LayoutParams slotParams = new LinearLayout.LayoutParams(
                    0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
            if (position % 2 == 0) slotParams.setMarginEnd(dp(6));
            else slotParams.setMarginStart(dp(6));
            row.addView(slot, slotParams);
            hours.add(hour);
            slots.add(slot);
        }

        // Khi số khung giờ lẻ, giữ ô cuối rộng bằng các ô phía trên.
        if (hours.size() % 2 != 0 && row != null) {
            View empty = new View(this);
            LinearLayout.LayoutParams emptyParams = new LinearLayout.LayoutParams(
                    0, 1, 1);
            emptyParams.setMarginStart(dp(6));
            row.addView(empty, emptyParams);
        }
    }

    private int dp(int value) {
        return Math.round(value * getResources().getDisplayMetrics().density);
    }
}
