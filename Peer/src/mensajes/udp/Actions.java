package mensajes.udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import bitTorrent.util.ByteUtils;
import controller.PeerController;
import controller.SwarmController;
import modelo.Peer;
import modelo.Swarm;
import udp.AnnounceRequest;
import udp.AnnounceRequest.Event;
import udp.AnnounceResponse;
import udp.Error;

public class Actions extends Thread{
	public static final String TRACKER_NAME = "230.0.0.1";
	public static final int TRACKER_PORT = 60000;
	public static final String INFO_HASH = "1959A52BAD89DE0D6C5FA65B57C99D85AC642EF5";
	private SwarmController swarmController;
	private PeerController peerController;

	public Actions(Swarm swarmModel,Peer peerModel) {
		super();
		this.swarmController = new SwarmController(swarmModel);
		this.peerController = new PeerController(peerModel);
	}

	@Override
	public void run() {
		super.run();
		System.out.println(INFO_HASH.getBytes());
		while(peerController.isActive()) {
			if(System.currentTimeMillis()-peerController.getLastannounce()>=peerController.getInterval() || peerController.isPrimerAnnounce()) {
				peerController.setLastannounce(System.currentTimeMillis());
				StringBuffer bufferOut = new StringBuffer();
				try (DatagramSocket udpSocket = new DatagramSocket()) {
					udpSocket.setSoTimeout(15000);

					InetAddress serverHost = InetAddress.getByName(TRACKER_NAME);

					AnnounceRequest request = new AnnounceRequest();
					request.setTransactionId(peerController.getTransactionID());
					request.setConnectionId(peerController.getConnectionID());
					request.setInfoHash(INFO_HASH.getBytes());
					request.setPeerId(peerController.getPeerId());
					request.setDownloaded(peerController.getDownloaded());
					request.setLeft(peerController.getLeft());
					request.setUploaded(peerController.getUploaded());
					if(peerController.isPrimerAnnounce()) {
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
					peerController.setPrimerAnnounce(false);
					byte[] responseBytes = new byte[512]; //16 bytes is the size of Connect Response Message
					packet = new DatagramPacket(responseBytes, responseBytes.length);
					udpSocket.receive(packet);

					if (packet.getLength() >= 8) {
						AnnounceResponse response = AnnounceResponse.parse(packet.getData());
						if(response.getAction().toString().equals("ANNOUNCE") && response.getTransactionId()==peerController.getTransactionID()) {
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
							peerController.setInterval(response.getInterval());
							peerController.setSeeders(response.getSeeders());
							peerController.setLeechers(response.getLeechers());
							peerController.setSwarmPeers(response.getPeers());
						}else if(response.getAction().toString() == "ERROR" && response.getTransactionId()==peerController.getTransactionID()){
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
					peerController.getModel().udpConnect = null;
				}
			}
			try {
			Thread.sleep(500);
			}catch(Exception e) {
				
			}
		}
		peerController.getModel().udpConnect = null;
	}

}
