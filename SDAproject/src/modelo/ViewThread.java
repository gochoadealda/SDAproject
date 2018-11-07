package modelo;

import vista.MainMenu;

public class ViewThread extends Thread{
	private MainMenu view;

	public ViewThread(MainMenu view) {
		super();
		this.view = view;
	}

	@Override
	public void run() {
		view.setVisible(true);
	}
	
}
