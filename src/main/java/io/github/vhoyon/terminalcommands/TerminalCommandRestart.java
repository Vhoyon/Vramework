package io.github.vhoyon.terminalcommands;

import io.github.vhoyon.abstracts.AbstractTerminalCommand;
import io.github.vhoyon.modules.Logger;

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
