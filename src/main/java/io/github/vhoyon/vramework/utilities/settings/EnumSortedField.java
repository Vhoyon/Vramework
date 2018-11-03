package io.github.vhoyon.vramework.utilities.settings;

import java.util.ArrayList;
import java.util.Collections;

public class EnumSortedField extends EnumField {
	
	public EnumSortedField(String name, String env, Object defaultValue,
			Object... otherValues){
		super(name, env, defaultValue, otherValues);
	}
	
	@Override
	public ArrayList<String> getPossibleValues(){
		ArrayList<String> sortedArray = super.getPossibleValues();
		Collections.sort(sortedArray);
		return sortedArray;
	}
	
	@Override
	protected EnumSortedField clone() throws CloneNotSupportedException{
		EnumSortedField cloned = new EnumSortedField(getName(), getEnv(),
				getDefaultValue()){
			@Override
			protected String sanitizeValue(Object value)
					throws IllegalArgumentException{
				return EnumSortedField.this.sanitizeValue(value);
			}
		};
		
		cloned.values = (ArrayList<String>)this.values.clone();
		
		return cloned;
	}
	
}
