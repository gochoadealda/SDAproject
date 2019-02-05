package bitTorrent.peer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;

import modelo.Peer;
import bitTorrent.peer.HandShake;
import bitTorrent.peer.InfoTracker; 
import util.Util;
import bitTorrent.peer.ReceiveThread;
import bitTorrent.peer.SendThread;


public class TCPClient {
	private Peer peer;
	private boolean[] bitmask; 
	private boolean handshakeCheck = false; 
	private byte[] infoHash;  
	private byte[] peerId; 
	private byte[] reserved;  
	private int piecesSize; 
	private long trunkLength, fileSize;
	private boolean[][] goingDownload;
	private SendThread send;
	private ReceiveThread receive;
	private static final String DEFAULT_IP = "0.0.0.0";
	private static final int DEFAULT_PORT = 8000;
	private static final String DEFAULT_MESSAGE = "Hello World!";

	public TCPClient(Peer peer, InfoTracker info) { 
		  this.peer = peer; 
		  this.infoHash = info.getInfoHash(); 
		  this.peerId = info.getPeerId(); 
		  this.piecesSize = info.getPieceSize(); 
		  this.trunkLength = info.getPieceLength() / Constant.TRUNK_LENGTH; 
		  this.reserved = new byte[] {0x00, 0x00, 0x00, 0x00, 0x00, 0x10, 0x00, 0x05}; 
		  this.fileSize = info.getFileSize(); 
		   
		  // sometimes there are no BitField packet in order to figure the valid packet 
		//  bitmask = new boolean[piecesSize]; 
		//  Arrays.fill(bitmask, true); 
		   
		  // check the status of downloading pieces 
		  goingDownload = new boolean[piecesSize][(int) trunkLength]; 
		 }
	
	public static void main(String args[]) throws IOException {
		
		byte[] b = new byte [20002];
		// args[0] = Server IP
		String serverIP = args.length < 1 ? TCPClient.DEFAULT_IP : args[0];
		// args[1] = Server socket port
		int serverPort = args.length < 2 ? TCPClient.DEFAULT_PORT : Integer.parseInt(args[1]);
		// argrs[2] = Message
		 
		Socket sr = new Socket(serverIP, serverPort);
		InputStream is = sr.getInputStream();
		FileOutputStream fr = new FileOutputStream("ruta del fichero");
		is.read(b,0,b.length);
		fr.write(b,0,b.length);
		
		/*
		String message = args.length < 3 ? TCPClient.DEFAULT_MESSAGE: args[2];
		try (Socket tcpSocket = new Socket(serverIP, serverPort);
		     DataInputStream in = new DataInputStream(tcpSocket.getInputStream());
			 DataOutputStream out = new DataOutputStream(tcpSocket.getOutputStream())){
			
			out.writeUTF(message);
			System.out.println(" - Sent data to '" + tcpSocket.getInetAddress().getHostAddress() + ":" + tcpSocket.getPort() + 
                               "' -> '" + message + "'");
						
			String data = in.readUTF();			
			System.out.println(" - Received data from '" + tcpSocket.getInetAddress().getHostAddress() + ":" + tcpSocket.getPort() + 
                               "' -> '" + data + "'");
		} catch (UnknownHostException e) {
			System.err.println("# TCPClient Socket error: " + e.getMessage());
		} catch (EOFException e) {
			System.err.println("# TCPClient EOF error: " + e.getMessage());
		} catch (IOException e) {
			System.err.println("# TCPClient IO error: " + e.getMessage());
		}*/
	}
	//Conectarse con otro peer
	 public void run() {  
		  // make connection to this peer 
		  try { 
		   Socket socket = new Socket(peer.getIp(), peer.getPuerto()); 
		   DataInputStream input = new DataInputStream(socket.getInputStream());  
		   DataOutputStream output = new DataOutputStream(socket.getOutputStream()); 
		    
		   // first need to make handshake with peer first 
		   handshake(input, output); 
		    
		   if (handshakeCheck) {  
		    send = new SendThread(output);  
		    receive = new ReceiveThread(input); 
		     
		    // start those threads  
		    send.start(); 
		    receive.start(); 
		   } 
		    
		  } catch (IOException e) { 
		   // TODO Auto-generated catch block 
		   e.printStackTrace(); 
		  } 
		 }
	 
	 //Handshake al otro peer
	 public void handshake(DataInputStream input, DataOutputStream output) throws IOException { 
		  // send handshake to peer 
		  HandShake handshake = new HandShake((byte) Constant.DEFAULT_BITTORRENT, Constant.DEFAULT_STRING, reserved, infoHash, peerId); 
		  output.write(handshake.getData()); 
		   
		  handshake = new HandShake(input); 
		  // already make connection 
		   
		  byte[] reponseInfoHash = handshake.getInfoHash(); 
		  if (!Arrays.equals(reponseInfoHash, infoHash)) {  
		   if (Constant.DEBUG) {  
		    System.out.println("[Bittorrent HandShake Different InfoHash] " + peer.getIp().getHostAddress() + ":" + peer.getPuerto()); 
		    Util.printHex(reponseInfoHash); 
		    Util.printHex(infoHash); 
		   } 
		  } else {  
		   if (Constant.DEBUG) {  
		    System.out.println("[Bittorrent HandShake] " + peer.getIp().getHostAddress() + ":" + peer.getPuerto()); 
		   } 
		   handshakeCheck = true; 
		  } 
		 }
	 
	 public synchronized boolean download(int piece) {  
		  if (bitmask == null || (bitmask != null && bitmask[piece])) { 
		   goingDownload[piece] = new boolean[(int) trunkLength]; 
		   Arrays.fill(goingDownload[piece], true); 
		   return true; 
		  } 
		  return false; 
		 } 
		
}