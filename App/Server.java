package app;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;

import views.BaseViews;
import views.MediaViews;

public class Server {
	ServerSocket server;

	Server(String host, int port) {
		try {
			Router route=Router.getInstanse();
			route.add("/voice/",Settings.POST ,MediaViews.class ,"getAudioFromJs");
			
			
			route.add("/",Settings.GET ,BaseViews.class ,"index");
			route.checkSettingsRoute();
			
			server = new ServerSocket();
			final InetAddress _host = InetAddress.getByName(host);
			SocketAddress socketAddress = new InetSocketAddress(_host, port);
			ClearSky.log(socketAddress.toString());
			server.bind(socketAddress);
			
		} catch (IOException e) {
			ClearSky.error(e.getMessage());
		}
	}

	void start() {
		try {
			while (server != null && !server.isClosed()) {
				Socket socket = server.accept();
				socket.setSoTimeout(10000);
				socket.setTcpNoDelay(true);
				ClearSky.log("connect");
				Runnable runable = new SocketWorker(socket);
				Thread t = new Thread(runable);
				t.start();
			}
		} catch (IOException ignore) {
			ignore.printStackTrace();
		}
	}

}
