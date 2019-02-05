package bitTorrent.peer;

import java.io.DataInputStream;
import java.io.File;
import java.util.Arrays;

import bitTorrent.peer.TCPClient;
import modelo.Peer;
import util.Util;

public class ReceiveThread extends Thread {
	  private Peer peer; 
	  DataInputStream input;
	  private int piecesSize; 
	  private TCPClient[] peers;
	  private boolean[] bitmask;
	  private boolean[][] goingDownload;
	  private long trunkLength, fileSize;
	  private SendThread send;
	  boolean done = false; 
	  public ReceiveThread (DataInputStream input) {  
	   this.input = input; 
	  } 
	   
	  public void run() {  
	   while (!done) {  
	    TCPBitTorrentPacket packet = new TCPBitTorrentPacket(input).getInstance(); 
	    if (packet == null) { 
	    } else { 
	     switch (packet.getType()) {  
	     case Constant.BITFIELD:  
	      handleBitField(packet); 
	      break; 
	     case Constant.PORT:  
	      handlePort(packet); 
	      break; 
	     case Constant.UNCHOKE:  
	      handleUnChoke(packet); 
	      break; 
	     case Constant.PIECE:  
	      handlePiece(packet); 
	      break; 
	     default: 
	      // just do nothing 
	     } 
	    } 
	   } 
	  } 
	   
	  public void handleBitField(TCPBitTorrentPacket packet) {  
	   if (Constant.DEBUG) {  
	    Util.debug("recieving BitField"); 
	   } 
	   BitField p = (BitField) packet; 
	   bitmask = p.getValid(); 
	    
	   // find other peers who can download invlalid pieces 
	   for (int i = 0; i < piecesSize; i++) {  
	    if (!bitmask[i]) {  
	     boolean found = false; 
	     for (int j = 0; j < peers.length; j++) { 
	      if (peers[j].download(i)) { 
	       found = true; 
	       break; 
	      } 
	     } 
	     if (found) { 
	      Util.debug("Can't download piece " + i + "."); 
	     } 
	    } 
	   } 
	    
	   if (Constant.DEBUG) {  
	    p.debug(); 
	    Util.pause(); 
	   } 
	    
	  } 
	   
	  public void handlePort(TCPBitTorrentPacket packet) {  
	   if (Constant.DEBUG) {  
	    Util.debug("recieving Port"); 
	   } 
	   Port p = (Port) packet; 
	   peer.setPuerto(p.getPort()); 
	    
	   if (Constant.DEBUG) {  
	    p.debug(); 
	   } 
	  } 
	   
	  public void handleUnChoke(TCPBitTorrentPacket packet) {  
	   if (Constant.DEBUG) {  
	    Util.debug("recieving UnChoke"); 
	   } 
	   UnChoke p = (UnChoke) packet; 
	    
	   // make the request and send to peer 
	   Request request = makeRequest(); 
	   send.sendPacket(request); 
	    
	   // print out sending request 
	   if (Constant.DEBUG) {  
	    p.debug(); 
	    Util.pause(); 
	     
	    request.debug(); 
	    Util.pause(); 
	   } 
	  } 
	   
	  public void handlePiece(TCPBitTorrentPacket packet) {  
	   if (Constant.DEBUG) {  
	    Util.debug("recieving Piece"); 
	   } 
	   Piece p = (Piece) packet; 
	   writeToDisk(p); 
	     
	   if (Constant.DEBUG) {  
	    p.debug(); 
	    Util.pause(); 
	   } 
	    
	   Request request = makeRequest(); 
	   if (Constant.DEBUG) {  
	    request.debug(); 
	    Util.pause(); 
	   } 
	   send.sendPacket(request); 
	  } 
	  
	  public Request makeRequest() {  
		  int index = -1; 
		  int begin = 0; 
		  int length = (int) Constant.TRUNK_LENGTH; 
		  for (int i = 0; i < piecesSize; i++) {  
		   if (bitmask == null || (bitmask != null && bitmask[i])) {  
		    for (int j = 0; j < trunkLength; j++) {  
		     if (goingDownload[i][j]) {  
		      goingDownload[i][j] = false; 
		      index = i; 
		      begin = (int) (j * Constant.TRUNK_LENGTH); 
		      break; 
		     } 
		    } 
		     
		    if (index != -1) {  
		     break; 
		    } 
		   } 
		  } 
		  if (index == piecesSize - 1) {  
		   length = (int) Math.abs(fileSize - (Constant.TRUNK_LENGTH * (piecesSize-1) * trunkLength)); 
		   begin = 0; 
		   Arrays.fill(goingDownload[index], false); 
		  } 
		   
		  if (index == -1) {  
		   send.setDone(); 
		   setDone(); 
		  } 
		  return new Request(index, begin, length); 
		 }
	  
	  public synchronized void setDone() {  
	   done = true; 
	  } 
	  
	  public void writeToDisk(Piece piece) {  
		  int part = piece.getBegin() / piece.getPieceLen(); 
		  String nameFile = piece.getIndex() + "_" + part; 
		  File file = new File(Constant.DIR,nameFile); 
		   
		  Util.writeData(file, piece.getPiece()); 
		 } 
	 } 
