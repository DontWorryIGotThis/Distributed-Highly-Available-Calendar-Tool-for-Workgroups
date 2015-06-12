import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Registry_Creator {								// This class creates the Registry that needs to be started manually
	public static void main(String[] args) throws RemoteException{
		Registry rmi_Registry = LocateRegistry.createRegistry(rmi_constants.rmi_port);		
	}

}
