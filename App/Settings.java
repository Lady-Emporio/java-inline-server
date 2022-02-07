package app;

import java.io.File;
import java.util.TimeZone;

public final class Settings {
	static final String HOST="127.0.0.1";;
	static final int PORT_SERVER = 8001;
	static final String POST="POST";
	static final String GET="GET";
	//static final String BASE_DIR="C:\\Users\\prog2.HLEB\\Desktop\\test1\\";
	static final String BASE_DIR="C:\\Users\\prog2.HLEB\\Desktop\\test1\\download\\";
	//static final TimeZone GMT = TimeZone.getTimeZone("GMT");

	static final String FUNCTIONAL_CACHE_CONTROL="no-cache";
	static final String FUNCTIONAL_CONNECTION="close";
	static final String FUNCTIONAL_SERVER="Champion";
	
	//static final String[] DAYS={"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
	//static final String[] MONTH={"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
	static {
		System.out.println("init settings testing.");
		File f = new File(BASE_DIR);
		if(!f.exists() && !f.isDirectory()) { 
		    throw new RuntimeException("BASE_DIR not exist.");
		}
		

	}//not work
};
