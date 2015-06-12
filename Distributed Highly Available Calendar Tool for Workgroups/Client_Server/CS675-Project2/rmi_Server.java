import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class rmi_Server 							// The server runs where the registry is and hence it tries to locate it in the localhost
{										// It instantiates the Calendar manager object and binds it to the registry
	static Registry rmi_Registry;
		public static void main(String[] args)
	{
		try {
			//rmi_Registry = LocateRegistry.getRegistry("Localhost", rmi_constants.rmi_port);
			rmi_Registry = LocateRegistry.createRegistry(rmi_constants.rmi_port);
			rmi_CManager_Implementation CManager = new rmi_CManager_Implementation();
			rmi_Registry.rebind(rmi_constants.rmi_CManager_Ref, CManager);	
			System.out.println("Server is up and running");
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		} 	
	}
}
