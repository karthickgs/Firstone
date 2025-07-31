package keyword_driven_model;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jspecify.annotations.Nullable;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;
import net.bytebuddy.agent.builder.AgentBuilder.FallbackStrategy.Simple;

public class DataObject_Definitions extends Excel_utility {

	private WebDriver driver;
	private WebDriverWait wait;
	private Select sel;
	private int premium;
	private String propno;
	private JavascriptExecutor js;

	private int getpremium() {
		return premium;
	}
	
	private void setpremium(int premium) {
		this.premium=premium;
	}

	private String getPropno() {
		return propno;
	}
	private void setPropno(String propno) {
		this.propno = propno;
	}
	public String simplifyKey(String key) throws IOException {

		setckey(key);
		key = getckey();
		return key;

	}

	public void launch_URL(String URL) throws IOException {

		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
		driver.get(simplifyKey(URL));
		driver.manage().window().maximize();

	}

	public void enter_credentials(String username, String password) throws IOException {

		driver.findElement(By.id("txtUserID")).sendKeys(simplifyKey(username));
		driver.findElement(By.id("txtPwd")).sendKeys(simplifyKey(password));

	}

	public void sel_division(String division) throws IOException, InterruptedException {

		driver.findElement(By.id("txtDivision")).sendKeys(simplifyKey(division));
		Thread.sleep(3000);

	}

	public void click_submit() throws InterruptedException {

		driver.findElement(By.id("txtDivision")).sendKeys(Keys.ARROW_DOWN);
		driver.findElement(By.id("txtDivision")).sendKeys(Keys.ENTER);
		Thread.sleep(3000);
		driver.findElement(By.id("btLogin")).click();
	}

	public void choose_product(String product) throws IOException {
		wait = new WebDriverWait(driver, Duration.ofMillis(4000));
		WebElement sid = wait.until(ExpectedConditions.elementToBeClickable(By.id("scriptBox")));
		sid.sendKeys(simplifyKey(product));
		sid.sendKeys(Keys.ENTER);
		sid.sendKeys(Keys.ARROW_DOWN);
		WebElement work = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//ul[@id='ulmenu']//li/a[contains(text(), '" +simplifyKey(product)+ "')]")));
		work.click();
	}

	public void add_newproposal() throws InterruptedException{

		wait = new WebDriverWait(driver, Duration.ofMillis(4000));
		Thread.sleep(4000);
		WebElement add = wait
				.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"ProposalSearchContainer\"]/div/div[3]/div[2]/span/span[1]")));
		add.click();

	}

	public void sel_proposaltype(String policyType, String proposalType) throws InterruptedException, IOException {

		Thread.sleep(3000);
		WebElement policy = driver.findElement(By.id("ddlPolTyp"));
		sel = new Select(policy);
		sel.selectByVisibleText(simplifyKey(policyType));
		WebElement prop = driver.findElement(By.id("ddlPropTyp"));
		sel = new Select(prop);
		sel.selectByVisibleText(simplifyKey(proposalType));
		driver.findElement(By.id("btnGo")).click();
	}

	public void sel_custcode(String custcode) throws IOException {

		WebElement custom = driver.findElement(By.xpath("//*[@id=\"imgCustomerName\"]"));
//		wait = new WebDriverWait(driver, Duration.ofMillis(10000));
//		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"imgCustomerName\"]")));
		custom.click();

		// code for filter the header to search the value
		WebElement cuselect = driver.findElement(By.id("ddlCust"));
		sel = new Select(cuselect);
		sel.selectByVisibleText("Customer Code");

		driver.findElement(By.id("txtCust")).sendKeys(simplifyKey(custcode));
		driver.findElement(By.xpath("//td[contains(text(),'" + simplifyKey(custcode) + "')]")).click();
	}

	public void sel_inscode(String fname,String inscode) throws IOException, InterruptedException {

		// code for filter the header to search the value
		driver.findElement(By.id("imgInsuredType")).click();
		WebElement instype = driver.findElement(By.id("SelectIns"));
		sel = new Select(instype);
		sel.selectByVisibleText("First Name");

		driver.findElement(By.id("TextInsCls")).sendKeys(simplifyKey(fname));
		Thread.sleep(3000);
		driver.findElement(By.id("Button5")).click();

//		Thread.sleep(3000);
		wait = new WebDriverWait(driver,Duration.ofMillis(4000));
		WebElement found_rec = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//table[@id='InsTable']/tbody/tr[2]/td[3][contains(text(),'"+simplifyKey(inscode)+"')]")));
		found_rec.click();
		
		
		

	}

	public void sel_locality(String locality) throws IOException {
		WebElement instype = driver.findElement(By.id("ddlLocality"));
		sel = new Select(instype);
		sel.selectByVisibleText(simplifyKey(locality));
	}
	public void sel_social(String social) throws IOException {
		WebElement instype = driver.findElement(By.id("ddlSocialOthers"));
		sel = new Select(instype);
		sel.selectByVisibleText(simplifyKey(social));
	}
	public void sel_doctype(String doc) throws IOException {
		WebElement instype = driver.findElement(By.id("ddlDoctype"));
		sel = new Select(instype);
		sel.selectByVisibleText(simplifyKey(doc));
	}
	public void sel_exiscust() throws IOException {
		driver.findElement(By.id("RDLExistingcustomer1")).click();
	}
	public void sel_phypolicy() throws IOException {
		driver.findElement(By.id("RDLPhysicalPolicy2")).click();
	}

	public void enter_location(String location) throws IOException {

		driver.findElement(By.id("txtRiskLocation")).sendKeys(simplifyKey(location));;
		

	}

	public void enter_businesstype(String business) throws IOException {
		driver.findElement(By.id("txtInsBusiness")).sendKeys(simplifyKey(business));
		

	}
	
	public void sel_sourcetype(String srctype) throws IOException {
		WebElement busiSource =driver.findElement(By.id("ddlBussSource"));
		sel = new Select(busiSource);
		sel.selectByVisibleText(simplifyKey(srctype));
		

	}
	
	public void sel_executive(String exeCode) throws IOException, InterruptedException {
		
		driver.findElement(By.id("imgbtnExecutive")).click();
		WebElement selexec = driver.findElement(By.id("SelectExec"));
		sel = new Select(selexec);
		sel.selectByVisibleText("Executive Name");
		//js.executeScript("document.getElementById('SelectExec').selectedIndex=2");
		driver.findElement(By.id("TextExecCls")).sendKeys(simplifyKey(exeCode));
		driver.findElement(By.id("Button2")).click();
		
		
//		Thread.sleep(2000);
		WebElement exe_code=wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//table[@id ='ExecTable']/tbody/tr[2]/td[contains(text(),'"+simplifyKey(exeCode)+"')]")));
		exe_code.click();
			

		}
		
	public void create_prop() {
		
		driver.findElement(By.id("btnSave")).click();
		WebElement modal = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(text(),'Proposal Saved & Generated Successfully')]")));
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

	public void sel_upload_PANForm60(String PANupload) throws IOException {
		
		driver.findElement(By.id("Imagepanupload")).click();
		WebElement PAN=driver.findElement(By.xpath("//input[@id='Button7']/following-sibling::button/span[2]"));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='Button7']/following-sibling::button/span[2]")));
		PAN.click();
