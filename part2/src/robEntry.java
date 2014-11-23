public class robEntry{
	int type;		// instruction type
	// destination
	String op;
	long value;		// value?
	boolean ready;	// ready
	int destination; 
	int stage; // issue=1 execute=2 memory=3 commit=4 
	public robEntry(){ ready = false; 
	stage=1;
	
	}
	
	public static void main(String[] args){
		// TODO Auto-generated method stub

	}

}