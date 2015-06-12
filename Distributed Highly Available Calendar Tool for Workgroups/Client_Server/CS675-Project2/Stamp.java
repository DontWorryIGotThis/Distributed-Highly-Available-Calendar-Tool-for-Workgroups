import java.io.Serializable;

public class Stamp implements Serializable{                        // This class is used to Set the basic data on which the entire Calendar system works
	// Input String should be of the format  "yyyy-mm-dd TTTT"  ----- Time should be out of 2400
	public Stamp(String k){
		Stamp_In_String = k;
		year = Integer.parseInt(k.substring(0, 4));
		month = Integer.parseInt(k.substring(5, 7));
		day= Integer.parseInt(k.substring(8, 10));
		time= Integer.parseInt(k.substring(11, 15));
	}
	String Stamp_In_String;
	int year ;
	int month;
	int day;
	int time;
	
	public boolean isbefore(Stamp Stamp_To_Compare)                   // Checks if the stamp object calling this function was set to a date prior to the stamp date sent as a parameter
	{
		if(this.year<=Stamp_To_Compare.year){
			if(this.year<Stamp_To_Compare.year)
				return true;
			if(this.year==Stamp_To_Compare.year)
				if(this.month<=Stamp_To_Compare.month){
					if(this.month<Stamp_To_Compare.month)
						return true;
					if(this.month==Stamp_To_Compare.month){
						if(this.day<=Stamp_To_Compare.day){
							if(this.day<Stamp_To_Compare.day)
								return true;
							if(this.day==Stamp_To_Compare.day){
								if(this.time<Stamp_To_Compare.time)
									return true;
								return false;
							}	
						}
					}			
				}
		}
		return false;
	}
	
	public boolean isafter(Stamp Stamp_To_Compare)			//// Checks if the stamp object calling this function was set to a date after the stamp object sent as a parameter
	{
		if(this.year>=Stamp_To_Compare.year){
			if(this.year>Stamp_To_Compare.year)
				return true;
			if(this.year==Stamp_To_Compare.year)
				if(this.month>=Stamp_To_Compare.month){
					if(this.month>Stamp_To_Compare.month)
						return true;
					if(this.month==Stamp_To_Compare.month){
						if(this.day>=Stamp_To_Compare.day){
							if(this.day>Stamp_To_Compare.day)
								return true;
							if(this.day==Stamp_To_Compare.day){
								if(this.time>=Stamp_To_Compare.time)
									return true;
								return false;
							}	
						}
					}			
				}
		}
		return false;
	}
}
