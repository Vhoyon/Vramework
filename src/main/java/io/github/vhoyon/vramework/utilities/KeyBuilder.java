package io.github.vhoyon.vramework.utilities;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import io.github.vhoyon.vramework.abstracts.AbstractBotCommand;
import io.github.vhoyon.vramework.interfaces.Utils;

public class KeyBuilder {
	
	protected KeyBuilder(){}
	
	public static String buildGuildKey(Guild guild){
		return Utils.buildKey(guild.getId());
	}
	
	public static String buildTextChannelKey(TextChannel channel){
		return Utils.buildKey(buildGuildKey(channel.getGuild()),
				channel.getId());
	}
	
	public static String buildUserKey(User user){
		return Utils.buildKey(user.getName(), user.getId());
	}
	
	public static String buildUserKey(Member member){
		return buildUserKey(member.getUser());
	}
	
	public static String buildCommandKey(TextChannel channel, String commandName){
		return Utils.buildKey(buildTextChannelKey(channel), commandName);
	}
	
	public static String buildCommandKey(AbstractBotCommand command){
		return buildCommandKey(command.getTextContext(),
				command.getDefaultCall());
	}
	
}
