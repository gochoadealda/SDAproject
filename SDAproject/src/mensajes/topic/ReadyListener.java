package mensajes.topic;

import javax.jms.Message;
import javax.jms.MessageListener;

import org.apache.activemq.command.ActiveMQTextMessage;

import controller.TrackerController;
import modelo.Tracker;

public class ReadyListener implements MessageListener{
	private TrackerController trackercontroller;
	
	
	public ReadyListener(Tracker model) {
		this.trackercontroller = new TrackerController(model);
	}


	@Override
	public void onMessage(Message message) {
		if (message != null) {

			try {
				System.out.println("   - TopicListener: " + message.getClass().getSimpleName() + " received!");

				//Depending on the type of the message the process is different
				if (message.getClass().getCanonicalName().equals(ActiveMQTextMessage.class.getCanonicalName())) {
					if(trackercontroller.isMaster()) {
						trackercontroller.getModel().okRecieve.start();
					}else {
						trackercontroller.getModel().okSend.start();
					}
					trackercontroller.setReady(true);
				}

			}catch (Exception ex) {
				System.err.println("# TopicListener error: " + ex.getMessage());
			}
		}
	}



}
