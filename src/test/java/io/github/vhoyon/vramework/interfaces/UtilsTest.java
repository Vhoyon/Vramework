package io.github.vhoyon.vramework.interfaces;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

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
	
	@Test
	void testFormatCurlyBrackets(){
		
		String test = "function(){{1}}";
		
		String replaced = format(test, "/*code*/");
		
		String expected = "function(){/*code*/}";
		
		assertEquals(expected, replaced);
		
	}
	
	@Test
	void testFormatNonWantedFormat(){
		
		String test = "Type example this : {\\1}. {1}";
		
		String replaced = format(test, "Voila!");
		
		String expected = "Type example this : {1}. Voila!";
		
		assertEquals(expected, replaced);
		
		String test2 = "Type example this : {\\01}. {1}";
		
		String replaced2 = format(test2, "Voila!");
		
		String expected2 = "Type example this : {01}. Voila!";
		
		assertEquals(expected2, replaced2);
		
	}
	
	@Test
	void testFormatNonWantedFormatProtected(){
		
		String test = "Type example this : {\\1}. {1}";
		
		String replaced = format(test, "Voila!");
		
		String expected = "Type example this : {1}. Voila!";
		
		assertEquals(expected, replaced);
		
		String test2 = "Type example this : {\\\\1}. {1}";
		
		String replaced2 = format(test2, "Voila!");
		
		String expected2 = "Type example this : {\\1}. Voila!";
		
		assertEquals(expected2, replaced2);
		
	}
	
	@Test
	void testFormatMultipleSameNumber(){
		
		String test = "{1} {1} {1}";
		
		String replaced = format(test, 1, 2, 3);
		
		String expected = "1 1 1";
		
		assertEquals(expected, replaced);
		
	}
	
	@Test
	void testFormatSkipZeroIndexes(){
		
		String test = "{0} {1}";
		
		String replaced = format(test, 1, 2);
		
		String expected = "{0} 1";
		
		assertEquals(expected, replaced);
		
	}
	
}
