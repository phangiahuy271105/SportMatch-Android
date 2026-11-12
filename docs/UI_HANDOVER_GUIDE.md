# Bàn giao phần sân và đặt sân mẫu

Giao diện 6 màn vẫn dùng Java + XML Views theo bố cục Antigravity. Màn chọn giờ dùng một ô XML lặp lại để hiện đủ giờ mở cửa của từng sân. Java lưu lịch mẫu trên thiết bị. Bốn địa điểm, tám sân con và bốn ảnh minh họa đều là dữ liệu hư cấu tại TP.HCM.

## Các file chính

| File | Việc của file |
| --- | --- |
| `model/Venue.java`, `model/Court.java` | Thông tin địa điểm và sân con. Mỗi địa điểm có mô tả, giờ mở/đóng cửa, ảnh, tiện ích, lưu ý riêng. |
| `data/SampleData.java` | Một nguồn dữ liệu mẫu dùng chung cho danh sách và chi tiết sân. |
| `res/drawable-nodpi/venue_*.png` | Bốn ảnh sân minh họa tạo mới, lưu trong app; không cần tải ảnh qua mạng. |
| `res/layout/item_time_slot.xml` | Mẫu của một ô giờ; Java lặp lại ô này theo giờ mở/đóng của địa điểm. |
| `model/Booking.java` | Một lượt đặt: người đặt, sân con, ngày `yyyy-MM-dd`, giờ bắt đầu, giá, liên hệ. |
| `data/BookingStore.java` | Lưu/đọc booking bằng `SharedPreferences` có sẵn của Android; kiểm tra trùng theo sân con + ngày + giờ. |
| `data/BookingTime.java` | Hiển thị ngày/giờ/giá và kiểm tra giờ đã qua theo TP.HCM. |
| `data/CurrentUser.java` | Tạm trả về `demo_user_1`. Đây là chỗ cần nối với đăng nhập của thành viên 2. |
| `MainActivity.java` | Lọc theo tên sân, môn, khu vực và ngày; mở chi tiết bằng `VENUE_ID` và ngày đã chọn. |
| `VenueDetailActivity.java` | Đọc đúng địa điểm, cho chọn sân con, truyền `VENUE_ID`, `COURT_ID` và ngày đã chọn. |
| `SelectTimeSlotActivity.java` | Chọn ngày và một giờ; hiện giờ đã qua hoặc đã có lịch. |
| `BookingConfirmActivity.java` | Kiểm tra tên, điện thoại, trùng giờ rồi lưu booking. |
| `MyBookingsActivity.java` | Đọc lịch của người dùng demo; tự chia “Sắp tới” và “Đã qua”. |
| `BookingDetailActivity.java` | Đọc lại booking đã lưu bằng `BOOKING_ID`; không dùng giá/ngày giờ giả từ màn trước. |

## Quy tắc hiện có

- Một lượt đặt gồm đúng một sân con, một ngày và một khung 1 giờ. Các khung bắt đầu từ giờ mở cửa đến trước giờ đóng cửa của địa điểm: Bình Thạnh 06–23h, Linh Trung 15–22h, Tân Bình 06–22h, Quận 7 07–23h.
- Chưa chọn ngày thì danh sách hiện mọi sân phù hợp với tên, môn và khu vực. Khi chọn ngày, chỉ hiện địa điểm còn ít nhất một khung giờ chưa bắt đầu và chưa có người đặt ở một sân con. Ngày chọn ở danh sách được chuyển tới màn chọn giờ; có thể xóa lọc để xem lại mọi sân.
- Không chọn giờ đã bắt đầu. Nếu cùng sân con + ngày + giờ đã có lịch còn hiệu lực, ô giờ hiện “Hết chỗ” và không lưu thêm lịch trùng.
- Giá của lượt đặt lấy từ sân con đã chọn và được lưu cùng booking, nên danh sách và chi tiết luôn hiện cùng giá.
- Tên và điện thoại phải nhập thật vào ô trống khi thử; nên dùng thông tin giả để demo.
- Lịch chỉ lưu trên máy này. Chưa có tài khoản thật, máy chủ, thông báo cho chủ sân, thanh toán hoặc hủy lịch. Việc chống trùng giữa **nhiều điện thoại** phải làm ở nguồn dữ liệu chung sau này.

## Cách thử trên emulator

1. Mở app, thử tìm tên sân, lọc môn/khu vực, chọn ngày mai. Danh sách phải hiện đúng số sân phù hợp. Mở một sân và kiểm tra màn chọn giờ giữ đúng ngày vừa lọc. Bấm **Xóa** để bỏ lọc ngày.
2. Xem ảnh minh họa; chọn một sân và đọc mô tả, giờ mở cửa, tiện ích, lưu ý riêng.
3. Chọn sân con số 2, kiểm tra giá ở thanh dưới đổi theo sân đó.
4. Chọn ngày mai và một giờ còn trống. Ví dụ Bình Thạnh phải có 06:00–07:00 và 22:00–23:00; Linh Trung chỉ có từ 15:00–16:00 đến 21:00–22:00. Màn xác nhận phải hiện đúng sân con, ngày, giờ, giá.
5. Bấm lưu khi tên/điện thoại còn trống: app báo lỗi tại ô nhập.
6. Nhập tên và số điện thoại mẫu hợp lệ, lưu lịch, xem chi tiết.
7. Đóng rồi mở lại app; vào Cá nhân → Lịch đặt của tôi. Lịch vừa lưu vẫn còn.
8. Mở lại cùng sân con, ngày và giờ: ô đó hiện “Hết chỗ”. Cùng giờ ở sân con khác vẫn có thể chọn.

Ảnh trong `docs/screenshots/` là ảnh chụp giai đoạn thiết kế UI; ngày và lịch giả trong các ảnh đó không dùng để kiểm tra logic hiện tại.
