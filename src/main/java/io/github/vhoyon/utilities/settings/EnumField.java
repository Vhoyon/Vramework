package io.github.vhoyon.utilities.settings;

import io.github.vhoyon.exceptions.BadFormatException;
import io.github.vhoyon.utilities.sanitizers.EnumSanitizer;

import java.util.ArrayList;

public class EnumField extends TextField {
	
	protected class CaseArrayList extends ArrayList<String>{
        @Override
        public boolean contains(Object o){
            if(o == null || o instanceof String){
                
                String e = (String)o;
                
                for(String s : this){
                    boolean val = e == null ? s == null : e.equalsIgnoreCase(s);
                    
                    if(val)
                        return true;
                }
                
            }
            
            return false;
        }
    }
	
	protected ArrayList<String> values;
	
	public EnumField(String name, String env, Object defaultValue,
			Object... otherValues){
		super(name, env, defaultValue.toString());
		
		this.values = this.getValuesArrayList(defaultValue, otherValues);
	}
	
	@Override
	protected String sanitizeValue(Object value)
			throws IllegalArgumentException{
		return EnumSanitizer.sanitizeValue(value, this.values);
	}
	
	public ArrayList<String> getPossibleValues(){
		return this.values;
	}
	
	@Override
	protected String formatEnvironment(String envValue)
			throws BadFormatException{
		this.values = new CaseArrayList();
		
		this.values.addAll(EnumSanitizer.extractEnumFromString(envValue));
		
		return this.values.get(0);
	}
	
	private ArrayList<String> getValuesArrayList(Object defaultValue,
			Object... otherValues){
		
		CaseArrayList newValues = new CaseArrayList();
		
		newValues.add(defaultValue.toString());
		
		for(Object otherValue : otherValues){
			newValues.add(otherValue.toString());
		}
		
		return newValues;
		
	}
	
}
