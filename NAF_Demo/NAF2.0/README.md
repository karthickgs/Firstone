
# Novac Automation Framework (NAF) - User Guide

## Table of Contents
1. [Framework Overview](#1-framework-overview)
2. [Prerequisites and Setup](#2-prerequisites-and-setup)
3. [Framework Architecture](#3-framework-architecture)
4. [Configuration Management](#4-configuration-management)
5. [Test Data Management](#5-test-data-management)
6. [Object Repository](#6-object-repository)
7. [Writing Test Scripts](#7-writing-test-scripts)
8. [Running Tests](#8-running-tests)
9. [Reporting and Results](#9-reporting-and-results)
10. [QMetry Integration](#10-qmetry-integration)
11. [Troubleshooting](#11-troubleshooting)
12. [Best Practices](#12-best-practices)

## 1. Framework Overview

The Novac Automation Framework (NAF) is a comprehensive test automation solution built on Selenium WebDriver and Cucumber. It provides a robust architecture for web application testing with enhanced features:

### Key Features
- **BDD Approach**: Cucumber-based test scripts with Gherkin syntax
- **Separated Configuration and Test Data**: Split architecture with RunManager.xlsx and TestData.xlsx
- **Batch Execution Optimization**: Single browser instance reused across test cases
- **Enhanced Scenario Filtering**: Tag-based execution of selected test cases
- **Object Repository Management**: JSON-based element locator storage
- **Multi-format Reporting**: HTML, Excel, and optional JSON/XML reporting
- **QMetry Integration**: Configurable integration with QMetry test management
- **Advanced Test Data Resolution**: Cross-sheet lookup with robust error handling
- **Screenshot Management**: Configurable screenshot capture modes

## 2. Prerequisites and Setup

### System Requirements
- Java JDK 11 or higher
- Maven 3.6 or higher
- Chrome, Firefox, Edge, or Safari browser
- Windows, Linux, or macOS operating system

### Installation

1. **Clone/Download the Framework**
   ```bash
   git clone <repository-url>
   cd NAF
   ```

2. **Build the Project**
   ```bash
   mvn clean package
   ```

3. **Verify Directory Structure**
   ```
   NAF/
   ├── TestData/
   │   ├── RunManager.xlsx    # Configuration and test execution control
   │   └── TestData.xlsx      # Test data for all test cases
   ├── src/main/resources/
   │   ├── TestScripts/       # Feature files
   │   └── ObjectRepo/        # Page object definitions
   └── Reports/               # Generated test reports
   ```

## 3. Framework Architecture

NAF follows a modular architecture with these key components:

### Core Components
- **ConfigLoader**: Loads framework configuration from RunManager.xlsx
- **RunManager**: Controls test execution flow and scenario filtering
- **DataManager**: Handles test data retrieval from TestData.xlsx
- **TestDataUtility**: Processes test data references with robust error handling
- **ORLoader**: Loads and manages object repository JSON files
- **WebDriverManager**: Manages browser instances with batch optimization
- **CucumberLauncher**: Executes Cucumber features with tag-based filtering
- **ReportManager**: Generates and manages test execution reports
- **QMetryClient**: Handles QMetry integration and result uploads

### Execution Flow
1. **Initialization**: Load configuration and test data
2. **Test Case Selection**: Identify Y-flagged test cases in RunManager.xlsx
3. **Batch Session Start**: Create single browser instance for all tests
4. **Feature Execution**: Run feature files with tag-based filtering
5. **Reporting**: Generate HTML, Excel, and optional JSON/XML reports
6. **QMetry Upload**: Upload results if integration is enabled
7. **Session Cleanup**: Close browser and finalize reports

## 4. Configuration Management

NAF uses `RunManager.xlsx` for framework configuration and test execution control.

### Framework Configuration Sheet

The **framework configuration** sheet contains global settings:

| Configuration Key | Description | Example Values |
|------------------|-------------|----------------|
| ProjectName | Test project name | "STATIM Test Suite" |
| Browser | Browser for test execution | Chrome, Firefox, Edge |
| Environment | Test environment | QA, DEV, STAGE, PROD |
| DataSource | Test data source | EXCEL (default) |
| HeadlessMode | Run browser in headless mode | true, false |
| ScreenshotMode | Screenshot capture mode | all, pass_fail, none |
| QMetryIntegration | Enable QMetry upload | Required, Not-Required |
| ResultUploadFormat | Report format for QMetry | XML, JSON, None |
| URL_QA | QA environment URL | https://qa.example.com |
| URL_PROD | Production environment URL | https://prod.example.com |
| QMetry_CycleID | QMetry test cycle ID | CYC-123 |
| QMetry_ProjectID | QMetry project ID | PROJ-456 |
| QMetry_API_Key | QMetry API key | your-api-key |

### Run Manager Sheet

The **Run Manager** sheet controls which test cases execute:

| TestID | Feature File | Execute |
|--------|-------------|---------|
| STATIMCM-TC-459 | LoginTest.feature | Y |
| STATIMCM-TC-460 | PlanMaster.feature | Y |
| STATIMCM-TC-461 | CheckoutTest.feature | N |

**Important**: Only test cases marked with "Y" in the Execute column will run.

### URL Configuration

Environment-specific URLs can be configured using the `URL_` prefix:

| Configuration Key | Description |
|------------------|-------------|
| URL_QA | QA environment base URL |
| URL_DEV | Development environment base URL |
| URL_STAGE | Staging environment base URL |
| URL_PROD | Production environment base URL |

Additional application-specific URLs:
| Configuration Key | Description |
|------------------|-------------|
| URL_STATIM | URL for STATIM application |
| URL_AppName | URL for specific application |

## 5. Test Data Management

NAF uses `TestData.xlsx` for storing all test data, separate from configuration.

### Test Data Structure

TestData.xlsx contains multiple sheets, typically one per page or module:

- **LoginPage**: Login-related test data
- **PlanMaster**: Plan management test data
- **Common**: Shared test data across features

### Data Organization

Each sheet has a similar structure:

| Test Case ID | Column1 | Column2 | Column3 | ... |
|-------------|---------|---------|---------|-----|
| STATIMCM-TC-459 | Value1 | Value2 | Value3 | ... |
| STATIMCM-TC-460 | Value1 | Value2 | Value3 | ... |

### Test Data References

In feature files, reference test data using the following format:
```
"SheetName.ColumnName"
```

For example:
- `"LoginPage.Username"` - Looks up the Username column in the LoginPage sheet
- `"Common.ApplicationURL"` - Looks up ApplicationURL in the Common sheet

### Test Data Resolution Process

The TestDataUtility resolves test data references in this order:

1. **Direct Match**: Looks up exact SheetName.ColumnName for current test case ID
2. **Cross-Sheet Lookup**: Searches all sheets if not found in specified sheet
3. **Test Case ID Variations**: Tries case variations and format alternatives
4. **Default Values**: Uses configured defaults if data not found

### Error Handling

If test data cannot be resolved:
- Detailed error logs are generated
- Test execution fails fast with clear error messages
- Alternative resolution approaches are attempted automatically

## 6. Object Repository

NAF uses JSON files to store page element locators in the `src/main/resources/ObjectRepo/` directory.

### Object Repository Structure

Each JSON file represents a page or component:

```json
{
  "pageName": "LoginPage",
  "elements": [
    {
      "name": "customInputUsername",
      "xpathValue": "//input[@name='username' or @id='username']"
    },
    {
      "name": "customInputPassword", 
      "xpathValue": "//input[@type='password']"
    },
    {
      "name": "login-btn",
      "xpathValue": "//button[@type='submit' or contains(text(),'Login')]"
    }
  ]
}
```

### Locator Strategies

NAF supports multiple locator strategies:
- XPath expressions
- CSS selectors
- ID, name, class based locators
- Text content based locators

### Best Practices for Element Locators

1. **Use OR conditions** for multiple possible locators
   ```
   "xpathValue": "//input[@id='username' or @name='username']"
   ```

2. **Include text-based fallbacks** for resilience
   ```
   "xpathValue": "//button[contains(text(),'Login') or @id='login-btn']"
   ```

3. **Use semantic element names** that describe functionality
   ```
   "name": "resetPasswordLink" (not "link1")
   ```

4. **Group related elements** in the same page object file

## 7. Writing Test Scripts

NAF uses Cucumber feature files for test scripts, located in `src/main/resources/TestScripts/`.

### Feature File Structure

```gherkin
# LoginTest.feature
Feature: Application Login Feature
  As a user
  I want to verify login functionality

  @LoginTest @STATIMCM-TC-459
  Scenario: Verify login functionality
    Given I navigate to "LoginPage.ApplicationURL"
    Then I wait for "2" seconds
    Then I should see "customInputUsername" on "LoginPage" page
    And I enter "LoginPage.Username" in "customInputUsername" field on "LoginPage" page
    And I enter "LoginPage.Password" in "customInputPassword" field on "LoginPage" page
    And I click on "login-btn" on "LoginPage" page
    Then I wait for "2" seconds
    # Additional verification steps
```

### Required Tags

Each scenario must have:
1. A test case ID tag: `@STATIMCM-TC-459`
2. An optional descriptive tag: `@LoginTest`

The test case ID tag must match an entry in RunManager.xlsx.

### Common Step Definitions

#### Navigation Steps
```gherkin
Given I navigate to "URL_or_TestDataReference"
```

#### Element Interaction Steps
```gherkin
When I click on "elementName" on "pageName" page
When I enter "value_or_TestDataReference" in "elementName" field on "pageName" page
When I select "optionText" from dropdown "dropdownElement" on "pageName" page
```

#### Verification Steps
```gherkin
Then I should see "elementName" on "pageName" page
Then I verify element "elementName" is enabled on "pageName" page
Then I verify element "elementName" contains "expectedText" on "pageName" page
Then I verify page title is "expectedTitle"
```

#### Wait Steps
```gherkin
Then I wait for "seconds" seconds
Then I wait for element "elementName" to appear on "pageName" page
```

#### Screenshot Steps
```gherkin
Then Take screenshot "screenshotName"
```

#### Custom Steps
Custom steps can be defined in CustomSteps.java for application-specific functionality.

## 8. Running Tests

### Using Launcher Scripts

**Windows:**
```cmd
init.bat
```

**Linux/macOS:**
```bash
./init.sh
```

### Manual Execution

```bash
mvn clean package
java -cp target/NAF-1.0-SNAPSHOT-jar-with-dependencies.jar com.novac.naf.Main
```

### Execution Process

1. **Framework Initialization**
   - Load configuration from RunManager.xlsx
   - Initialize DataManager with TestData.xlsx
   - Set up reporting directories
   - Prepare WebDriver session

2. **Test Case Selection**
   - Read Run Manager sheet
   - Identify test cases with Execute=Y
   - Group by feature file for optimization

3. **Batch Execution**
   - Start single browser instance
   - Execute feature files with tag filtering
   - Reuse browser across test cases
   - Clear browser state between tests

4. **Reporting**
   - Generate HTML reports with ExtentReports
   - Create Excel execution summary
   - Generate JSON/XML for QMetry if enabled
   - Capture screenshots based on ScreenshotMode

5. **Cleanup**
   - Close WebDriver session
   - Finalize all reports
   - Upload results to QMetry if enabled

### Execution Optimization

NAF optimizes test execution with:
- **Single Browser Instance**: Reused across test cases
- **Intelligent State Clearing**: Cookies and session data cleared between tests
- **Selective Execution**: Only Y-flagged test cases run
- **Test Data Caching**: Data loaded once and reused

## 9. Reporting and Results

NAF generates comprehensive reports in the `Reports/` directory.

### Report Types

1. **HTML Report** (ExtentReports)
   - Interactive UI with test case details
   - Step-by-step execution status
   - Embedded screenshots
   - Execution timeline and duration
   
2. **Excel Report**
   - Detailed execution data
   - Pass/fail statistics
   - Step-level information

3. **JSON/XML Reports** (Optional)
   - QMetry-compatible format
   - Automated upload capability
   - Configurable via ResultUploadFormat setting

### Report Location

Reports are organized in timestamped folders:
```
Reports/YYYY-MM-DD_HH-MM-SS/
├── ProjectName_Report_YYYY-MM-DD_HH-MM-SS.html
├── ProjectName_Report_YYYY-MM-DD_HH-MM-SS.xlsx
├── ProjectName_Report_YYYY-MM-DD_HH-MM-SS.json (if ResultUploadFormat=JSON)
└── Screenshots/
    └── TestCaseID_StepN_YYYYMMDD_HHMMSS.png
```

### Screenshot Management

Screenshots are taken based on the ScreenshotMode configuration:
- **all**: Screenshot for every step
- **pass_fail**: Screenshots for passed and failed steps
- **none**: No screenshots

Screenshots are named consistently:
```
TestCaseID_StepN_YYYYMMDD_HHMMSS.png
```

## 10. QMetry Integration

NAF integrates with QMetry for test management and result reporting.

### Configuration

In the Framework Configuration sheet of RunManager.xlsx:

| Configuration | Value | Description |
|--------------|-------|-------------|
| QMetryIntegration | Required/Not-Required | Enable/disable integration |
| ResultUploadFormat | JSON/XML/None | Report format for upload |
| QMetry_API_Key | your-api-key | QMetry API authentication |
| QMetry_ProjectID | project-id | QMetry project identifier |
| QMetry_CycleID | cycle-id | QMetry test cycle identifier |

### Upload Process

The QMetryClient handles the upload process:

1. **Authentication**: Using configured API key
2. **Report Generation**: Creating QMetry-compatible JSON/XML
3. **Upload Initiation**: Getting upload URL and tracking ID
4. **File Upload**: Sending report file to QMetry
5. **Status Monitoring**: Tracking import progress

### Test Case Mapping

Test cases are mapped to QMetry using:
- Test case ID from feature file tags (`@STATIMCM-TC-459`)
- QMetry ID from feature file tags (optional `@QMetry_ID=QT123`)

### Troubleshooting Integration

If QMetry integration fails:
- Check API credentials in the Framework Configuration sheet
- Verify network connectivity to QMetry server
- Ensure JSON/XML report format is valid
- Check QMetry project and cycle IDs

## 11. Troubleshooting

### Common Issues and Solutions

#### Test Data Not Found
```
CRITICAL ERROR processing test data reference 'LoginPage.Username' for test case 'STATIMCM-TC-459'
```
**Possible Solutions:**
- Check if TestData.xlsx exists and is accessible
- Verify the sheet "LoginPage" exists in TestData.xlsx
- Confirm the "Username" column exists for test case STATIMCM-TC-459
- Check for typos in the sheet name or column name reference

#### Element Not Found
```
Error: Element 'loginButton' not found on page 'LoginPage'
```
**Possible Solutions:**
- Verify LoginPage.json exists in ObjectRepo directory
- Check element name spelling and case (exact match required)
- Verify XPath or locator is valid for the target page
- Update object repository if the UI has changed

#### Test Case Not Executing
**Possible Solutions:**
- Check that Execute='Y' in the Run Manager sheet
- Verify the feature file exists in TestScripts directory
- Ensure the test case tag matches exactly what's in Run Manager

#### WebDriver Issues
**Possible Solutions:**
- Update your browser to the latest version
- Try a different browser (set Browser=Firefox)
- Check headless mode configuration (HeadlessMode=true/false)
- Ensure sufficient system resources are available

#### QMetry Upload Failure
**Possible Solutions:**
- Verify QMetry API credentials
- Check network connectivity to QMetry server
- Ensure ResultUploadFormat is correctly set
- Verify QMetry project and cycle IDs

### Debugging Tips

1. **Check Log Files**: Review console output for detailed error messages
2. **Inspect Reports**: HTML report contains execution details and errors
3. **Validate Configuration**: Ensure all required settings are correct
4. **Single Test Execution**: Mark only one test case with Y for focused debugging
5. **Manual Verification**: Try the test steps manually in a browser

## 12. Best Practices

### Test Data Management
- Organize test data in logical sheets by page/module
- Use descriptive column names
- Keep data specific to test case IDs
- Use Common sheet for shared data

### Object Repository
- Create separate JSON files for each page
- Use semantic element names
- Include multiple locator strategies
- Keep locators up-to-date with UI changes

### Feature Files
- Follow BDD best practices
- Use descriptive feature and scenario names
- Include proper tags for test case IDs
- Keep scenarios focused and atomic

### Framework Configuration
- Review configuration before each test run
- Use environment-specific URLs
- Configure screenshot mode based on needs
- Enable QMetry only when needed

### Maintenance
- Update object repository when UI changes
- Keep test data consistent with application state
- Maintain test case IDs consistently across files
- Follow a regular update schedule for browser drivers

---

## Framework Components Reference

### Key Java Classes

| Class | Description |
|-------|-------------|
| ConfigLoader | Loads configuration from RunManager.xlsx |
| RunManager | Manages test execution flow |
| DataManager | Handles test data retrieval |
| TestDataUtility | Processes test data references |
| ORLoader | Manages object repository |
| WebDriverManager | Controls browser sessions |
| ReportManager | Generates test reports |
| QMetryClient | Handles QMetry integration |
| CucumberLauncher | Executes Cucumber tests |
| CommonSteps | Common step definitions |

### Directory Structure

```
NAF/
├── TestData/
│   ├── RunManager.xlsx              # Framework configuration
│   └── TestData.xlsx                # Test data
├── src/
│   └── main/
│       ├── java/com/novac/naf/      # Framework code
│       └── resources/
│           ├── TestScripts/         # Feature files
│           ├── ObjectRepo/          # Page objects
│           └── FunctionalLibraries/ # Reusable functions
├── Reports/                         # Test reports
├── init.bat                         # Windows launcher
└── init.sh                          # Unix/Linux launcher
```

***

**Version:** NAF 2.0  
**Last Updated:** June 2025

