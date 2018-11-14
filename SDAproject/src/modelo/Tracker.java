package modelo;

import java.util.ArrayList;
import java.util.HashMap;

import com.sun.org.apache.xerces.internal.util.SynchronizedSymbolTable;

import mensajes.KeepAliveListener;
import mensajes.KeepAliveSubscriber;
import mensajes.NewMasterPublisher;
import mensajes.NewMasterSubscriber;
import mensajes.fileMessage.DBQueueFileReceiver;
import mensajes.fileMessage.DBQueueFileSender;
import mensajes.KeepAlivePublisher;
import vista.MainMenu;

public class Tracker {
	
	private String IP;
	private int puertoCom;
	private int ID;
	private boolean master;
	private int masterID;
	private int keepAliveTimer;
	private HashMap<Integer, Long> trackerMap;
	private ArrayList<Integer> trackerList;
	private ArrayList<Integer> okList;
	private TrackerDAO trackerDB;
	private boolean active;
	public KeepAliveSubscriber kaRecive;
	public KeepAlivePublisher kaSend; 
	public NewMasterSubscriber nmRecieve;
	public NewMasterPublisher nmSend;
	public DBQueueFileReceiver recieveDB;
	public DBQueueFileSender sendDB;
	
	public Tracker(String iP, int puertoCom) {
		super();
		IP = iP;
		this.ID = -1;
		this.puertoCom = puertoCom;
		this.trackerDB = new TrackerDAO("db/tracker.db");
		this.keepAliveTimer = 1;
		this.trackerList = new ArrayList<>();
		this.active = true;
		ViewThread trackerView = new ViewThread(this);
		
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
		kaRecive = new KeepAliveSubscriber(myTracker);
		kaRecive.start();
		try {
			Thread.sleep(10000);
		} catch (Exception e) {
			// TODO: handle exception
		}
		idSelector();

		kaSend = new KeepAlivePublisher(myTracker);
		recieveDB = new DBQueueFileReceiver(myTracker);
		kaSend = new KeepAlivePublisher(myTracker);
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
	
	public ArrayList<Integer> getOkList() {
		return okList;
	}

	public void setTrackerList(int id) {
		this.trackerList.add(id);
	}
	
	public void setOkList(int id) {
		this.okList.add(id);
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

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
	public HashMap<Integer, Long> getTrackerMap() {
		return trackerMap;
	}

	public void putTrackerMap(int ID, long time) {
		this.trackerMap.put(ID, time);
	}
	
	public void deleteIDfromList(int pos, int ID) {
		this.trackerList.remove(pos);
		this.trackerMap.remove(ID);
	}
	
	
	

}
