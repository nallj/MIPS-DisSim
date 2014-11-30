public class robEntry{
	int type;			// instruction type
	long destination;	// destination
	String op;
	long value;			// value?
	boolean ready;		// ready
	int stage; // issue=1 execute=2 memory=3 commit=4
	// why is memory stage necessary?
	int robIndex;

	public robEntry(){
		ready = true;; 
		stage = 1; // start at issue
	}
	
	public robEntry(String o, int s){
		op = o;
		stage = s;
	}
	
	public String display(){
		String disp = "Busy: ";
		
		if(ready) disp += "No   ";
		else disp += "Yes  ";
		
		disp += op + "     ";	// display op
		
		switch(stage){
			case 1: disp += "Issue"; break;
			case 2: disp += "Execute"; break;
			case 3: disp += "Memory"; break;
			case 4: disp += "Commit";
		}
		disp += "    Dest: #" + Long.toHexString(destination)
			  + "    Value: #" + Long.toHexString(value);
		
		return disp;
	}

}