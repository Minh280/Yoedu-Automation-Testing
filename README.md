# Yoedu Automation Testing Framework

Dự án kiểm thử tự động (Automation Testing) dành cho hệ thống **Yoedu**, được phát triển dựa trên ngôn ngữ lập trình **Java** kết hợp với mô hình **BDD Cucumber**, **Selenium WebDriver**, **Appium** và **RestAssured**.

---

## 📌 Công Nghệ & Thư Viện Sử Dụng

*   **Ngôn ngữ**: Java 17
*   **Quản lý dự án**: Maven
*   **Framework kiểm thử**: TestNG & JUnit
*   **Mô hình kiểm thử**: BDD Cucumber (Behavior-Driven Development)
*   **Kiểm thử Giao diện (UI Testing)**:
    *   **Web**: Selenium WebDriver
    *   **Mobile (Android Webview)**: Appium Java Client
    *   **Desktop App**: Winium WebDriver
    *   **Alternative**: Playwright
*   **Kiểm thử API**: Rest-Assured (với JSON Schema Validator & Hamcrest)
*   **Báo cáo**: Maven Cucumber Reporting
*   **Quản lý Driver**: WebDriverManager

---

## 📂 Cấu Trúc Thư Mục Dự Án

```text
Yoedu_Test/
│
├── .idea/                      # Cấu hình dự án trên IntelliJ IDEA
├── driver/                     # Thư mục chứa các file driver thực thi (ChromeDriver, GeckoDriver...)
├── image/                      # Thư mục lưu trữ hình ảnh/ảnh chụp màn hình kiểm thử
├── target/                     # Thư mục build của Maven & các báo cáo kiểm thử sinh ra sau khi chạy
│
├── pom.xml                     # Quản lý thư viện và cấu hình build dự án (Maven POM)
├── testng.xml                  # Cấu hình Suite Runner cho TestNG (chạy song song)
├── Yoedu - Test Cases.xlsx     # File Excel tài liệu các kịch bản kiểm thử (Test Cases)
│
└── src/
    ├── main/
    │   └── resources/          # Cấu hình hệ thống & môi trường
    │       ├── application.properties    # Cấu hình Browser, OS, Base URL, API URL...
    │       ├── data.properties           # Dữ liệu phục vụ kiểm thử
    │       ├── apiURL.json               # Danh sách các endpoint API
    │       └── errorMessage.properties   # Các thông báo lỗi mong đợi phục vụ assertion
    │
    └── test/
        ├── java/               # Mã nguồn các kịch bản kiểm thử
        │   ├── common/         # Khởi tạo Driver, Hooks và các hàm dùng chung
        │   │   ├── ThreadWebDriver.java  # Quản lý Thread-safe WebDriver cho chạy song song
        │   │   ├── RunParallel.java      # Runner để thực thi các kịch bản song song với TestNG
        │   │   ├── CommonFunctions.java  # Các hàm thao tác UI dùng chung (Click, SendKeys, Wait...)
        │   │   ├── CommonSteps.java      # Định nghĩa các step BDD dùng chung
        │   │   └── ContextSteps.java     # Quản lý chia sẻ dữ liệu giữa các Steps (Scenario Context)
        │   │
        │   ├── pageObject/     # Thiết kế theo mô hình Page Object Model (POM)
        │   │   ├── YoeduLoginPage.java
        │   │   ├── YoeduGradeLevelPage.java
        │   │   ├── YoeduRoomPage.java
        │   │   └── YoeduStudentPage.java
        │   │
        │   └── stepsDefinition/# Định nghĩa các bước thực thi kịch bản (Step Definitions)
        │       ├── LoginStep.java
        │       ├── GradeLevelStep.java
        │       ├── ClassRoomStep.java
        │       ├── StudentStep.java
        │       ├── API_Grades_Testing.java      # Kịch bản kiểm thử API Grades
        │       ├── API_ClassRoom_Testing.java   # Kịch bản kiểm thử API ClassRoom
        │       └── API_Student_Testing.java     # Kịch bản kiểm thử API Student
        │
        └── resources/
            └── feature/        # Định nghĩa các kịch bản kiểm thử BDD (Gherkin syntax)
                ├── Yoedu_HappyCase.feature      # Kịch bản UI thành công
                ├── Yoedu_UnhappyCase.feature    # Kịch bản UI không thành công/lỗi
                └── API_RestAssured.feature      # Kịch bản kiểm thử API
```

---

## ⚙️ Hướng Dẫn Cấu Hình Trước Khi Chạy

Tất cả các cấu hình quan trọng đều nằm tại thư mục [src/main/resources/](file:///c:/Yoedu_Test/Yoedu_Test/src/main/resources):

1.  **Chọn trình duyệt & Môi trường**: Chỉnh sửa file `application.properties`
    *   `browserName`: Trình duyệt chạy test (`chrome`, `firefox`, `edge`...).
    *   `osName`: Hệ điều hành (`Windows`, `Mac`, `Android`).
    *   `baseUrl`: Địa chỉ trang web Yoedu cần test (`https://management.yoedu.vn/`).
    *   `apiUrl`: Địa chỉ API Gateway (`https://api-gateway-uat2.dragoncapital.com.vn`).
2.  **Thông tin đăng nhập & Dữ liệu test**: Xem và chỉnh sửa trong `data.properties` hoặc `apiURL.json`.

---

## 🚀 Hướng Dẫn Chạy Kiểm Thử

Bạn có thể chạy kiểm thử bằng nhiều cách:

### 1. Chạy thông qua IntelliJ / Eclipse IDE
*   Chuột phải vào file [testng.xml](file:///c:/Yoedu_Test/Yoedu_Test/testng.xml) -> Chọn **Run '...testng.xml'**.
*   Hoặc chuột phải vào class runner [RunParallel.java](file:///c:/Yoedu_Test/Yoedu_Test/src/test/java/common/RunParallel.java) -> Chọn **Run**.

### 2. Chạy thông qua Command Line (Maven CLI)
Mở terminal tại thư mục gốc của dự án và chạy câu lệnh sau:

```bash
mvn clean test
```

---

## 📊 Xem Báo Cáo Kiểm Thử (Test Reports)

Sau khi chạy xong kiểm thử, báo cáo Cucumber HTML sinh động sẽ được tạo ra tại thư mục:
*   `target/cucumber-html-reports/overview-features.html` (Mở file này bằng trình duyệt để xem trực quan).