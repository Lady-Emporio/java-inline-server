package App;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

//jlhttp-2.6-distribution\jlhttp-2.6\src\main\java\net\freeutils\httpserver
//public Request(InputStream in, Socket sock) throws IOException {
//headers = readHeaders(in);
//while ((line = readLine(in)).length() > 0) {
//readToken(in, '\n', "ISO8859_1", 8192);
//public static String readToken(InputStream in, int delim,

class SocketWorker implements Runnable {
	private Socket socket;
	SocketWorker(Socket socket) {
		this.socket = socket;
	}

	@Override public void run() {workWithSocket();}

	private void workWithSocket() {
		try {
			InputStream in = socket.getInputStream();
			OutputStream out = socket.getOutputStream();
			out = new BufferedOutputStream(out, 4096);
			in = new BufferedInputStream(in, 4096);
			
			Request r=new Request(in,socket);
		} catch (IOException ignore) {
			ClearSky.error("socket error working.");
		}
	}
}

public class Main {
	final static int PORT_SERVER = 8001;
	final static String HOST = "127.0.0.1";
	
	public static void main(String[] args) {
		System.out.println("Begin: " + new Date());

		//String s[]=new String[0];
		//System.out.println(Arrays.asList(s));
		
		Server s=new Server(HOST,PORT_SERVER);
		s.start();
		
		System.out.println("End  : " + new Date());
	}
}
