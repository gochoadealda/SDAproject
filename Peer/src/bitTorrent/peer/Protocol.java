package bitTorrent.peer;

import java.util.List; 

import bitTorrent.peer.Piece; 
 
public interface Protocol { 
 public void updatePieces(List<Piece> pieces); 
}