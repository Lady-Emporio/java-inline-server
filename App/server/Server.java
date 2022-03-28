package app.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;

import app.ClearSky;
import app.views.BaseViews;
import app.views.MediaViews;

public class Server {
	ServerSocket server;

	public Server(String host, int port) {
		try {
			server = new ServerSocket();
			InetAddress _host = InetAddress.getByName(host);
			SocketAddress socketAddress = new InetSocketAddress(_host, port);
			ClearSky.log(socketAddress.toString());
			server.bind(socketAddress);
		} catch (IOException e) {
			ClearSky.error(e.getMessage());
		}
	}

	public void start() {
		try {
			while (server != null && !server.isClosed()) {
				Socket socket = server.accept();
				socket.setSoTimeout(10000);
				socket.setTcpNoDelay(true);
				ClearSky.log("new connection.");
				Runnable runable = new SocketWorker(socket);
				Thread t = new Thread(runable);
				t.start();
			}
		} catch (IOException ignore) {
			ignore.printStackTrace();
		}
	}

}
