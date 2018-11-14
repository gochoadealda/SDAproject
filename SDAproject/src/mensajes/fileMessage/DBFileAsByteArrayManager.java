package mensajes.fileMessage;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Calendar;

import modelo.Tracker;

public class DBFileAsByteArrayManager {

	private static DBFileAsByteArrayManager instance = null;

	
	private DBFileAsByteArrayManager() {
	}
	
	public static DBFileAsByteArrayManager getInstance() {
		if (instance == null) {
			instance = new DBFileAsByteArrayManager();
		}
		
		return instance;
	}
	
    public byte[] readFileAsBytes(String fileName) throws IOException {    
    	try (RandomAccessFile file = new RandomAccessFile(fileName, "r")) {
            byte[] bytes = new byte[(int) file.length()];
            file.readFully(bytes);
            
            return bytes;
        }
    }

    public void writeFile(byte[] bytes, String fileName) throws IOException {
    	try(RandomAccessFile file = new RandomAccessFile(fileName, "rw")) {
    		file.write(bytes);
    	}
    }
    
/*    public static void main(String[] args) {
    	String src = "./db/tracker"+mytracker.getMasterID()+".db";
    	String dst = "./db/tracker"+mytracker.getID()+".db";
    	
    	try {
    		byte[] bytes = DBFileAsByteArrayManager.getInstance().readFileAsBytes(src);
    		DBFileAsByteArrayManager.getInstance().writeFile(bytes, dst);
    	} catch (Exception ex) {
    		ex.printStackTrace();
    	}
    }
*/
}