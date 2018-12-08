package modelo;

public class Peer {

	private String peerId;
	private String ip;
	private int puerto;
	private float uploaded;  	//The total amount uploaded so far, encoded in base ten ascii.
	private float downloaded;	//The total amount downloaded so far, encoded in base ten ascii.
	private float left;			//The number of bytes this peer still has to download, encoded in base ten ascii.
	private String event;		//This is an optional key which maps to started, completed, or stopped
	
	public Peer(String peerId, String ip, int puerto, float uploaded, float downloaded, float left) {
		super();
		this.peerId = peerId;
		this.ip = ip;
		this.puerto = puerto;
		this.uploaded = uploaded;
		this.downloaded = downloaded;
		this.left = left;
	}
	
	public Peer(String peerId, String ip, int puerto, float uploaded, float downloaded, float left, String event) {
		super();
		this.peerId = peerId;
		this.ip = ip;
		this.puerto = puerto;
		this.uploaded = uploaded;
		this.downloaded = downloaded;
		this.left = left;
		this.event = event;
	}
	public String getPeerId() {
		return peerId;
	}
	public void setPeerId(String peerId) {
		this.peerId = peerId;
	}
	
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	
	public int getPuerto() {
		return puerto;
	}
	public void setPuerto(int puerto) {
		this.puerto = puerto;
	}
	
	public float getUploaded() {
		return uploaded;
	}
	public void setUploaded(float uploaded) {
		this.uploaded = uploaded;
	}
	
	public float getLeft() {
		return left;
	}
	public void setLeft(float left) {
		this.left = left;
	}

	public float getDownloaded() {
		return downloaded;
	}
	public void setDownloaded(float downloaded) {
		this.downloaded = downloaded;
	}

	public String getEvent() {
		return event;
	}
	public void setEvent(String event) {
		this.event = event;
	}
}
