package mensajes.udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import bitTorrent.util.ByteUtils;
import controller.TrackerController;
import mensajes.topic.ConnectionIDPublisher;
import modelo.Tracker;
import udp.ConnectRequest;
import udp.ConnectResponse;

public class Connect extends Thread{
	public static final String INFO_HASH = "1959A52BAD89DE0D6C5FA65B57C99D85AC642EF5";
	private TrackerController myTracker;
	private InetAddress peerIP;
	private int peerPort;
	public Connect(Tracker myTracker) {
		super();
		this.myTracker = new TrackerController(myTracker);
	}

	@Override
	public void run() {
		super.run();
		while(myTracker.isActive()) {
			StringBuffer bufferOut = new StringBuffer();
			try {
				MulticastSocket udpSocket = new MulticastSocket(55557);
				udpSocket.joinGroup(InetAddress.getByName("230.0.0.1"));
				//udpSocket.setSoTimeout(15000);

				byte[] requestBytes = new byte[16]; //16 bytes is the size of Connect Response Message
				DatagramPacket packet = new DatagramPacket(requestBytes, requestBytes.length);
				udpSocket.receive(packet);
				this.peerIP = packet.getAddress();
				this.peerPort = packet.getPort();
				if (packet.getLength() >= 16) {
					ConnectRequest request = ConnectRequest.parse(packet.getData());
					bufferOut.append("Connect Request\n - Action: ");
					bufferOut.append(request.getAction());
					bufferOut.append("\n - TransactionID: ");
					bufferOut.append(request.getTransactionId());
					bufferOut.append("\n - ConnectionID: ");
					bufferOut.append(request.getConnectionId());
					bufferOut.append("\n - Bytes: ");
					bufferOut.append(ByteUtils.toHexString(requestBytes));
					myTracker.setTransactionID(request.getTransactionId());
					HashMap<String, Integer> transactionIDs = myTracker.getTransactionIDs();
					transactionIDs.put(peerIP.getHostAddress(), request.getTransactionId());
					myTracker.setTransactionIDs(transactionIDs);
				} else {
					bufferOut.append("- ERROR: Response length to small ");
					bufferOut.append(packet.getLength());
				}

				System.out.println(bufferOut.toString());
				Thread.sleep(500);
			} catch (Exception ex) {
				System.err.println("Error: " + ex.getMessage());
			}

			if(myTracker.isMaster()) {
				try (DatagramSocket udpDataSocket = new DatagramSocket()){
					Random random = new Random();
					long connectionID = Math.abs(random.nextLong());

					HashMap<String, Long> oldConnectionIDs = myTracker.getOldConnectionIDs();
					HashMap<String, Long> connectionIDs = myTracker.getConnectionIDs();
					System.out.println(peerIP.getHostAddress());
					if(connectionIDs.get(peerIP.getHostAddress()) == null) {
						long l = 0;
						oldConnectionIDs.put(peerIP.getHostAddress(), l);
					}else {
						oldConnectionIDs.put(peerIP.getHostAddress(), connectionIDs.get(peerIP.getHostAddress()));
					}
					
					connectionIDs.put(peerIP.getHostAddress(), connectionID);
					myTracker.getModel().conSend = new ConnectionIDPublisher(peerIP.getHostAddress(), connectionID, myTracker, false);
					myTracker.getModel().conSend.start();
					myTracker.setOldConnectionIDs(oldConnectionIDs);
					myTracker.setConnectionIDs(connectionIDs);
					
					myTracker.setOldConnectionID(myTracker.getConnectionID());
					myTracker.setConnectionID(connectionID);

					ConnectResponse response = new ConnectResponse();
					response.setTransactionId(myTracker.getTransactionID());
					response.setConnectionId(connectionID);
					byte[] responseBytes = response.getBytes();	
					DatagramPacket datapacket = new DatagramPacket(responseBytes, responseBytes.length, peerIP, peerPort);
					udpDataSocket.send(datapacket);

					bufferOut.append("\n\nConnect Response\n - Action: ");
					bufferOut.append(response.getAction());
					bufferOut.append("\n - TransactionID: ");
					bufferOut.append(response.getTransactionId());
					bufferOut.append("\n - ConnectionID: ");
					bufferOut.append(response.getConnectionId());
					bufferOut.append("\n - Bytes: ");
					bufferOut.append(ByteUtils.toHexString(responseBytes));
					System.out.println(bufferOut.toString());
				}catch (Exception e) {
					System.err.println("Error: " + e.getMessage());
				}
			}
		}
	}

}
