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

public class TrackerDAO implements TrackerDAOInterface {

	private Connection con;
	private String dbname;

	public void createDatabase() {
		String url = "jdbc:sqlite:db/" + dbname;

		String sql1 = "CREATE TABLE IF NOT EXISTS Peer (\n" + "	idPeer integer PRIMARY KEY,\n" + "	ip text NOT NULL,\n"
				+ "	bytesDes real,\n" + "	bytesPen real,\n" + "	puerto integer,\n"
				+ " swarm_idTracker integer, FOREIGN KEY (swarm_idTracker) REFERENCES Swarm(idSwarm)" + ");";
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

	public TrackerDAO(String dbname) {
		super();
		con = null;
		this.dbname = dbname;
		try {
			Class.forName("org.sqlite.JDBC");
			con = DriverManager.getConnection("jdbc:sqlite:" + dbname);
			con.setAutoCommit(false);
			System.out.println(" - Db connection was opened :)");
		} catch (Exception ex) {
			System.err.println(" # Unable to create SQLiteDBManager: " + ex.getMessage());
		}
	}

	public void closeConnection() {
		try {
			con.close();
			System.out.println("\n - Db connection was closed :)");
		} catch (Exception ex) {
			System.err.println("\n # Error closing db connection: " + ex.getMessage());
		}
	}

	public void deleteDatabase() {
		try {
			String pathdbname="db/"+dbname;
			Files.deleteIfExists(Paths.get(pathdbname));
			System.out.println("- Db was deleted :)");
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("\n # Error deleting the db :(");
		}
	}

	@Override
	public void insertP(Peer p) {
		if (p != null) {

			String sqlString = "INSERT INTO Peer ('idPeer', 'ip', 'bytesDes', 'bytesPen', 'puerto') VALUES (?,?,?,?,?)";

			try (PreparedStatement stmt = con.prepareStatement(sqlString)) {
				stmt.setString(1, String.valueOf(p.getID()));
				stmt.setString(2, p.getIp());
				stmt.setString(3, String.valueOf(p.getBytesDes()));
				stmt.setString(4, String.valueOf(p.getBytesPen()));
				stmt.setString(5, String.valueOf(p.getPuerto()));

				if (stmt.executeUpdate() == 1) {
					System.out.println("\n - A new peer was inserted. :)");
					con.commit();
				} else {
					System.err.println("\n - The peer wasn't inserted. :(");
					con.rollback();
				}
			} catch (Exception ex) {
				System.err.println("\n # Error storing data in the db: " + ex.getMessage());
			}
		} else {
			System.err.println("\n # Error inserting a new Peer: some parameters are 'null' or 'empty'.");
		}

	}

	@Override
	public void insertS(Swarm s) {
		if (s != null) {

			String sqlString = "INSERT INTO Swarm ('idSwarm', 'nomCont', 'tamano', 'seeders', 'lechers') VALUES (?,?,?,?,?)";

			try (PreparedStatement stmt = con.prepareStatement(sqlString)) {
				stmt.setString(1, String.valueOf(s.getID()));
				stmt.setString(2, s.getNomCont());
				stmt.setString(3, String.valueOf(s.getTamaño()));
				stmt.setString(4, String.valueOf(s.getSeeders()));
				stmt.setString(5, String.valueOf(s.getLeechers()));

				if (stmt.executeUpdate() == 1) {
					System.out.println("\n - A new swarm was inserted. :)");
					con.commit();
				} else {
					System.err.println("\n - The swarm wasn't inserted. :(");
					con.rollback();
				}
			} catch (Exception ex) {
				System.err.println("\n # Error storing data in the db: " + ex.getMessage());
			}
		} else {
			System.err.println("\n # Error inserting a new swarm: some parameters are 'null' or 'empty'.");
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
				peer = new Peer(rs.getInt("idPeer"), rs.getString("ip"), rs.getInt("puerto"), rs.getInt("bytesDes"),
						rs.getInt("bytesPen"));
				System.out.println("Peer ID:" + peer.getID() + " IP:" + peer.getIp() + " Port:" + peer.getPuerto());
				peerList.add(peer);
			}
			return peerList;
		} catch (SQLException ex) {
			System.err.println("\n # Error retrieving peers from database: " + ex.getMessage());
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
				System.out.println("Swarm ID:" + swarm.getID() + " nomCont:" + swarm.getNomCont() + " Tamaño:"
						+ swarm.getTamaño());
				swarmList.add(swarm);
			}
			return swarmList;
		} catch (SQLException ex) {
			System.err.println("\n # Error retrieving swarms from database: " + ex.getMessage());
		}
		return swarmList;
	}

	@Override
	public void updateP(Peer p) {
		if (p != null) {

			String sqlString = "UPDATE Peer SET bytesDes = ?,bytesPen=? WHERE idPeer =?";
			try (PreparedStatement stmt = con.prepareStatement(sqlString)) {
				stmt.setString(1, String.valueOf(p.getBytesDes()));
				stmt.setString(2, String.valueOf(p.getBytesPen()));
				stmt.setString(3, String.valueOf(p.getID()));

				if (stmt.executeUpdate() != 0) {
					System.out.println("\n - Peer's data was updated. :)");
					con.commit();
				} else {
					System.err.println("\n - Peer's data wasn't updated. :(");
					con.rollback();
				}
			} catch (Exception ex) {
				System.err.println("\n # Error updating data in the db: " + ex.getMessage());
			}
		} else {
			System.err.println("\n # Error updating Peer's data: some parameters are 'null' or 'empty'.");
		}
	}

	@Override
	public void updateS(Swarm s) {
		if (s != null) {

			String sqlString = "UPDATE Swarm SET tamano = ?,seeders=?,leechers=? WHERE idSwarm =?";
			try (PreparedStatement stmt = con.prepareStatement(sqlString)) {
				stmt.setString(1, String.valueOf(s.getTamaño()));
				stmt.setString(2, String.valueOf(s.getSeeders()));
				stmt.setString(3, String.valueOf(s.getLeechers()));
				stmt.setString(4, String.valueOf(s.getID()));

				if (stmt.executeUpdate() != 0) {
					System.out.println("\n - Swarm's data was updated. :)");
					con.commit();
				} else {
					System.err.println("\n - Swarm's data wasn't updated. :(");
					con.rollback();
				}
			} catch (Exception ex) {
				System.err.println("\n # Error updating data in the db: " + ex.getMessage());
			}
		} else {
			System.err.println("\n # Error updating Swarm's data: some parameters are 'null' or 'empty'.");
		}
	}

	@Override
	public void deleteP(Integer idPeer) {
		String sqlString = "DELETE FROM Peer WHERE idPeer =" + idPeer + ";";

		try (PreparedStatement stmt = con.prepareStatement(sqlString)) {
			int deleted = stmt.executeUpdate();

			if (deleted > 0) {
				System.out.println("Peer deleted.");
				con.commit();
			} else {
				System.out.println("\n - No Peer was deleted.");
				con.rollback();
			}
		} catch (Exception ex) {
			System.err.println("\n # Error cleaning the db: " + ex.getMessage());
		}
	}

	@Override
	public void deleteS(Integer idSwarm) {
		String sqlString = "DELETE FROM Swarm WHERE idSwarm =" + idSwarm + ";";

		try (PreparedStatement stmt = con.prepareStatement(sqlString)) {
			int deleted = stmt.executeUpdate();

			if (deleted > 0) {
				System.out.println("Swarm deleted.");
				con.commit();
			} else {
				System.out.println("\n - No Swarm was deleted.");
				con.rollback();
			}
		} catch (Exception ex) {
			System.err.println("\n # Error cleaning the db: " + ex.getMessage());
		}
	}

	public static void main(String[] args) {
		//		System.out.println("Prueba");
		//		TrackerDAO manager = new TrackerDAO("db/tracker.db");
		//		Peer p= new Peer(1, "ipppp", 8080, 90.4, 90.6);
		//		manager.insertP(p);
		//		manager.selectPeers();
		//		manager.updateP(p);
		//		manager.deleteP(0);
		//		manager.deleteP(1);
		//		manager.closeConnection();
	}
}
