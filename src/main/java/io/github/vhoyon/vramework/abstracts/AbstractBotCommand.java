package io.github.vhoyon.vramework.abstracts;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.managers.AccountManager;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import io.github.ved.jrequester.OptionData;
import io.github.ved.jrequester.Request;
import io.github.ved.jsanitizers.exceptions.BadFormatException;
import io.github.vhoyon.vramework.exceptions.BadContentException;
import io.github.vhoyon.vramework.interfaces.*;
import io.github.vhoyon.vramework.modules.Logger;
import io.github.vhoyon.vramework.objects.*;
import io.github.vhoyon.vramework.res.FrameworkResources;
import io.github.vhoyon.vramework.util.KeyBuilder;
import io.github.vhoyon.vramework.util.TimerManager;
import io.github.vhoyon.vramework.util.formatting.DiscordFormatter;
import io.github.vhoyon.vramework.util.settings.Setting;
import io.github.vhoyon.vramework.util.settings.SettingRepository;

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
public abstract class AbstractBotCommand implements Translatable, Utils,
		LinkableCommand, FrameworkResources, DiscordFormatter, DiscordUtils {
	
	public static final BufferLevel DEFAULT_BUFFER_LEVEL = BufferLevel.CHANNEL;
	
	public static final String TYPING_TIMER_NAME = "VRAMEWORK_BOT_TYPING_TIMER";
	
	private Dictionary dict;
	
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
		this.putStateFromCommand(commandToCopy);
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
		
		this.setRouter(commandToCopy.getRouter());
		this.setDictionary(commandToCopy.getDictionary());
		
		this.isCopy = true;
		
	}
	
	@Override
	public void setDictionary(Dictionary dict){
		this.dict = dict;
	}
	
	@Override
	public Dictionary getDictionary(){
		return this.dict;
	}
	
	@Override
	public void action(){
		
		boolean shouldDisplayTypingIndicator = this.displayTypingIndicator();
		
		if(shouldDisplayTypingIndicator){
			
			TextChannel channel = this.getTextContext();
			
			String typingTimerName = KeyBuilder.buildTextChannelObjectKey(
					channel, TYPING_TIMER_NAME);
			
			Runnable sendTypingIndicator = () -> channel.sendTyping()
					.complete();
			
			sendTypingIndicator.run();
			
			TimerManager.schedule(typingTimerName, 7500, sendTypingIndicator,
					() -> TimerManager.resetTimer(typingTimerName));
			
		}
		
		this.actions();
		
		if(shouldDisplayTypingIndicator){
			String typingTimerName = KeyBuilder.buildTextChannelObjectKey(
					this.getTextContext(), TYPING_TIMER_NAME);
			
			TimerManager.stopTimer(typingTimerName);
		}
		
	}
	
	protected abstract void actions();
	
	@Override
	public String getActualCall(){
		return this.getRequest().getCommand();
	}
	
	public String getCommandName(){
		
		String requestName = getActualCall();
		
		List<String> calls = this.getAllCalls();
		
		if(!calls.contains(requestName)){
			return this.getCall();
		}
		
		return requestName;
		
	}
	
	public String getContent(){
		return this.getRequest().getContent();
	}
	
	public Mention getContentAsMention() throws BadContentException{
		if(!isStringMention(this.getContent()))
			throw new BadContentException("Content is not a mention.");
		
		return new Mention(this.getIdFromStringMention(this.getContent()),
				this.getEventDigger());
	}
	
	public boolean hasContent(){
		return this.getContent() != null;
	}
	
	public AbstractCommandRouter getRouter(){
		return this.router;
	}
	
	public void setRouter(AbstractCommandRouter router){
		this.router = router;
	}
	
	public Buffer getBuffer(){
		return this.getRouter().getBuffer();
	}
	
	public boolean remember(Object object, String associatedName){
		return this.remember(object, associatedName, DEFAULT_BUFFER_LEVEL);
	}
	
	public boolean remember(Object object, String associatedName,
			BufferLevel level){
		return this.getBuffer().push(object, getKey(associatedName, level));
	}
	
	public <E> E getMemory(String associatedName) throws IllegalStateException{
		return this.getMemory(associatedName, DEFAULT_BUFFER_LEVEL);
	}
	
	public <E> E getMemory(String associatedName, BufferLevel level)
			throws IllegalStateException{
		return this.getBuffer().get(getKey(associatedName, level));
	}
	
	public boolean forget(String associatedName){
		return this.forget(associatedName, DEFAULT_BUFFER_LEVEL);
	}
	
	public boolean forget(String associatedName, BufferLevel level){
		return this.getBuffer().remove(getKey(associatedName, level));
	}
	
	public boolean hasMemory(String associatedName){
		return this.hasMemory(associatedName, DEFAULT_BUFFER_LEVEL);
	}
	
	public boolean hasMemory(String associatedName, BufferLevel level){
		try{
			return this.getMemory(associatedName, level) != null;
		}
		catch(IllegalStateException e){
			return false;
		}
	}
	
	protected MessageReceivedEvent getEvent(){
		return this.getEventDigger().getEvent();
	}
	
	public Member getBotMember(){
		return this.getGuild().getSelfMember();
	}
	
	public SelfUser getBotUser(){
		return this.getEventDigger().getRunningBot();
	}
	
	public AccountManager getBotUserManager(){
		return this.getBotUser().getManager();
	}
	
	public Member getMember(){
		return this.getEventDigger().getMember();
	}
	
	public User getUser(){
		return this.getEventDigger().getUser();
	}
	
	public String getUserId(){
		return this.getEventDigger().getUserId();
	}
	
	public String getUserName(){
		return this.getEventDigger().getUserName();
	}
	
	public TextChannel getTextContext(){
		return this.getEventDigger().getChannel();
	}
	
	public String getTextChannelId(){
		return this.getEventDigger().getChannelId();
	}
	
	public Guild getGuild(){
		return this.getEventDigger().getGuild();
	}
	
	public String getGuildId(){
		return this.getEventDigger().getGuildId();
	}
	
	public String getKey(){
		return this.getKey(DEFAULT_BUFFER_LEVEL);
	}
	
	public MessageEventDigger getEventDigger(){
		return this.getRouter().getEventDigger();
	}
	
	public String getKey(String name, BufferLevel level){
		switch(level){
		case GUILD:
			return this.getEventDigger().getGuildKey(name);
		case USER:
			return this.getEventDigger().getUserKey(name);
		case CHANNEL:
		default:
			return this.getEventDigger().getChannelKey(name);
		}
	}
	
	public String getKey(BufferLevel level){
		return this.getKey(null, level);
	}
	
	public Request getRequest(){
		return this.getRouter().getRequest();
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
	
	public Map<String, OptionData> getOptionsData(){
		return this.getRequest().getOptionsData();
	}
	
	public Map<OptionData, List<String>> getOptionsLinks(){
		return this.getRequest().getOptionsLinks();
	}
	
	public OptionData getOption(String... optionNames){
		return this.getRequest().getOption(optionNames);
	}
	
	public Mention getOptionAsMention(String... optionNames)
			throws BadContentException{
		
		OptionData optionFound = this.getOption(optionNames);
		
		if(!isStringMention(optionFound.getContent()))
			throw new BadContentException("Option content is not a mention.");
		
		return new Mention(getIdFromStringMention(optionFound.getContent()),
				this.getEventDigger());
		
	}
	
	public boolean hasOption(String optionName){
		return this.getRequest().hasOption(optionName);
	}
	
	public boolean hasOption(String... optionNames){
		return this.getRequest().hasOption(optionNames);
	}
	
	public void onOptionPresent(String optionName,
			Consumer<OptionData> onOptionPresent){
		this.getRequest().onOptionPresent(optionName, onOptionPresent);
	}
	
	public void connect(VoiceChannel voiceChannel){
		this.getGuild().getAudioManager().openAudioConnection(voiceChannel);
		
		this.remember(voiceChannel, BUFFER_VOICE_CHANNEL);
	}
	
	public VoiceChannel getConnectedVoiceChannelBot(){
		return this.getGuild().getAudioManager().getConnectedChannel();
	}
	
	public boolean isConnectedToVoiceChannelBot(){
		return this.getConnectedVoiceChannelBot() != null;
	}
	
	public VoiceChannel getConnectedVoiceChannelMember(){
		return this.getMember().getVoiceState().getChannel();
	}
	
	public boolean isConnectedToVoiceChannelMember(){
		return this.getConnectedVoiceChannelMember() != null;
	}
	
	public boolean hasHumansLeftConnected(){
		return EventDigger.doesConnectedChannelHasHumansLeft(getGuild());
	}
	
	public void disconnect(){
		
		if(isConnectedToVoiceChannelBot()){
			this.getGuild().getAudioManager().closeAudioConnection();
		}
		
		this.forget(BUFFER_VOICE_CHANNEL);
		
	}
	
	public void setSelfNickname(String nickname){
		this.getBotMember().modifyNickname(nickname).complete();
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
		return this.sendMessage(createInfoMessage(messageToSend, isOneLiner));
	}
	
	public String sendInfoMessage(String messageToSend){
		return this.sendInfoMessage(messageToSend, true);
	}
	
	public String sendInfoPrivateMessage(String messageToSend){
		return this.sendInfoPrivateMessage(messageToSend, true);
	}
	
	public String sendInfoPrivateMessage(String messageToSend,
			boolean isOneLiner){
		return this.sendPrivateMessage(createInfoMessage(messageToSend,
				isOneLiner));
	}
	
	public String groupAndSendMessages(String... messages){
		
		StringBuilder messageToSend = new StringBuilder(messages[0]);
		
		for(int i = 1; i < messages.length; i++)
			messageToSend.append("\n").append(messages[i]);
		
		return this.sendMessage(messageToSend.toString());
		
	}
	
	public String groupAndSendMessages(List<String> messages){
		return this.groupAndSendMessages(messages.toArray(new String[0]));
	}
	
	public String editMessage(String messageId, String newMessage){
		return this.editMessageForChannel(getTextContext(), messageId,
				newMessage);
	}
	
	public void editMessageQueue(String messageId, String newMessage){
		this.getTextContext().editMessageById(messageId, newMessage).queue();
	}
	
	protected String sendMessageForChannel(MessageChannel channel,
			String message){
		return this.messageActionComplete(channel.sendMessage(message));
	}
	
	protected String editMessageForChannel(MessageChannel channel,
			String messageId, String newMessage){
		return this.messageActionComplete(channel.editMessageById(messageId,
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
	 * {@link io.github.vhoyon.vramework.util.settings.SettingRepository} object
	 * from this command's router.
	 *
	 * @return The {@link io.github.vhoyon.vramework.util.settings.Setting}
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
	 * {@link io.github.vhoyon.vramework.util.settings.SettingRepository} object
	 * from this command's router.
	 *
	 * @param level
	 *            The level at which the settings will be retrieved from.
	 * @return The {@link io.github.vhoyon.vramework.util.settings.Setting}
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
	 * Gets the {@link io.github.vhoyon.vramework.util.settings.Setting} object
	 * from this router.
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
	 * Gets the {@link io.github.vhoyon.vramework.util.settings.Setting} object
	 * from this router.
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
		return this.getSettings(level).getSetting(settingName);
	}
	
	/**
	 * Gets the value of the
	 * {@link io.github.vhoyon.vramework.util.settings.Setting} associated
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
	 * {@link io.github.vhoyon.vramework.util.settings.Setting} associated
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
		//noinspection unchecked
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
	 *             {@link io.github.vhoyon.vramework.util.settings.Setting}
	 *             associated with the {@code name} provided.
	 * @throws BadFormatException
	 *             Thrown if the {@code value} parameter is not the right type
	 *             for the setting searched for.
	 * @since v0.14.0
	 * @see #setSetting(String, Object, Consumer)
	 */
	public void setSetting(String settingName, Object value)
			throws BadFormatException{
		this.setSetting(settingName, value, null, null);
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
	 *             {@link io.github.vhoyon.vramework.util.settings.Setting}
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
	 *             {@link io.github.vhoyon.vramework.util.settings.Setting}
	 *             associated with the {@code name} provided.
	 * @throws BadFormatException
	 *             Thrown if the {@code value} parameter is not the right type
	 *             for the setting searched for.
	 * @since v0.14.0
	 */
	public void setSetting(String settingName, Object value,
			Consumer<Object> onChange) throws BadFormatException{
		this.setSetting(settingName, value, null, onChange);
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
	 *             {@link io.github.vhoyon.vramework.util.settings.Setting}
	 *             associated with the {@code name} provided.
	 * @throws BadFormatException
	 *             Thrown if the {@code value} parameter is not the right type
	 *             for the setting searched for.
	 * @since v0.14.0
	 */
	public void setSetting(String settingName, Object value, BufferLevel level,
			Consumer<Object> onChange) throws BadFormatException{
		
		if(level == null)
			level = DEFAULT_BUFFER_LEVEL;
		
		SettingRepository settings = this.getSettings(level);
		
		settings.save(settingName, value, onChange);
		
	}
	
	public boolean displayTypingIndicator(){
		return true;
	}
	
	public void stopTypingIndicator(){
		TimerManager.stopTimer(KeyBuilder.buildTextChannelObjectKey(
				this.getTextContext(), TYPING_TIMER_NAME));
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
		return this.getRequest().getCommandPrefix() + command;
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
	 * @return A String that starts with the option prefix of the current
	 *         Request's setting, doubled if it's a short option.
	 */
	public String buildOption(String option){
		char p = this.getRequest().getOptionsPrefix();
		return (option.length() > 1 ? p + "" + p : p) + option;
	}
	
	/**
	 * @return A String that starts with the option prefix of the current
	 *         Request's setting, doubled if it's a short option, surrounded by
	 *         backticks through the {@link #code(Object)} method.
	 */
	public String buildVOption(String option){
		return code(buildOption(option));
	}
	
	@Override
	public String formatCommand(String commandToFormat){
		return buildVCommand(commandToFormat);
	}
	
	@Override
	public String formatOption(String optionToFormat){
		return buildOption(optionToFormat);
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
