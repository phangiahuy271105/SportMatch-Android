# Bàn giao phần sân và đặt sân mẫu

Giao diện 6 màn trong `app/src/main/res/layout/` vẫn dùng XML Views của Antigravity. Java đã nối các màn và lưu lịch mẫu trên thiết bị. Bốn địa điểm, tám sân con đều là dữ liệu hư cấu tại TP.HCM.

## Các file chính

| File | Việc của file |
| --- | --- |
| `model/Venue.java`, `model/Court.java` | Thông tin địa điểm và sân con. Mỗi địa điểm có mô tả, giờ mở cửa, tiện ích, lưu ý riêng. |
| `data/SampleData.java` | Một nguồn dữ liệu mẫu dùng chung cho danh sách và chi tiết sân. |
| `model/Booking.java` | Một lượt đặt: người đặt, sân con, ngày `yyyy-MM-dd`, giờ bắt đầu, giá, liên hệ. |
| `data/BookingStore.java` | Lưu/đọc booking bằng `SharedPreferences` có sẵn của Android; kiểm tra trùng theo sân con + ngày + giờ. |
| `data/BookingTime.java` | Hiển thị ngày/giờ/giá và kiểm tra giờ đã qua theo TP.HCM. |
| `data/CurrentUser.java` | Tạm trả về `demo_user_1`. Đây là chỗ cần nối với đăng nhập của thành viên 2. |
| `MainActivity.java` | Lọc sân và mở chi tiết bằng `VENUE_ID`. |
| `VenueDetailActivity.java` | Đọc đúng địa điểm, cho chọn sân con, truyền `VENUE_ID` và `COURT_ID`. |
| `SelectTimeSlotActivity.java` | Chọn ngày và một giờ; hiện giờ đã qua hoặc đã có lịch. |
| `BookingConfirmActivity.java` | Kiểm tra tên, điện thoại, trùng giờ rồi lưu booking. |
| `MyBookingsActivity.java` | Đọc lịch của người dùng demo; tự chia “Sắp tới” và “Đã qua”. |
| `BookingDetailActivity.java` | Đọc lại booking đã lưu bằng `BOOKING_ID`; không dùng giá/ngày giờ giả từ màn trước. |

## Quy tắc hiện có

- Một lượt đặt gồm đúng một sân con, một ngày và một khung 1 giờ từ 16:00 đến 22:00.
- Không chọn giờ đã bắt đầu. Nếu cùng sân con + ngày + giờ đã có lịch còn hiệu lực, ô giờ hiện “Hết chỗ” và không lưu thêm lịch trùng.
- Giá của lượt đặt lấy từ sân con đã chọn và được lưu cùng booking, nên danh sách và chi tiết luôn hiện cùng giá.
- Tên và điện thoại phải nhập thật vào ô trống khi thử; nên dùng thông tin giả để demo.
- Lịch chỉ lưu trên máy này. Chưa có tài khoản thật, máy chủ, thông báo cho chủ sân, thanh toán hoặc hủy lịch. Việc chống trùng giữa **nhiều điện thoại** phải làm ở nguồn dữ liệu chung sau này.

## Cách thử trên emulator

1. Mở app, chọn một sân; đọc mô tả, giờ mở cửa, tiện ích và lưu ý riêng.
2. Chọn sân con số 2, kiểm tra giá ở thanh dưới đổi theo sân đó.
3. Chọn ngày mai và một giờ còn trống. Màn xác nhận phải hiện đúng sân con, ngày, giờ, giá.
4. Bấm lưu khi tên/điện thoại còn trống: app báo lỗi tại ô nhập.
5. Nhập tên và số điện thoại mẫu hợp lệ, lưu lịch, xem chi tiết.
6. Đóng rồi mở lại app; vào Cá nhân → Lịch đặt của tôi. Lịch vừa lưu vẫn còn.
7. Mở lại cùng sân con, ngày và giờ: ô đó hiện “Hết chỗ”. Cùng giờ ở sân con khác vẫn có thể chọn.

Ảnh trong `docs/screenshots/` là ảnh chụp giai đoạn thiết kế UI; ngày và lịch giả trong các ảnh đó không dùng để kiểm tra logic hiện tại.
