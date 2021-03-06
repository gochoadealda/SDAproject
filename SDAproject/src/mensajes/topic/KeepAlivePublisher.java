package mensajes.topic;

import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.naming.Context;
import javax.naming.InitialContext;

import controller.TrackerController;
import modelo.Tracker;



public class KeepAlivePublisher extends Thread{
	
	private TrackerController trackerController;
	
	
	public KeepAlivePublisher(Tracker model) {
		super();
		this.trackerController = new TrackerController(model);
	}


	@Override
	public void run() {
		String connectionFactoryName = "TopicConnectionFactory";
		//This name is defined in jndi.properties file
		String topicJNDIName = "jndi.keepalive.topic";		
		
		TopicConnection topicConnection = null;
		TopicSession topicSession = null;
		TopicPublisher topicPublisher = null;			
		
		try{
			//JNDI Initial Context
			Context ctx = new InitialContext();
		
			//Connection Factory
			TopicConnectionFactory topicConnectionFactory = (TopicConnectionFactory) ctx.lookup(connectionFactoryName);
			
			//Message Destination
			Topic myTopic = (Topic) ctx.lookup(topicJNDIName);
			
			//Connection			
			topicConnection = topicConnectionFactory.createTopicConnection();
			System.out.println("- Topic Connection created!");
			
			//Session
			topicSession = topicConnection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
			System.out.println("- Topic Session created!");

			//Message Publisher
			topicPublisher = topicSession.createPublisher(myTopic);
			System.out.println("- TopicPublisher created!");
			
			
			
			while(trackerController.isActive()) {
			
				//Text Message
				TextMessage textMessage = topicSession.createTextMessage();
				//Message Body
				textMessage.setText("KeepAlive " + trackerController.getID());
				topicPublisher.publish(textMessage);
				System.out.println("- TextMessage sent to the Queue!");
				Thread.sleep(5000);
			}
			
		} catch (Exception e) {
			System.err.println("# TopicPublisherTest Error: " + e.getMessage());
		} finally {
			try {
				//Close resources

				trackerController.getTrackerDB().closeConnection();
				trackerController.getTrackerDB().deleteDatabase();	

				topicPublisher.close();
				topicSession.close();
				topicConnection.close();
				System.out.println("- Topic resources closed!");

				trackerController.getModel().trackerView = null;
				trackerController.getModel().kaSend = null;
			} catch (Exception ex) {
				System.err.println("# TopicPublisherTest Error: " + ex.getMessage());
			}			
		}
	}

}