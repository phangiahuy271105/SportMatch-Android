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
import com.sportmatch.app.data.SampleData;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Màn hình 2: Chi tiết sân thi đấu
 * Trình độ sinh viên năm 3:
 * - Tự động nhận diện môn thể thao (Bóng đá mini hoặc Cầu lông).
 * - Cầu lông: Hiển thị đúng loại sân (Thảm PVC tiêu chuẩn, Sàn gỗ chuyên dụng, đèn chống chói, luật giày đế kếp), KHÔNG hiển thị cỏ nhân tạo.
 * - Bóng đá: Hiển thị sân cỏ nhân tạo 5 người / 7 người.
 * - Bấm chọn sân con cập nhật giá dính dưới đáy và truyền sang Màn hình 3.
 */
public class VenueDetailActivity extends AppCompatActivity {

    private String venueName = "Sân bóng Bình Thạnh";
    private String venueAddress = "Bình Thạnh, TP.HCM";
    private String sportId = SampleData.SPORT_FOOTBALL;
    private int basePrice = 220000;

    // Sân con đang được chọn
    private String selectedCourtName = "";
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

        // 2. Nhận dữ liệu truyền từ MainActivity
        if (getIntent().hasExtra("VENUE_NAME")) {
            venueName = getIntent().getStringExtra("VENUE_NAME");
        }
        if (getIntent().hasExtra("VENUE_ADDRESS")) {
            venueAddress = getIntent().getStringExtra("VENUE_ADDRESS");
        }
        if (getIntent().hasExtra("SPORT_ID")) {
            sportId = getIntent().getStringExtra("SPORT_ID");
        }
        if (getIntent().hasExtra("PRICE")) {
            basePrice = getIntent().getIntExtra("PRICE", 220000);
            selectedCourtPrice = basePrice;
        }

        // Xác định đây là sân Cầu lông hay Sân bóng đá
        boolean isBadminton = SampleData.SPORT_BADMINTON.equals(sportId)
                || (venueName != null && venueName.toLowerCase().contains("cầu lông"));

        // 3. Hiển thị thông tin tổng quan của sân
        TextView tvName = findViewById(R.id.venue_detail_name);
        if (tvName != null) tvName.setText(venueName);

        TextView tvAddress = findViewById(R.id.venue_detail_address);
        if (tvAddress != null) tvAddress.setText(venueAddress);

        tvBottomPrice = findViewById(R.id.tv_bottom_price);
        updateBottomPrice();

        // Cập nhật banner, môn thể thao, mô tả và nội quy theo môn thực tế
        updateSportSpecificContent(isBadminton);

        // 4. Cài đặt danh sách sân con đúng logic theo từng môn thể thao
        setupSubCourts(isBadminton);

