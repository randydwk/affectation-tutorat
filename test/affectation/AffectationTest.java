package affectation;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import app.App;
import database.*;

public class AffectationTest {
	static Affectation f;
	static Tuteur t1;
	static Tuteur t2;
	static Tuteur t3;
	static Tutore tr1;
	static Tutore tr2;
	static Tutore tr3;
	
	@BeforeAll
	public static void beforeAllTests() {
		System.out.println("Start test series");
		
		// Ici on se concentre sur la ressource DEV mais cela pourrait etre n'importe laquelle
		App.setRessource(Ressource.DEV);		
	}
	@AfterAll
	public static void afterAllTests() {System.out.println("End test series");}
	@BeforeEach
	public void beforeATest() {
		f = new Affectation(Ressource.DEV);
		
		ArrayList<Etudiant> tuteurs = new ArrayList<Etudiant>();
		t1 = new Tuteur("a", "A", 2, 17, Motivation.ELEVE, 1, Ressource.DEV);
		t2 = new Tuteur("b", "B", 3, 13, Motivation.MOYEN, 2, Ressource.DEV);
		t3 = new Tuteur("c", "C", 2, 14, Motivation.ELEVE, 0, Ressource.DEV);
		tuteurs.add(t1);
		tuteurs.add(t2);
		tuteurs.add(t3);
		f.getBdd().setTuteurs(tuteurs);
		
		ArrayList<Etudiant> tutores = new ArrayList<Etudiant>();
		
		HashMap<Ressource,Double> notesX = new HashMap<Ressource,Double>();
		notesX.put(Ressource.DEV,12.0);
		tr1 = new Tutore("x","X",15,Motivation.MOYEN,3,notesX);
		
		HashMap<Ressource,Double> notesY = new HashMap<Ressource,Double>();
		notesY.put(Ressource.DEV,9.0);
		tr2 = new Tutore("y","Y",13,Motivation.FAIBLE,1,notesY);
		
		HashMap<Ressource,Double> notesZ = new HashMap<Ressource,Double>();
		notesZ.put(Ressource.DEV,10.0);
		tr3 = new Tutore("z","Z",11,Motivation.ELEVE,1,notesZ);
		
		tutores.add(tr1);
		tutores.add(tr2);
		tutores.add(tr3);
		f.getBdd().setTutores(tutores);
	}
	@AfterEach
	public void afterATest() {System.out.println("--------- end of a test---------");}
	
	private void assertCouple(ArrayList<Couple> assignedCouples, Tuteur tuteurAffecte, Tutore tutoreAffecte) {
		boolean res = false;
		int i = 0;
		Couple c = new Couple(tuteurAffecte,tutoreAffecte);
		while (!res && i < assignedCouples.size()) {
			if (assignedCouples.get(i).equals(c)) {
				res = true;
			}
			i++;
		}
		assertTrue(res);
	}
	
	@Test
	public void testAffectationMoyenne() {
		System.out.println("testAffectationMoyenne");
		f.affectation(Ressource.DEV);
		
		//Résultats attendus
		assertCouple(f.getBdd().getAssignedCouples(),t2,tr1);
		assertCouple(f.getBdd().getAssignedCouples(),t3,tr2);
		assertCouple(f.getBdd().getAssignedCouples(),t1,tr3);
	}
	@Test
	public void testAffectationMotivation() {
		System.out.println("testAffectationMotivation");
		
		f.getBdd().setTuteurs(Data.sortByMotivation(f.getBdd().getTuteurs()));
		f.getBdd().setTutores(Data.sortByMotivation(f.getBdd().getTutores()));
		f.affectation(Ressource.DEV);
		
		//Résultats attendus
		assertCouple(f.getBdd().getAssignedCouples(),t1,tr1);
		assertCouple(f.getBdd().getAssignedCouples(),t3,tr2);
		assertCouple(f.getBdd().getAssignedCouples(),t2,tr3);
	}
	@Test
	public void testAffectationAbsences() {
		System.out.println("testAffectationAbsences");
		
		f.getBdd().setTuteurs(Data.sortByAbsences(f.getBdd().getTuteurs(), 99));
		f.getBdd().setTutores(Data.sortByAbsences(f.getBdd().getTutores(), 99));
		f.affectation(Ressource.DEV);
		
		//Résultats attendus
		assertCouple(f.getBdd().getAssignedCouples(),t2,tr1);
		assertCouple(f.getBdd().getAssignedCouples(),t1,tr3);
		assertCouple(f.getBdd().getAssignedCouples(),t3,tr2);
	}
	@Test
	public void testAffectationAnnee() {
		System.out.println("testAffectationAnnee");
		
		f.getBdd().setTuteurs(Data.sortByYear(f.getBdd().getTuteurs()));
		f.getBdd().setTutores(Data.sortByYear(f.getBdd().getTutores()));
		f.affectation(Ressource.DEV);
		
		//Résultats attendus
		assertCouple(f.getBdd().getAssignedCouples(),t1,tr1);
		assertCouple(f.getBdd().getAssignedCouples(),t2,tr3);
		assertCouple(f.getBdd().getAssignedCouples(),t3,tr2);
	}
	@Test
	public void testFiltreMoyenne() {
		System.out.println("testFiltreMoyenne");
		
		f.getBdd().setTuteurs(f.filterByMoyenne(f.getBdd().getTuteurs(), 15, 20));
		assertTrue(f.getBdd().getTuteurs().size() == 1);
		
		f.getBdd().setTutores(f.filterByMoyenne(f.getBdd().getTutores(), 0, 13));
		assertTrue(f.getBdd().getTutores().size() == 2);
	}
	@Test
	public void testFiltreAnnee() {
		System.out.println("testFiltreAnnee");
		
		f.getBdd().setTuteurs(f.filterByAnnee(f.getBdd().getTuteurs(), 2));
		assertTrue(f.getBdd().getTuteurs().size() == 2);
	}
	@Test
	public void testFiltreAbsences() {
		System.out.println("testFiltreAbsences");
		
		f.getBdd().setTuteurs(f.filterByAbsence(f.getBdd().getTuteurs(), 1));
		assertTrue(f.getBdd().getTuteurs().size() == 2);
		
		f.getBdd().setTutores(f.filterByAbsence(f.getBdd().getTutores(), 1));
		assertTrue(f.getBdd().getTutores().size() == 2);
	}
	@Test
	public void testStudentAlreadyExists() {
		System.out.println("testStudentAlreadyExists");
		
		assertTrue(f.getBdd().studentAlreadyExists(t1));
		assertFalse(f.getBdd().studentAlreadyExists(new Tuteur("d","D",0,0, Motivation.MOYEN,0, Ressource.WEB)));
	}
}