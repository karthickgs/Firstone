package keyword_driven_model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Constant_loader {

	static Properties proper = new Properties();

	public static String constants(String propertyname) throws IOException {
	
		
		FileInputStream fis = new FileInputStream(new File("C:\\Users\\k1027\\Documents\\Automation\\Framework_selenium\\WC_product_files\\Constants.properties"));
		proper.load(fis);
		return proper.getProperty(propertyname);

	}

}
