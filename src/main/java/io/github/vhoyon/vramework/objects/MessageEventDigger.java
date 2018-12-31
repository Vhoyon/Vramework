package io.github.vhoyon.vramework.objects;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import io.github.vhoyon.vramework.utilities.KeyBuilder;

public class MessageEventDigger {
	
	private MessageReceivedEvent event;
	
	public MessageEventDigger(MessageReceivedEvent event){
		this.event = event;
	}
	
	public MessageReceivedEvent getEvent(){
		return this.event;
	}
	
	public JDA getJDA(){
		return this.getEvent().getJDA();
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
		return event.getGuild();
	}
	
	public String getGuildId(){
		return getGuild().getId();
	}
	
	public TextChannel getChannel(){
		return event.getTextChannel();
	}
	
	public String getChannelId(){
		return getChannel().getId();
	}
	
	public Member getMember(){
		return event.getMember();
	}
	
	public User getUser(){
		return event.getAuthor();
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
