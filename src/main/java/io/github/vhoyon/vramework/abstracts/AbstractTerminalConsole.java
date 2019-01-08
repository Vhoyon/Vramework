package io.github.vhoyon.vramework.abstracts;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

import io.github.vhoyon.vramework.interfaces.Console;
import io.github.vhoyon.vramework.interfaces.Loggable;
import io.github.vhoyon.vramework.modules.Logger;
import io.github.vhoyon.vramework.modules.Logger.LogType;
import io.github.vhoyon.vramework.objects.CommandsRepository;
import io.github.vhoyon.vramework.objects.TerminalCommandsLinker;
import io.github.vhoyon.vramework.utils.UpdatableOutputStream;

public abstract class AbstractTerminalConsole implements Console, Loggable {
	
	protected BufferedReader reader;
	
	private CommandsRepository commandsRepo;
	
	private String inputPrefix;
	
	private UpdatableOutputStream outputStream;
	private UpdatableOutputStream errorStream;
	
	public AbstractTerminalConsole(){
		reader = new BufferedReader(new InputStreamReader(System.in));
		
		this.setInputPrefix(">");
		
		this.outputStream = new UpdatableOutputStream(System.out){
			@Override
			public String formatLatestInputMessage(String latestMessage){
				return AbstractTerminalConsole.this
						.formatInputMessage(latestMessage);
			}
			
			@Override
			public String getUpdatingString(){
				return "---";
			}
		};
		this.errorStream = new UpdatableOutputStream(System.err){
			@Override
			public String formatLatestInputMessage(String latestMessage){
				return AbstractTerminalConsole.this
						.formatInputMessage(latestMessage);
			}
			
			@Override
			public String getUpdatingString(){
				return "!---!";
			}
		};
	}
	
	public CommandsRepository getCommandsRepo(){
		if(commandsRepo == null){
			this.setCommandsRepo(new CommandsRepository(
					new TerminalCommandsLinker()));
		}
		
		return commandsRepo;
	}
	
	public void setCommandsRepo(CommandsRepository repository){
		this.commandsRepo = repository;
	}
	
	public void setInputPrefix(String inputPrefix){
		this.inputPrefix = inputPrefix;
	}
	
	public String getInputPrefix(){
		return this.inputPrefix;
	}
	
	protected void setIsWaitingForInput(boolean isWaitingForInput){
		this.outputStream.setIsWaitingForInput(isWaitingForInput);
		this.errorStream.setIsWaitingForInput(isWaitingForInput);
	}
	
	public boolean isWaitingForInput(){
		return this.outputStream.isWaitingForInput();
	}
	
	protected void setIsLogging(boolean isLogging){
		this.outputStream.setIsPrinting(isLogging);
		this.errorStream.setIsPrinting(isLogging);
	}
	
	public boolean isLogging(){
		return this.outputStream.isPrinting();
	}
	
	@Override
	public void log(String logText, String logType, boolean hasAppendedDate){
		logToChannel(logText, logType, true);
	}
	
	/**
	 * @deprecated
	 * @param log
	 *            Log message.
	 */
	protected void sendLog(String log){
		this.sendLog(log, true);
	}
	
	/**
	 * @deprecated
	 * @param log
	 *            Log message.
	 * @param appendNewLine
	 *            If the output should add a new line after the text.
	 */
	protected void sendLog(String log, boolean appendNewLine){
		logToChannel(log, null, appendNewLine);
	}
	
	private void logToChannel(String logText, String logType,
			boolean appendNewLine){
		
		PrintStream stream;
		
		if(LogType.ERROR.toString().equalsIgnoreCase(logType))
			stream = System.err;
		else
			stream = System.out;
		
		if(appendNewLine)
			stream.println(logText);
		else
			stream.print(logText);
		
	}
	
	protected boolean handleInput(String input){
		
		this.setIsWaitingForInput(false);
		
		if(input == null)
			return false;
		
		if(input.length() == 0){
			Logger.log("The input cannot be empty!", LogType.ERROR);
			
			return false;
		}
		
		AbstractTerminalCommand command = (AbstractTerminalCommand)getCommandsRepo()
				.getContainer().initiateLink(input);
		
		command.setConsole(this);
		
		command.action();
		
		return command.doesStopTerminal();
		
	}
	
	protected void printGetInputMessage(){
		this.printGetInputMessage(this.getInputPrefix());
	}
	
	protected void printGetInputMessage(String message){
		sendLog(formatInputMessage(message), false);
	}
	
	protected String formatInputMessage(String message){
		return "\n" + message + " ";
	}
	
	public String getInput(){
		return this.getInput(this.getInputPrefix());
	}
	
	@Override
	public String getInput(String message){
		
		printGetInputMessage(message);
		this.outputStream.setLatestInputMessage(message);
		this.errorStream.setLatestInputMessage(message);
		
		this.setIsWaitingForInput(true);
		
		try{
			String userInput = reader.readLine();
			
			this.setIsWaitingForInput(false);
			
			return userInput;
		}
		catch(IOException e){
			Logger.log(e);
			
			return null;
		}
		
	}
	
	@Override
	public int getConfirmation(String question, QuestionType questionType){
		
		this.setIsLogging(true);
		
		String[] choices = null;
		
		switch(questionType){
		case YES_NO:
			choices = new String[]
			{
				"y", "n"
			};
			break;
		case YES_NO_CANCEL:
			choices = new String[]
			{
				"y", "n", "c"
			};
			break;
		case OK:
			choices = new String[]
			{
				"press enter to continue"
			};
			break;
		}
		
		String choiceSeparator = " / ";
		
		StringBuilder choiceBuilder = new StringBuilder();
		
		choiceBuilder.append("(");
		
		for(String possibility : choices){
			choiceBuilder.append(possibility).append(choiceSeparator);
		}
		
		choiceBuilder.delete(choiceBuilder.length() - choiceSeparator.length(),
				choiceBuilder.length());
		
		choiceBuilder.append(")");
		
		boolean isChoiceValid = false;
		
		String formattedInput = null;
		
		do{
			
			String input = getInput(question + " " + choiceBuilder.toString())
					.trim();
			
			System.out.println();
			
			if(questionType == QuestionType.OK){
				isChoiceValid = true;
				formattedInput = "o";
			}
			else if(input.length() == 0){
				Logger.log("The choice cannot be empty!", LogType.ERROR);
			}
			else{
				
				formattedInput = input.substring(0, 1).toLowerCase();
				
				for(int i = 0; i < choices.length && !isChoiceValid; i++){
					if(choices[i].equals(formattedInput))
						isChoiceValid = true;
				}
				
			}
			
		}while(!isChoiceValid);
		
		switch(formattedInput){
		case "n":
			return NO;
		case "y":
			return YES;
		case "c":
			return CANCEL;
		case "o":
			return OK;
		default:
			return -1;
		}
		
	}
	
	@Override
	public void onStart() throws Exception{}
	
	@Override
	public void onStop() throws Exception{}
	
	@Override
	public void onInitialized(){}
	
	@Override
	public void onExit(){
		this.outputStream.resetStream();
		this.errorStream.resetStream();
	}
	
}
