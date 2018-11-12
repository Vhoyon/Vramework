package io.github.vhoyon.vramework.objects;

import io.github.vhoyon.vramework.exceptions.AmountNotDefinedException;
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
		
		doReturn("[0] Test|[1,2] Test {1}|[3,*] Tests {1}")
				.when(mockDict)
				.getString(eq("amountConsistentFormat"), nullable(String.class));
		
		doReturn("  [0]\tTest|  [1,2]Test {1}|[3,*]   \t  Tests {1}  ").when(
				mockDict).getString(eq("amountFunkySpaces"),
				nullable(String.class));
		
		doReturn(
				"[*,-3] Negative Infinity | [-2, 2] Passing 0 | [3, *] Positive Infinity")
				.when(mockDict).getString(eq("amountInfinities"),
						nullable(String.class));
		
		doReturn("[0] 0 : {0}|[1,2] 1 or 2 : {0}|[3,*] 3 or more : {0}").when(
				mockDict).getString(eq("amountReplaceCount"),
				nullable(String.class));
		
		doReturn(
				"[0] 0 : {\\0}|[1,2] 1 or 2 : {\\\\0}|[3,*] 3 or more : {\\\\\\0}")
				.when(mockDict).getString(eq("amountReplaceCountProtected"),
						nullable(String.class));
		
		doReturn("[1] {0} : {1}|[2] {0} : {1} {2}").when(mockDict).getString(
				eq("amountReplaceCountReplacements"), nullable(String.class));
		
		doReturn("[1] {\\0} : {1}|[2] {\\\\0} : {1} {2}").when(mockDict)
				.getString(eq("amountReplaceCountReplacementsProtected"),
						nullable(String.class));
		
		doReturn("Single|Plural {0}").when(mockDict).getString(
				eq("amountSingleAndPlural"), nullable(String.class));
		
		doReturn("Single \\| Still Single").when(mockDict).getString(
				eq("amountSingleProtectedSeparator"), nullable(String.class));
		
		doReturn("[0,1] First | Second").when(mockDict).getString(
				eq("amountMissingRange"), nullable(String.class));
		
		doReturn("[0,1] First|[2,12345678901234567890] Second").when(mockDict)
				.getString(eq("amountRangeTooBig"), nullable(String.class));
		
		doReturn("[0,1] First|[3,2] Second").when(mockDict).getString(
				eq("amountInverted"), nullable(String.class));
		
		doReturn("[0,3] First|[2,5] Second").when(mockDict).getString(
				eq("amountOverlapping"), nullable(String.class));
		
	}
	
	@Test
	void testLangAmountConsistent0(){
		
		int expectedAmount = 0;
		
		String expectedLang = "Test";
		
		String lang = mockDict.getStringAmount("amountConsistentFormat", null,
				expectedAmount);
		
		assertEquals(expectedLang, lang);
		
	}
	
	@Test
	void testLangAmountConsistent1And2(){
		
		int expectedAmount = 1;
		
		String expectedLang = "Test " + expectedAmount;
		
		String lang = mockDict.getStringAmount("amountConsistentFormat", null,
				expectedAmount, expectedAmount);
		
		assertEquals(expectedLang, lang);
		
		expectedAmount = 2;
		
		expectedLang = "Test " + expectedAmount;
		
		lang = mockDict.getStringAmount("amountConsistentFormat", null,
				expectedAmount, expectedAmount);
		
		assertEquals(expectedLang, lang);
		
	}
	
	@Test
	void testLangAmountConsistent3OrMore(){
		
		for(int i = 3; i < 10; i = i + 2){
			
			String expectedLang = "Tests " + i;
			
			String lang = mockDict.getStringAmount("amountConsistentFormat",
					null, i, i);
			
			assertEquals(expectedLang, lang);
			
		}
		
	}
	
	@Test
	void testLangAmountFunkySpaces0(){
		
		int expectedAmount = 0;
		
		String expectedLang = "Test";
		
		String lang = mockDict.getStringAmount("amountFunkySpaces", null,
				expectedAmount);
		
		assertEquals(expectedLang, lang);
		
	}
	
	@Test
	void testLangAmountFunkySpaces1And2(){
		
		int expectedAmount = 1;
		
		String expectedLang = "Test " + expectedAmount;
		
		String lang = mockDict.getStringAmount("amountFunkySpaces", null,
				expectedAmount, expectedAmount);
		
		assertEquals(expectedLang, lang);
		
		expectedAmount = 2;
		
		expectedLang = "Test " + expectedAmount;
		
		lang = mockDict.getStringAmount("amountFunkySpaces", null,
				expectedAmount, expectedAmount);
		
		assertEquals(expectedLang, lang);
		
	}
	
	@Test
	void testLangAmountFunkySpaces3OrMore(){
		
		for(int i = 3; i < 10; i = i + 2){
			
			String expectedLang = "Tests " + i;
			
			String lang = mockDict.getStringAmount("amountFunkySpaces", null,
					i, i);
			
			assertEquals(expectedLang, lang);
			
		}
		
	}
	
	@Test
	void testLangAmountInfinitiesNegative(){
		
		String expectedLang = "Negative Infinity";
		
		for(int i = -30; i <= -3; i++){
			
			String lang = mockDict.getStringAmount("amountInfinities", null, i);
			
			assertEquals(expectedLang, lang);
			
		}
		
	}
	
	@Test
	void testLangAmountInfinitiesNormal(){
		
		String expectedLang = "Passing 0";
		
		for(int i = -2; i <= 2; i++){
			
			String lang = mockDict.getStringAmount("amountInfinities", null, i);
			
			assertEquals(expectedLang, lang);
			
		}
		
	}
	
	@Test
	void testLangAmountInfinities3OrMore(){
		
		String expectedLang = "Positive Infinity";
		
		for(int i = 30; i >= 3; i--){
			
			String lang = mockDict.getStringAmount("amountInfinities", null, i);
			
			assertEquals(expectedLang, lang);
			
		}
		
	}
	
	@Test
	void testLangAmountReplaceCount(){
		
		int expectedAmount = 0;
		
		String expectedLang = "0 : 0";
		
		String lang = mockDict.getStringAmount("amountReplaceCount", null,
				expectedAmount);
		
		assertEquals(expectedLang, lang);
		
		int expectedAmount2 = 1;
		
		String expectedLang2 = "1 or 2 : 1";
		
		String lang2 = mockDict.getStringAmount("amountReplaceCount", null,
				expectedAmount2);
		
		assertEquals(expectedLang2, lang2);
		
		int expectedAmount3 = 4;
		
		String expectedLang3 = "3 or more : 4";
		
		String lang3 = mockDict.getStringAmount("amountReplaceCount", null,
				expectedAmount3);
		
		assertEquals(expectedLang3, lang3);
		
	}
	
	@Test
	void testLangAmountReplaceCountReplacements(){
		
		int expectedAmount = 1;
		
		String replacement1 = "hello";
		
		String expectedLang = "1 : " + replacement1;
		
		String lang = mockDict.getStringAmount(
				"amountReplaceCountReplacements", null, expectedAmount,
				replacement1);
		
		assertEquals(expectedLang, lang);
		
		expectedAmount = 2;
		
		replacement1 = "hello";
		String replacement2 = "world";
		
		expectedLang = "2 : " + replacement1 + " " + replacement2;
		
		lang = mockDict.getStringAmount("amountReplaceCountReplacements", null,
				expectedAmount, replacement1, replacement2);
		
		assertEquals(expectedLang, lang);
		
	}
	
	@Test
	void testLangAmountReplaceCountProtected(){
		
		int expectedAmount = 0;
		
		String expectedLang = "0 : {0}";
		
		String lang = mockDict.getStringAmount("amountReplaceCountProtected",
				null, expectedAmount);
		
		assertEquals(expectedLang, lang);
		
		int expectedAmount2 = 1;
		
		String expectedLang2 = "1 or 2 : {\\0}";
		
		String lang2 = mockDict.getStringAmount("amountReplaceCountProtected",
				null, expectedAmount2);
		
		assertEquals(expectedLang2, lang2);
		
		int expectedAmount3 = 4;
		
		String expectedLang3 = "3 or more : {\\\\0}";
		
		String lang3 = mockDict.getStringAmount("amountReplaceCountProtected",
				null, expectedAmount3);
		
		assertEquals(expectedLang3, lang3);
		
	}
	
	@Test
	void testLangAmountReplaceCountReplacementsProtected(){
		
		int expectedAmount = 1;
		
		String replacement1 = "hello";
		
		String expectedLang = "{0} : " + replacement1;
		
		String lang = mockDict.getStringAmount(
				"amountReplaceCountReplacementsProtected", null,
				expectedAmount, replacement1);
		
		assertEquals(expectedLang, lang);
		
		expectedAmount = 2;
		
		replacement1 = "hello";
		String replacement2 = "world";
		
		expectedLang = "{\\0} : " + replacement1 + " " + replacement2;
		
		lang = mockDict.getStringAmount(
				"amountReplaceCountReplacementsProtected", null,
				expectedAmount, replacement1, replacement2);
		
		assertEquals(expectedLang, lang);
		
	}
	
	@Test
	void testLangAmountSingleAndPlural(){
		
		int expectedAmount = 1;
		
		String expectedLang = "Single";
		
		String lang = mockDict.getStringAmount("amountSingleAndPlural", null,
				expectedAmount);
		
		assertEquals(expectedLang, lang);
		
		for(int i = 2; i <= 5; i++){
			
			String expectedLangPlural = "Plural " + i;
			
			String langPlural = mockDict.getStringAmount(
					"amountSingleAndPlural", null, i);
			
			assertEquals(expectedLangPlural, langPlural);
			
		}
		
		Executable shouldThrowBadFormatException = () -> mockDict
				.getStringAmount("amountSingleAndPlural", null, 0);
		
		assertThrows(AmountNotDefinedException.class,
				shouldThrowBadFormatException);
		
	}
	
	@Test
	void testLangAmountSingleProtectedSeparator(){
		
		int expectedAmount = 1;
		
		String expectedLang = "Single \\| Still Single";
		
		String lang = mockDict.getStringAmount(
				"amountSingleProtectedSeparator", null, expectedAmount);
		
		assertEquals(expectedLang, lang);
		
	}
	
	@Test
	void testLangAmountMissingRange(){
		
		Executable shouldThrowBadFormatException = () -> mockDict
				.getStringAmount("amountMissingRange", null, 1);
		
		BadFormatException exception = assertThrows(BadFormatException.class,
				shouldThrowBadFormatException);
		
		assertEquals(3, exception.getErrorCode());
		
	}
	
	@Test
	void testLangAmountRangeTooBig(){
		
		Executable shouldThrowBadFormatException = () -> mockDict
				.getStringAmount("amountRangeTooBig", null, 1);
		
		BadFormatException exception = assertThrows(BadFormatException.class,
				shouldThrowBadFormatException);
		
		assertEquals(4, exception.getErrorCode());
		
	}
	
	@Test
	void testLangAmountInverted(){
		
		Executable shouldThrowBadFormatException = () -> mockDict
				.getStringAmount("amountInverted", null, 2);
		
		BadFormatException exception = assertThrows(BadFormatException.class,
				shouldThrowBadFormatException);
		
		assertEquals(5, exception.getErrorCode());
		
	}
	
	@Test
	void testLangAmountOverlapping(){
		
		Executable shouldThrowBadFormatException = () -> mockDict
				.getStringAmount("amountOverlapping", null, 2);
		
		BadFormatException exception = assertThrows(BadFormatException.class,
				shouldThrowBadFormatException);
		
		assertEquals(6, exception.getErrorCode());
		
	}
	
}
