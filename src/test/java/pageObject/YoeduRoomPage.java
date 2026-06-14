package pageObject;

import common.CommonFunctions;
import io.appium.java_client.AppiumDriver;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

public class YoeduRoomPage {
    WebDriver driver;
    AppiumDriver appiumDriver;
    CommonFunctions commonFunction = new CommonFunctions();
    int intTimeOut = 20;

    // --- LOCATORS Sidebar ---
    By btnMenuHamburger = By.xpath("//div[@class='menu-icon']");
    By sidebarArea      = By.className("sidebar-menu");
    By menuDuLieuNen    = By.xpath("//div[@class='sidebar-menu--item' and .//p[text()='Dữ liệu nền']]");
    By menuPhongHoc     = By.xpath("//div[@class='sidebar-menu--child' and text()='Phòng học']");

    // --- LOCATORS Form & Action ---
    By btnAddPlus       = By.xpath("//button[@aria-label='add']");
    By txtTen           = By.xpath("//label[contains(.,'Tên')]/following-sibling::div//input");
    By txtDienGiai      = By.xpath("//label[contains(.,'Diễn giải')]/following-sibling::div//input");
    By txtCoSo          = By.xpath("//label[contains(.,'Cơ Sở')]/following-sibling::div//input");
    By btnLuu           = By.xpath("//button[contains(@class, 'save-btn')]");
    By imgUpload        = By.xpath("//div[contains(@class, 'dropzone')]//input[@type='file']");

    // --- LOCATORS Chữ đỏ thông báo tại ô (MUI Helper Text) ---
    By lblCoSoError     = By.xpath("//label[contains(.,'Cơ Sở')]/following-sibling::div/following-sibling::p[contains(@class, 'Mui-error')]");
    By lblTenError      = By.xpath("//label[contains(.,'Tên')]/following-sibling::div/following-sibling::p[contains(@class, 'Mui-error')]");

    // --- LOCATORS Lõi văn bản chứa chữ của Toast Snackbar ---
    By lblToastPopup    = By.xpath("//div[contains(@class, 'MuiSnackbar-root')]//div[contains(@class, 'MuiAlert-message')]");

    public YoeduRoomPage(WebDriver driver) {
        if (System.getProperty("osName").toLowerCase().contains("android")) {
            this.appiumDriver = (AppiumDriver) driver;
        } else {
            this.driver = driver;
        }
    }

    public void navigateToRoomMenu() {
        try {
            WebDriver currentDriver = (driver != null) ? driver : appiumDriver;
            currentDriver.manage().window().maximize();

            commonFunction.waitUntilElementLocated(currentDriver, btnMenuHamburger, intTimeOut);
            currentDriver.findElement(btnMenuHamburger).click();
            Thread.sleep(1000);

            Actions actions = new Actions(currentDriver);
            commonFunction.waitUntilElementLocated(currentDriver, sidebarArea, intTimeOut);
            actions.moveToElement(currentDriver.findElement(sidebarArea)).perform();

            commonFunction.waitUntilElementLocated(currentDriver, menuDuLieuNen, intTimeOut);
            currentDriver.findElement(menuDuLieuNen).click();
            Thread.sleep(500);

            commonFunction.waitUntilElementLocated(currentDriver, menuPhongHoc, intTimeOut);
            currentDriver.findElement(menuPhongHoc).click();
        } catch (Exception e) {
            Assert.fail("Lỗi điều hướng Menu Phòng học: " + e.getMessage());
        }
    }

    public void clickAdd() {
        try {
            WebDriver currentDriver = (driver != null) ? driver : appiumDriver;
            commonFunction.waitUntilElementLocated(currentDriver, btnAddPlus, intTimeOut);
            currentDriver.findElement(btnAddPlus).click();
            Thread.sleep(1000);
        } catch (Exception e) {
            Assert.fail("Không nhấn được nút (+): " + e.getMessage());
        }
    }

