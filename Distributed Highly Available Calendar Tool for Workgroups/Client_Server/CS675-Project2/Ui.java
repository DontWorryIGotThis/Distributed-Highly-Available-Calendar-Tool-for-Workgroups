// This is the user-Interface for the client

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Scanner;

public class Ui {
	
	public static void main(String args[]) throws NotBoundException, IOException{
		Ui obj_for_date_range_check = new Ui();
		int option;
		int flag=1;
		Scanner Scan = new Scanner(System.in);
		
		Registry Reg = LocateRegistry.getRegistry(args[0],rmi_constants.rmi_port);														// Registry Lookup after the Registry_Creator instantiates the rmi-Registry
		rmi_CManager_Interface CManager = (rmi_CManager_Interface) Reg.lookup(rmi_constants.rmi_CManager_Ref);					//CManager is the calendar manager object which is bound to the registry by the server
		System.out.println("Type the name of the  person for whom you would like to retrieve or create the calendar \n");
		String User_Name = Scan.nextLine();
		System.out.println("Please name the group to which "+User_Name+" belongs to.\n");
		String Group_Name = Scan.nextLine();
		System.out.println(CManager.Create_Calendar(User_Name, Group_Name));													// Creation of Calendar object
		while(true)
		{
		
			System.out.println("What would like to do with the calender?");
			System.out.println("Press: 1.To Schedule a private event");
			System.out.println("Press: 2.To Schedule a group event");
			System.out.println("Press: 3.To retrieve an event");
			System.out.println("Press: 4.To retrieve an event from the different user");
			System.out.println("Press: 5.To Schedule a public event");
			System.out.println("Press: 6.To exit");
			
			option= Scan.nextInt();
			Scan.nextLine();
			rmi_Calendar_Interface Users_Calender = (rmi_Calendar_Interface) Reg.lookup(User_Name.toLowerCase());				// After the Calendar Object is bound to the Registry by the calendar manager object this reference is used to call it's functions
			
			
			if(option ==1|| option ==5)															// Scheduling private event
			{
				String Start_String_Date, End_String_Date, Text_Description;
				System.out.println("Please enter the START date and time of the event to be organised in the format \"YYYY-MM-DD TTTT\" where 0000<TTTT<2400 \n");
				Start_String_Date=Scan.nextLine();
				System.out.println("Please enter the END date and time of the event to be organised in the format \"YYYY-MM-DD TTTT\" where 0000<TTTT<2400 \n");
				End_String_Date=Scan.nextLine();
				if(obj_for_date_range_check.date_range_check(Start_String_Date,End_String_Date)){
					System.out.println("Please enter the Text Description.");
					Text_Description=Scan.nextLine();
					if(option==1)
					if(Users_Calender.Schedule_Event(Start_String_Date, End_String_Date, Text_Description, "private"))
						System.out.println("The Private Event has been scheduled");
					else
						System.out.println("The Private Event could not be scheduled");
					if(option==5)
						if(Users_Calender.Schedule_Event(Start_String_Date, End_String_Date, Text_Description, "public"))
							System.out.println("The Public Event has been scheduled");
						else
							System.out.println("The Public Event could not be scheduled");
				}
				else
					System.out.println("The Date and time that you have mentioned are invalid");
				
			}
			
			if(option ==2)															// Scheduling Group event
			{
				String Start_String_Date, End_String_Date, Text_Description = "";
				String[] Group_Users;
				Integer Number_of_Group_Users;
				System.out.println("Please enter the START date and time of the event to be organised in the format \"YYYY-MM-DD TTTT\" where 0000<TTTT<2400 \n");
				Start_String_Date=Scan.nextLine();
				System.out.println("Please enter the END date and time of the event to be organised in the format \"YYYY-MM-DD TTTT\" where 0000<TTTT<2400 \n");
				End_String_Date=Scan.nextLine();
				if(obj_for_date_range_check.date_range_check(Start_String_Date,End_String_Date)){
					System.out.println("Please enter the Text Description.");
					Text_Description=Scan.nextLine();
				}
				System.out.println("Enter the number of users in the group");
				Number_of_Group_Users= Scan.nextInt();
				Scan.nextLine();
				Group_Users= new String[Number_of_Group_Users];
				System.out.println("Enter the name of the users");				
				for(int i=0; i<Number_of_Group_Users; i++)
					Group_Users[i]=Scan.nextLine();
				//add total_lol_events[ArrayString size+1] = return_Open_eventss
				if(Users_Calender.Schedule_Event(Group_Users, Start_String_Date, End_String_Date, Text_Description))
					System.out.println("The group event has been scheduled");
				else
					System.out.println("The group event was not scheduled");
			}
			
			if(option ==3)															// Retrieving events in a timespace
			{
				String a;
				String b;
				System.out.println("Enter the starting date and time from which you would like to view the events- format \"YYYY-MM-DD TTTT\" where 0000<TTTT<2400 \n");
				a = Scan.nextLine();
				System.out.println("Enter the ending date and time till which you would like to view the events- format \"YYYY-MM-DD TTTT\" where 0000<TTTT<2400 \n");
				b = Scan.nextLine();
				if(obj_for_date_range_check.date_range_check(a,b)){
					System.out.println("The private events scheduled for "+User_Name+" are as follows:");
					System.out.println(Users_Calender.Retrieve_Event(a,b)+"\n");
				}
				else
					System.out.println("The Date and time that you have mentioned are invalid");
			}
			
			if(option ==4)															// Retrieving events from the different User
			{
				String a;
				String b;
				System.out.println("Please enter the name of the user who's schedule you would like to view");
				String Other_User = Scan.nextLine();
				System.out.println("Enter the starting date in the format \"YYYY-MM-DD TTTT\" where 0000<TTTT<2400 \n");
				a = Scan.nextLine();
				System.out.println("Enter the ending date in the format \"YYYY-MM-DD TTTT\" where 0000<TTTT<2400 \n");
				b = Scan.nextLine();
				if(obj_for_date_range_check.date_range_check(a,b)){
				System.out.println("The viewable events scheduled for "+Other_User+" are as follows:");
				System.out.println(Users_Calender.Retrieve_Event(Other_User,a,b)+"\n");
				}
				else
					System.out.println("The Date and time that you have mentioned are invalid");
			}
			
			if(option ==6)
			{
				break;
			}	
		}
		Scan.close();
		System.exit(0);
	}
	
