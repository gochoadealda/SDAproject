package bitTorrent.peer;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringBufferInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer extends Thread{
	private static final int DEFAULT_PORT = 8000;
	@Override
	public void run() {
		//args[1] = Server socket port
		int serverPort = TCPServer.DEFAULT_PORT;
		int clientCount = 0;
		String clientSentence;

		try (ServerSocket tcpServerSocket = new ServerSocket(serverPort);) {
			System.out.println(" - Waiting for connections '" + 
					tcpServerSocket.getInetAddress().getHostAddress() + ":" + 
					tcpServerSocket.getLocalPort() + "' ...");

			while (true) {				
				EchoService e = new EchoService(tcpServerSocket.accept());
				Socket sr = tcpServerSocket.accept();
				BufferedReader inFromClient =
						new BufferedReader(new InputStreamReader(sr.getInputStream()));
				DataOutputStream outToClient = new DataOutputStream(sr.getOutputStream());
				clientSentence = inFromClient.readLine();
				System.out.println("------.-------El valor de clientSentence es :"+clientSentence);
				File newFile= new File("downloads/"+clientSentence);
				FileInputStream fr = new FileInputStream(newFile);
				byte b[] = new byte[(int) newFile.length()];
				fr.read(b,0,b.length);
				OutputStream os = sr.getOutputStream();
				os.write(b,0,b.length);

			}
		} catch (IOException e) {
			System.err.println("# TCPServer IO error:" + e.getMessage());
			System.out.println("333333");
		}
	}
}