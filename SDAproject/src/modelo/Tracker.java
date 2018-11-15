package modelo;

import java.util.ArrayList;
import java.util.HashMap;

import com.sun.org.apache.xerces.internal.util.SynchronizedSymbolTable;

import mensajes.fileMessage.DBQueueFileReceiver;
import mensajes.fileMessage.DBQueueFileSender;
import mensajes.topic.KeepAliveListener;
import mensajes.topic.KeepAlivePublisher;
import mensajes.topic.KeepAliveSubscriber;
import mensajes.topic.NewMasterPublisher;
import mensajes.topic.NewMasterSubscriber;
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
	private ArrayList<Long> timeList;
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
		this.keepAliveTimer = 1;
		this.trackerList = new ArrayList<>();
		this.timeList = new ArrayList<>();
		this.active = true;
		ViewThread trackerView = new ViewThread(this);
		
		trackerView.start();
	}
	
	public void newMaster() {	
		/*for (int n: this.trackerList) 
		    if(n>this.ID) this.setMasterID(this.ID); */
		
		for (int i =0; i<this.trackerList.size();i++) {
			if(this.trackerList.get(i)> this.ID) {
			}else {
				this.setMasterID(this.ID);
			}
		}
	}
	
	public void idSelector() {
		ArrayList<Integer> idList = this.trackerList;
		int maxid=0;
		if (idList.size()==0) {
			master = true;
			ID = 0;
			this.trackerDB = new TrackerDAO("db/tracker"+ID+".db");
			trackerDB.createDatabase();
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
		if(!master) {
			recieveDB = new DBQueueFileReceiver(myTracker);
			recieveDB.start();
		}
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
	
	public void asignNewMaster() {
		
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
	
	public void deleteIDfromList(int pos) {
		this.trackerList.remove(pos);
		this.timeList.remove(pos);
	}
	
	public void sendDB() {
		System.out.println("LLega");
		this.sendDB = new DBQueueFileSender(this);
		this.sendDB.start();
		
	}
	public ArrayList<Long> getTimeList() {
		return timeList;
	}

	public void setTimeList(int pos, long time) {
		this.timeList.set(pos, time);
	}
	
	public void addTimeList(long time) {
		this.timeList.add(time);
	}
	
	
	

}
