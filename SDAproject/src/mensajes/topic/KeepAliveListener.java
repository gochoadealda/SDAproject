package mensajes.topic;

import java.util.ArrayList;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.activemq.command.ActiveMQTextMessage;

import controller.TrackerController;
import modelo.Tracker;

public class KeepAliveListener implements MessageListener {
	
	private TrackerController trackerController;
	
	public KeepAliveListener(Tracker model) {
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
					System.out.println(((TextMessage)message).getText());
					String idString = ((TextMessage)message).getText();
					int arrivedID = Integer.parseInt(idString.substring(10));

					ArrayList<Integer> IDlist = trackerController.getTrackerList();
					//System.out.println("Is master "+trackerController.isMaster());
					//System.out.println("Master ID "+trackerController.getMasterID());
					//System.out.println("My ID "+trackerController.getID());
					//System.out.println("Tracker list size "+IDlist.size());
					//System.out.println("Times list size "+trackerController.getTimeList().size());

					int ID = 0;
					int i = 0;
					boolean count=true;
					long actualtime;
					if (IDlist.size()==0) {
						trackerController.setTrackerList(arrivedID);
						actualtime = System.currentTimeMillis();
						trackerController.addTimeList(actualtime);
						System.out.println(trackerController.getTimeList().size());
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
							trackerController.setTrackerList(arrivedID);
							actualtime = System.currentTimeMillis();
							trackerController.addTimeList(actualtime);
							if (trackerController.isMaster()) {
								trackerController.sendDB();
								trackerController.getModel().conSend = new ConnectionIDPublisher(trackerController.getConnectionIDs(), trackerController.getTransactionIDs(), trackerController.getOldConnectionIDs(),true);
								trackerController.getModel().conSend.start();
							}
						} else {
							actualtime = System.currentTimeMillis();
							trackerController.setTimeList(i-1, actualtime);
						}
					}
					
					i=0;
					count = true;
					boolean delete = false;
					ArrayList<Long> myTimeList = trackerController.getTimeList();
					while(i < myTimeList.size() && count) {
						long timenow = System.currentTimeMillis();
						System.out.println(timenow - myTimeList.get(i));
						if(timenow - myTimeList.get(i) > 2000) {
							count = false;
							delete = true;
						}
						i++;
					}
					if (delete) {
						trackerController.deleteIDfromList(i-1);
					}
					
				} 
			
			} catch (Exception ex) {
				System.err.println("# TopicListener error: " + ex.getMessage());
			}
		}
		
	}

}