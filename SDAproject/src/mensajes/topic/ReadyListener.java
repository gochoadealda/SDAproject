package mensajes.topic;

import javax.jms.Message;
import javax.jms.MessageListener;

import org.apache.activemq.command.ActiveMQTextMessage;

import controller.TrackerController;
import mensajes.queue.OkErrorReceiver;
import mensajes.queue.OkErrorSender;
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
<<<<<<< HEAD
					if(trackerController.isMaster()) {
						trackerController.getModel().okRecieve = new OkErrorReceiver(trackerController.getModel());
						trackerController.getModel().okRecieve.start();
					}else {
						trackerController.getModel().okSend = new OkErrorSender(trackerController.getModel());
						trackerController.getModel().okSend.start();
=======
					if(trackercontroller.isMaster()) {
						trackercontroller.getModel().okRecieve.start();
					}else {
						trackercontroller.getModel().okSend.start();
>>>>>>> 91242d79ffda633c4da6a2ede577e9f15b8baa19
					}
					trackercontroller.setReady(true);
				}

			}catch (Exception ex) {
				System.err.println("# TopicListener error: " + ex.getMessage());
			}
		}
	}



}
