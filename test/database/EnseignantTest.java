package database;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import utils.ObjectNotFoundException;

public class EnseignantTest {
	static Enseignant n1;
	static Enseignant n2;

	@BeforeAll
	public static void beforeAllTests() {
		System.out.println("Start test series");
		n1 = new Enseignant("Albert","Einstein",Ressource.DEV);
		n2 = new Enseignant("René","Descartes",Ressource.ARCHI);
	}
	@AfterAll
	public static void afterAllTests() {System.out.println("End test series");}
	@BeforeEach
	public void beforeATest() {}
	@AfterEach
	public void afterATest() {System.out.println("--------- end of a test---------");}

	@Test
	public void testFindTeacherByID() throws ObjectNotFoundException {
		System.out.println("testFindTeacherByID");

		assertTrue(n1.equals(Enseignant.findTeacherByID("ALBEIN1")));
		assertTrue(n2.equals(Enseignant.findTeacherByID("RENDES2")));
	}
	
	@Test 
	public void testFindTeacherByRessource() throws ObjectNotFoundException {
		System.out.println("testFindTeacherByRessource");

		assertTrue(n1.equals(Enseignant.findTeacherByRessource(Ressource.DEV)));
		assertTrue(n2.equals(Enseignant.findTeacherByRessource(Ressource.ARCHI)));
	}
}