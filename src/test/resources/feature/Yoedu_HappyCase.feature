Feature: TestYoedu

  Background: Đăng nhập hệ thống
    Given Tôi mở trang đăng nhập Yoedu
    When Tôi thực hiện đăng nhập với tài khoản "admin" và mật khẩu "admin@123"

  @Smoke @GradeLevel
  Scenario: Tạo mới khối lớp thành công
    And Tôi thực hiện mở menu và điều hướng đến mục Khối lớp
    And Tôi nhấn nút thêm mới khối lớp
    And Tôi nhập tên khối lớp là "Khối 12" và diễn giải "Khối tốt nghiệp 2026"
    And Tôi nhấn nút Lưu khối lớp
    Then Hệ thống phải hiển thị khối lớp mới trong danh sách

  @Smoke @Room
  Scenario: Tạo mới phòng học thành công
    And Tôi thực hiện mở menu và điều hướng đến mục Phòng học
    And Tôi nhấn nút thêm mới phòng học
    And Tôi nhập cơ sở là "Thạch Lam", tên phòng học "Phòng 101" và diễn giải "Lầu 1"
    And Tôi nhấn nút Lưu phòng học
    Then Hệ thống phải hiển thị phòng học mới trong danh sách

  @Smoke @Student
  Scenario: Tạo mới học viên thành công
    And Tôi thực hiện mở menu và điều hướng đến mục Học viên
    And Tôi nhấn nút thêm mới học viên
    And Tôi nhập thông tin phụ huynh gồm SĐT "0901234567", Tên "Nguyễn Văn A", Giới tính "Nam"
    And Tôi nhập thông tin học viên gồm Tên "Nguyễn Văn B", Ngày sinh "20/05/2015", Giới tính "Nam"
    And Tôi nhấn nút Lưu học viên
    Then Hệ thống phải hiển thị thông báo lưu học viên thành công


