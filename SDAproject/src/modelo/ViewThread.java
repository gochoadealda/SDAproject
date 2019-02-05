package modelo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.sun.xml.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

import controller.TrackerController;

import vista.MainMenu;

public class ViewThread extends Thread{
	private TrackerController trackerController;

	public ViewThread(Tracker model) {
		super();
		this.trackerController = new TrackerController(model);
	}

	@SuppressWarnings("serial")
	@Override
	public void run() {
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		MainMenu view = new MainMenu(trackerController.getModel());
		view.setVisible(true);
		while (true) {
			try {
				view.setTheMaster(trackerController.isMaster());
				if (view.isTheMaster()) {
					view.setTitle("Tracker Master "+trackerController.getID());
				}else {
					view.setTitle("Tracker Slave "+trackerController.getID());
				}
				Thread.sleep(1000);
				//Tracker JTable
				ArrayList<Integer> trackerList = trackerController.getTrackerList();
				ArrayList<Long> timeList = trackerController.getTimeList();
				DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				Object[][] tableData = new Object[trackerList.size()][2];
				for (int i=0;i<tableData.length;i++){
						tableData[i][0] = trackerList.get(i);
						tableData[i][1] = dateFormat.format(timeList.get(i));
					
				}
				//view.setDataTabla1(new Object[][] { { myTracker.getID() }});
				view.setDataTabla1(tableData);
				view.setModelTrackers(new DefaultTableModel(view.getDataTabla1(), view.getColumnsTabla1()) {
					@Override
					public boolean isCellEditable(int row, int column) {
						return false;
					}
				});

				view.setTableTrackers(new JTable(view.getModelTrackers())); 
				JScrollPane scrollPaneTabla1 = new JScrollPane(view.getTableTrackers());
				scrollPaneTabla1.setBounds(32, 28, 452, 431);
				view.getPanel2().add(scrollPaneTabla1);
				view.getTableTrackers().repaint();
				
				//Peer JTable
				ArrayList<Peer> dataPeer = trackerController.getModel().getTrackerDB().selectPeers();
				Object[][] peerTableData = new Object[dataPeer.size()][5];
				for (int i=0;i<peerTableData.length;i++){
					tableData[i][0] = dataPeer.get(i).getIdPeer();
					tableData[i][1] = dataPeer.get(i).getIp();
					tableData[i][2] = dataPeer.get(i).getPuerto();
					tableData[i][3] = dataPeer.get(i).getBytesPen();
					tableData[i][4] = dataPeer.get(i).getBytesDes();
				}
				view.setDataTablaPeer(peerTableData);
				view.setModelPeers(new DefaultTableModel(view.getDataTablaPeer(), view.getColumnsTablaPeer()) {
					@Override
					public boolean isCellEditable(int row, int column) {
						return false;
					}
				});
				view.setTablePeers(new JTable(view.getModelPeers()));
				JScrollPane scrollPaneTabla2 = new JScrollPane(view.getTablePeers());
				scrollPaneTabla2.setBounds(595, 309, 726, 347);
				view.getPanel3().add(scrollPaneTabla2);
				view.getTablePeers().repaint();
				
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}

