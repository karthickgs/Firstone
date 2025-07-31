package StepDefinitions;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class properties {
public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Properties prop = new Properties();
		InputStream file = properties.class.getClassLoader().getResourceAsStream("config.properties");
		prop.load(file);
		String user=prop.getProperty("Username");
		System.out.println(user);
	}

}
