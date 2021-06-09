#Author: Kameshwar Murali
@assignment
Feature: assignment2_getEmployeeDetails

  @scenario2
  Scenario Outline: To pick the Request Data from external file_<TestCaseName>
    Given I want to initiate the config file
    And use data from excel "User-Data" using sheet "Employee"
    And I pass path parameters
      | id | id |
    And I perform GET operation "<resourceName>"
    Then I should get "<status>" response
    #To validateBody Pattern, and the values like messages
    And I should validate employeeDetails response structure and values
    And I should get the response with the following values
      | message | Successfully! Record has been fetched. |

    Examples: 
      | TestCaseName | resourceName | status |
      | assignment2  | URL_details  |    200 |
