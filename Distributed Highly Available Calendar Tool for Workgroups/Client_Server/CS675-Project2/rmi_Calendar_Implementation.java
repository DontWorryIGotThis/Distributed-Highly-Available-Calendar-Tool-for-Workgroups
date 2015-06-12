import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantReadWriteLock;


public class rmi_Calendar_Implementation extends UnicastRemoteObject implements rmi_Calendar_Interface{                    //Implementation of the calendar object.
	
	String Owner, Group;
	ReentrantReadWriteLock l = new ReentrantReadWriteLock();
	ArrayList<event> Events_ArrayList;
	ArrayList<event>  Viewable_Events_ArrayList;
	rmi_CManager_Interface CManagerRef_for_connectcalender;

	protected rmi_Calendar_Implementation() throws RemoteException {                                                   
		super();
		Registry Registry_for_conect_calender = LocateRegistry.getRegistry( rmi_constants.rmi_port);
		try {
			CManagerRef_for_connectcalender = (rmi_CManager_Interface)Registry_for_conect_calender.lookup(rmi_constants.rmi_CManager_Ref);
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
		Events_ArrayList = new ArrayList<event>();
	}

	 public String Retrieve_Event(String User_Name, String Start_Date, String End_Date) throws RemoteException {					//Retrieve Event for Different user
		l.readLock().lock();
		String Return_String;
		event Event_Limits = new event(Start_Date, End_Date, "open", "Event to be checked with");		
		rmi_Calendar_Interface Requested_Calendar_InterfaceObject = (rmi_Calendar_Interface)CManagerRef_for_connectcalender.Connect_Calendar(User_Name.toLowerCase());
		Viewable_Events_ArrayList = Requested_Calendar_InterfaceObject.Return_Viewable_Events(Event_Limits, Group, Owner);
		//check_for_Viewable_Events_Interface(Event_Limits, Requested_Calendar_InterfaceObject);
		Return_String =Print_Event_Arraylist(Viewable_Events_ArrayList); 
		l.readLock().unlock();
		return Return_String;
	}


	 public String Retrieve_Event(String Start_Date, String End_Date) throws RemoteException {										//Retrieve Event for the owned Calendar
		l.readLock().lock();
		String Return_String;
		event Event_Limits = new event(Start_Date, End_Date, "open", "Event to be checked with");
		Viewable_Events_ArrayList = new ArrayList<event>();
		//check_for_Viewable_Events(Event_Limits, this);
		Return_String = Print_Event_Arraylist(Return_Viewable_Events(Event_Limits, Group, Owner));
		l.readLock().unlock();
		return Return_String;
	}
	 
	 
	 public boolean Schedule_Event(String[] Group_Users_StringArray, String Start_String_Date, String End_String_Date, String Description) throws RemoteException {										// Scheduling a Group Event
		 ArrayList<String> Group_Users = new ArrayList<String>();
		 String open = "open";
		 boolean Event_can_b_scheduled = false;
		 boolean Open_event_found = false;
		 
		 event Event_Limits = new event(Start_String_Date, End_String_Date, "group", Description);
		 Open_event_found = Can_Be_Scheduled(Event_Limits);
		 if(Open_event_found){
			 for(int i=0; i<Group_Users_StringArray.length; i++){
				 Group_Users.add(Group_Users_StringArray[i].toLowerCase());
				 rmi_Calendar_Interface Requested_Calendar_Interface = (rmi_Calendar_Interface) CManagerRef_for_connectcalender.Connect_Calendar(Group_Users.get(i));
				 Open_event_found = Requested_Calendar_Interface.Can_Be_Scheduled(Event_Limits);
				 if(Open_event_found){
					 	Event_can_b_scheduled = true;
					 	continue;
				 }
				 else{
					 Event_can_b_scheduled = false;
					 break;				 
				 }			 
			 }
		 }
		 if(Event_can_b_scheduled){
			 Schedule_Event(Start_String_Date, End_String_Date, Description, "group");
			 for(int i=0; i<Group_Users.size(); i++){
				 rmi_Calendar_Interface Requested_Calendar_Interface = (rmi_Calendar_Interface)CManagerRef_for_connectcalender.Connect_Calendar(Group_Users.get(i));
				 Requested_Calendar_Interface.Schedule_Event(Start_String_Date, End_String_Date, Description, "group");				 
			 }  
		 }
		 return Event_can_b_scheduled;
	 }


	 


	 public boolean Schedule_Event(String Start_String_Date, String End_String_Date, String Description, String Access_Control) throws RemoteException { 	// Scheduling a private Event
		l.writeLock().lock();
		event New_Event = new event(Start_String_Date, End_String_Date, Access_Control, Description);
		for(int i=0; i<Events_ArrayList.size(); i++){
			if((Events_ArrayList.get(i).Access_Control.equals("open")) && (Events_ArrayList.get(i).Contains_Event(New_Event))){
				Events_ArrayList.add(New_Event);
				event Remaining_Open_Event_Before = new event(Events_ArrayList.get(i).Start_Stamp.Stamp_In_String, New_Event.Start_Stamp.Stamp_In_String, "open", "Open Event");
				event Remaining_Open_Event_After = new event(New_Event.End_Stamp.Stamp_In_String,Events_ArrayList.get(i).End_Stamp.Stamp_In_String, "open", "Open Event");
				Events_ArrayList.remove(i);								
				Events_ArrayList.add(Remaining_Open_Event_Before);
				Events_ArrayList.add(Remaining_Open_Event_After);
				Sort_Events_ArrayList();
				FileOutputStream fout;
				try {
					fout = new FileOutputStream(Owner);
					ObjectOutputStream Oout = new ObjectOutputStream(fout);
					Oout.writeObject(this);
					fout.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				l.writeLock().unlock();
				return true;
			}
		}
		l.writeLock().unlock();
		return false;
	}
	 

	public ArrayList<event> Return_Viewable_Events(event Event_Limits,String param_Group, String param_Owner) throws RemoteException{
		l.readLock().lock();
		String pub = "public", group = "group", open = "open", pivate="private";
		Viewable_Events_ArrayList = new ArrayList<event>();
		for(int i=0;i<Events_ArrayList.size();i++){
			if(Event_Limits.Contains_Event(Events_ArrayList.get(i))){
				if(pub.equals(Events_ArrayList.get(i).Access_Control)||open.equals(Events_ArrayList.get(i).Access_Control)) {
				 event event_2b_viewed = new event(Events_ArrayList.get(i).Start_Stamp.Stamp_In_String, Events_ArrayList.get(i).End_Stamp.Stamp_In_String, Events_ArrayList.get(i).Access_Control, Events_ArrayList.get(i).Text_Description);
				 Viewable_Events_ArrayList.add(event_2b_viewed);
				}
				if(group.equals(Events_ArrayList.get(i).Access_Control)){
					if(param_Group.equals(Group)){ 
						event event_2b_viewed = new event(Events_ArrayList.get(i).Start_Stamp.Stamp_In_String, Events_ArrayList.get(i).End_Stamp.Stamp_In_String, Events_ArrayList.get(i).Access_Control, Events_ArrayList.get(i).Text_Description);
						Viewable_Events_ArrayList.add(event_2b_viewed);
					}
					else{
						event event_2b_viewed = new event(Events_ArrayList.get(i).Start_Stamp.Stamp_In_String, Events_ArrayList.get(i).End_Stamp.Stamp_In_String, Events_ArrayList.get(i).Access_Control, "You need to be a member of the group "+Group+" to view the details of this event");
						Viewable_Events_ArrayList.add(event_2b_viewed);
					}
				}
				if(pivate.equals(Events_ArrayList.get(i).Access_Control))
					if(param_Owner.equals(Owner)) {
						event  event_2b_viewed = new event(Events_ArrayList.get(i).Start_Stamp.Stamp_In_String, Events_ArrayList.get(i).End_Stamp.Stamp_In_String, Events_ArrayList.get(i).Access_Control, Events_ArrayList.get(i).Text_Description);
						Viewable_Events_ArrayList.add(event_2b_viewed);
					}
			}
		}
		l.readLock().unlock();
		return Viewable_Events_ArrayList;
	}
	
	private String Print_Event_Arraylist(ArrayList<event> Print_Event_Array){
		ArrayList<event> Copy_Print_Event_Array = (ArrayList<event>) Print_Event_Array.clone();
		String Return_String ="";
		for(int i=0; i<Print_Event_Array.size();i++)
		{
			Return_String= Return_String+"\n"+Copy_Print_Event_Array.get(i).Print_Event();
		}
		return Return_String;	
	}
	
	
	
	private void Sort_Events_ArrayList(){																		// Sorting the Events
		event Temp;
		int k;
		for(int i=1; i<Events_ArrayList.size(); i++){
			Temp = Events_ArrayList.get(i);
			for(k=i-1; k>=0 &&(Events_ArrayList.get(k).Start_Stamp.isafter(Events_ArrayList.get(i).Start_Stamp)); k--){
				Events_ArrayList.set(k+1, Events_ArrayList.get(k));
			}									
			Events_ArrayList.set(k+1,Temp);
		}
	}
	
	public boolean Can_Be_Scheduled (event Event_Limits) throws RemoteException{
		String open = "open";
		ArrayList<event> List_of_events_2b_checked;
		event Max_Event_Limits = new event("2013-01-01 0000", "2013-12-31 2400", "group", "retrieve all");
		List_of_events_2b_checked = Return_Viewable_Events(Max_Event_Limits, Group, Owner);
		 for(int j=0; j< List_of_events_2b_checked.size(); j++){
			 if(List_of_events_2b_checked.get(j).Contains_Event(Event_Limits)&& (open.equals(List_of_events_2b_checked.get(j).Access_Control)))
				 return true;
		 }
		 return false;
	}
}
