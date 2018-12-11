package mensajes.udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Random;

import udp.ConnectRequest;
import udp.ConnectResponse;
import bitTorrent.util.ByteUtils;

public class Connect extends Thread{
	public static final String TRACKER_NAME = "230.0.0.1";
	public static final int TRACKER_PORT = 55557;
	public static final String INFO_HASH = "1959A52BAD89DE0D6C5FA65B57C99D85AC642EF5";
	@Override
	public void run() {
		super.run();
		StringBuffer bufferOut = new StringBuffer();
		try (MulticastSocket udpSocket = new MulticastSocket()) {
			udpSocket.setSoTimeout(15000);
			InetAddress serverHost = InetAddress.getByName(TRACKER_NAME);

			Random random = new Random();
			int transactionID = random.nextInt(Integer.MAX_VALUE);

			ConnectRequest request = new ConnectRequest();
			request.setTransactionId(transactionID);
			byte[] requestBytes = request.getBytes();			
			DatagramPacket packet = new DatagramPacket(requestBytes, requestBytes.length, serverHost, TRACKER_PORT);
			udpSocket.send(packet);

			bufferOut.append("Connect Request\n - Action: ");
			bufferOut.append(request.getAction());
			bufferOut.append("\n - TransactionID: ");
			bufferOut.append(request.getTransactionId());
			bufferOut.append("\n - ConnectionID: ");
			bufferOut.append(request.getConnectionId());
			bufferOut.append("\n - Bytes: ");
			bufferOut.append(ByteUtils.toHexString(requestBytes));
		} catch (Exception ex) {
			System.err.println("Error: " + ex.getMessage());
		}
		
		try (DatagramSocket udpDataSocket = new DatagramSocket()){
			udpDataSocket.setSoTimeout(15000);
			byte[] responseBytes = new byte[16]; //16 bytes is the size of Connect Response Message
			DatagramPacket packet = new DatagramPacket(responseBytes, responseBytes.length);
			udpDataSocket.receive(packet);

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
			} else {
				bufferOut.append("- ERROR: Response length to small ");
				bufferOut.append(packet.getLength());
			}
			System.out.println(packet.getAddress());
			System.out.println(packet.getPort());
			System.out.println(bufferOut.toString());
		}catch (Exception e) {
			// TODO: handle exception
		}
	}


}
