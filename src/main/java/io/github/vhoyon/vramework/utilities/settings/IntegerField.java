package io.github.vhoyon.vramework.utilities.settings;

import io.github.vhoyon.vramework.utilities.sanitizers.IntegerSanitizer;

public class IntegerField extends SettingField<Integer> {
	
	protected int min;
	protected int max;
	
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
	
	@Override
	protected IntegerField clone() throws CloneNotSupportedException{
		IntegerField cloned = new IntegerField(getName(), getEnv(),
				getDefaultValue()){
			@Override
			protected Integer sanitizeValue(Object value)
					throws IllegalArgumentException{
				return IntegerField.this.sanitizeValue(value);
			}
		};
		
		cloned.min = this.min;
		cloned.max = this.max;
		
		return cloned;
	}
	
}
