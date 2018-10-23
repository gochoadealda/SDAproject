package modelo;

import java.util.ArrayList;

public class Tracker {
	
	private String IP;
	private int puertoCom;
	private int ID;
	private boolean master;
	private int masterID;
	public int getMasterID() {
		return masterID;
	}

	public void setMasterID(int masterID) {
		this.masterID = masterID;
	}

	private int keepAliveTimer;
	private ArrayList<Integer> trackerList;
	private TrackerDAO trackerDB;
	
	public Tracker(String iP, int puertoCom, int iD, int keepAliveTimer,
			TrackerDAO trackerDB) {
		super();
		IP = iP;
		this.puertoCom = puertoCom;
		ID = iD;
		this.keepAliveTimer = keepAliveTimer;
		this.trackerDB = trackerDB;
	}

	public String getIP() {
		return IP;
	}

	public void setIP(String iP) {
		IP = iP;
	}

	public int getPuertoCom() {
		return puertoCom;
	}

	public void setPuertoCom(int puertoCom) {
		this.puertoCom = puertoCom;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public boolean isMaster() {
		return master;
	}

	public void setMaster(boolean master) {
		this.master = master;
	}

	public int getKeepAliveTimer() {
		return keepAliveTimer;
	}

	public void setKeepAliveTimer(int keepAliveTimer) {
		this.keepAliveTimer = keepAliveTimer;
	}

	public ArrayList<Integer> getTrackerList() {
		return trackerList;
	}

	public void setTrackerList(ArrayList<Integer> trackerList) {
		this.trackerList = trackerList;
	}

	public TrackerDAO getTrackerDB() {
		return trackerDB;
	}

	public void setTrackerDB(TrackerDAO trackerDB) {
		this.trackerDB = trackerDB;
	}
	
	
	
	
	
	

}
