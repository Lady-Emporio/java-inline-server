package app.server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import app.ClearSky;
import app.route.Router;

public class SocketWorker implements Runnable {
	private Socket socket;
	SocketWorker(Socket socket) {
		this.socket = socket;
	}

	@Override public void run() {workWithSocket();}

	private void workWithSocket() {
		ClearSky.log("New thread.");
		try {
			InputStream in = socket.getInputStream();
			OutputStream out = socket.getOutputStream();
			out = new BufferedOutputStream(out, 4096);
			in = new BufferedInputStream(in, 4096);
			
			Request r;
			try {
				r=new Request(in,socket);
			}catch (Exception e) {
				ClearSky.error("Error with create request. "+e.getMessage());
				Response res=new Response(out);
				res.sendAll("Fatal error"+e.getMessage(),400);
				return;
			} 
			Response res=new Response(out);
			Router.Instanse().call(r,res);
			//Router_old.getInstanse().call(r,res);
		} catch (Exception ignore) {
			ignore.printStackTrace();
			ClearSky.error("socket error working."+ignore.getMessage());
		}
	}
}