# SportMatch Android — bản nháp thiết kế MVP

**Trạng thái:** Bản thiết kế chung. Phần sân/đặt sân mẫu đã triển khai cục bộ; tài khoản, tìm người và dữ liệu chung vẫn chờ nhóm hoàn thiện.

## 1. Phạm vi và giả định

- Đã chốt cho bản đầu: bóng đá mini và cầu lông; dữ liệu sân mẫu tại TP.HCM; Android native bằng Java và XML Views.
- Chưa có đề bài chi tiết, tiêu chí chấm và hạn nộp của giảng viên. Khi có, nhóm đối chiếu lại phạm vi.
- Phần sân dùng một nguồn dữ liệu mẫu trong app; booking tạm lưu trên thiết bị bằng SharedPreferences. Khi chọn cách lưu/kết nối chung, nhóm sẽ bàn phương án và phân quyền trước khi triển khai.
- Chưa làm thanh toán thật, chat trực tiếp, bản đồ, OTP hoặc quản trị chủ sân đầy đủ. Bài tìm người được tạo độc lập với booking trong bản đầu.
- Quên mật khẩu có màn hình và kiểm tra dữ liệu nhập; cách gửi liên kết khôi phục thật sẽ chốt cùng phương án xác thực.

## 2. Sơ đồ màn hình và di chuyển

Ba mục điều hướng chính sau khi vào app: **Sân**, **Tìm người**, **Cá nhân**.

| Mã | Màn hình | Đi tới |
| --- | --- | --- |
| A1 | Đăng nhập | Đăng ký (A2), Quên mật khẩu (A3), Sân (S1) khi đăng nhập thành công |
| A2 | Đăng ký | Đăng nhập (A1) |
| A3 | Quên mật khẩu | Đăng nhập (A1) |
| S1 | Danh sách sân, lọc môn/khu vực/ngày | Chi tiết sân (S2), Tìm người (P1), Cá nhân (U1) |
| S2 | Chi tiết sân | Chọn ngày và khung giờ (S3) |
| S3 | Chọn một ngày và một khung giờ | Xác nhận đặt sân (S4) |
| S4 | Xác nhận đặt sân | Chi tiết lịch đặt (S6) khi lưu thành công |
| S5 | Lịch đặt của tôi | Chi tiết lịch đặt (S6) |
| S6 | Chi tiết lịch đặt | Lịch đặt của tôi (S5) |
| P1 | Danh sách bài tìm người | Chi tiết bài (P2), Tạo bài (P3), Bài của tôi (P4) |
| P2 | Chi tiết bài và trạng thái yêu cầu của tôi | Gửi/rút yêu cầu; về P1 hoặc P4 |
| P3 | Tạo/chỉnh sửa bài của tôi | Chi tiết bài (P2) sau khi lưu |
| P4 | Bài của tôi | P2, P3, quản lý yêu cầu (P5) |
| P5 | Yêu cầu tham gia bài của tôi | Duyệt/từ chối; về P4 |
| U1 | Hồ sơ | Chỉnh sửa hồ sơ (U2), lịch đặt (S5), bài của tôi (P4), đăng xuất |
| U2 | Chỉnh sửa hồ sơ | Hồ sơ (U1) |

Người chưa đăng nhập có thể xem danh sách và chi tiết sân/bài mẫu. Khi bấm **Đặt sân**, **Tạo bài** hoặc **Gửi yêu cầu**, app mở A1; sau đăng nhập quay lại hành động trước đó. Đây là đề xuất cần cả nhóm duyệt vì ảnh hưởng điều hướng chung.

## 3. Tên dữ liệu chung

Tên trường dùng tiếng Anh trong Java; nhãn trên giao diện dùng tiếng Việt. `id` và các trường `...Id` cùng một kiểu `String`; ngày dạng `yyyy-MM-dd`, giờ dạng `HH:mm` theo giờ TP.HCM; tiền là số nguyên VND.

| Đối tượng | Trường tối thiểu | Ý nghĩa |
| --- | --- | --- |
| `UserProfile` | `id`, `displayName`, `email`, `phone`, `favoriteSportIds` | Thông tin hiển thị; không lưu mật khẩu trong hồ sơ |
| `Sport` | `id`, `name` | Hai giá trị mẫu: bóng đá mini, cầu lông |
| `Venue` | `id`, `name`, `district`, `address`, `description`, `openingHour`, `closingHour`, `rules`, `amenities`, `imageResId` | Địa điểm/cụm sân để hiển thị và lọc; giờ là số nguyên 0–23 |
| `Court` | `id`, `venueId`, `sportId`, `name`, `description`, `pricePerHour` | Sân cụ thể có thể đặt; một địa điểm có thể có nhiều sân |
| Khung giờ | `startHour` | Bản mẫu tạo các khung 1 giờ từ `openingHour` đến trước `closingHour`; chưa có lớp `TimeSlot` riêng |
| `Booking` | `id`, `userId`, `courtId`, `playDate`, `startHour`, `customerName`, `customerPhone`, `note`, `totalPrice`, `status` | Một người đặt một sân trong một khung giờ |
| `MatchPost` | `id`, `ownerId`, `sportId`, `title`, `locationText`, `playDate`, `startTime`, `endTime`, `neededPlayers`, `description`, `status` | Bài tìm thêm người chơi hoặc đối thủ |
| `JoinRequest` | `id`, `postId`, `requesterId`, `message`, `status`, `createdAt` | Yêu cầu tham gia một bài |

