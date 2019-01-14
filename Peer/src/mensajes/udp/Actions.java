package mensajes.udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import bitTorrent.util.ByteUtils;
import modelo.Peer;
import udp.AnnounceRequest;
import udp.AnnounceRequest.Event;
import udp.AnnounceResponse;
import udp.Error;

public class Actions extends Thread{
	public static final String TRACKER_NAME = "230.0.0.1";
	public static final int TRACKER_PORT = 60000;
	public static final String INFO_HASH = "1959A52BAD89DE0D6C5FA65B57C99D85AC642EF5";
	private Peer myPeer;

	public Actions(Peer myPeer) {
		super();
		this.myPeer = myPeer;
	}

	@Override
	public void run() {
		super.run();
		System.out.println(INFO_HASH.getBytes());
		while(myPeer.isActive()) {
			if(System.currentTimeMillis()-myPeer.getLastannounce()>=myPeer.getInterval() || myPeer.isPrimerAnnounce()) {
				myPeer.setLastannounce(System.currentTimeMillis());
				StringBuffer bufferOut = new StringBuffer();
				try (DatagramSocket udpSocket = new DatagramSocket()) {
					udpSocket.setSoTimeout(15000);

					InetAddress serverHost = InetAddress.getByName(TRACKER_NAME);

					AnnounceRequest request = new AnnounceRequest();
					request.setTransactionId(myPeer.getTransactionID());
					request.setConnectionId(myPeer.getConnectionID());
					request.setInfoHash(INFO_HASH.getBytes());
					request.setPeerId(myPeer.getPeerId());
					request.setDownloaded(myPeer.getDownloaded());
					request.setLeft(myPeer.getLeft());
					request.setUploaded(myPeer.getUploaded());
					if(myPeer.isPrimerAnnounce()) {
						request.setEvent(Event.parseInt(2));
					}else {
						request.setEvent(Event.parseInt(0));
					}
					request.setKey(0);
					request.setNumWant(2);
					byte[] requestBytes = request.getBytes();			
					DatagramPacket packet = new DatagramPacket(requestBytes, requestBytes.length, serverHost, TRACKER_PORT);
					udpSocket.send(packet);
					bufferOut.append("Announce Request\n - Action: ");
					bufferOut.append(request.getAction());
					bufferOut.append("\n - TransactionID: ");
					bufferOut.append(request.getTransactionId());
					bufferOut.append("\n - ConnectionID: ");
					bufferOut.append(request.getConnectionId());
					bufferOut.append("\n - InfoHash: ");
					bufferOut.append(ByteUtils.arrayToInt(request.getInfoHash()));
					bufferOut.append("\n - PeerID: ");
					bufferOut.append(request.getPeerId());
					bufferOut.append("\n - Downloaded: ");
					bufferOut.append(request.getDownloaded());
					bufferOut.append("\n - Left: ");
					bufferOut.append(request.getLeft());
					bufferOut.append("\n - Uploaded: ");
					bufferOut.append(request.getUploaded());
					bufferOut.append("\n - Event: ");
					bufferOut.append(request.getEvent());
					bufferOut.append("\n - Key: ");
					bufferOut.append(request.getKey());
					bufferOut.append("\n - NumWant: ");
					bufferOut.append(request.getNumWant());
					bufferOut.append("\n - IPAddress: ");
					bufferOut.append(request.getPeerInfo().getIpAddress());
					bufferOut.append("\n - Port: ");
					bufferOut.append(request.getPeerInfo().getPort());
					bufferOut.append("\n - Bytes: ");
					bufferOut.append(ByteUtils.toHexString(requestBytes));
					myPeer.setPrimerAnnounce(false);
					byte[] responseBytes = new byte[512]; //16 bytes is the size of Connect Response Message
					packet = new DatagramPacket(responseBytes, responseBytes.length);
					udpSocket.receive(packet);

					if (packet.getLength() >= 8) {
						AnnounceResponse response = AnnounceResponse.parse(packet.getData());
						if(response.getAction().toString() == "ANNOUNCE" && response.getTransactionId()==myPeer.getTransactionID()) {
							bufferOut.append("\n\nAnnounce Response\n - Action: ");
							bufferOut.append(response.getAction());
							bufferOut.append("\n - TransactionID: ");
							bufferOut.append(response.getTransactionId());
							bufferOut.append("\n - Interval: ");
							bufferOut.append(response.getInterval());
							bufferOut.append("\n - Seeders: ");
							bufferOut.append(response.getSeeders());
							bufferOut.append("\n - Leechers: ");
							bufferOut.append(response.getLeechers());
							bufferOut.append("\n - PeerInfo: ");
							bufferOut.append(response.getPeers());
							bufferOut.append("\n - Bytes: ");
							bufferOut.append(ByteUtils.toHexString(responseBytes));
							myPeer.setInterval(response.getInterval());
							myPeer.setSeeders(response.getSeeders());
							myPeer.setLeechers(response.getLeechers());
							myPeer.setSwarmPeers(response.getPeers());
						}else if(response.getAction().toString() == "ERROR" && response.getTransactionId()==myPeer.getTransactionID()){
							Error error = Error.parse(packet.getData());
							bufferOut.append("\n\nAnnounce Response\n - Action: ");
							bufferOut.append(error.getAction());
							bufferOut.append("\n - TransactionID: ");
							bufferOut.append(error.getTransactionId());
							bufferOut.append("\n - Message: ");
							bufferOut.append(error.getMessage());
						}
					} else {
						bufferOut.append("- ERROR: Response length to small ");
						bufferOut.append(packet.getLength());
					}
					System.out.println(packet.getAddress());
					System.out.println(packet.getPort());
					System.out.println(bufferOut.toString());
				}catch (Exception e) {
					System.err.println("ErrorAnn: " + e.getMessage());
					e.printStackTrace();
					myPeer.udpConnect = null;
				}
			}
			try {
			Thread.sleep(500);
			}catch(Exception e) {
				
			}
		}
		myPeer.udpConnect = null;
	}

}
