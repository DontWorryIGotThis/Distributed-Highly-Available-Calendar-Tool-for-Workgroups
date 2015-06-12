import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


public class Server {
	
	DatagramSocket ServerSocket;
	static Values Initializations;
	static Chord Chord_Object;
	static Integer Server_Process_Id;
	static Registry rmi_Registry;
	
	
	Server(){
		
	}

	public static void main(String[] args) {
		if(args.length==2){
			Initializations = new Values(args[0], args[1]);//own_ip,host_ip. Host it the system that introduces this server to the chord network 
		}
		else if(args.length==1){
			Initializations = new Values(args[0], null);
		}
		else {
			System.err.println("You have to enter this server's Ip address and/or also the IP address of the Server which will act as a host to the Chord Network");
			System.exit(0);
		}
		
		try {
			Chord_Object= new Chord(Initializations.host_IP_String);
		} catch (Exception e2) {
			System.err.println(e2.getMessage());
		}
			
		
		
		try {
			rmi_Registry = LocateRegistry.getRegistry("Localhost", rmi_constants.rmi_port);
			//rmi_Registry = LocateRegistry.createRegistry(rmi_constants.rmi_port);
			//rmi_CManager_Implementation CManager = new rmi_CManager_Implementation();
			//rmi_Registry.rebind(rmi_constants.rmi_CManager_Ref, CManager);	
			System.out.println("Server is up and running");
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		/*
		while (true){
			byte[] PacketBuffer = new byte[255];
			DatagramPacket Packet = new DatagramPacket(PacketBuffer, PacketBuffer.length);
			try {
				Server.ServerSocket.receive(Packet);
			} catch (IOException e) {
				System.err.println(e.getMessage());
			}
			
			int i;
			for(i =0; i<PacketBuffer.length&&PacketBuffer[i]!=0; i++){}
			String MessageReceived = new String(PacketBuffer, 0, i);
			
			String join = "join";
			if(join.equals(MessageReceived.toLowerCase())){
				
			}
				
			
		}*/ 
	}

}
