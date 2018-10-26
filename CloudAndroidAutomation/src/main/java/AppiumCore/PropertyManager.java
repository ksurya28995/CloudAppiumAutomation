package AppiumCore;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;
/**
 * Class is used to load the values/properties from the configuration file
 * 
 * @return returns the value for the key from the config file
 * 
 * @author SuryaRay
 */
public class PropertyManager{
	static public String fileName = "androidConfig.properties";
	static Properties prop = new Properties();
	static InputStream input = null;
	
	public static String getPropertyValue(String valueHead) {
		try {
		input = PropertyManager.class.getClassLoader().getResourceAsStream(fileName);
		if(input==null) {
			System.out.println("Unable to find the "+fileName);
			throw new FileNotFoundException();
		} 
		prop.load(input);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return prop.getProperty(valueHead.toLowerCase());
	}
}

