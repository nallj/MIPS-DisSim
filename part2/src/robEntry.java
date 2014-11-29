public class robEntry{
	int type;			// instruction type
	int destination;	// destination
	String op;
	long value;			// value?
	boolean ready;		// ready
	int stage; // issue=1 execute=2 memory=3 commit=4
	// why is memory stage necessary?
	int robIndex;
	public robEntry(){
		ready = false; 
		stage = 1; // start at issue
	}
	
	public robEntry(String o, int s){
		op = o;
		stage = s;
	}

}