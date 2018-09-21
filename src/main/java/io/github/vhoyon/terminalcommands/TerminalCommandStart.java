package io.github.vhoyon.terminalcommands;

import io.github.vhoyon.abstracts.AbstractTerminalCommand;
import io.github.vhoyon.modules.Logger;

public class TerminalCommandStart extends AbstractTerminalCommand {
	
	@Override
	public String[] getCalls(){
		return new String[]
		{
			"start"
		};
	}
	
	@Override
	public void action(){
		
		try{
			console.onStart();
		}
		catch(Exception e){
			Logger.log(e);
		}
		
	}
	
}
