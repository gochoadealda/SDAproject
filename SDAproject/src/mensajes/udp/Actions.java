package mensajes.udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;
import java.util.Random;

import bitTorrent.util.ByteUtils;
import controller.PeerController;
import controller.SwarmController;
import controller.TrackerController;
import mensajes.topic.ReadyPublisher;
import mensajes.topic.ReadySubscriber;
import modelo.Peer;
import modelo.Swarm;
import modelo.Tracker;
import udp.AnnounceRequest;
import udp.AnnounceResponse;
import udp.ConnectRequest;
import udp.ConnectResponse;
import udp.Error;

public class Actions extends Thread{
	private TrackerController myTracker;
	private InetAddress peerIP;
	private int peerPort;
	public Actions(Tracker myTracker) {
		super();
		this.myTracker = new TrackerController(myTracker);
	}

	@Override
	public void run() {
		super.run();
		while(myTracker.isActive()) {
			StringBuffer bufferOut = new StringBuffer();
			String SwarmId = null;
			try (MulticastSocket udpSocket = new MulticastSocket(60000)){
				udpSocket.joinGroup(InetAddress.getByName("230.0.0.1"));
				//udpSocket.setSoTimeout(15000);

				byte[] announceBytes = new byte[98];
				DatagramPacket packetAnnounce = new DatagramPacket(announceBytes, announceBytes.length);
				udpSocket.receive(packetAnnounce);
				this.peerIP = packetAnnounce.getAddress();
				this.peerPort = packetAnnounce.getPort();
				System.out.println("Llega1");
				if (packetAnnounce.getLength() >= 98) {
					System.out.println("Llega2");
					AnnounceRequest announceR = AnnounceRequest.parse(packetAnnounce.getData());
					if(announceR.getAction().toString()=="ANNOUNCE") {
						System.out.println(peerIP.getHostAddress());
						System.out.println(announceR.getTransactionId() + " " + myTracker.getTransactionID());
						System.out.println(announceR.getConnectionId() + " " + myTracker.getConnectionIDs().get(peerIP.getHostAddress()));
						System.out.println(announceR.getConnectionId() + " " + myTracker.getOldConnectionIDs().get(peerIP.getHostAddress()));
						if (announceR.getTransactionId()==myTracker.getTransactionID() && (announceR.getConnectionId()==myTracker.getConnectionIDs().get(peerIP.getHostAddress())||announceR.getConnectionId()==myTracker.getOldConnectionIDs().get(peerIP.getHostAddress()))) {
							System.out.println("Entra");
							bufferOut.append("Announce Request\n - Action: ");
							bufferOut.append(announceR.getAction());
							bufferOut.append("\n - TransactionID: ");
							bufferOut.append(announceR.getTransactionId());
							bufferOut.append("\n - ConnectionID: ");
							bufferOut.append(announceR.getConnectionId());
							bufferOut.append("\n - InfoHash: ");
							String info_hash = new String(announceR.getInfoHash());
							bufferOut.append(info_hash);
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

							Peer peer = new Peer(announceR.getPeerId(), 
									ByteUtils.intToIpAddress(announceR.getPeerInfo().getIpAddress()), 
									announceR.getPeerInfo().getPort(), announceR.getDownloaded(), announceR.getLeft(), announceR.getUploaded(), info_hash, announceR.getEvent().value());
							PeerController peerController = new PeerController(peer);
							//SwarmId = ByteUtils.arrayToInt(announceR.getInfoHash());
							SwarmId = info_hash;//he modificado de int a hexString porque hay que pasar String
							myTracker.setPeer(peerController.getModel());

							Swarm swarm = new Swarm(info_hash);
							SwarmController swarmController = new SwarmController(swarm);

							myTracker.setSwarm(swarmController.getModel());

							//TODO Metodo añadir a Peer a BD
							if(myTracker.getTrackerList().size() >1) {
								if(myTracker.isMaster()) {
									myTracker.getModel().readySend = new ReadyPublisher(myTracker.getModel());
									myTracker.getModel().readySend.start();
								}else {
									myTracker.getModel().readyRecieve = new ReadySubscriber(myTracker.getModel());
									myTracker.getModel().readyRecieve.start();
								}
								myTracker.setMulticast(true);
							}else {
								int event = myTracker.getModel().getPeer().getEvent();
								System.out.println(event);
								if(event == 2) {
									System.out.println("Event2");
									Swarm s = new Swarm("0");
									try {
										s = myTracker.getModel().getTrackerDB().selectSwarm(myTracker.getModel().getPeer().getIdSwarm());
									} catch (NullPointerException e) {
										//Esta bien el null
									}
									if(s == null) {
										Swarm sw = new Swarm(myTracker.getModel().getPeer().getIdSwarm());
										System.out.println(sw.getNomCont());
										myTracker.getModel().getTrackerDB().insertS(sw);
										myTracker.getModel().getTrackerDB().insertP(myTracker.getModel().getPeer());
									}else {
										Swarm swa = myTracker.getModel().getTrackerDB().selectSwarm(myTracker.getModel().getPeer().getIdSwarm());
										swa.setLeechers(swa.getLeechers()+1);
										myTracker.getModel().getTrackerDB().updateS(swa);
										myTracker.getModel().getTrackerDB().insertP(myTracker.getModel().getPeer());
									}
								}else if(event == 0) {
									System.out.println("Event0");
									myTracker.getModel().getTrackerDB().updateP(myTracker.getModel().getPeer());
								}else if(event == 1) {
									System.out.println("Event1");
									Swarm s = myTracker.getModel().getTrackerDB().selectSwarm(myTracker.getModel().getPeer().getIdSwarm());
									s.setSeeders(s.getSeeders()+1);
									s.setLeechers(s.getLeechers()-1);
									myTracker.getModel().getTrackerDB().updateS(s);
									myTracker.getModel().getTrackerDB().updateP(myTracker.getModel().getPeer());
								}else if(event == 3) {
									System.out.println("Event3");
									myTracker.getModel().getTrackerDB().deleteP(myTracker.getModel().getPeer().getID());
								}
							}
						}
					}
				}else {
					bufferOut.append("- ERROR: Response length to small ");
					bufferOut.append(packetAnnounce.getLength());
				}

				if(packetAnnounce.getLength() >= 8) {
					Error announceR = Error.parse(packetAnnounce.getData());
					if (announceR.getAction().toString()=="ERROR");{
						System.out.println(announceR.getMessage());
					}
				}else {
					bufferOut.append("- ERROR: Response length to small ");
					bufferOut.append(packetAnnounce.getLength());
				}
			} catch (Exception ex) {
				System.err.println("Error1: " + ex.getMessage());
				ex.printStackTrace();
				System.out.println(ex.toString());
			}

			if(myTracker.isMaster()) {

				AnnounceResponse response = new AnnounceResponse();
				response.setTransactionId(myTracker.getTransactionID());
				response.setInterval(30000);
				//sacar datos de la bd
				//TODO sacar datos de la bd
				Swarm mySwarm = myTracker.getTrackerDB().selectSwarm(SwarmId);
				response.setLeechers(mySwarm.getLeechers());
				response.setSeeders(mySwarm.getSeeders());
				response.setPeers(myTracker.getTrackerDB().selectPeersFromSwarm(SwarmId));

				try (DatagramSocket udpDataSocket = new DatagramSocket()){
					byte[] responseBytes = response.getBytes();			
					DatagramPacket packetResponse = new DatagramPacket(responseBytes, responseBytes.length, peerIP, peerPort);
					udpDataSocket.send(packetResponse);

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
				}catch (Exception e) {
					System.err.println("Error2: " + e.getMessage());
				}
			}
		}
	}
}
