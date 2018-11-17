package mensajes.queue;

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

public class DieReceiver extends Thread{

	private boolean active;
	private TrackerController trackerController;
	
	public DieReceiver(boolean active, Tracker model) {
		super();
		this.active = active;
		this.trackerController = new TrackerController(model);
	}
	
	@Override 
	public void run(){
		String connectionFactoryName = "QueueConnectionFactory";
		String queueJNDIName = "jndi.die.queue";
		
		QueueConnection queueConnection = null;
		QueueSession queueSession = null;
		QueueReceiver queueReceiver = null;	
		
		try{
			
			Context ctx = new InitialContext();
		
			QueueConnectionFactory queueConnectionFactory = (QueueConnectionFactory) ctx.lookup(connectionFactoryName);			
			
			Queue myQueue = (Queue) ctx.lookup(queueJNDIName);			

			queueConnection = queueConnectionFactory.createQueueConnection();
			System.out.println("- Queue Connection created!");
			
			queueSession = queueConnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);			
			System.out.println("- Queue Session created!");
			
			
			OkErrorListener listener = new OkErrorListener(trackerController.getModel());			
			queueReceiver.setMessageListener(listener);
			
			
			queueConnection.start();
			
			while(active) {
				
			}
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
