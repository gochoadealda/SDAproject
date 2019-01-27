package mensajes.topic;

import java.util.HashMap;

import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.apache.activemq.command.ActiveMQMapMessage;
import org.apache.activemq.command.ActiveMQObjectMessage;

import controller.TrackerController;

public class ConnectionIDListener implements MessageListener {
	private TrackerController trackerControler;
	private int cont;
	public ConnectionIDListener(TrackerController trackerControler) {
		super();
		this.trackerControler = trackerControler;
		cont = 0;
	}
	@Override
	public void onMessage(Message message) {
		if (message != null) {

			try {
				System.out.println("   - TopicListener: " + message.getClass().getSimpleName() + " received!");

				//Depending on the type of the message the process is different
				if (message.getClass().getCanonicalName().equals(ActiveMQMapMessage.class.getCanonicalName())) {
					System.out.println("     - TopicListener: MapMessage");				
					MapMessage mapMsg = ((MapMessage) message);
					System.out.println("ID" +mapMsg.getString("ID"));
					System.out.println("ConID" +mapMsg.getLong("ConID"));
					System.out.println("Aqui map");
					System.out.println(trackerControler.getConnectionIDs().get(mapMsg.getString("ID")));
					if(trackerControler.getConnectionIDs().get(mapMsg.getString("ID")) == null) {
						long l = 0;
						trackerControler.getOldConnectionIDs().put(mapMsg.getString("ID"), l);
						trackerControler.getConnectionIDs().put(mapMsg.getString("ID"), mapMsg.getLong("ConID"));
					}else {
						long connectionID = trackerControler.getConnectionIDs().get(mapMsg.getString("ID"));
						System.out.println(connectionID);
						trackerControler.putOldConnectionID(mapMsg.getString("ID"), connectionID);
						trackerControler.putConnectionID(mapMsg.getString("ID"), mapMsg.getLong("ConID"));
					}
					
				}else if (message.getClass().getCanonicalName().equals(ActiveMQObjectMessage.class.getCanonicalName())) {
					System.out.println("Pasar el Map completo");
					ObjectMessage obMessage = ((ObjectMessage) message);
					if (cont == 0){
						this.trackerControler.setConnectionIDs((HashMap<String, Long>)obMessage.getObject());
						cont++;
					}else if(cont == 1) {
						this.trackerControler.setTransactionIDs((HashMap<String, Integer>)obMessage.getObject());
						cont++;
					}else if(cont == 2) {
						this.trackerControler.setOldConnectionIDs((HashMap<String, Long>)obMessage.getObject());
					}
					
				}
			
			}catch(Exception e){
				System.err.println("Error " + e.getMessage());
			}

		}

	}
}
