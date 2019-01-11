package io.github.vhoyon.vramework.utilities.settings;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import io.github.vhoyon.vramework.objects.Buffer;
import io.github.vhoyon.vramework.utilities.KeyBuilder;

public class SettingRepositoryRepository {
	
	private HashMap<String, SettingRepository> repositories;
	
	private SettingRepositoryRepository(){
		this.repositories = new HashMap<>();
	}
	
	protected HashMap<String, SettingRepository> getRepos(){
		return this.repositories;
	}
	
	public boolean containsKey(String key){
		return this.getRepos().containsKey(key);
	}
	
	@SafeVarargs
	public static SettingRepository getSettingRepository(Guild guild,
			Setting<Object>... settingsIfNew){
		return SettingRepositoryRepository.getSettingRepository(
				KeyBuilder.buildGuildKey(guild), settingsIfNew);
	}
	
	@SafeVarargs
	public static SettingRepository getSettingRepository(TextChannel channel,
			Setting<Object>... settingsIfNew){
		
		String channelKey = KeyBuilder.buildTextChannelKey(channel);
		
		if(!hasSettingRepository(channelKey)){
			
			String guildKey = KeyBuilder.buildGuildKey(channel.getGuild());
			
			if(hasSettingRepository(guildKey)){
				
				SettingRepository guildSettingRepoCopy = getSettingRepository(
						channel.getGuild()).duplicate();
				
				getReposRepo().getRepos().put(channelKey, guildSettingRepoCopy);
				
				return guildSettingRepoCopy;
				
			}
			
		}
		
		return SettingRepositoryRepository.getSettingRepository(channelKey,
				settingsIfNew);
		
	}
	
	@SafeVarargs
	public static SettingRepository getSettingRepository(String repositoryKey,
			Setting<Object>... settingsIfNew){
		
		SettingRepositoryRepository repositoryRepository = getReposRepo();
		
		SettingRepository repository;
		
		if(repositoryRepository.getRepos().containsKey(repositoryKey)){
			repository = repositoryRepository.getRepos().get(repositoryKey);
		}
		else{
			repository = new SettingRepository(settingsIfNew);
			
			repositoryRepository.getRepos().put(repositoryKey, repository);
		}
		
		return repository;
		
	}
	
	public static List<SettingRepository> getReposOfGuildTextChannels(
			Guild guild){
		
		return guild
				.getTextChannels()
				.stream()
				.filter(t -> hasSettingRepository(KeyBuilder
						.buildTextChannelKey(t)))
				.map(SettingRepositoryRepository::getSettingRepository)
				.collect(Collectors.toList());
		
	}
	
	protected static SettingRepositoryRepository getReposRepo(){
		return Buffer.getSingleton(SettingRepositoryRepository.class,
				SettingRepositoryRepository::new);
	}
	
	protected static boolean hasSettingRepository(String repositoryKey){
		
		Buffer buffer = Buffer.get();
		
		if(!Buffer.hasSingleton(SettingRepositoryRepository.class)){
			return false;
		}
		else{
			SettingRepositoryRepository repositoryRepository = Buffer
					.getSingleton(SettingRepositoryRepository.class, null);
			
			if(repositoryRepository == null)
				return false;
			
			return repositoryRepository.getRepos().containsKey(repositoryKey);
		}
		
	}
	
}
