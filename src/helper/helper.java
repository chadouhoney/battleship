package helper;

import java.util.Arrays;
import java.util.ArrayList;

public class helper {

	public helper() {
		// TODO Auto-generated constructor stub
	}
	public static boolean isInList(final ArrayList<Integer[]> list, final Integer[] candidate){

	    for(final Integer[] item : list){
	        if(Arrays.equals(item, candidate)){
	            return true;
	        }
	    }
	    return false;
	}
	
	
	public static void removeFromList(final ArrayList<Integer[]> list, final Integer[] candidate){

	    for(final Integer[] item : list){
	        if(Arrays.equals(item, candidate)){
	            list.remove(item);
	            break;
	        }
	    }
	}
}
