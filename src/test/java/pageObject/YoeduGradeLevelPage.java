package pageObject;

import common.CommonFunctions;
import io.appium.java_client.AppiumDriver;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

public class YoeduGradeLevelPage {
    WebDriver driver;
    AppiumDriver appiumDriver;
    CommonFunctions commonFunction = new CommonFunctions();
    int intTimeOut = 20;

    // --- BỘ LOCATORS HOÀN CHỈNH ---
    // Sidebar & Navigation
    By btnMenuHamburger = By.xpath("//div[@class='menu-icon']");
    By sidebarArea      = By.className("sidebar-menu");
    By menuDuLieuNen    = By.xpath("//div[@class='sidebar-menu--item' and .//p[text()='Dữ liệu nền']]");
    By menuKhoiLop      = By.xpath("//div[contains(@class, 'sidebar-menu--child') and text()='Khối lớp']");

    // Action & Form
    By btnAddPlus       = By.xpath("//button[@aria-label='add']");
    By txtTen           = By.xpath("//label[contains(.,'Tên')]/following-sibling::div//input");
    By txtDienGiai      = By.xpath("//label[contains(.,'Diễn giải')]/following-sibling::div//input");
    By btnLuu           = By.xpath("//button[contains(@class, 'save-btn')]");


    public YoeduGradeLevelPage(WebDriver driver) {
        if (System.getProperty("osName").toLowerCase().contains("android")) {
            this.appiumDriver = (AppiumDriver) driver;
        } else {
            this.driver = driver;
        }
    }

    /**
     * Luồng: Mở Menu -> Hover Sidebar -> Chọn Dữ liệu nền -> Chọn Khối lớp
     */
    public void openSidebarAndNavigateToKhoiLop() {
        try {
            WebDriver currentDriver = (driver != null) ? driver : appiumDriver;
            Actions actions = new Actions(currentDriver);

            // 1. Click vào nút Hamburger Menu để đảm bảo Sidebar xuất hiện
            commonFunction.waitUntilElementLocated(currentDriver, btnMenuHamburger, intTimeOut);
            currentDriver.findElement(btnMenuHamburger).click();
            Thread.sleep(500); // Chờ sidebar trượt ra

            // 2. Kéo chuột (Hover) vào vùng Sidebar
            commonFunction.waitUntilElementLocated(currentDriver, sidebarArea, intTimeOut);
            WebElement sidebar = currentDriver.findElement(sidebarArea);
            actions.moveToElement(sidebar).perform();

            // 3. Click Menu cha "Dữ liệu nền"
            commonFunction.waitUntilElementLocated(currentDriver, menuDuLieuNen, intTimeOut);
            currentDriver.findElement(menuDuLieuNen).click();
            Thread.sleep(500); // Chờ menu con hiển thị

            // 4. Click Menu con "Khối lớp"
            commonFunction.waitUntilElementLocated(currentDriver, menuKhoiLop, intTimeOut);
            currentDriver.findElement(menuKhoiLop).click();

        } catch (Exception e) {
            Assert.fail("Lỗi trong quá trình điều hướng Sidebar: " + e.getMessage());
        }
    }

    public void clickAdd() {
        try {
            WebDriver currentDriver = (driver != null) ? driver : appiumDriver;
            commonFunction.waitUntilElementLocated(currentDriver, btnAddPlus, intTimeOut);
            currentDriver.findElement(btnAddPlus).click();
        } catch (Exception e) {
            Assert.fail("Không nhấn được nút Thêm mới (+): " + e.getMessage());
        }
    }

    public void fillFormAndSave(String ten, String dienGiai) {
        try {
            WebDriver currentDriver = (driver != null) ? driver : appiumDriver;
            commonFunction.waitUntilElementLocated(currentDriver, txtTen, intTimeOut);
            currentDriver.findElement(txtTen).sendKeys(ten);
            currentDriver.findElement(txtDienGiai).sendKeys(dienGiai);

            // Nhấn Lưu
            commonFunction.waitUntilElementLocated(currentDriver, btnLuu, intTimeOut);
            currentDriver.findElement(btnLuu).click();
        } catch (Exception e) {
            Assert.fail("Lỗi khi điền thông tin hoặc nhấn Lưu: " + e.getMessage());
        }
    }
}