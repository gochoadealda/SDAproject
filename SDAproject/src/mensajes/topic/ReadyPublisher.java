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
import mensajes.queue.OkErrorReceiver;
import modelo.Tracker;

public class ReadyPublisher extends Thread {
	private TrackerController trackerController;
	
	public ReadyPublisher(Tracker model) {
		super();
		this.trackerController = new TrackerController(model);
	}

	@Override
	public void run() {
		System.out.println("Ready master");
		String connectionFactoryName = "TopicConnectionFactory";
		//This name is defined in jndi.properties file
		String topicJNDIName = "jndi.ready.topic";		
		
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
			System.out.println("- Topic Connection created ready!");
			
			//Session
			topicSession = topicConnection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
			System.out.println("- Topic Session created ready!");

			//Message Publisher
			topicPublisher = topicSession.createPublisher(myTopic);
			System.out.println("- TopicPublisher created ready!");
			//Text Message
			TextMessage textMessage = topicSession.createTextMessage();
			Thread.sleep(500);
			//Message Body
			textMessage.setText("Ready");
			topicPublisher.publish(textMessage);
			System.out.println("- TextMessage sent to the Queue ready!");
			

			
		} catch (Exception e) {
			System.err.println("# TopicPublisherTest Error ready: " + e.getMessage());
		} finally {
			try {
				//Close resources
				topicPublisher.close();
				topicSession.close();
				topicConnection.close();
				System.out.println("- Topic resources closed ready!");
				trackerController.getModel().readySend = null;
				trackerController.getModel().okRecieve = new OkErrorReceiver(trackerController.getModel());
				trackerController.getModel().okRecieve.start();
			} catch (Exception ex) {
				System.err.println("# TopicPublisherTest Error ready: " + ex.getMessage());
			}			
		}
	}

}
