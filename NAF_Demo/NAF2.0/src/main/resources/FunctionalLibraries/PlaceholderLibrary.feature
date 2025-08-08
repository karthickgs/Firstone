
# Framework Name: Novac Automation Framework
# Author: SriramR-NOVAC
# File Name: PlaceholderLibrary.feature
# Description: Placeholder functional library file

@library
Feature: Placeholder Functional Library

  @library
  Scenario: Common Login Steps
    Given I navigate to "ENV_application.url"
    When I enter "TD_Username" in "username" field on "LoginPage" page
    And I enter "TD_Password" in "password" field on "LoginPage" page
    And I click on "loginButton" on "LoginPage" page
