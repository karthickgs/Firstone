package nova.Selenium;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class WC_configReader {

	Properties prop;
	private String conf = "Config//config.property";
	BufferedReader bf;

	public WC_configReader() throws IOException {
		bf = new BufferedReader(new FileReader(conf));
		prop = new Properties();
		prop.load(bf);
	}

	public String getURL() {

		String Url = prop.getProperty("URL");
		return Url;

	}

}
