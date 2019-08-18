package io.github.vhoyon.vramework.abstracts;

import io.github.vhoyon.vramework.interfaces.Emoji;

public abstract class AbstractBotError extends AbstractBotCommand {
	
	private String errorMessage;
	private Emoji emoji;
	private boolean isErrorOneLiner;
	
	public AbstractBotError(AbstractBotCommand commandInError,
			String errorMessage){
		this(commandInError, errorMessage, Emoji.RED_CROSS);
	}
	
	public AbstractBotError(AbstractBotCommand commandInError,
			String errorMessage, Emoji errorEmoji){
		this(commandInError, errorMessage, errorEmoji, true);
	}
	
	public AbstractBotError(AbstractBotCommand commandInError,
			String errorMessage, boolean isErrorOneLiner){
		this(commandInError, errorMessage, Emoji.RED_CROSS, isErrorOneLiner);
	}
	
	public AbstractBotError(AbstractBotCommand commandInError,
			String errorMessage, Emoji errorEmoji, boolean isErrorOneLiner){
		
		this.errorMessage = errorMessage;
		this.emoji = errorEmoji;
		this.isErrorOneLiner = isErrorOneLiner;
		
		this.putStateFromCommand(commandInError);
		
		this.action();
		
	}
	
	public AbstractBotError(String errorMessage){
		this(errorMessage, Emoji.RED_CROSS);
	}
	
	public AbstractBotError(String errorMessage, Emoji errorEmoji){
		this(errorMessage, errorEmoji, true);
	}
	
	public AbstractBotError(String errorMessage, boolean isErrorOneLiner){
		this(errorMessage, Emoji.RED_CROSS, isErrorOneLiner);
	}
	
	public AbstractBotError(String errorMessage, Emoji errorEmoji,
			boolean isErrorOneLiner){
		
		this.errorMessage = errorMessage;
		this.emoji = errorEmoji;
		this.isErrorOneLiner = isErrorOneLiner;
		
	}
	
	@Override
	public void actions(){
		
		String messageToSend = getEmoji() + " " + bold(getMessage());
		
		sendErrorMessage(messageToSend);
		
	}
	
	protected abstract void sendErrorMessage(String messageToSend);
	
	public String getMessage(){
		return this.errorMessage;
	}
	
	public Emoji getEmoji(){
		return this.emoji;
	}
	
	public boolean isErrorOneLiner(){
		return this.isErrorOneLiner;
	}
	
	@Override
	public String getCall(){
		return null;
	}
	
}
