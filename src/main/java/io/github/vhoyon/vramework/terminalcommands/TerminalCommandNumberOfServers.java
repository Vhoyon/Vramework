package io.github.vhoyon.vramework.terminalcommands;

import io.github.vhoyon.vramework.abstracts.AbstractTerminalCommand;
import io.github.vhoyon.vramework.exceptions.JDANotSetException;
import io.github.vhoyon.vramework.modules.Logger;
import io.github.vhoyon.vramework.modules.Metrics;

public class TerminalCommandNumberOfServers extends AbstractTerminalCommand {
	
	@Override
	public String[] getCalls(){
		return new String[]
		{
			"connections"
		};
	}
	
	@Override
	public void action(){
		
		try{
			
			int numberOfJoinedServers = Metrics.getNumberOfJoinedServers();
			
			Logger.log("The bot has joined " + numberOfJoinedServers
					+ " server(s) so far!");
			
		}
		catch(JDANotSetException e){
			Logger.log("Start the bot to get his details!");
		}
		
	}
	
}
