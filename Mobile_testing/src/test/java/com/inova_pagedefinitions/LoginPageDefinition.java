package com.inova_pagedefinitions;

import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.beust.jcommander.Parameter;
import com.inova_pageobjects.LoginPageObjects;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
public class LoginPageDefinition extends LoginPageObjects{
	
	 AndroidDriver driver;

	public LoginPageDefinition(AndroidDriver driver){
		
		super(driver);
		PageFactory.initElements(new AppiumFieldDecorator(driver), this);
		
	}
	
	
	public void login(String usr,String pswd) {
		
		username.click();
		if(driver.isKeyboardShown()) {
			driver.hideKeyboard();
		}
		username.sendKeys(usr);
		password.click();
		if(driver.isKeyboardShown()) {
			driver.hideKeyboard();
			
		}
		password.sendKeys(pswd);
	}
	
}