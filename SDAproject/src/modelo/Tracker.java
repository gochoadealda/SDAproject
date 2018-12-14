package modelo;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import mensajes.fileMessage.DBQueueFileReceiver;
import mensajes.fileMessage.DBQueueFileSender;
import mensajes.queue.DieReceiver;
import mensajes.queue.DieSender;
import mensajes.queue.OkErrorReceiver;
import mensajes.queue.OkErrorSender;
import mensajes.topic.KeepAlivePublisher;
import mensajes.topic.KeepAliveSubscriber;
import mensajes.topic.NewMasterPublisher;
import mensajes.topic.NewMasterSubscriber;
import mensajes.topic.ReadyPublisher;
import mensajes.topic.ReadySubscriber;
import mensajes.topic.UpdatePublisher;
import mensajes.topic.UpdateSubscriber;
import mensajes.udp.Actions;
import mensajes.udp.Connect;

public class Tracker {
	
	private String IP;
	private int puertoCom;
	private int ID;
	private boolean master;
	private int masterID;
	private int keepAliveTimer;
	private ArrayList<Integer> trackerList;
	private ArrayList<Integer> okList;         //SOBRA???
	private ArrayList<Long> timeList;
	private TrackerDAO trackerDB;
	private boolean active;
	public KeepAliveSubscriber kaRecive;
	public KeepAlivePublisher kaSend; 
	public NewMasterSubscriber nmRecieve;
	public NewMasterPublisher nmSend;
	public DBQueueFileReceiver recieveDB;
	public DBQueueFileSender sendDB;
	public OkErrorReceiver okRecieve;
	public OkErrorSender okSend;
	public UpdatePublisher updateSend;
	public UpdateSubscriber updateRecieve;
	public ReadyPublisher readySend;
	public ReadySubscriber readyRecieve;
	public DieReceiver dieRecieve;
	public DieSender dieSend;
	public Connect udpConnect;
	public Actions udpActions;
	public ViewThread trackerView;
	private long bdtimestamp;
	private int transactionID;
	private HashMap<String, Integer> transactionIDs;
	private long connectionID;
	private HashMap<String, Long> connectionIDs;
	private long oldConnectionID;
	private HashMap<String, Long> oldConnectionIDs;
	private Peer peer;
	private Swarm swarm;
	private boolean multicast, ready;


	public Tracker(String iP, int puertoCom) {
		super();
		IP = iP;
		this.ID = -1;
		this.masterID = -1;
		this.puertoCom = puertoCom;
		this.keepAliveTimer = 1;
		this.trackerList = new ArrayList<>();
		this.timeList = new ArrayList<>();
		this.active = true;
	}
	
	public void idSelector() {
		ArrayList<Integer> idList = this.trackerList;
		int maxid=0;
		if (idList.size()==0) {
			master = true;
			ID = 0;
			masterID = ID;
			this.bdtimestamp=System.currentTimeMillis();
			this.trackerDB = new TrackerDAO("tracker"+bdtimestamp+".db");
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
	public void start() {
		kaRecive = new KeepAliveSubscriber(this);
		kaRecive.start();

		dieRecieve = new DieReceiver(this);
		dieRecieve.start();

		transactionIDs = new HashMap<>();
		connectionIDs = new HashMap<>();
		oldConnectionIDs = new HashMap<>();
		try {
			Thread.sleep(3000);
		} catch (Exception e) {
			
		}
		idSelector();

		if(!master) {
			recieveDB = new DBQueueFileReceiver(this);
			recieveDB.start();
		}
		kaSend = new KeepAlivePublisher(this);

		kaSend.start();
		udpConnect = new Connect(this);
		udpConnect.start();
		
	
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
	
	public ArrayList<Integer> getOkList() {
		return okList;
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
	
	public void deleteIDfromList(int pos) {
		if(this.trackerList.get(pos) == this.masterID) {
			this.trackerList.remove(pos);
			this.timeList.remove(pos);
			int min = this.trackerList.get(0);
			for (int i = 0; i < this.trackerList.size(); i++) {
				if(this.trackerList.get(i) < min) {
					min = this.trackerList.get(i);
				}
			}
			if(min == this.ID) {
				this.master = true;
				this.masterID = this.ID;
				this.nmSend = new NewMasterPublisher(this.ID);
				this.nmSend.start();
			}else {
				this.master = false;
				this.nmRecieve = new NewMasterSubscriber(this);
				this.nmRecieve.start();
			}
		}else {
			this.trackerList.remove(pos);
			this.timeList.remove(pos);
		}
		
	}
	
	public void sendDB() {
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
	
	public void createConnectionDB() {
		this.trackerDB = new TrackerDAO("tracker"+bdtimestamp+".db");
	}
	
	public long getBdtimestamp() {
		return bdtimestamp;
	}

	public void setBdtimestamp(long bdtimestamp) {
		this.bdtimestamp = bdtimestamp;
	}
	
	public int getTransactionID() {
		return transactionID;
	}

	public void setTransactionID(int transactionID) {
		this.transactionID = transactionID;
	}

	public long getConnectionID() {
		return connectionID;
	}

	public void setConnectionID(long connectionID) {
		this.connectionID = connectionID;
	}

	public long getOldConnectionID() {
		return oldConnectionID;
	}

	public void setOldConnectionID(long oldConnectionID) {
		this.oldConnectionID = oldConnectionID;
	}

	public boolean isMulticast() {
		return multicast;
	}

	public void setMulticast(boolean multicast) {
		this.multicast = multicast;
	}

	public boolean isReady() {
		return ready;
	}

	public void setReady(boolean ready) {
		this.ready = ready;
	}

	public Peer getPeer() {
		return peer;
	}

	public void setPeer(Peer peer) {
		this.peer = peer;
	}

	public Swarm getSwarm() {
		return swarm;
	}

	public void setSwarm(Swarm swarm) {
		this.swarm = swarm;
	}

	public HashMap<String, Integer> getTransactionIDs() {
		return transactionIDs;
	}

	public void setTransactionIDs(HashMap<String, Integer> transactionIDs) {
		this.transactionIDs = transactionIDs;
	}

	public HashMap<String, Long> getConnectionIDs() {
		return connectionIDs;
	}

	public void setConnectionIDs(HashMap<String, Long> connectionIDs) {
		this.connectionIDs = connectionIDs;
	}

	public HashMap<String, Long> getOldConnectionIDs() {
		return oldConnectionIDs;
	}

	public void setOldConnectionIDs(HashMap<String, Long> oldConnectionIDs) {
		this.oldConnectionIDs = oldConnectionIDs;
	}
	
	
}