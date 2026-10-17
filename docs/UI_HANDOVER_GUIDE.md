# TÀI LIỆU BÀN GIAO GIAO DIỆN (UI/UX XML) PHẦN SÂN & ĐẶT SÂN
**Dự án:** SportMatch Android  
**Nhánh:** `feature/ui-venues-and-details`  
**Người làm UI:** Thành viên 1  
**Bàn giao cho:** Thành viên code Logic Java  

---

## 1. DANH SÁCH 6 MÀN HÌNH ĐÃ HOÀN THÀNH 100%
1. **Màn 1: Danh sách sân** (`activity_main.xml`, `item_venue.xml`)
2. **Màn 2: Chi tiết sân** (`activity_venue_detail.xml`, `item_sub_court.xml`)
3. **Màn 3: Chọn ngày & khung giờ** (`activity_select_time_slot.xml`)
4. **Màn 4: Xác nhận đặt sân** (`activity_booking_confirm.xml`)
5. **Màn 5: Lịch đặt của tôi** (`activity_my_bookings.xml`, `item_booking.xml`)
6. **Màn 6: Chi tiết lịch đặt** (`activity_booking_detail.xml`)

---

## 2. BẢNG TRA CỨU ID CHO LẬP TRÌNH VIÊN JAVA

### Màn 1: Danh sách sân (`activity_main.xml` & `item_venue.xml`)
- `spinnerDistrict`: Spinner chọn khu vực.
- `chipAll`, `chipFootball`, `chipBadminton`: Chip chọn môn thể thao.
- `tvVenueCount`: TextView hiện số lượng sân phù hợp.
- `recyclerViewVenues`: RecyclerView danh sách sân.
- `layoutEmpty`: Vùng hiển thị khi không có sân phù hợp.
- `btnItemDetail` trong `item_venue.xml`: Nút "Xem chi tiết →" để mở Màn 2.

### Màn 2: Chi tiết sân (`activity_venue_detail.xml`)
- `btn_back`: Nút quay lại.
- `tv_detail_venue_name`: Tên sân.
- `tv_detail_venue_address`: Địa chỉ sân.
- `tv_detail_venue_desc`: Mô tả sân.
- `layout_court_list`: LinearLayout chứa danh sách các `item_sub_court.xml`.
- `tv_detail_bottom_price`: Giá từ hiển thị ở thanh dưới.
- `btn_select_slot`: Nút "Chọn ngày và giờ" để mở Màn 3.

### Màn 3: Chọn ngày & khung giờ (`activity_select_time_slot.xml`)
- `btn_back`: Nút quay lại.
- `chip_date_today`, `chip_date_tomorrow`, `chip_date_custom`: Chip chọn ngày.
- `tv_slot_summary`: Dòng thông báo giờ đã chọn.
- `btn_confirm_slot`: Nút "Tiếp tục" để mở Màn 4.

### Màn 4: Xác nhận đặt sân (`activity_booking_confirm.xml`)
- `btn_back`: Nút quay lại.
- `tv_confirm_venue_name`: Tên sân đặt.
- `tv_confirm_court_name`: Tên sân con.
- `tv_confirm_date`: Ngày chơi.
- `tv_confirm_time`: Giờ chơi.
- `tv_confirm_court_fee`: Tiền thuê sân.
- `tv_confirm_total_fee`: Tổng tiền thanh toán.
- `edt_customer_name`: Ô nhập họ và tên.
- `edt_customer_phone`: Ô nhập số điện thoại.
- `btn_confirm_booking`: Nút "Xác nhận đặt sân" để lưu lịch và mở Màn 6.

### Màn 5: Lịch đặt của tôi (`activity_my_bookings.xml` & `item_booking.xml`)
- `btn_back`: Nút quay lại.
- `tab_upcoming`: Tab "Sắp tới" (Lọc các lượt đặt sắp tới).
- `tab_past`: Tab "Đã qua" (Lọc các lượt đặt đã hoàn thành).
- `rv_bookings`: RecyclerView hiển thị danh sách thẻ đặt sân (`item_booking.xml`).
- `layout_empty_bookings`: Giao diện hiện ra khi không có lịch đặt nào.
- `btn_explore_venues`: Nút bấm "Khám phá sân ngay" trên màn hình trống.
- `card_booking_item`: Thẻ lịch đặt trong Adapter, bấm để mở Màn 6.

### Màn 6: Chi tiết lịch đặt (`activity_booking_detail.xml`)
- `btn_back`: Nút quay lại.
- `tv_booking_detail_code`: Mã đặt sân check-in nổi bật (vd: `SM-2026-8899`).
- `tv_booking_detail_status`: Huy hiệu trạng thái (vd: `Đã xác nhận`).
- `tv_detail_venue_name`: Tên sân.
- `tv_detail_sport_badge`: Tag môn (Bóng đá mini / Cầu lông).
- `tv_detail_court_name`: Tên sân con.
- `tv_detail_venue_address`: Địa chỉ sân.
- `tv_detail_booking_date`: Ngày chơi.
- `tv_detail_booking_time`: Giờ chơi.
- `tv_detail_customer_name`: Tên người đặt.
- `tv_detail_customer_phone`: Số điện thoại người đặt.
- `tv_detail_total_amount`: Tổng tiền thanh toán.
- `btn_contact_venue`: Nút gọi điện cho quản lý sân.
- `btn_back_to_home`: Nút quay về danh sách sân.
