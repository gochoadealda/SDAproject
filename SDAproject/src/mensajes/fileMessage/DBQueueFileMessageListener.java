package mensajes.fileMessage;

import java.io.File;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.activemq.command.ActiveMQBytesMessage;
import org.apache.activemq.command.ActiveMQTextMessage;

import controller.TrackerController;
import modelo.Tracker;

public class DBQueueFileMessageListener implements MessageListener  {

	private String DEST_FILE = "./db/tracker";
	private TrackerController trackerController;
	
	
	
	public DBQueueFileMessageListener(Tracker model) {
		super();
		this.trackerController = new TrackerController(model);
	}



	@Override
	public void onMessage(Message message) {
		if (message != null) {
			try {
				System.out.println("   - FileQueueListener: " + message.getClass().getSimpleName() + " received!");
								
				if (message.getClass().getCanonicalName().equals(ActiveMQBytesMessage.class.getCanonicalName())) {
					String fileName = DEST_FILE + trackerController.getID() + ".db";
					
					//Read message content as an Array of bytes
					DBFileAsByteArrayManager.getInstance().writeFile(((ActiveMQBytesMessage)message).getContent().getData(), fileName);
					
					//Print received file details
					File file = new File(fileName);					
					System.out.println("     - Received file:  '" + file.getName() + "' (" + file.length() + " bytes)");
				}else if (message.getClass().getCanonicalName().equals(ActiveMQTextMessage.class.getCanonicalName())) {

					trackerController.setMasterID(Integer.parseInt(((TextMessage)message).getText()));
				}
				
			
			} catch (Exception ex) {
				System.err.println("# sendDBListener error: " + ex.getMessage());
			}
		}
	}
}