
import controller.TrackerController;
import modelo.Tracker;
import modelo.ViewThread;


public class Test {

	public static void main(String[] args) {
		Tracker tracker1 = new Tracker("120.120.120.120", 1);
		ViewThread trackerView = new ViewThread(tracker1);
		
		TrackerController trackerController= new TrackerController(tracker1, trackerView);
		trackerController.start();
	}

}
