package pageObject;

import common.CommonFunctions;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;


public class YoeduStudentPage {
    private final WebDriver driver;
    private final CommonFunctions commonFunction = new CommonFunctions();
    private final int intTimeOut = 20;

    // --- Locators Sidebar ---
    private final By btnMenuHamburger = By.xpath("//div[@class='menu-icon']");
    private final By sidebarArea      = By.className("sidebar-menu");
    private final By menuQuanLyHocVien = By.xpath("//div[@class='sidebar-menu--item' and .//p[text()='Quản lý học viên']]");
    private final By menuHocVienChild  = By.xpath("//div[@class='sidebar-menu--child' and text()='Học viên']");

    // --- Locators Form & Action ---
    private final By btnAddPlus       = By.xpath("//button[@aria-label='add']");
    private final By txtSdtPhuHuynh   = By.xpath("//h4[text()='Thông tin phụ huynh']/following-sibling::div//label[contains(.,'Số điện thoại')]/following-sibling::div//input");
    private final By txtTenPhuHuynh   = By.xpath("//h4[text()='Thông tin phụ huynh']/following-sibling::div//label[contains(.,'Tên phụ huynh')]/following-sibling::div//input");
    private final By drpGioiTinhPH    = By.xpath("//h4[text()='Thông tin phụ huynh']/following-sibling::div//div[@role='combobox']");

    private final By txtTenHocVien    = By.xpath("//h4[text()='Thông tin học viên']/following-sibling::div//label[contains(.,'Tên học viên')]/following-sibling::div//input");
    private final By txtNgaySinh      = By.xpath("//h4[text()='Thông tin học viên']/following-sibling::div//input[@placeholder='DD/MM/YYYY']");
    private final By drpGioiTinhHV    = By.xpath("//h4[text()='Thông tin học viên']/following-sibling::div//div[@role='combobox']");

    private final By btnLuu           = By.xpath("//button[contains(@class, 'save-btn')]");
    private final By btnBackDetail    = By.xpath("//button[contains(@class, 'page-detail-wrapper__back-icon')] | //*[@data-testid='KeyboardBackspaceIcon']");

    // --- Locators Tìm kiếm, Thông báo & Popup ---
    private final By txtSearch        = By.xpath("//input[@placeholder='Tìm kiếm ...']");
    private final By lblToastPopup    = By.xpath("//div[contains(@class, 'MuiSnackbar-root')]//div[contains(@class, 'MuiAlert-message')]");

    private final By btnRefresh       = By.xpath("//button[contains(@class, 'MuiIconButton-root')][2] | //*[local-name()='svg' and contains(@class, 'css-1yxmbwk')]/..");

    private final By btnFilterFunnel = By.xpath("//*[@data-testid='FilterAltIcon'] | //*[contains(@class, 'css-6flbmm')]");
    private final By txtSearchAdvanced = By.xpath("//input[@placeholder='Tìm kiếm theo người giới thiệu ...']");
    private final By btnSubmitSearch = By.xpath("//body/div[2]/div[@role='presentation']/div[@role='dialog']/div[@class='MuiDialogActions-root MuiDialogActions-spacing css-1vskg8q']/button[3]");

    private final By btnExportExcel = By.xpath("/html//div[@id='root']/div[@class='app']/div[3]//div[@class='list-common']/div/div[1]/button[3]");

    // --- Locators Cảnh báo lỗi dưới ô nhập liệu (Bổ sung mới) ---
    private final By lblSdtPhuHuynhError = By.xpath("//h4[text()='Thông tin phụ huynh']/following-sibling::div//label[contains(.,'Số điện thoại')]/following-sibling::div/following-sibling::p[contains(@class, 'Mui-error')]");
    private final By lblTenHocVienError   = By.xpath("//h4[text()='Thông tin học viên']/following-sibling::div//label[contains(.,'Tên học viên')]/following-sibling::div/following-sibling::p[contains(@class, 'Mui-error')]");
    private final By lblNgaySinhError     = By.xpath("//h4[text()='Thông tin học viên']/following-sibling::div//input[@placeholder='DD/MM/YYYY']/../following-sibling::p[contains(@class, 'Mui-error')]");
    private final By lblEmptyTableMessage = By.xpath("//td[contains(text(), 'Không tìm thấy') or contains(text(), 'Không có dữ liệu')] | //div[contains(text(), 'Không tìm thấy')]");

    private By btnThreeDotsByText(String textVal) {
        return By.xpath("//tr[descendant::*[contains(normalize-space(.), '" + textVal + "')]]//div[@class='pointer']");
    }

    public YoeduStudentPage(WebDriver driver) {
        this.driver = driver;
    }

