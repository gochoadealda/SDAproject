package mensajes.queue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.activemq.command.ActiveMQTextMessage;

import controller.TrackerController;
import modelo.Tracker;

public class OkErrorListener implements MessageListener {

	private TrackerController trackerController;
	private int ok,error;

	public OkErrorListener(Tracker model) {
		super();
		this.trackerController = new TrackerController(model);
		this.ok = 0;
		this.error = 0;
	}

	public void onMessage(Message message) {
		if (message != null) {

			try {
				System.out.println("   - OkErrorQueueListener: " + message.getClass().getSimpleName() + " received!");

				if (message.getClass().getCanonicalName().equals(ActiveMQTextMessage.class.getCanonicalName())) {
					System.out.println(((TextMessage)message).getText());
					String messageString = ((TextMessage)message).getText();
					int arrivedID = Integer.parseInt(messageString.substring(3));
					String texto = messageString.substring(0,1);
					//TODO no se pondra a 0???
					ArrayList<Integer> IDlist = trackerController.getTrackerList();

					HashMap<Integer, String> votos = new HashMap<Integer, String>();
					votos.put(arrivedID, texto);
					Iterator<?> it = votos.entrySet().iterator();
					while(it.hasNext()) {
						@SuppressWarnings("unchecked")
						Map.Entry<Integer, String> value = (Map.Entry<Integer, String>)it.next();
						if(value.getValue() == "OK") {
							ok++;
						}else{
							error++;
						}
						for(int i=0; i<IDlist.size(); i++) {
							if(IDlist.get(i) == value.getKey()) {
								IDlist.remove(i);
							}
						}
					}
					//Comparar si todos han votado
					if((ok+error) != trackerController.getTrackerList().size()) {
						if(trackerController.isMaster()) {
							for(int i=0; i<IDlist.size(); i++) {
								int trackerID = IDlist.get(i);
								//TODO por donde le paso el id de que tracker es???
								trackerController.getModel().dieSend.start();
							}
						}
						//TODO Aqui va el Receiver???
					}else {//sería que han votado todos
						//dieReceiver.start() donde????
						//TODO Update o No update???
					}

				}
			}catch (Exception ex) {
				System.err.println("# TopicListener error: " + ex.getMessage());
			}
		}
	}
}



