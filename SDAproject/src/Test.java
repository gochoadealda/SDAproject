import java.util.ArrayList;

import modelo.Tracker;

public class Test {

	public static void main(String[] args) {
		Tracker tracker1 = new Tracker("120", 1, null);
		tracker1.start(tracker1);
	}

}
