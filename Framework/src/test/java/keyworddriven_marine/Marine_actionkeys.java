package keyworddriven_marine;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import io.github.bonigarcia.wdm.WebDriverManager;
import keyworddriven_marine.Marine_utilityfile;

public class Marine_actionkeys {
	
	WebDriver driver;
	Marine_actionkeys(WebDriver driver){
		PageFactory.initElements(driver,this );
	}

	By username = By.id("txtUserID");
	By password = By.xpath("//input[contains(@placeholder,'User Id')]");
	By division = By.cssSelector("input#txtDivision");
	By submitlogin = By.name("btLogin");
	By inputdash = By.id("scriptBox");
	
	public void launchApp() {
		
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
		driver.get(Marine_utilityfile.Url);
		driver.manage().window().fullscreen();
		
	}
	public void loginApp() {
		
		driver.findElement(username).sendKeys(Marine_utilityfile.username);
		driver.findElement(password).sendKeys(Marine_utilityfile.pswd);
		driver.findElement(division).sendKeys(Marine_utilityfile.division);
		driver.findElement(username).sendKeys(Keys.ARROW_DOWN);
		driver.findElement(submitlogin).click();
	}	
}
