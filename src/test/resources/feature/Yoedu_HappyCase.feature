Feature: TestYoedu

  Background: Đăng nhập và chuẩn bị dữ liệu hệ thống
    Given Tôi mở trang đăng nhập Yoedu
    When Tôi thực hiện đăng nhập với tài khoản "admin" và mật khẩu "admin@123"

  # =========================================================================
  # KHỐI LỚP
  # =========================================================================
  @Smoke @GradeLevel
  Scenario: Tạo mới khối lớp
    And Tôi thực hiện mở menu và điều hướng đến mục Khối lớp
    And Tôi nhấn nút thêm mới khối lớp
    And Tôi nhập tên khối lớp là "Khối 12" và diễn giải "Khối tốt nghiệp 2026"
    And Tôi nhấn nút Lưu khối lớp
    Then Hệ thống phải hiển thị khối lớp mới trong danh sách

  # =========================================================================
  # PHÒNG HỌC
  # =========================================================================
  @Smoke @CreateRoom
  Scenario: Tạo mới phòng học
    And Tôi mở danh sách phòng học
    And Tôi nhấn nút thêm mới phòng học
    And Tôi nhập cơ sở là "Thạch Lam", tên phòng học "Phòng 101" và diễn giải "Lầu 1"
    And Tôi nhấn nút Lưu phòng học
    Then Hệ thống phải hiển thị phòng học mới trong danh sách

  @ViewRoom
  Scenario: Xem phòng học
    When Tôi mở danh sách phòng học
    And Tôi tìm kiếm phòng học "PH010"
    And Tôi chọn phòng học "PH010" và "Xem"
    Then Hiển thị thông tin chi tiết phòng và trở về danh sách Phòng học

  @EditRoom
  Scenario: Cập nhật phòng học
    When Tôi mở danh sách phòng học
    And Tôi tìm kiếm phòng học "PH010"
    And Tôi chọn phòng học "Diễn giải VP" và "Sửa"
    And Tôi sửa cơ sở "Thạch Lam", tên mới "Phòng học 10 Update" và diễn giải "Diễn giải VP", rồi Lưu
    Then Hệ thống hiển thị thông báo "Bỏ qua" và "Cập nhật thành công"

  @DeleteRoom
  Scenario: Xóa phòng học thành công
    When Tôi mở danh sách phòng học
    And Tôi tìm kiếm phòng học "PH515"
    And Tôi chọn phòng học "PH515" và "Xóa"
    And Tôi chọn xác nhận xóa trên popup dialog
    Then Hệ thống hiển thị thông báo "Bỏ qua" và "Xóa thành công"

  # =========================================================================
  # HỌC VIÊN
  # =========================================================================
  @Smoke @CreateStudent
  Scenario: Tạo mới học viên thành công
    And Tôi thực hiện mở menu và điều hướng đến mục Học viên
    And Tôi nhấn nút thêm mới học viên
    And Tôi nhập thông tin phụ huynh gồm SĐT "0901234567", Tên "Nguyễn Văn A", Giới tính "Nam"
    And Tôi nhập thông tin học viên gồm Tên "Nguyễn Văn B", Ngày sinh "20/05/2015", Giới tính "Nam"
    And Tôi nhấn nút Lưu học viên
    Then Hệ thống hiển thị thông báo lưu học viên thành công

  @ViewStudent
  Scenario: Xem chi tiết học viên
    When Tôi thực hiện mở menu và điều hướng đến mục Học viên
    And Tôi tìm kiếm học viên bằng từ khóa "HV1781"
    And Tôi chọn tác vụ "Xem" đối với học viên có thông tin "HV1781"
    Then Tôi xác nhận hành động xem chi tiết và nhấn nút Quay lại danh sách học viên

  @EditStudent #blocked
  Scenario: Chỉnh sửa thông tin học viên
    When Tôi thực hiện mở menu và điều hướng đến mục Học viên
    And Tôi tìm kiếm học viên bằng từ khóa "Lê Văn DD"
    And Tôi chọn tác vụ "Sửa" đối với học viên có thông tin "Lê Văn DD"
    And Tôi cập nhật thông tin học viên gồm Tên mới "Lê Văn Edit" và Ngày sinh mới "20/12/2015" rồi nhấn Lưu

  @DeleteStudent
  Scenario: Xóa học viên
    When Tôi thực hiện mở menu và điều hướng đến mục Học viên
    And Tôi tìm kiếm học viên bằng từ khóa "HV1685"
    And Tôi chọn tác vụ "Xóa" đối với học viên có thông tin "HV1685"

  @Student @Refresh
  Scenario: Làm mới danh sách học viên thành công
    When Tôi thực hiện mở menu và điều hướng đến mục Học viên
    And Tôi nhấn nút Refresh để làm mới danh sách học viên

  @Student @Search
  Scenario: Tìm kiếm học viên bằng bộ lọc thông thường
    When Tôi thực hiện mở menu và điều hướng đến mục Học viên
    And Tôi tìm kiếm học viên bằng từ khóa "Nguyễn Văn A"

  @Student @AdvancedSearch
  Scenario: Tìm kiếm nâng cao học viên theo người giới thiệu
    When Tôi thực hiện mở menu và điều hướng đến mục Học viên
    And Tôi thực hiện tìm kiếm nâng cao học viên theo người giới thiệu là "0919174757"

  @Student @ExportExcel
  Scenario: Xuất danh sách học viên ra file Excel và lưu thành công
    When Tôi thực hiện mở menu và điều hướng đến mục Học viên
    And Tôi nhấn nút Xuất tệp Excel dữ liệu học viên
    And Tôi xác nhận nút "Save" trên hộp thoại Save As của hệ thống