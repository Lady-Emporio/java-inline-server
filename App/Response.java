package app;

import static app.Settings.*;

import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.TimeZone;

//HTTP/1.1 200 OK
//Server: AkamaiNetStorage
//Content-Type: text/html
//Set-Cookie: bm_sv=3B6193563EE8460638F534F9308BE2B9~AIAvUlB75eQU5cQ87//BWgF+Ta3kYpc+tFeaSXY8h4Y0MyK++fOK5AdJKiTc+PB0deuwFINQmLth8LB0AXGLNWiS681YWePwHVjlv/iyBHiVG1HA+RRmlJA4dYJVN4eenecEjtGw2dTAwBlcuq9uxK1CETFCvZxd6V+xGTmhDlI=; Domain=.oracle.com; Path=/; Max-Age=3061; HttpOnly
//ETag: "392b0c741fd71015225981570f59e91c:1595391025.617556"
//Vary: Accept-Encoding
//Content-Encoding: gzip
//Cache-Control: max-age=18918
//Date: Wed, 02 Feb 2022 13:47:33 GMT
//Content-Length: 11584
//Connection: keep-alive

public class Response {
	HashMap<String, String> headers;
	OutputStream out;

	
	public Response(OutputStream out) {
		this.out = out;
		headers=new LinkedHashMap <String, String>();
	}

	public void sendAll(String html) throws IOException {
		sendAll(html,200);
	}
	
	public void sendAll(String html,int status_code) throws IOException {
		generateHeaders();
		byte[] byteHtml=html.getBytes();
		StringBuilder buffer = new StringBuilder();
		buffer.append("HTTP/1.1 "+status_code+" OK").append("\r\n");
		headers.put("Content-Length", ""+byteHtml.length);
		for(String headerKey:headers.keySet()) {
			buffer.append(headerKey+": "+headers.get(headerKey)).append("\r\n");
		}
		buffer.append("\r\n").append(html);
		out.write(buffer.toString().getBytes());
		out.flush();
		out.close();
	}
	
	private void generateHeaders() {
		headers.clear();
		headers.put("Date", serverDateNow()); 
		headers.put("Cache-Control", FUNCTIONAL_CACHE_CONTROL);
		headers.put("Connection", FUNCTIONAL_CONNECTION);
		headers.put("Server", FUNCTIONAL_SERVER);
		headers.put("Content-Type", "text/html");
		headers.put("Content-Length", "0");
	}
	
	private String serverDateNow() {
//		DateFormat dateFormat = new SimpleDateFormat("<day-name>, <day> <month> <year> <hour>:<minute>:<second>");  
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
		dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
		String serverDate = dateFormat.format(calendar.getTime());
		return serverDate;
	}
}
