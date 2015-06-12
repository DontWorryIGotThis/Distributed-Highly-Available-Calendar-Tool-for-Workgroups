import java.net.InetAddress;
import java.net.SocketAddress;


public class Join_Req_Handler_Thread implements Runnable{
	public InetAddress Requester_Ip;
	public int Requester_port; // is this port number necessary? ask abhishek
	public Join_Req_Handler_Thread(InetAddress Dummy_Requester_Ip, int Dummy_Requester_port) {
		Requester_Ip = Dummy_Requester_Ip;
		Requester_port = Dummy_Requester_port;
	}
	public void run(){
		
	}

}
