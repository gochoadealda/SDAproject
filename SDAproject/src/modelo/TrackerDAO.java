package modelo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import udp.PeerInfo;

public class TrackerDAO implements TrackerDAOInterface {

	private Connection con;
	private String dbname;

	public void createDatabase() {
		String url = "jdbc:sqlite:db/" + dbname;

		String sql1 = "CREATE TABLE IF NOT EXISTS Peer (\n" + "	idPeer text PRIMARY KEY,\n" + "	ip text NOT NULL,\n"
				+ "	bytesDes real,\n" + "	bytesPen real,\n" + "	bytesUp real,\n"+ "	puerto integer,\n"
				+ " idSwarm text, FOREIGN KEY (idSwarm) REFERENCES Swarm(nomCont)" + ");";//llamar idSwarm a swarm_idTracker
		String sql2 = "CREATE TABLE IF NOT EXISTS Swarm (\n" + "	idSwarm integer PRIMARY KEY,\n"
				+ "	nomCont text NOT NULL,\n" + "	tamano integer,\n" + "	seeders integer,\n" + "	leechers integer\n"
				+ ");";
		try (Connection conn = DriverManager.getConnection(url); Statement stmt = conn.createStatement()) {
			// create a new table
			System.out.println("Database created");
			stmt.execute(sql2);
			stmt.execute(sql1);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	public TrackerDAO(String dbname, boolean type) {
		super();
		con = null;
		this.dbname = dbname;
		if(type) {
			createDatabase();
		}
		try {
			Class.forName("org.sqlite.JDBC");
			con = DriverManager.getConnection("jdbc:sqlite:" +"db/"+ dbname);
			con.setAutoCommit(false);
			System.out.println("- Db connection was opened :)");
		} catch (Exception ex) {
			System.err.println(" # Unable to create SQLiteDBManager: " + ex.getMessage());
		}

	}

	public void closeConnection() {
		try {
			con.close();
			System.out.println("- Db connection was closed :)");
		} catch (Exception ex) {
			System.err.println("# Error closing db connection: " + ex.getMessage());
		}
	}

	public void deleteDatabase() {
		try {
			String pathdbname="./db/"+dbname;
			Files.deleteIfExists(Paths.get(pathdbname));
			System.out.println("\n- Db was deleted :)");
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("# Error deleting the db :(");
		}
	}

	@Override
	public void insertP(Peer p) {
		if (p != null) {

			String sqlString = "INSERT INTO Peer ('idPeer', 'ip', 'bytesDes', 'bytesPen', 'bytesUp', 'puerto', 'idSwarm') VALUES (?,?,?,?,?,?,?)";

			try (PreparedStatement stmt = con.prepareStatement(sqlString)) {
				stmt.setString(1, p.getID());
				stmt.setString(2, p.getIp());
				stmt.setString(3, String.valueOf(p.getBytesDes()));
				stmt.setString(4, String.valueOf(p.getBytesPen()));
				stmt.setString(5, String.valueOf(p.getBytesUp()));
				stmt.setString(6, String.valueOf(p.getPuerto()));
				stmt.setString(7, p.getIdSwarm());

				if (stmt.executeUpdate() == 1) {
					System.out.println("\n - A new peer was inserted. :)");
					con.commit();
				} else {
					System.err.println("\n - The peer wasn't inserted. :(");
					con.rollback();
				}
			} catch (Exception ex) {
				System.err.println("# Error storing peer in the db: " + ex.getMessage());
			}
		} else {
			System.err.println("# Error inserting a new Peer: some parameters are 'null' or 'empty'.");
		}

	}

	@Override
	public void insertS(Swarm s) {
		if (s != null) {

			String sqlString = "INSERT INTO Swarm ('idSwarm', 'nomCont', 'tamano', 'seeders', 'leechers') VALUES (?,?,?,?,?)";

			try (PreparedStatement stmt = con.prepareStatement(sqlString)) {
				stmt.setString(1, String.valueOf(s.getID()));
				stmt.setString(2, s.getNomCont());
				stmt.setString(3, String.valueOf(s.getTamano()));
				stmt.setString(4, String.valueOf(s.getSeeders()));
				stmt.setString(5, String.valueOf(s.getLeechers()));

				if (stmt.executeUpdate() == 1) {
					con.commit();
					System.out.println("- A new swarm was inserted. :)");
				} else {
					System.err.println("- The swarm wasn't inserted. :(");
					con.rollback();
				}
			} catch (Exception ex) {
				System.err.println("# Error storing data in the db: " + ex.getMessage());
				ex.printStackTrace();
			}
		} else {
			System.err.println("# Error inserting a new swarm: some parameters are 'null' or 'empty'.");
		}
	}

	@Override
	public ArrayList<Peer> selectPeers() {

		ArrayList<Peer> peerList = new ArrayList<Peer>();
		String sqlString = "Select * from Peer";
		Peer peer;
		try (PreparedStatement stmt = con.prepareStatement(sqlString)) {
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				peer = new Peer(rs.getString("idPeer"), rs.getString("ip"), rs.getInt("puerto"), rs.getInt("bytesDes"),
						rs.getInt("bytesPen"), rs.getInt("bytesUp"), rs.getString("idSwarm"), 0);
				System.out.println("Peer ID:" + peer.getID() + " IP:" + peer.getIp() + " Port:" + peer.getPuerto());
				peerList.add(peer);
			}
			return peerList;
		} catch (SQLException ex) {
			System.err.println("# Error retrieving peers from database: " + ex.getMessage());
		}
		return peerList;
	}

	@Override
	public ArrayList<Swarm> selectSwarms() {

		ArrayList<Swarm> swarmList = new ArrayList<Swarm>();
		String sqlString = "Select * from Swarm";
		Swarm swarm;
		try (PreparedStatement stmt = con.prepareStatement(sqlString)) {
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				swarm = new Swarm(rs.getInt("idSwarm"), rs.getString("nomCont"), rs.getInt("tamano"),
						rs.getInt("seeders"), rs.getInt("leechers"));
				System.out.println("Swarm ID:" + swarm.getID() + " nomCont:" + swarm.getNomCont() + " Tama�o:"
						+ swarm.getTamano());
				swarmList.add(swarm);
			}
			return swarmList;
		} catch (SQLException ex) {
			System.err.println("# Error retrieving swarms from database: " + ex.getMessage());
		}
		return swarmList;
	}


