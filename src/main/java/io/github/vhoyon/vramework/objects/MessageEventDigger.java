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
	
	public String getMessageContent(){
		return this.getEvent().getMessage().getContentRaw();
	}
	
	public String getGuildKey(){
		return KeyBuilder.buildGuildKey(this.getGuild());
	}
	
	public String getChannelKey(){
		return KeyBuilder.buildTextChannelKey(this.getChannel());
	}
	
	public String getUserKey(){
		return KeyBuilder.buildUserKey(this.getUser());
	}
	
	public String getCommandKey(String commandName){
		return KeyBuilder.buildCommandKey(this.getChannel(), commandName);
	}
	
	public String getGuildKey(Object object){
		return KeyBuilder.buildGuildObjectKey(this.getGuild(), object);
	}
	
	public String getChannelKey(Object object){
		return KeyBuilder.buildTextChannelObjectKey(this.getChannel(), object);
	}
	
	public String getUserKey(Object object){
		return KeyBuilder.buildUserObjectKey(this.getUser(), object);
	}
	
	public String getCommandKey(String commandName, Object object){
		return KeyBuilder.buildCommandObjectKey(this.getChannel(), commandName,
				object);
	}
	
	public Guild getGuild(){
		return this.getEvent().getGuild();
	}
	
	public String getGuildId(){
		return this.getGuild().getId();
	}
	
	public TextChannel getChannel(){
		return this.getEvent().getTextChannel();
	}
	
	public String getChannelId(){
		return this.getChannel().getId();
	}
	
	public Member getMember(){
		return this.getEvent().getMember();
	}
	
	public User getUser(){
		return this.getEvent().getAuthor();
	}
	
	public String getUserId(){
		return this.getUser().getId();
	}
	
	public String getUserName(){
		return this.getUser().getName();
	}
	
	public SelfUser getRunningBot(){
		return this.getJDA().getSelfUser();
	}
	
}
