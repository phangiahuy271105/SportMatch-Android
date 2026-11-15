# Rà soát phần sân và đặt sân — chuẩn bị demo giữa kỳ

Ngày rà soát: 13/11/2026. Source: `SportMatch-Integration-Test`, nhánh `feature/integrate-team-modules`, commit `44f83b3`.

Chưa có tiêu chí chấm và thời lượng trình bày của giảng viên. Kịch bản 5–7 phút bên dưới là đề xuất của nhóm, không phải yêu cầu môn học.

## Cập nhật giao diện ngày 15/11/2026

- Bỏ nhãn “Ảnh minh họa”, “Sân mẫu” và các hộp lưu ý thử nghiệm trên giao diện; nội dung dưới đây giữ lịch sử rà soát ngày 13/11.
- Nhãn hiện tại: “Lưu lịch đặt”, “Đã lưu”, “Mã lịch đặt”. Lịch vẫn lưu cục bộ, chưa gửi tới sân; xem README để biết phạm vi bản tích hợp.
- Build debug và unit test thành công. Kiểm thử Android: 3 ca BookingPaymentTest và 1 ca kiểm tra package đều đạt.
- Đã thao tác trên emulator: cuộn danh sách, xem chi tiết sân, chọn ngày mai/giờ, nhập thông tin giả, lưu lịch, xem chi tiết và lịch của tôi, mở tab Cá nhân và Tìm người. Phương thức tại sân và trạng thái chưa thanh toán hiển thị đúng.
- Đợt này chưa chạy lại toàn bộ đăng nhập/đổi tài khoản, xoay máy và các luồng duyệt yêu cầu tìm người. Không dùng kết quả kiểm tra nhanh để khẳng định toàn bộ app đã hết lỗi.

## Cập nhật sửa lỗi sau rà soát

- Bổ sung phương thức thanh toán tại sân theo thống nhất: màn xác nhận chọn sẵn phương thức duy nhất, lịch mới lưu `paymentMethod = PAY_AT_VENUE`, `paymentStatus = UNPAID`. Danh sách và chi tiết đọc lại hai trường này.
- Lịch cũ thiếu hai trường vẫn đọc được: phương thức là `NOT_SELECTED` (chưa chọn), trạng thái `UNPAID` vì bản trước chưa có giao dịch. Không tự gán phương thức cho lịch cũ.
- Trạng thái thanh toán độc lập trạng thái lịch; qua giờ chơi không tự đánh dấu đã thanh toán. Chưa có nút tự xác nhận đã trả tiền, đặt cọc hoặc giao dịch online.
- Khi demo, bổ sung bước chỉ phương thức tại sân ở màn xác nhận và trạng thái chưa thanh toán trong chi tiết/lịch đặt. Nói rõ đây là mô phỏng lựa chọn và lưu trạng thái.
- Đã build và chạy `BookingPaymentTest` trên Medium Phone API 37.0: 3/3 ca đạt, không lỗi. Các ca kiểm tra lưu/đọc phương thức và trạng thái kèm quyền sở hữu/chặn trùng, đọc lịch cũ thiếu trường, lịch quá khứ vẫn chưa thanh toán. Test dùng vùng lưu riêng. Chưa kiểm tra trực quan bố cục mới trên emulator.
- Đã sửa lỗi A: cả ba màn xác nhận, danh sách và chi tiết dùng `CurrentUser.getId(Context)` để lấy cùng email đăng nhập hoặc mã khách demo. Giữ nguyên kiểm tra chủ lịch trong BookingStore.
- Đã xử lý việc lưu/khôi phục sân con, ngày và giờ bằng Bundle cho hai màn chi tiết sân và chọn giờ; kiểm tra lại giờ hợp lệ sau khôi phục.
- Build sau thay đổi: `:app:assembleDebug` thành công. Chưa kiểm tra lại các thao tác đăng nhập/đổi tài khoản/xoay máy trên emulator; các ca 7, 10, 11 vẫn cần chạy.
- Chính sách khách được đặt bằng mã demo vẫn như trước; cần nhóm chốt yêu cầu đăng nhập. Các cải thiện mục D chưa triển khai.
- Những phát hiện bên dưới mô tả bản trước khi sửa để giữ căn cứ rà soát.

## 1. Kết quả kiểm tra thực tế

- Đã đọc source của danh sách sân, chi tiết sân, chọn giờ, xác nhận, lịch đặt, chi tiết lịch và lớp lưu dữ liệu.
- Đã chạy `:app:assembleDebug`: BUILD SUCCESSFUL.
- Chưa thực hiện lại các thao tác trên emulator trong đợt rà soát này. Build thành công chỉ chứng minh đóng gói được APK, chưa chứng minh toàn bộ luồng đúng.
- Chưa sửa logic app trong đợt rà soát; tài liệu này ghi phát hiện và hướng xử lý.

## 2. Các lỗi và điểm cần chốt trước demo

### A. Ưu tiên cao: mở chi tiết lịch của người đã đăng nhập bị sai chủ sở hữu

