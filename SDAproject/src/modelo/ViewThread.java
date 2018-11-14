package modelo;

import vista.MainMenu;

public class ViewThread extends Thread{
	private Tracker myTracker;

	public ViewThread(Tracker myTracker) {
		super();
		this.myTracker = myTracker;
	}

	@Override
	public void run() {
		MainMenu view = new MainMenu(myTracker);
		view.setVisible(true);
	}
	
}
