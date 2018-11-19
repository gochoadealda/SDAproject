package mensajes.topic;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.activemq.command.ActiveMQTextMessage;

import controller.TrackerController;
import modelo.Tracker;

public class NewMasterListener implements MessageListener{

	private TrackerController trackerController;
	
	public NewMasterListener(Tracker model) {
		super();
		this.trackerController = new TrackerController(model);
	}
	
	@Override
	public void onMessage(Message message) {
		if (message != null) {
			try {
				System.out.println("   - TopicListener: " + message.getClass().getSimpleName() + " received!");
				
				//Depending on the type of the message the process is different
				if (message.getClass().getCanonicalName().equals(ActiveMQTextMessage.class.getCanonicalName())) {
					System.out.println("     - TopicListener: TextMessage '" + ((TextMessage)message).getText());
					int arrivedID = Integer.parseInt(((TextMessage)message).getText());
					System.out.println("ID master recivido"+arrivedID);
					trackerController.setMasterID(arrivedID);
				}
			
			} catch (Exception ex) {
				System.err.println("# TopicListener error: " + ex.getMessage());
			}
		}		
		
	}

}