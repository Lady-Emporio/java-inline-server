package app.server;

import java.io.File;
import java.util.TimeZone;



public final class Settings {
	public static final String HOST="127.0.0.1";;
	public static final int PORT_SERVER = 8001;
	//static final String BASE_DIR="C:\\Users\\prog2.HLEB\\Desktop\\test1\\";
	public static final String BASE_DIR="C:\\Users\\prog2.HLEB\\Desktop\\test1\\download\\";
	public static final String TEMPLATES_DIR="D:\\Java\\EclipseProject\\T_2022_01_10\\src\\templates";
	//static final TimeZone GMT = TimeZone.getTimeZone("GMT");

	
	static final String FUNCTIONAL_CACHE_CONTROL="no-cache";
	static final String FUNCTIONAL_CONNECTION="close";
	static final String FUNCTIONAL_SERVER="Champion";

	static {//Check settings //***
		System.out.println("init settings testing.");
		for(String pathToFile : new String[]{BASE_DIR,TEMPLATES_DIR}) {
			File f = new File(pathToFile);
			if(!f.exists() && !f.isDirectory()) { 
			    throw new RuntimeException("Dir: '"+pathToFile+"' not exist.");
			}
		}
		
	
	}//Check settings ***//
};
