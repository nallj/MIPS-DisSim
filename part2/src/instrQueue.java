import java.util.LinkedList;
import java.util.Queue;

public class instrQueue{
	Queue<instr> fifo;
	
	public instrQueue(){ fifo = new LinkedList<instr>(); }
	
	public void push(instr e){ fifo.add(e); }
	public instr pop(){ return fifo.remove(); }
	public instr peek(){ return fifo.peek(); }
	public boolean kick(){
		if(fifo.remove() != null) return true;
		else return false;
	}
	
	public void printData(){
		System.out.println("\nIQ:");
		Queue<instr> temp = new LinkedList<instr>();
		
		for(int i = 0; i < fifo.size(); i++){
			System.out.print("[inst" + i + "]");
			System.out.println(" " + fifo.peek().getFriendRep() );
			temp.add(fifo.poll());
		}
		
		fifo = temp;
	}
}