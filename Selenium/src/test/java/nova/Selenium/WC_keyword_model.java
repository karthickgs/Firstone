package nova.Selenium;

import java.io.IOException;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class WC_keyword_model {
	
	WebDriver driver;
	WC_keyword_model wk = new WC_keyword_model(driver);
	
	public WC_keyword_model(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver,this);
	}
	
	
	@FindBy(how = How.ID, using = "txtUserID")
	private WebElement userid;
	@FindBy(how = How.ID, using = "txtPwd")
	private WebElement pswd;
	@FindBy(how = How.ID, using = "txtDivision")
	private WebElement division;
	@FindBy(how = How.XPATH, using = "//td[@width='94']/child::input")
	private WebElement submit;
	
	
	@Parameters(value= {"browser"})
	@BeforeSuite
	public void launch(String browser) throws IOException {
		if(browser.equalsIgnoreCase("chrome")) {
			WebDriverManager.chromedriver().setup();
			driver= new ChromeDriver();
		}
		else if(browser.equalsIgnoreCase("edge")) {
			WebDriverManager.edgedriver().setup();
			driver = new EdgeDriver();
		}
		
		WC_configReader wc = new WC_configReader();
		driver.get(wc.getURL());
		
	}
	
	
	@Parameters(value= {"Userid","Pswd","Division"})
	@Test
	public void login(String Userid,String Pswd,String Division ) {
		
		wk.userid.sendKeys(Userid);
		wk.pswd.sendKeys(Pswd);
		wk.division.sendKeys(Division);
		wk.division.sendKeys(Keys.ARROW_DOWN);
		wk.submit.click();
	}
}
