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
import com.sportmatch.app.data.BookingStore;
import com.sportmatch.app.data.BookingTime;
import com.sportmatch.app.data.CurrentUser;
import com.sportmatch.app.data.SampleData;
import com.sportmatch.app.model.Booking;
import com.sportmatch.app.model.Court;
import com.sportmatch.app.model.Venue;

import java.util.Locale;
import java.util.UUID;

/** Kiểm tra lại thông tin trước khi lưu một lịch đặt mẫu trên máy. */
public class BookingConfirmActivity extends AppCompatActivity {
    private Venue venue;
    private Court court;
    private String playDate;
    private int startHour;
    private BookingStore bookingStore;
    private EditText nameInput;
    private EditText phoneInput;
    private EditText noteInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_confirm);
        ((ImageButton) findViewById(R.id.btn_back)).setOnClickListener(v -> finish());

        venue = SampleData.getVenueById(getIntent().getStringExtra("VENUE_ID"));
        court = SampleData.getCourtById(getIntent().getStringExtra("COURT_ID"));
        playDate = getIntent().getStringExtra("PLAY_DATE");
        startHour = getIntent().getIntExtra("START_HOUR", -1);
        if (venue == null || court == null || !court.getVenueId().equals(venue.getId())
                || playDate == null || startHour < venue.getOpeningHour()
                || startHour >= venue.getClosingHour()) {
            Toast.makeText(this, "Thông tin đặt sân không hợp lệ", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        bookingStore = new BookingStore(this);

        ((TextView) findViewById(R.id.tv_confirm_venue_name)).setText(venue.getName());
        ((TextView) findViewById(R.id.tv_confirm_court_name)).setText(court.getName());
        ((TextView) findViewById(R.id.tv_confirm_date))
                .setText(BookingTime.displayDate(playDate));
        ((TextView) findViewById(R.id.tv_confirm_time))
                .setText(BookingTime.displaySlot(startHour));
        String price = BookingTime.displayPrice(court.getPricePerHour());
        ((TextView) findViewById(R.id.tv_confirm_court_fee)).setText(price);
        ((TextView) findViewById(R.id.tv_confirm_total_price)).setText(price);

        nameInput = findViewById(R.id.edt_customer_name);
        phoneInput = findViewById(R.id.edt_customer_phone);
        noteInput = findViewById(R.id.edt_booking_note);
        // Địa điểm đều hư cấu nên không hiển thị số điện thoại chủ sân.
        findViewById(R.id.btn_support_call).setVisibility(View.GONE);
        MaterialButton confirmButton = findViewById(R.id.btn_confirm_booking);
        confirmButton.setOnClickListener(v -> saveBooking(confirmButton));
    }

    private void saveBooking(MaterialButton button) {
        String name = nameInput.getText().toString().trim();
        String phone = phoneInput.getText().toString().replace(" ", "").trim();
        String note = noteInput.getText().toString().trim();
        if (name.length() < 2) {
            nameInput.setError("Nhập họ tên người đặt");
            nameInput.requestFocus();
            return;
        }
        if (!phone.matches("0[0-9]{9}")) {
            phoneInput.setError("Nhập số điện thoại gồm 10 chữ số, bắt đầu bằng 0");
            phoneInput.requestFocus();
            return;
        }
        if (BookingTime.hasStarted(playDate, startHour)) {
            Toast.makeText(this, "Khung giờ này đã qua", Toast.LENGTH_SHORT).show();
            return;
        }
        if (bookingStore.isSlotBooked(court.getId(), playDate, startHour)) {
            Toast.makeText(this, "Khung giờ này đã có lịch đặt", Toast.LENGTH_SHORT).show();
            return;
        }

        // UUID tạo mã khác nhau cho các lượt đặt mẫu.
        String id = "SM-" + UUID.randomUUID().toString().substring(0, 8)
                .toUpperCase(Locale.ROOT);
        Booking booking = new Booking(id, CurrentUser.getId(), court.getId(), playDate,
                startHour, name, phone, note, court.getPricePerHour(), Booking.CONFIRMED);
        button.setEnabled(false);
        if (!bookingStore.save(booking)) {
            button.setEnabled(true);
            Toast.makeText(this, "Không lưu được lịch. Hãy kiểm tra lại khung giờ.",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(this, "Đã lưu lịch đặt mẫu trên máy", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, BookingDetailActivity.class);
        intent.putExtra("BOOKING_ID", booking.getId());
        startActivity(intent);
        finish();
    }
}
