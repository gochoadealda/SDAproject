package mensajes.topic;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

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
					String mes = ((TextMessage)message).getText();
					System.out.println(mes);
					if(mes.equals("Ready")) {
						trackercontroller.setReady(true);
					}else {
						trackercontroller.setReady(false);
					}
				}else {
					trackercontroller.setReady(false);
				}
				trackercontroller.getModel().okSend = new OkErrorSender(trackercontroller.getModel());
				trackercontroller.getModel().okSend.start();
			}catch (Exception ex) {
				System.err.println("# TopicListener error: " + ex.getMessage());
			}
		}
	}



}
