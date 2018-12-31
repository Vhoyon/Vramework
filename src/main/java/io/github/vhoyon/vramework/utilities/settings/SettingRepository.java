package io.github.vhoyon.vramework.utilities.settings;

import java.util.HashMap;
import java.util.function.Consumer;

import io.github.vhoyon.vramework.exceptions.BadFormatException;
import io.github.vhoyon.vramework.objects.Dictionary;

public class SettingRepository {
	
	private HashMap<String, Setting<Object>> fields;
	
	public SettingRepository(Setting<Object>... fields){
		this(new Dictionary(), fields);
	}
	
	public SettingRepository(Dictionary dict, Setting<Object>... fields){
		
		this.fields = new HashMap<>();
		
		for(Setting<Object> field : fields){
			Setting<Object> clonedField = field;
			
			try{
				clonedField = field.clone();
			}
			catch(CloneNotSupportedException e){}
			
			clonedField.setDictionary(dict);
			
			this.getFieldsMap().put(clonedField.getName(), clonedField);
		}
		
	}
	
	public boolean save(String settingName, Object value,
			Consumer<Object> onChange) throws BadFormatException{
		
		if(!hasField(settingName)){
			return false;
		}
		
		Setting<Object> field = getField(settingName);
		
		field.setValue(value, onChange);
		
		return true;
		
	}
	
	public boolean hasField(String name){
		return this.getFieldsMap().containsKey(name);
	}
	
	public Setting<Object> getField(String name){
		return this.getFieldsMap().get(name);
	}
	
	public HashMap<String, Setting<Object>> getFieldsMap(){
		return this.fields;
	}
	
	public <SettingValue> SettingValue getFieldValue(String name){
		return (SettingValue)this.getField(name).getValue();
	}
	
}
