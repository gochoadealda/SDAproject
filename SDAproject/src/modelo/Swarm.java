package modelo;

import java.util.ArrayList;

public class Swarm {
	
	private int idSwarm;
	private String nomCont;
	private int tama�o;
	private int seeders;
	private int leechers;
	private ArrayList<Peer> peerList;
	public Swarm(int idSwarm, String nomCont, int tama�o, int seeders, int leechers) {
		super();
		this.idSwarm = idSwarm;
		this.nomCont = nomCont;
		this.tama�o = tama�o;
		this.seeders = seeders;
		this.leechers = leechers;
	}
	public int getID() {
		return idSwarm;
	}
	public void setID(int iD) {
		idSwarm = iD;
	}
	public String getNomCont() {
		return nomCont;
	}
	public void setNomCont(String nomCont) {
		this.nomCont = nomCont;
	}
	public int getTama�o() {
		return tama�o;
	}
	public void setTama�o(int tama�o) {
		this.tama�o = tama�o;
	}
	public int getSeeders() {
		return seeders;
	}
	public void setSeeders(int seeders) {
		this.seeders = seeders;
	}
	public int getLeechers() {
		return leechers;
	}
	public void setLeechers(int leechers) {
		this.leechers = leechers;
	}
	public ArrayList<Peer> getPeerList() {
		return peerList;
	}
	public void setPeerList(ArrayList<Peer> peerList) {
		this.peerList = peerList;
	}
	
	
}
