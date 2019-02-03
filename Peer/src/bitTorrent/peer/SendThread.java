package bitTorrent.peer;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class SendThread extends Thread { 
	  DataOutputStream out; 
	  boolean done = false; 
	  BlockingQueue<BitTorrentPacket> queuePacket = new ArrayBlockingQueue<BitTorrentPacket>(500); 
	   
	  public SendThread(DataOutputStream out) {  
	   this.out = out; 
	  } 
	   
	  public void run() {  
	   while (!done) {  
	    BitTorrentPacket packet  = queuePacket.poll();  
	    if (queuePacket.size() > 500 * 0.7) {  
	     if (packet.getType() == Constant.HAVE)  
	      continue; 
	    } 
	     
	    if (packet != null) { 
	     try { 
	      packet.write(out); 
	     } catch (IOException e) { 
	      // TODO Auto-generated catch block 
	      System.out.println(packet.getType()); 
	      System.out.println("=================="); 
	      ((TCPBitTorrentPacket) packet).debug(); 
	      System.out.println("=================="); 
	      e.printStackTrace(); 
	     } 
	    } 
	   } 
	  } 
	   
	  public void sendPacket(BitTorrentPacket packet) {  
	   queuePacket.add(packet); 
	  } 
	   
	  public synchronized void setDone() {  
	   done = true; 
	  } 
	 } 
