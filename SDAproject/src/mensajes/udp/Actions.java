package mensajes.udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Random;

import bitTorrent.util.ByteUtils;
import controller.TrackerController;
import modelo.Tracker;
import udp.AnnounceRequest;
import udp.AnnounceResponse;
import udp.ConnectRequest;
import udp.ConnectResponse;

public class Actions extends Thread{
	private TrackerController myTracker;
	private InetAddress peerIP;
	private int peerPort;
	private InetAddress peerIPErr;
	private int peerPortErr;
	public Actions(Tracker myTracker) {
		super();
		this.myTracker = new TrackerController(myTracker);
	}
	@Override
	public void run() {
		super.run();
		try (DatagramSocket udpSocket = new DatagramSocket()){
			udpSocket.setSoTimeout(15000);

			byte[] announceBytes = new byte[98];
			byte[] errorBytes = new byte[8];
			DatagramPacket packetAnnounce = new DatagramPacket(announceBytes, announceBytes.length);
			DatagramPacket packetError = new DatagramPacket(errorBytes, errorBytes.length);
			udpSocket.receive(packetAnnounce);
			udpSocket.receive(packetError);
			this.peerIP = packetAnnounce.getAddress();
			this.peerPort = packetAnnounce.getPort();
			this.peerIPErr = packetError.getAddress();
			this.peerPortErr = packetError.getPort();
			StringBuffer bufferOut = new StringBuffer();
			if (packetAnnounce.getLength() >= 98) {
				AnnounceRequest announceR = AnnounceRequest.parse(packetAnnounce.getData());
				if(announceR.getAction().toString()=="ANNOUNCE") {
					if (announceR.getTransactionId()==myTracker.getTransactionID() && (announceR.getConnectionId()==myTracker.getConnectionID()||announceR.getConnectionId()==myTracker.getOldConnectionID()))
					bufferOut.append("Announce Request\n - Action: ");
					bufferOut.append(announceR.getAction());
					bufferOut.append("\n - TransactionID: ");
					bufferOut.append(announceR.getTransactionId());
					bufferOut.append("\n - ConnectionID: ");
					bufferOut.append(announceR.getConnectionId());
					bufferOut.append("\n - InfoHash: ");
					bufferOut.append(announceR.getInfoHash());
					bufferOut.append("\n - PeerID: ");
					bufferOut.append(announceR.getPeerId());
					bufferOut.append("\n - Downloaded: ");
					bufferOut.append(announceR.getDownloaded());
					bufferOut.append("\n - Left: ");
					bufferOut.append(announceR.getLeft());
					bufferOut.append("\n - Uploaded: ");
					bufferOut.append(announceR.getUploaded());
					bufferOut.append("\n - Event: ");
					bufferOut.append(announceR.getEvent());
					bufferOut.append("\n - Key: ");
					bufferOut.append(announceR.getKey());
					bufferOut.append("\n - NumWant: ");
					bufferOut.append(announceR.getNumWant());
					bufferOut.append("\n - IPAddress: ");
					bufferOut.append(announceR.getPeerInfo().getIpAddress());
					bufferOut.append("\n - Port: ");
					bufferOut.append(announceR.getPeerInfo().getPort());
					bufferOut.append("\n - Bytes: ");
					bufferOut.append(ByteUtils.toHexString(announceBytes));
				}
			}else {
				bufferOut.append("- ERROR: Response length to small ");
				bufferOut.append(packetAnnounce.getLength());
			}
			
			if(packetError.getLength() >= 8) {
				
			}else {
				bufferOut.append("- ERROR: Response length to small ");
				bufferOut.append(packetError.getLength());
			}
			
			if(myTracker.isMaster()) {
				
				Random random = new Random();
				long connectionID = random.nextLong();

				AnnounceResponse response = new AnnounceResponse();
				response.setTransactionId(myTracker.getTransactionID());
				response.setInterval(30);
				//sacar datos de la bd
				response.setLeechers();
				response.getSeeders();
				
				byte[] responseBytes = response.getBytes();			
				packetAnnounce = new DatagramPacket(responseBytes, responseBytes.length, peerIP, peerPort);
				udpSocket.send(packetAnnounce);

				bufferOut.append("\n\nConnect Response\n - Action: ");
				bufferOut.append(response.getAction());
				bufferOut.append("\n - TransactionID: ");
				bufferOut.append(response.getTransactionId());
				bufferOut.append("\n - Interval: ");
				bufferOut.append(response.getInterval());
				bufferOut.append("\n - Leechers: ");
				bufferOut.append(response.getLeechers());
				bufferOut.append("\n - Seeders: ");
				bufferOut.append(response.getSeeders());
				bufferOut.append("\n - Bytes: ");
				bufferOut.append(ByteUtils.toHexString(responseBytes));
				System.out.println(bufferOut.toString());
			}
		} catch (Exception ex) {
			System.err.println("Error: " + ex.getMessage());
		}
	}

}
