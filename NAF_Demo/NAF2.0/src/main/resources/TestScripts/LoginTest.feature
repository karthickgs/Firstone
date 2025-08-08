# Framework Name: Novac Automation Framework
# Author: SriramR-NOVAC
# File Name: LoginTest.feature
# Description: Test script to verify login functionality

Feature: Application Login Feature
  As a user
  I want to verify the login page elements
  And login to the application

  #@LoginTest @STATIMCM-TC-459
  Scenario: Verify login page elements and login functionality
    Given I navigate to "LoginPage.ApplicationURL"
    Then I wait for "1" seconds
    #Then I verify page title is "LoginPage.LoginPageTitle"
    
    # Verify all elements exist and are enabled abd then enter test data
    Then I should see "customInputUsername" on "LoginPage" page
    Then I verify element "customInputUsername" is enabled on "LoginPage" page
    And I enter "LoginPage.Username" in "customInputUsername" field on "LoginPage" page

    Then I should see "customInputPassword" on "LoginPage" page
    Then I verify element "customInputPassword" is enabled on "LoginPage" page
    And I enter "LoginPage.Password" in "customInputPassword" field on "LoginPage" page
    
    Then I should see "login-btn" on "LoginPage" page
    Then I verify element "login-btn" is enabled on "LoginPage" page
    #And I click on "login-btn" on "LoginPage" page
    
    # Wait for login to complete
    Then I wait for "1" seconds
    
    # Verify successful login
   # When I click on "login-btn" button on "LoginPage" page and wait until page title matches "LoginPage.DashboardPageTitle"
    #Then I verify page title is "LoginPage.DashboardPageTitle"
    #Then Take screenshot "LoginSuccess"
    
    @LoginTestUserMaster @STATIMCM-TC-001
    
  Scenario: Verify login page elements and login functionality
  
    Given I navigate to "LoginPage.ApplicationURL"
    Then I wait for "1" seconds
    And I should see "customInputUsername" on "LoginPage" page
    And I verify element "customInputUsername" is enabled on "LoginPage" page
    Then I enter "LoginPage.Username" in "customInputUsername" field on "LoginPage" page
    And I enter "LoginPage.Password" in "customInputPassword" field on "LoginPage" page
    Then I select the "LoginPage.Division" in "customInputDivision" field on "LoginPage" page 
    And I click on "submit-btn" on "LoginPage" page 
    # Wait for login to complete
    And I wait for "1" seconds  
    # Verify successful login
    When I click on "submit-btn" button on "LoginPage" page and wait until page title matches "LoginPage.DashboardPageTitle"
    #Then I verify page title is "LoginPage.DashboardPageTitle"
    #Then Take screenshot "LoginSuccess"