
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class DisSim {

	
	public static void main(String[] args) {
		if (args.length>1 && !args[1].equals(null))
			
			if (args[0].equals("dis")) // run Disassembler
				try {
					helper p=new helper();
					PrintWriter writer = new PrintWriter(args[2], "UTF-8");
					writer.print(p.disassemble(args[1]));
					writer.close();
					
				} catch (IOException e){ e.printStackTrace(); }
		
			else if(args[0].equals("sim")) // run Simulator
				try {
					helper s = new helper();
					
					// Pipeline Units
					instrQueue iq = new instrQueue();	// Instruction Queue (unlimited entries)
					resSt rs = new resSt(10);			// Reservation Station (10 entries for general use)
					roBuff rob = new roBuff(6);			// Reorder Buffer (6 entries)
					long regs[] = new long[32];			// Register File
					// need to implement Main Memory (instructions + data segments)
					ArrayList<instr> instrMem = s.getInstrMem(args[1]); // Instruction Memory
					
					boolean stall;
					instr it = instrMem.get(0);
					for(int cc = 0; cc < instrMem.size() && !rob.isDone(); cc++){ // loop through Clock Cycles
						System.out.print(cc + ") ");
						it.printInstr();
						stall = false;
						
						//   I. IF : Instruction Fetch
						if(cc < instrMem.size()) iq.push(it);	// every cc, deploy 1 instr to the IQ; BROKEN
						
						
						//  II. ID : Decode & Issue
						
						if(!rob.isFull() && !rs.isFull()){ // BROKEN
							// send operands to RS if any are available in Regs or ROB
							// send future calculation's id to relevant RS operand slot so it can be found upon completion
						}else stall = true;
						
						
						// III. EX : Execute
						
						
						//  IV. ME : Write Result
						
						
						//   V. WB : Commit
						
						
						it = instrMem.get(cc);
					}
					
				} catch (IOException e){ e.printStackTrace(); }
		
			else System.out.println("invalid args");
		else System.out.println("invalid args");
	}
	
	
}
