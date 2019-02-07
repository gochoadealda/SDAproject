
import java.util.concurrent.ThreadLocalRandom;

import controller.PeerController;
import modelo.Peer;
import vista.PeerGestor;

public class Test {

	public static void main(String[] args) {
		int randNum = ThreadLocalRandom.current().nextInt(0, 5463654);
		Peer peer = new Peer(String.valueOf(randNum));
		PeerGestor peerView = new PeerGestor(peer);
		peerView.setVisible(true);
		
		PeerController peerControler = new PeerController(peer, peerView);
		peerControler.start();
	}

}
