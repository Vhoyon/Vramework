package io.github.vhoyon.vramework.abstracts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.function.Consumer;

import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.managers.AccountManager;
import net.dv8tion.jda.core.managers.GuildController;
import net.dv8tion.jda.core.requests.restaction.MessageAction;
import io.github.vhoyon.vramework.exceptions.BadContentException;
import io.github.vhoyon.vramework.exceptions.BadFormatException;
import io.github.vhoyon.vramework.interfaces.*;
import io.github.vhoyon.vramework.modules.Logger;
import io.github.vhoyon.vramework.objects.Buffer;
import io.github.vhoyon.vramework.objects.Mention;
import io.github.vhoyon.vramework.objects.MessageEventDigger;
import io.github.vhoyon.vramework.objects.Request;
import io.github.vhoyon.vramework.objects.Request.Parameter;
import io.github.vhoyon.vramework.res.FrameworkResources;
import io.github.vhoyon.vramework.utilities.formatting.DiscordFormatter;
import io.github.vhoyon.vramework.utilities.settings.Setting;
import io.github.vhoyon.vramework.utilities.settings.SettingRepository;

/**
 * Class that implements all the logic to execute actions for a Discord command
 * on the the bot will need to respond to. Note that this does not mean the
 * action is implemented : that's the exact reason why this class is abstract :
 * you need to implement your own action logic for this abstract BotCommand to
 * have any value.
 * This class has many io.github.vhoyon.vramework.utilities for handling
 * different condition types for doing appropriate actions depending on the
 * command you are trying to implement. They should all be available inside the
 * {@code action()} method body, so fire up your IDE to get auto completion to
 * know more about available methods!
 *
 * @version 1.0
 * @since v0.7.0
 * @author V-ed (Guillaume Marcoux)
 */
