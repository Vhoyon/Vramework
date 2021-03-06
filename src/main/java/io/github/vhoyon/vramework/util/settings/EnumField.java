package io.github.vhoyon.vramework.util.settings;

import java.util.ArrayList;
import java.util.List;

import io.github.ved.jsanitizers.EnumSanitizer;
import io.github.ved.jsanitizers.exceptions.BadFormatException;

public class EnumField extends TextField {
	
	protected class CaseArrayList extends ArrayList<String> {
		@Override
		public boolean contains(Object o){
			if(o == null || o instanceof String){
				String e = (String)o;
				return this.stream().anyMatch(
						s -> e == null ? s == null : e.equalsIgnoreCase(s));
			}
			return false;
		}
	}
	
	protected List<String> values;
	
	public EnumField(String name, String env, Object defaultValue,
			Object... otherValues){
		super(name, env, defaultValue.toString());
		
		this.values = this.getValuesList(defaultValue, otherValues);
	}
	
	@Override
	protected String sanitizeValue(Object value)
			throws IllegalArgumentException{
		return EnumSanitizer.sanitizeValue(value, this.values);
	}
	
	public List<String> getPossibleValues(){
		return this.values;
	}
	
	@Override
	protected String formatEnvironment(String envValue)
			throws BadFormatException{
		this.values = new CaseArrayList();
		
		this.values.addAll(EnumSanitizer.extractEnumFromString(envValue));
		
		return this.values.get(0);
	}
	
	private List<String> getValuesList(Object defaultValue,
			Object... otherValues){
		
		CaseArrayList newValues = new CaseArrayList();
		
		newValues.add(defaultValue.toString());
		
		for(Object otherValue : otherValues){
			newValues.add(otherValue.toString());
		}
		
		return newValues;
		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected EnumField duplicate(){
		EnumField cloned = new EnumField(getName(), getEnv(), getDefaultValue()){
			@Override
			protected String sanitizeValue(Object value)
					throws IllegalArgumentException{
				return EnumField.this.sanitizeValue(value);
			}
		};
		
		cloned.values = new ArrayList<>(this.values);
		
		return cloned;
	}
	
}
