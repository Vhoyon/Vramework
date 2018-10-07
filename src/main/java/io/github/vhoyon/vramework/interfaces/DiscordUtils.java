package io.github.vhoyon.vramework.interfaces;

public interface DiscordUtils {
	
	default boolean isStringMention(String string){
		return string.matches("^<@!?[0-9]{18}>$");
	}
	
	default String getIdFromStringMention(String stringMention){
		return stringMention.replaceAll("<@!?([0-9]{18})>", "$1");
	}
	
	default boolean isStringMentionRole(String string){
		return string.matches("^<@&[0-9]{18}>$");
	}
	
	default String getIdFromStringMentionRole(String stringMention){
		return stringMention.replaceAll("<@&([0-9]{18})>", "$1");
	}
	
}
