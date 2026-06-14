package stepsDefinition;

import common.ContextSteps;
import io.cucumber.java.en.*;
import org.openqa.selenium.WebDriver;
import pageObject.YoeduStudentPage;

public class StudentStep {
    WebDriver driver;
    YoeduStudentPage studentPage;

    public StudentStep() throws Throwable {
        ContextSteps contextSteps = new ContextSteps();
        this.driver = contextSteps.getDriver();
        studentPage = new YoeduStudentPage(driver);
    }

    @And("Tôi thực hiện mở menu và điều hướng đến mục Học viên")
    public void navToStudent() throws InterruptedException {
        studentPage.navigateToStudentMenu();
    }

    @And("Tôi nhấn nút thêm mới học viên")
    public void add() {
        studentPage.clickAdd();
    }

    @And("Tôi nhập thông tin phụ huynh gồm SĐT {string}, Tên {string}, Giới tính {string}")
    public void parentInfo(String sdt, String ten, String gt) throws InterruptedException {
        studentPage.fillParentInfo(sdt, ten, gt);
    }

    @And("Tôi nhập thông tin học viên gồm Tên {string}, Ngày sinh {string}, Giới tính {string}")
    public void studentInfo(String ten, String ns, String gt) throws InterruptedException {
        studentPage.fillStudentInfo(ten, ns, gt);
    }

    @And("Tôi nhấn nút Lưu học viên")
    public void save() {
        studentPage.clickSave();
    }

    @Then("Hệ thống phải hiển thị thông báo lưu học viên thành công")
    public void verify() {
        System.out.println("Tạo mới học viên thành công!");
    }
}