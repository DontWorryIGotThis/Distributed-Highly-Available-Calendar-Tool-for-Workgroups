import java.io.Serializable;
import java.net.DatagramSocket;
import java.net.SocketException;


public class Fault_Detection implements Runnable, Serializable {
	
	DatagramSocket Fault_Detection_Socket;
	
	public Fault_Detection() {
		try {
			Fault_Detection_Socket= new DatagramSocket(Server.Initializations.Fault_Detection_Port);
		} catch (SocketException e) {
			System.err.println(e.getMessage());
		}	
	}
	
	public void run(){
		
		
	}

}
