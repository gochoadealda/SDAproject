package mensajes.queue;

import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;

import controller.TrackerController;
import mensajes.topic.UpdateSubscriber;
import modelo.Tracker;

public class OkErrorSender extends Thread {
	
	
	private TrackerController trackerController;
	
	public OkErrorSender( Tracker model) {
		super();
		this.trackerController = new TrackerController(model);
	}
	
	@Override
	public void run() {		
		String connectionFactoryName = "QueueConnectionFactory";
		String queueJNDIName = "jndi.okerror.queue";		
		
		QueueConnection queueConnection = null;
		QueueSession queueSession = null;
		QueueSender queueSender = null;			
		
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
	
			//Message Producer
			queueSender = queueSession.createSender(myQueue);			
			System.out.println("- QueueSender created!");
			TextMessage textMessage = queueSession.createTextMessage();
			//OK
			if(trackerController.isActive() && trackerController.isMulticast() && trackerController.isReady()){
				textMessage.setText("OK "+ trackerController.getID());
				queueSender.send(textMessage);
				System.out.println("- TextMessage sent to the Queue!");
				
			//ERROR
//TODO si mandas keepalives y has recibido el mensaje de multicast del peer PERO no ha recibido el Ready del master
			}else if(trackerController.isActive() && trackerController.isMulticast() && !trackerController.isReady()){ 
				textMessage.setText("ER "+ trackerController.getID());
				queueSender.send(textMessage);
				System.out.println("- TextMessage sent to the Queue!");
			}
			

		} catch (Exception e) {
			System.err.println("# QueueOkErrorSenderTest Error: " + e.getMessage());
		} finally {
			try {
				
				queueSender.close();
				queueSession.close();
				queueConnection.close();
				System.out.println("- Queue resources closed!");	
				trackerController.getModel().okSend = null;
				trackerController.getModel().updateRecieve = new UpdateSubscriber(trackerController);
				trackerController.getModel().updateRecieve.start();
			} catch (Exception ex) {
				System.err.println("# QueueOkErrorSenderTest Error: " + ex.getMessage());
			}			
		}
	}
}