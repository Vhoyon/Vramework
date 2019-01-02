package io.github.vhoyon.vramework.utilities.settings;

import java.util.HashMap;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import io.github.vhoyon.vramework.objects.Buffer;
import io.github.vhoyon.vramework.utilities.KeyBuilder;

public class SettingRepositoryRepository {
	
	public final static String SETTINGS_BUFFER_KEY = "VRAMEWORK_SETTINGS_REPOSITORIES";
	
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
		
		if(!hasSettingRepository(SETTINGS_BUFFER_KEY, channelKey)){
			
			String guildKey = KeyBuilder.buildGuildKey(channel.getGuild());
			
			if(hasSettingRepository(SETTINGS_BUFFER_KEY, guildKey)){
				
				SettingRepository guildSettingRepoCopy = getSettingRepository(
						channel.getGuild()).duplicate();
				
				getReposRepo(SETTINGS_BUFFER_KEY).getRepos().put(channelKey,
						guildSettingRepoCopy);
				
				return guildSettingRepoCopy;
				
			}
			
		}
		
		return SettingRepositoryRepository.getSettingRepository(channelKey,
				settingsIfNew);
		
	}
	
	@SafeVarargs
	public static SettingRepository getSettingRepository(String repositoryKey,
			Setting<Object>... settingsIfNew){
		return SettingRepositoryRepository.getSettingRepository(
				SETTINGS_BUFFER_KEY, repositoryKey, settingsIfNew);
	}
	
	@SafeVarargs
	public static SettingRepository getSettingRepository(
			String settingRepositoryRepositoryKey, String repositoryKey,
			Setting<Object>... settingsIfNew){
		
		SettingRepositoryRepository repositoryRepository = getReposRepo(settingRepositoryRepositoryKey);
		
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
	
	protected static SettingRepositoryRepository getReposRepo(
			String settingRepositoryRepositoryKey){
		
		Buffer buffer = Buffer.get();
		
		SettingRepositoryRepository repositoryRepository;
		
		if(buffer.has(settingRepositoryRepositoryKey)){
			repositoryRepository = (SettingRepositoryRepository)buffer
					.get(settingRepositoryRepositoryKey);
		}
		else{
			repositoryRepository = new SettingRepositoryRepository();
			
			buffer.push(repositoryRepository, settingRepositoryRepositoryKey);
		}
		
		return repositoryRepository;
		
	}
	
	protected static boolean hasSettingRepository(
			String settingRepositoryRepositoryKey, String repositoryKey){
		
		Buffer buffer = Buffer.get();
		
		if(!buffer.has(settingRepositoryRepositoryKey)){
			return false;
		}
		else{
			SettingRepositoryRepository repositoryRepository = (SettingRepositoryRepository)buffer
					.get(settingRepositoryRepositoryKey);
			
			return repositoryRepository.getRepos().containsKey(repositoryKey);
		}
		
	}
	
}
