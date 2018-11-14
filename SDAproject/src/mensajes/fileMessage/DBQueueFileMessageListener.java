package mensajes.fileMessage;

import java.io.File;
import java.util.Calendar;

import javax.jms.Message;
import javax.jms.MessageListener;

import org.apache.activemq.command.ActiveMQBytesMessage;

import modelo.Tracker;

public class DBQueueFileMessageListener implements MessageListener  {

	private String DEST_FILE = "./db/tracker";
	private Tracker mytracker;
	
	
	
	public DBQueueFileMessageListener(Tracker mytracker) {
		super();
		this.mytracker = mytracker;
	}



	@Override
	public void onMessage(Message message) {
		if (message != null) {
			try {
				System.out.println("   - FileQueueListener: " + message.getClass().getSimpleName() + " received!");
								
				if (message instanceof ActiveMQBytesMessage) {
					String fileName = DEST_FILE + mytracker.getID() + ".db";
					
					//Read message content as an Array of bytes
					DBFileAsByteArrayManager.getInstance().writeFile(((ActiveMQBytesMessage)message).getContent().getData(), fileName);
					
					//Print received file details
					File file = new File(fileName);					
					System.out.println("     - Received file:  '" + file.getName() + "' (" + file.length() + " bytes)");
				}
			
			} catch (Exception ex) {
				System.err.println("# sendDBListener error: " + ex.getMessage());
			}
		}
	}
}
