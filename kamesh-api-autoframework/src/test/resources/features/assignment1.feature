#Author: Kameshwar Murali
@assignment
Feature: assignment1_getEmployeeList

  @scenario1
  Scenario Outline: To pick the API link from config file
    Given I want to initiate the config file
    And I perform GET operation "<resourceName>"
    Then I should get "<status>" response
    And I should verify if profile_image is blank for all the employees

    Examples: 
      | resourceName | status |
      | URL          |    200 |
