package mensajes;

import javax.jms.Message;
import javax.jms.MessageListener;

import org.apache.activemq.command.ActiveMQTextMessage;

public class UpdateListener implements MessageListener{

	@Override
	public void onMessage(Message message) {
		if (message != null) {

			try {
				System.out.println("   - TopicListener: " + message.getClass().getSimpleName() + " received!");

				//Depending on the type of the message the process is different
				if (message.getClass().getCanonicalName().equals(ActiveMQTextMessage.class.getCanonicalName())) {

				}

			}catch (Exception ex) {
				System.err.println("# TopicListener error: " + ex.getMessage());
			}
		}
		
	}



}
