import java.util.LinkedList;
import java.util.Queue;

public class instrQueue{
	Queue<instr> fifo;
	
	public instrQueue(){ fifo = new LinkedList<instr>(); }
	
	public void push(instr e){ fifo.add(e); }
	
	public boolean kick(){
		if(fifo.remove() != null) return true;
		else return false;
	}
}