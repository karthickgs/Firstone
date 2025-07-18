package nova.Selenium;



import java.time.Duration;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Datesone {

	WebDriver driver;

	@BeforeSuite	
	@Parameters({ "browser" })
	public void setup(@Optional("chrome") String browser) {

		if (browser.equalsIgnoreCase("chrome")) {
			WebDriverManager.chromedriver().setup();
			driver = new ChromeDriver();
			driver.get("https://jqueryui.com");
		}
//		else if(browser.equalsIgnoreCase("firefox")) {
//			WebDriverManager.firefoxdriver().setup();
//			driver = new FirefoxDriver();
		// }

		driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(9));
		driver.manage().window().maximize();
	}

	@Test
	public void draganddrop() throws InterruptedException {

		driver.findElement(By.xpath("//a[contains(text(),'Droppable')]")).click();
		Thread.sleep(1000);
		WebElement frame = driver.findElement(By.className("demo-frame"));
		driver.switchTo().frame(frame);
		WebElement drag = driver.findElement(By.xpath("//div[@id='draggable']"));
		Actions act = new Actions(driver);
		act.dragAndDrop(drag, driver.findElement(By.xpath("//div[@id='droppable']"))).build().perform();
		driver.switchTo().defaultContent();
	}

	@Test(enabled = false)
	@Parameters({ "day", "month", "year" })
	public void datepick(String day, String month, String year) throws InterruptedException {

		WebElement datepi = driver.findElement(By.xpath("//a[contains(text(),'Datepicker')]"));
		JavascriptExecutor je = (JavascriptExecutor) driver;
		je.executeScript("arguments[0].scrollIntoView;", datepi);
		datepi.click();
		WebElement frame = driver.findElement(By.className("demo-frame"));
		driver.switchTo().frame(frame);
		driver.findElement(By.id("datepicker")).click();
		List<WebElement> alldates = driver
				.findElements(By.xpath("//table[@class='ui-datepicker-calendar']/tbody/tr/td"));

		while (true) {
			
			int ayear = Integer.parseInt(year);
			int curyear = 2025;
			Thread.sleep(1000);
			WebElement next;
			next = driver.findElement(By.xpath("//div[@id='ui-datepicker-div']/child::div/a[2]"));
			if (ayear < curyear) {
				WebElement prev = driver.findElement(By.xpath("//div[@id='ui-datepicker-div']/child::div/a[1]"));
				prev.click();
			} else {
				next.click();
			}

			String nextmonth = driver.findElement(By.className("ui-datepicker-month")).getText();
			if (month.equalsIgnoreCase(nextmonth)) {
				next.click();
			}

		}

		

	}
	@Test
	public void draggable() throws InterruptedException {

		driver.findElement(By.xpath("//div[@id='sidebar']/descendant::a[contains(text(),'Draggable')]")).click();
		WebElement frame = driver.findElement(By.className("demo-frame"));
		driver.switchTo().frame(frame);
		WebElement drag = driver.findElement(By.id("draggable"));
		Actions act = new Actions(driver);
		act.dragAndDropBy(drag, 135, 75).build().perform();
		driver.switchTo().defaultContent();
	}
	
	@Test(description = "sizable")
	public void sizable() {
		WebElement frame = driver.findElement(By.className("demo-frame"));
		driver.switchTo().frame(frame);
		driver.findElement(By.xpath("//a[starts-with(text(),'Res')]")).click();
		
	}
}


