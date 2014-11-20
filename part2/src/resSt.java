import java.util.ArrayList;

public class resSt{
	ArrayList<rsEntry> table;
	int max;
	
	public resSt(int m){ max = m; }
	
	public boolean push(rsEntry e){
		if(!isFull()){
			table.add(e);
			return true;
		}else return false;
	}
	
	public boolean kick(rsEntry x){
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
}