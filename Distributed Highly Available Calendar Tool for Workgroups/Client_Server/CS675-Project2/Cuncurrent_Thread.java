import java.rmi.RemoteException;

public class Cuncurrent_Thread extends Thread{                        // This thread concurrently executes to display the current events to the user
	rmi_Calendar_Interface Calendar_Interface;
	public Cuncurrent_Thread(rmi_Calendar_Interface Calendar_Interface)
	{
		this.Calendar_Interface= Calendar_Interface;
	}
	
	public void Listen() throws RemoteException
	{
		System.out.println(Calendar_Interface.Retrieve_Event("2012-01-01 0000", "2014-12-31 2400"));
	}

}
