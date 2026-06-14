package stepsDefinition;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class API_Grades_Testing {

    private Response response;

//    // ==========================================
//    // CHỨC NĂNG: LOGIN
//    // ==========================================
//
//    @When("I execute the login API for grades module")
//    public void i_execute_the_login_api_for_grades_module() {
//
//        try {
//
//            RestAssured.baseURI = System.getProperty(
//                    "apiUrl",
//                    "https://management.yoedu.vn"
//            );
//
//            String loginUrl =
//                    "https://apigateway.yoot.vn/yoedu/management/api/users/login";
//
//            JSONObject requestBody = new JSONObject();
//            requestBody.put("userName", "admin");
//            requestBody.put("password", "admin@123");
//
//            response = given()
//                    .relaxedHTTPSValidation()
//                    .header("Content-Type", "application/json")
//                    .body(requestBody.toString())
//                    .when()
//                    .post(loginUrl)
//                    .then()
//                    .extract()
//                    .response();
//
//        } catch (Exception e) {
//
//            Assert.fail("Lỗi khi gọi API Login: " + e.getMessage());
//        }
//    }
//
//    @Then("I verify the login response for grades module is successful")
//    public void i_verify_the_login_response_for_grades_module_is_successful() {
//
//        try {
//
//            assertEquals(200, response.statusCode());
//
//            JSONObject jsonResponse =
//                    new JSONObject(response.getBody().asString());
//
//            assertTrue(
//                    "Đăng nhập thất bại!",
//                    jsonResponse.getBoolean("succeeded")
//            );
//
//            JSONObject dataObject =
//                    jsonResponse.getJSONObject("data");
//
//            // Lưu token động
//            if (dataObject.has("token")) {
//
//                String token = dataObject.getString("token");
//                System.setProperty("access_token", token);
//
//                System.out.println(">>> Đã lưu Token thành công!");
//
//            } else if (dataObject.has("accessToken")) {
//
//                String token = dataObject.getString("accessToken");
//                System.setProperty("access_token", token);
//
//                System.out.println(">>> Đã lưu Access Token thành công!");
//            }
//
//            System.out.println(">>> Login thành công!");
//
//        } catch (Exception e) {
//
//            Assert.fail("Lỗi verify Login Response: " + e.getMessage());
//        }
//    }

    // ==========================================
    // CHỨC NĂNG: TÌM KIẾM KHỐI LỚP (PAGING)
    // ==========================================

    @When("I execute the search grades API with pagination information")
    public void i_execute_the_search_grades_api_with_pagination_information() {

        try {

            String searchUrl =
                    "https://apigateway.yoot.vn/yoedu/management/api/grades/paging";

            String token = System.getProperty("access_token");

            if (token == null || token.isEmpty()) {

                Assert.fail("Không tìm thấy Access Token!");
            }

            // Body request chuẩn
            JSONObject paginationObj = new JSONObject();
            paginationObj.put("pageSize", 30);
            paginationObj.put("pageNumber", 1);
            paginationObj.put("isPaging", true);

            JSONObject sortObj = new JSONObject();
            sortObj.put("predicate", "AlterDate");
            sortObj.put("reverse", true);

            JSONArray sortArray = new JSONArray();
            sortArray.put(sortObj);

            JSONArray groupFiltersArray = new JSONArray();
            JSONArray includesArray = new JSONArray();

            JSONObject requestBody = new JSONObject();
            requestBody.put("pagination", paginationObj);
            requestBody.put("groupFilters", groupFiltersArray);
            requestBody.put("includes", includesArray);
            requestBody.put("sort", sortArray);

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

            Assert.fail("Lỗi khi gọi API Search Grades: " + e.getMessage());
        }
    }

    @Then("I verify the grades list response returns successfully")
    public void i_verify_the_grades_list_response_returns_successfully() {

        try {

            assertEquals(200, response.statusCode());

            JSONObject jsonResponse =
                    new JSONObject(response.getBody().asString());

            assertTrue(
                    "Search Grades thất bại!",
                    jsonResponse.getBoolean("succeeded")
            );

            Assert.assertFalse(
                    "Data bị null!",
                    jsonResponse.isNull("data")
            );

            JSONArray gradesList =
                    jsonResponse.getJSONArray("data");

            // Dynamic ID
            if (gradesList.length() > 0) {

                JSONObject firstGrade =
                        gradesList.getJSONObject(0);

                String dynamicId =
                        firstGrade.get("id").toString();

                System.setProperty(
                        "edit_grade_id",
                        dynamicId
                );

                System.out.println(
                        ">>> Đã lưu Grade ID động: "
                                + dynamicId
                );

            } else {

                Assert.fail("Không có dữ liệu khối lớp!");
            }

        } catch (Exception e) {

            Assert.fail(
                    "Lỗi verify Search Grades Response: "
                            + e.getMessage()
            );
        }
    }

    // ==========================================
    // CHỨC NĂNG: XEM CHI TIẾT KHỐI LỚP
    // ==========================================

    @When("I execute the view grade details API for the retrieved grade id")
    public void i_execute_the_view_grade_details_api_for_the_retrieved_grade_id() {

        try {

            String dynamicId =
                    System.getProperty("edit_grade_id");

            if (dynamicId == null || dynamicId.isEmpty()) {

                Assert.fail("Không tìm thấy Grade ID!");
            }

            String viewUrl =
                    "https://apigateway.yoot.vn/yoedu/management/api/grades/"
                            + dynamicId;

            System.out.println(
                    ">>> API View Grade Details: "
                            + viewUrl
            );

            String token =
                    System.getProperty("access_token");

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

            Assert.fail(
                    "Lỗi gọi API View Grade Details: "
                            + e.getMessage()
            );
        }
    }

    @Then("I verify the grade details response returns correctly")
    public void i_verify_the_grade_details_response_returns_correctly() {

        try {

            assertEquals(200, response.statusCode());

            JSONObject jsonResponse =
                    new JSONObject(response.getBody().asString());

            assertTrue(
                    "View Grade Details thất bại!",
                    jsonResponse.getBoolean("succeeded")
            );

            JSONObject dataObject =
                    jsonResponse.getJSONObject("data");

            System.out.println(">>> THÔNG TIN KHỐI LỚP");
            System.out.println(
                    "    - ID: "
                            + dataObject.get("id")
            );

            System.out.println(
                    "    - Code: "
                            + dataObject.optString("code")
            );

            System.out.println(
                    "    - Name: "
                            + dataObject.optString("name")
            );

            System.out.println(
                    "    - Description: "
                            + dataObject.optString("description")
            );

            System.out.println(
                    "    - Is Active: "
                            + dataObject.optBoolean("isActive")
            );

        } catch (Exception e) {

            Assert.fail(
                    "Lỗi verify Grade Details Response: "
                            + e.getMessage()
            );
        }
    }

    // ==========================================
    // CHỨC NĂNG: CHỈNH SỬA KHỐI LỚP
    // ==========================================

    @When("I execute the edit grade API for the retrieved grade id")
    public void i_execute_the_edit_grade_api_for_the_retrieved_grade_id() {

        try {
            // Lấy ID và Token từ hệ thống
            String dynamicId = System.getProperty("edit_grade_id");
            String token = System.getProperty("access_token");

            if (dynamicId == null || dynamicId.isEmpty()) {
                Assert.fail("Không tìm thấy Grade ID để chỉnh sửa!");
            }

            // Endpoint chuẩn (Không truyền ID lên URL)
            String editUrl = "https://apigateway.yoot.vn/yoedu/management/api/grades";

            System.out.println(">>> API Edit Grade đang gọi (PUT): " + editUrl);

            // Tạo Request Body JSON đúng cấu trúc hệ thống yêu cầu
            JSONObject updateBody = new JSONObject();
            updateBody.put("id", Integer.parseInt(dynamicId));
            updateBody.put("name", "Khối 9");
            updateBody.put("description", "Khối tốt nghiệp 2024");
            updateBody.put("code", "KL150");
            updateBody.put("isActive", false);

            // Thực hiện gửi request PUT kèm Body dữ liệu
            response = given()
                    .relaxedHTTPSValidation()
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + token)
                    .body(updateBody.toString())
                    .when()
                    .put(editUrl)
                    .then()
                    .extract()
                    .response();

        } catch (Exception e) {
            Assert.fail("Lỗi gọi API Edit Grade: " + e.getMessage());
        }
    }

    @Then("I verify the grade edit response is successful")
    public void i_verify_the_grade_edit_response_is_successful() {

        try {

            assertEquals(200, response.statusCode());

            JSONObject jsonResponse =
                    new JSONObject(response.getBody().asString());

            assertTrue(
                    "Edit Grade thất bại!",
                    jsonResponse.getBoolean("succeeded")
            );

            JSONObject dataObject =
                    jsonResponse.getJSONObject("data");

            System.out.println(">>> CHỈNH SỬA KHỐI LỚP THÀNH CÔNG!");
            System.out.println(
                    "    - Code: "
                            + dataObject.optString("code")
            );

            System.out.println(
                    "    - Name: "
                            + dataObject.optString("name")
            );

            System.out.println(
                    "    - Description: "
                            + dataObject.optString("description")
            );

        } catch (Exception e) {

            Assert.fail(
                    "Lỗi verify Edit Grade Response: "
                            + e.getMessage()
            );
        }
    }
}