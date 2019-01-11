package mensajes.queue;

import java.util.ArrayList;

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
import mensajes.topic.UpdatePublisher;
import modelo.Tracker;

public class DieSender extends Thread{

	private TrackerController trackerController;
	
	
	public DieSender(Tracker model) {
		super();
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
			TextMessage textMessage = queueSession.createTextMessage();
			
//TODO Borrar directamente de la lista vivos // mirar la de keepAlive			
			
			for(int i = 0; i < trackerController.getTrackerListDIE().size(); i++) {
				int trackerID = trackerController.getTrackerListDIE().get(i);
				for(int j=0; j < trackerController.getTrackerList().size(); j++) {
					if(trackerController.getID() == trackerID) {
						trackerController.deleteIDfromList(j);
						textMessage.setText("DIE");
						queueSender.send(textMessage);
						System.out.println("- TextMessage sent to the Queue!");
					}
				}		
			}
			//TODO Vaciar el ArrayList trackerListDIE
			ArrayList<Integer> newtrackerListDIE = new ArrayList<>();
			trackerController.setTrackerListDIE(newtrackerListDIE);
			
			String update;
			if(trackerController.getOkVotoUpdate() > trackerController.getErrorVotoUpdate()) {
				update = "UPDATE";
			}else {
				update = "NO UPDATE";
			}
			trackerController.getModel().updateSend = new UpdatePublisher(update);
			trackerController.getModel().updateSend.start();
		
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