    public void funcSelectFileToUpload() {
        try {
            WebDriver currentDriver = (driver != null) ? driver : appiumDriver;
            org.openqa.selenium.support.ui.WebDriverWait wait = new org.openqa.selenium.support.ui.WebDriverWait(currentDriver, java.time.Duration.ofSeconds(intTimeOut));
            wait.until(org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated(imgUpload));

            WebElement uploadElement = currentDriver.findElement(imgUpload);
            String strImageFile = System.getProperty("user.dir").trim() + "/image/zootopia.jpg";
            uploadElement.sendKeys(strImageFile);
            Thread.sleep(2000);
        } catch (Exception e) {
            Assert.fail("Lỗi upload hình ảnh: " + e.getMessage());
        }
    }

    // --- Hàm lấy chữ đỏ tại ô nhập liệu chỉ định ---
    public String getFieldErrorText(String fieldType) {
        WebDriver currentDriver = (driver != null) ? driver : appiumDriver;
        try {
            if (fieldType.equalsIgnoreCase("Cơ sở") && currentDriver.findElements(lblCoSoError).size() > 0) {
                return currentDriver.findElement(lblCoSoError).getText().trim();
            }
            if (fieldType.equalsIgnoreCase("Tên") && currentDriver.findElements(lblTenError).size() > 0) {
                return currentDriver.findElement(lblTenError).getText().trim();
            }
        } catch (Exception e) {
            System.out.println("Không đọc được chữ đỏ tại ô: " + fieldType);
        }
        return "";
    }

    // --- Hàm lấy text từ lõi Toast Snackbar ---
    public String getToastPopupText() {
        WebDriver currentDriver = (driver != null) ? driver : appiumDriver;
        try {
            if (currentDriver.findElements(lblToastPopup).size() > 0) {
                return currentDriver.findElement(lblToastPopup).getText().trim();
            }
        } catch (Exception e) {
            System.out.println("Không đọc được text ẩn của Toast Popup.");
        }
        return "";
    }

    // --- Hàm xử lý điền Form ---
    public void fillFormAndSave(String coSo, String tenPhong, String dienGiai) {
        try {
            WebDriver currentDriver = (driver != null) ? driver : appiumDriver;

            // Ô chọn Cơ Sở
            commonFunction.waitUntilElementLocated(currentDriver, txtCoSo, intTimeOut);
            WebElement inputCoSo = currentDriver.findElement(txtCoSo);
            if (coSo != null && !coSo.trim().isEmpty()) {
                inputCoSo.clear();
                inputCoSo.sendKeys(coSo);
                Thread.sleep(1000);
                inputCoSo.sendKeys(org.openqa.selenium.Keys.ARROW_DOWN);
                inputCoSo.sendKeys(org.openqa.selenium.Keys.ENTER);
            } else {
                inputCoSo.click();
                inputCoSo.sendKeys(org.openqa.selenium.Keys.TAB);
            }

            // Ô điền Tên Phòng
            commonFunction.waitUntilElementLocated(currentDriver, txtTen, intTimeOut);
            WebElement inputTen = currentDriver.findElement(txtTen);
            if (tenPhong != null && !tenPhong.trim().isEmpty()) {
                inputTen.sendKeys(tenPhong);
            } else {
                inputTen.click();
                inputTen.sendKeys(org.openqa.selenium.Keys.TAB);
            }

            // Điền Diễn giải (nếu có)
            if (dienGiai != null && !dienGiai.trim().isEmpty()) {
                currentDriver.findElement(txtDienGiai).sendKeys(dienGiai);
            }

            // Thực hiện click nút Lưu để kích hoạt đồng thời 2 tầng Validation
            commonFunction.waitUntilElementLocated(currentDriver, btnLuu, intTimeOut);
            currentDriver.findElement(btnLuu).click();
            System.out.println("Đã bấm Lưu form tạo mới phòng học.");

        } catch (Exception e) {
            Assert.fail("Lỗi thao tác form Phòng học: " + e.getMessage());
        }
    }
}