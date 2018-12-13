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

public class DieSender extends Thread{

	private boolean active;
	private TrackerController trackerController;
	
	
	public DieSender(boolean active, Tracker model) {
		super();
		this.active = active;
		this.trackerController = new TrackerController(model);
	}
	
	@Override
	public void run() {		
		String connectionFactoryName = "QueueConnectionFactory";
		String queueJNDIName = "jndi.die.queue";		
		
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
				//Preguntar si esta alive
				if(tiene el keepalive){
				no mandar die				
				
				//No esta alive
				}else if(no devuelve keepalive){
					mandar die	
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
				trackerController.getModel().dieSend = null;
			} catch (Exception ex) {
				System.err.println("# QueueOkErrorSenderTest Error: " + ex.getMessage());
			}			
		}
	}
}