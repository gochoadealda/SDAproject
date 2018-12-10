package udp;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

import udp.BitTorrentUDPMessage.Action;

/**
 * 
 * Offset  Size            	Name            	Value
 * 0       32-bit integer  	action          	3 // error
 * 4       32-bit integer  	transaction_id
 * 8       string  message
 * 
 */

public class Error extends BitTorrentUDPMessage {

	private String message;

	public Error() {
		super(Action.ERROR);
	}
	
	@Override
	public byte[] getBytes() {
		ByteBuffer buffer = ByteBuffer.allocate(16);
		buffer.order(ByteOrder.BIG_ENDIAN);
	
		buffer.putInt(0, super.getAction().value());
		buffer.putInt(4, super.getTransactionId());
		buffer.position(8);
		buffer.put(this.message.getBytes());
		
		buffer.flip();
			
		return buffer.array();
	}
	
	public static Error parse(byte[] byteArray) {
		 try {
		    	ByteBuffer buffer = ByteBuffer.wrap(byteArray);
			    buffer.order(ByteOrder.BIG_ENDIAN);
			    
			    Error msg = new Error();
			   
			    msg.setAction(Action.valueOf(buffer.getInt(0)));	    
			    msg.setTransactionId(buffer.getInt(4));
			    msg.setMessage(Byte.toString(buffer.get(8)));
			    
			   
				return msg;
			} catch (Exception ex) {
				System.out.println("# Error parsing Error message: " + ex.getMessage());
			}
		
		return null;
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
