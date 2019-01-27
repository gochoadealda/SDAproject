package mensajes.topic;

import java.util.UUID;

import javax.jms.Session;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;
import javax.naming.Context;
import javax.naming.InitialContext;

import controller.TrackerController;

public class UpdateSubscriber extends Thread{
	
	private TrackerController myTracker;
	
	

	public UpdateSubscriber(TrackerController myTracker) {
		super();
		this.myTracker = myTracker;
	}



	@Override
	public void run() {
		System.out.println("Update slave");
		String connectionFactoryName = "TopicConnectionFactory";
		//This name is defined in jndi.properties file
		String topicJNDIName = "jndi.update.topic";

		TopicConnection topicConnection = null;
		TopicSession topicSession = null;
		TopicSubscriber topicNONDurableSubscriber = null;			

		try{
			Context ctx = new InitialContext();

			//Connection Factories
			TopicConnectionFactory topicConnectionFactory = (TopicConnectionFactory) ctx.lookup(connectionFactoryName);

			//Message Destinations
			Topic myTopic = (Topic) ctx.lookup(topicJNDIName);			

			//Connections			
			topicConnection = topicConnectionFactory.createTopicConnection();
			//Set an ID to create a durable connection (optional)
			String uuid = UUID.randomUUID().toString();
			System.out.println(uuid);

			topicConnection.setClientID("Client-"+uuid);
			System.out.println("- Topic Connection created update!");

			//Sessions
			topicSession = topicConnection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
			System.out.println("- Topic Session created update!");

			//Define a non-durable connection using a filter (the filter is optional)
			topicNONDurableSubscriber = topicSession.createSubscriber(myTopic);

			//Topic Listener
			UpdateListener listener = new UpdateListener(myTracker);

			//Set the same message listener for the non-durable subscriber
			topicNONDurableSubscriber.setMessageListener(listener);

			//Begin message delivery
			topicConnection.start();
			Thread.sleep(2000);
		} catch (Exception e) {
			System.err.println("# TopicSubscriberTest Error update: " + e.getMessage());			
		} finally {
			try {
				topicNONDurableSubscriber.close();
				topicSession.close();
				topicConnection.close();
				myTracker.getModel().updateRecieve = null;
				System.out.println("- Topic resources closed update!");				
			} catch (Exception ex) {
				System.err.println("# TopicSubscriberTest Error update: " + ex.getMessage());
			}
		}
	}
}


