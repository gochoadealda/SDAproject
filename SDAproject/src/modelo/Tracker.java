package modelo;

import java.util.ArrayList;

import com.sun.org.apache.xerces.internal.util.SynchronizedSymbolTable;

import mensajes.KeepAliveListener;
import mensajes.KeepAliveReciever;
import mensajes.KeepAliveSender;
import vista.MainMenu;

public class Tracker {
	
	private String IP;
	private int puertoCom;
	private int ID;
	private boolean master;
	private int masterID;
	private int keepAliveTimer;
	private ArrayList<Integer> trackerList;
	private TrackerDAO trackerDB;
	private MainMenu view;
	private boolean active;
	
	public Tracker(String iP, int puertoCom, TrackerDAO trackerDB) {
		super();
		IP = iP;
		this.ID = -1;
		this.puertoCom = puertoCom;
		this.keepAliveTimer = 1;
		this.trackerDB = trackerDB;
		this.trackerList = new ArrayList<>();
		this.view = new MainMenu();
		ViewThread trackerView = new ViewThread(view);
		//trackerView.start();
	}

	public void idSelector() {
		ArrayList<Integer> idList = this.trackerList;
		int maxid=0;
		if (idList.size()==0) {
			master = true;
			ID = 0;
		}else {
			for (int i = 0; i < idList.size(); i++) {
				if (idList.get(i)>maxid) {
					maxid=idList.get(i);
				}
			}
			ID=maxid + 1;
		}
	}
	
	public void start(Tracker myTracker) {
		KeepAliveReciever kaRecive = new KeepAliveReciever(true, myTracker);
		kaRecive.start();
		try {
			Thread.sleep(10000);
		} catch (Exception e) {
			// TODO: handle exception
		}
		idSelector();
		KeepAliveSender kaSend = new KeepAliveSender(true, this.ID);
		kaSend.start();
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

	public void setTrackerList(int id) {
		this.trackerList.add(id);
	}

	public TrackerDAO getTrackerDB() {
		return trackerDB;
	}

	public void setTrackerDB(TrackerDAO trackerDB) {
		this.trackerDB = trackerDB;
	}
	
	public int getMasterID() {
		return masterID;
	}

	public void setMasterID(int masterID) {
		this.masterID = masterID;
	}

	public MainMenu getView() {
		return view;
	}

	public void setView(MainMenu view) {
		this.view = view;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
	
	
	
	
	

}
