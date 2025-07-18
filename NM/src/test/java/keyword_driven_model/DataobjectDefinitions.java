package keyword_driven_model;

import java.io.IOException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class DataobjectDefinitions {
	
	WebDriver driver;
	String header;
	String username;
	Excel_utility utility;
	
	public DataobjectDefinitions(Excel_utility utility) {
		
		this.utility=utility;
	}
	
	
	public String getusername() {
		return username;
	}
		
	
	public void launch_URL(String setdata) {
		
		
	}
	
	public String enter_credentials(String username) throws IOException {
		
		driver.findElement(By.id("txtUserID")).sendKeys(utility.keyword(username));
		return username;
		
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
