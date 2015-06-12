import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;


public class Values implements Serializable {
	int ServerPort = rmi_constants.rmi_port;
	int Fault_Detection_Port = 8683;
	InetAddress host_InetAddress;
	String host_IP_String;
	String Own_IP_String;
	int m =3;
	
	
	public Values(String Own_ip_addr,String Host_to_connect_to) {
		Own_IP_String = Own_ip_addr;
		host_IP_String = Host_to_connect_to;
		if(host_IP_String != null){			
			try {
				host_InetAddress = InetAddress.getByName(Host_to_connect_to);
			} catch (UnknownHostException e) {
				System.err.println(e.getMessage());
				System.out.println("Invalid IpAddress for host");
			}
		}
	}
}
