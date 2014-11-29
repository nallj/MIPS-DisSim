import java.util.ArrayList;

public class resSt{
	ArrayList<rsEntry> table;
	int max;
	
	public resSt(int m){
		max = m;
		table = new ArrayList<rsEntry>();						// initialize table list
		for(int i = 0; i < m; i++) table.add(new rsEntry());	// initialize all RS table entry slots
	}
	
	public boolean push(rsEntry e){
		if(!isFull()){
			table.add(e);
			return true;
		}else return false;
	}
	

	public void freeRS(long A){
		for(int i=0; i< table.size(); i++){
			if (A==table.get(i).A && table.get(i).stage==4)
				table.get(i).busy=false;
				
		}
	}
	
	public void updateOperands(int ROBindex, long value){
		for(int i=0;i<table.size();i++){
			rsEntry loopRS=table.get(i);
			if((long)ROBindex==loopRS.Qj){
				loopRS.Vj=value;
				loopRS.VjSrc=true;
			}
			if((long)ROBindex==loopRS.Qk){
				loopRS.Vk=value;
				loopRS.VkSrc=true;
			}
				
		}
	}
	
	public boolean kick(int x){

		if(table.size() != 0){
			table.remove(x);
			return true;
		}else return false;
	}
	
	public boolean isFull(){
		boolean full = true;
		for(int i = 0; i < max; i++)
			if(!table.get(i).isBusy()) full = false;
		
		return full;
	}
	
	public int getMax(){ return max; }
}