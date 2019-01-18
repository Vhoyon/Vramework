package io.github.vhoyon.vramework.modules;

import java.util.Date;

import net.dv8tion.jda.core.JDA;
import io.github.vhoyon.vramework.abstracts.Module;
import io.github.vhoyon.vramework.exceptions.JDANotSetException;

public class Metrics extends Module {
	
	private static JDA jda;
	
	private static Date clock;
	
	@Override
	public void build(){
		jda = null;
		clock = null;
	}
	
	public static void setJDA(JDA jda){
		Metrics.jda = jda;
	}
	
	public static void startClock(){
		clock = new Date();
	}
	
	public static void stopClock(){
		clock = null;
	}
	
	public static long getUptime(){
		if(clock == null){
			return 0;
		}
		
		Date now = new Date();
		
		return now.getTime() - clock.getTime();
	}
	
	public static int getNumberOfJoinedServers() throws JDANotSetException{
		
		if(jda == null)
			throw new JDANotSetException();
		
		return jda.getGuilds().size();
		
	}
	
}
