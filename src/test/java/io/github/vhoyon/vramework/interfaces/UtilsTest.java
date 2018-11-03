package io.github.vhoyon.vramework.interfaces;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UtilsTest implements Utils {
	
	@Test
	void testFormatPercentages(){
		
		String test = "The volume has been set to {1}%!";
		
		String replaced = format(test, 10);
		
		String expected = "The volume has been set to 10%!";
		
		assertEquals(expected, replaced);
		
	}
	
	@Test
	void testFormatParentheses(){
		
		String test = "Hello (World) {1}!";
		
		String replaced = format(test, "Justice");
		
		String expected = "Hello (World) Justice!";
		
		assertEquals(expected, replaced);
		
	}
	
	@Test
	void testFormatBrackets(){
		
		String test = "Hello [World] {1}!";
		
		String replaced = format(test, "Justice");
		
		String expected = "Hello [World] Justice!";
		
		assertEquals(expected, replaced);
		
	}
	
	@Test
	void testFormatDot(){
		
		String test = "Hello World {1}.";
		
		String replaced = format(test, "Justice");
		
		String expected = "Hello World Justice.";
		
		assertEquals(expected, replaced);
		
	}
	
	@Test
	void testFormatDollars(){
		
		String test = "{1}$";
		
		String replaced = format(test, 5);
		
		String expected = "5$";
		
		assertEquals(expected, replaced);
		
	}
	
}
