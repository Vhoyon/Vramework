package io.github.vhoyon.vramework.objects;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RequestTest {
	
	@Test
	void testWeightHandling(){
		
		Request testRequest = new Request("!test -abcd");
		
		testRequest.setParameterWeight("a", 1);
		testRequest.setParameterWeight("b", 2);
		testRequest.setParameterWeight("d", 3);
		
		int weightForA = testRequest.getParameter("a").getWeight();
		int weightForB = testRequest.getParameter("b").getWeight();
		int weightForC = testRequest.getParameter("c").getWeight();
		int weightForD = testRequest.getParameter("d").getWeight();
		
		assertEquals(1, weightForA);
		assertEquals(2, weightForB);
		assertEquals(0, weightForC);
		assertEquals(4, weightForD);
		
	}
	
	@Test
	void testWeightHandlingModifications(){
		
		Request testRequest = new Request("!test -abcd");
		
		testRequest.setParameterWeight("a", 1);
		testRequest.setParameterWeight("b", 2);
		testRequest.setParameterWeight("d", 3);
		
		testRequest.setParameterWeight("a", 3);
		testRequest.setParameterWeight("b", 1);
		testRequest.setParameterWeight("c", 6);
		testRequest.setParameterWeight("d", 2);
		
		int weightForAMod = testRequest.getParameter("a").getWeight();
		int weightForBMod = testRequest.getParameter("b").getWeight();
		int weightForCMod = testRequest.getParameter("c").getWeight();
		int weightForDMod = testRequest.getParameter("d").getWeight();
		
		assertEquals(4, weightForAMod);
		assertEquals(1, weightForBMod);
		assertEquals(8, weightForCMod);
		assertEquals(2, weightForDMod);
		
	}
	
}
