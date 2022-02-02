package App;

import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;

import static App.Settings.*;

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
	HashMap<String,String>Headers;
	OutputStream out;
	
	public Response(OutputStream out) {
		this.out=out;
	}
	public void headersCreate() {
		//Date date = Calendar.getInstance().;
//		GregorianCalendar calendar = new GregorianCalendar();
//		x=calendar.get(Calendar.DAY_OF_WEEK);
//		
//		String day_name="";
//		DateFormat dateFormat = new SimpleDateFormat("<day-name>, <day> <month> <year> <hour>:<minute>:<second>");  
//		
//		String strDate = dateFormat.format(date)+" GMT";  
	}
}
