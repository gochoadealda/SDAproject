package modelo;

import java.util.ArrayList;
import java.util.List;

import udp.PeerInfo;

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
	public Swarm selectSwarm(String nomCont);
	public void seedersleechers();
	public List<PeerInfo> selectPeersFromSwarm(int idSwarm);
}
