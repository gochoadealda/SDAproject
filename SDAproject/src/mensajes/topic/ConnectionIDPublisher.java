package mensajes.topic;

import javax.jms.MapMessage;
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

public class ConnectionIDPublisher extends Thread {
	private String message;
	private long connectionID;
	private TrackerController trackerController;

	public ConnectionIDPublisher(String message, long connectionID, TrackerController trackerController) {
		super();
		this.message = message;
		this.connectionID = connectionID;
		this.trackerController = trackerController;
	}

	@Override
	public void run() {
		super.run();
		String connectionFactoryName = "TopicConnectionFactory";
		//This name is defined in jndi.properties file
		String topicJNDIName = "jndi.connectionID.topic";		

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

			//Text Message
			MapMessage conIDMessage = topicSession.createMapMessage();
			//Message Body
			conIDMessage.setString("ID", message);
			conIDMessage.setLong("ConID", connectionID);
			topicPublisher.publish(conIDMessage);
			System.out.println("- TextMessage sent to the Queue!");
			


		} catch (Exception e) {
			System.err.println("# TopicPublisherTest Error: " + e.getMessage());
		} finally {
			try {
				//Close resources

				topicPublisher.close();
				topicSession.close();
				topicConnection.close();
				System.out.println("- Topic resources closed!");

				trackerController.getModel().conSend = null;
			} catch (Exception ex) {
				System.err.println("# TopicPublisherTest Error: " + ex.getMessage());
			}			
		}
	}

}
