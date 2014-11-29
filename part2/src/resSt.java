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
	
	public rsEntry get(int x){
		return table.get(x);
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