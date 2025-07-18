package mainpackage.NM;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.time.Duration;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jspecify.annotations.Nullable;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WindowType;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.WheelInput.ScrollOrigin;
import org.openqa.selenium.remote.service.DriverFinder;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class WC_insured  {

	WebDriver driver;
	WebDriverWait wait;
	Actions act;
	Select sel;
	Robot bot;
	JavascriptExecutor js;
	String propno;
	int premium;
	
	public int getpremium() {
		return premium;
	}
	
	public void setpremium(int premium) {
		this.premium=premium;
	}

	public String getPropno() {
		return propno;
	}
	public void setPropno(String propno) {
		this.propno = propno;
	}
	
	@Parameters({"browser"})
	@BeforeSuite
	
	public void launch(@Optional("chrome") String browser) {

		if (browser.equalsIgnoreCase("chrome")) {
			WebDriverManager.chromedriver().setup();
			driver = new ChromeDriver();
		} else if (browser.equalsIgnoreCase("edge")) {
			WebDriverManager.edgedriver().setup();
			driver = new EdgeDriver();
		}

		driver.get("https://uatnovacbs.shriramgi.com/uatnovacbs/GI.Common/Home/Login.aspx");
		driver.manage().window().maximize();
	}

	@Parameters({"username","pswd","division"})
	@Test
	public void login(String username, String pswd, String division) throws InterruptedException {

		driver.findElement(By.id("txtUserID")).sendKeys(username);
		driver.findElement(By.id("txtPwd")).sendKeys(pswd);
		driver.findElement(By.id("txtDivision")).sendKeys(division);
		Thread.sleep(3000);
		driver.findElement(By.id("txtDivision")).sendKeys(Keys.ARROW_DOWN);
		driver.findElement(By.id("txtDivision")).sendKeys(Keys.ENTER);
		driver.findElement(By.id("btLogin")).click();

	}
	

	@Parameters({"screenname"})
	@Test(enabled= true)
	public void mainpage(String screenname) throws InterruptedException {
		Thread.sleep(1000);
		WebElement sid = driver.findElement(By.id("scriptBox"));
		sid.sendKeys(screenname);
		Thread.sleep(2000);
		sid.sendKeys(Keys.ENTER);
		sid.sendKeys(Keys.ARROW_DOWN);
		Thread.sleep(2000);
		WebElement work = driver
				.findElement(By.xpath("//ul[@id='ulmenu']//li/a[contains(text(), '"+screenname+"')]"));
		
		work.click();
		

	}
	
	@Test(enabled= true)
	public void proposal_add() throws InterruptedException {
		Thread.sleep(2000);
		wait = new WebDriverWait(driver, Duration.ofMillis(10000));
		WebElement add = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[@class='jtable-toolbar-item-text']")));
		add.click();
	}
	@DataProvider(name = "prop_creation")
	public Object propCreation() {
		return new Object[][] {{"EMPLOYEE COMPENSATION - NAMED","1"}};
	}
	
//	@Parameters({"policyType","proposalType"})
	@Test(enabled = true,dataProvider = "prop_creation")
	public void proposal_creation(String policyType, String proposalType) throws InterruptedException {
		Thread.sleep(2000);
		WebElement policy = driver.findElement(By.id("ddlPolTyp"));
		sel = new Select(policy);
		sel.selectByVisibleText(policyType);
		WebElement prop = driver.findElement(By.id("ddlPropTyp"));
		sel = new Select(prop);
		sel.selectByValue(proposalType);
		driver.findElement(By.id("btnGo")).click();
	}
	
	@DataProvider(name = "dp")
	public Object add() {
		return new Object[][] {{"AA0000000003","Mr. abiman  tets","IN-23474115","Chennai","Farm","Direct","KARETI SUKUMAR"}};
	}
	
	@Test(dataProvider = "dp",enabled = true)
	public void basic_info(String customer,String Fname,String Inscode,String riskLoc,String insBusiness,String busSource,String exeCode) throws InterruptedException {

		
		WebElement custom =driver.findElement(By.xpath("//*[@id=\"imgCustomerName\"]"));
		wait = new WebDriverWait(driver, Duration.ofMillis(10000));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"imgCustomerName\"]")));
		custom.click();
		WebElement cuselect = driver.findElement(By.id("ddlCust"));
		sel = new Select(cuselect);
		sel.selectByVisibleText("Customer Code");
		driver.findElement(By.id("txtCust")).sendKeys(customer);
		driver.findElement(By.xpath("//td[contains(text(),'"+customer+"')]")).click();
		
		js = (JavascriptExecutor) driver;
		driver.findElement(By.id("imgInsuredType")).click();
		WebElement instype =driver.findElement(By.id("SelectIns"));
		sel = new Select(instype);
		sel.selectByVisibleText("First Name");
		
		WebElement typefName = driver.findElement(By.id("TextInsCls"));
		js.executeScript("arguments[0].value = arguments[1];",typefName,Fname );
		Thread.sleep(3000);
		driver.findElement(By.id("Button5")).click();
		
		Thread.sleep(5000);
		wait = new WebDriverWait(driver, Duration.ofSeconds(4));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//table[@id='InsTable']/tbody/tr[3]/td")));
		WebElement InsSel =driver.findElement(By.xpath("//table[@id='InsTable']/tbody/tr[3]/td"));
		InsSel.click();
		
		js.executeScript("document.getElementById('ddlLocality').selectedIndex=1");
		
		js.executeScript("document.getElementById('ddlSocialOthers').selectedIndex =1");
		
		js.executeScript("document.getElementById('ddlDoctype').selectedIndex =2");
		
		driver.findElement(By.id("RDLExistingcustomer1")).click();
		
		driver.findElement(By.id("RDLPhysicalPolicy2")).click();
		
		driver.findElement(By.id("txtRiskLocation")).sendKeys(riskLoc);
		
		driver.findElement(By.id("txtInsBusiness")).sendKeys(insBusiness);
		
		WebElement busiSource =driver.findElement(By.id("ddlBussSource"));
		sel = new Select(busiSource);
		sel.selectByVisibleText(busSource);
		
		driver.findElement(By.id("imgbtnExecutive")).click();
		js.executeScript("document.getElementById('SelectExec').selectedIndex=2");
		driver.findElement(By.id("TextExecCls")).sendKeys(exeCode);
		driver.findElement(By.id("Button2")).click();
		
		
		Thread.sleep(2000);
		WebElement exe_code=driver.findElement(By.xpath("//table[@id = 'ExecTable']/tbody/tr[2]/td[contains(text(),'"+exeCode+"')]"));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//table[@id = 'ExecTable']/tbody/tr[2]/td[contains(text(),'"+exeCode+"')]")));
		exe_code.click();
		
		driver.findElement(By.id("btnSave")).click();
		WebElement modal = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(text(),'Proposal Saved & Generated Successfully')]")));
		// Click the OK button inside the modal
		WebElement okButton = modal.findElement(By.xpath("//button[text()='OK']"));
		WebElement propNo = driver.findElement(By.xpath("//div[@class='jconfirm-content']/div"));
		String propno = propNo.getText();
		System.out.println(propno);
		okButton.click();
		String getProp = "\\d{6}[\\/]\\d{2}[\\/]\\d{2}[\\/][P][\\/]\\d{7}";
		Pattern pat = Pattern.compile(getProp);
		Matcher match = pat.matcher(propno);
		String prop = "";
		if(match.find()) {
			 prop = match.group();
		}
		
		 setPropno(prop);
	}

	@Test
	public void docUpload(String filePath,String remarks) throws InterruptedException, AWTException {
		
		driver.findElement(By.id("Imagepanupload")).click();
		WebElement PAN=driver.findElement(By.xpath("//input[@id='Button7']/following-sibling::button/span[2]"));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='Button7']/following-sibling::button/span[2]")));
		PAN.click();
		Thread.sleep(3000);
		driver.switchTo().frame("iframediv1");
		driver.findElement(By.xpath("//input[@type='file']")).sendKeys(filePath);
		Thread.sleep(3000);
		driver.findElement(By.xpath("//input[@id='btnUpload']")).click();
		js.executeScript("window.alert = function(){};");
		driver.switchTo().defaultContent();
		driver.findElement(By.id("Button15")).click();
		driver.findElement(By.id("txtbranremark")).sendKeys(remarks);
		
	}
	
	public void addRisk(String workNature,String job,String wageType,String Empl,String Wperson,String riskUpload,String DocType,String propUpload) throws InterruptedException, AWTException {
		
		driver.findElement(By.xpath("//input[@id='Contract1']")).click();
		driver.findElement(By.xpath("//*[@id=\"saverecord1\"]/div/label")).click();
		driver.switchTo().frame("iframediv_LocPopUp");
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//textarea[@id='txtRISK_DATA_02']")));
		Thread.sleep(2000);
		WebElement Work1 = driver.findElement(By.xpath("//textarea[@id='txtRISK_DATA_02']"));
		Work1.sendKeys(workNature);
		Thread.sleep(2000);
		Work1.sendKeys(Keys.ARROW_DOWN);
		Work1.sendKeys(Keys.ENTER);
		sel = new Select(driver.findElement(By.id("ddlRISK_LOV_05")));
		sel.selectByVisibleText(job);
		sel = new Select(driver.findElement(By.id("ddlRISK_LOV_04")));
		sel.selectByVisibleText(wageType);
		driver.findElement(By.id("txtRISK_NUM_04")).sendKeys(Empl);
		driver.findElement(By.id("txtRISK_NUM_03")).sendKeys(Wperson);
//		WebElement TotalEstAmount = driver.findElement(By.id("txtRISK_SI_FC"));
		driver.findElement(By.id("lblRISK_SI_FC")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//input[@id='btnsave']")).click();
		Thread.sleep(1000);
		//05-06-2025
		
		Alert save = driver.switchTo().alert();
		
		System.out.println(save.getText());
		
		save.accept();
		
		driver.switchTo().defaultContent();
		driver.findElement(By.id("imgLocPopUp_Close")).click();
		Thread.sleep(1000);
		WebElement TotalEstAmount = driver.findElement(By.xpath("//*[@id=\"LocTable1\"]/tr[2]/td[5]"));
		String Total=TotalEstAmount.getText();
		System.out.println("The Total estimated amount is "+Total);
		WebElement excelUpload = driver.findElement(By.id("Excel7"));
		excelUpload.click();
		driver.switchTo().frame("iframePolicydiv");
		driver.findElement(By.id("fuPhotos")).sendKeys(riskUpload);
		Thread.sleep(3000);
		driver.findElement(By.name("btnExUpload")).click();
		Thread.sleep(2000);
		Alert Alt2 = driver.switchTo().alert();
		Alt2.accept();
		driver.switchTo().defaultContent();
		driver.findElement(By.id("ImgPolicyBtn")).click();
		driver.findElement(By.id("btnSave")).click();
//		WebElement modal = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(text(),'Proposal Saved & Generated Successfully')]")));
//		// Click the OK button inside the modal
		WebElement okButton = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[text()='OK']")));
		Thread.sleep(2000);
		okButton.click();
		sel = new Select(driver.findElement(By.id("ddlDocCat")));
		sel.selectByVisibleText(DocType);
		Thread.sleep(3000);
		driver.findElement(By.xpath("//*[@id=\"Div11\"]/table/tbody/tr[3]/td/button[1]/span[2]")).click();
		driver.switchTo().frame("iframediv");	
		Thread.sleep(3000);
		driver.findElement(By.id("fuPhotos")).sendKeys(propUpload);
		Thread.sleep(3000);
		driver.findElement(By.id("btnUpload")).click();
		Thread.sleep(3000);

//		StringSelection propUpload1 = new StringSelection("C:\\Users\\k1027\\Downloads\\HLSuraksha_policy.pdf");
//		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(propUpload1, null);
		js.executeScript("window.alert=function(){};");
		driver.switchTo().defaultContent();
		driver.findElement(By.xpath("//span[contains(text(),'Back')]")).click();
		driver.findElement(By.id("btnSubmit")).click();

//		WebDriverWait wait = new WebDriverWait(driver,Duration.ofMillis(7000));
		Thread.sleep(5000);
		WebElement okButton3 = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(),'Yes')]")));
		okButton3.click();
		driver.findElement(By.xpath("//span[contains(text(),'close')]")).click();
		//24-06-2025-----------
	
	}
	
		public void switchNewindow() {
		
			driver.switchTo().newWindow(WindowType.TAB);
			driver.get("https://uatnovacbs.shriramgi.com/uatnovacbs/GI.Common/Home/Login.aspx");
	}
		public void addApproval() throws InterruptedException {
			
			
			js.executeScript("document.getElementById('ddlSearchby').selectedIndex=1");
			driver.findElement(By.id("txtSearchValue")).sendKeys(getPropno());
			//27-06-2025
			Thread.sleep(3000);
			driver.findElement(By.xpath("(//span[contains(text(),'Search')])[2]")).click();
			Thread.sleep(4000);
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			WebElement editButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@title='Edit Record']")));
			editButton.click();
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("btnGo")));
			WebElement status = driver.findElement(By.xpath("//th[contains(text(),'Status')]"));
			act = new Actions(driver);
			act.scrollToElement(status);
			//30-06-2025
			Select apr1 = new Select(driver.findElement(By.xpath("(//select[@id='GVApprddlApprStatus'])[1]")));
			apr1.selectByVisibleText("Approve");
			Select apr2 = new Select(driver.findElement(By.xpath("(//select[@id='GVApprddlApprStatus'])[2]")));
			apr2.selectByVisibleText("Approve");
