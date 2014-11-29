
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class DisSim {
	static boolean DIAG = true;
	
	public static void main(String[] args) {
		System.out.println(args);
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
					for(int i=0;i<32;i++) status[i]=-1;	// set status to ready
					
					// need to implement Main Memory (instructions + data segments)
					ArrayList<instr> mem = s.getMem(args[1]); //  Memory 
					//GETMEM returns the standard instruction array, with the NOPS after it 
					//the NOPS have only their friendrep field set and type=4
					ArrayList<Pair> CDB= new ArrayList<Pair>();//data bus
					//int CDBtag=0;// tagging results on the bus
					boolean stall;
					instr it = mem.get(0);
					int cc = 0, pc = 0;
					
					while(true){ // loop through Clock Cycles independent of PC value 
						if(DIAG) System.out.print(cc + ") ");
						
						it.printInstr();
						stall = false;
						
						//   I. IF : Instruction Fetch
						if(cc < mem.size() && !stall) {
							it = mem.get(pc+1);
							iq.push(it);	
						}
							
						
						//  II. ID : Decode & Issue
						if(!rob.isFull() && !rs.isFull()&& !iq.kick() && !stall){ // if IQ has instruction and ROB and RS are ready, else stall
							// send operands to RS if any are available in Regs or ROB
							// send future calculation's id to relevant RS operand slot so it can be found upon completion
							instr instruction=iq.pop();
							
							if(instruction.type==4){ // nop/break type
								
								robEntry br = new robEntry(instruction.getOp(), 4);
									// nop and break goes straight to commit stage
								rob.push(br);
									// don't send to RS
							}else{
								
								if(instruction.type==2){ // if branch, stall and kick previous instruction
									/*	is this really right? we don't immediately know the resolved target address
										that gets handled when the branch passes through EX stage	*/ 
									iq.pop();
									stall=true;
								}
								
								robEntry br = new robEntry(instruction.getOp(), 2);	// new ROB extry @ execute stage
										br.robIndex=cc;
								rsEntry branch = new rsEntry(instruction.getOp(), rob.push(br),2); //new RS entry @execute
									// push ROB entry, assign ROB index to RS entry
										branch.robIndex=cc;
								if(status[(int) instruction.getField(1)]==-1) // if operand 1 ready
									branch.setVj(regs[(int) instruction.getField(1)], true);
										//register is ready, Vj holds value of operand
								else branch.setQj(status[(int) instruction.getField(1)], false);
										//register is not ready Qj holds value of index in ROB
								
								if(status[(int) instruction.getField(2)]==-1) // if operand 2 ready
									branch.setVk(regs[(int) instruction.getField(2)], true);
										//register is ready, Vk holds value of operand
								else branch.setQk(status[(int) instruction.getField(2)], false);
										//register is not ready Qk holds value of index in ROB
								
								branch.setAddr( instruction.getField(3) ); //A holds offsets for immediate and branch
								rs.push(branch);
							}
							
				
						
						}else stall = true;
						
						//end IF ID
						
						// III. EX : Execute
						//for each Reservation station check if operands are available 
						//perform operations
						
						for(int i=0; i<rs.getMax(); i++){
							rsEntry task=rs.table.get(i);
							
						/*the operands are now updated in MEM/WB stage
						 * 
						 * 	if(task.getVal('Q','j')==CDBtag) //check bus for matching tag
								task.setVj(CDB, true); //if match set Vj
							
							if(task.getVal('Q','k')==CDBtag)
								task.setVk(CDB, true); //if match set Vk
							*/
							//if operands are ready
							if(task.getSrc('V','j') && task.getSrc('V','k') && task.stage==2){
								//execute OP
								switch( task.getOp() ){
								
									case "000001":  if(task.getVal('V','k')==1){ //bgez
														if(task.getVal('V','j')>=0)
															task.setAddr(pc+ task.getAddr()/4) ;
														else task.setAddr(pc++); //BGTZ or BLTZ vk holds secondary opcode
														
													}else if(task.getVal('V','k')==0){
														if(task.getVal('V','j')<0)
															task.setAddr(pc+ task.getAddr()/4) ;
														else  task.setAddr(pc++);
													}
													task.stage=4;
													
													break;
									case "101011":
													task.setAddr(task.getVal('V','j')+task.getAddr());//store
													if(rob.isDependent(task.getAddr(), task.robIndex))
														break; //if dependent, then instruction must wait since store must be done in order
													task.stage=4;
													
													break;
									case "100011": 
													task.setAddr(task.getVal('V','j')+task.getAddr());//load
													if(rob.isDependent(task.getAddr(), task.robIndex))
														break; //if dependent, then instruction must wait
													task.stage=3;
													break;
									case "000010": task.stage=4; //jump address is already calculated
												   rob.getIndex(task.robIndex).stage=4;
												  
												   
												   break;
									case "000111":  //bgTz
													if(task.getVal('V','j')>0)
														task.setAddr(pc+ task.getAddr()/4) ;
													else task.setAddr(pc++); 
													task.stage=4;
													
													break;
									//case "000100" : if()
									
												   
								
								}
							}
							
						}
						//for opcode 000001 bgtz and bltz you will have to check vk to determine which comparison
						//to use eg. bltz=vk =00000
						
						
						//  IV. ME : Write Result
						//when one operation completes, check if it is the source of any other RS comparing its ID to Qs
						//update vk or vj values with operation value
						//write value onto CDB with ROB tab
						for(int i=0;i<rs.getMax();i++){// for each RS
							rsEntry loopRs= rs.table.get(i);
							
							if(loopRs.stage==3 && loopRs.type!=2 && loopRs.type!=4){	//if in ME stage not branch or NOP
								if(loopRs.type==3){//if LS
									if(!loopRs.op.equals("101011")){ //if not STore store skips this stage
										
									//load takes two cycles, first cycle memory access
										loopRs.result=mem.get((int) loopRs.A).mem;
										loopRs.stage=7;
									}
									
								}
								else {
									Pair result=new Pair(loopRs.robIndex,loopRs.result);
									CDB.add(result);
									rs.updateOperands(loopRs.robIndex,loopRs.result);
									robEntry loopRob=rob.fifo.peek();
									if(loopRob.robIndex==loopRs.robIndex){
										loopRob.value=loopRs.result;
										loopRob.stage=4;
									}
								}
							}
							
							if(loopRs.stage==7){ //if second stage of load normal WB
								Pair result=new Pair(loopRs.robIndex,loopRs.result);
								CDB.add(result);
								rs.updateOperands(loopRs.robIndex,loopRs.result);
								robEntry loopRob=rob.fifo.peek();
								if(loopRob.robIndex==loopRs.robIndex){
									loopRob.value=loopRs.result;
									loopRob.stage=4;
							}
						}
						
						}	
						
						//   V. WB : Commit
						//check head of the ROB if it is commit stage then write to memory
						//

						if(rob.isCommit()){
							
							robEntry head=rob.pop();// get head of ROB and remove from ROB
						
							if(head.type==3 && head.op.equals("101011")){ //if store
								mem.get(head.destination).mem=head.value;
								for(int i=0; i<rs.max;i++){
									if(rs.table.get(i).A==head.destination && rs.table.get(i).stage==6 ){//if in dependent state
										rs.table.get(i).stage=3;
									}
								}
							}	
							else //if load or alu instruction
								regs[head.destination]=head.value;
						}
						

						++cc;
						//it = mem.get(++cc); // increment CCs, load next instruction

					}
					
				} catch (IOException e){ e.printStackTrace(); }
		
			else System.out.println("invalid args");
		else System.out.println("invalid args");
	}
	
	
}
