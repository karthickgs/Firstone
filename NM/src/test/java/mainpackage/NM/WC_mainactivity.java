package mainpackage.NM;

import java.awt.AWTException;
import java.io.IOException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;

import com.aventstack.extentreports.reporter.ExtentReporter;

import io.github.bonigarcia.wdm.WebDriverManager;

public class WC_mainactivity extends WC_insured implements ExtentReporter {
	
	public static void main(String[] args) throws InterruptedException, AWTException {
		// TODO Auto-generated method stub
		WC_mainactivity wc = new WC_mainactivity();
		wc.launch("chrome");
		wc.login("admin", "shriram@1", "421010");
		wc.mainpage("Work");
		wc.proposal_add();
		wc.proposal_creation("EMPLOYEE COMPENSATION - NAMED", "1");
		wc.basic_info("AA0000000003","Abiman","IN-23474115","Chennai","Aviation","Direct","KARETI SUKUMAR");
		wc.docUpload("C:\\Users\\k1027\\Downloads\\HLSuraksha_policy.pdf","Branch rem1");
		wc.addRisk("Aviation", "WC_SK2--SKILLED", "Daily", "10", "100","C:\\Users\\k1027\\Downloads\\WC Risk Details.xlsx","Proposal Form","C:\\Users\\k1027\\Downloads\\HLSuraksha_policy.pdf");
		wc.switchNewindow();
		wc.login("NMapproval", "Shriram@2", "421010");
		wc.mainpage("S0528");
		wc.addApproval();
		wc.switchMainWin();
		wc.afterApproved_pay();
		wc.genPolicy("NEFT");
	}

}
