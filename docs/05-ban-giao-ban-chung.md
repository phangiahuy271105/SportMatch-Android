# Nhận bản chung SportMatch

## Nội dung tích hợp

- Sân/đặt sân, tài khoản/hồ sơ và tìm người đã cùng nằm trong một app Java + XML.
- Cụm tìm kiếm cuộn cùng danh sách; giờ đặt theo từng địa điểm; dữ liệu mẫu gồm bóng đá mini và cầu lông.
- Các màn đặt sân dùng chung mã người dùng; giữ lựa chọn sân con/ngày/giờ khi màn hình được tạo lại.
- Lưu phương thức thanh toán tại sân và trạng thái chưa thanh toán. Không tự đánh dấu đã trả tiền khi lịch đã qua giờ.
- Bỏ nhãn ảnh minh họa, chữ demo/mẫu và các hộp thử nghiệm trên giao diện. Không thay đổi nguồn dữ liệu, cấu hình SDK, Gradle hoặc thêm thư viện.

## Lấy code mới

Mở Git Bash trong project đã clone từ repository chung. Chạy `git status` trước.

Nếu còn việc làm dở, commit trên nhánh của mình hoặc lưu tạm bằng `git stash push -u -m "Luu viec dang do"`. Không dùng reset/clean để bỏ thay đổi chỉ vì pull bị lỗi.

Khi thư mục làm việc đã sạch:

```bash
git fetch origin
git switch main
git pull --ff-only origin main
```

Nếu chưa có nhánh main tại máy mình, dùng `git switch --track origin/main`. Nếu pull báo nhánh phân kỳ, dừng lại và gửi thông báo cho người điều phối; không force push.

Mở đúng project trong Android Studio, đợi Gradle Sync, dùng JDK đi kèm Studio rồi Run app. Giữ `local.properties` riêng trên từng máy. Không lấy file này từ máy bạn.

Tạo nhánh mới trước khi làm việc tiếp, ví dụ `git switch -c feature/ho-so-update`. Nếu đã stash, chỉ áp dụng lại sau khi quay về nhánh đang làm; không tự chép việc dở vào main. Làm xong chạy thử, commit, push, mở Pull Request.

## Thử nhanh trước khi nhận việc mới

1. Mở Tìm sân, cuộn danh sách; lọc tên, môn, khu vực và ngày.
2. Xem chi tiết sân, chọn sân con và một giờ còn trống ngày mai.
3. Nhập thông tin giả; lưu lịch, xem “Đã lưu” và “Chưa thanh toán”.
4. Vào Lịch đặt của tôi, mở lại lịch. Giờ vừa đặt phải khóa khi quay lại chọn cùng sân con/ngày.
5. Mở Cá nhân, màn đăng nhập và Tìm người để kiểm tra điều hướng của phần tích hợp.

Nguồn dữ liệu vẫn nằm trên thiết bị; hai emulator không chia sẻ lịch. Các giới hạn xác thực, dữ liệu và thanh toán được ghi trong README.
