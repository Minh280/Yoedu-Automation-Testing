Feature: Kiểm thử các trường hợp dữ liệu lỗi chức năng Học viên Yoedu

  Background: Đăng nhập và chuẩn bị dữ liệu hệ thống
    Given Tôi mở trang đăng nhập Yoedu
    When Tôi thực hiện đăng nhập với tài khoản "admin" và mật khẩu "admin@123"

  @Validation @CreateStudentError
  Scenario Outline: Thêm mới học viên không thành công do dữ liệu không hợp lệ
    Given Tôi thực hiện mở menu và điều hướng đến mục Học viên
    When Tôi nhấn nút thêm mới học viên
    And Tôi nhập thông tin phụ huynh gồm SĐT "<sdt_phu_huynh>", Tên "Nguyễn Văn Phụ Huynh", Giới tính "Nam"
    And Tôi nhập thông tin học viên gồm Tên "<ten_hoc_vien>", Ngày sinh "<ngay_sinh>", Giới tính "Nam"
    And Tôi nhấn nút Lưu học viên
    Then Hệ thống hiển thị lỗi học viên tại ô "<loi_tai_o>" và popup "<loi_popup>"

    Examples:
      | sdt_phu_huynh | ten_hoc_vien | ngay_sinh  | loi_tai_o                                     | loi_popup  |
      | 0901234567    |              | 20/05/2015 | Vui lòng nhập tên!                            | Bỏ qua     |
      | 0912          | Nguyễn Văn B | 20/05/2015 | Số điện thoại không đúng, vui lòng nhập lại   | Bỏ qua     |
      | 0901234567    | Nguyễn Văn B | abcde      | Vui lòng chọn ngày sinh                       | Bỏ qua     |

  @Validation @EditStudentError #Blocked
  Scenario Outline: Chỉnh sửa học viên không thành công do xóa dữ liệu bắt buộc
    Given Tôi thực hiện mở menu và điều hướng đến mục Học viên
    And Tôi tìm kiếm học viên bằng từ khóa "<ma_hoc_vien>"
    And Tôi chọn tác vụ "Sửa" đối với học viên có thông tin "<ma_hoc_vien>"
    And Tôi cập nhật thông tin học viên gồm Tên mới "<ten_moi>" và Ngày sinh mới "" rồi nhấn Lưu
    Then Hệ thống hiển thị lỗi học viên tại ô "<loi_tai_o>" và popup "Bỏ qua"

    Examples:
      | ma_hoc_vien | ten_moi | loi_tai_o          |
      | HV1781      |         | Vui lòng nhập tên! |

  @Validation @SearchStudentError
  Scenario: Tìm kiếm học viên không tồn tại
    Given Tôi thực hiện mở menu và điều hướng đến mục Học viên
    When Tôi tìm kiếm học viên bằng từ khóa "KJHDKJ123_RAC"


  @Validation @AdvancedSearchStudentError
  Scenario: Tìm kiếm nâng cao học viên theo người giới thiệu không tồn tại
    Given Tôi thực hiện mở menu và điều hướng đến mục Học viên
    When Tôi thực hiện tìm kiếm nâng cao học viên theo người giới thiệu là "Ten_Nguoi_Gioi_Thieu_Khong_Ton_Tai_999"