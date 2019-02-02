package modelo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import bitTorrent.metainfo.handler.MetainfoHandler;
import bitTorrent.metainfo.handler.MetainfoHandlerMultipleFile;
import bitTorrent.metainfo.handler.MetainfoHandlerSingleFile;
import mensajes.udp.Actions;
import mensajes.udp.Connect;
import udp.PeerInfo;

public class Peer {

	private String peerId;
	private int ip;
	private int puerto;
	private int uploaded; // The total amount uploaded so far, encoded in base ten ascii.
	private int downloaded; // The total amount downloaded so far, encoded in base ten ascii.
	private int left; // The number of bytes this peer still has to download, encoded in base ten
						// ascii.
	private int event; // 0: none; 1: completed; 2: started; 3: stopped
	private int transactionID;
	private long connectionID;
	public Connect udpConnect;
	public Actions udpActions;
	private long lastconnection;
	private long lastannounce;
	private boolean active;
	private byte[] infoHash;
	private int seeders;
	private int leechers;
	private int interval;
	private List<PeerInfo> swarmPeers;
	private boolean primerConnect;
	private boolean primerAnnounce;
	private Swarm swarm;

	public Peer() {
		super();
		swarmPeers = new ArrayList<>();
	}

	public Peer(String peerId, int ip, int puerto, int uploaded, int downloaded, int left) {
		super();
		this.peerId = peerId;
		this.ip = ip;
		this.puerto = puerto;
		this.uploaded = uploaded;
		this.downloaded = downloaded;
		this.left = left;
		this.interval = 0;
	}

	public Peer(String peerId, int ip, int puerto, int uploaded, int downloaded, int left, int event) {
		super();
		this.peerId = peerId;
		this.ip = ip;
		this.puerto = puerto;
		this.uploaded = uploaded;
		this.downloaded = downloaded;
		this.left = left;
		this.event = event;
		this.interval = 0;
	}

	public Peer(String peerId) {
		super();
		this.peerId = peerId;
		this.interval = 60000;
	}

	public void start() {
		this.active = true;
		this.primerConnect = true;
		this.primerAnnounce = true;
		udpConnect = new Connect(this, swarm);
		udpConnect.start();
	}

	public void leerTorrent(File torrent) {
		MetainfoHandler<?> handler = null;
		try {
			if (torrent.getPath().contains(".torrent")) {
				handler = new MetainfoHandlerSingleFile();
				handler.parseTorrenFile(torrent.getPath());
			}
		} catch (Exception ex) {
			System.out.println(" Peer: Couldn't parse file as single, trying multiple");
			if (torrent.getPath().contains(".torrent")) {
				handler = new MetainfoHandlerMultipleFile();
				handler.parseTorrenFile(torrent.getPath());
			}
		}
		if (handler != null) {
			System.out.println("#######################################\n" + torrent.getPath());
			System.out.println(handler.getMetainfo());
			int tamaño = handler.getMetainfo().getInfo().getLength();
			File newFile = new File("downloads/" + handler.getMetainfo().getInfo().getName());
			if (!newFile.exists()) {
				System.out.println("El archivo no existe por lo que guardamos la memoria necesaria, que es " + tamaño);
				try {
					FileOutputStream fos = new FileOutputStream(newFile);
					fos.write(new byte[tamaño]);
					fos.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				System.out.println("Existe, por lo que lo leemos");
				left = 0;
				try {
					FileInputStream fis = new FileInputStream(newFile);
					fis.read(new byte[(int) newFile.length()]);
					fis.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

	}

	public String getPeerId() {
		return peerId;
	}

	public void setPeerId(String peerId) {
		this.peerId = peerId;
	}

	public int getIp() {
		return ip;
	}

	public void setIp(int ip) {
		this.ip = ip;
	}

	public int getPuerto() {
		return puerto;
	}

	public void setPuerto(int puerto) {
		this.puerto = puerto;
	}

	public int getUploaded() {
		return uploaded;
	}

	public void setUploaded(int uploaded) {
		this.uploaded = uploaded;
	}

	public int getLeft() {
		return left;
	}

	public void setLeft(int left) {
		this.left = left;
	}

	public int getDownloaded() {
		return downloaded;
	}

	public void setDownloaded(int downloaded) {
		this.downloaded = downloaded;
	}

	public int getEvent() {
		return event;
	}

	public void setEvent(int event) {
		this.event = event;
	}

	public int getTransactionID() {
		return transactionID;
	}

	public void setTransactionID(int transactionID) {
		this.transactionID = transactionID;
	}

	public long getConnectionID() {
		return connectionID;
	}

	public void setConnectionID(long connectionID) {
		this.connectionID = connectionID;
	}

	public long getLastconnection() {
		return lastconnection;
	}

	public void setLastconnection(long lastconnection) {
		this.lastconnection = lastconnection;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public long getLastannounce() {
		return lastannounce;
	}

	public void setLastannounce(long lastannounce) {
		this.lastannounce = lastannounce;
	}

	public byte[] getInfoHash() {
		return infoHash;
	}

	public void setInfoHash(byte[] infoHash) {
		this.infoHash = infoHash;
	}

	public int getSeeders() {
		return seeders;
	}

	public void setSeeders(int seeders) {
		this.seeders = seeders;
	}

	public int getLeechers() {
		return leechers;
	}

	public void setLeechers(int leechers) {
		this.leechers = leechers;
	}

	public int getInterval() {
		return interval;
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}

	public List<PeerInfo> getSwarmPeers() {
		return swarmPeers;
	}

	public void setSwarmPeers(List<PeerInfo> swarmPeers) {
		this.swarmPeers = swarmPeers;
	}

	public boolean isPrimerConnect() {
		return primerConnect;
	}

	public void setPrimerConnect(boolean primerConnect) {
		this.primerConnect = primerConnect;
	}

	public boolean isPrimerAnnounce() {
		return primerAnnounce;
	}

	public void setPrimerAnnounce(boolean primerAnnounce) {
		this.primerAnnounce = primerAnnounce;
	}
	
}
