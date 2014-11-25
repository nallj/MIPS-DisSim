public class rsEntry{
	boolean busy = false;	// busy?
	int robIndex;			
	String op;       //opcode for execute stage
	long Vj;					// Vj : value of operand j
	boolean VjSrc;			// Vj : whether Vj is set or not
	
	long Vk;					// Vk : value of operand K
	boolean VkSrc;			// Vk : is Vk set?
	
	long Qj;					// Qj : id of reservation station where j operand will appear
	boolean QjSrc;			// Qj : source = {true for RS, false for ROB)
	
	long Qk;					// Qk : id of reservation station where k will appear
	boolean QkSrc;			// Qk : source = {true for RS, false for ROB)
	long  A	;				//address offset for load store in ID, holds effective address after execution
	
	public rsEntry(){ busy = true; 
	VjSrc=false;
	VkSrc=false;
	
	
	}
	
	public boolean isBusy(){ return busy; }
}