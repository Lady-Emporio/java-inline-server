package app.server;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;

import app.ClearSky;
import app.forms.GenForm;
import app.forms.annotations.FormField;
import app.forms.annotations.FormField.FormTypes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MyTestFunctional {
	public static class TestHref{
		public int id;
		public String name;
		public TestHref(int id,String name){
			this.id=id;
			this.name=name;
		}
	}
	
	public static void testLogger() {
		try {
			LogManager.getLogManager().readConfiguration(MyTestFunctional.class.getResourceAsStream("logging.properties"));
		} catch (SecurityException | IOException e) {
			e.printStackTrace();
		}
		//java.util.logging.ConsoleHandler.level = ALL;
		//java.util.logging.ConsoleHandler.pattern = log.log;
		//java.util.logging.ConsoleHandler.formatter =java.util.logging.SimpleFormatter;
				
		Logger logger = LoggerFactory.getLogger(MyTestFunctional.class);
		System.out.println(logger);
		logger.debug("[THYMELEAF] INITIALIZING TEMPLATE ENGINE");
		logger.info("info qwe");
		logger.error("error qwe");
		logger.debug("debug qwe");
		//java.util.logging.Logger.getLogger("")
	}
	
	public static List<Boolean> readFileFromResource()  {
		try {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("today", "WeAre");
			String html=Templates.getTemplateFromMap("index.html", map);
			System.out.println(html);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new ArrayList<Boolean>();
	}

	public static class TestCast {
		// @FormField(typeGetData = FormTypes.typeStr)
		// public String id;
		@FormField(typeGetData = FormTypes.typeStr)
		public byte[] wtf;
		@FormField()
		public String name;
	}

	public static List<Boolean> testUtf8() {
		String utf = "Привет";
		System.out.println(utf + ": " + utf.length());
		return new ArrayList<Boolean>();
	}

	public static List<Boolean> testCast() {
		System.out.println("##################");
		TestCast t = new TestCast();
		t.name = "This is my string Вася.";
		t.wtf = "Привет".getBytes();
		try {
			String w = GenForm.generateHtml(TestCast.class, t);
			System.out.println(w);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("##################");
		return new ArrayList<Boolean>();
	}

	public static List<Boolean> testByte() {
		System.out.println("Max val = " + Byte.MAX_VALUE); // prints: Max val = 127
		System.out.println("Min val = " + Byte.MIN_VALUE); // prints: Min val = -128

		System.out.println("(byte)137 = " + (byte) 137); // prints: (byte)137 = -119
		System.out.println("(byte)128 = " + (byte) 128); // prints: (byte)128 = -128
		System.out.println("(byte)-129 = " + (byte) -129); // prints: (byte)-129 = 127

		byte b = (byte) 137;
		int i = ((int) b) & 0xff;
		System.out.println("From byte: " + i);

		return new ArrayList<Boolean>();
	}

	public static List<Boolean> indexOfArray() {
		byte[] first = { 1, 2, 3, 4, 5, 6 };
		byte[] second;
		int beginFirst = 0;

		boolean[] results = new boolean[4];

		first = new byte[] { 1, 2, 3, 4, 5, 6 };
		second = new byte[] { 1, 2, 3, 4, 5, 6 };
		results[0] = ClearSky.indexOfArray(first, second, beginFirst) == 0;

		first = new byte[] { 1, 2, 3, 4, 5, 6 };
		second = new byte[] { 1, 2, 3, 4, 5, 6, 7 };
		results[1] = ClearSky.indexOfArray(first, second, beginFirst) == -1;

		first = new byte[] { 1, 2, 3, 4, 5, 6 };
		second = new byte[] { 2, 3, 4 };
		results[2] = ClearSky.indexOfArray(first, second, beginFirst) == 1;

		first = new byte[] { 1, 2, 3, 4, 5, 6, 7 };
		second = new byte[] { 7 };
		results[3] = ClearSky.indexOfArray(first, second, beginFirst) == 6;

		List<Boolean> w = new ArrayList<Boolean>();
		for (boolean r : results) {
			w.add(r);
		}
		return w;
	}

	public static List<Boolean> arrayEndsWith() {
		byte[] first = { 1, 2, 3, 4, 5, 6 };
		byte[] second;
		ArrayList<Boolean> results = new ArrayList<Boolean>();

		results.add(ClearSky.arrayEndsWith(first, new byte[] { 1, 2, 3, 4, 5, 6 }) == true);

		results.add(ClearSky.arrayEndsWith(first, new byte[] { 5, 6 }) == true);

		results.add(ClearSky.arrayEndsWith(first, new byte[] { 1, 2, 3, 4, 6 }) == false);

		results.add(ClearSky.arrayEndsWith(first, new byte[] { 4, 5, 6 }) == true);

		results.add(ClearSky.arrayEndsWith(first, new byte[] { 3, 5, 6 }) == false);

		results.add(ClearSky.arrayEndsWith(first, new byte[] { 1, 2, 3 }) == false);

		results.add(ClearSky.arrayEndsWith(first, new byte[] { 6 }) == true);

		results.add(ClearSky.arrayEndsWith(first, new byte[] { 1 }) == false);

		results.add(ClearSky.arrayEndsWith(new byte[] { 1, 2 }, new byte[] { 1, 2, 3 }) == false);
		results.add(ClearSky.arrayEndsWith(new byte[] { 2, 3 }, new byte[] { 1, 2, 3 }) == false);

		return results;
	}

}
