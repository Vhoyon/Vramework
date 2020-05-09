package io.github.vhoyon.vramework.abstracts;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
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
			MessageReceivedEvent event, Buffer buffer,
			CommandsRepository commandsRepo);
	
	@Override
	public void onMessageReceived(MessageReceivedEvent event){
		
		// Bots doesn't need attention...
		if(!event.getAuthor().isBot()){
			
			this.createRouter(event, buffer, commandsRepo).start();
			
		}
		
	}
	
}
