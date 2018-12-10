package controller;

import modelo.Peer;

public class PeerController {
	private Peer model;

	public PeerController(Peer model) {
		super();
		this.model = model;
	}
	
	public int getID() {
		return model.getID();
	}
	public void setID(int iD) {
		model.setID(iD);
	}
	public String getIp() {
		return model.getIp();
	}
	public void setIp(String ip) {
		model.setIp(ip);;
	}
	public int getPuerto() {
		return model.getPuerto();
	}
	public void setPuerto(int puerto) {
		model.setPuerto(puerto);
	}
	public double getBytesDes() {
		return model.getBytesDes();
	}
	public void setBytesDes(double bytesDes) {
		model.setBytesDes(bytesDes);;
	}
	public double getBytesPen() {
		return model.getBytesPen();
	}
	public void setBytesPen(double bytesPen) {
		model.setBytesPen(bytesPen);;
	}
}
