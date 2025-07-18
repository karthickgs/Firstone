package Appium;

import java.io.File;
import java.net.URL;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;

public class Loginpage {
	
	AppiumDriver driver;
	public AppiumDriverLocalService service;
	
	@BeforeSuite
	public void setup() throws Exception {
		
		service = new AppiumServiceBuilder().withAppiumJS(new File("C:\\Users\\k1027\\AppData\\Roaming\\npm\\node_modules\\appium\\build\\lib\\main.js"))
				.withIPAddress("127.0.0.1").usingPort(4723).withTimeout(Duration.ofSeconds(30)).build();
		
		service.start();
		
		UiAutomator2Options opt = new UiAutomator2Options();
		opt.setDeviceName("Abiman Z");
		opt.setPlatformName("Android");
//		opt.setApp("D:\\androidapk\\INova2.O(5).apk");
		opt.setUdid("10BD4U1CC10006B");
		opt.setNewCommandTimeout(Duration.ofSeconds(60));
		opt.autoGrantPermissions();
		opt.setCapability("appPackage", "com.inova.sgi");
 		opt.setCapability("appActivity", "com.inova.sgi.MainActivity");
 		opt.setCapability("isRealDevice", true);
		URL url = new URL("http://127.0.0.1:4723/wd/hub"); 
		driver = new AndroidDriver(url,opt);

}
	@Test
	public void login() {
		
		WebElement username=driver.findElement(By.xpath("//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View/android.widget.EditText[1]"));
		username.sendKeys("shrigenuat82@moruba.com");
		WebElement password = driver.findElement(By.xpath("//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View/android.widget.EditText[2]"));
		password.sendKeys("cherry123");
		driver.findElement(By.id("Login")).click();
		
	}
}
