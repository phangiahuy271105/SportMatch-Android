package com.sportmatch.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.sportmatch.app.data.BookingTime;
import com.sportmatch.app.data.SampleData;
import com.sportmatch.app.model.Court;
import com.sportmatch.app.model.Venue;

import java.util.ArrayList;
import java.util.List;

/** Hiển thị thông tin riêng của địa điểm và cho chọn một sân con. */
public class VenueDetailActivity extends AppCompatActivity {
    private Venue venue;
    private Court selectedCourt;
    private final List<Court> courts = new ArrayList<>();
    private final List<MaterialCardView> cards = new ArrayList<>();
    private final List<ImageView> radioIcons = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venue_detail);

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());
        venue = SampleData.getVenueById(getIntent().getStringExtra("VENUE_ID"));
        if (venue == null) {
            Toast.makeText(this, "Không tìm thấy sân này", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        courts.addAll(SampleData.getCourtsForVenue(venue.getId()));
        if (courts.isEmpty()) {
            Toast.makeText(this, "Sân chưa có khung đặt", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        selectedCourt = courts.get(0);

        ((TextView) findViewById(R.id.venue_detail_name)).setText(venue.getName());
        ((TextView) findViewById(R.id.venue_detail_address)).setText(venue.getAddress());
        ((TextView) findViewById(R.id.venue_detail_description)).setText(venue.getDescription());
        ((TextView) findViewById(R.id.venue_detail_hours)).setText(venue.getOpeningHours());
        ((TextView) findViewById(R.id.venue_detail_rules)).setText(venue.getRules());

        boolean badminton = SampleData.SPORT_BADMINTON.equals(selectedCourt.getSportId());
        ((ImageView) findViewById(R.id.iv_venue_banner_img)).setImageResource(
                badminton ? R.drawable.ic_badminton_banner : R.drawable.ic_stadium_banner);
        ((TextView) findViewById(R.id.venue_detail_sport)).setText(
                badminton ? R.string.sport_badminton : R.string.sport_football);

        showAmenities();
        showCourts();
        updatePrice();

        MaterialButton button = findViewById(R.id.btn_select_time_slot);
        button.setOnClickListener(v -> {
            Intent intent = new Intent(this, SelectTimeSlotActivity.class);
            intent.putExtra("VENUE_ID", venue.getId());
            intent.putExtra("COURT_ID", selectedCourt.getId());
            startActivity(intent);
        });
    }

    private void showAmenities() {
        int[] ids = {R.id.amenity_1, R.id.amenity_2, R.id.amenity_3,
                R.id.amenity_4, R.id.amenity_5};
        String[] amenities = venue.getAmenities();
        for (int i = 0; i < ids.length; i++) {
            TextView item = findViewById(ids[i]);
            if (i < amenities.length) {
                item.setText(amenities[i]);
                item.setVisibility(View.VISIBLE);
            } else {
                item.setVisibility(View.GONE);
            }
        }
    }

    private void showCourts() {
        LinearLayout container = findViewById(R.id.sub_courts_container);
        container.removeAllViews();
        for (Court court : courts) {
            View row = getLayoutInflater().inflate(R.layout.item_sub_court, container, false);
            MaterialCardView card = row.findViewById(R.id.card_sub_court);
            ImageView radio = row.findViewById(R.id.iv_court_radio);
            ((TextView) row.findViewById(R.id.tv_court_name)).setText(court.getName());
            ((TextView) row.findViewById(R.id.tv_court_desc)).setText(court.getDescription());
            ((TextView) row.findViewById(R.id.tv_court_price))
                    .setText(BookingTime.displayPrice(court.getPricePerHour()) + "/giờ");
            card.setContentDescription(court.getName() + ", "
                    + BookingTime.displayPrice(court.getPricePerHour()) + " mỗi giờ");
            card.setOnClickListener(v -> {
                selectedCourt = court;
                updatePrice();
                updateCourtSelection();
            });
            cards.add(card);
            radioIcons.add(radio);
            container.addView(row);
        }
        updateCourtSelection();
    }

    private void updatePrice() {
        ((TextView) findViewById(R.id.tv_bottom_price))
                .setText(BookingTime.displayPrice(selectedCourt.getPricePerHour()) + "/giờ");
    }

    private void updateCourtSelection() {
        for (int i = 0; i < courts.size(); i++) {
            boolean selected = courts.get(i).getId().equals(selectedCourt.getId());
            MaterialCardView card = cards.get(i);
            card.setCardBackgroundColor(ContextCompat.getColor(this, selected
                    ? R.color.sportmatch_primary_container : R.color.sportmatch_surface));
            card.setStrokeColor(ContextCompat.getColor(this, selected
                    ? R.color.sportmatch_primary : R.color.sportmatch_stroke));
            card.setStrokeWidth(selected ? 4 : 2);
            radioIcons.get(i).setImageResource(selected
                    ? R.drawable.ic_radio_checked : R.drawable.ic_radio_unchecked);
        }
    }
}
