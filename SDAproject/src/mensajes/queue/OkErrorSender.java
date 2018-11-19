package mensajes.queue;

import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;

import controller.TrackerController;
import modelo.Tracker;

public class OkErrorSender extends Thread {
	
	
	private TrackerController trackerController;
	
	
	public OkErrorSender(boolean active, Tracker model) {
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
			
			/*
				//OK
				if(OK){
				
				TextMessage textMessage = queueSession.createTextMessage();
				textMessage.setText("OK "+ myTracker.getID());
				queueSender.send(textMessage);
				System.out.println("- TextMessage sent to the Queue!");
				Thread.sleep(5000);
				
				//ERROR
				}else if(ERROR){
					TextMessage textMessage = queueSession.createTextMessage();
					textMessage.setText("ER "+ myTracker.getID());
					queueSender.send(textMessage);
					System.out.println("- TextMessage sent to the Queue!");
					Thread.sleep(5000);	
				}
			*/
			
			
		} catch (Exception e) {
			System.err.println("# QueueOkErrorSenderTest Error: " + e.getMessage());
		} finally {
			try {
				
				queueSender.close();
				queueSession.close();
				queueConnection.close();
				System.out.println("- Queue resources closed!");	
				trackerController.getModel().kaSend = null;
			} catch (Exception ex) {
				System.err.println("# QueueOkErrorSenderTest Error: " + ex.getMessage());
			}			
		}
	}
}