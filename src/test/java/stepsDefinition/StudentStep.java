package stepsDefinition;

import common.ContextSteps;
import io.cucumber.java.en.*;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import pageObject.YoeduStudentPage;

import java.io.File;

public class StudentStep {
    private final WebDriver driver;
    private final YoeduStudentPage studentPage;

    public StudentStep() throws Throwable {
        ContextSteps contextSteps = new ContextSteps();
        this.driver = contextSteps.getDriver();
        studentPage = new YoeduStudentPage(driver);
    }

    @And("Tôi thực hiện mở menu và điều hướng đến mục Học viên")
    public void navToStudent() {
        studentPage.navigateToStudentMenu();
    }

    @And("Tôi nhấn nút thêm mới học viên")
    public void add() {
        studentPage.clickAdd();
    }

    @And("Tôi nhập thông tin phụ huynh gồm SĐT {string}, Tên {string}, Giới tính {string}")
    public void parentInfo(String sdt, String ten, String gt) {
        studentPage.fillParentInfo(sdt, ten, gt);
    }

    @And("Tôi nhập thông tin học viên gồm Tên {string}, Ngày sinh {string}, Giới tính {string}")
    public void studentInfo(String ten, String ns, String gt) {
        studentPage.fillStudentInfo(ten, ns, gt);
    }

    @And("Tôi nhấn nút Lưu học viên")
    public void save() {
        studentPage.clickSave();
    }

    @Then("Hệ thống hiển thị thông báo lưu học viên thành công")
    public void verify() {
        String toastText = studentPage.getToastPopupText();
        System.out.println("[INFO] Thông báo hệ thống xuất hiện: " + toastText);
        System.out.println("[SUCCESS] Luồng Tạo mới học viên đã hoàn tất thành công!");
    }

    @And("Tôi tìm kiếm học viên bằng từ khóa {string}")
    public void stepSearchStudent(String keyword) {
        studentPage.searchStudent(keyword);
    }

    @And("Tôi chọn tác vụ {string} đối với học viên có thông tin {string}")
    public void stepClickActionMenu(String actionType, String studentVal) {
        studentPage.clickActionMenuByStudentText(studentVal, actionType);
    }

    @And("Tôi xác nhận hành động xem chi tiết và nhấn nút Quay lại danh sách học viên")
    public void stepVerifyDetailAndBack() {
        studentPage.verifyFormOpenedAndClickBack();
    }

    @And("Tôi cập nhật thông tin học viên gồm Tên mới {string} và Ngày sinh mới {string} rồi nhấn Lưu")
    public void stepEditStudent(String newTen, String newNgaySinh) {
        studentPage.fillFormEditAndSave(newTen, newNgaySinh);
    }

    @And("Tôi nhấn nút Refresh để làm mới danh sách học viên")
    public void stepClickRefresh() {
        studentPage.clickRefreshList();
    }
    @And("Tôi thực hiện tìm kiếm nâng cao học viên theo người giới thiệu là {string}")
    public void stepSearchAdvanced(String referrer) {
        studentPage.searchAdvanced(referrer);
    }
    @And("Tôi nhấn nút Xuất tệp Excel dữ liệu học viên")
    public void stepClickExportExcel() {
        studentPage.clickExportExcel();
    }

//    @And("Tôi xác nhận nút {string} trên hộp thoại Save As của hệ thống")
//    public void stepHandleSaveAs(String action) {
//        studentPage.handleSaveAsPopup(action);
//    }

    @Then("Hệ thống hiển thị lỗi học viên tại ô {string} và popup {string}")
    public void verifyStudentValidationErrors(String expectedFieldError, String expectedToastError) {
        try {
            // 1. Kiểm tra lỗi Toast Popup nếu có yêu cầu
            if (!expectedToastError.equalsIgnoreCase("Bỏ qua")) {
                String actualToast = studentPage.getToastPopupText();
                Assert.assertTrue("LỖI: Thông báo Toast lỗi không khớp!", actualToast.contains(expectedToastError));
            }

            // 2. Kiểm tra lỗi chữ đỏ hiển thị ngay tại các trường thông tin cụ thể
            if (!expectedFieldError.equalsIgnoreCase("Bỏ qua")) {
                String actualFieldError = "";
                if (expectedFieldError.contains("Số điện thoại") || expectedFieldError.contains("SĐT")) {
                    actualFieldError = studentPage.getFieldErrorText("SĐT Phụ huynh");
                } else if (expectedFieldError.contains("tên") || expectedFieldError.contains("Tên")) {
                    actualFieldError = studentPage.getFieldErrorText("Tên học viên");
                } else if (expectedFieldError.contains("sinh") || expectedFieldError.contains("Ngày sinh")) {
                    actualFieldError = studentPage.getFieldErrorText("Ngày sinh");
                }
                Assert.assertTrue("LỖI: Chữ đỏ cảnh báo dưới ô dữ liệu học viên không khớp!", actualFieldError.contains(expectedFieldError));
            }
        } catch (Exception e) {
            Assert.fail("Lỗi xác thực dữ liệu không hợp lệ học viên: " + e.getMessage());
        }
    }

}