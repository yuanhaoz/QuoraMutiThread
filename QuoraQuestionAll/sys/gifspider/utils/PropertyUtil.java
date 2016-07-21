package sys.gifspider.utils;


import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;



public class PropertyUtil {
	
	public static Properties getProperties(){
		Properties result = new Properties();
		InputStream is = PropertyUtil.class.getResourceAsStream("/config.properties");
			try {
				result.load(is);
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		return result;
	}
	
	public static Properties getWProperties(){
		Properties result = new Properties();
		InputStream is = PropertyUtil.class.getResourceAsStream("/wconfig.properties");
			try {
				result.load(is);
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		return result;
	}
	
}
