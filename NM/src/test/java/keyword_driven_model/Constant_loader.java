package keyword_driven_model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class Constant_loader {

	static Properties proper = new Properties();

	public static String constants(String propertyname) throws IOException {
		FileInputStream fis = new FileInputStream("Constants.properties");
		proper.load(fis);
		return proper.getProperty(propertyname);

	}

}
