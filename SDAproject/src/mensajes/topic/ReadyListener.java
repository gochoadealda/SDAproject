package mensajes.topic;

import javax.jms.Message;
import javax.jms.MessageListener;

import org.apache.activemq.command.ActiveMQTextMessage;

import controller.TrackerController;
import modelo.Tracker;

public class ReadyListener implements MessageListener{
	private TrackerController trackerController;
	
	
	public ReadyListener(Tracker model) {
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
					if(trackerController.isMaster()) {
						trackerController.getModel().okRecieve.start();
					}else {
						trackerController.getModel().okSend.start();
					}
					trackerController.setReady(true);
				}

			}catch (Exception ex) {
				System.err.println("# TopicListener error: " + ex.getMessage());
			}
		}
	}



}
