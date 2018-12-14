package udp;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Random;

import bitTorrent.util.ByteUtils;
import bitTorrent.util.StringUtils;

/**
 *
 * Offset  Size    			Name    			Value
 * 0       64-bit integer  	connection_id
 * 8       32-bit integer  	action          	1 // announce
 * 12      32-bit integer  	transaction_id
 * 16      20-byte string  	info_hash
 * 36      20-byte string  	peer_id
 * 56      64-bit integer  	downloaded
 * 64      64-bit integer  	left
 * 72      64-bit integer  	uploaded
 * 80      32-bit integer  	event           	0 // 0: none; 1: completed; 2: started; 3: stopped
 * 84      32-bit integer  	IP address      	0 // default
 * 88      32-bit integer  	key
 * 92      32-bit integer  	num_want        	-1 // default
 * 96      16-bit integer  	port
 * 98
 * 
 */

public class AnnounceRequest extends BitTorrentUDPRequestMessage {

	public enum Event {		
		NONE(0),
		COMPLETED(1),
		STARTED(2),
		STOPPED(3);
		
		private int value;
		
		private Event(int value) {
			this.value = value;
		}
		
		public int value() {
			return this.value;
		}
		
		public static Event parseInt(int value) {
			for (Event event : Event.values()) {
				if (event.value == value) {
					return event;
				}
			}
			
			return null;
		}
	}
	
	private byte[] infoHash;
	private String peerId;
	private long downloaded;
	private long left;
	private long uploaded;
	private Event event;
	private int key;
	private int swarmId;
	

	private int numWant = -1;
	
	private PeerInfo peerInfo;
	
	public AnnounceRequest() {
		super(Action.ANNOUNCE);
		
		this.peerInfo = new PeerInfo();
		this.peerInfo.setIpAddress(0);
	}
	
	@Override
	public byte[] getBytes() {
		ByteBuffer buffer = ByteBuffer.allocate(98);
		buffer.order(ByteOrder.BIG_ENDIAN);
		
		buffer.putLong(0, super.getConnectionId());
		buffer.putInt(8, super.getAction().value());
		buffer.putInt(12, super.getTransactionId());
		buffer.position(16);
		buffer.put(this.infoHash);
		buffer.position(36);
		buffer.put(this.peerId.getBytes());
		buffer.putLong(56, this.downloaded);
		buffer.putLong(64, this.left);
		buffer.putLong(72, this.uploaded);
		buffer.putInt(80, event.value);
		buffer.putInt(84, this.peerInfo.getIpAddress());
		buffer.putInt(88, this.key);
		buffer.putInt(92, this.numWant);
		buffer.position(96);
		buffer.put(ByteUtils.int16ToByteArray(this.peerInfo.getPort()));
		
		buffer.flip();
			
		return buffer.array();
	}
	
	public static AnnounceRequest parse(byte[] byteArray) {
		ByteBuffer buffer = ByteBuffer.wrap(byteArray);
	    buffer.order(ByteOrder.BIG_ENDIAN);
	    
	    AnnounceRequest msg = new AnnounceRequest();

	    msg.setConnectionId(buffer.getLong(0));
	    msg.setAction(Action.valueOf(buffer.getInt(8)));
	    msg.setTransactionId(buffer.getInt(12));
	    msg.setInfoHash(ByteUtils.subArray(buffer.array(), 16, 20));
	    msg.setPeerId(StringUtils.toHexString(ByteUtils.subArray(buffer.array(), 36, 20)));
	    msg.setDownloaded(buffer.getLong(56));
	    msg.setLeft(buffer.getLong(64));
	    msg.setUploaded(buffer.getLong(72));
	    msg.setEvent(Event.parseInt(buffer.getInt(80)));
	    msg.getPeerInfo().setIpAddress(buffer.getInt(84));
	    msg.setKey(buffer.getInt(88));
	    msg.setNumWant(buffer.getInt(92));
	    msg.getPeerInfo().setPort(ByteUtils.byteArrayToInt(ByteUtils.subArray(buffer.array(), 96, 2)));
	    
		return msg;
	}

	public byte[] getInfoHash() {
		return infoHash;
	}
	
	public String getHexInfoHash() {
		return ByteUtils.toHexString(this.infoHash);
	}
	
	public void setInfoHash(byte[] infoHash) {
		this.infoHash = infoHash;
	}

	public String getPeerId() {
		return peerId;
	}

	public void setPeerId(String peerId) {
		this.peerId = peerId;
	}

	public long getDownloaded() {
		return downloaded;
	}

	public void setDownloaded(long downloaded) {
		this.downloaded = downloaded;
	}

	public long getLeft() {
		return left;
	}

	public void setLeft(long left) {
		this.left = left;
	}

	public long getUploaded() {
		return uploaded;
	}

	public void setUploaded(long uploaded) {
		this.uploaded = uploaded;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}

	public int getNumWant() {
		return numWant;
	}

	public void setNumWant(int numWant) {
		this.numWant = numWant;
	}

	public PeerInfo getPeerInfo() {
		return peerInfo;
	}

	public void setPeerInfo(PeerInfo peerInfo) {
		this.peerInfo = peerInfo;
	}
	
	public int getSwarmId() {
		return swarmId;
	}

	public void setSwarmId(int swarmId) {
		this.swarmId = swarmId;
	}
	
	public static void main(String args[]) {
		Random random = new Random();
		String infoHash = "";
		String peerId = ByteUtils.generatePeerId();
		String ip = "192.168.1.1";		
		int port = 65535;
		
	    AnnounceRequest msg = new AnnounceRequest();

	    msg.setConnectionId(random.nextLong());
	    msg.setAction(Action.ANNOUNCE);
	    msg.setTransactionId(random.nextInt());
	    msg.setInfoHash(infoHash.getBytes());
	    msg.setPeerId(peerId);
	    msg.setDownloaded(0l);
	    msg.setLeft(1000000l);
	    msg.setUploaded(0l);
	    msg.setEvent(Event.STARTED);
	    msg.getPeerInfo().setIpAddress(ByteUtils.ipAddressToInt(ip));
	    msg.getPeerInfo().setPort(port);	    
	    msg.setKey(random.nextInt());
	    msg.setNumWant(10);
	    
		
		System.out.println("- Org msg: " +  msg.getPeerInfo());
		
		byte[] msgBytes = msg.getBytes();
		
		System.out.println("- Org msg: " + StringUtils.toHexString(msgBytes));
		
		AnnounceRequest msg2 =  AnnounceRequest.parse(msgBytes);
		
		System.out.println("- New msg: " + StringUtils.toHexString(msgBytes));
		
		System.out.println("- New msg: " + msg2.getPeerInfo());
	}
}