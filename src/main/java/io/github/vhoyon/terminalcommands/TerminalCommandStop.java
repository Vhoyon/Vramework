package io.github.vhoyon.terminalcommands;

import io.github.vhoyon.abstracts.AbstractTerminalCommand;
import io.github.vhoyon.modules.Logger;

public class TerminalCommandStop extends AbstractTerminalCommand {
	
	@Override
	public String[] getCalls(){
		return new String[]
		{
			"stop"
		};
	}
	
	@Override
	public void action(){
		
		try{
			console.onStop();
		}
		catch(Exception e){
			Logger.log(e);
		}
		
	}
	
}
