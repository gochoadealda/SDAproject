package mensajes.udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Random;

import bitTorrent.util.ByteUtils;
import controller.TrackerController;
import modelo.Tracker;
import udp.ConnectRequest;
import udp.ConnectResponse;

public class connect extends Thread{
	public static final String INFO_HASH = "1959A52BAD89DE0D6C5FA65B57C99D85AC642EF5";
	private TrackerController myTracker;
	private InetAddress peerIP;
	private int peerPort;
	public connect(Tracker myTracker) {
		super();
		this.myTracker = new TrackerController(myTracker);
	}

	@Override
	public void run() {
		super.run();

		try (DatagramSocket udpSocket = new DatagramSocket()){
			udpSocket.setSoTimeout(15000);

			byte[] requestBytes = new byte[16]; //16 bytes is the size of Connect Response Message
			DatagramPacket packet = new DatagramPacket(requestBytes, requestBytes.length);
			udpSocket.receive(packet);
			this.peerIP = packet.getAddress();
			this.peerPort = packet.getPort();
			System.out.println(peerIP);
			System.out.println(peerPort);
			StringBuffer bufferOut = new StringBuffer();
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
			} else {
				bufferOut.append("- ERROR: Response length to small ");
				bufferOut.append(packet.getLength());
			}

			System.out.println(bufferOut.toString());
			
			if(myTracker.isMaster()) {
				
				Random random = new Random();
				long connectionID = random.nextLong();
				myTracker.setOldConnectionID(myTracker.getConnectionID());
				myTracker.setConnectionID(connectionID);

				ConnectResponse response = new ConnectResponse();
				response.setTransactionId(myTracker.getTransactionID());
				response.setConnectionId(connectionID);
				byte[] responseBytes = response.getBytes();			
				packet = new DatagramPacket(responseBytes, responseBytes.length, peerIP, peerPort);
				udpSocket.send(packet);

				bufferOut.append("\n\nConnect Response\n - Action: ");
				bufferOut.append(response.getAction());
				bufferOut.append("\n - TransactionID: ");
				bufferOut.append(response.getTransactionId());
				bufferOut.append("\n - ConnectionID: ");
				bufferOut.append(response.getConnectionId());
				bufferOut.append("\n - Bytes: ");
				bufferOut.append(ByteUtils.toHexString(requestBytes));
				System.out.println(bufferOut.toString());
			}
		} catch (Exception ex) {
			System.err.println("Error: " + ex.getMessage());
		}
	}

}