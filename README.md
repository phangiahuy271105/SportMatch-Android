# SportMatch Android

Ứng dụng đặt sân và kết nối người chơi thể thao tại TP.HCM. Bản đầu dùng Java, XML Views và dữ liệu mẫu để cả nhóm hoàn thiện giao diện, điều hướng và luồng sử dụng trước khi chọn cách kết nối dữ liệu.

Hai môn của bản đầu: **bóng đá mini** và **cầu lông**.

## Chạy project

1. Mở thư mục này bằng Android Studio.
2. Đợi Gradle đồng bộ xong. Nếu Studio yêu cầu Gradle JDK, chọn JDK đi kèm Android Studio.
3. Chọn Android Emulator và bấm **Run app**.

Đã tích hợp phần sân/đặt sân, tài khoản/hồ sơ và tìm người vào cùng ứng dụng. Phần đặt sân có 6 màn, lọc sân, chọn sân con/giờ, lưu lịch theo người dùng và xem lại lịch. Thanh toán hiện hỗ trợ ghi nhận phương thức **Thanh toán tại sân**, trạng thái **Chưa thanh toán**; chưa xử lý giao dịch thật.

Xem [hướng dẫn thử luồng sân](docs/UI_HANDOVER_GUIDE.md) và [hướng dẫn nhận bản chung](docs/05-ban-giao-ban-chung.md).

## Phạm vi của bản hiện tại

- Dữ liệu sân là hư cấu; ảnh sân được tạo để minh họa. Giao diện đã bỏ các nhãn thử nghiệm để gọn hơn, nhưng dữ liệu vẫn là dữ liệu phục vụ học tập.
- Lịch, tài khoản và bài tìm người lưu trên thiết bị. Chưa đồng bộ giữa các điện thoại hoặc gửi lịch tới chủ sân.
- “Đã lưu” chỉ xác nhận lưu trong app. Chống trùng giờ hiện áp dụng trên một thiết bị, chưa xử lý đặt đồng thời qua server.
- Tài khoản và khôi phục mật khẩu hiện là luồng cục bộ, chưa có xác minh email/OTP hay phân quyền tại server. Không dùng thông tin hoặc mật khẩu thật để thử.
- Chưa có đặt cọc, thanh toán online, xác nhận đã thu tiền hoặc hủy lịch.

## Làm việc nhóm

- `main` là bản chung đã kiểm tra. Mỗi việc làm trên một branch riêng.
- Trước khi làm việc mới: lưu việc đang dở, chuyển về `main`, lấy code mới, rồi tạo branch từ `main`.
- Trước Pull Request: chạy app trên emulator, ghi phần đã làm, cách kiểm tra và lỗi còn tồn tại.
- Chỉ ghép Pull Request vào `main` sau khi nhóm xem và kiểm tra.
- Báo nhóm trước khi sửa file dùng chung hoặc thêm thư viện. Không commit mật khẩu, API key, keystore, `local.properties` hay dữ liệu cá nhân thật.

Website SportMatch cũ là tài liệu tham khảo nghiệp vụ; source Android được phát triển riêng trong repo này.
