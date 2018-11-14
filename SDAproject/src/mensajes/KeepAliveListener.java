package mensajes;

import java.util.ArrayList;
import java.util.HashMap;

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
					System.out.println(myTracker.isMaster());
					System.out.println(myTracker.getID());
					System.out.println(IDlist.size());
					int ID = 0;
					int i = 0;
					boolean count=true;
					System.currentTimeMillis();
					if (IDlist.size()==0) {
						myTracker.setTrackerList(arrivedID);
						myTracker.putTrackerMap(arrivedID, System.currentTimeMillis());
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
						if (save) {
							myTracker.setTrackerList(arrivedID);
							myTracker.putTrackerMap(arrivedID, System.currentTimeMillis());
						} else {
							myTracker.putTrackerMap(arrivedID, System.currentTimeMillis());
						}
					}
					i=0;
					count = true;
					boolean delete = false;
					HashMap<Integer, Long> myMap = myTracker.getTrackerMap();
					while(i < IDlist.size() && count) {
						ID = IDlist.get(i);
						i++;
						if(myMap.get(ID) - System.currentTimeMillis() > 6000) {
							count = false;
							delete = true;
						}
					}
					if (delete) {
						myTracker.deleteIDfromList(i, ID);
					}
					
				} 
			
			} catch (Exception ex) {
				System.err.println("# TopicListener error: " + ex.getMessage());
			}
		}
		
	}

}
