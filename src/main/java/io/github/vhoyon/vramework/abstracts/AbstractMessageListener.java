package io.github.vhoyon.vramework.abstracts;

import net.dv8tion.jda.core.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import io.github.vhoyon.vramework.objects.Buffer;
import io.github.vhoyon.vramework.objects.CommandsRepository;

public abstract class AbstractMessageListener extends ListenerAdapter {
	
	private Buffer buffer;
	private CommandsRepository commandsRepo;
	
	public AbstractMessageListener(){
		buffer = Buffer.get();
		
		commandsRepo = new CommandsRepository(createCommandLinker());
	}
	
	protected abstract CommandsLinker createCommandLinker();
	
	protected abstract AbstractCommandRouter createRouter(
			MessageReceivedEvent event, String receivedMessage, Buffer buffer,
			CommandsRepository commandsRepo);
	
	@Override
	public void onMessageReceived(MessageReceivedEvent event){
		
		String receivedMessage = event.getMessage().getContentRaw();
		
		// Bots doesn't need attention...
		if(!event.getAuthor().isBot()){
			
			createRouter(event, receivedMessage, buffer, commandsRepo).start();
			
		}
		
	}
	
	@Override
	public void onGuildVoiceLeave(GuildVoiceLeaveEvent event){
		
		//		// No events from bots
		//		if(!event.getMember().getUser().isBot()){
		//			
		//			try{
		//				
		//				VoiceChannel playerVoiceChannel = (VoiceChannel)buffer.get(
		//						BUFFER_VOICE_CHANNEL, event.getGuild().getId());
		//				
		//				System.out.println(playerVoiceChannel);
		//				
		//				if(playerVoiceChannel.equals(event.getChannelLeft())){
		//					System.out.println("test leaves from same lel");
		//				}
		//				else{
		//					System.out.println("fuk");
		//				}
		//				
		//			}
		//			catch(NullPointerException e){
		//				System.out.println("hehe");
		//			}
		//			
		//		}
		
	}
	
	@Override
	public void onGuildVoiceMove(GuildVoiceMoveEvent event){
		//		event.getChannelLeft();
		//		
		//		System.out.println("test moves between channels");
	}
	
}
