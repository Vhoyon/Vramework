package io.github.vhoyon.vramework.util;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import io.github.vhoyon.vramework.abstracts.AbstractBotCommand;
import io.github.vhoyon.vramework.interfaces.Utils;

public class KeyBuilder {
	
	protected KeyBuilder(){}
	
	public static String buildGuildKey(Guild guild){
		return Utils.buildKey(guild.getId());
	}
	
	public static String buildGuildObjectKey(Guild guild, Object object){
		String guildKey = buildGuildKey(guild);
		
		return object == null ? guildKey : Utils.buildKey(guildKey,
				object.toString());
	}
	
	public static String buildTextChannelKey(TextChannel channel){
		return Utils.buildKey(buildGuildKey(channel.getGuild()),
				channel.getId());
	}
	
	public static String buildTextChannelObjectKey(TextChannel channel,
			Object object){
		String channelKey = buildTextChannelKey(channel);
		
		return object == null ? channelKey : Utils.buildKey(channelKey,
				object.toString());
	}
	
	public static String buildUserKey(User user){
		return Utils.buildKey(user.getName(), user.getId());
	}
	
	public static String buildUserObjectKey(User user, Object object){
		String userKey = buildUserKey(user);
		
		return object == null ? userKey : Utils.buildKey(userKey,
				object.toString());
	}
	
	public static String buildUserKey(Member member){
		return buildUserKey(member.getUser());
	}
	
	public static String buildUserObjectKey(Member member, Object object){
		return buildUserObjectKey(member.getUser(), object);
	}
	
	public static String buildCommandKey(TextChannel channel, String commandName){
		return Utils.buildKey(buildTextChannelKey(channel), commandName);
	}
	
	public static String buildCommandObjectKey(TextChannel channel,
			String commandName, Object object){
		return Utils.buildKey(buildCommandKey(channel, commandName),
				object.toString());
	}
	
	public static String buildCommandKey(AbstractBotCommand command){
		return buildCommandKey(command.getTextContext(), command.getCall());
	}
	
	public static String buildCommandObjectKey(AbstractBotCommand command,
			Object object){
		return Utils.buildKey(buildCommandKey(command), object.toString());
	}
	
}
