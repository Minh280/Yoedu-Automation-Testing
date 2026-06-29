@YoeduAPITesting
Feature: Authentication API Testing for Yoedu Management System

  Background:
    When I execute the login API with valid credentials
    Then I verify the login response is successful

  # =====================================================
  # CLASSROOM APIs
  # =====================================================

  @CreateClassroom
  Scenario: Verify user can create a new classroom successfully
    Given I execute the login API with valid credentials
    Then I verify the login response is successful
    When I execute the create classroom API with valid information
    Then I verify the classroom creation response is successful


  @SearchClassroom
  Scenario: Verify user can search and fetch list of classrooms successfully
    When I execute the search classroom API with pagination information
    Then I verify the classroom list response returns successfully


  @ViewClassroomDetails
  Scenario: Verify user can view specific classroom details successfully
    When I execute the search classroom API with pagination information
    Then I verify the classroom list response returns successfully
    When I execute the view classroom details API for the retrieved classroom id
    Then I verify the classroom details response returns correctly


  @EditClassroom
  Scenario: Verify user can edit classroom details dynamically
    When I execute the search classroom API with pagination information
    Then I verify the classroom list response returns successfully
    When I execute the edit classroom API for the retrieved classroom id
    Then I verify the classroom edit response is successful

  @DeleteClassroom
  Scenario: Verify user can delete specific classroom dynamically
    When I execute the search classroom API with pagination information
    Then I verify the classroom list response returns successfully
    When I execute the delete classroom API for the retrieved classroom id
    Then I verify the classroom delete response is successful
  # =====================================================
  # GRADES APIs
  # =====================================================

  @SearchGrades
  Scenario: Verify user can search and fetch list of grades successfully
    When I execute the search grades API with pagination information
    Then I verify the grades list response returns successfully


  @ViewGradeDetails
  Scenario: Verify user can view specific grade details successfully
    When I execute the search grades API with pagination information
    Then I verify the grades list response returns successfully
    When I execute the view grade details API for the retrieved grade id
    Then I verify the grade details response returns correctly


  @EditGrade
  Scenario: Verify user can edit grade details dynamically
    When I execute the search grades API with pagination information
    Then I verify the grades list response returns successfully
    When I execute the edit grade API for the retrieved grade id
    Then I verify the grade edit response is successful

  # =====================================================
  # STUDENTS APIs
  # =====================================================

  @CreateStudent
  Scenario: Verify user can create a new student with valid dynamic information successfully
    When I execute the create student API with valid dynamic information
    Then I verify the student creation response is successful

  @SearchStudent
  Scenario: Verify user can search student list by keyword successfully
    When I execute the search student API with keyword "Michael"
    Then I verify the student search response returns successfully

  @ViewStudentDetails
  Scenario: Verify user can view specific student profile details successfully
    When I execute the search student API with keyword "Michael"
    Then I verify the student search response returns successfully
    When I execute the view student details API for the retrieved student id
    Then I verify the student details response returns correctly

  @EditStudent
  Scenario: Verify user can edit student dynamic information successfully
    When I execute the search student API with keyword "Michael"
    Then I verify the student search response returns successfully
    When I execute the edit student API for the retrieved student id
    Then I verify the student edit response is successful
