Feature: Login functionality on the SauceDemo login page

  This feature verifies the login behaviour of the SauceDemo application under both positive and negative scenarios.
  It includes successful login with valid credentials, and various invalid login attempts such as empty fields and incorrect data.
  The goal is to ensure authentication behaves as expected and appropriate error messages are displayed.

  Background:
    Given the user is on the SauceDemo login page

  @positive
  Scenario: User logs in successfully with valid credentials
    When the user enters valid username and password
    And clicks the login button
    Then the user should be navigated to the inventory page

  @negative @invalidCredentials
  Scenario Outline: User attempts to login with invalid credentials
    When the user enters "<username>" as username and "<password>" as password
    And clicks the login button
    Then an error message "<errorMessage>" should be displayed

    Examples:
      | username      | password     | errorMessage                                           |
      | invalid_user  | secret_sauce | Epic sadface: Username and password do not match any user in this service |
      | standard_user | wrong_pass   | Epic sadface: Username and password do not match any user in this service |
      | locked_out_user | secret_sauce | Epic sadface: Sorry, this user has been locked out.   |

  @negative @emptyUsername
  Scenario: User leaves the username field empty
    When the user leaves the username field empty
    And enters a valid password
    And clicks the login button
    Then an error message "Epic sadface: Username is required" should be displayed

  @negative @emptyPassword
  Scenario: User leaves the password field empty
    When the user enters a valid username
    And leaves the password field empty
    And clicks the login button
    Then an error message "Epic sadface: Password is required" should be displayed
