package io.github.vhoyon.vramework.objects;

import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import io.github.vhoyon.vramework.utilities.KeyBuilder;

public class MessageEventDigger extends EventDigger {
	
	public MessageEventDigger(MessageReceivedEvent event){
		super(event);
	}
	
	public MessageReceivedEvent getEvent(){
		return (MessageReceivedEvent)super.getEvent();
	}
	
	public String getGuildKey(){
		return KeyBuilder.buildGuildKey(getGuild());
	}
	
	public String getChannelKey(){
		return KeyBuilder.buildTextChannelKey(getChannel());
	}
	
	public String getUserKey(){
		return KeyBuilder.buildUserKey(getUser());
	}
	
	public String getCommandKey(String commandName){
		return KeyBuilder.buildCommandKey(getChannel(), commandName);
	}
	
	public String getGuildKey(Object object){
		return KeyBuilder.buildGuildObjectKey(getGuild(), object);
	}
	
	public String getChannelKey(Object object){
		return KeyBuilder.buildTextChannelObjectKey(getChannel(), object);
	}
	
	public String getUserKey(Object object){
		return KeyBuilder.buildUserObjectKey(getUser(), object);
	}
	
	public String getCommandKey(String commandName, Object object){
		return KeyBuilder.buildCommandObjectKey(getChannel(), commandName,
				object);
	}
	
	public Guild getGuild(){
		return this.getEvent().getGuild();
	}
	
	public String getGuildId(){
		return getGuild().getId();
	}
	
	public TextChannel getChannel(){
		return this.getEvent().getTextChannel();
	}
	
	public String getChannelId(){
		return getChannel().getId();
	}
	
	public Member getMember(){
		return this.getEvent().getMember();
	}
	
	public User getUser(){
		return this.getEvent().getAuthor();
	}
	
	public String getUserId(){
		return getUser().getId();
	}
	
	public String getUserName(){
		return getUser().getName();
	}
	
	public SelfUser getRunningBot(){
		return this.getJDA().getSelfUser();
	}
	
}
