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
import modelo.Swarm;
import modelo.Tracker;

public class OkErrorReceiver extends Thread{
	private TrackerController trackerController;

	public OkErrorReceiver(Tracker model) {
		super();
		this.trackerController = new TrackerController(model);
	}

	@Override
	public void run(){
		System.out.println("OK master");
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


			Thread.sleep(1000);

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
				System.out.println("Tamaño die list"+dieList.size());
				if(dieList.size()>0) {
					trackerController.getModel().dieSend = new DieSender(trackerController.getModel());
					trackerController.getModel().dieSend.start();
				}
				int oks = trackerController.getModel().ok;
				int errs = trackerController.getModel().error;
				System.out.println(oks);
				System.out.println(errs);
				if((oks>0||errs>0)) {
					System.out.println("Recive oks y errs");
					if(oks>=errs) {
						int event = trackerController.getModel().getPeer().getEvent();
						if(event ==2) {
							Swarm s = trackerController.getModel().getTrackerDB().selectSwarm(trackerController.getModel().getPeer().getIdSwarm());
							if(s == null) {
								Swarm sw = new Swarm(trackerController.getModel().getPeer().getIdSwarm());
								trackerController.getModel().getTrackerDB().insertS(sw);
							}else {
								Swarm swa = trackerController.getModel().getTrackerDB().selectSwarm(trackerController.getModel().getPeer().getIdSwarm());
								swa.setLeechers(swa.getLeechers()+1);
								trackerController.getModel().getTrackerDB().updateS(swa);;
							}
							trackerController.getModel().getTrackerDB().insertP(trackerController.getModel().getPeer());
						}else if(event == 0) {
							trackerController.getModel().getTrackerDB().updateP(trackerController.getModel().getPeer());
						}else if(event == 1) {
							Swarm s = trackerController.getModel().getTrackerDB().selectSwarm(trackerController.getModel().getPeer().getIdSwarm());
							s.setSeeders(s.getSeeders()+1);
							s.setLeechers(s.getLeechers()-1);
							trackerController.getModel().getTrackerDB().updateS(s);
							trackerController.getModel().getTrackerDB().updateP(trackerController.getModel().getPeer());
						}else if(event == 3) {
							trackerController.getModel().getTrackerDB().deleteP(trackerController.getModel().getPeer().getID());
						}
						trackerController.getModel().updateSend = new UpdatePublisher("UPDATE", trackerController);
						trackerController.getModel().updateSend.start();
					}else if(oks<errs) {
						trackerController.getModel().updateSend = new UpdatePublisher("NO UPDATE", trackerController);
						trackerController.getModel().updateSend.start();
					}
				}
				trackerController.getModel().okRecieve = null;

			} catch (Exception ex) {
				System.err.println("# QueueReceiverTest Error: " + ex.getMessage());
			}
		}
	}
}
