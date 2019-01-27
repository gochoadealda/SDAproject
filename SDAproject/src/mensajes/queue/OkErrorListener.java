package mensajes.queue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.activemq.command.ActiveMQTextMessage;

import controller.TrackerController;
import mensajes.topic.UpdatePublisher;
import modelo.Tracker;

public class OkErrorListener implements MessageListener {

	private TrackerController trackerController;
	private int ok,error;

	private ArrayList<Integer> IDlist;

	public OkErrorListener(Tracker model) {
		super();
		this.trackerController = new TrackerController(model);
		Iterator<Integer> list = trackerController.getTrackerList().iterator();
		this.IDlist = new ArrayList<>();
		while(list.hasNext()) {
			int id = list.next();
			if(id != trackerController.getID()) {
				this.IDlist.add(id);
			}
		}
	}

	public void onMessage(Message message) {
		if (message != null) {

			try {
				System.out.println("   - OkErrorQueueListener: " + message.getClass().getSimpleName() + " received!");

				if (message.getClass().getCanonicalName().equals(ActiveMQTextMessage.class.getCanonicalName())) {
					System.out.println(((TextMessage)message).getText());
					String messageString = ((TextMessage)message).getText();
					int arrivedID = Integer.parseInt(messageString.substring(3));
					String texto = messageString.substring(0,2);
					System.out.println(arrivedID);
					System.out.println(texto);
					for(int i=0; i<IDlist.size(); i++){
						if(IDlist.get(i)==arrivedID) {
							IDlist.remove(i);
						}
					}
					
					trackerController.setTrackerListDIE(IDlist);
					if(texto.equals("OK")) {
						trackerController.getModel().ok++;
					}else {
						trackerController.getModel().error++;
					}
				}
			}catch (Exception ex) {
				System.err.println("# TopicListener error okErr: " + ex.getMessage());
			}
		}
	}
}



