package mensajes.fileMessage;

import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;

import controller.TrackerController;
import modelo.Tracker;

public class DBQueueFileReceiver extends Thread{
	
	private TrackerController trackerController;
	public QueueConnection queueConnection;
	public QueueSession queueSession;
	public QueueReceiver queueReceiver;
	
	public DBQueueFileReceiver(Tracker model) {
		super();
		this.trackerController = new TrackerController(model);
	}


	@Override
	public void run() {
		
		String connectionFactoryName = "QueueConnectionFactory";
		String queueJNDIName = "jndi.senddb.fileQueue";
		
		queueConnection = null;
		queueSession = null;
		queueReceiver = null;			
		
		try{
			//JNDI Initial Context
			Context ctx = new InitialContext();
		
			//Connection Factory
			QueueConnectionFactory queueConnectionFactory = (QueueConnectionFactory) ctx.lookup(connectionFactoryName);			
			
			//Message Destination
			Queue myQueue = (Queue) ctx.lookup(queueJNDIName);			
	
			//Connection	
			queueConnection = queueConnectionFactory.createQueueConnection();
			System.out.println("- FileQueue Connection created!");
			
			//Session
			queueSession = queueConnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);			
			System.out.println("- FileQueue Session created!");
			
			//Message Receiver
			queueReceiver = queueSession.createReceiver(myQueue);
			System.out.println("- FileQueueReceiver created!");
						
			DBQueueFileMessageListener listener = new DBQueueFileMessageListener(trackerController.getModel(), this);
			
			queueReceiver.setMessageListener(listener);
			
			//Start receiving messages
			queueConnection.start();
			
			Thread.sleep(3000);
		} catch (Exception e) {
			System.err.println("# QueueReceiverTest Error: " + e.getMessage());
		} finally {
			try {
				trackerController.getModel().recieveDB = null;				
			} catch (Exception ex) {
				System.err.println("# QueueReceiverTest Error: " + ex.getMessage());
			}
		}
	}

}