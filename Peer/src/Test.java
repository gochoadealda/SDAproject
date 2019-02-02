
import controller.PeerController;
import modelo.Peer;
import vista.PeerGestor;

public class Test {

	public static void main(String[] args) {
		Peer peer = new Peer("0");
		PeerGestor peerView = new PeerGestor(peer);
		peerView.setVisible(true);
		
		PeerController peerControler = new PeerController(peer, peerView);
		peerControler.start();

		
	}

}
