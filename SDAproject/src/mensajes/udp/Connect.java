package mensajes.udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Random;

import bitTorrent.util.ByteUtils;
import controller.TrackerController;
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
		StringBuffer bufferOut = new StringBuffer();
		try (MulticastSocket udpSocket = new MulticastSocket(55557)){
			udpSocket.joinGroup(InetAddress.getByName("230.0.0.1"));
			udpSocket.setSoTimeout(15000);

			byte[] requestBytes = new byte[16]; //16 bytes is the size of Connect Response Message
			DatagramPacket packet = new DatagramPacket(requestBytes, requestBytes.length);
			udpSocket.receive(packet);
			this.peerIP = packet.getAddress();
			this.peerPort = packet.getPort();
			System.out.println(peerIP);
			System.out.println(peerPort);
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

		} catch (Exception ex) {
			System.err.println("Error: " + ex.getMessage());
		}

		if(myTracker.isMaster()) {
			try (DatagramSocket udpDataSocket = new DatagramSocket()){
				Random random = new Random();
				long connectionID = Math.abs(random.nextLong());
				myTracker.setOldConnectionID(myTracker.getConnectionID());
				myTracker.setConnectionID(connectionID);

				ConnectResponse response = new ConnectResponse();
				response.setTransactionId(myTracker.getTransactionID());
				response.setConnectionId(connectionID);
				byte[] responseBytes = response.getBytes();	
				DatagramPacket packet = new DatagramPacket(responseBytes, responseBytes.length, peerIP, peerPort);
				udpDataSocket.send(packet);

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
