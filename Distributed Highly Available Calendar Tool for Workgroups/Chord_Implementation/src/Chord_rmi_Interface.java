import java.rmi.Remote;
import java.util.ArrayList;

public interface Chord_rmi_Interface extends Remote {
public Integer Find_Successor(Integer Id)throws java.rmi.RemoteException;
public Integer Get_Successor()throws java.rmi.RemoteException;
public Integer Get_Predecessor()throws java.rmi.RemoteException;
public void Set_Predecessor(Integer k)throws java.rmi.RemoteException;
public void Update_Finger_Table(Integer s, Integer i)throws java.rmi.RemoteException;
public Integer Closest_Preceding_Finger(Integer Id)throws java.rmi.RemoteException;
public Integer Get_ID()throws java.rmi.RemoteException;
}
