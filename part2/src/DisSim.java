
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
					
					int status[]=new int[32];           // register status file, -1=ready, all other values indicate ROB
					for (int i=0;i<32;i++)
						status[i]=-1;
					//set status to ready
					
					// need to implement Main Memory (instructions + data segments)
					ArrayList<instr> Mem = s.getMem(args[1]); //  Memory 
					//GETMEM returns the standard instruction array, with the NOPS after it 
					//the NOPS have only their friendrep field set and type=5
					long CDB=0;//data bus
					int CDBtag=0;// tagging results on the bus
					boolean stall;
					instr it =Mem.get(0);
					int pc=0;
					while(true){ // loop through Clock Cycles independent of PC value 
						System.out.print(pc + ") ");
						it.printInstr();
						stall = false;
						
						//   I. IF : Instruction Fetch
						if(pc-600 < Mem.size() && !stall) {
							it=Mem.get(pc+1);
						
								iq.push(it);	
						}
							
						
						//  II. ID : Decode & Issue
						
						if(!rob.isFull() && !rs.isFull()&& !iq.kick() && !stall){ // if IQ has instruction and ROB and RS are ready
							// send operands to RS if any are available in Regs or ROB
							// send future calculation's id to relevant RS operand slot so it can be found upon completion
							instr instruction=iq.pop();
							if(instruction.type==4){
								robEntry br= new robEntry();
								br.stage=4;// nop and break goes straight to commit stage
								br.op=instruction.op;
								rob.push(br);
							}
								
							
							else{
								
								if(instruction.type==2){ // if branch stall and kick previous instruction
									iq.pop();
									stall=true;
								}
								
								
								rsEntry branch= new rsEntry();
								//00
								robEntry br= new robEntry();
								
								br.op=instruction.op;
								branch.op=instruction.op;
								br.stage=2; //execute stage
								branch.robIndex=rob.push(br); //adds entry to ROB, gives RS value in ROB
								if(status[(int) instruction.f1]==-1){
									branch.Vj=regs[(int)instruction.f1]; //register is ready, Vj holds value of operand
									branch.VjSrc=true;
								}
								else{
									branch.Qj=status[(int)instruction.f1] ;
									
																			//register is not ready Qj holds value of index in ROB
								}
								if(status[(int) instruction.f2]==-1)
									branch.Vk=regs[(int)instruction.f2]; //register is ready, Vk holds value of operand
								else{
									branch.Qk=status[(int)instruction.f2] ; 
								}
								
								branch.A=instruction.f3; //A holds offsets for immediate and branch
								
								rs.push(branch);
								
							}
							
				
						
						}
						else 
							stall=true;
						
						//end IF ID
						
						// III. EX : Execute
						//for each Reservation station check if operands are available 
						//perform operations
						
						for(int i=0;i<rs.max;i++){
							rsEntry station=rs.table.get(i);
							if(station.Qj==CDBtag){ //check bus for matching tag
								station.Vj=CDB;//if match set Vj
								station.VjSrc=true;
							}
							if(station.Qk==CDBtag){
								station.Vk=CDB; //same as above but for K
								station.VkSrc=true;
							}
							if(station.VjSrc && station.VkSrc){//if operands are ready
								//execute OP
								switch (station.op){
									case "000001":  if(station.Vk==1) {//bgez
													if(station.Vj>=0)
														pc+=station.A/4;
													else pc++;
														//BGTZ or BLTZ vk holds secondary opcode
													}
													else if(station.Vk==0){
														if(station.Vj<0)
															pc+=station.A/4;
														else pc++;
														
													}
									stall=false;//
									break;
								
								
								
								
								
								}
								
								
								
								
								
							}
							
						}
						
						
					
						//for opcode 000001 bgtz and bltz you will have to check vk to determine which comparison
						//to use eg. bltz=vk =00000
						
						
						//  IV. ME : Write Result
						//when one operation completes, check if it is the source of any other RS comparing its ID to Qs
						//update vk or vj values with operation value
						//write value onto CDB with ROB tab
						
						//   V. WB : Commit
						//check head of the ROB if it is commit stage then write to memory
						//
						
						
						
						it = Mem.get(pc-600);
					}
					
				} catch (IOException e){ e.printStackTrace(); }
		
			else System.out.println("invalid args");
		else System.out.println("invalid args");
	}
	
	
}
