package modelo;

import java.util.ArrayList;

public class Swarm {
	
	private int idSwarm; 
	private String nomCont;   //Infohash
	private int tamano;
	private int seeders;
	private int leechers;
	private ArrayList<Peer> peerList;

	public Swarm(int idSwarm, String nomCont, int tamano, int seeders, int leechers) {
		super();
		this.idSwarm = idSwarm;
		this.nomCont = nomCont;
		this.tamano = tamano;
		this.seeders = seeders;
		this.leechers = leechers;
	}
	public Swarm(int idSwarm) {
		super();
		this.idSwarm = idSwarm;
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
	public int getTamano() {
		return tamano;
	}
	public void setTamano(int tamaño) {
		this.tamano = tamaño;
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
