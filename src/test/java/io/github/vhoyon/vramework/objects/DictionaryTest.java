package io.github.vhoyon.vramework.objects;

import io.github.vhoyon.vramework.exceptions.BadFormatException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class DictionaryTest {
	
	@Mock
	Dictionary mockDict;
	
	@BeforeEach
	void setUp(){
		mockDict = spy(Dictionary.class);
		
		doReturn("[0] Test|[1,2] Test {1}|[3,*] Tests {1}").when(mockDict)
				.getString(eq("consistentFormat"), nullable(String.class));
		
		doReturn("  [0]\tTest|  [1,2]Test {1}|[3,*]   \t  Tests {1}  ").when(
				mockDict).getString(eq("funkySpaces"), nullable(String.class));
		
		doReturn(
				"[*,-3] Negative Infinity | [-2, 2] Passing 0 | [3, *] Positive Infinity")
				.when(mockDict).getString(eq("infinities"),
						nullable(String.class));
		
		doReturn("[0,1] First | Second").when(mockDict).getString(
				eq("missingRange"), nullable(String.class));
		
		doReturn("[0,1] First|[2,12345678901234567890] Second").when(mockDict)
				.getString(eq("rangeTooBig"), nullable(String.class));
		
		doReturn("[0,3] First|[2,5] Second").when(mockDict).getString(
				eq("overlapping"), nullable(String.class));
		
	}
	
	@Test
	void testLangAmountConsistent0(){
		
		int expectedAmount = 0;
		
		String expectedLang = "Test";
		
		String lang = mockDict.getStringAmount("consistentFormat", null,
				expectedAmount);
		
		assertEquals(expectedLang, lang);
		
	}
	
	@Test
	void testLangAmountConsistent1And2(){
		
		int expectedAmount = 1;
		
		String expectedLang = "Test " + expectedAmount;
		
		String lang = mockDict.getStringAmount("consistentFormat", null,
				expectedAmount, expectedAmount);
		
		assertEquals(expectedLang, lang);
		
		expectedAmount = 2;
		
		expectedLang = "Test " + expectedAmount;
		
		lang = mockDict.getStringAmount("consistentFormat", null,
				expectedAmount, expectedAmount);
		
		assertEquals(expectedLang, lang);
		
	}
	
	@Test
	void testLangAmountConsistent3OrMore(){
		
		for(int i = 3; i < 10; i = i + 2){
			
			String expectedLang = "Tests " + i;
			
			String lang = mockDict.getStringAmount("consistentFormat", null, i,
					i);
			
			assertEquals(expectedLang, lang);
			
		}
		
	}
	
	@Test
	void testLangAmountFunkySpaces0(){
		
		int expectedAmount = 0;
		
		String expectedLang = "Test";
		
		String lang = mockDict.getStringAmount("funkySpaces", null,
				expectedAmount);
		
		assertEquals(expectedLang, lang);
		
	}
	
	@Test
	void testLangAmountFunkySpaces1And2(){
		
		int expectedAmount = 1;
		
		String expectedLang = "Test " + expectedAmount;
		
		String lang = mockDict.getStringAmount("funkySpaces", null,
				expectedAmount, expectedAmount);
		
		assertEquals(expectedLang, lang);
		
		expectedAmount = 2;
		
		expectedLang = "Test " + expectedAmount;
		
		lang = mockDict.getStringAmount("funkySpaces", null, expectedAmount,
				expectedAmount);
		
		assertEquals(expectedLang, lang);
		
	}
	
	@Test
	void testLangAmountFunkySpaces3OrMore(){
		
		for(int i = 3; i < 10; i = i + 2){
			
			String expectedLang = "Tests " + i;
			
			String lang = mockDict.getStringAmount("funkySpaces", null, i, i);
			
			assertEquals(expectedLang, lang);
			
		}
		
	}
	
	@Test
	void testLangAmountInfinitiesNegative(){
		
		String expectedLang = "Negative Infinity";
		
		for(int i = -30; i <= -3; i++){
			
			String lang = mockDict.getStringAmount("infinities", null, i);
			
			assertEquals(expectedLang, lang);
			
		}
		
	}
	
	@Test
	void testLangAmountInfinitiesNormal(){
		
		String expectedLang = "Passing 0";
		
		for(int i = -2; i <= 2; i++){
			
			String lang = mockDict.getStringAmount("infinities", null, i);
			
			assertEquals(expectedLang, lang);
			
		}
		
	}
	
	@Test
	void testLangAmountInfinities3OrMore(){
		
		String expectedLang = "Positive Infinity";
		
		for(int i = 30; i >= 3; i--){
			
			String lang = mockDict.getStringAmount("infinities", null, i);
			
			assertEquals(expectedLang, lang);
			
		}
		
	}
	
	@Test
	void testLangAmountMissingRange(){
		
		Executable shouldThrowBadFormatException = () -> mockDict
				.getStringAmount("missingRange", null, 1);
		
		BadFormatException exception = assertThrows(BadFormatException.class,
				shouldThrowBadFormatException);
		
		assertEquals(3, exception.getErrorCode());
		
	}
	
	@Test
	void testLangAmountRangeTooBig(){
		
		Executable shouldThrowBadFormatException = () -> mockDict
				.getStringAmount("rangeTooBig", null, 1);
		
		BadFormatException exception = assertThrows(BadFormatException.class,
				shouldThrowBadFormatException);
		
		assertEquals(4, exception.getErrorCode());
		
	}
	
	@Test
	void testLangAmountOverlapping(){
		
		Executable shouldThrowBadFormatException = () -> mockDict
				.getStringAmount("overlapping", null, 2);
		
		BadFormatException exception = assertThrows(BadFormatException.class,
				shouldThrowBadFormatException);
		
		assertEquals(5, exception.getErrorCode());
		
	}
	
}
