# TÀI LIỆU THIẾT KẾ PHÂN HỆ: TÀI KHOẢN & HỒ SƠ
**Người phụ trách:** Người số 2 (Triệu Hoàng Đăng Khoa)
**Nhánh Git:** `feature/tai-khoan`

---

## 1. Danh sách các màn hình
1. **LoginActivity (`activity_login.xml`):** Đăng nhập với Email và Mật khẩu, có validation bắt lỗi chuẩn.
2. **RegisterActivity (`activity_register.xml`):** Đăng ký tài khoản (Tên >= 2 từ, Email chuẩn quốc tế, SĐT 10 số nhà mạng VN, Mật khẩu chữ và số, chọn môn thể thao Pickleball/Bóng đá...).
3. **ForgotPasswordActivity (`activity_forgot_password.xml`):** Khôi phục mật khẩu, kiểm tra email tồn tại và đặt lại mật khẩu mới.
4. **ProfileActivity (`activity_profile.xml`):** Xem & sửa thông tin cá nhân (Tên, SĐT, Giới tính Nam/Nữ, Môn thể thao). Avatar tự động thay đổi động theo Giới tính. Có nút Đăng xuất.

---

## 2. Luồng chuyển màn hình (Navigation Flow)
* **Khởi động App:** Mở `LoginActivity`
    * Bấm "Đăng ký ngay" $\rightarrow$ Mở `RegisterActivity` (Đăng ký xong quay về Login để đăng nhập).
    * Bấm "Quên mật khẩu?" $\rightarrow$ Mở `ForgotPasswordActivity` (Đặt pass mới xong quay về Login).
    * Đăng nhập thành công $\rightarrow$ Chuyển vào `ProfileActivity` (hoặc màn hình chính của App).
* **Tại ProfileActivity:**
    * Bấm "ĐĂNG XUẤT" $\rightarrow$ Thoát phiên đăng nhập, quay về `LoginActivity`.
    * Bảo mật: Chưa đăng nhập mà cố tình vào Profile sẽ tự động bị chuyển hướng về `LoginActivity`.

---

## 3. Cấu trúc trường dữ liệu (Model: `User`)
* `id` (String): Mã định danh duy nhất (ví dụ: `u01`, `user_...`).
* `email` (String): Email đăng nhập duy nhất.
* `password` (String): Mật khẩu đăng nhập (tối thiểu 6 ký tự, gồm cả chữ và số).
* `fullName` (String): Họ và tên hiển thị (tối thiểu 2 từ).
* `phoneNumber` (String): Số điện thoại liên hệ (10 chữ số, chuẩn đầu số VN: 03, 05, 07, 08, 09).
* `favoriteSport` (String): Môn thể thao yêu thích (Bóng đá, Cầu lông, Tennis, Bóng rổ, Bóng chuyền, Pickleball).
* `gender` (String): Giới tính ("Nam" / "Nữ") để hiển thị Avatar cầu thủ tương ứng.

---

## 4. Điểm tích hợp dùng chung cho Nhóm (Sân & Tìm người)
* Bạn số 1 (Đặt sân) và Bạn số 3 (Tìm người) khi cần lấy thông tin người đang đăng nhập, chỉ cần gọi:
  ```java
  User currentUser = SharedPrefManager.getInstance(context).getUser();