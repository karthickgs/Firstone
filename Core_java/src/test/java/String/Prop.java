package String;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Prop {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Properties prop = new Properties();
		FileInputStream file = new FileInputStream("Prop.properties");
		prop.load(file);
		String user=prop.getProperty("Username");
		System.out.println(user);
	}

}
