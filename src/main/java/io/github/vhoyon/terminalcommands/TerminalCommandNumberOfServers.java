package io.github.vhoyon.terminalcommands;

import io.github.vhoyon.abstracts.AbstractTerminalCommand;
import io.github.vhoyon.exceptions.JDANotSetException;
import io.github.vhoyon.modules.Logger;
import io.github.vhoyon.modules.Metrics;

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
