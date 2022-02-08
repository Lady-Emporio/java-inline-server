package app;

import static app.Settings.*;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Date;

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
			Response res=new Response(out);
			Router.getInstanse().call(r,res);
		} catch (Exception ignore) {
			ignore.printStackTrace();
			ClearSky.error("socket error working."+ignore.getMessage());
		}
	}
}

public class Main {
	public static void main(String[] args) {
		System.out.println("Begin: " + new Date());
		{Settings s=new Settings();}
		
		
		//Server s=new Server(HOST,PORT_SERVER);
		//s.start();
		
		new Server(HOST,PORT_SERVER).start();
		
		//Test.indexOfArray();

		
		System.out.println("End  : " + new Date());
	}
}
