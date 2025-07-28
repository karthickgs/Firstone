package keyword_driven_model;

import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestResult;

public class TestResult implements ITestResult{

	public void onTestFailure(ITestResult result) {
		
			String failed_TC = result.getName();
			TakesScreenshot ss = (driver)TakeScreenshot;
		}
		
	}
}
