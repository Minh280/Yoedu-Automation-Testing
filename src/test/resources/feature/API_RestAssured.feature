@YoeduAPITesting
Feature: Authentication API Testing for Yoedu Management System

  Background:
    When I execute the login API with valid credentials
    Then I verify the login response is successful

  # =====================================================
  # CLASSROOM APIs
  # =====================================================

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