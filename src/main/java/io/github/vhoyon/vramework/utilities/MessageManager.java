package io.github.vhoyon.vramework.utilities;

import java.util.HashMap;

import io.github.vhoyon.vramework.interfaces.Utils;
import io.github.vhoyon.vramework.objects.Dictionary;

public class MessageManager {
	
	private HashMap<Integer, Message> messages;
	private HashMap<String, Object> messageReplacements;
	
	private boolean isInvertedIndice;
	private int indice;
	
	public class Message implements Utils {
		
		protected String langKey;
		protected String[] replacementsKeys;
		
		protected boolean isLangAmount;
		protected int amount;
		
		protected Message(String langKey, String... replacementsKeys){
			this.langKey = langKey;
			this.replacementsKeys = replacementsKeys;
			
			this.isLangAmount = false;
			this.amount = 0;
		}
		
		protected Message(String langKey, int amount,
				String... replacementsKeys){
			this.langKey = langKey;
			this.replacementsKeys = replacementsKeys;
			
			this.isLangAmount = true;
			this.amount = amount;
		}
		
		public boolean isMessageAmount(){
			return this.isLangAmount;
		}
		
		public int getMessageAmount(){
			return this.amount;
		}
		
		@Override
		public String toString(){
			return format(this.langKey, createReplacementArray(this));
		}
		
	}
	
	public MessageManager(){
		this.isInvertedIndice = false;
		this.indice = 0;
	}
	
	public void bumpIndice(int amount){
		if(this.isInvertedIndice()){
			this.indice -= amount;
		}
		else{
			this.indice += amount;
		}
	}
	
	public int getCurrentIndice(){
		return this.indice;
	}
	
	public void invertIndice(){
		this.isInvertedIndice = !this.isInvertedIndice;
		this.indice = -this.indice;
	}
	
	public boolean isInvertedIndice(){
		return this.isInvertedIndice;
	}
	
	public MessageManager addMessage(int indice, String langKey,
			String... replacementsKeys){
		
		if(this.messages == null)
			this.messages = new HashMap<>();
		
		this.messages.put(indice, new Message(langKey, replacementsKeys));
		
		return this;
		
	}
	
	public MessageManager addMessageAmount(int indice, int amount,
			String langKey, String... replacementsKeys){
		
		if(this.messages == null)
			this.messages = new HashMap<>();
		
		this.messages.put(indice,
				new Message(langKey, amount, replacementsKeys));
		
		return this;
		
	}
	
	public MessageManager addReplacement(String key, Object value){
		
		if(this.messageReplacements == null)
			this.messageReplacements = new HashMap<>();
		
		this.messageReplacements.put(key, value);
		
		return this;
		
	}
	
	private Object getReplacement(String key){
		
		if(this.messageReplacements == null)
			return null;
		
		return this.messageReplacements.get(key);
		
	}
	
	public Message getMessageRaw(){
		return this.getMessageRaw(this.getCurrentIndice());
	}
	
	public String getMessage(Dictionary dictionary){
		return this.getMessage(this.getCurrentIndice(), dictionary,
				(String)null);
	}
	
	public String getMessage(Dictionary dictionary, Object possiblePrefixObject){
		return this.getMessage(this.getCurrentIndice(), dictionary,
				possiblePrefixObject);
	}
	
	public String getMessage(Dictionary dictionary, Class<?> possiblePrefixClass){
		return this.getMessage(this.getCurrentIndice(), dictionary,
				possiblePrefixClass);
	}
	
	public String getMessage(Dictionary dictionary, String possiblePrefix){
		return getMessage(this.getCurrentIndice(), dictionary, possiblePrefix);
	}
	
	public Message getMessageRaw(int indice){
		return this.messages.get(indice);
	}
	
	public String getMessage(int indice, Dictionary dictionary){
		return this.getMessage(indice, dictionary, (String)null);
	}
	
	public String getMessage(int indice, Dictionary dictionary,
			Object possiblePrefixObject){
		return this.getMessage(indice, dictionary,
				possiblePrefixObject.getClass());
	}
	
	public String getMessage(int indice, Dictionary dictionary,
			Class<?> possiblePrefixClass){
		return this.getMessage(indice, dictionary,
				possiblePrefixClass.getSimpleName());
	}
	
	public String getMessage(int indice, Dictionary dictionary,
			String possiblePrefix){
		
		Message message = this.getMessageRaw(indice);
		
		Object[] replacements = createReplacementArray(message);
		
		if(message.isMessageAmount()){
			return dictionary.getStringAmount(message.langKey, possiblePrefix,
					message.getMessageAmount(), replacements);
		}
		else{
			return dictionary.getString(message.langKey, possiblePrefix,
					replacements);
		}
		
	}
	
	private Object[] createReplacementArray(Message message){
		Object[] replacements = new Object[message.replacementsKeys.length];
		
		for(int i = 0; i < replacements.length; i++){
			replacements[i] = this.getReplacement(message.replacementsKeys[i]);
		}
		
		return replacements;
	}
	
}
