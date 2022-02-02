package App;

import java.io.File;

public final class Settings {
	public static final String HOST="127.0.0.1";;
	final static int PORT_SERVER = 8001;
	public static final String BASE_DIR="C:\\Users\\prog2.HLEB\\Desktop\\test1\\";
	

	static {
		System.out.println("init settings testing.");
		File f = new File(BASE_DIR);
		if(!f.exists() && !f.isDirectory()) { 
		    throw new RuntimeException("BASE_DIR not exist.");
		}
	}//not work
};
