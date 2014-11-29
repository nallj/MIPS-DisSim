public class rsEntry{
	boolean busy = false;	// busy?

	int robIndex;	
	int stage;
	String op;       //opcode for execute stage
	long Vj;					// Vj : value of operand j

	boolean VjSrc;			// Vj : whether Vj is set or not
	
	long Vk;				// Vk : value of operand k
	boolean VkSrc;			// Vk : is Vk set?
	
	long Qj;				// Qj : id of reservation station where j operand will appear
	boolean QjSrc;			// Qj : source = {true for RS, false for ROB)
	
	long Qk;				// Qk : id of reservation station where k will appear
	boolean QkSrc;			// Qk : source = {true for RS, false for ROB)
	
	long A;		// address offset for load store in ID, holds effective address after execution
	
	public rsEntry(){
		busy = true; 
		VjSrc=false;
		VkSrc=false;
	}
	
	public rsEntry(String o, int r){
		op = o;
		robIndex = r;
		
		busy = true; 
		VjSrc=false;
		VkSrc=false;
	}
	
	// setters
	public void setOp(String o){ op = o; }
	public void setVj(long t, boolean src){ Vj = t; VjSrc = src; }
	public void setVk(long t, boolean src){ Vk = t; VkSrc = src; }
	public void setQj(long t, boolean src){ Qj = t; QjSrc = src; }
	public void setQk(long t, boolean src){ Qk = t; QkSrc = src; }
	public void setAddr(long a){ A = a; }
	
	// getters
	public String getOp(){ return op; }
	public long getAddr(){ return A; }
	public long getVal(char o, char t){
		long r = 0;
		if(o == 'V'){
			if(t == 'j') r = Vj;
			if(t == 'k') r = Vk;
		}else if(o == 'Q'){
			if(t == 'j') r = Qj;
			if(t == 'k') r = Qk;
		}
		return r;
	}
	public boolean getSrc(char o, char t){
		boolean r = false;
		if(o == 'V'){
			if(t == 'j') r = VjSrc;
			if(t == 'k') r = VkSrc;
		}else if(o == 'Q'){
			if(t == 'j') r = QjSrc;
			if(t == 'k') r = QkSrc;
		}
		return r;
	}
	
	public boolean isBusy(){ return busy; }
}