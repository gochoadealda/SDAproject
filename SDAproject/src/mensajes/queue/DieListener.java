package mensajes.queue;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.activemq.command.ActiveMQTextMessage;

import modelo.Tracker;

public class DieListener implements MessageListener {
	
private Tracker myTracker;
	
	public DieListener(Tracker myTracker) {
		super();
		this.myTracker = myTracker;
	}
	
	public void onMessage(Message message) {
		if (message != null) {
			
			try {
				System.out.println("   - DieQueueListener: " + message.getClass().getSimpleName() + " received!");
				
				if (message.getClass().getCanonicalName().equals(ActiveMQTextMessage.class.getCanonicalName())) {
					System.out.println(((TextMessage)message).getText());
					String messageString = ((TextMessage)message).getText();
					int arrivedID = Integer.parseInt(messageString.substring(3));
					String texto = messageString.substring(1,2);
					if(texto == "OK"){
						myTracker.setOkList(arrivedID);
						
					}else if (texto == "ER"){
						myTracker.getOkList().clear();
					
					}
					
					
				}
					
	}catch (Exception ex) {
		System.err.println("# TopicListener error: " + ex.getMessage());
	}
}}

}
