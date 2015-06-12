import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class rmi_CManager_Implementation extends UnicastRemoteObject implements rmi_CManager_Interface{


	protected rmi_CManager_Implementation() throws RemoteException {
		super();
	}


	
	synchronized public String Create_Calendar(String User_Name, String Group_Name) throws IOException 												//Creates calendar Object verifies if a person has a calendar object for himself on the physical DIsk
	{
		try {
			FileInputStream fin = new FileInputStream(User_Name.toLowerCase());
			ObjectInputStream Oin = new ObjectInputStream(fin);
			rmi_Calendar_Implementation Old_Calendar = (rmi_Calendar_Implementation) Oin.readObject();
			rmi_Server.rmi_Registry.rebind(Old_Calendar.Owner, Old_Calendar);
			for(int i=0; i<List_Existing_Users.size();i++){
				if(List_Existing_Users.get(i).Owner.equals(User_Name.toLowerCase())){
					List_Existing_Users.remove(i);
					List_Existing_Users.add(Old_Calendar);
					return "recovered calendar from cold backup and rebound the registry and List of existing users";
				}
			}
			List_Existing_Users.add(Old_Calendar);
				
			
		} catch (FileNotFoundException e1) {
			
			FileOutputStream fout = new FileOutputStream(User_Name.toLowerCase());
			ObjectOutputStream Oout = new ObjectOutputStream(fout);
			rmi_Calendar_Implementation Calendar = new rmi_Calendar_Implementation();
			Calendar.Owner= User_Name.toLowerCase();
			Calendar.Group= Group_Name.toLowerCase();
			Oout.writeObject(Calendar);
			try {
				rmi_Server.rmi_Registry.bind(Calendar.Owner, Calendar);
			} catch (AlreadyBoundException e) {
				return "The specified name is already allocated with a calendar.  A new calendar could not be bound";
			}
			List_Existing_Users.add(Calendar);
			event Massive_Open_Event_2013 = new event("2013-01-01 0001", "2013-12-31 2400", "open", "Massive Open Event");							//Massive Open Event assigned to the Calendar
			Calendar.Events_ArrayList.add(Massive_Open_Event_2013);
			return "A new Calendar has been alloted to "+Calendar.Owner+" Under the group "+Calendar.Group;
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return "Recovered Calendar from cold backup, Calendar wasn't present in the list of existing users so updated it";
		
	}
	 
	 public rmi_Calendar_Interface Connect_Calendar(String User_name) throws RemoteException {	//Connect_Calendar Implementation
		 	Registry Reg = LocateRegistry.getRegistry("localhost",rmi_constants.rmi_port);
		 	try {
				return (rmi_Calendar_Interface) Reg.lookup(User_name.toLowerCase());
			} catch (NotBoundException e) {
				e.printStackTrace();
				return null;
				
			}
		}
}
