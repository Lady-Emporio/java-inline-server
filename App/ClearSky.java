package app;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;

import app.server.HttpFiles;
import app.server.Headers;

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
            Headers.Header replaced = headers.replace(name, value);
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
		while (true) {
			try {
			b = in.read();
			}catch (IOException e) {
				throw e;
			}
			if (!(b != -1 && b != delim)) {
				break;
			}
			
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
	
	public static byte[] readBody(InputStream in,int len) throws IOException{
		byte body[]=new byte[len];
		  for(int i=0;i<len;++i) { 
			  int b=in.read();
			  if(b==-1) {
				  throw new IOException("fast end body.");
			  }
			  body[i]=(byte) b;
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
	
	public static ArrayList<HttpFiles> parseBoundary(byte[] rawBody, String boundary) throws IOException  {
		ArrayList<HttpFiles> boundaries = new ArrayList<HttpFiles>();
		byte[] bBoundary;

		{
			byte[] lboundary = boundary.getBytes();
			bBoundary = new byte[lboundary.length + 2];
			bBoundary[0] = '-';
			bBoundary[1] = '-';
			System.arraycopy(lboundary, 0, bBoundary, 2, lboundary.length);
		}
		
		if (indexOfArray(rawBody, bBoundary, 0) != 0) {
			throw new IOException("Not found boundary in begin body.");
		}
		
		byte[] eofBoundary = {'-','-','\n'};
		byte[] passBoundary = {'\n','\n'};
		
		int posNextBlock = 0;
		int stop = -1;
		byte[][] boundariesRaw = new byte[12][];
		while(true) {
			++stop;
			if (stop > 11) {
				throw new IOException("Not support vary many files in boundary.");
			}
			
			posNextBlock=indexOfArray(rawBody, bBoundary, posNextBlock);
			posNextBlock+=bBoundary.length;
			int contentDispositionBegin=posNextBlock;
			
			{
				int endBoundary;
				boolean foundEndBoundary;
				while( true ) {
					foundEndBoundary = true;
					endBoundary = posNextBlock;
					
					if( rawBody[endBoundary] == '\r') {
						++endBoundary;
					}
					if( rawBody[endBoundary] != '\n') {
						foundEndBoundary=false;
					}
					++endBoundary;
					
					if( rawBody[endBoundary] == '\r') {
						++endBoundary;
					}
					if( rawBody[endBoundary] != '\n') {
						foundEndBoundary=false;
					}
					
					if (foundEndBoundary) {
						posNextBlock=endBoundary+1;
						break;
					}
					++posNextBlock;
				}
			}
			int contentDispositionEnd=posNextBlock-1;
			
			int endBlock = indexOfArray(rawBody, bBoundary, posNextBlock);
			if(-1 == endBlock) {
				throw new IOException("Promlem with found boundary.");
			}
			int beginNextBoundary=endBlock;
			beginNextBoundary+= bBoundary.length;	
			boolean isEnd = true;
			if(rawBody[beginNextBoundary] == '-') {
				int endCounter=beginNextBoundary;
				endCounter++;
				if (rawBody[endCounter]!='-') {
					isEnd=false;
				}
				endCounter++;
				if (rawBody[endCounter]=='\r') {
					endCounter++;
				}
				if (rawBody[endCounter]!='\n') {
					isEnd=false;
				}
				if(rawBody.length != endCounter+1) {
					isEnd=false;
				}
			}
			while ( rawBody[endBlock-1] == '\n' ) {
				--endBlock;
				if (rawBody[endBlock-1] == '\r') {
					--endBlock;
				}
			}
			
			byte[] nowBoundary=new byte[endBlock-posNextBlock];
			System.arraycopy(rawBody, posNextBlock, nowBoundary, 0, endBlock-posNextBlock);
			boundariesRaw[stop]=nowBoundary;
			posNextBlock=endBlock;
			
			byte[] rawContentDisposition=new byte[contentDispositionEnd-contentDispositionBegin];
			System.arraycopy(rawBody, contentDispositionBegin, rawContentDisposition, 0, contentDispositionEnd-contentDispositionBegin);
			String contentDispositionStr = new String(rawContentDisposition,StandardCharsets.ISO_8859_1);
			contentDispositionStr= new String(contentDispositionStr.getBytes(),StandardCharsets.UTF_8) ;
			
			HttpFiles file = new HttpFiles(contentDispositionStr, nowBoundary);
			boundaries.add(file);
			
			if(isEnd) {
				break;
			}
			endBlock=beginNextBoundary;
		}
		
		
		
		

		return boundaries;
	}

	public static ArrayList<HttpFiles> parseBoundary_1version(byte[] rawBody, String boundary) throws IOException {
		byte[] lboundary = boundary.getBytes();
		byte[] bBoundary = new byte[lboundary.length + 2];
		bBoundary[0] = '-';
		bBoundary[1] = '-';
		System.arraycopy(lboundary, 0, bBoundary, 2, lboundary.length);
		
		
		if (indexOfArray(rawBody,bBoundary,0)!=0) {
			throw new IOException("Not found boundary in begin body.");
		}
			
		ArrayList<HttpFiles> boundaries=new ArrayList<HttpFiles>();
		int contentDispositionPos=bBoundary.length;
		boolean allFound = false;
		int stop=40;
		while (!allFound) {
			--stop;
			if(stop<0) {
				throw new IOException("Very many boundary. Or parse error.");
			}
			

			if(rawBody[contentDispositionPos]=='\r' && rawBody[contentDispositionPos+1]=='\n') {
				contentDispositionPos+=2;
			}else if(rawBody[contentDispositionPos]=='\n') {
				contentDispositionPos+=1;;
			}else {
				throw new IOException("Not found content-disposition in begin body.");
			}
			
			int endContentDisposition = contentDispositionPos;
			for (; rawBody[endContentDisposition] != '\n'; ++endContentDisposition) {
			}
			if (rawBody[endContentDisposition - 1] == '\r') {
				--endContentDisposition;
			}
			byte[] contentDispositionByte = new byte[endContentDisposition - contentDispositionPos];
			System.arraycopy(rawBody, contentDispositionPos, contentDispositionByte, 0,
					endContentDisposition - contentDispositionPos);
			//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
			//String contentDispositionStr = new String(contentDispositionByte);
			String contentDispositionStr = new String(contentDispositionByte,StandardCharsets.ISO_8859_1);
			contentDispositionStr= new String(contentDispositionByte,StandardCharsets.UTF_8) ;
			
			
			//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
			
			int nextPosBeginBoundary = indexOfArray(rawBody, bBoundary, endContentDisposition);
			if (rawBody[nextPosBeginBoundary + bBoundary.length] == '-'
					&& rawBody[nextPosBeginBoundary + bBoundary.length + 1] == '-') {
				allFound=true;
				if( (nextPosBeginBoundary + bBoundary.length + 1)!=rawBody.length-1 && rawBody[rawBody.length-1]!='\n') {
					throw new IOException("Can not parse END of boundaries.");
				}
				
			}else {
				contentDispositionPos=nextPosBeginBoundary + bBoundary.length;
			}
			
			int afterEndContentDisposition=endContentDisposition;
			
			byte[] splitRawBody1 = {'\r','\n','\r','\n'};
			byte[] splitRawBody2 = {'\n','\n'};
			
			boolean passAllTrashLine;
			while( true ) {
				passAllTrashLine=true;
				for(int j=0;j<splitRawBody1.length;++j) {
					if( rawBody[afterEndContentDisposition+j]!=splitRawBody1[j]) {
						passAllTrashLine=false;
					}
				}
				if (passAllTrashLine) {
					afterEndContentDisposition+=splitRawBody1.length;
					break;
				}
				
				passAllTrashLine=true;
				for(int j=0; j<splitRawBody2.length;++j) {
					if( rawBody[afterEndContentDisposition+j]!=splitRawBody2[j]) {
						passAllTrashLine=false;
					}
				}
				if (passAllTrashLine) {
					afterEndContentDisposition+=splitRawBody2.length;
					break;
				}
				++afterEndContentDisposition;
			}
			
			
			int beforeNextPosBeginBoundary=nextPosBeginBoundary-1;//not include
//			while ('\r'==rawBody[afterEndContentDisposition] || '\n'==rawBody[afterEndContentDisposition]) {
//				++afterEndContentDisposition;
//			}
			while ('\r'==rawBody[beforeNextPosBeginBoundary] || '\n'==rawBody[beforeNextPosBeginBoundary]) {
				--beforeNextPosBeginBoundary;
			}
			
			int lenData=beforeNextPosBeginBoundary-afterEndContentDisposition;
			byte []dataBoundary=new byte[lenData];
			System.arraycopy(rawBody,afterEndContentDisposition,dataBoundary,0,lenData);
			boundaries.add(new HttpFiles(contentDispositionStr,dataBoundary));
		}
		
		return boundaries;
	}
	
	public static boolean arrayStartsWith(byte[]first, byte[]second, int beginFirst) {
		if (first.length < beginFirst+second.length) {
			return false;//In pos in first array can not put second
		}
		int i=0;
		if(first[beginFirst+second.length-1]!=second[second.length-1] && first[beginFirst+second.length-1]!=second[second.length-1]) {
			return false;
		}
		for( ;i<second.length;++i) {
			if (first[beginFirst+i]!=second[i]) {
				return false;
			}
		}
		return true;
	}
	
	public static boolean arrayEndsWith(byte[]first, byte[]second) {
		if (first.length < second.length) {
			return false;
		}
		for(int i=0;i<second.length;++i) {
			if (first[first.length-1-i] != second[second.length-1-i]) {
				return false;
			}
		}
		return true;
	}
	public static int indexOfArray(byte[]first, byte[]second, int beginFirst) {
		int i=beginFirst;
		boolean found;
		for(; first.length>=i+second.length;++i) {
			found=arrayStartsWith(first,second,i);
			if( found ) {
				return i;
			}
		}
		return -1;
	}
}

