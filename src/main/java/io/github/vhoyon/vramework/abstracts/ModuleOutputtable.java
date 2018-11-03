package io.github.vhoyon.vramework.abstracts;

import io.github.vhoyon.vramework.interfaces.Outputtable;

import java.util.ArrayList;
import java.util.function.Consumer;

public abstract class ModuleOutputtable extends Module {
	
	protected static <E extends Outputtable> boolean handleMessageAndWarning(
			String message, ArrayList<E> outputs, boolean hasIssuedWarning,
			Consumer<E> onOutputs){
		
		boolean isOutputLess = outputs == null || outputs.isEmpty();
		
		if(isOutputLess && !hasIssuedWarning){
			hasIssuedWarning = true;
			
			System.err
					.println("[OUTPUT WARNING] There is no outputs attached and a call to send data has been sent - using the System's output by default. This warning will only be shown once.\n");
		}
		
		if(isOutputLess){
			System.out.println(message);
		}
		else{
			for(E output : outputs){
				onOutputs.accept(output);
			}
		}
		
		return hasIssuedWarning;
		
	}
	
}