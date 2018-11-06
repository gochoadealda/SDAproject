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
		this.puertoCom = puertoCom;
		this.keepAliveTimer = 1;
		this.trackerDB = trackerDB;
		this.trackerList = new ArrayList<>();
		this.view = new MainMenu();
		ViewThread trackerView = new ViewThread(view);
		trackerView.start();
		KeepAliveReciever kaRecive = new KeepAliveReciever(true);
		kaRecive.start();
		try {
			Thread.sleep(2000);
		}catch (Exception e) {
			System.out.println(e.getMessage());
		}
		ID = idSelector(this.trackerList);
		KeepAliveSender kaSend = new KeepAliveSender(true, this.ID);
		kaSend.start();
		
	}

	private int idSelector(ArrayList<Integer> idList) {
		int maxID=0;
		if (idList.size()==0) {
			maxID=1;
		}else {
			for (int i = 0; i < idList.size(); i++) {
				if (idList.get(i)>maxID) {
					maxID=idList.get(i) + 1;
				}
			}
		}
		return maxID;
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
