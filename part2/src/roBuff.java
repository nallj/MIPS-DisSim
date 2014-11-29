import java.util.LinkedList;
import java.util.Queue;

public class roBuff{
	Queue<robEntry> fifo;
	int max;
	boolean finished = false;
	
	public roBuff(int m){
		max = m;
		fifo = new LinkedList<robEntry>();	// initialize table list
	}
	
	public int push(robEntry e){
			fifo.add(e);
			return fifo.size();
	}
	
	public boolean kick(){
		if(fifo.remove() != null){
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