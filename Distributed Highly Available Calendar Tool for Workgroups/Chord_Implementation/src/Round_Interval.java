
public class Round_Interval {
		Integer a;
		Integer b;
		public Round_Interval(Integer i_start, Integer i_plus1_start) {
			a=i_start;
			b=i_plus1_start;
			
		}
		
		public boolean contains( Integer ID){
			Integer id =ID;
			if(a<b)
				if((a<=id)&&(id<b))
					return true;
			if(a>b)
				if((a<=id)||(id<b))
					return true;
			return false;
		}
	}