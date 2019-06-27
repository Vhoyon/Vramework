package io.github.vhoyon.vramework.utilities;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import io.github.vhoyon.vramework.abstracts.AbstractMessageListener;
import io.github.vhoyon.vramework.interfaces.Console;
import io.github.vhoyon.vramework.modules.Logger;
import io.github.vhoyon.vramework.modules.Metrics;
import io.github.vhoyon.vramework.modules.Logger.LogType;
import io.github.vhoyon.vramework.objects.Buffer;

public class VrameworkTemplate {
	
	public static JDA jda;
	public static String botToken;
	
	public static void startBot(Console console,
			AbstractMessageListener messageListener) throws Exception{
		
		boolean success = false;
		
		do{
			
			Logger.log("Starting the bot...", LogType.INFO);
			
			try{
				
				jda = new JDABuilder(AccountType.BOT).setToken(botToken)
						.build().awaitReady();
				jda.addEventListener(messageListener);
				jda.setAutoReconnect(true);
				
				Metrics.startClock();
				Metrics.setJDA(jda);
				
				Logger.log("Bot started!", LogType.INFO);
				
				success = true;
				
			}
			catch(LoginException e){
				
				botToken = console
						.getInput("The bot token provided is invalid. Please enter a valid token here :");
				
				if(botToken == null || botToken.length() == 0)
					throw e;
				
				Logger.log("Application's Bot Token has been set to : "
						+ botToken, LogType.INFO);
				
			}
			
		}while(!success);
		
	}
	
	public static void stopBot(Console console){
		
		if(jda != null){
			
			Logger.log("Shutting down the bot...", LogType.INFO);
			
			boolean canStopBot = true;
			
			if(CommandsThreadManager.hasRunningCommands()){
				
				int conf = console
						.getConfirmation(
								"There are running commands, are you sure you want to stop the bot?",
								Console.QuestionType.YES_NO);
				
				if(conf == Console.NO){
					canStopBot = false;
				}
				else{
					
					int numberOfStoppedCommands = CommandsThreadManager
							.stopAllCommands();
					
					Logger.log("Stopped " + numberOfStoppedCommands
							+ " running commands before stopping the bot.",
							LogType.INFO);
					
				}
				
			}
			
			if(!canStopBot){
				
				Logger.log("Bot not stopped.", LogType.INFO);
				
			}
			else{
				
				jda.shutdownNow();
				
				jda = null;
				
				Buffer.get().emptyAllMemory();
				
				Logger.log("Bot has been shutdown!", LogType.INFO);
				
				Metrics.stopClock();
				Metrics.setJDA(null);
				
			}
			
		}
		else{
			Logger.log("The JDA was already closed, no action was taken.",
					LogType.INFO);
		}
		
	}
	
}
