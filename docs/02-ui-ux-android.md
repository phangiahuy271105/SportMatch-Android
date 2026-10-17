# SportMatch Android — bản nháp UI/UX

**Trạng thái:** Bản định hướng UI/UX. Sáu màn sân/đặt sân đã có XML và luồng mẫu; các màn tài khoản và tìm người vẫn chờ nhóm triển khai.

## Mục tiêu trải nghiệm

1. Người dùng tìm sân và nhìn thấy **môn, khu vực, giá, khung giờ** trước khi quyết định đặt.
2. Trước khi xác nhận, người dùng nhìn lại đúng **sân cụ thể + ngày + giờ + tiền**.
3. Người tìm người chơi thấy ngay **môn, địa điểm, thời gian, số người còn thiếu** và trạng thái yêu cầu của mình.
4. Mỗi màn có một hành động chính nổi bật, thông báo lỗi dễ hiểu và cách quay lại rõ ràng.

## Điều hướng chung

- Thanh dưới gồm ba mục: **Sân**, **Tìm người**, **Cá nhân**. Đây là ba khu vực chính; màn chi tiết dùng mũi tên quay lại ở thanh trên.
- Thanh dưới chỉ xuất hiện ở màn gốc của ba khu vực; màn đăng nhập, chi tiết, biểu mẫu và xác nhận tập trung vào tác vụ đang làm.
- Xem danh sách và chi tiết không cần đăng nhập (đề xuất). Khi bấm đặt sân, tạo bài hoặc gửi yêu cầu, mở đăng nhập rồi quay về tác vụ trước đó. Cần cả nhóm chốt quy tắc này.
- Nút quay lại Android phải trở về màn ngay trước đó mà không tạo booking hoặc bài đăng ngoài ý muốn.

## Luồng sân và đặt sân

| Màn | Nội dung cần thấy ngay | Hành động chính |
| --- | --- | --- |
| Danh sách sân | Tiêu đề “Tìm sân”, hai lựa chọn môn, bộ lọc khu vực/ngày, số kết quả; thẻ sân có ảnh, tên, khu vực, môn, giá từ… | Chạm thẻ để xem chi tiết |
| Chi tiết sân | Ảnh, tên, địa chỉ, môn, mô tả ngắn, danh sách sân con/giá; không giấu giá ở cuối trang | **Chọn ngày và giờ** |
| Chọn ngày giờ | Tên địa điểm và sân con đang chọn; ngày; các khung giờ dạng ô; giờ hết chỗ bị vô hiệu hóa kèm nhãn | **Tiếp tục** khi đủ lựa chọn |
| Xác nhận | Tóm tắt sân con, ngày, giờ, giá; ghi rõ bản đầu chưa thanh toán thật | **Xác nhận đặt sân** |
| Lịch đặt | Mục “Sắp tới” và “Đã qua”; thẻ gồm sân, ngày giờ, trạng thái | Chạm thẻ xem chi tiết |
| Chi tiết lịch | Mã đặt, sân, địa chỉ, ngày giờ, giá, trạng thái | Quay lại lịch đặt |

Phác thảo màn danh sách sân:

```text
Tìm sân                         [Lịch đặt]
[Bóng đá mini] [Cầu lông]
[Khu vực ▾]  [Ngày ▾]
4 sân phù hợp
┌───────────────────────────────┐
│ [Ảnh sân]  Sân A              │
│ Bình Thạnh · Bóng đá mini     │
│ Từ 200.000đ/giờ    Xem sân →  │
└───────────────────────────────┘
            ...
       Sân   Tìm người   Cá nhân
```

Phác thảo màn chọn giờ:

```text
← Chọn ngày và giờ
Sân A · Sân số 1
[Ngày: 15/10/2026 ▾]
Khung giờ còn trống
[17:00–18:00] [18:00–19:00]
[19:00–20:00 Hết chỗ]
Đã chọn: 18:00–19:00 · 200.000đ
           [Tiếp tục]
```

Ngày trong phác thảo chỉ để minh họa; dữ liệu mẫu khi chạy phải dùng ngày phù hợp thời điểm kiểm thử. Bản đầu chọn **một sân con, một ngày, một khung giờ** mỗi lượt đặt. Khi xác nhận thành công, mở lịch đặt và hiển thị booking mới ngay.

## Luồng tìm người