//		Thread.sleep(3000);
		driver.switchTo().frame("iframediv1");
		driver.findElement(By.xpath("//input[@type='file']")).sendKeys(simplifyKey(PANupload));
//		Thread.sleep(3000);
		driver.findElement(By.xpath("//input[@id='btnUpload']")).click();
		js = (JavascriptExecutor) driver;
		js.executeScript("window.alert = function(){};");
		driver.switchTo().defaultContent();
		driver.findElement(By.id("Button15")).click();
	}
	
	public void enter_remarks(String branrem) throws IOException {
		driver.findElement(By.id("txtbranremark")).sendKeys(simplifyKey(branrem));
	}
	
	public void sel_nature(String jobnature) throws InterruptedException, IOException {
		driver.findElement(By.xpath("//input[@id='Contract1']")).click();
		driver.findElement(By.xpath("//*[@id=\"saverecord1\"]/div/label")).click();
		driver.switchTo().frame("iframediv_LocPopUp");
		Thread.sleep(2000);
		WebElement Work1 = driver.findElement(By.xpath("//textarea[@id='txtRISK_DATA_02']"));
		Work1.sendKeys(simplifyKey(jobnature));
		Work1.sendKeys(Keys.ARROW_DOWN);
		Work1.sendKeys(Keys.ENTER);
	}
	
	public void sel_job(String job) throws IOException {
		sel = new Select(driver.findElement(By.id("ddlRISK_LOV_05")));
		sel.selectByVisibleText(simplifyKey(job));
	}
	
	public void sel_wage(String wage) throws IOException {
		sel = new Select(driver.findElement(By.id("ddlRISK_LOV_04")));
		sel.selectByVisibleText(simplifyKey(wage));
	}
	public void enter_noofemp(String empno) throws IOException {
		driver.findElement(By.id("txtRISK_NUM_04")).sendKeys(simplifyKey(empno));
		
	}
	public void enter_wagesper(String Wperemp) throws IOException, InterruptedException {
		driver.findElement(By.id("txtRISK_NUM_03")).sendKeys(simplifyKey(Wperemp));
		driver.findElement(By.id("lblRISK_SI_FC")).click();
//		Thread.sleep(2000);
		driver.findElement(By.xpath("//input[@id='btnsave']")).click();
		Thread.sleep(1000);
		Alert save = driver.switchTo().alert();
		System.out.println(save.getText());
		save.accept();
		driver.switchTo().defaultContent();
		driver.findElement(By.id("imgLocPopUp_Close")).click();
//		Thread.sleep(1000);
		WebElement TotalEstAmount = driver.findElement(By.xpath("//*[@id=\"LocTable1\"]/tr[2]/td[5]"));
		String Total=TotalEstAmount.getText();
		System.out.println("The Total estimated amount is "+Total);
	}
	
	public void sel_risk(String riskupload) throws IOException, InterruptedException{
		WebElement excelUpload = driver.findElement(By.id("Excel7"));
		excelUpload.click();
		driver.switchTo().frame("iframePolicydiv");
		driver.findElement(By.id("fuPhotos")).sendKeys(simplifyKey(riskupload));
		Thread.sleep(3000);
		driver.findElement(By.name("btnExUpload")).click();
		Thread.sleep(2000);
		Alert Alt2 = driver.switchTo().alert();
		Alt2.accept();
		driver.switchTo().defaultContent();
		driver.findElement(By.id("ImgPolicyBtn")).click();
		driver.findElement(By.id("btnSave")).click();
	}
	
}
