# SportMatch Android

Ứng dụng đặt sân và kết nối người chơi thể thao tại TP.HCM. Bản đầu dùng Java, XML Views và dữ liệu mẫu để cả nhóm hoàn thiện giao diện, điều hướng và luồng sử dụng trước khi chọn cách kết nối dữ liệu.

Hai môn của bản đầu: **bóng đá mini** và **cầu lông**.

## Chạy project

1. Mở thư mục này bằng Android Studio.
2. Đợi Gradle đồng bộ xong. Nếu Studio yêu cầu Gradle JDK, chọn JDK đi kèm Android Studio.
3. Chọn Android Emulator và bấm **Run app**.

Project gốc đã chạy màn hình mặc định trên emulator. Chức năng SportMatch sẽ được thêm sau khi nhóm duyệt [bản nháp thiết kế MVP](docs/01-thiet-ke-mvp.md) và [bản nháp UI/UX](docs/02-ui-ux-android.md).

## Làm việc nhóm

- `main` là bản chung đã kiểm tra. Mỗi việc làm trên một branch riêng.
- Trước khi làm việc mới: lưu việc đang dở, chuyển về `main`, lấy code mới, rồi tạo branch từ `main`.
- Trước Pull Request: chạy app trên emulator, ghi phần đã làm, cách kiểm tra và lỗi còn tồn tại.
- Chỉ ghép Pull Request vào `main` sau khi nhóm xem và kiểm tra.
- Báo nhóm trước khi sửa file dùng chung hoặc thêm thư viện. Không commit mật khẩu, API key, keystore, `local.properties` hay dữ liệu cá nhân thật.

Website SportMatch cũ là tài liệu tham khảo nghiệp vụ; source Android được phát triển riêng trong repo này.
