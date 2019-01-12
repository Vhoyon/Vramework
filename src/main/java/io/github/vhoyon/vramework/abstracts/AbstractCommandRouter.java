package io.github.vhoyon.vramework.abstracts;

import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import io.github.vhoyon.vramework.exceptions.NoCommandException;
import io.github.vhoyon.vramework.interfaces.*;
import io.github.vhoyon.vramework.interfaces.Translatable;
import io.github.vhoyon.vramework.objects.*;
import io.github.vhoyon.vramework.res.FrameworkResources;
import io.github.vhoyon.vramework.utilities.settings.Setting;
import io.github.vhoyon.vramework.utilities.settings.SettingRepository;
import io.github.vhoyon.vramework.utilities.settings.SettingRepositoryRepository;

public abstract class AbstractCommandRouter extends Thread implements Utils,
		DiscordUtils, Translatable, FrameworkResources {
	
	private Dictionary dict;
	private Request request;
	private Buffer buffer;
	private CommandsRepository commandsRepo;
	private MessageEventDigger eventDigger;
	protected Command command;
	
	private boolean isDead;
	
	public AbstractCommandRouter(MessageReceivedEvent event, Buffer buffer,
			CommandsRepository commandsRepo){
		
		this.isDead = false;
		
		this.buffer = buffer;
		
		eventDigger = new MessageEventDigger(event);
		
		try{
			
			Object bufferedDict = buffer.get(eventDigger
					.getGuildKey(BUFFER_DICTIONARY));
			setDictionary((Dictionary)bufferedDict);
			
		}
		catch(IllegalStateException e){
			
			setDictionary(new Dictionary());
			
			try{
				buffer.push(getDictionary(),
						eventDigger.getGuildKey(BUFFER_DICTIONARY));
			}
			catch(NullPointerException e1){}
			
		}
		
		commandsRepo.setDictionary(getDictionary());
		
		this.commandsRepo = commandsRepo;
		
		this.request = createRequest(eventDigger.getMessageContent());
		
	}
	
	protected abstract Request createRequest(String receivedMessage);
	
	public abstract void route();
	
	@Override
	public void run(){
		this.route();
	}
	
	public Command getCommand(){
		return this.command;
	}
	
	public void setCommand(Command command){
		this.command = command;
	}
	
	public CommandsRepository getCommandsRepo(){
		return this.commandsRepo;
	}
	
	public MessageEventDigger getEventDigger(){
		return this.eventDigger;
	}
	
	public MessageReceivedEvent getEvent(){
		return getEventDigger().getEvent();
	}
	
	public Buffer getBuffer(){
		return this.buffer;
	}
	
	public Request getRequest(){
		return this.request;
	}
	
	@Override
	public Dictionary getDictionary(){
		return this.dict;
	}
	
	@Override
	public void setDictionary(Dictionary dict){
		this.dict = dict;
	}
	
	protected AbstractBotCommand getAbstractBotCommand(){
		
		AbstractBotCommand botCommand = (AbstractBotCommand)getCommand();
		
		if(botCommand != null){
			
			botCommand.setRouter(this);
			botCommand.setDictionary(getDictionary());
			
		}
		
		return botCommand;
		
	}
	
	/**
	 * Method that validates the message received and return the command to
	 * execute if it is not validated. In the case where the message received
	 * isn't a command (a message that starts with
	 * <i>Ressources.<b>PREFIX</b></i>), a <i>NoCommandException</i> is thrown.
	 * <p>
	 * If the message received is from a Private Channel, the
	 * {@link #commandWhenFromPrivate()} method is called and returns its value.
	 * </p>
	 * <p>
	 * If the message is from a Text Channel but is only the text of the
	 * {@link #getCommandPrefix()}, the value of the
	 * {@link #commandWhenFromServerIsOnlyPrefix()} is returned.
	 * </p>
	 * 
	 * @return The content of {@link #commandIfValidated()} if valid; a command
	 *         to execute otherwise.
	 * @throws NoCommandException
	 *             Generic exception thrown if the message isn't a command.
	 */
	protected Command validateMessage() throws NoCommandException{
		
		boolean isOnlyBotMention = false;
		
		String message = this.getEventDigger().getMessageContent();
		
		if(isStringMention(message)){
			
			String botId = this.getEventDigger().getRunningBot().getId();
			
			isOnlyBotMention = this.getEvent().getMessage().getMentionedUsers()
					.get(0).getId().equals(botId);
			
		}
		
		if(isOnlyBotMention){
			return commandWhenBotMention();
		}
		else if(this.getEvent().isFromType(ChannelType.PRIVATE)){
			return commandWhenFromPrivate();
		}
		else if(this.getEvent().isFromType(ChannelType.TEXT)){
			
			Request request = this.getRequest();
			
			if(!request.isCommand()){
				throw new NoCommandException();
			}
			else{
				
				if(request.isOnlyCommandPrefix()){
					return commandWhenFromServerIsOnlyPrefix();
				}
				
			}
			
		}
		
		return commandIfValidated();
		
	}
	
	/**
	 * @return {@code null} by default, can be overridden to return another
	 *         value for {@link #validateMessage()}.
	 */
	protected Command commandIfValidated(){
		return null;
	}
	
	public abstract Command commandWhenBotMention();
	
	public abstract Command commandWhenFromPrivate();
	
	public abstract Command commandWhenFromServerIsOnlyPrefix();
	
	public String getCommandPrefix(){
		return Request.DEFAULT_COMMAND_PREFIX;
	}
	
	public char getCommandParameterPrefix(){
		return Request.DEFAULT_PARAMETER_PREFIX;
	}
	
	public LinkableCommand getLinkableCommand(String commandName){
		return this.getCommandsRepo().getContainer().initiateLink(commandName);
	}
	
	@Override
	public void interrupt(){
		super.interrupt();
		
		this.isDead = true;
	}
	
	public boolean isDead(){
		return this.isDead;
	}
	
	/**
	 * Gets the
	 * {@link io.github.vhoyon.vramework.utilities.settings.SettingRepository}
	 * object from the Buffer for the TextChannel of this Router or create it if
	 * there is currently none in the
	 * {@link io.github.vhoyon.vramework.utilities.settings.SettingRepositoryRepository}
	 * Buffer.
	 *
	 * @return The
	 *         {@link io.github.vhoyon.vramework.utilities.settings.SettingRepository}
	 *         object from the current TextChannel-associated buffer.
	 * @since 0.14.0
	 */
	public SettingRepository getSettings(){
		return getSettings(BufferLevel.CHANNEL);
	}
	
	/**
	 * Gets the
	 * {@link io.github.vhoyon.vramework.utilities.settings.SettingRepository}
	 * object from the Buffer for the TextChannel or Guild, depending on the
	 * level provided by the {@code level} parameter, of this Router or create
	 * it if there is currently none in the
	 * {@link io.github.vhoyon.vramework.utilities.settings.SettingRepositoryRepository}
	 * Buffer.
	 *
	 * @param level
	 *            The level at which the SettingRepository must be taken from.
	 * @return The
	 *         {@link io.github.vhoyon.vramework.utilities.settings.SettingRepository}
	 *         object from the associated buffer.
	 * @since 0.14.0
	 */
	public SettingRepository getSettings(BufferLevel level){
		
		if(level == null)
			level = AbstractBotCommand.DEFAULT_BUFFER_LEVEL;
		
		SettingRepository repo;
		
		Setting<Object>[] defaultSettings = this.getDefaultSettings();
		
		switch(level){
		default:
		case USER:
		case CHANNEL:
			repo = SettingRepositoryRepository.getSettingRepository(
					getEventDigger().getChannel(), defaultSettings);
			break;
		case GUILD:
			repo = SettingRepositoryRepository.getSettingRepository(
					getEventDigger().getGuild(), defaultSettings);
			break;
		}
		
		return repo;
		
	}
	
	/**
	 * @return The initial settings when using the methods
	 *         {@link #getSettings()} and {@link #getSettings(BufferLevel)}.
	 */
	protected abstract Setting<Object>[] getDefaultSettings();
	
}
