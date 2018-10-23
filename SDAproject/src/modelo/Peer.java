package modelo;

public class Peer {
	
	private int idPeer;
	private String ip;
	private int puerto;
	private double bytesDes;
	private double bytesPen;
	public Peer(int idPeer, String ip, int puerto, double bytesDes, double bytesPen) {
		super();
		this.idPeer = idPeer;
		this.ip = ip;
		this.puerto = puerto;
		this.bytesDes = bytesDes;
		this.bytesPen = bytesPen;
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
	
	
}
