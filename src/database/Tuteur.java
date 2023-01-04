package database;


import java.util.ArrayList;
import java.util.Collections;

import com.google.gson.reflect.TypeToken;

import utils.GsonUtils;
import utils.Keyboard;


/**
 * 
 * @author Malak Hayar, Maddy De Wancker, Gwendal Margely
 *
 */
public class Tuteur extends Etudiant {
	
	/////////////////
	//   Attribut  //
	/////////////////

	protected Ressource ressourceEnseigne;
	private static ArrayList<Etudiant> tuteurs = GsonUtils.deserializeEtudiant(GsonUtils.getListetuteurDir(), new TypeToken<ArrayList<Tuteur>>(){}.getType(), false);

	
	///////////////////
	//  Constructeur //
	///////////////////
	
	public Tuteur(String prenom, String nom,int annee, double moyenne, Motivation motivation, int nbrAbsences, Ressource ressourceEnseigne) {
		super(prenom,nom, annee, moyenne, motivation,nbrAbsences);
		this.ressourceEnseigne = ressourceEnseigne;
		getTuteurs().add(this); 
		Collections.sort(Tuteur.getTuteurs(), Collections.reverseOrder());
	}
	
	///////////////////
	//    M�thodes   //
	///////////////////

	
	public static void addTuteur() {
		System.out.println("Entrez le nom du tuteur :");
		String nom = Keyboard.readString();
		System.out.println("Entrez le prenom du tuteur :");
		String prenom = Keyboard.readString();
		System.out.println("Entrez son ann�e d'�tude :");
		int annee = Keyboard.customReadInt(2, 3);
		System.out.println("Entrez la moyenne du tuteur : (PS : Un tuteur doit au moins avoir la moyenne pour �tre tuteur. Si le tuteur a une note beaucoup plus basse que celle d'un tutor�, l'algorithme d'affectation risque de donner de mauvais r�sultats...)");
		int moyenne = Keyboard.customReadInt(10, 20);
		System.out.println("Dans quelle ressource fait-il tutorat : 1. Maths , 2. dev , 3. web , 4. Archi (Choisissez le num�ro correspondant)");
		int res = Keyboard.customReadInt(1, 4);
		System.out.println("Son niveau de motivation ? 1. FAIBLE , 2. MOYEN , 3. ELEVE");
		int motiv = Keyboard.customReadInt(1, 3);
		System.out.println("Son nombre d'absences au cours du dernier semestre:");
		int abs = Keyboard.positiveReadInt();
		new Tuteur(nom, prenom, annee, moyenne, Motivation.getByValue(motiv), abs, Ressource.getByValue(res));
		System.out.println("Tuteur ajout� avec succ�s");
	}
	
	public String toString() {
		return this.getNom().toUpperCase() + " " + this.getPrenom();
	}


	///////////////////////////////
	// 	   getters et setters    //
	///////////////////////////////
	
	public Ressource getRessourceEnseigne() {
		return ressourceEnseigne;
	}


	public void setRessourceEnseigne(Ressource ressourceEnseigne) {
		this.ressourceEnseigne = ressourceEnseigne;
	}


	public static ArrayList<Etudiant> getTuteurs() {
		return tuteurs;
	}
	public static void setTuteurs(ArrayList<Etudiant> tuteurs) {
		Tuteur.tuteurs = tuteurs;
	}



}
