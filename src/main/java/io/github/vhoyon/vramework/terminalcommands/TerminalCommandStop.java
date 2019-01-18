package io.github.vhoyon.vramework.terminalcommands;

import io.github.vhoyon.vramework.abstracts.AbstractTerminalCommand;
import io.github.vhoyon.vramework.modules.Logger;

public class TerminalCommandStop extends AbstractTerminalCommand {
	
	@Override
	public String getCall(){
		return "stop";
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
