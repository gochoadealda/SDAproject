package mensajes;

import java.util.ArrayList;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.jms.TopicConnection;

import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.messaging.simp.broker.SubscriptionRegistry;

import modelo.Tracker;

public class KeepAliveListener implements MessageListener {
	
	private Tracker myTracker;
	
	public KeepAliveListener(Tracker myTracker) {
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
					System.out.println(((TextMessage)message).getText());
					String idString = ((TextMessage)message).getText();
					int arrivedID = Integer.parseInt(idString.substring(10));
					ArrayList<Integer> IDlist = myTracker.getTrackerList();
					System.out.println(myTracker.getID());
					System.out.println(IDlist.size());
					if (myTracker.getID() != arrivedID) {
						int ID;
						int i = 0;
						boolean count=true;
						if (IDlist.size()==0) {
							myTracker.setTrackerList(arrivedID);
						} else {
							
							
							boolean save = true;
							while (i < IDlist.size() && count) {
								ID = IDlist.get(i);
								if (ID == arrivedID) {
									save = false;
									count = false;
								}
								i++;
							}
							if (save == true) {
								myTracker.setTrackerList(arrivedID);
							}
						}
					}
				} 
			
			} catch (Exception ex) {
				System.err.println("# TopicListener error: " + ex.getMessage());
			}
		}
		
	}

}
