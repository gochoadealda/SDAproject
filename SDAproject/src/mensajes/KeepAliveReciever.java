package mensajes;

import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;
import javax.naming.Context;
import javax.naming.InitialContext;




public class KeepAliveReciever extends Thread{

	private boolean active;
	
	public KeepAliveReciever(boolean active) {
		super();
		this.active = active;
	}

	@Override
	public void run() {
		String connectionFactoryName = "TopicConnectionFactory";
		//This name is defined in jndi.properties file
		String topicJNDIName = "jndi.keepalive.topic";
		
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
			topicConnection.setClientID("SSDD_TopicSubscriber");
			System.out.println("- Topic Connection created!");
			
			//Sessions
			topicSession = topicConnection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
			System.out.println("- Topic Session created!");

			//Define a non-durable connection using a filter (the filter is optional)
			topicNONDurableSubscriber = topicSession.createSubscriber(myTopic);
			
			//Topic Listener
			KeepAliveListener listener = new KeepAliveListener();
			
			//Set the same message listener for the non-durable subscriber
			topicNONDurableSubscriber.setMessageListener(listener);
			
			//Begin message delivery
			topicConnection.start();
			Thread.sleep(2000);
			while(active) {
				System.out.println("- Waiting 1 second for messages...");
				Thread.sleep(1000);
			}
			
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
