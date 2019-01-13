package mensajes.queue;

import java.util.ArrayList;

import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;

import controller.TrackerController;
import mensajes.topic.UpdatePublisher;
import modelo.Tracker;

public class OkErrorReceiver extends Thread{
	private TrackerController trackerController;

	public OkErrorReceiver(Tracker model) {
		super();
		this.trackerController = new TrackerController(model);
	}

	@Override
	public void run(){
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

		} catch (Exception e) {
			System.err.println("# QueueReceiverTest Error: " + e.getMessage());
		} finally {
			try {
				//Close resources
				queueReceiver.close();
				queueSession.close();
				queueConnection.close();
				System.out.println("- Queue resources closed!");				
				
//TODO aqui generar los DIE y Update
				ArrayList<Integer> dieList = trackerController.getTrackerListDIE();
				if(dieList.size()>0) {
					trackerController.getModel().dieSend = new DieSender(trackerController.getModel());
					trackerController.getModel().dieSend.start();
				}
				int oks = trackerController.getModel().ok;
				int errs = trackerController.getModel().error;
				if(oks>=errs) {
					trackerController.getModel().updateSend = new UpdatePublisher("UPDATE");
					trackerController.getModel().updateSend.start();
				}else if(oks<errs) {
					trackerController.getModel().updateSend = new UpdatePublisher("NO UPDATE");
					trackerController.getModel().updateSend.start();
				}
				
			} catch (Exception ex) {
				System.err.println("# QueueReceiverTest Error: " + ex.getMessage());
			}
		}
	}
}
