
import java.io.IOException;

import java.io.PrintWriter;

public class Disassembler {

	
	public static void main(String[] args) {
		if (args.length>1 && args[0].equals("dis") && !args[1].equals(null))
			try {
				helper p=new helper();
				PrintWriter writer = new PrintWriter("Output.txt", "UTF-8");
				writer.print(p.disassemble(args[1]));
				writer.close();
				
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		else 
			System.out.println("invalid args");
		

	}
	
	
	
	
	

}
