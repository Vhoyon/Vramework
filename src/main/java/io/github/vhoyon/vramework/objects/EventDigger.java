package io.github.vhoyon.vramework.objects;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.Event;

public class EventDigger {
	
	private Event event;
	
	public EventDigger(Event event){
		this.event = event;
	}
	
	public Event getEvent(){
		return this.event;
	}
	
	public JDA getJDA(){
		return this.getEvent().getJDA();
	}
	
	public static boolean isUserBot(Member member){
		return EventDigger.isUserBot(member.getUser());
	}
	
	public static boolean isUserBot(User user){
		return user.isBot() || user.isFake();
	}
	
	public static boolean doesConnectedChannelHasHumansLeft(Guild guild){
		
		VoiceChannel connectedChannel = guild.getAudioManager()
				.getConnectedChannel();
		
		if(connectedChannel == null){
			return false;
		}
		else{
			
			return connectedChannel.getMembers().stream()
					.anyMatch(member -> !EventDigger.isUserBot(member));
			
		}
		
	}
	
}
