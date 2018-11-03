package io.github.vhoyon.vramework.utilities.settings;

import io.github.vhoyon.vramework.exceptions.BadFormatException;
import io.github.vhoyon.vramework.objects.Dictionary;

import java.util.HashMap;
import java.util.function.Consumer;

public class Setting {
	
	private HashMap<String, SettingField<Object>> fields;
	
	public Setting(SettingField<Object>... fields){
		this(new Dictionary(), fields);
	}
	
	public Setting(Dictionary dict, SettingField<Object>... fields){
		this(dict, true, fields);
	}
	
	public Setting(Dictionary dict, boolean doClone,
			SettingField<Object>... fields){
		
		this.fields = new HashMap<>();
		
		for(SettingField<Object> field : fields){
			SettingField<Object> clonedField = field;
			
			if(doClone)
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
		
		SettingField<Object> field = getField(settingName);
		
		field.setValue(value, onChange);
		
		return true;
		
	}
	
	public boolean hasField(String name){
		return this.getFieldsMap().containsKey(name);
	}
	
	public SettingField<Object> getField(String name){
		return this.getFieldsMap().get(name);
	}
	
	public HashMap<String, SettingField<Object>> getFieldsMap(){
		return this.fields;
	}
	
	public <SettingValue> SettingValue getFieldValue(String name){
		return (SettingValue)this.getField(name).getValue();
	}
	
}
