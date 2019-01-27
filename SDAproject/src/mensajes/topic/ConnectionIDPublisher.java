package mensajes.topic;

import java.util.HashMap;

import javax.jms.MapMessage;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.camel.model.dataformat.XStreamDataFormat;

import controller.TrackerController;

public class ConnectionIDPublisher extends Thread {
	private String message;
	private long connectionID;
	private TrackerController trackerController;
	private HashMap<String, Long> connectionIDs;
	private HashMap<String, Integer> transactionIDs;
	private HashMap<String, Long> oldConnectionIDs;
	private boolean type;

	public ConnectionIDPublisher(String message, long connectionID, TrackerController trackerController, boolean type) {
		super();
		this.message = message;
		this.connectionID = connectionID;
		this.trackerController = trackerController;
		this.type = type;
	}

	public ConnectionIDPublisher(HashMap<String, Long> connectionIDs, HashMap<String, Integer> transactionIDs, HashMap<String, Long> oldConnectionIDs, boolean type) {
		this.connectionIDs = connectionIDs;
		this.transactionIDs = transactionIDs;
		this.oldConnectionIDs = oldConnectionIDs;
		this.type = type;
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
			System.out.println("- Topic Connection created connection!");

			//Session
			topicSession = topicConnection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
			System.out.println("- Topic Session created connection!");

			//Message Publisher
			topicPublisher = topicSession.createPublisher(myTopic);
			System.out.println("- TopicPublisher created connection!");
			if(type) {
				ObjectMessage obMessage = topicSession.createObjectMessage();
				obMessage.setObject(connectionIDs);
				topicPublisher.publish(obMessage);
				obMessage.setObject(transactionIDs);
				topicPublisher.publish(obMessage);
				obMessage.setObject(oldConnectionIDs);
				topicPublisher.publish(obMessage);
			}else {
				//Map Message
				MapMessage conIDMessage = topicSession.createMapMessage();
				//Message Body
				conIDMessage.setString("ID", message);
				conIDMessage.setLong("ConID", connectionID);
				topicPublisher.publish(conIDMessage);
			}
			System.out.println("- TextMessage sent to the Queue connection!");



		} catch (Exception e) {
			System.err.println("# TopicPublisherTest Error connection: " + e.getMessage());
		} finally {
			try {
				//Close resources

				topicPublisher.close();
				topicSession.close();
				topicConnection.close();
				System.out.println("- Topic resources closed connection!");

				trackerController.getModel().conSend = null;
			} catch (Exception ex) {
				System.err.println("# TopicPublisherTest Error connection: " + ex.getMessage());
			}			
		}
	}

}
