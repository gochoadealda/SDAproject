package mensajes.topic;

import java.util.ArrayList;
import java.util.HashMap;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.jms.TopicConnection;

import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.messaging.simp.broker.SubscriptionRegistry;

import mensajes.fileMessage.DBQueueFileSender;
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
					System.out.println("Is master "+myTracker.isMaster());
					System.out.println("Master ID "+myTracker.getMasterID());
					System.out.println("My ID "+myTracker.getID());
					System.out.println("Tracker list size "+IDlist.size());
					System.out.println("Times list size "+myTracker.getTimeList().size());
					int ID = 0;
					int i = 0;
					boolean count=true;
					long actualtime;
					if (IDlist.size()==0) {
						myTracker.setTrackerList(arrivedID);
						actualtime = System.currentTimeMillis();
						myTracker.addTimeList(actualtime);
						System.out.println(myTracker.getTimeList().size());
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
							actualtime = System.currentTimeMillis();
							myTracker.addTimeList(actualtime);
							if (myTracker.isMaster()) {
								myTracker.sendDB();
							}
						} else {
							actualtime = System.currentTimeMillis();
							myTracker.setTimeList(i-1, actualtime);
						}
					}
					
					i=0;
					count = true;
					boolean delete = false;
					ArrayList<Long> myTimeList = myTracker.getTimeList();
					while(i < myTimeList.size() && count) {
						long timenow = System.currentTimeMillis();
						System.out.println(timenow - myTimeList.get(i));
						if(timenow - myTimeList.get(i) > 6000) {
							count = false;
							delete = true;
						}
						i++;
					}
					if (delete) {
						myTracker.deleteIDfromList(i-1);
					}
					
				} 
			
			} catch (Exception ex) {
				System.err.println("# TopicListener error: " + ex.getMessage());
			}
		}
		
	}

}
