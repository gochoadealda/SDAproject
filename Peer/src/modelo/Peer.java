package modelo;

import mensajes.udp.Actions;
import mensajes.udp.Connect;

public class Peer {

	private String peerId;
	private int ip;
	private int puerto;
	private int uploaded;  	//The total amount uploaded so far, encoded in base ten ascii.
	private int downloaded;	//The total amount downloaded so far, encoded in base ten ascii.
	private int left;			//The number of bytes this peer still has to download, encoded in base ten ascii.
	private int event;		// 0: none; 1: completed; 2: started; 3: stopped
	private int transactionID;
	private long connectionID;
	public Connect udpConnect;
	public Actions udpActions;
	private long lastconnection;
	private boolean active;

	public Peer() {
		super();
	}

	public Peer(String peerId, int ip, int puerto, int uploaded, int downloaded, int left) {
		super();
		this.peerId = peerId;
		this.ip = ip;
		this.puerto = puerto;
		this.uploaded = uploaded;
		this.downloaded = downloaded;
		this.left = left;
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
	}
	public void start() {
		udpConnect = new Connect(this);
		udpConnect.start();
		while(true) {
			if(System.currentTimeMillis()-lastconnection >= 60000) {
				udpConnect = new Connect(this);
				udpConnect.start();
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
	


}
