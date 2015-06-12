import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

public interface rmi_CManager_Interface extends Remote{
	static ArrayList<rmi_Calendar_Implementation> List_Existing_Users = new ArrayList<rmi_Calendar_Implementation>();
	
	public String Create_Calendar( String User_Name, String Group_Name) throws RemoteException, IOException; // Return String confirmation or error message
	public rmi_Calendar_Interface Connect_Calendar(String User_name) throws RemoteException;
}
