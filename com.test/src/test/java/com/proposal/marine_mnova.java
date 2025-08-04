package com.proposal;

import java.util.LinkedList;
import java.util.List;
import java.util.Spliterator;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.remote.DesiredCapabilities;

import io.appium.java_client.AppiumDriver;


public class marine_mnova {
		
	AppiumDriver driver;
	
	public void login_marine()
	{
		DesiredCapabilities cap = new DesiredCapabilities();
		cap.setCapability("platform","Android" );
		cap.setCapability("AndroidVersion", "14");
		cap.setCapability("deviceName", "Abiman Z");
		cap.setCapability("app","D:\\Downloads\\Marine(INC-37369).apk" );
	}
		
	}
	
	

