import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class helper {
	//public enum instrType{FP, LD, SD, NA}
	
	String file;
	int typeHold = 5; // 0 = FP, 1 = IM, 2 = BR, 3 = LS, 4 = N/A, 5 = ??
	//long f1, f2, f3;
	String b0, b1, b2, b3;
	long rs,rt,rd=0;
	
	public helper(){ file=""; }
	
	public String disassemble(String inputfilename) throws IOException {
		
		FileInputStream fis = null;
		
		fis = new FileInputStream(inputfilename);
        byte[] bs= new byte[4];
        // read bytes to the buffer
        int n=4;
        boolean broken=false;
        String[] instructionParts;
        int memcounter=600;
        while(!broken){
        	n=fis.read(bs);
        
        	if(n<4){
        		System.out.println("incomplete word");
        		fis.close();
        		return "error";
        	}
        	 instructionParts=formatWord(bs);
    	
        	for(int i=0;i<6;i++)
        		file+=instructionParts[i]+" ";
        	file+=memcounter+" ";
        	memcounter+=4;
      
        	broken=disassembleInstruction(instructionParts, true);
        	file+="\n";
        	
		
       }
        while(true){
        	n=fis.read(bs);
        	if(n!=4)
        		break;
        	String word=getWord(bs);
        	file+=word+" "+ memcounter+" "+Integer.parseInt(word, 2);
        	memcounter+=4;
        	file+="\n";
        	
        }
        fis.close();
       return (file);
	}
	

// very similar to "str disassemble(str)" - instead of outputting a string file, output instr ArrayList
public ArrayList<instr> getMem(String inputfilename) throws IOException {
		
		FileInputStream fis = null;
		ArrayList<instr> instrArr = new ArrayList<instr>();	// collect instructions for use by the simulator
		
		fis = new FileInputStream(inputfilename);
        byte[] bs= new byte[4];
        // read bytes to the buffer
        int n=4;
        boolean broken=false;
        String[] instructionParts;
        int memcounter=600;
        
        while(!broken){
        	instr subject = new instr(memcounter);	// create new Instruction at address 'memcounter'
        	n=fis.read(bs);
        
        	if(n<4){
        		System.out.println("incomplete word");
        		fis.close();
        		return null;
        	}
        	instructionParts=formatWord(bs);
        	
        	broken=disassembleInstruction(instructionParts, false);
        	
        	// transfer data to instruction object
        	subject.setType(typeHold);
        	subject.setOp(file.split(" ")[0]);
        	subject.setFriendRep(file);
        	subject.setFields(rs, rt, rd);
        	
        	
        	file = "";	// clear all holding variables
        	memcounter+=4;
        	
        	instrArr.add(subject);
       }
        
        while(true){	// handle NOPs after the BREAK command
        	n=fis.read(bs);
        	if(n!=4)
        		break;
        	instr subject=new instr();
        	subject.friendRep=getWord(bs);
        	subject.type=4;
        	instrArr.add(subject);
        	
        	memcounter+=4;
        }
        
        fis.close();
        return instrArr;
	}
	
	public boolean disassembleInstruction(String[] instructionParts, boolean print){
		boolean broken=false;
		//int rs,rt,rd=0;
		String foo;
		int opcode=Integer.parseInt(instructionParts[0], 2);
		switch (opcode) {
        case 0: 
        		
        		if(instructionParts[5].equals("001101")){
	        		broken=true;
	        		file+="BREAK";
	        		typeHold = 4; // N/A
	        		//b0 = "001101";
        		}
        		if(instructionParts[5].equals("000000") && instructionParts[2].equals("00000")){
        			rd=Integer.parseInt(instructionParts[3],2);
        			if(rd==0)
        			file+="NOP";
        			typeHold = 4; // N/A
        			//b0 = "000000";
        		}
        		if(instructionParts[5].equals("100001")){
        			file+="ADDU ";
        			rs=Integer.parseInt(instructionParts[1], 2);
            		rt=Integer.parseInt(instructionParts[2], 2);
            		rd=Integer.parseInt(instructionParts[3], 2);
            		file+="R"+rd+", R"+rs+", R"+rt;
            		typeHold = 0; // FP
            		//f1 = rs; f2 = rt; f3 = rd;
            		//b0 = ;
        		}
        		if(instructionParts[5].equals("100000")){
        			file+="ADD ";
        			rs=Integer.parseInt(instructionParts[1], 2);
            		rt=Integer.parseInt(instructionParts[2], 2);
            		rd=Integer.parseInt(instructionParts[3],2);
            		file+="R"+rd+", R"+rs+", R"+rt;
            		typeHold = 0; // FP
            		//b0 = ;
        		}
        		if(instructionParts[5].equals("100100")){
        			file+="AND ";
        			rs=Integer.parseInt(instructionParts[1], 2);
            		rt=Integer.parseInt(instructionParts[2], 2);
            		rd=Integer.parseInt(instructionParts[3],2);
            		file+="R"+rd+", R"+rs+", R"+rt;
            		typeHold = 0; // FP
            		//b0 = ;
        		}
        		if(instructionParts[5].equals("100101")){
        			file+="OR ";
        			rs=Integer.parseInt(instructionParts[1], 2);
            		rt=Integer.parseInt(instructionParts[2], 2);
            		rd=Integer.parseInt(instructionParts[3],2);
            		file+="R"+rd+", R"+rs+", R"+rt;
            		typeHold = 0; // FP
            		//b0 = ;
        		}
        		if(instructionParts[5].equals("100110")){
        			file+="XOR ";
        			rs=Integer.parseInt(instructionParts[1], 2);
            		rt=Integer.parseInt(instructionParts[2], 2);
            		rd=Integer.parseInt(instructionParts[3],2);
            		file+="R"+rd+", R"+rs+", R"+rt;
            		typeHold = 0; // FP
            		//b0 = ;
        		}
        		if(instructionParts[5].equals("100111")){
        			file+="NOR ";
        			rs=Integer.parseInt(instructionParts[1], 2);
            		rt=Integer.parseInt(instructionParts[2], 2);
            		rd=Integer.parseInt(instructionParts[3],2);
            		file+="R"+rd+", R"+rs+", R"+rt;
            		typeHold = 0; // FP
            		//b0 = ;
        		}
        		if(instructionParts[5].equals("100010")){
        			file+="SUB ";
        			rs=Integer.parseInt(instructionParts[1], 2);
            		rt=Integer.parseInt(instructionParts[2], 2);
            		rd=Integer.parseInt(instructionParts[3],2);
            		file+="R"+rd+", R"+rs+", R"+rt;
            		typeHold = 0; // FP
            		//b0 = ;
        		}
        		if(instructionParts[5].equals("100011")){
        			file+="SUBU ";
        			rs=Integer.parseInt(instructionParts[1], 2);
            		rt=Integer.parseInt(instructionParts[2], 2);
            		rd=Integer.parseInt(instructionParts[3],2);
            		file+="R"+rd+", R"+rs+", R"+rt;
            		typeHold = 0; // FP
            		//b0 = ;
        		}
        		
        		if(instructionParts[5].equals("000000")){
        			
        			rs=Integer.parseInt(instructionParts[4], 2);
            		rt=Integer.parseInt(instructionParts[2], 2);
            		rd=Integer.parseInt(instructionParts[3],2);
            		if(rt!=0){
            			file+="SLL "; 
            			file+="R"+rd+", R"+rt+", #"+rs;
            			typeHold = 1; // IM
            		}
        		}
        		if(instructionParts[5].equals("000010")){
        			file+="SRL ";
        			rs=Integer.parseInt(instructionParts[4], 2);
            		rt=Integer.parseInt(instructionParts[2], 2);
            		rd=Integer.parseInt(instructionParts[3],2);
            		file+="R"+rd+", R"+rt+", #"+rs;
            		typeHold = 1; // IM
        		}
        		if(instructionParts[5].equals("000011")){
        			file+="SRA ";
        			rs=Integer.parseInt(instructionParts[4], 2);
            		rt=Integer.parseInt(instructionParts[2], 2);
            		rd=Integer.parseInt(instructionParts[3],2);
            		file+="R"+rd+", R"+rt+", #"+rs;
            		typeHold = 1; // IM
        		}
        		if(instructionParts[5].equals("101010")){
        			file+="SLT ";
        			rs=Integer.parseInt(instructionParts[1], 2);
            		rt=Integer.parseInt(instructionParts[2], 2);
            		rd=Integer.parseInt(instructionParts[3],2);
            		file+="R"+rd+", R"+rs+", R"+rt;
            		typeHold = 0; // FP
        		}
        		if(instructionParts[5].equals("101011")){
        			file+="SLTU ";
        			rs=Integer.parseInt(instructionParts[1], 2);
            		rt=Integer.parseInt(instructionParts[2], 2);
            		rd=Integer.parseInt(instructionParts[3],2);
            		file+="R"+rd+", R"+rs+", R"+rt;
            		typeHold = 0; // FP
        		}
        		
        		
                break;
                
        case 8: file+="ADDI ";
        		 rs=Integer.parseInt(instructionParts[1], 2);
        		 rt=Integer.parseInt(instructionParts[2], 2);
        		 foo=instructionParts[3]+instructionParts[4]+instructionParts[5];
        		 rd=Integer.parseInt(foo, 2);
        		 if(foo.startsWith("1"))
					 rd=rd-65536;
        		 file+="R"+rt+", R"+rs+", #"+rd;
        		 typeHold = 1; // IM
            	break;  
            	
            	
        case 10:file+="SLTI ";
        		rs=Integer.parseInt(instructionParts[1], 2);
        		rt=Integer.parseInt(instructionParts[2], 2);
        		foo=instructionParts[3]+instructionParts[4]+instructionParts[5];
        		rd=Integer.parseInt(foo, 2);
        		if(foo.startsWith("1"))
        			rd=rd-65536;
        		file+="R"+rt+", R"+rs+", #"+rd;
        		typeHold = 1; // IM
        		break;  
       
                
        case 9: file+="ADDIU ";
				 rs=Integer.parseInt(instructionParts[1], 2);
				 rt=Integer.parseInt(instructionParts[2], 2);
				 foo=instructionParts[3]+instructionParts[4]+instructionParts[5];
				 rd=Integer.parseInt(foo, 2);
				 if(foo.startsWith("1"))
					 rd=rd-65536;
				file+="R"+rt+", R"+rs+", #"+rd;
				typeHold = 1; // IM
				break; 
        case 4: file+="BEQ ";
        		rs=Integer.parseInt(instructionParts[1], 2);
        		rt=Integer.parseInt(instructionParts[2], 2);
        		foo=instructionParts[3]+instructionParts[4]+instructionParts[5];
        		foo+="00";
        		rd=Integer.parseInt(foo);
        		file+="R"+rs+", R"+rt+", #"+Integer.parseInt(foo, 2);
        		typeHold = 2; // BR
        		break;
        case 5: file+="BNE ";
				rs=Integer.parseInt(instructionParts[1], 2);
				rt=Integer.parseInt(instructionParts[2], 2);
				foo=instructionParts[3]+instructionParts[4]+instructionParts[5];
				foo+="00";
				rd=Integer.parseInt(foo);
				file+="R"+rs+", R"+rt+", #"+Integer.parseInt(foo, 2);
				typeHold = 2; // BR
				break;		
        case 1: 
        		
				rs=Integer.parseInt(instructionParts[1], 2);
				rt=Integer.parseInt(instructionParts[2], 2);
				foo=instructionParts[3]+instructionParts[4]+instructionParts[5];
				if(rt==1){
					foo+="00";
					file+="BGEZ ";
					file+="R"+rs+", #"+Integer.parseInt(foo, 2);
				}
				if(rt==0){
					foo+="00";
					file+="BLTZ ";
					file+="R"+rs+", #"+Integer.parseInt(foo, 2);
				}
				rd=Integer.parseInt(foo);
				typeHold = 2; // BR
				break;
        case 6: 
    		
			rs=Integer.parseInt(instructionParts[1], 2);
			rt=Integer.parseInt(instructionParts[2], 2);
			foo=instructionParts[3]+instructionParts[4]+instructionParts[5];
			
			foo+="00";
			rd=Integer.parseInt(foo);
			file+="BLEZ ";
			if(foo.startsWith("0"))
				file+="R"+rs+", #"+Integer.parseInt(foo, 2);
			else
				file+="R"+rs+", #"+(Integer.parseInt(foo, 2) - 262144);
			typeHold = 2; // BR
			break;
		
        case 7: 
    		
			rs=Integer.parseInt(instructionParts[1], 2);
			rt=Integer.parseInt(instructionParts[2], 2);
			foo=instructionParts[3]+instructionParts[4]+instructionParts[5];
			
			foo+="00";
			rd=Integer.parseInt(foo);
			file+="BGTZ ";
			file+="R"+rs+", #"+Integer.parseInt(foo, 2);
			typeHold = 2; // BR
			break;
        case 2: file+="J #";
				rs=Integer.parseInt(instructionParts[1], 2);
				rt=Integer.parseInt(instructionParts[2], 2);
				foo=instructionParts[1]+instructionParts[2]+instructionParts[3]+instructionParts[4]+instructionParts[5];
				foo+="00";
				rd=Integer.parseInt(foo);
				file+= Integer.parseInt(foo, 2);
				typeHold = 2; // BR
				break;
        case 43: file+="SW ";
        		rs=Integer.parseInt(instructionParts[1], 2);
        		rt=Integer.parseInt(instructionParts[2], 2);
        		foo=instructionParts[3]+instructionParts[4]+instructionParts[5];
        		rd=Integer.parseInt(foo);
        		file+="R"+rt+", "+ Integer.parseInt(foo, 2)+"(R"+rs+")";
        		typeHold = 3; // LS
        		 break;
	case 35: file+="LW ";
        		rs=Integer.parseInt(instructionParts[1], 2);
        		rt=Integer.parseInt(instructionParts[2], 2);
        		foo=instructionParts[3]+instructionParts[4]+instructionParts[5];
        		rd=Integer.parseInt(foo);
        		file+="R"+rt+", "+ Integer.parseInt(foo, 2)+"(R"+rs+")";
        		typeHold = 3; // LS
        		 break;
       
       
        default: 
        		//System.out.println("unknown op code");
        		typeHold = 5; // ??
        		break;
    }
		
		
		
		if(instructionParts[0].equals("000000")&&instructionParts[5].equals("001101")){
			broken=true;
			typeHold = 4; // N/A
		}
		return broken;
		
	}
	
	
	public  String[] formatWord(byte [] b){
			String bytes=getWord(b);
			
			String[] instructionParts=new String[6];
			instructionParts[0]=bytes.substring(0,6);
			instructionParts[1]=bytes.substring(6,11);
			instructionParts[2]=bytes.substring(11,16);
			instructionParts[3]=bytes.substring(16,21);
			instructionParts[4]=bytes.substring(21,26);
			instructionParts[5]=bytes.substring(26);
			
			//for(int i=0; i<6; i++)
			//	System.out.print(instructionParts[i]+" ");
			//System.out.print("\n");
			
			return instructionParts;
	}
	
	//converts byte array into a string binary
	public  String getWord(byte [] b){
		String bytes="";
		
		for(int i=0;  i<4; i++){
			//System.out.println("byte "+i+" "+b[i]);
			
			int unsigned=unsignedToBytes(b[i]);
			String bin = Integer.toBinaryString(unsigned);
		
		// creates binary string representation of the byte and adds missing zeroes
		
			while(bin.length()<8)
				bin= new StringBuilder(bin).insert(0, "0").toString();
		
			bytes+=bin;
		}
		
		return bytes;
		
	}
	
	public int unsignedToBytes(byte b) {
	    return b & 0xFF;
	  }
	public int unsignedToSigned(int b) {
	    return b;// ^ 0xFFFFFFFF;
	  }

}
