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
import android.widget.Toast;

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
import com.sportmatch.app.model.User;
import com.sportmatch.app.model.Venue;
import com.sportmatch.app.utils.SharedPrefManager;

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
    private MaterialButton filterToggleButton;
    private LinearLayout filterDetails;
    private TextView filterSummary;
    private TextView clearDateButton;
    private BookingStore bookingStore;
    private String selectedSportId;
    private String selectedDate;

    // View cho Tab Cá nhân
    private TextView tvProfileName;
    private TextView tvProfileEmail;
    private View btnProfileLogin;
    private View btnProfileDetail;
    private View btnProfileLogout;

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
        setupProfileTab();

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
        // Cập nhật lại giao diện Tab Cá nhân mỗi khi quay lại màn hình chính.
        updateProfileTabState();
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
            findViewById(R.id.profile_panel).setVisibility(
                    selectedId == R.id.nav_profile ? View.VISIBLE : View.GONE);

            // Khi bấm vào Tab Tìm người (Matches)
            if (selectedId == R.id.nav_matches) {
                findViewById(R.id.matches_panel).setVisibility(View.VISIBLE);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.matches_panel, new TimNguoiFragment())
                        .commit();
            } else {
                findViewById(R.id.matches_panel).setVisibility(View.GONE);
            }

            if (selectedId == R.id.nav_profile) {
                updateProfileTabState();
            }
            return true;
        });
    }

    /**
     * Khởi tạo các sự kiện cho Tab Cá nhân (Profile Panel)
     */
    private void setupProfileTab() {
        tvProfileName = findViewById(R.id.tv_profile_name);
        tvProfileEmail = findViewById(R.id.tv_profile_email);
        btnProfileLogin = findViewById(R.id.btn_profile_login);
        btnProfileDetail = findViewById(R.id.btn_profile_detail);
        btnProfileLogout = findViewById(R.id.btn_profile_logout);

        // Nút Đăng nhập / Đăng ký
        if (btnProfileLogin != null) {
            btnProfileLogin.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            });
        }

        // Nút Xem & Chỉnh sửa hồ sơ
        if (btnProfileDetail != null) {
            btnProfileDetail.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
            });
        }

        // Nút Đăng xuất
        if (btnProfileLogout != null) {
            btnProfileLogout.setOnClickListener(v -> {
                SharedPrefManager.getInstance(MainActivity.this).logout();
                Toast.makeText(MainActivity.this, "Đã đăng xuất thành công!", Toast.LENGTH_SHORT).show();
                updateProfileTabState();
            });
        }

        updateProfileTabState();
    }

    /**
     * Cập nhật trạng thái hiển thị của Tab Cá nhân theo phiên đăng nhập
     */
    private void updateProfileTabState() {
        if (tvProfileName == null || tvProfileEmail == null) return;

        SharedPrefManager pref = SharedPrefManager.getInstance(this);
        if (pref.isLoggedIn()) {
            User user = pref.getCurrentUser();
            if (user != null) {
                tvProfileName.setText(user.getFullName());
                tvProfileEmail.setText(user.getEmail());
            } else {
                tvProfileName.setText("Người dùng SportMatch");
                tvProfileEmail.setText(pref.getCurrentUserEmail());
            }

            // Đã đăng nhập: Hiện nút xem hồ sơ & đăng xuất, ẩn nút đăng nhập
            if (btnProfileLogin != null) btnProfileLogin.setVisibility(View.GONE);
            if (btnProfileDetail != null) btnProfileDetail.setVisibility(View.VISIBLE);
            if (btnProfileLogout != null) btnProfileLogout.setVisibility(View.VISIBLE);
        } else {
            // Chưa đăng nhập: Hiện trạng thái Khách
            tvProfileName.setText("Khách");
            tvProfileEmail.setText("Chưa đăng nhập");

            // Chưa đăng nhập: Hiện nút đăng nhập, ẩn nút hồ sơ & đăng xuất
            if (btnProfileLogin != null) btnProfileLogin.setVisibility(View.VISIBLE);
            if (btnProfileDetail != null) btnProfileDetail.setVisibility(View.GONE);
            if (btnProfileLogout != null) btnProfileLogout.setVisibility(View.GONE);
        }
    }

    private void setupFilters() {
        filterToggleButton = findViewById(R.id.btn_toggle_filters);
        filterDetails = findViewById(R.id.filter_details);
        filterSummary = findViewById(R.id.tv_filter_summary);

        filterToggleButton.setOnClickListener(v -> {
            boolean filtersAreVisible = filterDetails.getVisibility() == View.VISIBLE;
            filterDetails.setVisibility(filtersAreVisible ? View.GONE : View.VISIBLE);
            filterToggleButton.setText(filtersAreVisible
                    ? R.string.filter_toggle_show : R.string.filter_toggle_hide);
        });

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
            updateFilterSummary();
            renderVenues();
        });

        districtFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateFilterSummary();
                renderVenues();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                updateFilterSummary();
                renderVenues();
            }
        });

        updateFilterSummary();
    }

    private void updateFilterSummary() {
        String sportName = getString(R.string.filter_all_sports);
        if (SampleData.SPORT_FOOTBALL.equals(selectedSportId)) {
            sportName = getString(R.string.sport_football);
        } else if (SampleData.SPORT_BADMINTON.equals(selectedSportId)) {
            sportName = getString(R.string.sport_badminton);
        }

        Object selectedDistrict = districtFilter.getSelectedItem();
        String districtName = selectedDistrict == null
                ? getString(R.string.filter_all_districts_summary)
                : selectedDistrict.toString();
        String dateName = selectedDate == null
                ? getString(R.string.filter_any_day_summary)
                : BookingTime.displayDate(selectedDate);

        filterSummary.setText(getString(R.string.filter_summary,
                sportName, districtName, dateName));
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
            updateFilterSummary();
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
        updateFilterSummary();
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
