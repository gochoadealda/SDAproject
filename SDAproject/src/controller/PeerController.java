package controller;

import modelo.Peer;
import modelo.Tracker;

public class PeerController {
	private Peer model;

	public PeerController(Peer model) {
		super();
		this.model = model;
	}
	
	public Peer getModel() {
		return model;
	}

	public void setModel(Peer model) {
		this.model = model;
	}
	
	public String getID() {
		return model.getID();
	}
	public void setID(String iD) {
		model.setID(iD);
	}
	public String getIp() {
		return model.getIp();
	}
	public void setIp(String ip) {
		model.setIp(ip);
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
	public double getBytesUp() {
		return model.getBytesUp();
	}
	public void setBytesUp(double bytesUp) {
		model.getBytesUp();
	}
}
