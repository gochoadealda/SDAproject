package controller;

import java.util.ArrayList;

import modelo.Peer;
import modelo.Swarm;

public class SwarmController {
	private Swarm model;

	public SwarmController(Swarm model) {
		super();
		this.model = model;
	}
	
		
	public Swarm getModel() {
		return model;
	}
	public void setModel(Swarm model) {
		this.model = model;
	}
	public int getID() {
		return model.getID();
	}
	public void setID(int iD) {
		model.setID(iD);
	}
	public String getNomCont() {
		return model.getNomCont();
	}
	public void setNomCont(String nomCont) {
		model.setNomCont(nomCont);
	}
	public int getTamano() {
		return model.getTamano();
	}
	public void setTamano(int tamano) {
		model.setTamano(tamano);
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
	public ArrayList<Peer> getPeerList() {
		return model.getPeerList();
	}
	public void setPeerList(Peer peer) {
		model.setPeerList(peer);
	}	
}