        // 5. Nút "Chọn ngày và giờ" -> Chuyển sang Màn hình 3
        MaterialButton btnSelectSlot = findViewById(R.id.btn_select_time_slot);
        if (btnSelectSlot != null) {
            btnSelectSlot.setOnClickListener(v -> {
                Intent intent = new Intent(VenueDetailActivity.this, SelectTimeSlotActivity.class);
                intent.putExtra("VENUE_NAME", venueName);
                intent.putExtra("COURT_NAME", selectedCourtName);
                intent.putExtra("PRICE", selectedCourtPrice);
                intent.putExtra("SPORT_ID", isBadminton ? SampleData.SPORT_BADMINTON : SampleData.SPORT_FOOTBALL);
                startActivity(intent);
            });
        }
    }

    private void updateSportSpecificContent(boolean isBadminton) {
        ImageView ivBanner = findViewById(R.id.iv_venue_banner_img);
        TextView tvSport = findViewById(R.id.venue_detail_sport);
        TextView tvDescription = findViewById(R.id.venue_detail_description);
        TextView tvRules = findViewById(R.id.venue_detail_rules);

        if (isBadminton) {
            // Giao diện & Banner cho sân Cầu lông
            if (ivBanner != null) {
                ivBanner.setImageResource(R.drawable.ic_badminton_banner);
            }
            if (tvSport != null) {
                tvSport.setText("Cầu lông");
            }
            if (tvDescription != null) {
                tvDescription.setText("Nhà thi đấu cầu lông trong nhà có mái che, thảm PVC và sàn gỗ chống trơn trượt, trần cao thoáng khí, đèn LED chống chói mắt chuyên dụng.");
            }
            if (tvRules != null) {
                tvRules.setText("• Bắt buộc mang giày chuyên dụng đế kếp cao su (non-marking), không để lại vệt đen trên sàn thảm.\n• Giữ gìn vệ sinh chung, không hút thuốc và không mang đồ ăn vào khu vực thảm thi đấu.\n• Vui lòng đến trước 10 phút để nhận sân và chuẩn bị thi đấu.");
            }
        } else {
            // Giao diện & Banner cho sân Bóng đá mini
            if (ivBanner != null) {
                ivBanner.setImageResource(R.drawable.ic_stadium_banner);
            }
            if (tvSport != null) {
                tvSport.setText("Bóng đá mini");
            }
            if (tvDescription != null) {
                tvDescription.setText("Cụm sân bóng đá mini tiêu chuẩn, mặt cỏ nhân tạo êm chân, hệ thống chiếu sáng ban đêm hiện đại, thuận tiện cho sinh viên và người đi làm.");
            }
            if (tvRules != null) {
                tvRules.setText("• Mang trang phục thể thao và giày đinh dăm TF hoặc giày bata phù hợp mặt cỏ nhân tạo.\n• Nghiêm cấm sử dụng giày đinh sắt (FG/SG) tránh gây hỏng mặt cỏ và chấn thương.\n• Giữ gìn vệ sinh chung, không hút thuốc hoặc mang đồ uống có cồn vào sân thi đấu.");
            }
        }
    }

    private void updateBottomPrice() {
        if (tvBottomPrice != null) {
            String formatted = NumberFormat.getNumberInstance(Locale.forLanguageTag("vi-VN")).format(selectedCourtPrice) + "đ/giờ";
            tvBottomPrice.setText(formatted);
        }
    }

    private void setupSubCourts(boolean isBadminton) {
        LinearLayout container = findViewById(R.id.sub_courts_container);
        if (container == null) return;
        container.removeAllViews();

        String courtName1;
        String courtDesc1;
        int courtPrice1 = basePrice;

        String courtName2;
        String courtDesc2;
        int courtPrice2;

        if (isBadminton) {
            // Cầu lông: Thảm PVC và Sàn gỗ (CHUẨN LOGIC CẦU LÔNG)
            courtName1 = "Sân số 1 (Thảm PVC tiêu chuẩn)";
            courtDesc1 = "Thảm PVC 4.5mm chống trượt • Đèn chống chói";

            courtName2 = "Sân số 2 (Sàn gỗ chuyên dụng)";
            courtDesc2 = "Sàn gỗ cao cấp giảm chấn • Đạt chuẩn BWF";
            courtPrice2 = basePrice + 20000; // Sàn gỗ đắt hơn 20k
        } else {
            // Bóng đá: Cỏ nhân tạo 5 người và 7 người (CHUẨN LOGIC BÓNG ĐÁ)
            courtName1 = "Sân số 1 (Cỏ nhân tạo 5 người)";
            courtDesc1 = "Cỏ nhân tạo 50mm êm chân • Tiêu chuẩn thi đấu";

            courtName2 = "Sân số 2 (Cỏ nhân tạo 7 người)";
            courtDesc2 = "Cỏ nhân tạo 7 người • Đèn cao áp ban đêm";
            courtPrice2 = basePrice + 50000; // Sân 7 đắt hơn 50k
        }

        selectedCourtName = courtName1;
        selectedCourtPrice = courtPrice1;
        updateBottomPrice();

        // Tạo thẻ Sân con 1
        View view1 = getLayoutInflater().inflate(R.layout.item_sub_court, container, false);
        MaterialCardView card1 = view1.findViewById(R.id.card_sub_court);
        ImageView radio1 = view1.findViewById(R.id.iv_court_radio);
        TextView name1 = view1.findViewById(R.id.tv_court_name);
        TextView desc1 = view1.findViewById(R.id.tv_court_desc);
        TextView price1 = view1.findViewById(R.id.tv_court_price);

        if (name1 != null) name1.setText(courtName1);
        if (desc1 != null) desc1.setText(courtDesc1);
        if (price1 != null) price1.setText(NumberFormat.getNumberInstance(Locale.forLanguageTag("vi-VN")).format(courtPrice1) + "đ/giờ");

        // Tạo thẻ Sân con 2
        View view2 = getLayoutInflater().inflate(R.layout.item_sub_court, container, false);
        MaterialCardView card2 = view2.findViewById(R.id.card_sub_court);
        ImageView radio2 = view2.findViewById(R.id.iv_court_radio);
        TextView name2 = view2.findViewById(R.id.tv_court_name);
        TextView desc2 = view2.findViewById(R.id.tv_court_desc);
        TextView price2 = view2.findViewById(R.id.tv_court_price);

        if (name2 != null) name2.setText(courtName2);
        if (desc2 != null) desc2.setText(courtDesc2);
        if (price2 != null) price2.setText(NumberFormat.getNumberInstance(Locale.forLanguageTag("vi-VN")).format(courtPrice2) + "đ/giờ");

        // Mặc định chọn Sân 1
        setCourtSelectionStyle(card1, radio1, true);
        setCourtSelectionStyle(card2, radio2, false);

        // Bấm chọn Sân 1
        card1.setOnClickListener(v -> {
            setCourtSelectionStyle(card1, radio1, true);
            setCourtSelectionStyle(card2, radio2, false);
            selectedCourtName = courtName1;
            selectedCourtPrice = courtPrice1;
            updateBottomPrice();
        });

        // Bấm chọn Sân 2
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
            card.setStrokeWidth(4);
            card.setCardBackgroundColor(ContextCompat.getColor(this, R.color.sportmatch_primary_container));
            radio.setImageResource(R.drawable.ic_radio_checked);
        } else {
            card.setStrokeColor(ContextCompat.getColor(this, R.color.sportmatch_stroke));
            card.setStrokeWidth(2);
            card.setCardBackgroundColor(ContextCompat.getColor(this, R.color.sportmatch_surface));
            radio.setImageResource(R.drawable.ic_radio_unchecked);
        }
    }
}
