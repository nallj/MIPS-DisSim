public class instr{
	//public enum instrType{FP, LD, SD, NA}
	
	int addr;
	String op;
	int type;					// 0 = FP, 1 = IM, 2 = BR, 3 = LS, 4 = N/A, 5 = ??
	//boolean r1, r2;			// are second and third field registers?  if not, they're immediate value
	long f1, f2, f3;			// value of fields
	//String b0, b1, b2, b3;	// bitfields as strings
	String friendRep;	// easy to read representation from 'helper.disassembleInstruction'
	
	public instr(){}
	public instr(int a){ addr = a; }
	
	public boolean isEmpty(){ return op.isEmpty(); }
	
	// getters
	public String getOp(){ return op; }
	public int getType(){ return type; }
	public String getFriendRep() { return friendRep; }
	public void printInstr(){ System.out.println(friendRep); }
	
	// setters
	public void setOp(String o){ op = o; }
	public void setType(int t){ type = t; }
	public void setFriendRep(String f){ friendRep = f; }
	public void setFields(long p1, long p2, long p3){ f1 = p1; f2 = p2; f3 = p3; }
}