- `BookingConfirmActivity.java:113` lưu `userId` theo email khi đã đăng nhập.
- `MyBookingsActivity.java:83` cũng đọc lịch theo email.
- `BookingDetailActivity.java:30` lại đọc theo `CurrentUser.getId()`, hiện luôn trả `demo_user_1`.
- Hệ quả suy ra trực tiếp từ source: người đã đăng nhập đặt thành công hoặc mở lịch từ danh sách sẽ không tìm thấy chi tiết lịch thuộc email đó.
- Cách tái hiện: đăng nhập A → đặt sân → màn chi tiết báo không tìm thấy; vào lịch đặt A → bấm lịch vừa lưu → gặp lại lỗi.
- Hướng sửa: dùng cùng một cách lấy người hiện tại tại cả ba màn; giữ kiểm tra chủ sở hữu trong BookingStore, không bỏ điều kiện userId để chữa lỗi.

### B. Cần chốt: khách chưa đăng nhập vẫn được đặt vào một người mẫu chung

- Cả bước lưu và danh sách đều dùng `demo_user_1` khi chưa đăng nhập.
- Hệ quả: lịch khách trên cùng thiết bị dùng chung một danh tính; đặt lúc là khách rồi đăng nhập sẽ không thấy lịch khách trong danh sách của tài khoản.
- Đây là giới hạn/chính sách chưa thống nhất, không phải phân quyền tài khoản hoàn chỉnh.
- Đề xuất: cho khách xem sân, yêu cầu đăng nhập trước khi xác nhận đặt; sau đăng nhập giữ lại sân/ngày/giờ đang chọn. Cần thống nhất với thành viên tài khoản trước khi nối luồng.

### C. Cần cải thiện: giữ lựa chọn khi màn hình được tạo lại

- `VenueDetailActivity.java:52` luôn chọn sân con đầu tiên trong onCreate.
- `SelectTimeSlotActivity` luôn khởi tạo lại ngày từ Intent hoặc hôm nay; selectedHour bắt đầu bằng -1 và chưa được lưu vào savedInstanceState.
- Hệ quả: xoay máy hoặc Android tạo lại Activity có thể mất lựa chọn sân con, ngày và giờ; trạng thái Chip được hệ thống phục hồi có thể không đồng bộ với biến Java.
- Hướng sửa đơn giản: lưu COURT_ID, ngày và giờ đã chọn bằng Bundle; khôi phục trước khi vẽ, rồi kiểm tra lại giờ còn hợp lệ.
- Kiểm tra thêm trạng thái mở/đóng bộ lọc và tab lịch đang xem khi xoay máy.

### D. Cải thiện sau các lỗi trên

- Khi ngày đã hết giờ trống, nên có thông báo riêng và nút chọn ngày mai. Hiện source chỉ khóa các ô và vô hiệu hóa nút tiếp tục.
- Tìm tên sân hiện so khớp chữ thường có dấu; gõ `tan binh` chưa khớp `Tân Bình`. Có thể bổ sung tìm không dấu.
- `BookingTime.slotTime()` dùng Calendar mặc định có thể tự điều chỉnh ngày sai, ví dụ ngày 31 của tháng 2. Nên kiểm tra ngày nghiêm ngặt ở lớp dữ liệu; DatePicker thông thường đã giới hạn ngày chọn hợp lệ.
- `BookingStore.readArray()` trả mảng rỗng nếu JSON hỏng. Nếu sau đó lưu mới, dữ liệu hỏng có thể bị ghi đè. Nên báo lỗi đọc dữ liệu thay vì coi như chưa có lịch.
- Chưa cần thêm thanh toán, bản đồ, chat hoặc nhiều khung giờ để chuẩn bị demo này.

## 3. Những chức năng đã thấy trong source

- 4 địa điểm hư cấu, 8 sân con, 2 môn; ảnh minh họa, mô tả, tiện ích và giờ mở cửa riêng.
- Lọc kết hợp tên sân, môn, khu vực và ngày còn giờ trống.
- Khối tìm kiếm/bộ lọc và danh sách nằm chung vùng cuộn; nút bộ lọc mở/thu gọn phần lựa chọn.
- Mỗi booking đặt một sân con, một ngày và một khung 1 giờ theo giờ mở cửa.
- Chặn giờ đã bắt đầu; kiểm tra lại khi tiếp tục và xác nhận.
- Kiểm tra tên, số điện thoại và lấy giá từ sân con.
- Kiểm tra trùng courtId + playDate + startHour với lịch CONFIRMED.
- Lưu JSON trong SharedPreferences; phân nhóm lịch sắp tới/đã qua giờ và sắp xếp thời gian.

## 4. Giới hạn phải nói đúng khi báo cáo

