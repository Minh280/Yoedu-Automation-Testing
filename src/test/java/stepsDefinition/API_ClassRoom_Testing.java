package stepsDefinition;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.junit.Assert;
import io.restassured.http.ContentType;

import java.util.LinkedHashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class API_ClassRoom_Testing {

    private Response response;

    @When("I execute the login API with valid credentials")
    public void i_execute_the_login_api_with_valid_credentials() {
        try {
            RestAssured.baseURI = System.getProperty("apiUrl", "https://management.yoedu.vn");

            String loginUrl = "https://apigateway.yoot.vn/yoedu/management/api/users/login";

            JSONObject requestBody = new JSONObject();
            requestBody.put("userName", "admin");
            requestBody.put("password", "admin@123");

            // Thực thi gửi request POST và BỎ QUA kiểm tra SSL Certificate
            response = given()
                    .relaxedHTTPSValidation() // <-- THÊM DÒNG NÀY VÀO ĐÂY
                    .header("Content-Type", "application/json")
                    .body(requestBody.toString())
                    .when()
                    .post(loginUrl)
                    .then()
                    .extract()
                    .response();

        } catch (Exception e) {
            Assert.fail("Lỗi khi thực thi API Login: " + e.getMessage());
        }
    }

    @Then("I verify the login response is successful")
    public void i_verify_the_login_response_is_successful() {
        try {
            assertEquals(200, response.statusCode());

            JSONObject jsonResponse = new JSONObject(response.getBody().asString());
            assertTrue("Đăng nhập thất bại, thuộc tính 'succeeded' là false!", jsonResponse.getBoolean("succeeded"));

            Assert.assertFalse("Trường 'data' trong response bị null", jsonResponse.isNull("data"));
            JSONObject dataObject = jsonResponse.getJSONObject("data");

            // Tự động kiểm tra và lưu token để các API test phía sau (tạo phòng, sửa phòng) sử dụng
            if (dataObject.has("token")) {
                String token = dataObject.getString("token");
                System.setProperty("access_token", token);
                System.out.println(">>> Đã lưu Token thành công vào hệ thống!");
            } else if (dataObject.has("accessToken")) {
                String accessToken = dataObject.getString("accessToken");
                System.setProperty("access_token", accessToken);
                System.out.println(">>> Đã lưu Access Token thành công!");
            }

            System.out.println(">>> Đăng nhập thành công! Xin chào: " + dataObject.optString("name")
                    + " (Email: " + dataObject.optString("email") + ")");

        } catch (Exception e) {
            Assert.fail("Lỗi kiểm tra JSON Response: " + e.getMessage());
        }
    }
    // ==========================================
    // CHỨC NĂNG: TÌM KIẾM PHÒNG HỌC (PAGING)
    // ==========================================

    @When("I execute the search classroom API with pagination information")
    public void i_execute_the_search_classroom_api_with_pagination_information() {
        try {
            // KHẮC PHỤC LỖI 404: Sử dụng trực tiếp URL tuyệt đối giống bước Login để tránh bị ghi đè domain
            String searchUrl = "https://apigateway.yoot.vn/yoedu/management/api/class-rooms/paging";

            String token = System.getProperty("access_token");
            if (token == null || token.isEmpty()) {
                Assert.fail("Lỗi: Không tìm thấy Access Token!");
            }

            // Cấu trúc Body JSON lồng nhau chuẩn theo Yoedu API
            JSONObject paginationObj = new JSONObject();
            paginationObj.put("pageSize", 30);
            paginationObj.put("pageNumber", 1);
            paginationObj.put("isPaging", true);

            JSONObject sortObj = new JSONObject();
            sortObj.put("predicate", "AlterDate");
            sortObj.put("reverse", true);

            org.json.JSONArray sortArray = new org.json.JSONArray();
            sortArray.put(sortObj);

            org.json.JSONArray groupFiltersArray = new org.json.JSONArray();
            org.json.JSONArray includesArray = new org.json.JSONArray();

            JSONObject requestBody = new JSONObject();
            requestBody.put("pagination", paginationObj);
            requestBody.put("groupFilters", groupFiltersArray);
            requestBody.put("includes", includesArray);
            requestBody.put("sort", sortArray);

            // Thực thi gửi request POST bằng URL tuyệt đối
            response = given()
                    .relaxedHTTPSValidation()
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + token)
                    .body(requestBody.toString())
                    .when()
                    .post(searchUrl)
                    .then()
                    .extract()
                    .response();

        } catch (Exception e) {
            Assert.fail("Lỗi khi thực thi API Tìm kiếm phòng học: " + e.getMessage());
        }
    }

    @Then("I verify the classroom list response returns successfully")
    public void i_verify_the_classroom_list_response_returns_successfully() {
        try {
            assertEquals(200, response.statusCode());
            JSONObject jsonResponse = new JSONObject(response.getBody().asString());
            assertTrue("Tìm kiếm phòng học thất bại!", jsonResponse.getBoolean("succeeded"));

            Assert.assertFalse("Trường 'data' bị null", jsonResponse.isNull("data"));
            org.json.JSONArray classroomList = jsonResponse.getJSONArray("data");

            // TỰ ĐỘNG HÓA DYNAMIC ID: Lấy id của phòng học đầu tiên trong mảng kết quả
            if (classroomList.length() > 0) {
                JSONObject firstClassroom = classroomList.getJSONObject(0);
                String dynamicId = firstClassroom.get("id").toString();

                // Lưu lại ID này để bước Chỉnh sửa phía sau bóc ra dùng luôn
                System.setProperty("edit_classroom_id", dynamicId);
                System.out.println(">>> Đã bắt được ID phòng học động để chỉnh sửa: " + dynamicId);
            } else {
                Assert.fail("Không tìm thấy phòng học nào trong danh sách trả về để test chỉnh sửa!");
            }

        } catch (Exception e) {
            Assert.fail("Lỗi kiểm tra dữ liệu tìm kiếm phòng học: " + e.getMessage());
        }
    }

    // ==========================================
    // CHỨC NĂNG: XEM CHI TIẾT PHÒNG HỌC (GET)
    // ==========================================

    @When("I execute the view classroom details API for the retrieved classroom id")
    public void i_execute_the_view_classroom_details_api_for_the_retrieved_classroom_id() {
        try {
            // Lấy ID động từ bộ nhớ hệ thống do bước Tìm kiếm (Paging) lưu lại
            String dynamicId = System.getProperty("edit_classroom_id");
            if (dynamicId == null || dynamicId.isEmpty()) {
                Assert.fail("Lỗi: Không tìm thấy ID phòng học động! Hãy đảm bảo step Tìm kiếm đã chạy trước.");
            }

            // Đường dẫn API xem chi tiết theo ID phòng
            String viewUrl = "https://apigateway.yoot.vn/yoedu/management/api/class-rooms/" + dynamicId;
            System.out.println(">>> Đường dẫn API Xem chi tiết đang gọi: " + viewUrl);

            String token = System.getProperty("access_token");

            // Thực thi gửi request GET (Không truyền kèm Body Request)
            response = given()
                    .relaxedHTTPSValidation()
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + token)
                    .when()
                    .get(viewUrl)
                    .then()
                    .extract()
                    .response();

        } catch (Exception e) {
            Assert.fail("Lỗi khi thực thi API Xem chi tiết phòng học: " + e.getMessage());
        }
    }

    @Then("I verify the classroom details response returns correctly")
    public void i_verify_the_classroom_details_response_returns_correctly() {
        try {
            // 1. Kiểm tra Status Code phải là 200 OK
            assertEquals(200, response.statusCode());

            // 2. Parse Response Body ra JSON Object
            JSONObject jsonResponse = new JSONObject(response.getBody().asString());

            // 3. Kiểm tra thuộc tính "succeeded" phải là true
            assertTrue("Xem chi tiết phòng học thất bại, 'succeeded' là false!", jsonResponse.getBoolean("succeeded"));

            // 4. Bóc tách dữ liệu từ object "data" để kiểm tra cấu trúc thông tin trả về
            Assert.assertFalse("Trường 'data' trong response bị null", jsonResponse.isNull("data"));
            JSONObject dataObject = jsonResponse.getJSONObject("data");

            System.out.println(">>> THÔNG TIN CHI TIẾT PHÒNG HỌC:");
            System.out.println("    - ID phòng: " + dataObject.get("id").toString());
            System.out.println("    - Mã phòng (Code): " + dataObject.optString("code", "N/A"));
            System.out.println("    - Tên phòng (Name): " + dataObject.optString("name", "N/A"));
            System.out.println("    - Sức chứa (Capacity): " + dataObject.optInt("capacity", 0));
            System.out.println("    - Thuộc chi nhánh (BranchID): " + dataObject.optInt("branchId", 0));
            System.out.println("    - Trạng thái hoạt động (IsActive): " + dataObject.optBoolean("isActive"));

        } catch (Exception e) {
            Assert.fail("Lỗi kiểm tra dữ liệu JSON Xem chi tiết phòng học: " + e.getMessage());
        }
    }
    // ==========================================
    // CHỨC NĂNG: CHỈNH SỬA PHÒNG HỌC (PUT)
    // ==========================================
    @When("I execute the edit classroom API for the retrieved classroom id")
    public void i_execute_the_edit_classroom_api_for_the_retrieved_classroom_id() {
        try {
            String dynamicId = System.getProperty("edit_classroom_id");
            String token = System.getProperty("access_token");

            if (dynamicId == null || dynamicId.isEmpty()) {
                Assert.fail("Lỗi: Không tìm thấy ID phòng học động!");
            }

            String editUrl = "https://apigateway.yoot.vn/yoedu/management/api/class-rooms";
            System.out.println(">>> API Edit Classroom đang gọi (PUT): " + editUrl);

            // 1. Tạo Map chứa các tham số dạng Form (Giống hệt tab Params trong ảnh Postman của bạn)
            Map<String, Object> formFields = new LinkedHashMap<>();
            formFields.put("id", Integer.parseInt(dynamicId));
            formFields.put("code", "PH500");
            formFields.put("name", "Phòng Lý Thuyết 500 - Updated");
            formFields.put("description", "Phòng học máy chiếu khu A");
            formFields.put("note", "");
            formFields.put("photo", "");
            formFields.put("branchId", 1);
            formFields.put("capacity", 10);
            formFields.put("isActive", true);

            // 2. Thực hiện gọi API bằng phương thức PUT với cấu hình .formParams
            response = given()
                    .relaxedHTTPSValidation()
                    // Thay contentType thành URLENC hoặc FORM-DATA tùy thuộc cấu hình hệ thống
                    .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                    .header("Authorization", "Bearer " + token)
                    .formParams(formFields) // Kéo toàn bộ Map tham số vào dạng Form
                    .when()
                    .put(editUrl)
                    .then()
                    .extract()
                    .response();

        } catch (Exception e) {
            Assert.fail("Lỗi khi thực thi API PUT Chỉnh sửa phòng học: " + e.getMessage());
        }
    }

    @Then("I verify the classroom edit response is successful")
    public void i_verify_the_classroom_edit_response_is_successful() {
        try {
            // Xác nhận cập nhật thành công (200 OK)
            assertEquals(200, response.statusCode());

            JSONObject jsonResponse = new JSONObject(response.getBody().asString());
            assertTrue("Chỉnh sửa phòng học thất bại, 'succeeded' là false!", jsonResponse.getBoolean("succeeded"));

            // Bóc tách dữ liệu từ object "data" (Key chữ thường theo đúng ảnh Postman)
            JSONObject dataObject = jsonResponse.getJSONObject("data");

            System.out.println(">>> CHỈNH SỬA PHÒNG HỌC THÀNH CÔNG THỰC SỰ!");
            System.out.println("    - ID Phòng học: " + dataObject.optInt("id"));
            System.out.println("    - Mã phòng mới: " + dataObject.optString("code"));
            System.out.println("    - Tên phòng mới: " + dataObject.optString("name"));
            System.out.println("    - Sức chứa sau sửa: " + dataObject.optInt("capacity"));

        } catch (Exception e) {
            Assert.fail("Lỗi kiểm tra dữ liệu JSON Chỉnh sửa phòng học: " + e.getMessage());
        }
    }
}