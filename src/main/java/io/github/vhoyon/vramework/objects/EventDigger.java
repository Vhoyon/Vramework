package io.github.vhoyon.vramework.objects;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.Event;

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
	
	public static boolean doesConnectedChannelHasHumansLeft(Guild guild){
		
		VoiceChannel connectedChannel = guild.getAudioManager()
				.getConnectedChannel();
		
		if(connectedChannel == null){
			return false;
		}
		else{
			
			return connectedChannel
					.getMembers()
					.stream()
					.anyMatch(
							member -> !(member.getUser().isBot() || member
									.getUser().isFake()));
			
		}
		
	}
	
}
