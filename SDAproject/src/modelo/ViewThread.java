package modelo;

import java.util.ArrayList;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import controller.TrackerController;

import vista.MainMenu;

public class ViewThread extends Thread{
	private TrackerController trackerController;

	public ViewThread(Tracker model) {
		super();
		this.trackerController = new TrackerController(model);
	}

	@Override
	public void run() {
		try {
			Thread.sleep(20000);
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
				Thread.sleep(3000);

				ArrayList<Integer> trackerList = trackerController.getTrackerList();
				Object[][] tableData = new Object[trackerList.size()][1];
				for (int i=0;i<tableData.length;i++){
					for(int j=0;j<tableData[0].length;j++){
						tableData[i][j] = trackerList.get(i);
					}
				}
				//view.setDataTabla1(new Object[][] { { myTracker.getID() }});
				view.setDataTabla1(tableData);
				view.setModelTrackers(new DefaultTableModel(view.getDataTabla1(), view.getColumnsTabla1()) {
					@Override
					public boolean isCellEditable(int row, int column) {
						return false;
					}

					//aqui falta columnclasstabla1
				});

				view.setTableTrackers(new JTable(view.getModelTrackers())); 
				JScrollPane scrollPaneTabla1 = new JScrollPane(view.getTableTrackers());
				scrollPaneTabla1.setBounds(32, 28, 452, 431);
				view.getPanel2().add(scrollPaneTabla1);
				view.getTableTrackers().repaint();
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}

