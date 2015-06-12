import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.*;
import java.rmi.server.UnicastRemoteObject;

public class Chord extends UnicastRemoteObject implements Serializable, Chord_rmi_Interface {
	
	Integer Predecessor, Successor;
	ArrayList<Finger> Finger_Table;
	public static HashMap<String, Integer> IpToId_Map;
	public static HashMap<Integer, String> IdToIp_Map;
	public static HashMap<String, Integer> CalendarToID_Map;
	Chord_rmi_Interface Host_Chord;
	//ArrayList<Finger> Host_Finger_Table;
	
	protected Chord(String Host_Ip_String) throws RemoteException { // Initialization when there are more than 1 server
		super();
		Registry rmi_Registry = LocateRegistry.getRegistry("Localhost", rmi_constants.rmi_port);
		try {
			rmi_Registry.bind("Chord_Object", this);
		} catch (AlreadyBoundException e) {
			System.err.println("Error at binding the object to the registry in the constructor of chord object");
			System.err.println(e.getMessage());
		}
		Finger Dummy_Finger = new Finger();
		Finger_Table = new ArrayList<Finger>();
		Finger_Table.add(Dummy_Finger);
		
		IpToId_Map = new HashMap<String, Integer>();
		IdToIp_Map = new HashMap<Integer, String>();
		CalendarToID_Map = new HashMap<String, Integer>();
		for(int i=0; i<7; i++){
			IpToId_Map.put("10.1.255."+(242+i), i);
			IdToIp_Map.put(i, "10.1.255."+(242+i));
		}
		Server.Server_Process_Id= IpToId_Map.get(Server.Initializations.Own_IP_String);
		System.out.println("Server process id "+ Server.Server_Process_Id);
		System.out.println("Generated Startkeys are as follows");
		Integer a,b;
		for(int i=1; i<=Server.Initializations.m; i++){
			a = (int) (Server.Server_Process_Id+Math.pow(2, i-1));
			b = (int) (a % (Math.pow(2, 3)));
			Dummy_Finger = new Finger();
			Dummy_Finger.set_startkey(b);
			Finger_Table.add(Dummy_Finger);
		}
		if(Host_Ip_String ==null)
		join(null);
		else{
			Integer Host_ID = IpToId_Map.get(Host_Ip_String);
			join(Host_ID);
		}
		Print_Finger_Table();
	}	

//################################################################################-- Paper functions
	public void join(Integer Host_Id) throws RemoteException{
		if(Host_Id!=null){
			Init_Finger_Table(Host_Id);
			Update_Other_Nodes();
		}
		else{
			for(int i=1; i<=Server.Initializations.m; i++){
				Finger_Table.get(i).set_NodeID(Server.Server_Process_Id);
			}
			Predecessor = Server.Server_Process_Id;
			System.out.println("Predecessor: "+Predecessor);
			Successor = Finger_Table.get(1).NodeID;
			System.out.println("Successor: "+Successor);
		}		
	}

	public void Init_Finger_Table(Integer np) throws RemoteException{
		
		Chord_rmi_Interface np_Chord_Object = Get_Chord_Object_Using_NodeID(np);
		System.out.println("Init_Finger_Table : Requesting Object to find successor of "+this.Finger_Table.get(1).start_KeyID);
		Finger_Table.get(1).NodeID =  np_Chord_Object.Find_Successor(this.Finger_Table.get(1).start_KeyID);   // Inserting it self in to the network
		Successor = Finger_Table.get(1).NodeID;
		System.out.println("Finger table(1) : "+Successor);
		System.out.println("Find_Predecessor : Requesting Object to find predecessor");
		Chord_rmi_Interface Successor_Chord_Object = Get_Chord_Object_Using_NodeID(Successor);			// Finding it's predecessor
		Predecessor = Successor_Chord_Object.Get_Predecessor();
		System.out.println("Predecessor: "+Predecessor);
		
		Successor_Chord_Object.Set_Predecessor(Server.Server_Process_Id); 						// Setting itself as the predecessor 
		for(int i=1; i<=(Server.Initializations.m-1);i++){
			if(Lies_in_Close_Open(Server.Server_Process_Id, Finger_Table.get(i).NodeID, Finger_Table.get(i).start_KeyID)){
				Finger_Table.get(i+1).set_NodeID(Finger_Table.get(i).NodeID);
				System.out.println(Finger_Table.get(i+1).start_KeyID+" (Key) = "+ Finger_Table.get(i).NodeID+" (node)");
			}
			else{
				Finger_Table.get(i+1).NodeID = np_Chord_Object.Find_Successor(Finger_Table.get(i+1).start_KeyID);
				System.out.println(Finger_Table.get(i+1).start_KeyID+" (Key) = "+ Finger_Table.get(i+1).NodeID+" (node)");
			}
		}
	}
	
	public boolean Lies_in_Close_Open(Integer a, Integer b, Integer ID){
		System.out.println(ID+" lies between [ "+a+","+b+" )");
		Integer id =ID;
		if(a<b)
			if((a<=id)&&(id<b)){
				return true;
			}
		if(a>=b)
			if((a<=id)||(id<b)){
				return true;
			}
		return false;
	}
	//----------------------------------------------------------------------------------------------------------------------------------
	