    // Hàm trợ giúp dùng chung: Clear và Nhập text an toàn
    private void clearAndSendKeys(WebElement element, String text) {
        element.click();
        element.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.BACK_SPACE);
        element.sendKeys(text);
    }

    public void navigateToStudentMenu() {
        try {
            driver.manage().window().maximize();
            commonFunction.waitUntilElementLocated(driver, btnMenuHamburger, intTimeOut);
            driver.findElement(btnMenuHamburger).click();

            commonFunction.waitUntilElementLocated(driver, sidebarArea, intTimeOut);
            new Actions(driver).moveToElement(driver.findElement(sidebarArea)).perform();

            commonFunction.waitUntilElementLocated(driver, menuQuanLyHocVien, intTimeOut);
            driver.findElement(menuQuanLyHocVien).click();

            commonFunction.waitUntilElementLocated(driver, menuHocVienChild, intTimeOut);
            driver.findElement(menuHocVienChild).click();
        } catch (Exception e) {
            Assert.fail("Lỗi điều hướng Sidebar mục Học viên: " + e.getMessage());
        }
    }

    public void clickAdd() {
        try {
            commonFunction.waitUntilElementLocated(driver, btnAddPlus, intTimeOut);
            driver.findElement(btnAddPlus).click();
        } catch (Exception e) {
            Assert.fail("Không nhấn được nút thêm học viên (+): " + e.getMessage());
        }
    }

    private void selectGender(WebElement dropdown, String gender) {
        try {
            if (gender != null && !gender.trim().isEmpty()) {
                dropdown.click();
                By genderOption = By.xpath("//li[@role='option' and text()='" + gender + "']");
                commonFunction.waitUntilElementLocated(driver, genderOption, intTimeOut);
                driver.findElement(genderOption).click();
            }
        } catch (Exception e) {
            Assert.fail("Lỗi khi chọn giới tính [" + gender + "]: " + e.getMessage());
        }
    }

    private void inputBirthDate(String date) {
        try {
            commonFunction.waitUntilElementLocated(driver, txtNgaySinh, intTimeOut);
            clearAndSendKeys(driver.findElement(txtNgaySinh), date);
            driver.findElement(txtNgaySinh).sendKeys(Keys.TAB);
        } catch (Exception e) {
            Assert.fail("Không thể nhập ngày sinh: " + e.getMessage());
        }
    }

    public void fillParentInfo(String sdt, String ten, String gioiTinh) {
        commonFunction.waitUntilElementLocated(driver, txtSdtPhuHuynh, intTimeOut);
        if (sdt != null) driver.findElement(txtSdtPhuHuynh).sendKeys(sdt);
        if (ten != null) driver.findElement(txtTenPhuHuynh).sendKeys(ten);
        selectGender(driver.findElement(drpGioiTinhPH), gioiTinh);
    }

    public void fillStudentInfo(String ten, String ngaySinh, String gioiTinh) {
        commonFunction.waitUntilElementLocated(driver, txtTenHocVien, intTimeOut);
        if (ten != null) driver.findElement(txtTenHocVien).sendKeys(ten);
        if (ngaySinh != null && !ngaySinh.trim().isEmpty()) inputBirthDate(ngaySinh);
        selectGender(driver.findElement(drpGioiTinhHV), gioiTinh);
    }

    public void clickSave() {
        try {
            commonFunction.waitUntilElementLocated(driver, btnLuu, intTimeOut);
            driver.findElement(btnLuu).click();
        } catch (Exception e) {
            Assert.fail("Lỗi khi nhấn nút Lưu học viên: " + e.getMessage());
        }
    }

    public void searchStudent(String keyword) {
        try {

            commonFunction.waitUntilElementLocated(driver, txtSearch, intTimeOut);
            WebElement searchInput = driver.findElement(txtSearch);
            clearAndSendKeys(searchInput, keyword);
            searchInput.sendKeys(Keys.ENTER);

            Thread.sleep(1500);

            By specificRow = By.xpath("//tr[descendant::*[contains(normalize-space(.), '" + keyword + "')]]");

            if (driver.findElements(specificRow).isEmpty()) {

            } else {
                commonFunction.waitUntilElementLocated(driver, specificRow, intTimeOut);
            }

        } catch (Exception e) {
            Assert.fail("Lỗi khi tìm kiếm học viên với từ khóa [" + keyword + "]: " + e.getMessage());
        }
    }

    public void clickActionMenuByStudentText(String studentVal, String actionType) {
        try {

            By specificRow = By.xpath("//tr[descendant::*[contains(normalize-space(.), '" + studentVal + "')]]");
            commonFunction.waitUntilElementLocated(driver, specificRow, intTimeOut);

            try { Thread.sleep(600); } catch (InterruptedException ignored) {}

            By targetThreeDots = btnThreeDotsByText(studentVal);
            driver.findElement(targetThreeDots).click();

            try { Thread.sleep(400); } catch (InterruptedException ignored) {}

            By menuAction = By.xpath("//li[contains(@class, 'MuiMenuItem-root') and (normalize-space(.)='" + actionType + "')]");
            commonFunction.waitUntilElementLocated(driver, menuAction, intTimeOut);
            driver.findElement(menuAction).click();

        } catch (Exception e) {
            Assert.fail("Lỗi khi mở menu tác vụ [" + actionType + "] của học viên [" + studentVal + "]: " + e.getMessage());
        }
    }

    public void verifyFormOpenedAndClickBack() {
        try {
            By lblChiTietHocVien = By.xpath("//h4[contains(text(), 'Chi tiết học viên')]");
            commonFunction.waitUntilElementLocated(driver, lblChiTietHocVien, intTimeOut);

            commonFunction.waitUntilElementLocated(driver, btnBackDetail, intTimeOut);
            driver.findElement(btnBackDetail).click();

            commonFunction.waitUntilElementLocated(driver, txtSearch, intTimeOut);
        } catch (Exception e) {
            Assert.fail("Lỗi khi xác nhận màn hình Chi tiết học viên và quay lại: " + e.getMessage());
        }
    }

    public void fillFormEditAndSave(String newTenHV, String newNgaySinh) {
        try {
            commonFunction.waitUntilElementLocated(driver, txtTenHocVien, intTimeOut);

            if (newTenHV != null && !newTenHV.trim().isEmpty()) {
                clearAndSendKeys(driver.findElement(txtTenHocVien), newTenHV);
            }

            if (newNgaySinh != null && !newNgaySinh.trim().isEmpty()) {
                inputBirthDate(newNgaySinh);
            }

            commonFunction.waitUntilElementLocated(driver, btnLuu, intTimeOut);
            driver.findElement(btnLuu).click();

            // Đồng bộ: Chờ cho nút Lưu ẩn đi hoặc tải xong để rút ngắn thời gian xử lý dứt điểm luồng test
            new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.invisibilityOfElementLocated(btnLuu));
        } catch (Exception e) {
            Assert.fail("Lỗi khi chỉnh sửa và Lưu thông tin học viên: " + e.getMessage());
        }
    }

    public String getToastPopupText() {
        try {
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0));
            WebElement toast = new WebDriverWait(driver, Duration.ofSeconds(2))
                    .until(ExpectedConditions.presenceOfElementLocated(lblToastPopup));
            String text = toast.getText().trim();
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
            return text;
        } catch (Exception e) {
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
            return "";
        }
    }

    public void clickRefreshList() {
        try {

            commonFunction.waitUntilElementLocated(driver, btnRefresh, intTimeOut);
            driver.findElement(btnRefresh).click();

            Thread.sleep(1000);
        } catch (Exception e) {
            Assert.fail("Lỗi khi nhấn nút Refresh danh sách học viên: " + e.getMessage());
        }
    }

    public void searchAdvanced(String referrerName) {
        try {

            commonFunction.waitUntilElementLocated(driver, btnFilterFunnel, intTimeOut);
            driver.findElement(btnFilterFunnel).click();
            Thread.sleep(500);

            commonFunction.waitUntilElementLocated(driver, txtSearchAdvanced, intTimeOut);
            WebElement advInput = driver.findElement(txtSearchAdvanced);
            advInput.click();
            advInput.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.BACK_SPACE);
            advInput.sendKeys(referrerName);

            commonFunction.waitUntilElementLocated(driver, btnSubmitSearch, intTimeOut);
            driver.findElement(btnSubmitSearch).click();

            Thread.sleep(1500);
        } catch (Exception e) {
            Assert.fail("Lỗi thao tác trên khung Tìm kiếm nâng cao: " + e.getMessage());
        }
    }

    public void clickExportExcel() {
        try {

            commonFunction.waitUntilElementLocated(driver, btnExportExcel, intTimeOut);
            driver.findElement(btnExportExcel).click();
            Thread.sleep(2000);

        } catch (Exception e) {
            Assert.fail("Lỗi không click được nút Xuất Excel: " + e.getMessage());
        }
    }

