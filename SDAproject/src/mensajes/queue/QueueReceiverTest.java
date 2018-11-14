package mensajes.queue;

import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;

public class QueueReceiverTest {	
	
	public static void main(String[] args) {
		String connectionFactoryName = "QueueConnectionFactory";
		//This name is defined in jndi.properties file
		String queueJNDIName = "jndi.ssdd.queue";
		
		QueueConnection queueConnection = null;
		QueueSession queueSession = null;
		QueueReceiver queueReceiver = null;			
		
		try{
			//JNDI Initial Context
			Context ctx = new InitialContext();
		
			//Connection Factory
			QueueConnectionFactory queueConnectionFactory = (QueueConnectionFactory) ctx.lookup(connectionFactoryName);			
			
			//Message Destination
			Queue myQueue = (Queue) ctx.lookup(queueJNDIName);			
	
			//Connection	
			queueConnection = queueConnectionFactory.createQueueConnection();
			System.out.println("- Queue Connection created!");
			
			//Session
			queueSession = queueConnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);			
			System.out.println("- Queue Session created!");
			
			//Define a message Receiver using a filter (the filter is optional)
			queueReceiver = queueSession.createReceiver(myQueue, "Filter = '2'");
			System.out.println("- QueueReceiver created!");
			
			//Set the message lister for the receiver
			QueueMessageListener listener = new QueueMessageListener();			
			queueReceiver.setMessageListener(listener);
			
			//Start receiving messages
			queueConnection.start();
			
			//Wait 10 seconds for messages. After that period the program stops.
			System.out.println("- Waiting 10 seconds for messages...");
			Thread.sleep(10000);
		} catch (Exception e) {
			System.err.println("# QueueReceiverTest Error: " + e.getMessage());
		} finally {
			try {
				//Close resources
				queueReceiver.close();
				queueSession.close();
				queueConnection.close();
				System.out.println("- Queue resources closed!");				
			} catch (Exception ex) {
				System.err.println("# QueueReceiverTest Error: " + ex.getMessage());
			}
		}
	}
}