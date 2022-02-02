package App;

import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import static App.Settings.*;

class Request {
	private Socket socket;
	private String method;
	private URI uri;
	private Headers headers;

	byte[] rawBody = new byte[0];
	String rawStrBody = "";
	HashMap<String, String> params = new HashMap<String, String>();
	ArrayList<Files> files = new ArrayList<Files>();

	public Request(InputStream in, Socket socket) throws IOException {
		this.socket = socket;
		readRequestLine(in);
		headers = ClearSky.readHeaders(in);
		// RFC2616#3.6 - if "chunked" is used, it must be the last one
		// RFC2616#4.4 - if non-identity Transfer-Encoding is present,
		// it must either include "chunked" or close the connection after
		// the body, and in any case ignore Content-Length.
		// if there is no such Transfer-Encoding, use Content-Length
		// if neither header exists, there is no body

		String header = headers.get("Transfer-Encoding");
		String contentType = headers.get("Content-Type");
		String contentLength = headers.get("Content-Length");
		if (header != null && !header.toLowerCase(Locale.US).equals("identity")) {
			ClearSky.log("chunked");
			throw new IOException("chunked not support.");
		} else {
			ClearSky.log("raw body");
			long lenRaw = contentLength == null ? 0 : ClearSky.parseULong(contentLength);
			if (lenRaw >= Integer.MAX_VALUE) {
				throw new IOException("To big body size.");
			}
			int len = (int) lenRaw;
			rawBody = ClearSky.readBody(in, len);

			if (contentType != null
					&& contentType.toLowerCase(Locale.US).startsWith("application/x-www-form-urlencoded")) {
				rawStrBody = new String(rawBody, StandardCharsets.UTF_8);
				rawStrBody = URLDecoder.decode(rawStrBody, "UTF-8");
				params = ClearSky.parseParamsList(rawStrBody);
			}
			if (contentType != null && contentType.toLowerCase(Locale.US).startsWith("multipart/form-data")) {
				int x = contentType.indexOf("boundary=");
				if (x == -1) {
					throw new IOException("Not found boundary.");
				}
				String boundary = contentType.substring(x + "boundary=".length());
				if (boundary.length() > 70 || boundary.length() < 1) {
					throw new IOException("Unvalid boundary.");
				}
				System.out.println(boundary);
				files = ClearSky.parseBoundary(rawBody, boundary);
			}

		}

		ClearSky.log("method:      " + method);
		ClearSky.log("uri:         " + uri.toString());
		ClearSky.log("uriHost:     " + uri.getHost());
		ClearSky.log("uriPath:     " + uri.getPath());
		ClearSky.log("uriFragment: " + uri.getFragment());
		ClearSky.log("headers:     " + headers.toString());
		ClearSky.log("body:        " + rawBody.length);
		ClearSky.log("rawStrBody:  " + rawStrBody);
		ClearSky.log("params:      " + params);

		for (var i : files) {
			System.out.println(i);
			FileOutputStream fos = new FileOutputStream(BASE_DIR + "download\\" + i.getFilename());
			fos.write(i.getData());
		}
		BufferedWriter writer = new BufferedWriter(new FileWriter(BASE_DIR + "qwe.jpg"));
		for (byte b : rawBody) {
			writer.write(b);
		}
		writer.close();
	}

	private void readRequestLine(InputStream in) throws IOException {
		// RFC2616#4.1: should accept empty lines before request line
		// RFC2616#19.3: tolerate additional whitespace between tokens

		String line;
		try {
			do {
				line = ClearSky.readLine(in);
			} while (line.length() == 0);
		} catch (IOException ioe) { // if EOF, timeout etc.
			throw new IOException("missing request line"); // signal that the request did not begin
		}
		String[] tokens = ClearSky.split(line, " ", -1);
		if (tokens.length != 3)
			throw new IOException("invalid request line: \"" + line + "\"");
		try {
			method = tokens[0];
			// must remove '//' prefix which constructor parses as host name
			uri = new URI(ClearSky.trimDuplicates(tokens[1], '/'));
		} catch (URISyntaxException use) {
			throw new IOException("invalid URI: " + use.getMessage());
		}

	}

}