	public boolean date_range_check(String String_Start_Date, String String_End_Date){
		try{
			int Start_year = Integer.parseInt(String_Start_Date.substring(0, 4));
			int Start_month = Integer.parseInt(String_Start_Date.substring(5, 7));
			int Start_day= Integer.parseInt(String_Start_Date.substring(8, 10));
			int Start_time= Integer.parseInt(String_Start_Date.substring(11, 15));
		
			int End_year = Integer.parseInt(String_End_Date.substring(0, 4));
			int End_month = Integer.parseInt(String_End_Date.substring(5, 7));
			int End_day= Integer.parseInt(String_End_Date.substring(8, 10));
			int End_time= Integer.parseInt(String_End_Date.substring(11, 15));
		
		
		
		if((End_year>=Start_year) && (Start_year>=2013) &&(End_year<2014))
			if((End_month>=Start_month) && (Start_month>0) &&(End_month<=12)){
				if(End_month>Start_month)
					return true;
				if(End_month==Start_month)
					if((End_day>=Start_day) && (Start_day>0) &&(End_day<=31)){
						if(End_day>Start_day)
							return true;
						if(End_day==Start_day)
							if((End_time>=Start_time) && (Start_time>=0) &&(End_time<=2400))
								return true;
					}
			}			
		return false;
		} catch (Exception e)
		{
			System.out.println("There was an error in reading the date, Please try again.");
			return false;
		}
	}
	
	
	

	
	
	


}
