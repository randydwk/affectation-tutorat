package database;

import java.util.ArrayList;

import utils.Keyboard;
import utils.ObjectNotFoundException;

/**
 * @author malak Hayar, Maddy De Wancker, Gwendal Margely
 *
 */
public class Etudiant extends Personne implements Comparable<Etudiant>  {
	
	//////////////////
	//  Attributs   //
	//////////////////
	
	private int annee; 
	private double moyenne;
	private Motivation motivation;
	private int nbrAbsences;
	protected final String ID;
	private static int numeroAutomatique = 1;


	//////////////////
	// Constructeur //
	//////////////////

	
	public Etudiant(String prenom, String nom, int annee, double moyenne, Motivation motivation,int nbrAbsences){
		super(nom, prenom);
		this.setAnnee(annee);
		this.setMoyenne(moyenne);
		this.setMotivation(motivation);
		this.setNbrAbsences(Math.abs(nbrAbsences));
		this.ID = super.nom.substring(0,3).toUpperCase() + super.prenom.substring(0, 3).toUpperCase() + numeroAutomatique;
		numeroAutomatique++;
		
	}
	
	
	//////////////////
	//   Méthodes   //
	//////////////////

	
	public static Etudiant findStudentByID(ArrayList <Etudiant> etu, String id) throws ObjectNotFoundException  {
		if (thereIsNoStudents(etu)) throw new ObjectNotFoundException();
		for (int i = 0; i < etu.size(); i++) {
			if (etu.get(i).ID.equals(id)) {
				return etu.get(i);
			}
		} 
		throw new ObjectNotFoundException();
	}

	public static boolean thereIsNoStudents(ArrayList<Etudiant> etu) {
		return etu.isEmpty();
	}



	public static void printStudentList(ArrayList<Etudiant> liste, String type) {
		System.out.println("Liste des " + type + " :");
		for (int i =0; i < liste.size(); i++) {
			System.out.println(liste.get(i).toString());
		}
		System.out.println();
	}
	
	

	public static ArrayList <Etudiant> removeManually(ArrayList <Etudiant> etu, String type){
		System.out.println("Voulez vous enlever manuellement des " + type + "s ? (oui/non)");
		boolean passed = false;
		if (Keyboard.theySaidOui()) {
			do {
				System.out.println("Tapez l'identifiant du " + type);
				try {
					Etudiant e = Etudiant.findStudentByID(etu, Keyboard.readString());
					passed = true;
					etu.remove(e);
					System.out.println(type + " supprimé avec succès");
				} catch (ObjectNotFoundException s) { System.out.println("Ce " + type + " n'existe pas"); }
				System.out.println("Voulez vous encore supprimer des " + type + "s ? (tapez 'stop' si non)");
				if (Keyboard.readString().toLowerCase().equals("stop")) break;
			} while (!passed);
			Etudiant.printStudentList(etu, type);
		}
		return etu;
	}

	
	public int compareTo(Etudiant etu){
		return Integer.compare((int) this.getMoyenne(),(int) etu.getMoyenne());
	}


	public String toString() {
		return "Etudiant :" + this.prenom + " " + this.getNom();
	}


	@Override
	public boolean equals(Object obj) { // automatically generated method
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Etudiant other = (Etudiant) obj;
		if (getAnnee() != other.getAnnee())
			return false;
		if (Double.doubleToLongBits(getMoyenne()) != Double.doubleToLongBits(other.getMoyenne()))
			return false;
		if (getNom() == null) {
			if (other.getNom() != null)
				return false;
		} else if (!getNom().equals(other.getNom()))
			return false;
		if (prenom == null) {
			if (other.prenom != null)
				return false;
		} else if (!prenom.equals(other.prenom))
			return false;
		return true;
	}



	///////////////////////////////
	// 	   getters et setters    //
	///////////////////////////////
	

	public int getAnnee() {
		return annee;
	}


	public void setAnnee(int annee) {
		this.annee = annee;
	}


	public Motivation getMotivation() {
		return motivation;
	}


	public void setMotivation(Motivation motivation) {
		this.motivation = motivation;
	}


	public int getNbrAbsences() {
		return nbrAbsences;
	}


	public void setNbrAbsences(int nbrAbsences) {
		this.nbrAbsences = nbrAbsences;
	}


	public double getMoyenne() {
		return moyenne;
	}


	public void setMoyenne(double moyenne) {
		this.moyenne = moyenne;
	}
	









}