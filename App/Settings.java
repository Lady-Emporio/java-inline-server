package app;

import java.io.File;
import java.util.TimeZone;



public final class Settings {
	public static final String HOST="127.0.0.1";;
	public static final int PORT_SERVER = 8001;
	public static final String POST="POST";
	public static final String GET="GET";
	//static final String BASE_DIR="C:\\Users\\prog2.HLEB\\Desktop\\test1\\";
	public static final String BASE_DIR="C:\\Users\\prog2.HLEB\\Desktop\\test1\\download\\";
	//static final TimeZone GMT = TimeZone.getTimeZone("GMT");

	
	static final String FUNCTIONAL_CACHE_CONTROL="no-cache";
	static final String FUNCTIONAL_CONNECTION="close";
	static final String FUNCTIONAL_SERVER="Champion";

	static {
		System.out.println("init settings testing.");
		File f = new File(BASE_DIR);
		if(!f.exists() && !f.isDirectory()) { 
		    throw new RuntimeException("BASE_DIR not exist.");
		}
		

	}//not work
};
