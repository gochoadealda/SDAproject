package controller;

import modelo.Peer;

public class PeerController {
	private Peer model;

	public PeerController(Peer model) {
		super();
		this.model = model;
	}
	
	public String getPeerId() {
		return model.getPeerId();
	}
	public void setPeerId(String peerId) {
		model.setPeerId(peerId);
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
	public float getUploaded() {
		return model.getUploaded();
	}
	public void setUploaded(float uploaded) {
		model.setUploaded(uploaded);
	}
	public float getLeft() {
		return model.getLeft();
	}
	public void setLeft(float left) {
		model.setLeft(left);
	}
	public float getDownloaded() {
		return model.getDownloaded();
	}
	public void setDownloaded(float downloaded) {
		model.setDownloaded(downloaded);
	}
	public String getEvent() {
		return model.getEvent();
	}
	public void setEvent(String event) {
		model.setEvent(event);
	}
}