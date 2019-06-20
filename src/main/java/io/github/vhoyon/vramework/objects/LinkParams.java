package io.github.vhoyon.vramework.objects;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import io.github.vhoyon.vramework.interfaces.LinkableCommand;

public class LinkParams extends Link {
	
	private Object[] params;
	
	public LinkParams(Link link, Object... params){
		super(link.getClassToLink());
		
		this.params = params;
	}
	
	@Override
	public LinkableCommand getInstance() throws IllegalStateException{
		
		Class<?>[] constructorClassesArray = Arrays.stream(params)
				.map(Object::getClass).toArray(Class<?>[]::new);
		
		try{
			return this.getClassToLink().getDeclaredConstructor(
					constructorClassesArray).newInstance(params);
		}
		catch(InstantiationException | IllegalAccessException
				| InvocationTargetException | NoSuchMethodException e){
			throw new IllegalStateException(
					"The command to link is not accessible nor instantiable!");
		}
		
	}
}
