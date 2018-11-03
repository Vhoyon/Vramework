package io.github.vhoyon.vramework.terminalcommands;

import io.github.vhoyon.vramework.abstracts.AbstractTerminalCommand;
import io.github.vhoyon.vramework.modules.Logger;

public class TerminalCommandRestart extends AbstractTerminalCommand {
	
	@Override
	public String[] getCalls(){
		return new String[]
		{
			"restart"
		};
	}
	
	@Override
	public void action(){
		
		try{
			console.onStop();
			
			console.onStart();
		}
		catch(Exception e){
			Logger.log(e);
		}
		
	}
	
}
