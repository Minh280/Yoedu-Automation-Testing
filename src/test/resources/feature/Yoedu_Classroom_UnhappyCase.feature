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
    And Tôi mở danh sách phòng học
    And Tôi nhấn nút thêm mới phòng học
    When Tôi nhập cơ sở là "<co_so>" và tên phòng học là "<ten_phong>"
    Then Hệ thống hiển thị thông báo "<loi_tai_o>" và "<loi_popup>"

    Examples:
      | co_so     | ten_phong | loi_tai_o           | loi_popup                  |
      |           | Phòng 501 | Bỏ qua              | Lỗi                        |
      | Thạch Lam |           | Vui lòng nhập tên   | Vui lòng nhập đủ thông tin |
      |           |           | Vui lòng chọn cơ sở | Vui lòng nhập đủ thông tin |

  @Validation @RoomEditError
  Scenario Outline: Chỉnh sửa phòng học không thành công do dữ liệu chỉnh sửa lỗi
    Given Tôi mở trang đăng nhập Yoedu
    When Tôi thực hiện đăng nhập với tài khoản "admin" và mật khẩu "admin@123"
    When Tôi mở danh sách phòng học
    And Tôi tìm kiếm phòng học "<ma_phong_cu>"
    And Tôi chọn phòng học "<ma_phong_cu>" và "Sửa"
    And Tôi thực hiện sửa thông tin lỗi gồm Cơ sở mới "<co_so_moi>" và Tên mới "<ten_phong_moi>" rồi nhấn Lưu
    Then Hệ thống hiển thị thông báo "<loi_tai_o>" và "<loi_popup>"

    Examples:
      | ma_phong_cu | co_so_moi | ten_phong_moi       | loi_tai_o           | loi_popup                  |
      | PH010       | Thạch Lam |                     | Vui lòng nhập tên   | Vui lòng nhập đủ thông tin |
      | PH010       |           | Phòng 101           | Vui lòng nhập cơ sở | Lỗi                        |
      | PH010       | Thạch Lam | Phòng học 10 Update | Bỏ qua              | Lỗi                        |

    