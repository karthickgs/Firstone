package marine;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;

public class MarineLogin {

	WebDriver driver;

	public void start(String start) {

		if (start.equals("Chrome"))
			WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
		driver.get("http://172.23.1.62/NOVACBS_Testing/GI.Common/Home/Login.aspx");
		driver.manage().window().maximize();
		driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(20));
	}

	public void login(String username, String pwsd, String division) throws InterruptedException {

		driver.findElement(By.id("txtUserID")).sendKeys(username);
		WebElement pass = driver.findElement(By.xpath("//input[@id ='txtPwd']"));
		pass.sendKeys(pwsd);
		WebElement div = driver.findElement(By.xpath("//input[@id ='txtDivision']"));
		div.sendKeys(division);
		Thread.sleep(1000);
		div.sendKeys(Keys.ARROW_DOWN);
		div.sendKeys(Keys.ENTER);
		driver.findElement(By.name("btLogin")).click();

	}

	public void mainscreen() throws InterruptedException {
		Thread.sleep(2000);
		driver.findElement(By.id("scriptBox")).sendKeys("mar");
		Thread.sleep(5000);
		driver.findElement(By.xpath("//a[normalize-space()='S1006 - Marine Incoming Proposal']")).click();
		driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(10));
	}

	public void addquote(int prodtype, int policytype, int proptype) throws InterruptedException {

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofMillis(6000));
		By addbutton = By.xpath("//span[@class='jtable-toolbar-item-text']");
		wait.until(ExpectedConditions.elementToBeSelected(driver.findElement(addbutton)));
		driver.findElement(By.xpath("//span[@class='jtable-toolbar-item-text']")).click();
		Select sel = new Select(driver.findElement(By.id("ddlProductTyp")));
		sel.selectByIndex(prodtype);
		Thread.sleep(2000);
		Select sel1 = new Select(driver.findElement(By.id("ddlPolTyp")));
		sel1.selectByIndex(policytype);
		Thread.sleep(2000);
		driver.findElement(By.id("btnGo")).click();

	}

	public void proposerdetails(String Customer, String Insured,int Locality, int Social, int Doctype) {
		
		driver.findElement(By.id("imgCustomerName")).click();
		driver.findElement(By.xpath("//td[normalize-space()="+Customer+"]")).click();
		driver.findElement(By.xpath("//td[normalize-space()="+Insured+"]")).click();
		Select localitySel = new Select (driver.findElement(By.id("ddlLocality")));
		localitySel.selectByIndex(Locality);
		Select socialSel = new Select (driver.findElement(By.id("ddlSocialOthers")));
		socialSel.selectByIndex(Social);
		Select doctypeSel = new Select (driver.findElement(By.id("ddlDoctype")));
		doctypeSel.selectByIndex(Doctype);
	}

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		MarineLogin marineq = new MarineLogin();
		marineq.start("Chrome");
		marineq.login("admin", "nova@123", "421010");
		marineq.mainscreen();
		marineq.addquote(1, 1, 0);
		marineq.proposerdetails("AA0000000003", "IN-28962858", 0, 0, 0);

	}

}
