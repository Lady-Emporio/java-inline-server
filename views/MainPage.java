package views;

import java.io.IOException;

import app.Request;
import app.Response;

public class MainPage {
	public static void cgi(Request req, Response resp) throws IOException {
		String fileName = req.uri.getPath().replace("\\", "");
		System.out.println("cgi send: " + fileName);
		resp.sendFile(fileName);
	}
}
