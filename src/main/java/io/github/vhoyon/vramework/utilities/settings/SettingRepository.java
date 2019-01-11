package io.github.vhoyon.vramework.utilities.settings;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import io.github.vhoyon.vramework.exceptions.BadFormatException;

public class SettingRepository {
	
	private Map<String, Setting<Object>> settings;
	
	public SettingRepository(Setting<Object>... settings){
		
		this.settings = new HashMap<>();
		
		for(Setting<Object> setting : settings){
			Setting<Object> clonedSetting = setting.duplicate();
			
			this.getSettingsMap().put(clonedSetting.getName(), clonedSetting);
		}
		
	}
	
	public <E> boolean save(String settingName, E value)
			throws BadFormatException{
		return this.save(settingName, value, null);
	}
	
	public <E> boolean save(String settingName, E value, Consumer<E> onChange)
			throws BadFormatException{
		
		if(!hasSetting(settingName)){
			return false;
		}
		
		//noinspection unchecked
		Setting<E> setting = (Setting<E>)this.getSetting(settingName);
		
		setting.setValue(value, onChange);
		
		return true;
		
	}
	
	public boolean hasSetting(String name){
		return this.getSettingsMap().containsKey(name);
	}
	
	public Setting<Object> getSetting(String name){
		return this.getSettingsMap().get(name);
	}
	
	public Map<String, Setting<Object>> getSettingsMap(){
		return this.settings;
	}
	
	public <SettingValue> SettingValue getSettingValue(String name){
		//noinspection unchecked
		return (SettingValue)this.getSetting(name).getValue();
	}
	
	SettingRepository duplicate(){
		//noinspection unchecked
		return new SettingRepository(getSettingsMap().values().toArray(
				new Setting[0]));
	}
	
}
