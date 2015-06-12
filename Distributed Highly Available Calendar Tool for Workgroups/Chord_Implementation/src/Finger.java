import java.io.Serializable;
import java.util.ArrayList;
import java.util.Set;

public class Finger implements Serializable{
	Integer start_KeyID;
	Round_Interval Interval;
	Integer NodeID;			
	Finger(){
		start_KeyID=0;
		Interval = null;
		NodeID = 0;
	}
	public void set_startkey(Integer k){
		start_KeyID = k;
	}
	public void set_NodeID(Integer k){
		NodeID = k;
	}
}