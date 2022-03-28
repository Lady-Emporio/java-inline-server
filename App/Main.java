package app;

import static app.server.Settings.*;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;

import app.orm.Conn;
import app.route.Router;
import app.server.Server;
import app.server.Settings;
import app.server.MyTestFunctional;
import app.views.BaseViews;
import app.views.MediaViews;


public class Main {
	
	public static void init() throws IOException {
		{Settings s=new Settings();}
		
		Router.Instanse().addControlers(BaseViews.class);
		Router.Instanse().addControlers(MediaViews.class);
		
		Router.Instanse().init();
	}
	
	public static void main(String[] args) {
		
		int isRunTest = 0;
		
		System.out.println("Begin: " + new Date());
		
		try {
			init();
		}catch (Exception e) {
			System.out.println(e.getMessage());
			return;
		}
		
		switch(isRunTest) {
		case 1:
			run_test();
			return;
		case 2:
			MyTestFunctional.testLogger();
			return;
		case 3:
			try {
				Conn con=new Conn();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
				
		new Server(HOST,PORT_SERVER).start();
		System.out.println("End  : " + new Date());
	}
	
	private static void run_test() {
		System.out.println("Begin test:");
		MyTestFunctional.indexOfArray();
		MyTestFunctional.arrayEndsWith();
		Method[] testingMethods = MyTestFunctional.class.getDeclaredMethods();
		for (Method testMeta:testingMethods) {
			int modifiers = testMeta.getModifiers();
			if (!Modifier.isPublic(modifiers)) {
				System.out.println(testMeta.getName()+" is pass.");
				continue;
			}
			if (testMeta.getAnnotatedParameterTypes().length!=0) {
				System.out.println(testMeta.getName()+" pass. Have: "+testMeta.getAnnotatedParameterTypes().length+
						" parameters.");
				continue;
			}
			try {
				ArrayList<Boolean>results=(ArrayList<Boolean>) testMeta.invoke(MyTestFunctional.class);
				boolean isError=false;
				for(boolean r:results) {
					if(!r) {
						isError=true;
					}
				}
				System.out.println(testMeta.getName() + " is " + (isError ? "FALLING test!!!" : "ok."));
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				System.out.println("ERROR!!!! "+testMeta.getName() + " can not invoke");
				e.printStackTrace();
			}
		}
	}
	
}

