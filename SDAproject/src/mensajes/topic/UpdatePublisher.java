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

public class UpdatePublisher extends Thread{
	private String update;
	private TrackerController myTracker;

	public UpdatePublisher(String update, TrackerController myTracker) {
		super();
		this.update = update;
		this.myTracker = myTracker;
	}

	@Override
	public void run() {
		System.out.println("Update master");
		String connectionFactoryName = "TopicConnectionFactory";
		//This name is defined in jndi.properties file
		String topicJNDIName = "jndi.update.topic";		

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
			System.out.println("- Topic Connection created update!");

			//Session
			topicSession = topicConnection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
			System.out.println("- Topic Session created update!");

			//Message Publisher
			topicPublisher = topicSession.createPublisher(myTopic);
			System.out.println("- TopicPublisher created update!");
			//Text Message
			TextMessage textMessage = topicSession.createTextMessage();
			//Message Body
			textMessage.setText(update);
			topicPublisher.publish(textMessage);
			System.out.println("- TextMessage sent to the Queue update!");



		} catch (Exception e) {
			System.err.println("# TopicPublisherTest Error update: " + e.getMessage());
		} finally {
			try {
				//Close resources
				topicPublisher.close();
				topicSession.close();
				topicConnection.close();
				myTracker.getModel().updateSend = null;
				System.out.println("- Topic resources closed update!");				
			} catch (Exception ex) {
				System.err.println("# TopicPublisherTest Error update: " + ex.getMessage());
			}			
		}
	}
}


