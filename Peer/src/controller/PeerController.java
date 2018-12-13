package controller;


import modelo.Peer;

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
	
	public String getPeerId() {
		return model.getPeerId();
	}
	public void setPeerId(String peerId) {
		model.setPeerId(peerId);
	}
	public int getIp() {
		return model.getIp();
	}
	public void setIp(int ip) {
		model.setIp(ip);
	}
	public int getPuerto() {
		return model.getPuerto();
	}
	public void setPuerto(int puerto) {
		model.setPuerto(puerto);
	}
	public int getUploaded() {
		return model.getUploaded();
	}
	public void setUploaded(int uploaded) {
		model.setUploaded(uploaded);
	}
	public int getLeft() {
		return model.getLeft();
	}
	public void setLeft(int left) {
		model.setLeft(left);
	}
	public float getDownloaded() {
		return model.getDownloaded();
	}
	public void setDownloaded(int downloaded) {
		model.setDownloaded(downloaded);
	}
	public int getEvent() {
		return model.getEvent();
	}
	public void setEvent(int event) {
		model.setEvent(event);
	}
	
	public void setActive(boolean active) {
		model.setActive(active);
	}
	
	public boolean isActive() {
		return model.isActive();
	}
}