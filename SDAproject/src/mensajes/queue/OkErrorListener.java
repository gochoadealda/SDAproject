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
		this.IDlist = trackerController.getTrackerList();
	}

	public void onMessage(Message message) {
		if (message != null) {

			try {
				System.out.println("   - OkErrorQueueListener: " + message.getClass().getSimpleName() + " received!");

				if (message.getClass().getCanonicalName().equals(ActiveMQTextMessage.class.getCanonicalName())) {
					System.out.println(((TextMessage)message).getText());
					String messageString = ((TextMessage)message).getText();
					int arrivedID = Integer.parseInt(messageString.substring(3));
					String texto = messageString.substring(0,1);
					
					for(int i=0; i<IDlist.size(); i++){
						if(IDlist.get(i)==arrivedID) {
							IDlist.remove(i);
						}
					}
					
					trackerController.setTrackerListDIE(IDlist);
					if(texto == "OK") {
						trackerController.getModel().ok++;
					}else {
						trackerController.getModel().error++;
					}
				}
			}catch (Exception ex) {
				System.err.println("# TopicListener error: " + ex.getMessage());
			}
		}
	}
}



