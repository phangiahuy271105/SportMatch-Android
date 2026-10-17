package com.sportmatch.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Màn hình 2: Chi tiết sân thi đấu
 * Trình độ sinh viên năm 3: Chọn sân con, cập nhật giá và chuyển sang Màn hình 3.
 */
public class VenueDetailActivity extends AppCompatActivity {

    private String venueName = "Sân bóng Bình Thạnh";
    private String venueAddress = "Bình Thạnh, TP.HCM";
    private int basePrice = 220000;

    // Sân con đang được chọn
    private String selectedCourtName = "Sân số 1 (Sân cỏ nhân tạo 5 người)";
    private int selectedCourtPrice = 220000;

    private TextView tvBottomPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venue_detail);

        // 1. Nút quay lại
        ImageButton btnBack = findViewById(R.id.btn_back);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        // 2. Nhận dữ liệu truyền từ MainActivity nếu có
        if (getIntent().hasExtra("VENUE_NAME")) {
            venueName = getIntent().getStringExtra("VENUE_NAME");
        }
        if (getIntent().hasExtra("VENUE_ADDRESS")) {
            venueAddress = getIntent().getStringExtra("VENUE_ADDRESS");
        }
        if (getIntent().hasExtra("PRICE")) {
            basePrice = getIntent().getIntExtra("PRICE", 220000);
            selectedCourtPrice = basePrice;
        }

        // 3. Hiển thị thông tin lên giao diện
        TextView tvName = findViewById(R.id.venue_detail_name);
        if (tvName != null) tvName.setText(venueName);

        TextView tvAddress = findViewById(R.id.venue_detail_address);
        if (tvAddress != null) tvAddress.setText(venueAddress);

        tvBottomPrice = findViewById(R.id.tv_bottom_price);
        updateBottomPrice();

        // 4. Cài đặt danh sách sân con có thể bấm chọn
        setupSubCourts();

        // 5. Nút "Chọn ngày và giờ" -> Chuyển sang Màn hình 3
        MaterialButton btnSelectSlot = findViewById(R.id.btn_select_time_slot);
        if (btnSelectSlot != null) {
            btnSelectSlot.setOnClickListener(v -> {
                Intent intent = new Intent(VenueDetailActivity.this, SelectTimeSlotActivity.class);
                intent.putExtra("VENUE_NAME", venueName);
                intent.putExtra("COURT_NAME", selectedCourtName);
                intent.putExtra("PRICE", selectedCourtPrice);
                startActivity(intent);
            });
        }
    }

    private void updateBottomPrice() {
        if (tvBottomPrice != null) {
            String formatted = NumberFormat.getNumberInstance(Locale.forLanguageTag("vi-VN")).format(selectedCourtPrice) + "đ/giờ";
            tvBottomPrice.setText(formatted);
        }
    }

    private void setupSubCourts() {
        LinearLayout container = findViewById(R.id.sub_courts_container);
        if (container == null) return;
        container.removeAllViews();

        // Tạo Sân số 1 (5 người)
        View view1 = getLayoutInflater().inflate(R.layout.item_sub_court, container, false);
        MaterialCardView card1 = view1.findViewById(R.id.card_sub_court);
        ImageView radio1 = view1.findViewById(R.id.iv_court_radio);
        TextView name1 = view1.findViewById(R.id.tv_court_name);
        TextView desc1 = view1.findViewById(R.id.tv_court_desc);
        TextView price1 = view1.findViewById(R.id.tv_court_price);

        String courtName1 = "Sân số 1 (Sân cỏ nhân tạo 5 người)";
        int courtPrice1 = basePrice;
        if (name1 != null) name1.setText(courtName1);
        if (desc1 != null) desc1.setText("Cỏ nhân tạo 5 người • Tiêu chuẩn thi đấu");
        if (price1 != null) price1.setText(NumberFormat.getNumberInstance(Locale.forLanguageTag("vi-VN")).format(courtPrice1) + "đ/giờ");

        // Tạo Sân số 2 (7 người)
        View view2 = getLayoutInflater().inflate(R.layout.item_sub_court, container, false);
        MaterialCardView card2 = view2.findViewById(R.id.card_sub_court);
        ImageView radio2 = view2.findViewById(R.id.iv_court_radio);
        TextView name2 = view2.findViewById(R.id.tv_court_name);
        TextView desc2 = view2.findViewById(R.id.tv_court_desc);
        TextView price2 = view2.findViewById(R.id.tv_court_price);

        String courtName2 = "Sân số 2 (Sân cỏ nhân tạo 7 người)";
        int courtPrice2 = basePrice + 50000;
        if (name2 != null) name2.setText(courtName2);
        if (desc2 != null) desc2.setText("Cỏ nhân tạo 7 người • Đèn chiếu sáng cao áp");
        if (price2 != null) price2.setText(NumberFormat.getNumberInstance(Locale.forLanguageTag("vi-VN")).format(courtPrice2) + "đ/giờ");

        // Mặc định chọn Sân 1
        setCourtSelectionStyle(card1, radio1, true);
        setCourtSelectionStyle(card2, radio2, false);

        // Sự kiện khi bấm chọn Sân 1
        card1.setOnClickListener(v -> {
            setCourtSelectionStyle(card1, radio1, true);
            setCourtSelectionStyle(card2, radio2, false);
            selectedCourtName = courtName1;
            selectedCourtPrice = courtPrice1;
            updateBottomPrice();
        });

        // Sự kiện khi bấm chọn Sân 2
        card2.setOnClickListener(v -> {
            setCourtSelectionStyle(card1, radio1, false);
            setCourtSelectionStyle(card2, radio2, true);
            selectedCourtName = courtName2;
            selectedCourtPrice = courtPrice2;
            updateBottomPrice();
        });

        container.addView(view1);
        container.addView(view2);
    }

    private void setCourtSelectionStyle(MaterialCardView card, ImageView radio, boolean isSelected) {
        if (card == null || radio == null) return;
        if (isSelected) {
            card.setStrokeColor(ContextCompat.getColor(this, R.color.sportmatch_primary));
            card.setStrokeWidth(4); // 2dp viền đậm
            card.setCardBackgroundColor(ContextCompat.getColor(this, R.color.sportmatch_primary_container));
            radio.setImageResource(R.drawable.ic_radio_checked);
        } else {
            card.setStrokeColor(ContextCompat.getColor(this, R.color.sportmatch_stroke));
            card.setStrokeWidth(2); // 1dp viền thường
            card.setCardBackgroundColor(ContextCompat.getColor(this, R.color.sportmatch_surface));
            radio.setImageResource(R.drawable.ic_radio_unchecked);
        }
    }
}