	public void Update_Other_Nodes() throws RemoteException{
		for(int i =1; i<=Server.Initializations.m; i++){
			Integer p = Find_Predecessor(Server.Server_Process_Id-(2^(i-1)));
			System.out.println("Update_Other_Nodes : Requesting Object");
			Chord_rmi_Interface p_Chord_Object = Get_Chord_Object_Using_NodeID(p);
			System.out.println("Update_Other_Nodes : Retrieved Object");
			p_Chord_Object.Update_Finger_Table(Server.Server_Process_Id, i);
		}
	}
	
	public void Update_Finger_Table(Integer s, Integer i) throws RemoteException
	{
		if(Lies_in_Close_Open(Server.Server_Process_Id, Finger_Table.get(i).NodeID, s)){
			Finger_Table.get(i).set_NodeID(s);
			System.out.println("Finger Update");
			System.out.println(Server.Server_Process_Id+" .Finger( "+i+" ) = "+s);
			if(i ==1)
				Successor=s;
			int p= Predecessor;
			if(p !=s){				
				System.out.println("Update_Finger_Table : Requesting Object");
				Chord_rmi_Interface p_Chord_Object = Get_Chord_Object_Using_NodeID(p);
				
				p_Chord_Object.Update_Finger_Table(s,i);
			}
		}
		Print_Finger_Table();
	}
	
	public Integer Find_Successor(Integer Id) throws RemoteException{
		Integer np = Find_Predecessor(Id);
		System.out.println("Find_Successor : Requesting Object");
		Chord_rmi_Interface np_Chord_Object = Get_Chord_Object_Using_NodeID(np);
		System.out.println("Returning "+np_Chord_Object.Get_Successor()+" as successor for "+Id+" on server id "+Server.Server_Process_Id);
		return np_Chord_Object.Get_Successor();
	}
	//----------------------------------------------------------------------------------------------------------------------------------
	public Integer Find_Predecessor(int Id) throws RemoteException{
		Integer np = Server.Server_Process_Id;
		System.out.println("Find_Predecessor : Requesting Object");
		Chord_rmi_Interface np_Chord_Object = Get_Chord_Object_Using_NodeID(np); 		
		while((Doesnt_Lie_Between_Open_Close(np_Chord_Object.Get_ID(), np_Chord_Object.Get_Successor(), Id))){
			System.out.println("Find_Predecessor : Requesting Object in the while loop");
			np = np_Chord_Object.Closest_Preceding_Finger(Id);
			np_Chord_Object = Get_Chord_Object_Using_NodeID(np);
		}
		System.out.println("Returning "+np+" as predecessor of "+Id+" on Server ID "+Server.Server_Process_Id);
		return np;
	}
	
	private boolean Doesnt_Lie_Between_Open_Close(int a, int b, int Id_to_Check){
		System.out.println(Id_to_Check+" Doesn't lie between ( "+a+","+b+" ]");
		if(a == b){
			System.out.println("While loop a==b");
			return false;   
		}
		if(a<b){
			if(((a>Id_to_Check)&&(Id_to_Check>=b))||((a<Id_to_Check)&&(Id_to_Check<=b))){
				System.out.println("While loop: a<b");
				return true;
				}
		}
		if(a>b){
			if((a>Id_to_Check)&&(Id_to_Check<=b)){
				System.out.println("While loop: a>b");
				return true;
			}
		}
			
		System.out.println("While loop all conditions failed");
		return false;
	}
	//----------------------------------------------------------------------------------------------------------------------------------
	public Integer Closest_Preceding_Finger(Integer Id){
		for(int i=Server.Initializations.m; i>0;i--)
			if(Lies_Between_Open_Open(Server.Server_Process_Id, Id, Finger_Table.get(i).NodeID))
				return Finger_Table.get(i).NodeID;
		return Server.Server_Process_Id;
	}
	
	private boolean Lies_Between_Open_Open(Integer a, Integer b, Integer Thing_to_Check){
		System.out.println(Thing_to_Check+" Lies between ( "+a+","+b+" )");
		if(a<b)
			if((a<Thing_to_Check)&&(Thing_to_Check<b))
				return true;
		if(a>b)
			if((a<Thing_to_Check)||(Thing_to_Check<b))
				return true;
		if(a==b)
			return false;
		return false;
	}
	//----------------------------------------------------------------------------------------------------------------------------------
	public Integer Get_Successor(){
		return Successor;
	}
	
	public Integer Get_ID(){
		return Server.Server_Process_Id;
	}
	
	public Integer Get_Predecessor(){
		return Predecessor;
	}
	
	public void Set_Predecessor(Integer k){
		Predecessor =k;	
	}
	
	private Chord_rmi_Interface Get_Chord_Object_Using_NodeID (Integer Id){
		String ip_String =IdToIp_Map.get(Id);
		Registry reg;
		Chord_rmi_Interface Chord_Object;
		try {
			System.out.println("Requesting for Process "+Id+" chord object");
			reg = LocateRegistry.getRegistry(ip_String, Server.Initializations.ServerPort);
			Chord_Object = (Chord_rmi_Interface) reg.lookup("Chord_Object");
			
			return Chord_Object;
		} catch (RemoteException e) {
			System.err.println("My message:Could not retrieve the remote object from the registry");
			System.err.println(e.getMessage());
			return null;
		} catch (NotBoundException e) {
			System.err.println("My message: Says that the Requested Remote Object is not bound to the registry");
			System.err.println(e.getMessage());
			return null;
			
		}
	}
	
	private void Print_Finger_Table(){
		for(int i=1; i<=Server.Initializations.m; i++)
			System.out.println(Finger_Table.get(i).start_KeyID+" (Key) = "+Finger_Table.get(i).NodeID+" (Node)");
	}
}
