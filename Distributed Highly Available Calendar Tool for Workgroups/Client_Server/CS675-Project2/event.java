import java.io.Serializable;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class event implements Serializable{                                      // This class forms the basic building block of the Calendar
	//String time = "2009-07-20 0533";                                 
	String Text_Description;
	String Access_Control; 							// Private or public or group or open
	Stamp Start_Stamp, End_Stamp;
	event(String Start_String_Date, String End_String_Date, String Access_String_Control, String Text_Desc)      //constructor
	{
		String Lower_Access_String_Control= Access_String_Control.toLowerCase();
		String Lower_Text_Desc= Text_Desc.toLowerCase();
		Access_Control= Lower_Access_String_Control;
		Text_Description = Lower_Text_Desc;
		Start_Stamp = new Stamp(Start_String_Date);
		End_Stamp = new Stamp(End_String_Date);
	}  
	
	public boolean Contains_Event(event Param_Event)                                     // Method used to check if a event overlap another event
	{
		if(Start_Stamp.isbefore(Param_Event.Start_Stamp))
			if(End_Stamp.isafter(Param_Event.End_Stamp))
						return true;
		return false;
	}
	
	public boolean Intersects(event param_Event)						// Method used to check if an event collides with another event
	{
		if((Start_Stamp.isafter(param_Event.Start_Stamp))&&(Start_Stamp.isbefore(param_Event.End_Stamp)))
			return true;
		if((End_Stamp.isafter(param_Event.Start_Stamp))&&(End_Stamp.isbefore(param_Event.End_Stamp)))
			return true;
		return false;
	}
	
	public event Find_Intersection(event param_Event)					//This method is used in conjunction with the Intersects method to find the portion of the event that doesn't collide
	{
		if(Contains_Event(param_Event))
			return param_Event;
		if(param_Event.Contains_Event(this))
			return this;
		if((Start_Stamp.isafter(param_Event.Start_Stamp))&&(Start_Stamp.isbefore(param_Event.End_Stamp))){
			event Intersection_event_Start = new event(Start_Stamp.Stamp_In_String, param_Event.End_Stamp.Stamp_In_String, "group", "group event");
			return Intersection_event_Start;
		}
		if((End_Stamp.isafter(param_Event.Start_Stamp))&&(End_Stamp.isbefore(param_Event.End_Stamp))){
			event Intersection_event_End = new event(param_Event.Start_Stamp.Stamp_In_String, End_Stamp.Stamp_In_String, "group", "group event");
			return Intersection_event_End;
		}
		return null;
	}
	
	
	
	public String Print_Event()								//Print event description with the start and end dates of the event
	{
		String Event_To_String = "Starts: "+this.Start_Stamp.Stamp_In_String+"  Ends: "+this.End_Stamp.Stamp_In_String+"  Description:  "+this.Text_Description;
		return Event_To_String;
		
	}
	

}