- Dữ liệu đang nằm trên thiết bị, chưa đồng bộ giữa hai máy.
- Chống trùng hiện là kiểm tra dữ liệu cục bộ. Chưa có giao dịch tại server để xử lý nhiều người đặt đồng thời.
- Phân tách lịch hiện dựa vào mã/email người dùng trong app; chưa có xác thực và phân quyền ở server.
- Phần tài khoản tích hợp đang lưu mật khẩu cục bộ dạng chuỗi; cần trao đổi riêng với người phụ trách, không mô tả là hệ thống tài khoản bảo mật dùng thật.
- Giờ được kiểm tra dựa trên đồng hồ thiết bị theo múi giờ TP.HCM, chưa có giờ server.
- Lịch “Đã lưu (mẫu)” chưa gửi tới sân, không có thanh toán hoặc hủy lịch. “Đã qua giờ” không khẳng định khách đã đến chơi.

## 5. Checklist chạy trên emulator trước khi trình bày

Mỗi ca ghi Đạt/Không đạt, ảnh minh chứng và lỗi nếu có. Hiện các ca bên dưới chưa được đánh dấu đạt trong đợt rà soát này.

| Ca | Thao tác | Kết quả mong đợi |
| --- | --- | --- |
| 1 | Mở app, vuốt xem sân | Cả phần đầu cuộn đi, thanh điều hướng dưới vẫn dùng được |
| 2 | Mở bộ lọc, chọn cầu lông/Tân Bình | Chỉ còn sân phù hợp; tóm tắt và số kết quả đúng |
| 3 | Nhập tên không tồn tại | Có thông báo rỗng, đổi/bỏ lọc xem lại được sân |
| 4 | Chọn sân con số 2, đổi ngày, chọn giờ | Tên sân, sân con, ngày, giờ, giá đúng tại xác nhận |
| 5 | Nhập tên trống hoặc số điện thoại sai | Báo lỗi tại ô, không lưu booking |
| 6 | Chọn hôm nay và xem giờ đã bắt đầu | Không cho đặt giờ đã qua |
| 7 | Đăng nhập A, đặt ngày mai | Lưu thành công, mở được chi tiết, xuất hiện trong lịch A — cần sửa lỗi A trước |
| 8 | Đặt lại đúng sân con/ngày/giờ | Không tạo booking thứ hai; sân con khác cùng giờ vẫn đặt được |
| 9 | Đóng rồi mở app | Lịch đã lưu còn nguyên |
| 10 | Đổi từ tài khoản A sang B | B không thấy lịch của A; giờ A đã đặt vẫn bận trên thiết bị đó |
| 11 | Xoay máy ở màn chọn sân con/ngày/giờ | Lựa chọn và nội dung xác nhận vẫn đồng nhất — cần xử lý lỗi C |
| 12 | Quay lại sau khi đặt thành công | Giờ vừa đặt được cập nhật thành đã đặt |

Không xóa dữ liệu ứng dụng để chuẩn bị demo khi chưa lưu lại các dữ liệu cần giữ. Dùng tên/số điện thoại giả, chọn ngày mai và một khung còn trống để lặp lại demo.

## 6. Kịch bản demo đề xuất 5–7 phút, sau khi sửa và kiểm tra

1. Khoảng 30 giây: mục tiêu SportMatch, người dùng sinh viên/người trẻ và phần phụ trách sân/đặt sân.
2. Khoảng 1 phút: giới thiệu danh sách, ảnh, cuộn trang, lọc cầu lông theo khu vực; thử một bộ lọc không có kết quả.
3. Khoảng 1 phút: mở sân, mô tả riêng, tiện ích, giờ hoạt động và hai sân con với giá.
4. Khoảng 1–2 phút: chọn ngày mai/khung 1 giờ, nhập sai số điện thoại để minh họa kiểm tra đầu vào, sửa đúng rồi lưu.
5. Khoảng 1 phút: xem chi tiết/lịch của tài khoản và quay lại thấy giờ đã đặt bị khóa.
6. Khoảng 30–60 giây: giải thích Java xử lý sự kiện, XML dựng giao diện, Venue/Court/Booking mô tả dữ liệu và SharedPreferences lưu bản mẫu.
7. Kết thúc bằng giới hạn hiện tại và việc tiếp theo: hoàn thiện luồng tài khoản, kiểm thử chéo, chốt giải pháp dữ liệu chung với nhóm.

## 7. Đoạn trình bày mẫu

“Em phụ trách chức năng tìm sân và đặt sân của SportMatch. Bản giữa kỳ dùng Java và XML Views, gồm bốn địa điểm mẫu cho bóng đá mini và cầu lông. Người dùng lọc sân, xem chi tiết, chọn sân con, ngày và khung một giờ, sau đó xác nhận và xem lịch đã lưu. App kiểm tra dữ liệu nhập, giờ đã qua và khung giờ đã được đặt trên cùng thiết bị. Dữ liệu hiện lưu bằng SharedPreferences; nhóm chưa kết nối dữ liệu chung hay thanh toán thật. Giai đoạn tiếp theo nhóm sẽ thống nhất xác thực, phân quyền và cách chống trùng khi nhiều thiết bị cùng đặt.”

Chỉ nói các ca kiểm tra đã đạt sau khi nhóm thực sự chạy thử; bổ sung lỗi còn tồn tại nếu chưa xử lý xong.
