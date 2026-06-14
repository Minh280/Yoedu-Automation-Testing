package stepsDefinition;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.junit.Assert;

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
}