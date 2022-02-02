package App;

import static App.Settings.*;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Date;

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
			ClearSky.error("socket error working."+ignore.getMessage());
		}
	}
}

public class Main {
	public static void main(String[] args) {
		System.out.println("Begin: " + new Date());
		{Settings s=new Settings();}
		
		//String s[]=new String[0];
		//System.out.println(Arrays.asList(s));
		
		//Server s=new Server(HOST,PORT_SERVER);
		//s.start();
		new Server(HOST,PORT_SERVER).start();
		//Test.indexOfArray();
		
		System.out.println("End  : " + new Date());
	}
}
