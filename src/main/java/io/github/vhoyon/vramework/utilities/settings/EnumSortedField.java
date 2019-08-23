package io.github.vhoyon.vramework.utilities.settings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EnumSortedField extends EnumField {
	
	public EnumSortedField(String name, String env, Object defaultValue,
			Object... otherValues){
		super(name, env, defaultValue, otherValues);
	}
	
	@Override
	public List<String> getPossibleValues(){
		List<String> sortedArray = super.getPossibleValues();
		Collections.sort(sortedArray);
		return sortedArray;
	}
	
	@Override
	protected EnumSortedField duplicate(){
		EnumSortedField cloned = new EnumSortedField(getName(), getEnv(),
				getDefaultValue()){
			@Override
			protected String sanitizeValue(Object value)
					throws IllegalArgumentException{
				return EnumSortedField.this.sanitizeValue(value);
			}
		};
		
		cloned.values = new ArrayList<>(this.values);
		
		return cloned;
	}
	
}
