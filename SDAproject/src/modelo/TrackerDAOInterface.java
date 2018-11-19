package modelo;

import java.util.ArrayList;

public interface TrackerDAOInterface {
	public void createDatabase();
	public void closeConnection();
	public void deleteDatabase();
	public void insertP(Peer p);
	public void insertS(Swarm s);
	public void updateP(Peer p);
	public void updateS(Swarm s);
	public void deleteP(Integer idPeer);
	public void deleteS(Integer idSwarm);
	public ArrayList<Peer> selectPeers();
	public ArrayList<Swarm> selectSwarms();
}
