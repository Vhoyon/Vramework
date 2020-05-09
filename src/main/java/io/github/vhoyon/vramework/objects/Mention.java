package io.github.vhoyon.vramework.objects;

import java.awt.*;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;

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
	
	@Nonnull
	@Override
	public EnumSet<Permission> getPermissions(){
		return this.mentionnedMember.getPermissions();
	}
	
	@Nonnull
	@Override
	public EnumSet<Permission> getPermissions(@Nonnull GuildChannel channel){
		return this.mentionnedMember.getPermissions(channel);
	}
	
	@Nonnull
	@Override
	public EnumSet<Permission> getPermissionsExplicit(){
		return this.mentionnedMember.getPermissionsExplicit();
	}
	
	@Nonnull
	@Override
	public EnumSet<Permission> getPermissionsExplicit(
			@Nonnull GuildChannel channel){
		return this.mentionnedMember.getPermissionsExplicit(channel);
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
	public boolean hasPermission(GuildChannel channel,
			Permission... permissions){
		return this.mentionnedMember.hasPermission(channel, permissions);
	}
	
	@Override
	public boolean hasPermission(GuildChannel channel,
			Collection<Permission> permissions){
		return this.mentionnedMember.hasPermission(channel, permissions);
	}
	
	@Override
	public JDA getJDA(){
		return this.mentionnedMember.getJDA();
	}
	
	@Nonnull
	@Override
	public OffsetDateTime getTimeJoined(){
		return this.mentionnedMember.getTimeJoined();
	}
	
	@Override
	public boolean hasTimeJoined(){
		return this.mentionnedMember.hasTimeJoined();
	}
	
	@Nullable
	@Override
	public OffsetDateTime getTimeBoosted(){
		return this.mentionnedMember.getTimeBoosted();
	}
	
	@Override
	public GuildVoiceState getVoiceState(){
		return this.mentionnedMember.getVoiceState();
	}
	
	@Nonnull
	@Override
	public List<Activity> getActivities(){
		return this.mentionnedMember.getActivities();
	}
	
	@Override
	public OnlineStatus getOnlineStatus(){
		return this.mentionnedMember.getOnlineStatus();
	}
	
	@Nonnull
	@Override
	public OnlineStatus getOnlineStatus(@Nonnull ClientType type){
		return this.mentionnedMember.getOnlineStatus(type);
	}
	
	@Nonnull
	@Override
	public EnumSet<ClientType> getActiveClients(){
		return this.mentionnedMember.getActiveClients();
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
	
	@Override
	public boolean isFake(){
		return false;
	}
	
	@Override
	public long getIdLong(){
		return 0;
	}
}
