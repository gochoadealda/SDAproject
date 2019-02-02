package modelo;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Servidor {
	
	public static void main (String[] args){
		
		ServerSocket servidor = null;
		Socket sc = null;
		DataInputStream in;
		DataOutputStream out;
		
		final int PUERTO = 5000;
		
		try{
			servidor = new ServerSocket(PUERTO);
			System.out.println("Server iniciado");
			
			while(true){
				sc = servidor.accept();
				//hasta que no entra alguien no se ejecutanada a partir de aquí
				in = new DataInputStream(sc.getInputStream());
				out = new DataOutputStream(sc.getOutputStream());
				
				String mensaje = in.readUTF();
				System.out.println(mensaje);
				
				out.writeUTF("Hola mundo desde server");
				sc.close();
				System.out.println("Cliente desconectado");
			}
		}catch (IOException ex) {
			Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null , ex);
		}
	}

}
