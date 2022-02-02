package App;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class ClearSky {
	public static long parseULong(String s) throws NumberFormatException {
		long val = Long.parseLong(s, 10); // throws NumberFormatException
		if (s.charAt(0) == '-' || s.charAt(0) == '+')
			throw new NumberFormatException("invalid digit: " + s.charAt(0));
		return val;
	}
	public static void log(String text) {
		System.out.println(text);
	}
	public static void error(String text) {
		System.out.println("!!!!!Error!!!!! "+text);
	}
	public static String trimDuplicates(String s, char c) {
        int start = 0;
        while ((start = s.indexOf(c, start) + 1) > 0) {
            int end;
            for (end = start; end < s.length() && s.charAt(end) == c; end++);
            if (end > start)
                s = s.substring(0, start) + s.substring(end);
        }
        return s;
    }
	
	//split(line, " ", -1);
	public static String[] split(String str, String delimiters, int limit) {
        if (str == null)
            return new String[0];
        Collection<String> elements = new ArrayList<String>();
        int len = str.length();
        int start = 0;
        int end;
        while (start < len) {
            for (end = --limit == 0 ? len : start;
                 end < len && delimiters.indexOf(str.charAt(end)) < 0; end++);
            String element = str.substring(start, end).trim();
            if (element.length() > 0)
                elements.add(element);
            start = end + 1;
        }
        return elements.toArray(new String[0]);
    }
	
	public static Headers readHeaders(InputStream in) throws IOException {
        Headers headers = new Headers();
        String line;
        String prevLine = "";
        int count = 0;
        while ((line = ClearSky.readLine(in)).length() > 0) {
            int start; // start of line data (after whitespace)
            for (start = 0; start < line.length() &&
                Character.isWhitespace(line.charAt(start)); start++);
            if (start > 0) // unfold header continuation line
                line = prevLine + ' ' + line.substring(start);
            int separator = line.indexOf(':');
            if (separator < 0)
                throw new IOException("invalid header: \"" + line + "\"");
            String name = line.substring(0, separator);
            String value = line.substring(separator + 1).trim(); // ignore LWS
            Header replaced = headers.replace(name, value);
            // concatenate repeated headers (distinguishing repeated from folded)
            if (replaced != null && start == 0) {
                value = replaced.getValue() + ", " + value;
                line = name + ": " + value;
                headers.replace(name, value);
            }
            prevLine = line;
            if (++count > 100)
                throw new IOException("too many header lines");
        }
        return headers;
    }
	
	public static String readLine(InputStream in) throws IOException {
		return readToken(in, '\n', "ISO8859_1", 8192);
	}

	public static String readToken(InputStream in, int delim, String enc, int maxLength) throws IOException {
		// note: we avoid using a ByteArrayOutputStream here because it
		// suffers the overhead of synchronization for each byte written
		int b;
		int len = 0; // buffer length
		int count = 0; // number of read bytes
		byte[] buf = null; // optimization - lazy allocation only if necessary
		while ((b = in.read()) != -1 && b != delim) {
			if (count == len) { // expand buffer
				if (count == maxLength)
					throw new IOException("token too large (" + count + ")");
				len = len > 0 ? 2 * len : 256; // start small, double each expansion
				len = maxLength < len ? maxLength : len;
				byte[] expanded = new byte[len];
				if (buf != null)
					System.arraycopy(buf, 0, expanded, 0, count);
				buf = expanded;
			}
			buf[count++] = (byte) b;
		}
		if (b < 0 && delim != -1)
			throw new EOFException("unexpected end of stream");
		if (delim == '\n' && count > 0 && buf[count - 1] == '\r')
			count--;
		return count > 0 ? new String(buf, 0, count, enc) : "";
	}
	
	public static ArrayList<Byte> readBody(InputStream in,int len) throws IOException{
		ArrayList<Byte> body=new ArrayList<Byte>(len);
		  for(int i=0;i<len;++i) { 
			  int b=in.read();
			  if(b==-1) {
				  throw new IOException("fast end body.");
			  }
			  body.add((byte) b);
			  
		  }
		 return body;
	}
	public static HashMap<String,String> parseParamsList(String s) throws IOException {
        if (s == null || s.length() == 0)
            return new HashMap<String,String>();
        HashMap<String,String> params = new HashMap<String,String>();
        for (String pair : split(s, "&", -1)) {
            int pos = pair.indexOf('=');
            String name = pos < 0 ? pair : pair.substring(0, pos);
            String val = pos < 0 ? "" : pair.substring(pos + 1);
                name = URLDecoder.decode(name.trim(), "UTF-8");
                val = URLDecoder.decode(val.trim(), "UTF-8");
                if (name.length() > 0) {
                	params.put(name, val);
                }
        }
        return params;
    }
}

