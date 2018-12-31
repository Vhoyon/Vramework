package io.github.vhoyon.vramework.utilities.settings;

import io.github.vhoyon.vramework.abstracts.Translatable;
import io.github.vhoyon.vramework.exceptions.BadFormatException;
import io.github.vhoyon.vramework.modules.Environment;
import io.github.vhoyon.vramework.modules.Logger;
import io.github.vhoyon.vramework.modules.Logger.LogType;

import java.util.function.Consumer;

public abstract class Setting<E> extends Translatable implements Cloneable {
	
	protected E value;
	private E defaultValue;
	
	private String name;
	private String env;
	
	public Setting(String name, String env, E defaultValue){
		
		this.name = name;
		this.defaultValue = defaultValue;
		this.env = env;
		
	}
	
	public E getValue(){
		if(this.value != null){
			return this.value;
		}
		
		try{
			E envValue = null;
			
			try{
				envValue = Environment.getVar(this.env);
			}
			catch(NullPointerException e){}
			
			if(envValue == null){
				this.value = this.getDefaultValue();
			}
			else{
				this.value = this.formatEnvironment(envValue);
			}
		}
		catch(ClassCastException | BadFormatException e){
			
			Logger.log(
					"Environment variable is not formatted correctly for its data type! Using default value.",
					LogType.WARNING);
			
			this.value = this.getDefaultValue();
			
		}
		
		return this.value;
	}
	
	public String getName(){
		return this.name;
	}
	
	protected String getEnv(){
		return this.env;
	}
	
	public E getDefaultValue(){
		return this.defaultValue;
	}
	
	public final void setValue(E value) throws BadFormatException{
		this.setValue(value, null);
	}
	
	public final void setToDefaultValue(){
		this.setToDefaultValue(null);
	}
	
	public final void setToDefaultValue(Consumer<E> onChange){
		this.setValue(getDefaultValue(), onChange);
	}
	
	public final void setValue(E value, Consumer<E> onChange)
			throws BadFormatException{
		
		if(value == null){
			throw new BadFormatException("Value cannot be null!", 0);
		}
		
		this.value = this.sanitizeValue(value);
		
		if(onChange != null)
			onChange.accept(this.value);
		
	}
	
	protected abstract E sanitizeValue(Object value)
			throws IllegalArgumentException;
	
	protected E formatEnvironment(E envValue) throws BadFormatException{
		return envValue;
	}
	
	@Override
	protected Setting<E> clone() throws CloneNotSupportedException{
		Setting<E> settingClone = new Setting<E>(name, env,
				defaultValue){
			@Override
			protected E sanitizeValue(Object value)
					throws IllegalArgumentException{
				return Setting.this.sanitizeValue(value);
			}
		};
		
		return settingClone;
	}
	
}
