package views;
import java.io.IOException;

import app.Request;
import app.Response;

public class BaseViews {
	public static void page404(Request req,Response resp) throws IOException {
		resp.sendFile("page404.html");
	}
	public static void index(Request req,Response resp) throws IOException{
		resp.sendFile("index.html");
	}
}
