import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class roBuff{
	ArrayList<robEntry> fifo;  //needed to be array list so i can access elements out of order
	int max;
	boolean finished = false;
	
	public roBuff(int m){
		max = m;
		fifo = new ArrayList<robEntry>();						// initialize table list
	}
	

	public int push(robEntry e){
		
			
			fifo.add(e);
			return fifo.size();
	}
	
	public robEntry getIndex(int index){
		robEntry thisEntry = null;
		for(int i=0;i<fifo.size();i++){
			if(fifo.get(i).robIndex==index)
				thisEntry=fifo.get(i);
		}
		return thisEntry;
		
	}
	
	public boolean isDependent(long A,int robIndex){
		boolean dependent=false;
		
		for(int i=0; i< fifo.size(); i++){
			if(fifo.get(i).robIndex<robIndex && fifo.get(i).destination==A)// if entry preceeds current address
				if(fifo.get(i).stage!=4)
					dependent=true;
		}
		
		return dependent;
		
	}
	
	
	public boolean isCommit(){
		robEntry head=fifo.get(0);
		if(head!=null && head.stage==4){
			return true;
		}
		return false;
		
		
	}
	public robEntry pop(){
		return fifo.get(0);
	}
	public boolean kick(){
		if(fifo.get(fifo.size()-1) != null){
			if(fifo.size() == 0) finished = true;	// if you commit the final instruction, we're done
			return true;
		}else return false;
	}
	
	public boolean isFull(){
		if(fifo.size() == max) return true;
		else return false;
	}
	
	public boolean isDone(){ return finished; }
}