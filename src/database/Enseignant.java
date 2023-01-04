package database;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import utils.DuplicateTeachersException;
import utils.ObjectNotFoundException;

/**
 * 
 * @author Malak Hayar, Maddy De Wancker, Gwendal Margely
 *
 */
public class Enseignant extends Personne implements Iterable<Enseignant> {
	
	
	//////////////////
	//  Attributs   //
	//////////////////

	
	private final String ID;
	private Ressource ressourceEnseigne;
	private static Set<Enseignant> listeEnseignants = new HashSet<Enseignant>();
	private static int numeroAutomatique = 1;


	//////////////////
	// Constructeur //
	//////////////////
	
	public Enseignant(String nom, String prenom, Ressource ressourceEnseigne) {
		super(nom, prenom);
		this.ID = super.getNom().substring(0, 3).toUpperCase() + super.prenom.substring(0, 3).toUpperCase() + numeroAutomatique;
		this.ressourceEnseigne = ressourceEnseigne;
		numeroAutomatique++;
		try {
			addEnseignant(this);
		} catch (DuplicateTeachersException d) {System.out.println("Il y'a dï¿½jï¿½ un enseignant du mï¿½me nom dans cette matiï¿½re");}
	}
	
	
	//////////////////
	//   Méthodes   //
	//////////////////

	public static void printTeacherList() {
		for (Enseignant e : listeEnseignants) {
			System.out.println(e);
		}
	}

	public String toString() {
		return this.ID + " , " + super.getNom() + " " + super.prenom + " , " + this.ressourceEnseigne;
	}



	public static void addEnseignant(Enseignant e) throws DuplicateTeachersException {
		if (Enseignant.listeEnseignants.contains(e)) throw new DuplicateTeachersException();
		else {Enseignant.listeEnseignants.add(e);};
	}


	public static Enseignant findTeacherByID(String id) throws ObjectNotFoundException {
		for (Enseignant e : listeEnseignants) {
			if (e.ID.equals(id)) return e;
		}
		throw new ObjectNotFoundException();
	}

	public static Enseignant findTeacherByRessource(Ressource res) throws ObjectNotFoundException {
		for (Enseignant e : listeEnseignants) {
			if (e.getRessourceEnseigne() == res) return e;
		}
		throw new ObjectNotFoundException();
	}


	public Iterator<Enseignant> iterator() {
		return listeEnseignants.iterator();
	}
	
	
	///////////////////////////////
	// 	   getters et setters    //
	///////////////////////////////

	public Ressource getRessourceEnseigne() {
		return this.ressourceEnseigne;
	}
	
	public void setRessourceEnseigne(Ressource res) {
		this.ressourceEnseigne = res;
	}

}