public abstract class AbstractBotCommand extends Translatable implements
		Emojis, Utils, LinkableCommand, FrameworkResources, DiscordFormatter,
		DiscordUtils {
	
	public static final BufferLevel DEFAULT_BUFFER_LEVEL = BufferLevel.CHANNEL;
	
	protected AbstractCommandRouter router;
	
	private boolean isCopy;
	
	public AbstractBotCommand(){
		this.isCopy = false;
	}
	
	/**
	 * Creates a new AbstractBotCommand with the same context as the command
	 * added in the parameter {@code commandToCopy}.
	 *
	 * @version 1.0
	 * @since v0.7.0
	 * @see #putStateFromCommand(AbstractBotCommand)
	 */
	public AbstractBotCommand(AbstractBotCommand commandToCopy){
		this();
		putStateFromCommand(commandToCopy);
	}
	
	/**
	 * Sets the context of the command in the parameter {@code commandToCopy}
	 * into this command by giving the same router and dictionary pf the
	 * commandToCopy and flag this command as a copy.
	 *
	 * @version 1.0
	 * @since v0.7.0
	 */
	public void putStateFromCommand(AbstractBotCommand commandToCopy){
		
		setRouter(commandToCopy.getRouter());
		setDictionary(commandToCopy.getDictionary());
		
		this.isCopy = true;
		
	}
	
	public String getCommandName(){
		
		String requestName = getRequest().getCommand();
		
		Object calls = getCalls();
		
		if(calls instanceof String[]){
			
			if(Arrays.asList((String[])calls).contains(requestName))
				return requestName;
			
		}
		else{
			
			if(calls.equals(requestName))
				return requestName;
			
		}
		
		return getDefaultCall();
		
	}
	
	public String getContent(){
		return getRequest().getContent();
	}
	
	public String[] getContentParsed(){
		return this.getContentParsedMaxed(-1);
	}
	
	public String[] getContentParsedMaxed(int maxSize){
		
		if(!this.hasContent())
			return null;
		
		ArrayList<String> possibleContent = splitSpacesExcludeQuotesMaxed(
				this.getContent(), maxSize);
		
		return possibleContent.toArray(new String[0]);
		
	}
	
	public Mention getContentAsMention() throws BadContentException{
		if(!isStringMention(getContent()))
			throw new BadContentException("Content is not a mention.");
		
		return new Mention(getIdFromStringMention(getContent()),
				getEventDigger());
	}
	
	public boolean hasContent(){
		return getContent() != null;
	}
	
	public AbstractCommandRouter getRouter(){
		return router;
	}
	
	public void setRouter(AbstractCommandRouter router){
		this.router = router;
	}
	
	public Buffer getBuffer(){
		return getRouter().getBuffer();
	}
	
	public boolean remember(Object object, String associatedName){
		return remember(object, associatedName, DEFAULT_BUFFER_LEVEL);
	}
	
	public boolean remember(Object object, String associatedName,
			BufferLevel level){
		return getBuffer().push(object, associatedName, getKey(level));
	}
	
	public Object getMemory(String associatedName) throws NullPointerException{
		return getMemory(associatedName, DEFAULT_BUFFER_LEVEL);
	}
	
	public Object getMemory(String associatedName, BufferLevel level)
			throws NullPointerException{
		return getBuffer().get(associatedName, getKey(level));
	}
	
	public boolean forget(String associatedName){
		return forget(associatedName, DEFAULT_BUFFER_LEVEL);
	}
	
	public boolean forget(String associatedName, BufferLevel level){
		return getBuffer().remove(associatedName, getKey(level));
	}
	
	public boolean hasMemory(String associatedName){
		return hasMemory(associatedName, DEFAULT_BUFFER_LEVEL);
	}
	
	public boolean hasMemory(String associatedName, BufferLevel level){
		try{
			return getMemory(associatedName, level) != null;
		}
		catch(NullPointerException e){
			return false;
		}
	}
	
	protected MessageReceivedEvent getEvent(){
		return getEventDigger().getEvent();
	}
	
	public Member getBotMember(){
		return getGuild().getSelfMember();
	}
	
	public SelfUser getBotUser(){
		return getEventDigger().getRunningBot();
	}
	
	public AccountManager getBotUserManager(){
		return getBotUser().getManager();
	}
	
	public Member getMember(){
		return getEventDigger().getMember();
	}
	
	public User getUser(){
		return getEventDigger().getUser();
	}
	
	public String getUserId(){
		return getEventDigger().getUserId();
	}
	
	public String getUserName(){
		return getEventDigger().getUserName();
	}
	
	public TextChannel getTextContext(){
		return getEventDigger().getChannel();
	}
	
	public String getTextChannelId(){
		return getEventDigger().getChannelId();
	}
	
	public GuildController getGuildController(){
		return new GuildController(getGuild());
	}
	
	public Guild getGuild(){
		return getEventDigger().getGuild();
	}
	
	public String getGuildId(){
		return getEventDigger().getGuildId();
	}
	
	public String getKey(){
		return getKey(BufferLevel.CHANNEL);
	}
	
	public MessageEventDigger getEventDigger(){
		return getRouter().getEventDigger();
	}
	
	public String getKey(BufferLevel level){
		switch(level){
		case SERVER:
			return getEventDigger().getGuildKey();
		case USER:
			return getEventDigger().getUserKey();
		case CHANNEL:
		default:
			return getEventDigger().getChannelKey();
		}
	}
	
	public Request getRequest(){
		return getRouter().getRequest();
	}
	
	public boolean isCopy(){
		return this.isCopy;
	}
	
	public boolean isAlive(){
		return !this.getRouter().isDead();
	}
	
	public boolean kill(){
		
		if(this instanceof Stoppable && ((Stoppable)this).stopMiddleware()){
			this.getRouter().interrupt();
		}
		
		return !this.isAlive();
		
	}
	
	public HashMap<String, Parameter> getParameters(){
		return this.getRequest().getParameters();
	}
	
	public HashMap<Parameter, ArrayList<String>> getParametersLinks(){
		return this.getRequest().getParametersLinks();
	}
	
	public Parameter getParameter(String... parameterNames){
		return this.getRequest().getParameter(parameterNames);
	}
	
	public Mention getParameterAsMention(String... parametersNames)
			throws BadContentException{
		
		Parameter paramFound = getParameter(parametersNames);
		
		if(!isStringMention(paramFound.getContent()))
			throw new BadContentException("Parameter content is not a mention.");
		
		return new Mention(getIdFromStringMention(paramFound.getContent()),
				getEventDigger());
		
	}
	
	public boolean hasParameter(String parameterName){
		return this.getRequest().hasParameter(parameterName);
	}
	
	public boolean hasParameter(String... parameterNames){
		return this.getRequest().hasParameter(parameterNames);
	}
	
	public void onParameterPresent(String parameterName,
			Consumer<Parameter> onParamPresent){
		this.getRequest().onParameterPresent(parameterName, onParamPresent);
	}
	
	public void connect(VoiceChannel voiceChannel){
		getGuild().getAudioManager().openAudioConnection(voiceChannel);
		
		remember(voiceChannel, BUFFER_VOICE_CHANNEL);
	}
	
	public VoiceChannel getConnectedVoiceChannelBot(){
		return getGuild().getAudioManager().getConnectedChannel();
	}
	
	public boolean isConnectedToVoiceChannelBot(){
		return this.getConnectedVoiceChannelBot() != null;
	}
	
	public VoiceChannel getConnectedVoiceChannelMember(){
		return getMember().getVoiceState().getChannel();
	}
	
	public boolean isConnectedToVoiceChannelMember(){
		return this.getConnectedVoiceChannelMember() != null;
	}
	
	public boolean hasHumansLeftConnected(){
		
		if(!this.isConnectedToVoiceChannelBot()){
			return false;
		}
		else{
			
			VoiceChannel channel = getConnectedVoiceChannelBot();
			
			for(Member member : channel.getMembers()){
				
				if(!(member.getUser().isBot() || member.getUser().isFake())){
					return true;
				}
				
			}
			
			return false;
			
		}
		
	}
	
	public void disconnect(){
		
		if(isConnectedToVoiceChannelBot()){
			getGuild().getAudioManager().closeAudioConnection();
		}
		
		forget(BUFFER_VOICE_CHANNEL);
		
	}
	
	public void setSelfNickname(String nickname){
		this.setNicknameOf(this.getBotMember(), nickname);
	}
	
	public void setNicknameOf(Member member, String nickname){
		this.getGuildController().setNickname(member, nickname).complete();
	}
	
	public String sendMessage(String messageToSend){
		if(messageToSend == null){
			log("The bot attempted to send a null message - probably a fail safe, but concerning nontheless...");
			
			return null;
		}
		
		try{
			return sendMessageForChannel(this.getTextContext(), messageToSend);
		}
		catch(IllegalArgumentException e){
			log(e.getMessage());
			
			return null;
		}
	}
	
	public String sendPrivateMessage(String messageToSend){
		return this.sendMessageToMember(this.getMember(), messageToSend);
	}
	
	public String sendMessageToMember(Member member, String messageToSend){
		
		if(messageToSend == null){
			if(isDebugging())
				log("The bot attempted to send a null message - probably a fail safe, but concerning nonetheless...");
			
			return null;
		}
		if(member.getUser().isBot() || member.getUser().isFake()){
			if(isDebugging())
				log("The bot attempted to send a message to a bot or to a fake user");
			
			return null;
		}
		
		PrivateChannel channel = member.getUser().openPrivateChannel()
				.complete();
		
		try{
			return sendMessageForChannel(channel, messageToSend);
		}
		catch(IllegalArgumentException e){
			log(e);
			
			return null;
		}
		
	}
	
	public String createInfoMessage(String messageToSend, boolean isOneLiner){
		
		String infoChars = "\\~\\~";
		
		String separator;
		
		if(isOneLiner)
			separator = " ";
		else
			separator = "\n";
		
		return infoChars + separator + messageToSend + separator + infoChars;
		
	}
	
	public String sendInfoMessage(String messageToSend, boolean isOneLiner){
		return sendMessage(createInfoMessage(messageToSend, isOneLiner));
	}
	
	public String sendInfoMessage(String messageToSend){
		return sendInfoMessage(messageToSend, true);
	}
	
	public String sendInfoPrivateMessage(String messageToSend){
		return sendInfoPrivateMessage(messageToSend, true);
	}
	
	public String sendInfoPrivateMessage(String messageToSend,
			boolean isOneLiner){
		return sendPrivateMessage(createInfoMessage(messageToSend, isOneLiner));
	}
	
	public String groupAndSendMessages(String... messages){
		
		StringBuilder messageToSend = new StringBuilder(messages[0]);
		
		for(int i = 1; i < messages.length; i++)
			messageToSend.append("\n").append(messages[i]);
		
		return sendMessage(messageToSend.toString());
		
	}
	
	public String groupAndSendMessages(ArrayList<String> messages){
		return groupAndSendMessages(messages
				.toArray(new String[messages.size()]));
	}
	
	public String editMessage(String messageId, String newMessage){
		return editMessageForChannel(getTextContext(), messageId, newMessage);
	}
	
	public void editMessageQueue(String messageId, String newMessage){
		getTextContext().editMessageById(messageId, newMessage).queue();
	}
	
	protected String sendMessageForChannel(MessageChannel channel,
			String message){
		return messageActionComplete(channel.sendMessage(message));
	}
	
	protected String editMessageForChannel(MessageChannel channel,
			String messageId, String newMessage){
		return messageActionComplete(channel.editMessageById(messageId,
				newMessage));
	}
	
	private String messageActionComplete(MessageAction action){
		return action.complete().getId();
	}
	
	public void callCommand(String commandName){
		AbstractBotCommand command = (AbstractBotCommand)getRouter()
				.getLinkableCommand(commandName);
		
		this.callCommand(command);
	}
	
	public void callCommand(AbstractBotCommand command){
		command.putStateFromCommand(this);
		
		command.action();
	}
	
	/**
	 * Gets the
	 * {@link io.github.vhoyon.vramework.utilities.settings.SettingRepository}
	 * object from this command's router.
	 *
	 * @return The {@link io.github.vhoyon.vramework.utilities.settings.Setting}
	 *         object of this
	 *         {@link io.github.vhoyon.vramework.abstracts.AbstractCommandRouter
	 *         Router}.
	 * @since v0.14.0
	 * @see AbstractCommandRouter#getSettings(BufferLevel)
	 */
	public SettingRepository getSettings(){
		return this.getSettings(DEFAULT_BUFFER_LEVEL);
	}
	
	/**
	 * Gets the
	 * {@link io.github.vhoyon.vramework.utilities.settings.SettingRepository}
	 * object from this command's router.
	 *
	 * @param level
	 *            The level at which the settings will be retrieved from.
	 * @return The {@link io.github.vhoyon.vramework.utilities.settings.Setting}
	 *         object of this
	 *         {@link io.github.vhoyon.vramework.abstracts.AbstractCommandRouter
	 *         Router}.
	 * @since v0.14.0
	 * @see AbstractCommandRouter#getSettings()
	 */
	public SettingRepository getSettings(BufferLevel level){
		return this.getRouter().getSettings(level);
	}
	
	/**
	 * Gets the {@link io.github.vhoyon.vramework.utilities.settings.Setting}
	 * object from this router.
	 *
	 * @param settingName
	 *            The name of the setting to get from the SettingRepository of
	 *            this Router.
	 * @return The Setting object, generalized to Object to include all Fields
	 *         possible. The burden of casting to the right type goes to you.<br>
	 *         If you want to get the value and have it casted automatically to
	 *         your own return value, please see {@link #setting(String)}.
	 * @since v0.14.0
	 * @see #setting(String)
	 */
	public Setting<Object> getSetting(String settingName){
		return this.getSetting(settingName, DEFAULT_BUFFER_LEVEL);
	}
	
	/**
	 * Gets the {@link io.github.vhoyon.vramework.utilities.settings.Setting}
	 * object from this router.
	 *
	 * @param settingName
	 *            The name of the setting to get from the SettingRepository of
	 *            this Router.
	 * @param level
	 *            The level at which the settings will be retrieved from.
	 * @return The Setting object, generalized to Object to include all Fields
	 *         possible. The burden of casting to the right type goes to you.<br>
	 *         If you want to get the value and have it casted automatically to
	 *         your own return value, please see {@link #setting(String)}.
	 * @since v0.14.0
	 * @see #setting(String)
	 */
	public Setting<Object> getSetting(String settingName, BufferLevel level){
		return this.getSettings(level).getField(settingName);
	}
	
	/**
	 * Gets the value of the
	 * {@link io.github.vhoyon.vramework.utilities.settings.Setting} associated
	 * to the name of the parameter {@code settingName}.
	 *
	 * @param settingName
	 *            The name of the Setting to get the value from.
	 * @param <SettingValue>
	 *            The type of the value to be casted automatically to.
	 * @return The value of the Setting with the name {@code settingName}.
	 * @since v0.14.0
	 */
	public <SettingValue> SettingValue setting(String settingName){
		return this.setting(settingName, DEFAULT_BUFFER_LEVEL);
	}
	
	/**
	 * Gets the value of the
	 * {@link io.github.vhoyon.vramework.utilities.settings.Setting} associated
	 * to the name of the parameter {@code settingName}.
	 *
	 * @param settingName
	 *            The name of the Setting to get the value from.
	 * @param level
	 *            The level at which the settings will be retrieved from.
	 * @param <SettingValue>
	 *            The type of the value to be casted automatically to.
	 * @return The value of the Setting with the name {@code settingName}.
	 * @since v0.14.0
	 */
	public <SettingValue> SettingValue setting(String settingName,
			BufferLevel level){
		Object value = this.getSetting(settingName, level).getValue();
		return (SettingValue)value;
	}
	
	/**
	 * Sets the setting with the associated name from the parameter
	 * {@code settingName} to the value from the parameter {@code value}.
	 *
	 * @param settingName
	 *            Name of the setting to change
	 * @param value
	 *            {@code Object} value to be set to this setting.
	 * @throws IllegalArgumentException
	 *             {@code value} parameter is not the type of the
	 *             {@link io.github.vhoyon.vramework.utilities.settings.Setting}
	 *             associated with the {@code name} provided.
	 * @throws BadFormatException
	 *             Thrown if the {@code value} parameter is not the right type
	 *             for the setting searched for.
	 * @since v0.14.0
	 * @see #setSetting(String, Object, Consumer)
	 */
	public void setSetting(String settingName, Object value)
			throws BadFormatException{
		this.setSetting(settingName, value, DEFAULT_BUFFER_LEVEL, null);
	}
	
	/**
	 * Sets the setting with the associated name from the parameter
	 * {@code settingName} to the value from the parameter {@code value}.
	 *
	 * @param settingName
	 *            Name of the setting to change
	 * @param value
	 *            {@code Object} value to be set to this setting.
	 * @param level
	 *            The level at which the settings will be retrieved from.
	 * @throws IllegalArgumentException
	 *             {@code value} parameter is not the type of the
	 *             {@link io.github.vhoyon.vramework.utilities.settings.Setting}
	 *             associated with the {@code name} provided.
	 * @throws BadFormatException
	 *             Thrown if the {@code value} parameter is not the right type
	 *             for the setting searched for.
	 * @since v0.14.0
	 * @see #setSetting(String, Object, Consumer)
	 */
	public void setSetting(String settingName, Object value, BufferLevel level)
			throws BadFormatException{
		this.setSetting(settingName, value, level, null);
	}
	
	/**
	 * Sets the setting with the associated name from the parameter
	 * {@code settingName} to the value from the parameter {@code value} and
	 * runs arbitrary code after a successful change.
	 *
	 * @param settingName
	 *            Name of the setting to change
	 * @param value
	 *            {@code Object} value to be set to this setting.
	 * @param onChange
	 *            Arbitrary code to run when the setting has been changed using
	 *            a {@link java.util.function.Consumer} and the
	 *            {@link java.util.function.Consumer#accept(Object)} method, in
	 *            which the validated value is sent to. Can be {@code null} (or
	 *            use {@link #setSetting(String, Object)}) to not run anything
	 *            on change success.
	 * @throws IllegalArgumentException
	 *             {@code value} parameter is not the type of the
	 *             {@link io.github.vhoyon.vramework.utilities.settings.Setting}
	 *             associated with the {@code name} provided.
	 * @throws BadFormatException
	 *             Thrown if the {@code value} parameter is not the right type
	 *             for the setting searched for.
	 * @since v0.14.0
	 */
	public void setSetting(String settingName, Object value,
			Consumer<Object> onChange) throws BadFormatException{
		this.setSetting(settingName, value, DEFAULT_BUFFER_LEVEL, onChange);
	}
	
	/**
	 * Sets the setting with the associated name from the parameter
	 * {@code settingName} to the value from the parameter {@code value} and
	 * runs arbitrary code after a successful change.
	 *
	 * @param settingName
	 *            Name of the setting to change
	 * @param value
	 *            {@code Object} value to be set to this setting.
	 * @param level
	 *            The level at which the settings will be retrieved from.
	 * @param onChange
	 *            Arbitrary code to run when the setting has been changed using
	 *            a {@link java.util.function.Consumer} and the
	 *            {@link java.util.function.Consumer#accept(Object)} method, in
	 *            which the validated value is sent to. Can be {@code null} (or
	 *            use {@link #setSetting(String, Object)}) to not run anything
	 *            on change success.
	 * @throws IllegalArgumentException
	 *             {@code value} parameter is not the type of the
	 *             {@link io.github.vhoyon.vramework.utilities.settings.Setting}
	 *             associated with the {@code name} provided.
	 * @throws BadFormatException
	 *             Thrown if the {@code value} parameter is not the right type
	 *             for the setting searched for.
	 * @since v0.14.0
	 */
	public void setSetting(String settingName, Object value, BufferLevel level,
			Consumer<Object> onChange) throws BadFormatException{
		
		SettingRepository settings = this.getSettings(level);
		
		settings.save(settingName, value, onChange);
		
	}
	
	public void log(String message){
		Logger.log(message);
	}
	
	public void log(Exception e){
		Logger.log(e);
	}
	
	/**
	 * @return A String that starts with the router's command prefix
	 *         followed by the <i>commandName</i> parameter.
	 */
	public String buildCommand(String command){
		return getRequest().getCommandPrefix() + command;
	}
	
	/**
	 * @return A String that starts with the <i>PREFIX</i> found in Ressources
	 *         followed by the <i>commandName</i> parameter, surrounded by two
	 *         "<b>`</b>" tick, meaning the visual will be like code in Discord.
	 */
	public String buildVCommand(String command){
		return code(buildCommand(command));
	}
	
	/**
	 * @return A String that starts with the <i>PARAMETER_PREFIX</i> found in
	 *         Ressources followed by the <i>parameter</i> parameter.
	 */
	public String buildParameter(String parameter){
		char p = getRequest().getParametersPrefix();
		return (parameter.length() > 1 ? p + "" + p : p) + parameter;
	}
	
	/**
	 * @return A String that starts with the <i>PARAMETER_PREFIX</i> found in
	 *         Ressources followed by the <i>parameter</i> parameter.
	 */
	public String buildVParameter(String parameter){
		return code(buildParameter(parameter));
	}
	
	@Override
	public String formatParameter(String parameterToFormat){
		return buildParameter(parameterToFormat);
	}
	
	/**
	 * Gets the formatted usage for this command.
	 *
	 * @return A formatted String that uses {@link #buildVCommand(String)} and
	 *         {@link #getCommandName()}.
	 * @version 1.0
	 * @since v0.13.0
	 */
	public String getUsage(){
		return buildVCommand(getCommandName());
	}
	
}
