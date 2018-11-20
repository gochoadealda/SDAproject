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
	private DBQueueFileReceiver reciever;
	
	
	
	public DBQueueFileMessageListener(Tracker model, DBQueueFileReceiver reciever) {
		super();
		this.trackerController = new TrackerController(model);
		this.reciever = reciever;
	}



	@Override
	public void onMessage(Message message) {
		if (message != null) {
			try {
				System.out.println("   - FileQueueListener: " + message.getClass().getSimpleName() + " received!");
								
				if (message.getClass().getCanonicalName().equals(ActiveMQBytesMessage.class.getCanonicalName())) {
					trackerController.setBdtimestamp(System.currentTimeMillis());
					String fileName = DEST_FILE + trackerController.getBdtimestamp() + ".db";
					
					//Read message content as an Array of bytes
					DBFileAsByteArrayManager.getInstance().writeFile(((ActiveMQBytesMessage)message).getContent().getData(), fileName);
					
					trackerController.createConnectionDB();
					//Print received file details
					try {
						reciever.queueReceiver.close();
						reciever.queueSession.close();
						reciever.queueConnection.close();
						System.out.println("- Queue resources closed!");
					}catch (Exception e) {
						
					}
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