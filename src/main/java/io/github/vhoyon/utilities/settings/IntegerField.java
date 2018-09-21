package io.github.vhoyon.utilities.settings;

import io.github.vhoyon.utilities.sanitizers.IntegerSanitizer;

public class IntegerField extends SettingField<Integer> {
	
	private int min;
	private int max;
	
	public IntegerField(String name, String env, int defaultValue){
		this(name, env, defaultValue, Integer.MIN_VALUE, Integer.MAX_VALUE);
	}
	
	public IntegerField(String name, String env, int defaultValue,
			int minValue, int maxValue){
		super(name, env, defaultValue);
		
		this.min = minValue;
		this.max = maxValue;
	}
	
	@Override
	protected Integer sanitizeValue(Object value)
			throws IllegalArgumentException{
		return IntegerSanitizer.sanitizeValue(value, this.min, this.max);
	}
	
}
