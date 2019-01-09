package mensajes.udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.util.Random;

import udp.ConnectRequest;
import udp.ConnectResponse;
import bitTorrent.util.ByteUtils;
import modelo.Peer;

public class Connect extends Thread{
	public static final String TRACKER_NAME = "230.0.0.1";
	public static final int TRACKER_PORT = 55557;
	public static final String INFO_HASH = "1959A52BAD89DE0D6C5FA65B57C99D85AC642EF5";
	private Peer myPeer;

	public Connect(Peer myPeer) {
		super();
		this.myPeer = myPeer;
	}

	@Override
	public void run() {
		super.run();
		while(myPeer.isActive()) {
			if(System.currentTimeMillis()-myPeer.getLastconnection()>=60000 || myPeer.isPrimerConnect()) {
				myPeer.setLastconnection(System.currentTimeMillis());
				StringBuffer bufferOut = new StringBuffer();
				try (DatagramSocket udpSocket = new DatagramSocket()) {
					udpSocket.setSoTimeout(15000);

					InetAddress serverHost = InetAddress.getByName(TRACKER_NAME);
					Random random = new Random();
					int transactionID = random.nextInt(Integer.MAX_VALUE);

					myPeer.setTransactionID(transactionID);
					ConnectRequest request = new ConnectRequest();
					request.setTransactionId(transactionID);
					byte[] requestBytes = request.getBytes();			
					DatagramPacket packet = new DatagramPacket(requestBytes, requestBytes.length, serverHost, TRACKER_PORT);
					udpSocket.send(packet);
					System.out.println(udpSocket.getLocalAddress());
					bufferOut.append("Connect Request\n - Action: ");
					bufferOut.append(request.getAction());
					bufferOut.append("\n - TransactionID: ");
					bufferOut.append(request.getTransactionId());
					bufferOut.append("\n - ConnectionID: ");
					bufferOut.append(request.getConnectionId());
					bufferOut.append("\n - Bytes: ");
					bufferOut.append(ByteUtils.toHexString(requestBytes));

					byte[] responseBytes = new byte[16]; //16 bytes is the size of Connect Response Message
					packet = new DatagramPacket(responseBytes, responseBytes.length);
					udpSocket.receive(packet);

					if (packet.getLength() >= 16) {
						ConnectResponse response = ConnectResponse.parse(packet.getData());
						bufferOut.append("\n\nConnect Response\n - Action: ");
						bufferOut.append(response.getAction());
						bufferOut.append("\n - TransactionID: ");
						bufferOut.append(response.getTransactionId());
						bufferOut.append("\n - ConnectionID: ");
						bufferOut.append(response.getConnectionId());
						bufferOut.append("\n - Bytes: ");
						bufferOut.append(ByteUtils.toHexString(responseBytes));
						myPeer.setConnectionID(response.getConnectionId());
					} else {
						bufferOut.append("- ERROR: Response length to small ");
						bufferOut.append(packet.getLength());
					}
					System.out.println(packet.getAddress());
					System.out.println(packet.getPort());
					System.out.println(bufferOut.toString());

				}catch (Exception e) {
					System.err.println("ErrorCon: " + e.getMessage());
				}
				if(myPeer.isPrimerConnect()) {
					myPeer.udpActions = new Actions(myPeer);
					myPeer.udpActions.start();
				}
				myPeer.setPrimerConnect(false);
				try {
					Thread.sleep(500);
				}catch(Exception e) {

				}
			}
		}
		myPeer.udpConnect = null;
	}


}
