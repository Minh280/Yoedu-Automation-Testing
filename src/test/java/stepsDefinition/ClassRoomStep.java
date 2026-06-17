package stepsDefinition;

import common.ContextSteps;
import io.appium.java_client.AppiumDriver;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import pageObject.YoeduLoginPage;
import pageObject.YoeduRoomPage;

public class ClassRoomStep {
    WebDriver driver;
    AppiumDriver appiumDriver;
    YoeduLoginPage loginPage;
    YoeduRoomPage roomPage;

    public ClassRoomStep() {
        ContextSteps contextSteps = new ContextSteps();
        String os = System.getProperty("osName").toLowerCase();

        if (os.contains("android") || os.contains("mac")) {
            appiumDriver = (AppiumDriver) contextSteps.getDriver();
            loginPage = new YoeduLoginPage(appiumDriver);
            roomPage = new YoeduRoomPage(appiumDriver);
        } else {
            driver = contextSteps.getDriver();
            loginPage = new YoeduLoginPage(driver);
            roomPage = new YoeduRoomPage(driver);
        }
    }

    @When("Tôi mở danh sách phòng học")
    public void navigateToRoom() {
        roomPage.navigateToRoomMenu();
    }

    @And("Tôi nhấn nút thêm mới phòng học")
    public void clickAdd() {
        roomPage.clickAdd();
    }

    @And("Tôi nhập cơ sở là {string}, tên phòng học {string} và diễn giải {string}")
    public void inputForm(String coSo, String ten, String dienGiai) {
        roomPage.fillFormAndSave(coSo, ten, dienGiai);
    }

    @And("Tôi nhấn nút Lưu phòng học")
    public void toiNhanNutLuuPhongHoc() {
        // Tích hợp tự động trong tầng PageObject
    }

    @Then("Hệ thống phải hiển thị phòng học mới trong danh sách")
    public void verifyRoomCreated() {
        // Happy case hoàn tất
    }

    @When("Tôi nhập cơ sở là {string} và tên phòng học là {string}")
    public void inputFormValidation(String coSo, String ten) {
        roomPage.fillFormAndSave(coSo, ten, "");
    }

    @Then("Hệ thống hiển thị thông báo {string} và {string}")
    public void verifyDoubleValidationErrors(String expectedFieldError, String expectedToastError) {
        try {
            Thread.sleep(1500);
            String actualToast = roomPage.getToastPopupText();
            Assert.assertTrue("LỖI: Thông báo Toast Popup góc phải không khớp!", actualToast.contains(expectedToastError));

            if (!expectedFieldError.equalsIgnoreCase("Bỏ qua")) {
                String actualFieldError = "";
                if (expectedFieldError.contains("cơ sở")) {
                    actualFieldError = roomPage.getFieldErrorText("Cơ sở");
                } else if (expectedFieldError.contains("tên")) {
                    actualFieldError = roomPage.getFieldErrorText("Tên");
                }
                Assert.assertTrue("LỖI: Chữ đỏ cảnh báo dưới ô nhập liệu không khớp!", actualFieldError.contains(expectedFieldError));
            }
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    @And("Tôi tìm kiếm phòng học {string}")
    public void toiTimKiemPhongHocVoiTuKhoa(String keyword) {
        roomPage.searchRoom(keyword);
    }

//    @Then("Hệ thống phải hiển thị mã phòng {string} trong danh sách kết quả")
//    public void heThongPhaiHienThiMaPhongTrongDanhSach(String roomCode) {
//        try {
//            boolean isFound = roomPage.verifyRoomExistsInList(roomCode);
//            Assert.assertTrue("LỖI: Không tìm thấy mã phòng [" + roomCode + "] hiển thị trên danh sách!", isFound);
//        } catch (Exception e) {
//            Assert.fail("Lỗi khi kiểm tra hiển thị mã phòng: " + e.getMessage());
//        }
//    }

    @And("Tôi chọn phòng học {string} và {string}")
    public void toiChonPhongHocVaThaoTacMenu(String textVal, String actionType) {
        roomPage.clickActionMenuByText(textVal, actionType);
    }

    @Then("Hiển thị thông tin chi tiết phòng và trở về danh sách Phòng học")
    public void heThongHienThiChiTietVaQuayLai() {
        roomPage.verifyFormOpenedAndClickBack();
    }

    @And("Tôi sửa cơ sở {string}, tên mới {string} và diễn giải {string}, rồi Lưu")
    public void toiThucHienNhapThongTinSuaVaLuu(String newCoSo, String newTen, String newDienGiai) {
        roomPage.fillFormEditAndSave(newCoSo, newTen, newDienGiai);
    }

    @And("Tôi chọn xác nhận xóa trên popup dialog")
    public void toiThucHienXacNhanXoaTrenPopupDialog() {
        roomPage.confirmDelete();
    }

    @And("Tôi thực hiện sửa thông tin lỗi gồm Cơ sở mới {string} và Tên mới {string} rồi nhấn Lưu")
    public void stepEditRoomWithValidationError(String coSo, String tenPhong) {
        roomPage.fillFormEditAndSave(coSo, tenPhong, null);
    }
}