	@Override
	public Swarm selectSwarm(String nomCont) {

		String sqlString = "Select * from Swarm WHERE nomCont = '" + nomCont + "'";
		Swarm swarm = null;
		try (PreparedStatement stmt = con.prepareStatement(sqlString)) {
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				swarm = new Swarm(rs.getInt("idSwarm"), rs.getString("nomCont"), rs.getInt("tamano"),
						rs.getInt("seeders"), rs.getInt("leechers"));
				System.out.println("Swarm ID:" + swarm.getID() + " nomCont:" + swarm.getNomCont() + " Tamaño:"
						+ swarm.getTamano());
			}
			return swarm;
		} catch (SQLException ex) {
			System.err.println("# Error retrieving swarm from database: " + ex.getMessage());
		}
		return swarm;
	}

	@Override
	public void updateP(Peer p) {
		if (p != null) {

			String sqlString = "UPDATE Peer SET ip=?, puerto=?, bytesDes = ?,bytesPen=?, bytesUp=?, idSwarm=? WHERE idPeer =?";
			try (PreparedStatement stmt = con.prepareStatement(sqlString)) {
				stmt.setString(1, p.getIp());
				stmt.setString(2, String.valueOf(p.getPuerto()));
				stmt.setString(3, String.valueOf(p.getBytesDes()));
				stmt.setString(4, String.valueOf(p.getBytesPen()));
				stmt.setString(5, String.valueOf(p.getBytesUp()));
				stmt.setString(6, p.getIdSwarm());
				stmt.setString(7, p.getID());

				if (stmt.executeUpdate() != 0) {
					System.out.println("- Peer's data was updated. :)");
					con.commit();
				} else {
					System.err.println("- Peer's data wasn't updated. :(");
					con.rollback();
				}
			} catch (Exception ex) {
				System.err.println("# Error updating data in the db: " + ex.getMessage());
			}
		} else {
			System.err.println("# Error updating Peer's data: some parameters are 'null' or 'empty'.");
		}
	}

	@Override
	public void updateS(Swarm s) {
		if (s != null) {

			String sqlString = "UPDATE Swarm SET tamano = ?,seeders=?,leechers=? WHERE nomCont =?";
			try (PreparedStatement stmt = con.prepareStatement(sqlString)) {
				stmt.setString(1, String.valueOf(s.getTamano()));
				stmt.setString(2, String.valueOf(s.getSeeders()));
				stmt.setString(3, String.valueOf(s.getLeechers()));
				stmt.setString(4, String.valueOf(s.getNomCont()));

				if (stmt.executeUpdate() != 0) {
					System.out.println("- Swarm's data was updated. :)");
					con.commit();
				} else {
					System.err.println("- Swarm's data wasn't updated. :(");
					con.rollback();
				}
			} catch (Exception ex) {
				System.err.println("# Error updating data in the db: " + ex.getMessage());
			}
		} else {
			System.err.println("# Error updating Swarm's data: some parameters are 'null' or 'empty'.");
		}
	}

	@Override
	public void deleteP(String idPeer) {
		String sqlString = "DELETE FROM Peer WHERE idPeer = '" + idPeer + "'";

		try (PreparedStatement stmt = con.prepareStatement(sqlString)) {
			int deleted = stmt.executeUpdate();

			if (deleted > 0) {
				System.out.println("Peer deleted.");
				con.commit();
			} else {
				System.out.println("- No Peer was deleted.");
				con.rollback();
			}
		} catch (Exception ex) {
			System.err.println("# Error cleaning the db: " + ex.getMessage());
		}
	}

	@Override
	public void deleteS(Integer idSwarm) {
		String sqlString = "DELETE FROM Swarm WHERE nomCont = '" + idSwarm + "'";

		try (PreparedStatement stmt = con.prepareStatement(sqlString)) {
			int deleted = stmt.executeUpdate();

			if (deleted > 0) {
				System.out.println("Swarm deleted.");
				con.commit();
			} else {
				System.out.println("- No Swarm was deleted.");
				con.rollback();
			}
		} catch (Exception ex) {
			System.err.println("# Error cleaning the db: " + ex.getMessage());
		}
	}
	/*
	@Override
	public void seedersleechers() {

		int seeders=0;
		int leechers=0;
		ArrayList<Swarm> swarmList= selectSwarms();
		ArrayList<Peer> peerList= selectPeers();
		for(int i=0;i<swarmList.size();i++) {
			for(int j=0;j<peerList.size();j++) {
				if(String.valueOf(swarmList.get(i).getID())==peerList.get(j).getIdSwarm()) {//coger el id del swarm relacionado en el peer
					if(peerList.get(j).getBytesDes()!=0 && peerList.get(j).getBytesPen()==0) {
						seeders++;
					}else if(peerList.get(j).getBytesPen()!=0) {
						leechers++;
					}
				}
			}
			Swarm swarm= new Swarm(swarmList.get(i).getID(), swarmList.get(i).getNomCont(), swarmList.get(i).getTamano(), seeders, leechers);
			updateS(swarm);
			//swarmList.get(i).setLeechers(leechers);
			//swarmList.get(i).setSeeders(seeders);
			seeders=0;
			leechers=0;
		}
	}
	 */

	@Override
	public List<PeerInfo> selectPeersFromSwarm(String nomCont) {
		List<PeerInfo> peersFromSwarm = new ArrayList<PeerInfo>();
		ArrayList<Peer> peerList= selectPeers();
		for(int j=0;j<peerList.size();j++) {
			if(peerList.get(j).getIdSwarm()==nomCont) {
				PeerInfo peerinfo= new PeerInfo(Integer.parseInt(peerList.get(j).getIp()), peerList.get(j).getPuerto());
				peersFromSwarm.add(peerinfo);
			}
		}
		return peersFromSwarm;
	}

	public static void main(String[] args) {
		System.out.println("Prueba");
		TrackerDAO manager = new TrackerDAO("/tracker1542815698871.db", true);
		//		Peer p= new Peer(1, "ipppp", 8080, 90.4, 90.6);
		//		manager.insertP(p);
		//		manager.selectPeers();
		//		manager.updateP(p);
		//		manager.deleteP(0);
		//		manager.deleteP(1);
		//		manager.closeConnection();
		/*Peer p= new Peer(1, "ip", 8080, 10000, 0, 0, 1, 0);//2 seed y 1 leech
				Peer p2= new Peer(2, "ipp", 8081, 0, 10, 0, 1, 0);
				Peer p3= new Peer(3, "ippp", 8082, 10000, 0, 0, 1, 0);
				manager.insertP(p);
				manager.insertP(p2);
				manager.insertP(p3);
				Swarm s=new Swarm(4, "Asier", 3, 0, 0);
				manager.insertS(s);

				manager.seedersleechers();
				System.out.println("terminado");
				Swarm a=manager.selectSwarm(4);
				System.out.println(a.getID());
				System.out.println(a.getLeechers());
				System.out.println(a.getSeeders());
				System.out.println(a.getNomCont());*/
	}

}