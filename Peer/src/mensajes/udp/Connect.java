package mensajes.udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Random;

import udp.ConnectRequest;
import udp.ConnectResponse;
import bitTorrent.util.ByteUtils;
import controller.PeerController;
import controller.SwarmController;
import modelo.Peer;
import modelo.Swarm;

public class Connect extends Thread{
	public static final String TRACKER_NAME = "230.0.0.1";
	public static final int TRACKER_PORT = 55557;
	public static final String INFO_HASH = "1959A52BAD89DE0D6C5FA65B57C99D85AC642EF5";
	private SwarmController swarmController;
	private PeerController peerController;

	public Connect(Peer peerModel, Swarm swarmModel) {
		super();
		this.peerController = new PeerController(peerModel);
		this.swarmController = new SwarmController(swarmModel);
	}

	@Override
	public void run() {
		super.run();
		while(peerController.isActive()) {
			if(System.currentTimeMillis()-peerController.getLastconnection()>=60000 || peerController.isPrimerConnect()) {
				peerController.setLastconnection(System.currentTimeMillis());
				StringBuffer bufferOut = new StringBuffer();
				try (DatagramSocket udpSocket = new DatagramSocket()) {
					udpSocket.setSoTimeout(15000);

					InetAddress serverHost = InetAddress.getByName(TRACKER_NAME);
					Random random = new Random();
					int transactionID = random.nextInt(Integer.MAX_VALUE);

					peerController.setTransactionID(transactionID);
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
						peerController.setConnectionID(response.getConnectionId());
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
				if(peerController.isPrimerConnect()) {
					try {
						Thread.sleep(3000);
					}catch(Exception e) {

					}
					peerController.getModel().udpActions = new Actions(swarmController.getModel(), peerController.getModel());
					peerController.getModel().udpActions.start();
				}
				peerController.setPrimerConnect(false);
				
			}
		}
		peerController.getModel().udpConnect = null;
	}


}
