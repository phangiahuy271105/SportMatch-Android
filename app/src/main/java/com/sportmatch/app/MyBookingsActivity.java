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

import java.util.ArrayList;
import java.util.List;

/**
 * Màn hình 5: Lịch đặt của tôi
 * Trình độ sinh viên năm 3: Hiển thị danh sách vé đặt qua RecyclerView và lọc tab Sắp tới / Đã qua.
 */
public class MyBookingsActivity extends AppCompatActivity {

    private TextView tabUpcoming;
    private TextView tabPast;
    private RecyclerView rvBookings;
    private LinearLayout layoutEmpty;
    private BookingSimpleAdapter adapter;

    private final List<BookingItemModel> upcomingList = new ArrayList<>();
    private final List<BookingItemModel> pastList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bookings);

        // 1. Nút quay lại
        ImageButton btnBack = findViewById(R.id.btn_back);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        tabUpcoming = findViewById(R.id.tab_upcoming);
        tabPast = findViewById(R.id.tab_past);
        rvBookings = findViewById(R.id.rv_bookings);
        layoutEmpty = findViewById(R.id.layout_empty_bookings);

        MaterialButton btnExplore = findViewById(R.id.btn_explore_venues);
        if (btnExplore != null) {
            btnExplore.setOnClickListener(v -> finish());
        }

        // 2. Tạo dữ liệu mẫu lịch đặt
        initSampleData();

        // 3. Cài đặt RecyclerView
        rvBookings.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BookingSimpleAdapter(upcomingList);
        rvBookings.setAdapter(adapter);

        // 4. Bắt sự kiện chọn tab "Sắp tới" / "Đã qua"
        tabUpcoming.setOnClickListener(v -> selectTab(true));
        tabPast.setOnClickListener(v -> selectTab(false));
    }

    private void selectTab(boolean isUpcoming) {
        if (isUpcoming) {
            tabUpcoming.setBackgroundResource(R.drawable.bg_tab_selected);
            tabUpcoming.setTextColor(ContextCompat.getColor(this, R.color.white));

            tabPast.setBackgroundResource(R.drawable.bg_tab_unselected);
            tabPast.setTextColor(ContextCompat.getColor(this, R.color.sportmatch_text_secondary));

            adapter.updateData(upcomingList);
            layoutEmpty.setVisibility(upcomingList.isEmpty() ? View.VISIBLE : View.GONE);
        } else {
            tabPast.setBackgroundResource(R.drawable.bg_tab_selected);
            tabPast.setTextColor(ContextCompat.getColor(this, R.color.white));

            tabUpcoming.setBackgroundResource(R.drawable.bg_tab_unselected);
            tabUpcoming.setTextColor(ContextCompat.getColor(this, R.color.sportmatch_text_secondary));

            adapter.updateData(pastList);
            layoutEmpty.setVisibility(pastList.isEmpty() ? View.VISIBLE : View.GONE);
        }
    }

    private void initSampleData() {
        // Lịch sắp tới
        upcomingList.add(new BookingItemModel(
                "SM-2026-8899",
                "Sân bóng đá Chảo Lửa",
                "Sân số 1 (Bóng đá mini 5 người)",
                "18:00 – 19:00 | Hôm nay, 18/10/2026",
                "30 Phan Thúc Duyện, Tân Bình",
                "180.000 đ",
                "Đã xác nhận",
                true
        ));
        upcomingList.add(new BookingItemModel(
                "SM-2026-7712",
                "Sân cầu lông Tân Bình",
                "Sân số 2 (Thảm tiêu chuẩn)",
                "09:00 – 10:00 | Ngày mai, 19/10/2026",
                "Tân Bình, TP.HCM",
                "90.000 đ",
                "Đã xác nhận",
                true
        ));

        // Lịch đã qua
        pastList.add(new BookingItemModel(
                "SM-2026-5520",
                "Sân bóng Bình Thạnh",
                "Sân số 1 (Cỏ nhân tạo 5 người)",
                "19:00 – 20:00 | 12/10/2026",
                "Bình Thạnh, TP.HCM",
                "220.000 đ",
                "Đã hoàn thành",
                false
        ));
    }

    // Model đơn giản cho thẻ lịch đặt
    static class BookingItemModel {
        String code, venueName, court, dateTime, address, price, status;
        boolean isConfirmed;

        BookingItemModel(String code, String venueName, String court, String dateTime, String address, String price, String status, boolean isConfirmed) {
            this.code = code;
            this.venueName = venueName;
            this.court = court;
            this.dateTime = dateTime;
            this.address = address;
            this.price = price;
            this.status = status;
            this.isConfirmed = isConfirmed;
        }
    }

    // Adapter RecyclerView cơ bản cho sinh viên
    private class BookingSimpleAdapter extends RecyclerView.Adapter<BookingSimpleAdapter.ViewHolder> {
        private final List<BookingItemModel> list = new ArrayList<>();

        BookingSimpleAdapter(List<BookingItemModel> initial) {
            this.list.addAll(initial);
        }

        void updateData(List<BookingItemModel> newList) {
            this.list.clear();
            this.list.addAll(newList);
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_booking, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            BookingItemModel item = list.get(position);
            holder.tvVenueName.setText(item.venueName);
            holder.tvStatus.setText(item.status);
            holder.tvStatus.setBackgroundResource(item.isConfirmed ? R.drawable.bg_status_confirmed : R.drawable.bg_status_completed);
            holder.tvStatus.setTextColor(ContextCompat.getColor(MyBookingsActivity.this, item.isConfirmed ? R.color.sportmatch_accent_dark : R.color.sportmatch_text_secondary));

            holder.tvCourt.setText(item.court);
            holder.tvDateTime.setText(item.dateTime);
            holder.tvAddress.setText(item.address);
            holder.tvCode.setText("Mã: " + item.code);
            holder.tvPrice.setText(item.price);

            // Bấm vào thẻ để mở Màn hình 6 (Chi tiết lịch đặt)
            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(MyBookingsActivity.this, BookingDetailActivity.class);
                intent.putExtra("BOOKING_CODE", item.code);
                intent.putExtra("VENUE_NAME", item.venueName);
                intent.putExtra("COURT_NAME", item.court);
                intent.putExtra("SELECTED_DATE", item.dateTime);
                intent.putExtra("PRICE", 180000);
                startActivity(intent);
            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvVenueName, tvStatus, tvCourt, tvDateTime, tvAddress, tvCode, tvPrice;

            ViewHolder(@NonNull View v) {
                super(v);
                tvVenueName = v.findViewById(R.id.tv_booking_item_venue_name);
                tvStatus = v.findViewById(R.id.tv_booking_item_status);
                tvCourt = v.findViewById(R.id.tv_booking_item_court);
                tvDateTime = v.findViewById(R.id.tv_booking_item_datetime);
                tvAddress = v.findViewById(R.id.tv_booking_item_address);
                tvCode = v.findViewById(R.id.tv_booking_item_code);
                tvPrice = v.findViewById(R.id.tv_booking_item_price);
            }
        }
    }
}
