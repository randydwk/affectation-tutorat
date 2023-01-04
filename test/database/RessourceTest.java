package database;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RessourceTest {

	@BeforeAll
	public static void beforeAllTests() {System.out.println("Start test series");}
	@AfterAll
	public static void afterAllTests() {System.out.println("End test series");}
	@BeforeEach
	public void beforeATest() {}
	@AfterEach
	public void afterATest() {System.out.println("--------- end of a test---------");}
		
	@Test
	public void testGetByValue() {
		System.out.println("testAffectationMoyenne");
		
		assertTrue(Ressource.getByValue(1).equals(Ressource.MATHS));
		assertTrue(Ressource.getByValue(2).equals(Ressource.DEV));
		assertTrue(Ressource.getByValue(3).equals(Ressource.WEB));
		assertFalse(Ressource.getByValue(59).equals(null));
		
	}
	
}
