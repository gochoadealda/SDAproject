package modelo;

public interface TrackerDAOInterface {
	
	public void insertP(Peer peer);
	public void insertS(Swarm swarm);
	public void updateP(Peer peer);
	public void updateS(Swarm swarm);
	public void deleteP(int id);
	public void deleteS(int id);
}
