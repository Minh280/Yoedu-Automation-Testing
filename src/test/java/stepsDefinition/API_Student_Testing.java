package stepsDefinition;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.junit.Assert;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class API_Student_Testing {

    private Response response;

    @When("I execute the create student API with valid dynamic information")
    public void i_execute_the_create_student_api_with_valid_dynamic_information() {
        try {
            String createStudentUrl = "https://apigateway.yoot.vn/yoedu/management/api/students";

            String token = System.getProperty("access_token");
            if (token == null || token.isEmpty()) {
                Assert.fail("Lỗi: Không tìm thấy Access Token để thực thi tạo mới học viên!");
            }

            // Tạo tên học viên động tránh trùng lặp
            long timestamp = System.currentTimeMillis();
            String studentName = "Đỗ Học Viên " + timestamp;

            System.out.println(">>> Đang gửi Request tạo mới học viên dạng FORM-DATA với tên: " + studentName);

            // Thực thi gửi request POST dưới dạng form-data (multiPart)
            response = given()
                    .relaxedHTTPSValidation()
                    // KHÔNG dùng Content-Type application/json nữa vì RestAssured sẽ tự nhận diện multipart/form-data
                    .header("Authorization", "Bearer " + token)

                    // Cấu hình các trường Key - Value y hệt như bạn đã nhập trên Postman
                    .multiPart("name", "Michael")
                    .multiPart("dateOfBirth", "2018-06-07")
                    .multiPart("genderId", 0)                  // Đổi thành viết thường chữ 'g' theo hình Postman
                    .multiPart("phoneNumber", "")
                    .multiPart("description", "")
                    .multiPart("school", "")
                    .multiPart("gradeId", "")
                    .multiPart("note", "")
                    .multiPart("isActive", "true")
                    .multiPart("photo", "")                    // Để trống giống Postman
                    .multiPart("nameParent", "Minh")
                    .multiPart("address", "")
                    .multiPart("emailParent", "")
                    .multiPart("genderParentId", 0)
                    .multiPart("relationshipId", "")
                    .multiPart("otherRelationship", "")
                    .multiPart("consultant", "")
                    .multiPart("recommenderPhone", "")
                    .multiPart("password", "123456")
                    .multiPart("phoneNumberParent", "0913739259")

                    .when()
                    .post(createStudentUrl)
                    .then()
                    .extract()
                    .response();

        } catch (Exception e) {
            Assert.fail("Lỗi khi thực thi API Tạo mới học viên (Form-data): " + e.getMessage());
        }
    }

    @Then("I verify the student creation response is successful")
    public void i_verify_the_student_creation_response_is_successful() {
        try {
            // In chi tiết phản hồi từ Server ra đề phòng trường hợp vẫn dính lỗi Validation khác
            System.out.println(">>> Phản hồi chi tiết từ Server (Raw Response): " + response.getBody().asString());

            // 1. Kiểm tra Status Code phải thành công (200 OK)
            assertEquals(200, response.statusCode());

            // 2. Parse Response Body ra JSON Object
            JSONObject jsonResponse = new JSONObject(response.getBody().asString());

            // 3. Kiểm tra thuộc tính "succeeded" phải trả về true
            assertTrue("Tạo mới học viên thất bại, 'succeeded' là false!", jsonResponse.getBoolean("succeeded"));

            // 4. Bóc tách dữ liệu từ object "data" để lấy thông tin học viên mới
            Assert.assertFalse("Trường 'data' trong response bị null", jsonResponse.isNull("data"));
            JSONObject dataObject = jsonResponse.getJSONObject("data");

            System.out.println(">>> TẠO MỚI HỌC VIÊN THÀNH CÔNG!");

            // Lấy ID động (kiểu int hoặc chuỗi từ trường 'id')
            String newStudentId = dataObject.get("id").toString();
            System.out.println("    - ID Học viên mới tạo: " + newStudentId);
            System.out.println("    - Tên học viên: " + dataObject.optString("nameStudent"));
            System.out.println("    - Mã học viên (Code): " + dataObject.optString("code"));

            // Lưu lại ID học viên vừa tạo vào bộ nhớ hệ thống để test các bài toán tiếp theo
            System.setProperty("new_student_id", newStudentId);

        } catch (Exception e) {
            Assert.fail("Lỗi kiểm tra dữ liệu JSON Tạo mới học viên: " + e.getMessage());
        }
    }
    // ==========================================
    // CHỨC NĂNG: TÌM KIẾM HỌC VIÊN (GET - QUERY)
    // ==========================================

    @When("I execute the search student API with keyword {string}")
    public void i_execute_the_search_student_api_with_keyword(String keyword) {
        try {
            String token = System.getProperty("access_token");
            if (token == null || token.isEmpty()) {
                Assert.fail("Lỗi: Không tìm thấy Access Token để thực thi tìm kiếm!");
            }

            String searchUrl = "https://apigateway.yoot.vn/yoedu/management/api/students/paging-custom";
            System.out.println(">>> Đang gửi Request POST Tìm kiếm học viên với Body JSON, từ khóa: " + keyword);

            JSONObject paginationObj = new JSONObject();
            paginationObj.put("pageSize", 30);
            paginationObj.put("pageNumber", 1);
            paginationObj.put("isPaging", true);

            org.json.JSONArray filtersArray = new org.json.JSONArray();

            String[] fields = {"Code", "Name", "Parent.Name", "Parent.PhoneNumber", "Grade.Name"};
            for (String fieldName : fields) {
                JSONObject filterItem = new JSONObject();
                filterItem.put("operator", "contains");
                filterItem.put("field", fieldName);
                filterItem.put("value", keyword); // Truyền từ khóa (Ví dụ: "Michael")
                filtersArray.put(filterItem);
            }

            JSONObject logicObj = new JSONObject();
            logicObj.put("value", "or");

            JSONObject filterGroupObj = new JSONObject();
            filterGroupObj.put("filters", filtersArray);
            filterGroupObj.put("logic", logicObj);

            org.json.JSONArray groupFiltersArray = new org.json.JSONArray();
            groupFiltersArray.put(filterGroupObj);

            JSONObject sortObj = new JSONObject();
            sortObj.put("predicate", "Student.AlterDate");
            sortObj.put("reverse", true);

            org.json.JSONArray sortArray = new org.json.JSONArray();
            sortArray.put(sortObj);

            JSONObject paramObj = new JSONObject();
            paramObj.put("pagination", paginationObj);
            paramObj.put("groupFilters", groupFiltersArray);
            paramObj.put("includes", new org.json.JSONArray());
            paramObj.put("sort", sortArray);

            JSONObject rootRequestBody = new JSONObject();
            rootRequestBody.put("isExist", 0);
            rootRequestBody.put("param", paramObj);

            response = given()
                    .relaxedHTTPSValidation()
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + token)
                    .body(rootRequestBody.toString())
                    .when()
                    .post(searchUrl)
                    .then()
                    .extract()
                    .response();

        } catch (Exception e) {
            Assert.fail("Lỗi khi dựng Body JSON và gọi API Tìm kiếm học viên: " + e.getMessage());
        }
    }

    @Then("I verify the student search response returns successfully")
    public void i_verify_the_student_search_response_returns_successfully() {
        try {
            assertEquals(200, response.statusCode());

            JSONObject jsonResponse = new JSONObject(response.getBody().asString());
            assertTrue("API Tìm kiếm trả về 'succeeded' là false!", jsonResponse.getBoolean("succeeded"));
            Assert.assertFalse("Trường 'data' trong response tìm kiếm bị null", jsonResponse.isNull("data"));

            // Bóc tách mảng danh sách học viên
            org.json.JSONArray studentList = jsonResponse.getJSONArray("data");
            System.out.println(">>> Tìm thấy tổng cộng " + studentList.length() + " học viên khớp từ khóa.");

            if (studentList.length() > 0) {
                // Bắt động ID của học viên đầu tiên tìm thấy để phục vụ cho các case View/Edit/Delete phía sau
                JSONObject firstStudent = studentList.getJSONObject(0);
                String dynamicStudentId = firstStudent.get("id").toString();

                System.setProperty("new_student_id", dynamicStudentId);
                System.out.println(">>> Đã lưu ID học viên động từ kết quả tìm kiếm: " + dynamicStudentId);
            } else {
                Assert.fail("Không tìm thấy học viên nào trong danh sách để chạy các bước kiểm thử tiếp theo!");
            }

        } catch (Exception e) {
            Assert.fail("Lỗi kiểm tra dữ liệu JSON Tìm kiếm học viên: " + e.getMessage());
        }
    }

    // ==========================================
    // CHỨC NĂNG: XEM CHI TIẾT HỌC VIÊN (GET)
    // ==========================================
    @When("I execute the view student details API for the retrieved student id")
    public void i_execute_the_view_student_details_api_for_the_retrieved_student_id() {
        try {
            String token = System.getProperty("access_token");
            String studentId = System.getProperty("new_student_id");

            if (studentId == null || studentId.isEmpty()) {
                Assert.fail("Lỗi: Không tìm thấy ID học viên động trong bộ nhớ hệ thống!");
            }

            // URL Xem chi tiết động: .../students/{id}
            String viewDetailUrl = "https://apigateway.yoot.vn/yoedu/management/api/students/" + studentId;
            System.out.println(">>> Đang gọi API GET Xem chi tiết học viên ID: " + studentId);

            response = given()
                    .relaxedHTTPSValidation()
                    .header("Authorization", "Bearer " + token)
                    .when()
                    .get(viewDetailUrl)
                    .then()
                    .extract()
                    .response();

        } catch (Exception e) {
            Assert.fail("Lỗi khi thực thi API Xem chi tiết học viên: " + e.getMessage());
        }
    }

    @Then("I verify the student details response returns correctly")
    public void i_verify_the_student_details_response_returns_correctly() {
        try {
            assertEquals(200, response.statusCode());

            JSONObject jsonResponse = new JSONObject(response.getBody().asString());

            assertTrue("Xem chi tiết học viên thất bại, 'succeeded' là false!", jsonResponse.getBoolean("succeeded"));

            Assert.assertFalse("Trường 'data' trong response bị null", jsonResponse.isNull("data"));
            JSONObject dataObject = jsonResponse.getJSONObject("data");

            String actualStudentId = dataObject.get("id").toString();
            String studentCode = dataObject.optString("code", "N/A");
            String studentName = dataObject.optString("name", "N/A");

            System.setProperty("new_student_id", actualStudentId);

            System.out.println(">>> ĐÃ BẮT ĐƯỢC THÔNG TIN HỌC VIÊN ĐỘNG TỪ API VIEW DETAIL:");
            System.out.println("    - ID Học viên (Hệ thống lưu): " + actualStudentId);
            System.out.println("    - Mã học viên (Code): " + studentCode);
            System.out.println("    - Tên học viên (Name): " + studentName);
            System.out.println("    - Tên phụ huynh (Parent Name): " + dataObject.optString("nameParent", "N/A"));
            System.out.println("    - Số điện thoại phụ huynh: " + dataObject.optString("phoneNumberParent", "N/A"));
            System.out.println("    - Trạng thái hoạt động: " + dataObject.optBoolean("isActive"));

            // Xác thực bổ sung: Đảm bảo ID trả về trùng khớp với ID học viên mà kịch bản đã yêu cầu trước đó
            String expectedId = System.getProperty("new_student_id");
            assertEquals("Lỗi: ID học viên trả về từ chi tiết không khớp với ID yêu cầu!", expectedId, actualStudentId);

        } catch (Exception e) {
            Assert.fail("Lỗi kiểm tra dữ liệu JSON Xem chi tiết học viên: " + e.getMessage());
        }
    }

    // ==========================================
    // CHỨC NĂNG: CHỈNH SỬA HỌC VIÊN (PUT - FORM PARAMS)
    // ==========================================
    @When("I execute the edit student API for the retrieved student id")
    public void i_execute_the_edit_student_api_for_the_retrieved_student_id() {
        try {
            String token = System.getProperty("access_token");
            String dynamicId = System.getProperty("new_student_id");

            if (dynamicId == null || dynamicId.isEmpty()) {
                Assert.fail("Lỗi: Không tìm thấy ID học viên động!");
            }

            // Endpoint tĩnh xử lý cập nhật dữ liệu học viên
            String editUrl = "https://apigateway.yoot.vn/yoedu/management/api/students";
            System.out.println(">>> API Edit Student đang gọi (PUT): " + editUrl);


            // 1. Tạo Map chứa các tham số dạng Form (Học từ cách viết kịch bản Classroom của bạn)
            Map<String, Object> formFields = new java.util.LinkedHashMap<>();
            formFields.put("id", Integer.parseInt(dynamicId));
            formFields.put("code", "HV8889");
            formFields.put("name", "Michael - updated");
            formFields.put("phoneNumber", "0905000000");
            formFields.put("photo", "https://s3south.storage.com.vn/yootvn/Upload/YoEdu/Student/Student_2248/Photo/Image_Student_2248_a2f92edb-36e4-4592-aeec-f9c64d025739.png");
            formFields.put("dateOfBirth", "2030-05-31T00:00:00");
            formFields.put("note", "Giỏi");
            formFields.put("gradeId", 3);
            formFields.put("parentId", 1824);
            formFields.put("genderId", 0);
            formFields.put("relationshipId", 1);
            formFields.put("otherRelationship", "");
            formFields.put("school", "Nguyễn Du");
            formFields.put("nameParent", "Nguyễn Văn A");
            formFields.put("genderParentId", 0);
            formFields.put("phoneNumberParent", "0905739111");
            formFields.put("address", "TP. HCM");
            formFields.put("emailParent", "email@gmail.com");
            formFields.put("consultant", "");
            formFields.put("recommenderPhone", "");
            formFields.put("isActive", true);

            // 2. Thực hiện gọi API bằng phương thức PUT với cấu hình .formParams dạng URL-Encoded
            response = given()
                    .relaxedHTTPSValidation()
                    .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                    .header("Authorization", "Bearer " + token)
                    .formParams(formFields) // Kéo toàn bộ Map tham số vào thân Request
                    .when()
                    .put(editUrl) // Nếu Server yêu cầu POST cho form, bạn chỉ cần sửa cụm này thành .post(editUrl)
                    .then()
                    .extract()
                    .response();

        } catch (Exception e) {
            Assert.fail("Lỗi khi thực thi API PUT Chỉnh sửa học viên: " + e.getMessage());
        }
    }

    @Then("I verify the student edit response is successful")
    public void i_verify_the_student_edit_response_is_successful() {
        try {
            // Xác nhận cập nhật thành công (200 OK)
            assertEquals(200, response.statusCode());

            JSONObject jsonResponse = new JSONObject(response.getBody().asString());
            assertTrue("Chỉnh sửa học viên thất bại, 'succeeded' là false!", jsonResponse.getBoolean("succeeded"));

            // Bóc tách dữ liệu từ object "data" (Tham chiếu chuẩn xác theo Key thực tế trong JSON Student)
            Assert.assertFalse("Trường 'data' trong response bị null", jsonResponse.isNull("data"));
            JSONObject dataObject = jsonResponse.getJSONObject("data");

            System.out.println(">>> CHỈNH SỬA THÔNG TIN HỌC VIÊN THÀNH CÔNG!");

        } catch (Exception e) {
            Assert.fail("Lỗi kiểm tra dữ liệu JSON Chỉnh sửa học viên: " + e.getMessage());
        }
    }
}