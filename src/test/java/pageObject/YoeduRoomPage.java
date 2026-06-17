package pageObject;

import common.CommonFunctions;
import io.appium.java_client.AppiumDriver;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class YoeduRoomPage {
    private WebDriver driver;
    private final CommonFunctions commonFunction = new CommonFunctions();
    private final int intTimeOut = 20;

    // --- LOCATORS: Điều hướng Sidebar ---
    By btnMenuHamburger = By.xpath("//div[@class='menu-icon']");
    By sidebarArea      = By.className("sidebar-menu");
    By menuDuLieuNen    = By.xpath("//div[@class='sidebar-menu--item' and .//p[text()='Dữ liệu nền']]");
    By menuPhongHoc     = By.xpath("//div[@class='sidebar-menu--child' and text()='Phòng học']");

    // --- LOCATORS: Form nhập liệu & Hành động ---
    By btnAddPlus    = By.xpath("//button[@aria-label='add']");
    By txtTen        = By.xpath("//label[contains(.,'Tên')]/following-sibling::div//input");
    By txtDienGiai   = By.xpath("//label[contains(.,'Diễn giải')]/following-sibling::div//input");
    By txtCoSo       = By.xpath("//label[contains(.,'Cơ Sở')]/following-sibling::div//input");
    By btnLuu        = By.xpath("//button[contains(@class, 'save-btn')]");
    By btnBackDetail = By.xpath("//button[.//shadow/*[local-name()='svg' and @data-testid='KeyboardBackspaceIcon'] or ./*[local-name()='svg' and @data-testid='KeyboardBackspaceIcon']]");

    // --- LOCATORS: Thông báo & Tìm kiếm ---
    By lblCoSoError  = By.xpath("//label[contains(.,'Cơ Sở')]/following-sibling::div/following-sibling::p[contains(@class, 'Mui-error')]");
    By lblTenError   = By.xpath("//label[contains(.,'Tên')]/following-sibling::div/following-sibling::p[contains(@class, 'Mui-error')]");
    By lblToastPopup = By.xpath("//div[contains(@class, 'MuiSnackbar-root')]//div[contains(@class, 'MuiAlert-message')]");
    By txtSearch     = By.xpath("//input[@placeholder='Tìm kiếm ...']");

    // --- LOCATORS: Popup & Menu tác vụ ---
    By btnConfirmDelete = By.xpath("//div[contains(@class, 'MuiDialogActions-root')]//button[contains(@class, 'MuiButton-containedError') or text()='XÓA']");

    By btnThreeDotsByText(String textVal) {
        return By.xpath("//tr[.//td//*[contains(text(),'" + textVal + "')] or .//td[contains(text(),'" + textVal + "')]]//div[@class='pointer']");
    }

    public YoeduRoomPage(WebDriver driver) {
        if (System.getProperty("osName").toLowerCase().contains("android")) {
            this.driver = (AppiumDriver) driver;
        } else {
            this.driver = driver;
        }
    }

    public void navigateToRoomMenu() {
        try {
            driver.manage().window().maximize();
            commonFunction.waitUntilElementLocated(driver, btnMenuHamburger, intTimeOut);
            driver.findElement(btnMenuHamburger).click();

            commonFunction.waitUntilElementLocated(driver, sidebarArea, intTimeOut);
            new Actions(driver).moveToElement(driver.findElement(sidebarArea)).perform();

            commonFunction.waitUntilElementLocated(driver, menuDuLieuNen, intTimeOut);
            driver.findElement(menuDuLieuNen).click();

            commonFunction.waitUntilElementLocated(driver, menuPhongHoc, intTimeOut);
            driver.findElement(menuPhongHoc).click();
        } catch (Exception e) {
            Assert.fail("Lỗi điều hướng Menu Phòng học: " + e.getMessage());
        }
    }

    public void clickAdd() {
        try {
            commonFunction.waitUntilElementLocated(driver, btnAddPlus, intTimeOut);
            driver.findElement(btnAddPlus).click();
        } catch (Exception e) {
            Assert.fail("Không nhấn được nút (+): " + e.getMessage());
        }
    }

    public void fillFormAndSave(String coSo, String tenPhong, String dienGiai) {
        try {
            // Xử lý ô chọn Cơ Sở (Autocomplete/Dropdown)
            commonFunction.waitUntilElementLocated(driver, txtCoSo, intTimeOut);
            WebElement inputCoSo = driver.findElement(txtCoSo);
            if (coSo != null && !coSo.trim().isEmpty()) {
                inputCoSo.clear();
                inputCoSo.sendKeys(coSo, Keys.ARROW_DOWN, Keys.ENTER);
            } else {
                inputCoSo.click();
                inputCoSo.sendKeys(Keys.TAB);
            }

            // Xử lý ô nhập Tên
            commonFunction.waitUntilElementLocated(driver, txtTen, intTimeOut);
            WebElement inputTen = driver.findElement(txtTen);
            if (tenPhong != null && !tenPhong.trim().isEmpty()) {
                inputTen.sendKeys(tenPhong);
            } else {
                inputTen.click();
                inputTen.sendKeys(Keys.TAB);
            }

            // Xử lý ô nhập Diễn giải
            if (dienGiai != null && !dienGiai.trim().isEmpty()) {
                driver.findElement(txtDienGiai).sendKeys(dienGiai);
            }

            commonFunction.waitUntilElementLocated(driver, btnLuu, intTimeOut);
            driver.findElement(btnLuu).click();
        } catch (Exception e) {
            Assert.fail("Lỗi thao tác form thêm mới Phòng học: " + e.getMessage());
        }
    }

    public void searchRoom(String keyword) {
        try {
            commonFunction.waitUntilElementLocated(driver, txtSearch, intTimeOut);
            WebElement searchInput = driver.findElement(txtSearch);
            searchInput.click();
            searchInput.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.BACK_SPACE);
            searchInput.sendKeys(keyword, Keys.ENTER);
            Thread.sleep(1500); // Giữ lại một khoảng ngắn để bảng Table kịp render data mới
        } catch (Exception e) {
            Assert.fail("Lỗi khi thao tác tìm kiếm phòng học: " + e.getMessage());
        }
    }

    public boolean verifyRoomExistsInList(String roomCode) {
        try {
            By codeLocator = By.xpath("//a[contains(text(),'" + roomCode + "')] | //*[text()='" + roomCode + "']");
            return new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.presenceOfElementLocated(codeLocator)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getFieldErrorText(String fieldType) {
        try {
            By locator = fieldType.equalsIgnoreCase("Cơ sở") ? lblCoSoError : (fieldType.equalsIgnoreCase("Tên") ? lblTenError : null);
            if (locator != null && !driver.findElements(locator).isEmpty()) {
                return driver.findElement(locator).getText().trim();
            }
        } catch (Exception e) {
            System.out.println("Không đọc được chữ đỏ thông báo tại ô: " + fieldType);
        }
        return "";
    }

    public String getToastPopupText() {
        try {
            return !driver.findElements(lblToastPopup).isEmpty() ? driver.findElement(lblToastPopup).getText().trim() : "";
        } catch (Exception e) {
            System.out.println("Không đọc được văn bản hiển thị của Toast Popup.");
        }
        return "";
    }

    public void clickActionMenuByText(String textVal, String actionType) {
        try {
            By targetThreeDots = btnThreeDotsByText(textVal);
            commonFunction.waitUntilElementLocated(driver, targetThreeDots, intTimeOut);
            driver.findElement(targetThreeDots).click();

            By menuAction = By.xpath("//li[contains(@class, 'MuiMenuItem-root') and (text()='" + actionType + "' or contains(., '" + actionType + "'))]");
            commonFunction.waitUntilElementLocated(driver, menuAction, intTimeOut);
            driver.findElement(menuAction).click();
        } catch (Exception e) {
            Assert.fail("Lỗi khi mở menu tác vụ [" + actionType + "]: " + e.getMessage());
        }
    }

    public void verifyFormOpenedAndClickBack() {
        try {
            commonFunction.waitUntilElementLocated(driver, txtTen, intTimeOut);
            commonFunction.waitUntilElementLocated(driver, btnBackDetail, intTimeOut);
            driver.findElement(btnBackDetail).click();
            commonFunction.waitUntilElementLocated(driver, txtSearch, intTimeOut);
        } catch (Exception e) {
            Assert.fail("Lỗi khi thực hiện hành động Back về danh sách: " + e.getMessage());
        }
    }

    public void fillFormEditAndSave(String newCoSo, String newTenPhong, String newDienGiai) {
        try {
            commonFunction.waitUntilElementLocated(driver, txtTen, intTimeOut);

            if (newCoSo != null) {
                WebElement inputCoSo = driver.findElement(txtCoSo);
                inputCoSo.click();
                inputCoSo.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.BACK_SPACE);
                if (!newCoSo.trim().isEmpty()) {
                    inputCoSo.sendKeys(newCoSo, Keys.ARROW_DOWN, Keys.ENTER);
                } else {
                    inputCoSo.sendKeys(Keys.TAB);
                }
            }

            if (newTenPhong != null) {
                WebElement inputTen = driver.findElement(txtTen);
                inputTen.click();
                inputTen.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.BACK_SPACE);
                if (!newTenPhong.trim().isEmpty()) {
                    inputTen.sendKeys(newTenPhong);
                } else {
                    inputTen.sendKeys(Keys.TAB);
                }
            }

            if (newDienGiai != null && !newDienGiai.trim().isEmpty()) {
                WebElement inputDienGiai = driver.findElement(txtDienGiai);
                inputDienGiai.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.BACK_SPACE, newDienGiai);
            }

            commonFunction.waitUntilElementLocated(driver, btnLuu, intTimeOut);
            driver.findElement(btnLuu).click();
        } catch (Exception e) {
            Assert.fail("Lỗi khi nhập liệu form Sửa và Lưu: " + e.getMessage());
        }
    }

    public void confirmDelete() {
        try {
            commonFunction.waitUntilElementLocated(driver, btnConfirmDelete, intTimeOut);
            driver.findElement(btnConfirmDelete).click();
        } catch (Exception e) {
            Assert.fail("Lỗi khi click nút xác nhận Xóa trên popup dialog: " + e.getMessage());
        }
    }
}