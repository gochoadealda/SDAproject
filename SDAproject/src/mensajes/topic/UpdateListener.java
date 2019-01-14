package mensajes.topic;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.activemq.command.ActiveMQTextMessage;



import controller.TrackerController;
import modelo.Swarm;

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
							Swarm s = myTracker.getModel().getTrackerDB().selectSwarm(myTracker.getModel().getPeer().getIdSwarm());
							if(s == null) {
								Swarm sw = new Swarm(myTracker.getModel().getPeer().getIdSwarm());
								myTracker.getModel().getTrackerDB().insertS(sw);
							}else {
								Swarm swa = myTracker.getModel().getTrackerDB().selectSwarm(myTracker.getModel().getPeer().getIdSwarm());
								swa.setLeechers(swa.getLeechers()+1);
								myTracker.getModel().getTrackerDB().updateS(swa);;
							}
							myTracker.getModel().getTrackerDB().insertP(myTracker.getModel().getPeer());
						}else if(event == 0) {
							myTracker.getModel().getTrackerDB().updateP(myTracker.getModel().getPeer());
						}else if(event == 1) {
							Swarm s = myTracker.getModel().getTrackerDB().selectSwarm(myTracker.getModel().getPeer().getIdSwarm());
							s.setSeeders(s.getSeeders()+1);
							s.setLeechers(s.getLeechers()-1);
							myTracker.getModel().getTrackerDB().updateS(s);
							myTracker.getModel().getTrackerDB().updateP(myTracker.getModel().getPeer());
						}else if(event == 3) {
							myTracker.getModel().getTrackerDB().deleteP(myTracker.getModel().getPeer().getID());
						}
					}else if(mes == "NO UPDATE") {
						System.out.println("No update");
					}
				}

			}catch (Exception ex) {
				System.err.println("# TopicListener error: " + ex.getMessage());
			}
		}
		
	}



}
