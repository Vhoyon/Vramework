package io.github.vhoyon.vramework.objects;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.*;

import javax.annotation.Nullable;
import java.awt.*;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;

public class Mention implements Member {
	
	private MessageEventDigger digger;
	
	private Member mentionnedMember;
	
	public Mention(String userId, MessageEventDigger digger){
		this.digger = digger;
		
		this.mentionnedMember = this.digger.getGuild().getMember(
				digger.getJDA().getUserById(userId));
	}
	
	public boolean isMentionningSelf(){
		return this.getUser().equals(this.digger.getUser());
	}
	
	public boolean isUserBot(){
		return this.getUser().isBot();
	}
	
	public boolean isRunningBot(){
		return this.digger.getRunningBot().getIdLong() == this.getUser()
				.getIdLong();
	}
	
	@Override
	public User getUser(){
		return this.mentionnedMember.getUser();
	}
	
	@Override
	public Guild getGuild(){
		return this.mentionnedMember.getGuild();
	}
	
	@Override
	public List<Permission> getPermissions(){
		return this.mentionnedMember.getPermissions();
	}
	
	@Override
	public boolean hasPermission(Permission... permissions){
		return this.mentionnedMember.hasPermission(permissions);
	}
	
	@Override
	public boolean hasPermission(Collection<Permission> permissions){
		return this.mentionnedMember.hasPermission(permissions);
	}
	
	@Override
	public boolean hasPermission(Channel channel, Permission... permissions){
		return this.mentionnedMember.hasPermission(channel, permissions);
	}
	
	@Override
	public boolean hasPermission(Channel channel,
			Collection<Permission> permissions){
		return this.mentionnedMember.hasPermission(channel, permissions);
	}
	
	@Override
	public JDA getJDA(){
		return this.mentionnedMember.getJDA();
	}
	
	@Override
	public OffsetDateTime getJoinDate(){
		return this.mentionnedMember.getJoinDate();
	}
	
	@Override
	public GuildVoiceState getVoiceState(){
		return this.mentionnedMember.getVoiceState();
	}
	
	@Override
	public Game getGame(){
		return this.mentionnedMember.getGame();
	}
	
	@Override
	public OnlineStatus getOnlineStatus(){
		return this.mentionnedMember.getOnlineStatus();
	}
	
	@Override
	public String getNickname(){
		return this.mentionnedMember.getNickname();
	}
	
	@Override
	public String getEffectiveName(){
		return this.mentionnedMember.getEffectiveName();
	}
	
	@Override
	public List<Role> getRoles(){
		return this.mentionnedMember.getRoles();
	}
	
	@Override
	public Color getColor(){
		return this.mentionnedMember.getColor();
	}
	
	@Override
	public int getColorRaw(){
		return this.mentionnedMember.getColorRaw();
	}
	
	@Override
	public List<Permission> getPermissions(Channel channel){
		return this.mentionnedMember.getPermissions(channel);
	}
	
	@Override
	public boolean canInteract(Member member){
		return this.mentionnedMember.canInteract(member);
	}
	
	@Override
	public boolean canInteract(Role role){
		return this.mentionnedMember.canInteract(role);
	}
	
	@Override
	public boolean canInteract(Emote emote){
		return this.mentionnedMember.canInteract(emote);
	}
	
	@Override
	public boolean isOwner(){
		return this.mentionnedMember.isOwner();
	}
	
	@Nullable
	@Override
	public TextChannel getDefaultChannel(){
		return this.mentionnedMember.getDefaultChannel();
	}
	
	@Override
	public String getAsMention(){
		return this.mentionnedMember.getAsMention();
	}
	
	@Override
	public boolean equals(Object obj){
		
		if(obj == null)
			return false;
		
		if(obj instanceof Member){
			
			Member mbr = (Member)obj;
			return this.getUser().getId().equals(mbr.getUser().getId());
			
		}
		
		return false;
		
	}
	
}
