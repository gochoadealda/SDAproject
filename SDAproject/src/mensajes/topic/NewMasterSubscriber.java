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

public class NewMasterSubscriber extends Thread{
	
	private TrackerController trackerController;

	public NewMasterSubscriber(Tracker model) {
		super();
		this.trackerController = new TrackerController(model);
	}

	@Override
	public void run() {
		String connectionFactoryName = "TopicConnectionFactory";
		//This name is defined in jndi.properties file
		String topicJNDIName = "jndi.newmaster.topic";
		
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
			NewMasterListener listener = new NewMasterListener(trackerController.getModel());
			
			//Set the same message listener for the non-durable subscriber
			topicNONDurableSubscriber.setMessageListener(listener);
			
			//Begin message delivery
			topicConnection.start();
			Thread.sleep(3000);
		} catch (Exception e) {
			System.err.println("# TopicSubscriberTest Error: " + e.getMessage());			
		} finally {
			try {
				topicNONDurableSubscriber.close();
				topicSession.close();
				topicConnection.close();
				System.out.println("- Topic resources closed!");				
			} catch (Exception ex) {
				System.err.println("# TopicSubscriberTest Error: " + ex.getMessage());
			}
		}
	}

}