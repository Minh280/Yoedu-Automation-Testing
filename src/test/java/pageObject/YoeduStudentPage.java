package pageObject;

import common.CommonFunctions;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;

public class YoeduStudentPage {
    WebDriver driver;
    CommonFunctions commonFunction = new CommonFunctions();
    int intTimeOut = 20;

    // --- Locators Sidebar ---
    By btnMenuHamburger = By.xpath("//div[@class='menu-icon']");
    By sidebarArea      = By.className("sidebar-menu");
    By menuQuanLyHocVien = By.xpath("//div[@class='sidebar-menu--item' and .//p[text()='Quản lý học viên']]");
    By menuHocVienChild  = By.xpath("//div[@class='sidebar-menu--child' and text()='Học viên']");

    // --- Locators Form & Action ---
    By btnAddPlus       = By.xpath("//button[@aria-label='add']");
    By txtSdtPhuHuynh   = By.xpath("//h4[text()='Thông tin phụ huynh']/following-sibling::div//label[contains(.,'Số điện thoại')]/following-sibling::div//input");
    By txtTenPhuHuynh   = By.xpath("//h4[text()='Thông tin phụ huynh']/following-sibling::div//label[contains(.,'Tên phụ huynh')]/following-sibling::div//input");
    By drpGioiTinhPH    = By.xpath("//h4[text()='Thông tin phụ huynh']/following-sibling::div//div[@role='combobox']");

    By txtTenHocVien    = By.xpath("//h4[text()='Thông tin học viên']/following-sibling::div//label[contains(.,'Tên học viên')]/following-sibling::div//input");
    By txtNgaySinh      = By.xpath("//h4[text()='Thông tin học viên']/following-sibling::div//input[@placeholder='DD/MM/YYYY']");
    By drpGioiTinhHV    = By.xpath("//h4[text()='Thông tin học viên']/following-sibling::div//div[@role='combobox']");

    By btnLuu           = By.xpath("//button[contains(@class, 'save-btn')]");

    public YoeduStudentPage(WebDriver driver) {
        this.driver = driver;
    }

    public void navigateToStudentMenu() {
        try {
            Actions actions = new Actions(driver);
            driver.manage().window().maximize();

            commonFunction.waitUntilElementLocated(driver, btnMenuHamburger, intTimeOut);
            driver.findElement(btnMenuHamburger).click();
            Thread.sleep(1000);

            commonFunction.waitUntilElementLocated(driver, sidebarArea, intTimeOut);
            WebElement sidebar = driver.findElement(sidebarArea);
            actions.moveToElement(sidebar).perform();
            Thread.sleep(500);

            commonFunction.waitUntilElementLocated(driver, menuQuanLyHocVien, intTimeOut);
            driver.findElement(menuQuanLyHocVien).click();
            Thread.sleep(500);

            commonFunction.waitUntilElementLocated(driver, menuHocVienChild, intTimeOut);
            driver.findElement(menuHocVienChild).click();
            System.out.println("Đã điều hướng tới trang Học viên thành công.");
        } catch (Exception e) {
            Assert.fail("Lỗi điều hướng Sidebar mục Học viên: " + e.getMessage());
        }
    }

    public void clickAdd() {
        commonFunction.waitUntilElementLocated(driver, btnAddPlus, intTimeOut);
        driver.findElement(btnAddPlus).click();
    }

    // --- Hàm chọn Giới tính tối ưu cho MuiSelect ---
    private void selectGender(WebElement dropdown, String gender) throws InterruptedException {
        dropdown.click(); // Click vào div role="combobox"
        Thread.sleep(1000); // Đợi Listbox xuất hiện
        By genderOption = By.xpath("//li[@role='option' and text()='" + gender + "']");
        commonFunction.waitUntilElementLocated(driver, genderOption, intTimeOut);
        driver.findElement(genderOption).click();
    }

    // --- Hàm nhập Ngày sinh xử lý Clear dữ liệu cũ ---
    private void inputBirthDate(String date) {
        try {
            commonFunction.waitUntilElementLocated(driver, txtNgaySinh, intTimeOut);
            WebElement inputDate = driver.findElement(txtNgaySinh);
            inputDate.click();
            // Xóa sạch ô nhập trước khi sendKeys
            inputDate.sendKeys(Keys.chord(Keys.CONTROL, "a"));
            inputDate.sendKeys(Keys.BACK_SPACE);
            inputDate.sendKeys(date);
            inputDate.sendKeys(Keys.TAB);
        } catch (Exception e) {
            Assert.fail("Không thể nhập ngày sinh: " + e.getMessage());
        }
    }

    public void fillParentInfo(String sdt, String ten, String gioiTinh) throws InterruptedException {
        commonFunction.waitUntilElementLocated(driver, txtSdtPhuHuynh, intTimeOut);
        driver.findElement(txtSdtPhuHuynh).sendKeys(sdt);
        driver.findElement(txtTenPhuHuynh).sendKeys(ten);
        selectGender(driver.findElement(drpGioiTinhPH), gioiTinh);
    }

    public void fillStudentInfo(String ten, String ngaySinh, String gioiTinh) throws InterruptedException {
        commonFunction.waitUntilElementLocated(driver, txtTenHocVien, intTimeOut);
        driver.findElement(txtTenHocVien).sendKeys(ten);
        inputBirthDate(ngaySinh); // Sử dụng hàm nhập ngày sinh chuyên dụng
        selectGender(driver.findElement(drpGioiTinhHV), gioiTinh);
    }

    public void clickSave() {
        try {
            commonFunction.waitUntilElementLocated(driver, btnLuu, intTimeOut);
            driver.findElement(btnLuu).click();
            System.out.println("Đã nhấn nút Lưu thành công.");

            // Dừng 5 giây để bạn kịp nhìn thông báo "Lưu thành công" hiện lên
            Thread.sleep(3000);
        } catch (Exception e) {
            System.out.println("Lỗi khi nhấn lưu: " + e.getMessage());
        }
    }
}