Trạng thái dự kiến: `Booking.status`: `CONFIRMED` (thêm `CANCELLED` khi có chức năng hủy); `MatchPost.status`: `OPEN`, `CLOSED`; `JoinRequest.status`: `PENDING`, `APPROVED`, `REJECTED`, `WITHDRAWN`.

Dữ liệu sân mẫu: bốn địa điểm hư cấu (hai địa điểm bóng đá mini, hai địa điểm cầu lông), mỗi địa điểm có hai sân con, giá và mô tả riêng. Phần tìm người sẽ dùng người dùng mẫu sau. Không dùng thông tin liên hệ thật trong dữ liệu mẫu.

## 4. Quy tắc cần kiểm tra

### Tài khoản và quyền

- Thiếu hoặc sai dữ liệu nhập phải có thông báo ngay tại trường liên quan.
- Chỉ người đang đăng nhập được xem/sửa hồ sơ của mình, xem booking của mình và xem trạng thái yêu cầu mình đã gửi.
- Chỉ chủ bài được chỉnh sửa/đóng bài và duyệt/từ chối yêu cầu của bài đó. Kiểm tra quyền tại nơi xử lý dữ liệu, không chỉ ẩn nút.

### Đặt sân

- Không chọn ngày/giờ đã qua hoặc ngoài giờ mở cửa. Một booking chọn đúng một `Court`, một ngày và một khung 1 giờ.
- Không tạo hai booking còn hiệu lực cho cùng `courtId` + `playDate` + `startHour`.
- Sau khi đặt thành công, booking xuất hiện ở lịch của đúng `userId` và chi tiết hiển thị cùng thông tin đã xác nhận.
- Khi dùng dữ liệu chung trên mạng, việc kiểm tra trùng phải xảy ra ở nguồn dữ liệu chung trong thao tác đặt, vì hai người có thể bấm xác nhận gần như cùng lúc. Cách triển khai sẽ chốt cùng giải pháp lưu trữ.

### Kết nối người chơi

- `neededPlayers` là số người cần tuyển thêm, không tính chủ bài; phải lớn hơn 0.
- Chủ bài không gửi yêu cầu vào bài mình. Mỗi cặp `postId` + `requesterId` chỉ có một bản ghi yêu cầu; rút yêu cầu thì đổi trạng thái sang `WITHDRAWN`. Việc gửi lại sau khi rút hoặc bị từ chối để nhóm quyết định sau.
- Chỉ duyệt yêu cầu `PENDING`; số yêu cầu `APPROVED` không vượt `neededPlayers`.
- Người gửi chỉ được rút yêu cầu của mình khi đang `PENDING`; bài `CLOSED` không nhận yêu cầu mới.
- Khi chủ bài sửa bài, app kiểm tra lại dữ liệu nhập và không cho giảm `neededPlayers` thấp hơn số người đã duyệt.

## 5. Giao diện và thông báo đề xuất

- Giao diện tiếng Việt, nền sáng, màu chính xanh dương, màu nhấn xanh lá cho hành động tích cực. Nhóm chốt mã màu cụ thể trước khi tạo tài nguyên dùng chung.
- Nút chính của mỗi màn rõ ràng: **Đặt sân**, **Xác nhận đặt**, **Tạo bài**, **Gửi yêu cầu**, **Duyệt**. Nút phá huỷ/đóng bài cần hộp xác nhận.
- Lỗi nhập liệu hiển thị cạnh ô nhập; thành công/thất bại của thao tác hiển thị bằng thông báo ngắn, dễ hiểu. Danh sách trống có lời giải thích và hành động tiếp theo.

## 6. Thứ tự triển khai và điểm cần nhóm quyết định

1. Cả ba duyệt sơ đồ màn hình, tên dữ liệu, trạng thái và quy tắc ở trên. Mọi đổi tên trường dùng chung phải thông báo cả nhóm.
2. Hoàn thành giao diện và điều hướng với một nguồn dữ liệu mẫu. Mỗi người chạy phần của mình trên emulator trước khi mở Pull Request.
3. Chọn cách xác thực và lưu dữ liệu sau khi nhóm xem lý do, phương án thay thế, quyền truy cập và cách chống đặt trùng. Firebase chưa được tự thêm.
4. Kiểm thử chéo, sửa lỗi, viết báo cáo và tập demo.

**Cần chốt thêm:** yêu cầu/hạn nộp của giảng viên; có cho khách xem sân/bài trước đăng nhập không; khung giờ mẫu và cách hiển thị giá; có cần hủy booking trong bản đầu không; màu và kiểu nút dùng chung.
