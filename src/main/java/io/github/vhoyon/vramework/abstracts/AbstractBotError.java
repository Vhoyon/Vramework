package io.github.vhoyon.vramework.abstracts;

public abstract class AbstractBotError extends AbstractBotCommand {
	
	private String errorMessage;
	private String emoji;
	private boolean isErrorOneLiner;
	
	public AbstractBotError(AbstractBotCommand commandInError, String errorMessage){
		this(commandInError, errorMessage, EMOJI_RED_CROSS);
	}

	public AbstractBotError(AbstractBotCommand commandInError, String errorMessage, String errorEmoji){
		this(commandInError, errorMessage, errorEmoji, true);
	}

	public AbstractBotError(AbstractBotCommand commandInError, String errorMessage, boolean isErrorOneLiner){
		this(commandInError, errorMessage, EMOJI_RED_CROSS, isErrorOneLiner);
	}

	public AbstractBotError(AbstractBotCommand commandInError, String errorMessage, String errorEmoji,
							boolean isErrorOneLiner){
		
		this.errorMessage = errorMessage;
		this.emoji = errorEmoji;
		this.isErrorOneLiner = isErrorOneLiner;
		
		putStateFromCommand(commandInError);
		
		action();

	}
	
	public AbstractBotError(String errorMessage){
		this(errorMessage, EMOJI_RED_CROSS);
	}
	
	public AbstractBotError(String errorMessage, String errorEmoji){
		this(errorMessage, errorEmoji, true);
	}
	
	public AbstractBotError(String errorMessage, boolean isErrorOneLiner){
		this(errorMessage, EMOJI_RED_CROSS, isErrorOneLiner);
	}
	
	public AbstractBotError(String errorMessage, String errorEmoji,
			boolean isErrorOneLiner){
		
		this.errorMessage = errorMessage;
		this.emoji = errorEmoji;
		this.isErrorOneLiner = isErrorOneLiner;
		
	}
	
	@Override
	public void action(){
		
		String messageToSend = getEmoji() + " " + bold(getMessage());
		
		sendErrorMessage(messageToSend);
		
	}
	
	protected abstract void sendErrorMessage(String messageToSend);
	
	public String getMessage(){
		return this.errorMessage;
	}
	
	public String getEmoji(){
		return this.emoji;
	}
	
	public boolean isErrorOneLiner(){
		return this.isErrorOneLiner;
	}
	
	@Override
	public Object getCalls(){
		return null;
	}
}
