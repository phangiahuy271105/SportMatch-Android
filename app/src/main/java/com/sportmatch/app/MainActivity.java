package com.sportmatch.app;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.chip.ChipGroup;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationBarView;
import com.sportmatch.app.data.BookingStore;
import com.sportmatch.app.data.BookingTime;
import com.sportmatch.app.data.SampleData;
import com.sportmatch.app.model.Court;
import com.sportmatch.app.model.Venue;

import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private LinearLayout venueContainer;
    private TextView resultCount;
    private TextView emptyVenues;
    private Spinner districtFilter;
    private EditText searchInput;
    private MaterialButton dateButton;
    private TextView clearDateButton;
    private BookingStore bookingStore;
    private String selectedSportId;
    private String selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        venueContainer = findViewById(R.id.venue_container);
        resultCount = findViewById(R.id.result_count);
        emptyVenues = findViewById(R.id.empty_venues);
        districtFilter = findViewById(R.id.district_filter);
        searchInput = findViewById(R.id.search_venue_input);
        dateButton = findViewById(R.id.btn_filter_date);
        clearDateButton = findViewById(R.id.btn_clear_date);
        bookingStore = new BookingStore(this);
        if (savedInstanceState != null) {
            selectedDate = savedInstanceState.getString("SELECTED_DATE");
            if (selectedDate != null) {
                dateButton.setText(BookingTime.displayDate(selectedDate));
                clearDateButton.setVisibility(View.VISIBLE);
            }
        }

        setupNavigation();
        setupFilters();
        renderVenues();

        View btnMyBookings = findViewById(R.id.btn_open_my_bookings);
        if (btnMyBookings != null) {
            btnMyBookings.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, MyBookingsActivity.class);
                startActivity(intent);
            });
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // Từ lịch đặt quay lại đúng tab Sân.
        NavigationBarView navigation = findViewById(R.id.bottom_navigation);
        navigation.setSelectedItemId(R.id.nav_venues);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (venueContainer == null) return;
        // Nếu app mở qua ngày mới, bỏ ngày lọc đã qua và cập nhật giờ còn trống.
        if (selectedDate != null && selectedDate.compareTo(BookingTime.today()) < 0) {
            clearDateFilter();
        } else {
            renderVenues();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("SELECTED_DATE", selectedDate);
    }

    private void setupNavigation() {
        NavigationBarView navigation = findViewById(R.id.bottom_navigation);
        navigation.setOnItemSelectedListener(item -> {
            int selectedId = item.getItemId();
            findViewById(R.id.venues_panel).setVisibility(
                    selectedId == R.id.nav_venues ? View.VISIBLE : View.GONE);
            findViewById(R.id.matches_panel).setVisibility(
                    selectedId == R.id.nav_matches ? View.VISIBLE : View.GONE);
            findViewById(R.id.profile_panel).setVisibility(
                    selectedId == R.id.nav_profile ? View.VISIBLE : View.GONE);
            return true;
        });
    }

    private void setupFilters() {
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                renderVenues();
            }
            @Override public void afterTextChanged(Editable s) { }
        });
        dateButton.setOnClickListener(v -> pickDate());
        clearDateButton.setOnClickListener(v -> clearDateFilter());

        ChipGroup sportFilters = findViewById(R.id.sport_filters);
        sportFilters.setOnCheckedStateChangeListener((group, checkedIds) -> {
            int checkedId = checkedIds.isEmpty() ? View.NO_ID : checkedIds.get(0);
            if (checkedId == R.id.filter_football) {
                selectedSportId = SampleData.SPORT_FOOTBALL;
            } else if (checkedId == R.id.filter_badminton) {
                selectedSportId = SampleData.SPORT_BADMINTON;
            } else {
                selectedSportId = null;
            }
            renderVenues();
        });

        districtFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                renderVenues();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                renderVenues();
            }
        });
    }

    private void renderVenues() {
        venueContainer.removeAllViews();
        String searchText = searchInput.getText().toString().trim().toLowerCase(Locale.ROOT);
        String selectedDistrict = districtFilter.getSelectedItemPosition() == 0
                ? null : districtFilter.getSelectedItem().toString();
        int visibleCount = 0;

        for (Venue venue : SampleData.getVenues()) {
            Court court = SampleData.getFirstCourtForVenue(venue.getId());
            if (court == null || (selectedSportId != null
                    && !selectedSportId.equals(court.getSportId()))
                    || (selectedDistrict != null
                    && !selectedDistrict.equals(venue.getDistrict()))
                    || !venue.getName().toLowerCase(Locale.ROOT).contains(searchText)
                    || (selectedDate != null && !hasAvailableSlot(venue, selectedDate))) {
                continue;
            }

            View card = getLayoutInflater().inflate(R.layout.item_venue, venueContainer, false);
            bindVenueCard(card, venue, court);
            venueContainer.addView(card);
            visibleCount++;
        }

        resultCount.setText(getString(R.string.venue_count, visibleCount));
        emptyVenues.setText(selectedDate == null
                ? R.string.empty_venues_without_date : R.string.empty_venues);
        emptyVenues.setVisibility(visibleCount == 0 ? View.VISIBLE : View.GONE);
    }

    private boolean hasAvailableSlot(Venue venue, String date) {
        for (Court court : SampleData.getCourtsForVenue(venue.getId())) {
            for (int hour = venue.getOpeningHour(); hour < venue.getClosingHour(); hour++) {
                if (!BookingTime.hasStarted(date, hour)
                        && !bookingStore.isSlotBooked(court.getId(), date, hour)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void pickDate() {
        Calendar today = BookingTime.now();
        DatePickerDialog picker = new DatePickerDialog(this, (view, year, month, day) -> {
            Calendar chosen = BookingTime.now();
            chosen.set(year, month, day);
            selectedDate = BookingTime.toIsoDate(chosen);
            dateButton.setText(BookingTime.displayDate(selectedDate));
            clearDateButton.setVisibility(View.VISIBLE);
            renderVenues();
        }, today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH));

        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        picker.getDatePicker().setMinDate(today.getTimeInMillis());
        picker.show();
    }

    private void clearDateFilter() {
        selectedDate = null;
        dateButton.setText(R.string.filter_any_date);
        clearDateButton.setVisibility(View.GONE);
        renderVenues();
    }

    private void bindVenueCard(View card, Venue venue, Court court) {
        boolean football = SampleData.SPORT_FOOTBALL.equals(court.getSportId());
        int bannerColor = football ? R.color.sportmatch_football : R.color.sportmatch_badminton;
        FrameLayout banner = card.findViewById(R.id.venue_banner);
        banner.setBackgroundColor(ContextCompat.getColor(this, bannerColor));
        ((ImageView) card.findViewById(R.id.venue_photo)).setImageResource(venue.getImageResId());

        TextView sport = card.findViewById(R.id.venue_sport);
        TextView name = card.findViewById(R.id.venue_name);
        TextView address = card.findViewById(R.id.venue_address);
        TextView price = card.findViewById(R.id.venue_price);
        sport.setText(football ? R.string.sport_football : R.string.sport_badminton);
        name.setText(venue.getName());
        address.setText(venue.getAddress());
        ((TextView) card.findViewById(R.id.venue_hours))
                .setText(getString(R.string.select_slot_opening_hours, venue.getOpeningHours()));
        String formattedPrice = NumberFormat.getNumberInstance(Locale.forLanguageTag("vi-VN"))
                .format(court.getPricePerHour()) + "đ";
        price.setText(getString(R.string.price_from, formattedPrice));

        // Bấm vào thẻ sân để mở Màn hình 2: Chi tiết sân
        card.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, VenueDetailActivity.class);
            intent.putExtra("VENUE_ID", venue.getId());
            if (selectedDate != null) intent.putExtra("PLAY_DATE", selectedDate);
            startActivity(intent);
        });
    }
}
