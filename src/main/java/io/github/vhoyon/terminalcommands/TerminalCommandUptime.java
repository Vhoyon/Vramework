package io.github.vhoyon.terminalcommands;

import io.github.vhoyon.abstracts.AbstractTerminalCommand;
import io.github.vhoyon.modules.Logger;
import io.github.vhoyon.modules.Metrics;

public class TerminalCommandUptime extends AbstractTerminalCommand {
	
	@Override
	public String[] getCalls(){
		return new String[]
		{
			"uptime"
		};
	}
	
	@Override
	public void action(){

		long milliseconds = Metrics.getUptime();

		Logger.log("The bot has been up for " + milliseconds + " milliseconds!");
		
	}

}
