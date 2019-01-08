package io.github.vhoyon.vramework.terminalcommands;

import io.github.vhoyon.vramework.abstracts.AbstractTerminalCommand;
import io.github.vhoyon.vramework.modules.Logger;
import io.github.vhoyon.vramework.modules.Metrics;

public class TerminalCommandUptime extends AbstractTerminalCommand {
	
	@Override
	public String getCall(){
		return "uptime";
	}
	
	@Override
	public void action(){
		
		long milliseconds = Metrics.getUptime();
		
		Logger.log("The bot has been up for " + milliseconds + " milliseconds!");
		
	}
	
}
