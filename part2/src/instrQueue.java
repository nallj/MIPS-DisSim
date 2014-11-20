import java.util.Queue;

public class instrQueue{
	Queue<instr> fifo;
	
	public instrQueue(){ }
	
	public void push(instr e){ fifo.add(e); }
	
	public boolean kick(){
		if(fifo.remove() != null) return true;
		else return false;
	}

}