public class rsEntry{
	boolean busy = false;	// busy?

	int robIndex;
	int stage;				// issue=1 execute=2 memory=3 commit=4
	String op;				//opcode for execute stage
	int type;				// instruction type 2= branch 4 nop/break   //special codes 6+
	
	long Vj;				// Vj : value of operand j
	boolean VjSrc;			// Vj : whether Vj is set or not
	
	long Vk;				// Vk : value of operand k
	boolean VkSrc;			// Vk : is Vk set?
	
	long Qj;				// Qj : id of reservation station where j operand will appear
	boolean QjSrc;			// Qj : source = {true for RS, false for ROB)
	
	long Qk;				// Qk : id of reservation station where k will appear
	boolean QkSrc;			// Qk : source = {true for RS, false for ROB)
	
	long A;		// address offset for load store in ID, holds effective address after execution
	long result;
	
	public rsEntry(){
		busy = false; 
		VjSrc=false;
		VkSrc=false;
	}
	
	public rsEntry(String o, int r,int stage){
		op = o;
		robIndex = r;
		this.stage=stage;
		busy = false; 
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
	public boolean isBusy(){ return busy; }
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
	
	public String display(){
		String disp = "Busy: ";
		
		if(busy) disp += "Yes  ";
		else disp += "No   ";
		
		disp += op + "     ";	// display op
		disp += "Vj: #" + Long.toHexString(Vj) + "   ";
		disp += "Vk: #" + Long.toHexString(Vk) + "   ";
		disp += "Qj: #" + Long.toHexString(Qj) + "   ";
		disp += "Qk: #" + Long.toHexString(Qk);
		
		return disp;
	}
}