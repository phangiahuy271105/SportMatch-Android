package com.sportmatch.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.sportmatch.app.data.BookingStore;
import com.sportmatch.app.data.BookingTime;
import com.sportmatch.app.data.CurrentUser;
import com.sportmatch.app.data.SampleData;
import com.sportmatch.app.model.Booking;
import com.sportmatch.app.model.Court;
import com.sportmatch.app.model.Venue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/** Hiển thị đúng các lịch đã lưu của người dùng demo. */
public class MyBookingsActivity extends AppCompatActivity {
    private final List<Booking> upcoming = new ArrayList<>();
    private final List<Booking> past = new ArrayList<>();
    private TextView upcomingTab;
    private TextView pastTab;
    private LinearLayout emptyLayout;
    private BookingAdapter adapter;
    private boolean showingUpcoming = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bookings);
        ((ImageButton) findViewById(R.id.btn_back)).setOnClickListener(v -> finish());
        upcomingTab = findViewById(R.id.tab_upcoming);
        pastTab = findViewById(R.id.tab_past);
        emptyLayout = findViewById(R.id.layout_empty_bookings);

        RecyclerView list = findViewById(R.id.rv_bookings);
        list.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BookingAdapter();
        list.setAdapter(adapter);
        upcomingTab.setOnClickListener(v -> showTab(true));
        pastTab.setOnClickListener(v -> showTab(false));

        MaterialButton explore = findViewById(R.id.btn_explore_venues);
        explore.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });
        showTab(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadBookings();
    }

    private void loadBookings() {
        upcoming.clear();
        past.clear();
        BookingStore store = new BookingStore(this);
        for (Booking booking : store.getBookingsForUser(CurrentUser.getId())) {
            if (BookingTime.hasEnded(booking.getPlayDate(), booking.getStartHour())) {
                past.add(booking);
            } else {
                upcoming.add(booking);
            }
        }
        // Ngày yyyy-MM-dd sắp theo chữ cũng là thứ tự thời gian.
        Collections.sort(upcoming, (a, b) -> bookingKey(a).compareTo(bookingKey(b)));
        Collections.sort(past, (a, b) -> bookingKey(b).compareTo(bookingKey(a)));
        showTab(showingUpcoming);
    }

    private String bookingKey(Booking booking) {
        return booking.getPlayDate() + String.format(Locale.US, "%02d", booking.getStartHour());
    }

    private void showTab(boolean showUpcoming) {
        showingUpcoming = showUpcoming;
        upcomingTab.setBackgroundResource(showUpcoming
                ? R.drawable.bg_tab_selected : R.drawable.bg_tab_unselected);
        pastTab.setBackgroundResource(showUpcoming
                ? R.drawable.bg_tab_unselected : R.drawable.bg_tab_selected);
        upcomingTab.setTextColor(ContextCompat.getColor(this, showUpcoming
                ? R.color.white : R.color.sportmatch_text_secondary));
        pastTab.setTextColor(ContextCompat.getColor(this, showUpcoming
                ? R.color.sportmatch_text_secondary : R.color.white));
        if (adapter != null) {
            adapter.setBookings(showUpcoming ? upcoming : past);
            emptyLayout.setVisibility(adapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
        }
    }

    private class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.Holder> {
        private final List<Booking> items = new ArrayList<>();

        void setBookings(List<Booking> bookings) {
            items.clear();
            items.addAll(bookings);
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View row = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_booking, parent, false);
            return new Holder(row);
        }

        @Override
        public void onBindViewHolder(@NonNull Holder holder, int position) {
            Booking booking = items.get(position);
            Court court = SampleData.getCourtById(booking.getCourtId());
            Venue venue = court == null ? null : SampleData.getVenueById(court.getVenueId());
            boolean ended = BookingTime.hasEnded(booking.getPlayDate(), booking.getStartHour());

            holder.venue.setText(venue == null ? "Sân mẫu đã thay đổi" : venue.getName());
            holder.court.setText(court == null ? "Không tìm thấy sân con" : court.getName());
            holder.address.setText(venue == null ? "" : venue.getAddress());
            holder.dateTime.setText(BookingTime.displaySlot(booking.getStartHour())
                    + " | " + BookingTime.displayDate(booking.getPlayDate()));
            holder.code.setText("Mã: " + booking.getId());
            holder.price.setText(BookingTime.displayPrice(booking.getTotalPrice()));
            holder.status.setText(ended
                    ? R.string.booking_status_completed : R.string.booking_status_confirmed);
            holder.status.setBackgroundResource(ended
                    ? R.drawable.bg_status_completed : R.drawable.bg_status_confirmed);
            holder.status.setTextColor(ContextCompat.getColor(MyBookingsActivity.this, ended
                    ? R.color.sportmatch_text_secondary : R.color.sportmatch_accent_dark));
            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(MyBookingsActivity.this, BookingDetailActivity.class);
                intent.putExtra("BOOKING_ID", booking.getId());
                startActivity(intent);
            });
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        class Holder extends RecyclerView.ViewHolder {
            TextView venue, court, dateTime, address, code, price, status;

            Holder(View row) {
                super(row);
                venue = row.findViewById(R.id.tv_booking_item_venue_name);
                court = row.findViewById(R.id.tv_booking_item_court);
                dateTime = row.findViewById(R.id.tv_booking_item_datetime);
                address = row.findViewById(R.id.tv_booking_item_address);
                code = row.findViewById(R.id.tv_booking_item_code);
                price = row.findViewById(R.id.tv_booking_item_price);
                status = row.findViewById(R.id.tv_booking_item_status);
            }
        }
    }
}
