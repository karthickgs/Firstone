package com.proposal;

import org.testng.annotations.Test;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.RepeatedTest;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class Marine_proposal {
	
	private WebDriver driver;
	By User_name = By.id("txtUserID");
	By Login_pswd = By.id("txtPwd");
	By Division = By.id("txtDivision");
	By submit = By.name("btLogin");

	@Parameters("browser")
	@BeforeTest
	public void setup(String browser_name) {
		if(browser_name == "Chrome") {
			WebDriverManager.chromedriver().setup();
			driver = new ChromeDriver();
		}
		else if(browser_name == "Edge") {
			WebDriverManager.edgedriver().setup();
			driver = new EdgeDriver();
		}
		else {
			System.out.println("No browsers found");
		}
		driver.get("https://uatnovacbs.shriramgi.com/uatnovacbs/GI.Common/Home/Login.aspx");
		driver.manage().window().maximize();
		driver.manage().timeouts().pageLoadTimeout(5, TimeUnit.SECONDS);
	}
	
	@Test
	public void navigate() {
		driver.findElement(User_name).sendKeys("EM_007138");
		driver.findElement(Login_pswd).sendKeys("shriram@1");
		WebElement div =driver.findElement(Division);
		div.click();
		div.sendKeys("421010");
    	div.sendKeys(Keys.DOWN);
		div.sendKeys(Keys.ENTER);
		driver.findElement(submit).click();
	}
	
}
