package bitTorrent.peer;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {
	private static final int DEFAULT_PORT = 8000;
	
	public static void main(String args[]) {
		//args[1] = Server socket port
		int serverPort = args.length < 1 ? TCPServer.DEFAULT_PORT : Integer.parseInt(args[1]);
		int clientCount = 0;
		
		try (ServerSocket tcpServerSocket = new ServerSocket(serverPort);) {
			System.out.println(" - Waiting for connections '" + 
		                           tcpServerSocket.getInetAddress().getHostAddress() + ":" + 
		                           tcpServerSocket.getLocalPort() + "' ...");
			
			while (true) {				
				EchoService e = new EchoService(tcpServerSocket.accept());
				Socket sr = tcpServerSocket.accept();
				FileInputStream fr = new FileInputStream("ruta del fichero");
				byte b[] = new byte[2002];
				fr.read(b,0,b.length);
				OutputStream os = sr.getOutputStream();
				os.write(b,0,b.length);
				
			}
		} catch (IOException e) {
			System.err.println("# TCPServer IO error:" + e.getMessage());
		}
	}
}