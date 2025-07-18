package com.MNova;

import java.io.File;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;

public class UtilityClass {
	
	AppiumDriverLocalService service;
	AndroidDriver driver;
	
	public void setup() {
		service = new AppiumServiceBuilder().withAppiumJS(new File("C:\\Users\\k1027\\AppData\\Roaming\\npm\\node_modules\\appium\\lib\\main.js")).usingPort(4723)
				.withIPAddress("127.0.0.1").build();
		service.start();
		
		}
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