//			js.executeScript("document.getElementById('GVApprddlApprStatus').selectedIndex=2");
//			js.executeScript("document.getElementById('GVApprddlApprStatus').selectedIndex=3");
			driver.findElement(By.xpath("//textarea[@id='txtbranremark']")).sendKeys("rem2");
			driver.findElement(By.id("btnApprove")).click();
		}
		
		public void switchMainWin() {
			
			String parent = driver.getWindowHandle();
			Set<String> Allwin= driver.getWindowHandles();
			for(String window:Allwin) {
			if(!window.equals(parent)) {
			driver.close();
			driver.switchTo().window(window);
			}
			}
		}
		//01-07-2025
		public void afterApproved_pay() throws InterruptedException {
			driver.findElement(By.id("divbtnClose")).click();
			js.executeScript("document.getElementById('ddlSearchby').selectedIndex=1");
			driver.findElement(By.id("txtSearchValue")).sendKeys(getPropno());
			driver.findElement(By.xpath("(//span[contains(text(),'Search')])[2]")).click();
			Thread.sleep(3000);
			wait = new WebDriverWait(driver, Duration.ofMillis(6000));
			WebElement editButton =wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@title='Edit Record']")));
			editButton.click();
		}
		//02-07-2025
		
		public void genPolicy(String paymode) throws InterruptedException {
			driver.manage().timeouts().implicitlyWait(Duration.ofMillis(10000));
			act = new Actions(driver);
			WebElement addins = driver.findElement(By.id("btnAddCheque"));
			act.scrollToElement(addins).click().build().perform();
			addins.click();
			WebDriverWait wait = new WebDriverWait(driver,Duration.ofMillis(5000));
			WebElement receipt = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[normalize-space()='Receipt Details']")));
			receipt.click();
			String prm =driver.findElement(By.id("txtpremamt")).getAttribute("value");
			double conprm = Double.parseDouble(prm);
			int conprm1 = (int)conprm;
//			Math.floor(conprm);
			System.out.println(prm);
			
			driver.findElement(By.id("btnInstrument")).click();
			Select pay = new Select(driver.findElement(By.id("ddlInsType")));
			pay.selectByVisibleText(paymode);
			driver.findElement(By.xpath("(//img[@class='ui-datepicker-trigger'])[2]")).click();
			WebElement monele = driver.findElement(By.className("ui-datepicker-month"));
//			monele.click();
			Select month = new Select(monele);
			month.selectByIndex(6);
			WebElement yearele = driver.findElement(By.className("ui-datepicker-year"));
//			yearele.click();
			Select year = new Select(yearele);
			year.selectByIndex(0);
			driver.findElement(By.xpath("(//a[contains(text(),'3')])[1]")).click();
			driver.findElement(By.id("txtInstRefNo")).sendKeys("124466");
			driver.findElement(By.id("txtInsAmt")).sendKeys(String.valueOf(conprm1));
			driver.findElement(By.xpath("//span[contains(normalize-space()='ADD')]")).click();
			driver.findElement(By.id("btnSave")).click();
			Thread.sleep(4000);
			driver.findElement(By.xpath("//button[conatins(text(),'OK')]")).click();
			Thread.sleep(4000);
			act.scrollToElement(driver.findElement(By.id("btnGenPolicy"))).click().build().perform();
			String polnum = driver.findElement(By.xpath("//div[normalize-space()='Successfully']")).getText();
			System.out.println(polnum);
			
			
			
		}
}
