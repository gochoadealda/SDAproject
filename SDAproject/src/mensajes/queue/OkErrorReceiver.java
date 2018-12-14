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

public class OkErrorReceiver extends Thread{
	private TrackerController trackerController;

	public OkErrorReceiver(Tracker model) {
		super();
		this.trackerController = new TrackerController(model);
	}

	@Override
	public void run(){
<<<<<<< HEAD
	String connectionFactoryName = "QueueConnectionFactory";
	String queueJNDIName = "jndi.okerror.queue";
	
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
		
		queueReceiver = queueSession.createReceiver(myQueue);
		System.out.println("- QueueReceiver created!");
		
		OkErrorListener listener = new OkErrorListener(trackerController.getModel());			
		queueReceiver.setMessageListener(listener);
		
		
		queueConnection.start();
		
		
		while(trackerController.isActive()) {
			
		}
=======
		String connectionFactoryName = "QueueConnectionFactory";
		String queueJNDIName = "jndi.okerror.queue";

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

			queueReceiver = queueSession.createReceiver(myQueue);
			System.out.println("- QueueReceiver created!");

			OkErrorListener listener = new OkErrorListener(trackerController.getModel());			
			queueReceiver.setMessageListener(listener);


			queueConnection.start();

			//while(active) {

			//}
>>>>>>> 91242d79ffda633c4da6a2ede577e9f15b8baa19
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
