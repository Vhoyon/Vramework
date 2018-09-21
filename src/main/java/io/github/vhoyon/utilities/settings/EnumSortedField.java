package io.github.vhoyon.utilities.settings;

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
	
}
