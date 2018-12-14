package modelo;

public class Peer {
	
	private int idPeer;
	private String ip;
	private int puerto;
	private double bytesDes;
	private double bytesPen;
	private double bytesUp;
	private int idSwarm;
	
	public Peer(int idPeer, String ip, int puerto, double bytesDes, double bytesPen, double bytesUp, int idSwarm) {
		super();
		this.idPeer = idPeer;
		this.ip = ip;
		this.puerto = puerto;
		this.bytesDes = bytesDes;
		this.bytesPen = bytesPen;
		this.bytesUp = bytesUp;
		this.idSwarm = idSwarm;
	}
	public int getID() {
		return idPeer;
	}
	public void setID(int iD) {
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
	public int getIdPeer() {
		return idPeer;
	}
	public void setIdPeer(int idPeer) {
		this.idPeer = idPeer;
	}
	public int getIdSwarm() {
		return idSwarm;
	}
	public void setIdSwarm(int idSwarm) {
		this.idSwarm = idSwarm;
	}
}