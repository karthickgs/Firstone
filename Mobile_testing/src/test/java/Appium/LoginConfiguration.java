package Appium;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.remote.AutomationName;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import com.inova_pagedefinitions.*;
import static org.testng.Assert.ARRAY_MISMATCH_TEMPLATE;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

public class LoginConfiguration {
	
	AppiumDriverLocalService service;
	AndroidDriver driver;

	@BeforeMethod
	public void setup() throws MalformedURLException {
		
		service = new AppiumServiceBuilder().withAppiumJS(new File("C:\\Users\\k1027\\AppData\\Roaming\\npm\\node_modules\\appium\\build\\lib\\main.js"))
				.withArgument(()->"--allow-cors").usingPort(4723).withIPAddress("127.0.0.1").build();
//		service = AppiumDriverLocalService.buildDefaultService();
		service.start();
		UiAutomator2Options options = new UiAutomator2Options();
		options.setAutomationName(AutomationName.ANDROID_UIAUTOMATOR2);
		options.setAutoGrantPermissions(true);
		options.setUdid("10BD4U1CC10006B");
		options.setApp("D:\\androidapk\\INova2.O.apk");
		options.nativeWebScreenshot();
		options.setAppWaitForLaunch(true);
		options.setAppPackage("com.inova.sgi");
		options.setAppActivity("com.inova.sgi.MainActivity");
		driver = new AndroidDriver(new URL("http://127.0.0.1:4723"),options);
		
	}
	
	@Test
	public  void invokemethods() {
		LoginPageDefinition def = new LoginPageDefinition(driver);
		def.login("sowmya@shriramgi.com","shriram@2");
		
	}
	
	@AfterMethod
	public void teardown() {
		
		driver.quit();
		service.stop();
	}
}
