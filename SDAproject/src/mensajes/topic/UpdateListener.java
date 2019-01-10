package mensajes.topic;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.activemq.command.ActiveMQTextMessage;

import controller.TrackerController;

public class UpdateListener implements MessageListener{
	
	private TrackerController myTracker;
	

	public UpdateListener(TrackerController myTracker) {
		super();
		this.myTracker = myTracker;
	}


	@Override
	public void onMessage(Message message) {
		if (message != null) {

			try {
				System.out.println("   - TopicListener: " + message.getClass().getSimpleName() + " received!");

				//Depending on the type of the message the process is different
				if (message.getClass().getCanonicalName().equals(ActiveMQTextMessage.class.getCanonicalName())) {
					String mes = ((TextMessage)message).getText();
					if(mes == "UPDATE") {
						int event = myTracker.getModel().getPeer().getEvent();
						if(event ==2) {
							//Comprobar si hay el swarm al que pertenece el peer si si hay guardar y actualizar el swarm si no hay crear el swarm en la bd y actualizarlo
							myTracker.getModel().getTrackerDB().insertP(myTracker.getModel().getPeer());
						}else if(event == 0) {
							myTracker.getModel().getTrackerDB().updateP(myTracker.getModel().getPeer());
						}else if(event == 1) {
							
						}else if(event == 3) {
							myTracker.getModel().getTrackerDB().deleteP(myTracker.getModel().getPeer().getID());
						}
					}else if(mes == "NO UPDATE") {
						
					}
				}

			}catch (Exception ex) {
				System.err.println("# TopicListener error: " + ex.getMessage());
			}
		}
		
	}



}
