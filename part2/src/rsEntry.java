public class rsEntry{
	boolean busy = false;	// busy?
	int instrId;			// 
	
	int Vj;					// Vj : id
	boolean VjSrc;			// Vj : source = {true for RS, false for ROB)
	
	int Vk;					// Vk : id
	boolean VkSrc;			// Vk : source = {true for RS, false for ROB)
	
	int Qj;					// Qj : id
	boolean QjSrc;			// Qj : source = {true for RS, false for ROB)
	
	int Qk;					// Qk : id
	boolean QkSrc;			// Qk : source = {true for RS, false for ROB)
	// A
	
	public rsEntry(){ busy = true; }
	
	public boolean isBusy(){ return busy; }
}