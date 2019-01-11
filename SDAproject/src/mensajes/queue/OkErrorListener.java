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
import mensajes.topic.UpdatePublisher;
import modelo.Tracker;

public class OkErrorListener implements MessageListener {

	private TrackerController trackerController;
	private int ok,error;

	private ArrayList<Integer> IDlist;

	public OkErrorListener(Tracker model) {
		super();
		this.trackerController = new TrackerController(model);
		this.ok = 0;
		this.error = 0;
		this.IDlist = trackerController.getTrackerList();
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

					// no se pondra a 0??? En el constructor 
					//ArrayList<Integer> IDlist = trackerController.getTrackerList();

					HashMap<Integer, String> votos = trackerController.getVotos();
					votos.put(arrivedID, texto);
					trackerController.setVotos(votos);
					
					Iterator<?> it = votos.entrySet().iterator();
					while(it.hasNext()) {
						@SuppressWarnings("unchecked")
						Map.Entry<Integer, String> value = (Map.Entry<Integer, String>)it.next();
						if(value.getValue() == "OK") {
							ok++;
						}else{
							error++;
						}
						//Borrar el id de quien ha contestado
						for(int i=0; i<IDlist.size(); i++) {
							if(IDlist.get(i) == value.getKey()) {
								IDlist.remove(i);
							}
						}
					}
					trackerController.setTrackerListDIE(IDlist);
					
					//Comparar si todos han votado
					//TODO y si hay los mismos?????
//					if((ok+error) != trackerController.getTrackerList().size()) {
//						if(trackerController.isMaster()) {
//							for(int i=0; i<IDlist.size(); i++) {
//								int trackerID = IDlist.get(i);
//								//TODO por donde le paso el id de que tracker es???
//								trackerController.getModel().dieSend = new DieSender(trackerController.getModel());
//								trackerController.getModel().dieSend.start();
//							}
//						}
//					}
					trackerController.setOkVotoUpdate(ok);
					trackerController.setErrorVotoUpdate(error);
				}
			}catch (Exception ex) {
				System.err.println("# TopicListener error: " + ex.getMessage());
			}
		}
	}
}