| Màn | Nội dung cần thấy ngay | Hành động chính |
| --- | --- | --- |
| Danh sách bài | Lọc theo hai môn; mỗi thẻ có tiêu đề, khu vực, ngày giờ, số người còn thiếu, trạng thái bài | **Tạo bài** |
| Chi tiết bài | Đầy đủ môn, địa điểm, thời gian, số người cần/đã duyệt, mô tả; nếu đã gửi thì hiện trạng thái yêu cầu | **Gửi yêu cầu** hoặc **Rút yêu cầu** |
| Tạo/chỉnh sửa bài | Trường bắt buộc có nhãn rõ; môn, tiêu đề, địa điểm, ngày giờ, số người cần, mô tả | **Đăng bài** hoặc **Lưu thay đổi** |
| Bài của tôi | Bài đang mở/đã đóng, số yêu cầu đang chờ | Chạm bài để quản lý |
| Quản lý yêu cầu | Mỗi yêu cầu có tên người gửi, lời nhắn, trạng thái | **Duyệt** / **Từ chối**; chủ bài có nút **Đóng bài** |

Phác thảo thẻ bài tìm người:

```text
┌───────────────────────────────┐
│ Cầu lông · Đang mở            │
│ Cần 2 bạn đánh đôi tối thứ 6 │
│ Tân Bình · 19:00, 16/10      │
│ Còn thiếu 2 người   Xem bài →│
└───────────────────────────────┘
```

Với chủ bài, màn chi tiết hiển thị **Sửa bài**, **Xem yêu cầu**, **Đóng bài**. Với người khác, màn này hiển thị một nút phù hợp trạng thái: **Gửi yêu cầu**, **Đang chờ duyệt**, **Đã được duyệt**, **Đã từ chối** hoặc **Rút yêu cầu**. Không chỉ dùng màu để truyền trạng thái; luôn có chữ.

## Tài khoản và hồ sơ

- Đăng nhập: email, mật khẩu, **Đăng nhập**, đường dẫn **Đăng ký** và **Quên mật khẩu**.
- Đăng ký: chỉ hỏi thông tin cần cho bản đầu; lỗi thiếu/sai hiện dưới đúng ô nhập.
- Quên mật khẩu: giao diện nhập email; cách khôi phục thật chốt cùng phương án xác thực. Không hiện thông báo “đã gửi email” khi dữ liệu mẫu chưa gửi gì.
- Hồ sơ: tên hiển thị, email, số liên hệ, môn yêu thích; đặt **Chỉnh sửa** và **Đăng xuất** ở vị trí dễ tìm.

## Quy ước giao diện dùng chung — đề xuất

| Thành phần | Quy ước |
| --- | --- |
| Màu chính | Xanh dương `#1565C0`; màu nhấn tích cực xanh lá `#2E7D32` |
| Nền và chữ | Nền sáng `#F7F9FC`; chữ chính `#17212B`; thẻ trắng |
| Khoảng cách | Lề màn 16dp; khoảng cách phần tử 8/16/24dp theo cấp độ |
| Nút chính | Rõ chữ, rộng đủ chạm; chỉ một nút chính trên mỗi bước |
| Nút nguy hiểm | “Đóng bài” có xác nhận; màu và chữ khác hành động tích cực |
| Bộ lọc | Hiện lựa chọn đang áp dụng và có cách xóa/làm mới |
| Danh sách trống | Nêu lý do bằng câu ngắn và gợi ý đổi bộ lọc hoặc tạo bài |
| Lỗi | Nói việc gì sai và cách sửa; không chỉ hiện “Có lỗi xảy ra” |

Vùng bấm của nút, ô chọn giờ và biểu tượng tương tác nên đạt ít nhất **48dp × 48dp** để dễ chạm trên điện thoại, theo [hướng dẫn khả năng tiếp cận cho Android Views](https://developer.android.com/guide/topics/ui/accessibility/views/apps-views). Thanh điều hướng dưới phù hợp với ba khu vực chính; Android cung cấp `BottomNavigationView` cho Views và hướng dẫn dùng với ba đến năm điểm đến cấp cao. [Tham khảo Android Developers](https://developer.android.com/reference/com/google/android/material/bottomnavigation/BottomNavigationView)

Project gốc đã có thư viện Material trong cấu hình do Android Studio tạo. Nếu khi triển khai cần thêm thư viện hoặc đổi cấu hình, người làm phải giải thích và báo nhóm trước.

## Những điểm cần cả ba duyệt trước khi viết XML

1. Khách chưa đăng nhập có được xem sân và bài tìm người không?
2. Giữ ba mục điều hướng **Sân / Tìm người / Cá nhân** chứ?
3. Màu xanh dương + xanh lá trên có phù hợp với SportMatch không? Nếu website cũ có màu nhận diện muốn giữ, chọn mã màu cụ thể.
4. Bản đầu có nút hủy booking không? Nếu có, phải bổ sung quy tắc và màn xác nhận hủy.
5. Chọn danh sách khung giờ mẫu và giá mẫu thống nhất để màn sân, booking và lịch đặt hiển thị cùng một dữ liệu.
