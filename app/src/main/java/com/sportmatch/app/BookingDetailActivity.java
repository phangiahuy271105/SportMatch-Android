package com.sportmatch.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.sportmatch.app.data.BookingStore;
import com.sportmatch.app.data.BookingTime;
import com.sportmatch.app.data.CurrentUser;
import com.sportmatch.app.data.SampleData;
import com.sportmatch.app.model.Booking;
import com.sportmatch.app.model.Court;
import com.sportmatch.app.model.Venue;

/** Màn chi tiết đọc lại lịch đã lưu, không nhận giá/ngày giờ từ màn trước. */
public class BookingDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_detail);
        ((ImageButton) findViewById(R.id.btn_back)).setOnClickListener(v -> finish());

        BookingStore store = new BookingStore(this);
        Booking booking = store.getBooking(getIntent().getStringExtra("BOOKING_ID"),
                CurrentUser.getId(this));
        Court court = booking == null ? null : SampleData.getCourtById(booking.getCourtId());
        Venue venue = court == null ? null : SampleData.getVenueById(court.getVenueId());
        if (booking == null || court == null || venue == null) {
            Toast.makeText(this, "Không tìm thấy lịch đặt", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        boolean ended = BookingTime.hasEnded(booking.getPlayDate(), booking.getStartHour());
        ((TextView) findViewById(R.id.tv_booking_detail_code)).setText(booking.getId());
        ((TextView) findViewById(R.id.tv_detail_venue_name)).setText(venue.getName());
        ((TextView) findViewById(R.id.tv_detail_venue_address)).setText(venue.getAddress());
        ((TextView) findViewById(R.id.tv_detail_court_name)).setText(court.getName());
        ((TextView) findViewById(R.id.tv_detail_sport_badge)).setText(
                SampleData.SPORT_BADMINTON.equals(court.getSportId())
                        ? R.string.sport_badminton : R.string.sport_football);
        ((TextView) findViewById(R.id.tv_detail_booking_date))
                .setText(BookingTime.displayDate(booking.getPlayDate()));
        ((TextView) findViewById(R.id.tv_detail_booking_time))
                .setText(BookingTime.displaySlot(booking.getStartHour()));
        ((TextView) findViewById(R.id.tv_detail_customer_name))
                .setText(booking.getCustomerName());
        ((TextView) findViewById(R.id.tv_detail_customer_phone))
                .setText(booking.getCustomerPhone());
        if (!booking.getNote().isEmpty()) {
            findViewById(R.id.booking_note_row).setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.tv_detail_booking_note)).setText(booking.getNote());
        }
        String price = BookingTime.displayPrice(booking.getTotalPrice());
        ((TextView) findViewById(R.id.tv_detail_court_fee)).setText(price);
        ((TextView) findViewById(R.id.tv_detail_total_amount)).setText(price);
        ((TextView) findViewById(R.id.tv_detail_payment_method)).setText(
                Booking.PAY_AT_VENUE.equals(booking.getPaymentMethod())
                        ? R.string.payment_at_venue : R.string.payment_not_selected);
        ((TextView) findViewById(R.id.tv_detail_payment_status)).setText(
                getString(R.string.payment_status_line, getString(
                        Booking.UNPAID.equals(booking.getPaymentStatus())
                                ? R.string.payment_unpaid : R.string.payment_unknown)));

        TextView status = findViewById(R.id.tv_booking_detail_status);
        status.setText(ended ? R.string.booking_status_completed
                : R.string.booking_status_confirmed);
        status.setBackgroundResource(ended
                ? R.drawable.bg_status_completed : R.drawable.bg_status_confirmed);
        status.setTextColor(ContextCompat.getColor(this, ended
                ? R.color.sportmatch_text_secondary : R.color.sportmatch_accent_dark));
        ((TextView) findViewById(R.id.tv_detail_status_banner_title)).setText(ended
                ? R.string.booking_detail_past_title
                : R.string.booking_detail_status_banner_title);

        // Các sân là dữ liệu hư cấu nên không có số điện thoại để gọi.
        findViewById(R.id.btn_contact_venue).setVisibility(View.GONE);
        View btnFindTeammates = findViewById(R.id.btn_find_teammates);
        if (ended) {
            btnFindTeammates.setVisibility(View.GONE);
        } else {
            btnFindTeammates.setVisibility(View.VISIBLE);
            btnFindTeammates.setOnClickListener(v -> {
                Intent intent = new Intent(this, CreatePostActivity.class);
                String sportName = SampleData.SPORT_BADMINTON.equals(court.getSportId())
                        ? getString(R.string.sport_badminton) : getString(R.string.sport_football);

                intent.putExtra("EXTRA_SPORT", sportName);
                intent.putExtra("EXTRA_LOCATION", venue.getName() + " - " + court.getName() + " (" + venue.getAddress() + ")");
                intent.putExtra("EXTRA_TIME", BookingTime.displaySlot(booking.getStartHour()) + " • " + BookingTime.displayDate(booking.getPlayDate()));
                intent.putExtra("EXTRA_PHONE", booking.getCustomerPhone());
                intent.putExtra("EXTRA_BOOKING_ID", booking.getId());

                startActivity(intent);
            });
        }
        findViewById(R.id.btn_back_to_home).setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });
    }
}
