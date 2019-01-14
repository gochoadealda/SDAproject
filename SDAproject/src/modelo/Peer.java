package modelo;

public class Peer {
	
	private String idPeer;
	private String ip;
	private int puerto;
	private double bytesDes;
	private double bytesPen;
	private double bytesUp;
	private String idSwarm;
	private int event;
	
	public Peer(String idPeer, String ip, int puerto, double bytesDes, double bytesPen, double bytesUp, String idSwarm, int event) {
		super();
		this.idPeer = idPeer;
		this.ip = ip;
		this.puerto = puerto;
		this.bytesDes = bytesDes;
		this.bytesPen = bytesPen;
		this.bytesUp = bytesUp;
		this.idSwarm = idSwarm;
		this.event = event;
	}
	public String getID() {
		return idPeer;
	}
	public void setID(String iD) {
		idPeer = iD;
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
	public double getBytesDes() {
		return bytesDes;
	}
	public void setBytesDes(double bytesDes) {
		this.bytesDes = bytesDes;
	}
	public double getBytesPen() {
		return bytesPen;
	}
	public void setBytesPen(double bytesPen) {
		this.bytesPen = bytesPen;
	}
	public double getBytesUp() {
		return bytesUp;
	}
	public void setBytesUp(double bytesUp) {
		this.bytesUp = bytesUp;
	}
	public String getIdPeer() {
		return idPeer;
	}
	public void setIdPeer(String idPeer) {
		this.idPeer = idPeer;
	}
	public String getIdSwarm() {
		return idSwarm;
	}
	public void setIdSwarm(String idSwarm) {
		this.idSwarm = idSwarm;
	}
	public int getEvent() {
		return event;
	}
	public void setEvent(int event) {
		this.event = event;
	}
	
}