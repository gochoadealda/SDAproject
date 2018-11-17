package controller;

import java.util.ArrayList;

import modelo.Tracker;
import modelo.TrackerDAO;
import modelo.ViewThread;

public class TrackerController {
	Tracker model;
	ViewThread view;
	
	public TrackerController(Tracker model) {
		super();
		this.model = model;
	}
	
	public TrackerController(Tracker model, ViewThread view) {
		super();
		this.model = model;
		this.view = view;
		view.start();
	}

	public Tracker getModel() {
		return model;
	}

	public void setModel(Tracker model) {
		this.model = model;
	}
	
	public void idSelector() {
		model.idSelector();
	}
	
	public void start() {
		model.start();
	}
	
	public String getIP() {
		return model.getIP();
	}
	
	public void setIP(String iP) {
		model.setIP(iP);
	}
	
	public int getPuertoCom() {
		return model.getPuertoCom();
	}

	public void setPuertoCom(int puertoCom) {
		model.setPuertoCom(puertoCom);
	}

	public int getID() {
		return model.getID();
	}

	public void setID(int iD) {
		model.setID(iD);
	}
	
	public boolean isMaster() {
		return model.isMaster();
	}

	public void setMaster(boolean master) {
		model.setMaster(master);
	}

	public int getKeepAliveTimer() {
		return model.getKeepAliveTimer();
	}

	public void setKeepAliveTimer(int keepAliveTimer) {
		model.setKeepAliveTimer(keepAliveTimer);
	}

	public ArrayList<Integer> getTrackerList() {
		return model.getTrackerList();
	}
	
	public ArrayList<Integer> getOkList() {
		return model.getOkList();
	}

	public void setTrackerList(int id) {
		model.setTrackerList(id);
	}
	
	public void setOkList(int id) {
		model.setOkList(id);
	}

	public TrackerDAO getTrackerDB() {
		return model.getTrackerDB();
	}

	public void setTrackerDB(TrackerDAO trackerDB) {
		model.setTrackerDB(trackerDB);;
	}
	
	public int getMasterID() {
		return model.getMasterID();
	}

	public void setMasterID(int masterID) {
		model.setMasterID(masterID);
	}

	public boolean isActive() {
		return model.isActive();
	}

	public void setActive(boolean active) {
		model.setActive(active);
	}
	
	public void deleteIDfromList(int pos) {
		model.deleteIDfromList(pos);
	}
	
	public void sendDB() {
		model.sendDB();
	}
	
	public ArrayList<Long> getTimeList() {
		return model.getTimeList();
	}

	public void setTimeList(int pos, long time) {
		model.setTimeList(pos, time);
	}
	
	public void addTimeList(long time) {
		model.addTimeList(time);
	}
	
	
}