import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface rmi_Calendar_Interface extends Remote{                                          // Calendar Interface
	public String Retrieve_Event(String User_Name, String Start_Date, String End_Date) throws RemoteException;
	public String Retrieve_Event(String Start_Date, String End_Date) throws RemoteException;
	public boolean Schedule_Event (String[] Group_Users, String Start_String_Date, String End_String_Date, String Description ) throws RemoteException;
	public boolean Schedule_Event(String Start_String_Date, String End_String_Date, String Description, String Access_Control) throws RemoteException;
	
	public ArrayList<event> Return_Viewable_Events(event Event_Limits,String param_Group, String param_Owner) throws RemoteException;
	public boolean Can_Be_Scheduled(event Event_Limits) throws RemoteException;
	

}
