package cn.luozhuowei.common;

import java.io.InputStream;
import java.util.Properties;

/**
 * 读取配置文件
 * 
 * @author zhuowei.luo
 */
public class ConfigUtil {

	private static final String CONFIG_NAME = "config.properties";
	private static Properties p;

	static {
		p = new Properties();
		InputStream inStream = ConfigUtil.class.getClassLoader().getResourceAsStream(CONFIG_NAME);
		try {
			p.load(inStream);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String getProperty(String key) {
		return p.getProperty(key);
	}
	
}
