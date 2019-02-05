package controller;


import java.io.File;
import java.net.InetAddress;
import java.util.List;

import mensajes.udp.Connect;
import modelo.Peer;
import udp.PeerInfo;
import vista.PeerGestor;

public class PeerController {
	private Peer model;
	private PeerGestor view;

	public PeerController(Peer model) {
		super();
		this.model = model;
	}
	public PeerController(Peer model, PeerGestor view) {
		super();
		this.model = model;
		this.view = view;
	}
	public Peer getModel() {
		return model;
	}
	
	public void setModel(Peer model) {
		this.model = model;
	}
	
	public void start() {
		model.start();
	}

	public void leerTorrent(File torrent) {
		model.leerTorrent(torrent);
	}
	
	public String getPeerId() {
		return model.getPeerId();
	}
	public void setPeerId(String peerId) {
		model.setPeerId(peerId);
	}
	public InetAddress getIp() {
		return model.getIp();
	}
	public void setIp(InetAddress ip) {
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
	public int getDownloaded() {
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
	
	public int getTransactionID() {
		return model.getTransactionID();
	}

	public void setTransactionID(int transactionID) {
		model.setTransactionID(transactionID);
	}

	public long getConnectionID() {
		return model.getConnectionID();
	}

	public void setConnectionID(long connectionID) {
		model.setConnectionID(connectionID);
	}

	public long getLastconnection() {
		return model.getLastconnection();
	}

	public void setLastconnection(long lastconnection) {
		model.setLastconnection(lastconnection);
	}
	public void setActive(boolean active) {
		model.setActive(active);
	}
	
	public boolean isActive() {
		return model.isActive();
	}
	public long getLastannounce() {
		return model.getLastannounce();
	}

	public void setLastannounce(long lastannounce) {
		model.setLastannounce(lastannounce);
	}

	public byte[] getInfoHash() {
		return model.getInfoHash();
	}

	public void setInfoHash(byte[] infoHash) {
		model.setInfoHash(infoHash);
	}

	public int getSeeders() {
		return model.getSeeders();
	}

	public void setSeeders(int seeders) {
		model.setSeeders(seeders);
	}

	public int getLeechers() {
		return model.getLeechers();
	}

	public void setLeechers(int leechers) {
		model.setLeechers(leechers);
	}

	public int getInterval() {
		return model.getInterval();
	}

	public void setInterval(int interval) {
		model.setInterval(interval);
	}

	public List<PeerInfo> getSwarmPeers() {
		return model.getSwarmPeers();
	}

	public void setSwarmPeers(List<PeerInfo> swarmPeers) {
		model.setSwarmPeers(swarmPeers);
	}

	public boolean isPrimerConnect() {
		return model.isPrimerConnect();
	}

	public void setPrimerConnect(boolean primerConnect) {
		model.setPrimerConnect(primerConnect);
	}

	public boolean isPrimerAnnounce() {
		return model.isPrimerAnnounce();
	}

	public void setPrimerAnnounce(boolean primerAnnounce) {
		model.setPrimerAnnounce(primerAnnounce);
	}
}