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
import modelo.Tracker;

public class ConnectionIDSubscriber extends Thread {
	private TrackerController trackerController;
	
	
	public ConnectionIDSubscriber(Tracker tracker) {
		super();
		this.trackerController = new TrackerController(tracker);
	}


	@Override
	public void run() {
		super.run();
		String connectionFactoryName = "TopicConnectionFactory";
		//This name is defined in jndi.properties file
		String topicJNDIName = "jndi.connectionID.topic";
		
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
			System.out.println("- Topic Connection created!");
			
			//Sessions
			topicSession = topicConnection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
			System.out.println("- Topic Session created!");

			//Define a non-durable connection using a filter (the filter is optional)
			topicNONDurableSubscriber = topicSession.createSubscriber(myTopic);
			
			//Topic Listener
			ConnectionIDListener listener = new ConnectionIDListener(trackerController);
			
			//Set the same message listener for the non-durable subscriber
			topicNONDurableSubscriber.setMessageListener(listener);
			
			//Begin message delivery
			topicConnection.start();
			while(trackerController.isActive()) {
				
			}
		} catch (Exception e) {
			System.err.println("# TopicSubscriberTest Error: " + e.getMessage());			
		} finally {
			try {
				
				topicNONDurableSubscriber.close();
				topicSession.close();
				topicConnection.close();
				System.out.println("- Topic resources closed!");
				trackerController.getModel().conRecieve = null;
			} catch (Exception ex) {
				System.err.println("# TopicSubscriberTest Error: " + ex.getMessage());
			}
		}
	}

}
