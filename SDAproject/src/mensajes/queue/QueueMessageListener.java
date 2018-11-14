package mensajes.queue;

import java.util.Enumeration;

import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class QueueMessageListener implements MessageListener  {

	@Override
	//This method is call when a new Message arrives to the queue
	public void onMessage(Message message) {
		if (message != null) {
			try {
				System.out.println("   - TopicListener: " + message.getClass().getSimpleName() + " received!");
				
				//Depending on the type of the message the process is different
				if (message instanceof TextMessage) {
					System.out.println("     - TopicListener: TextMessage '" + ((TextMessage)message).getText());
				} else if (message instanceof MapMessage) {
					System.out.println("     - TopicListener: MapMessage");				
					MapMessage mapMsg = ((MapMessage) message);
					
					@SuppressWarnings("unchecked")
					Enumeration<String> mapKeys = (Enumeration<String>)mapMsg.getMapNames();
					String key = null;
					
					while (mapKeys.hasMoreElements()) {
						key = mapKeys.nextElement();
						System.out.println("       + " + key + ": " + mapMsg.getObject(key));
					}								
				}
			
			} catch (Exception ex) {
				System.err.println("# TopicListener error: " + ex.getMessage());
			}
		}
	}
}
