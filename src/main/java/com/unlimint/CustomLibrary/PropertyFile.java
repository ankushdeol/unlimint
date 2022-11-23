package com.unlimint.CustomLibrary;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyFile {
	
public static Properties prop;
	
	static{
		try {
		InputStream input = null;
		prop = new Properties();
		//input=
		//input = ClassLoader.class.getResourceAsStream("/PropertyFile.properties");
		input=new FileInputStream("PropertyFile.properties");
		
			prop.load(input);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
