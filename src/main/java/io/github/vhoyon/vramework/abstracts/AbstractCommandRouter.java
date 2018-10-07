package io.github.vhoyon.vramework.abstracts;

import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import io.github.vhoyon.vramework.exceptions.NoCommandException;
import io.github.vhoyon.vramework.interfaces.Command;
import io.github.vhoyon.vramework.interfaces.LinkableCommand;
import io.github.vhoyon.vramework.interfaces.Translatable;
import io.github.vhoyon.vramework.interfaces.Utils;
import io.github.vhoyon.vramework.objects.*;
import io.github.vhoyon.vramework.res.FrameworkResources;

public abstract class AbstractCommandRouter extends Thread implements Utils,
		Translatable, FrameworkResources {
	
	private Dictionary dict;
	private Request request;
	private Buffer buffer;
	private CommandsRepository commandsRepo;
	private MessageEventDigger eventDigger;
	protected Command command;
	
	private boolean isDead;
	
	public AbstractCommandRouter(MessageReceivedEvent event,
			String receivedMessage, Buffer buffer,
			CommandsRepository commandsRepo){
		
		this.isDead = false;
		
		this.buffer = buffer;
		
		eventDigger = new MessageEventDigger(event);
		
		try{
			
			Object bufferedDict = buffer.get(BUFFER_DICTIONARY,
					eventDigger.getGuildKey());
			setDictionary((Dictionary)bufferedDict);
			
		}
		catch(NullPointerException e){
			
			setDictionary(new Dictionary());
			
			try{
				buffer.push(getDictionary(), BUFFER_DICTIONARY,
						eventDigger.getGuildKey());
			}
			catch(NullPointerException e1){}
			
		}
		
		commandsRepo.setDictionary(getDictionary());
		
		this.commandsRepo = commandsRepo;
		
		this.request = createRequest(receivedMessage);
		
	}
	
	protected abstract Request createRequest(String receivedMessage);
	
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
		
		MessageReceivedEvent event = getEvent();
		
		if(event.isFromType(ChannelType.PRIVATE)){
			
			return commandWhenFromPrivate();
			
		}
		else if(event.isFromType(ChannelType.TEXT)){
			
			Request request = getRequest();
			String commandPrefix = getCommandPrefix();
			
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
	
	public abstract Command commandWhenFromPrivate();
	
	public abstract Command commandWhenFromServerIsOnlyPrefix();
	
	public abstract String getCommandPrefix();
	
	public abstract char getCommandParameterPrefix();
	
	public LinkableCommand getLinkableCommand(String commandName){
		return this.getCommandsRepo().getContainer().initiateLink(commandName);
	}

	@Override
	public void interrupt() {
		super.interrupt();
		
		this.isDead = true;
	}
	
	public boolean isDead(){
		return this.isDead;
	}
	
}
