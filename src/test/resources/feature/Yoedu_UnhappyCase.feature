Feature: Kiểm thử các trường hợp lỗi chức năng Đăng nhập Yoedu

  Background: Mở trang đăng nhập
    Given Tôi mở trang đăng nhập Yoedu

  @Smoke @Regression @Validation
  Scenario Outline: Đăng nhập không thành công với dữ liệu không hợp lệ
    When Tôi thực hiện nhập tài khoản là "<tai_khoan>" và mật khẩu là "<mat_khau>"
    And Tôi nhấn nút Đăng nhập
    Then Hệ thống phải hiển thị thông báo lỗi tương ứng là "<thong_bao_loi>"

    Examples:
      | tai_khoan   | mat_khau   | thong_bao_loi                                          |
      |             | admin@123  | Vui lòng nhập tài khoản/mật khẩu                       |
      | admin       |            | Vui lòng nhập tài khoản/mật khẩu                       |
      |             |            | Vui lòng nhập tài khoản/mật khẩu                       |
      | admin_sai   | admin@123  | tên tài khoản hoặc mật khẩu không hợp lệ.              |
      | admin       | 123456     | tên tài khoản hoặc mật khẩu không hợp lệ.              |


  @Validation @RoomError
  Scenario Outline: Thêm mới phòng học không thành công do dữ liệu không hợp lệ
    Given Tôi mở trang đăng nhập Yoedu
    When Tôi thực hiện đăng nhập với tài khoản "admin" và mật khẩu "admin@123"
    And Tôi thực hiện mở menu và điều hướng đến mục Phòng học
    And Tôi nhấn nút thêm mới phòng học
    When Tôi nhập cơ sở là "<co_so>" và tên phòng học là "<ten_phong>"
    Then Hệ thống phải chặn lại hiển thị lỗi tại ô là "<loi_tai_o>" và popup tổng là "<loi_popup>"

    Examples:
      | co_so     | ten_phong | loi_tai_o           | loi_popup                  |
      |           | Phòng 501 | Bỏ qua              | Lỗi                        |
      | Thạch Lam |           | Vui lòng nhập tên   | Vui lòng nhập đủ thông tin |
      |           |           | Vui lòng chọn cơ sở | Vui lòng nhập đủ thông tin |