//    public void handleSaveAsPopup(String actionType) {
//        try {
//            java.awt.Robot robot = new java.awt.Robot();
//
//            if (actionType.equalsIgnoreCase("Save")) {
//
//                robot.keyPress(java.awt.event.KeyEvent.VK_ENTER);
//                robot.keyRelease(java.awt.event.KeyEvent.VK_ENTER);
//
//            } else if (actionType.equalsIgnoreCase("Cancel")) {
//
//                robot.keyPress(java.awt.event.KeyEvent.VK_ESCAPE);
//                robot.keyRelease(java.awt.event.KeyEvent.VK_ESCAPE);
//            }
//
//            Thread.sleep(2000);
//        } catch (Exception e) {
//            Assert.fail("Lỗi khi giả lập bàn phím tương tác hộp thoại Save As: " + e.getMessage());
//        }
//    }

    public String getFieldErrorText(String fieldType) {
        try {
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0));
            By locator = null;
            if (fieldType.equalsIgnoreCase("SĐT Phụ huynh")) locator = lblSdtPhuHuynhError;
            else if (fieldType.equalsIgnoreCase("Tên học viên")) locator = lblTenHocVienError;
            else if (fieldType.equalsIgnoreCase("Ngày sinh")) locator = lblNgaySinhError;

            if (locator != null && !driver.findElements(locator).isEmpty()) {
                String text = driver.findElement(locator).getText().trim();
                driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
                return text;
            }
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
        } catch (Exception e) {
            System.out.println("Không tìm thấy chữ đỏ cảnh báo lỗi của trường: " + fieldType);
        }
        return "";
    }

}
