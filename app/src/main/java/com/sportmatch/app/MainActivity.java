package com.sportmatch.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
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
import com.google.android.material.navigation.NavigationBarView;
import com.sportmatch.app.data.SampleData;
import com.sportmatch.app.model.Court;
import com.sportmatch.app.model.Venue;

import java.text.NumberFormat;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private LinearLayout venueContainer;
    private TextView resultCount;
    private TextView emptyVenues;
    private Spinner districtFilter;
    private String selectedSportId;

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
        String selectedDistrict = districtFilter.getSelectedItemPosition() == 0
                ? null : districtFilter.getSelectedItem().toString();
        int visibleCount = 0;

        for (Venue venue : SampleData.getVenues()) {
            Court court = SampleData.getFirstCourtForVenue(venue.getId());
            if (court == null || (selectedSportId != null
                    && !selectedSportId.equals(court.getSportId()))
                    || (selectedDistrict != null
                    && !selectedDistrict.equals(venue.getDistrict()))) {
                continue;
            }

            View card = getLayoutInflater().inflate(R.layout.item_venue, venueContainer, false);
            bindVenueCard(card, venue, court);
            venueContainer.addView(card);
            visibleCount++;
        }

        resultCount.setText(getString(R.string.venue_count, visibleCount));
        emptyVenues.setVisibility(visibleCount == 0 ? View.VISIBLE : View.GONE);
    }

    private void bindVenueCard(View card, Venue venue, Court court) {
        boolean football = SampleData.SPORT_FOOTBALL.equals(court.getSportId());
        int bannerColor = football ? R.color.sportmatch_football : R.color.sportmatch_badminton;
        FrameLayout banner = card.findViewById(R.id.venue_banner);
        banner.setBackgroundColor(ContextCompat.getColor(this, bannerColor));

        TextView sport = card.findViewById(R.id.venue_sport);
        TextView name = card.findViewById(R.id.venue_name);
        TextView address = card.findViewById(R.id.venue_address);
        TextView price = card.findViewById(R.id.venue_price);
        sport.setText(football ? R.string.sport_football : R.string.sport_badminton);
        name.setText(venue.getName());
        address.setText(venue.getAddress());
        String formattedPrice = NumberFormat.getNumberInstance(Locale.forLanguageTag("vi-VN"))
                .format(court.getPricePerHour()) + "đ";
        price.setText(getString(R.string.price_from, formattedPrice));

        // Bấm vào thẻ sân để mở Màn hình 2: Chi tiết sân
        card.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, VenueDetailActivity.class);
            intent.putExtra("VENUE_ID", venue.getId());
            intent.putExtra("VENUE_NAME", venue.getName());
            intent.putExtra("VENUE_ADDRESS", venue.getAddress());
            intent.putExtra("PRICE", court.getPricePerHour());
            startActivity(intent);
        });
    }
}
