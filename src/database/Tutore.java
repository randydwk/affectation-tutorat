package database;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.reflect.TypeToken;

import utils.GsonUtils;
import utils.Keyboard;

public class Tutore extends Etudiant {


	//////////////////
	//   Attributs  //
	//////////////////
	
	private static ArrayList<Etudiant> tutores = GsonUtils.deserializeEtudiant(GsonUtils.getListetutoreDir(), new TypeToken<ArrayList<Tutore>>(){}.getType(), false);
	private HashMap<Ressource,Double> notesRessources;

	//////////////////
	// Constructeur //
	//////////////////
	
	public Tutore(String prenom, String nom, double moyenne, Motivation motivation, int nbrAbsences, HashMap<Ressource,Double> notesRessources) {
		super(prenom,nom, 1, moyenne, motivation, nbrAbsences);
		this.setNotesRessources(notesRessources);
		getTutores().add(this);
	}

	//////////////////
	//   M�thodes   //
	//////////////////

	public static void addTutore(Tutore c) {
		getTutores().add(c);
	}

	public static HashMap<Ressource, Double> randomputInReleve(HashMap<Ressource, Double> releveDeNote, int high){
		releveDeNote.put(Ressource.ARCHI, (Math.random()*high));
		releveDeNote.put(Ressource.WEB,(Math.random()*high));
		releveDeNote.put(Ressource.DEV,(Math.random()*high));
		releveDeNote.put(Ressource.MATHS,(Math.random()*high));
		return releveDeNote;
	}
	
	public static void manualPutIntReleve(HashMap<Ressource, Double> notesRes,Ressource ressource, Double note){
		if (!notesRes.containsKey(ressource)) notesRes.put(ressource, note);
	}



	public String toString() {
		return this.getNom().toUpperCase() + " " + this.getPrenom();
	}
	
	public static void addTutore() {
		System.out.println("Entrez le nom du tutor� :");
		String nom = Keyboard.readString();
		System.out.println("Entrez le prenom du tutor� :");
		String prenom = Keyboard.readString();
		System.out.println("Entrez la moyenne du tutor� :");
		int moyenne = Keyboard.customReadInt(0, 20);
		System.out.println("Son niveau de motivation ? 1. FAIBLE , 2. MOYEN , 3. ELEVE");
		int motiv = Keyboard.customReadInt(1, 3);
		System.out.println("Son nombre d'absences au cours du dernier semestre:");
		int abs = Keyboard.positiveReadInt();
		HashMap<Ressource, Double> releve  = new HashMap<>();
		System.out.println("Voulez vous un remplissage automatique du releve [Utile en cas de flemme..] au lieu d'ajouter chaque note du tutor� � la main ? (oui/non)");
		if (Keyboard.theySaidOui()) {releve = Tutore.randomputInReleve(releve, 13);}
		else {
			System.out.println("Note de Dev :");
			Tutore.manualPutIntReleve(releve, Ressource.DEV, Keyboard.readDouble());
			System.out.println("Note de Maths :");
			Tutore.manualPutIntReleve(releve, Ressource.MATHS, Keyboard.readDouble());
			System.out.println("Note de web :");
			Tutore.manualPutIntReleve(releve, Ressource.WEB, Keyboard.readDouble());
			System.out.println("Note d'archi :");
			Tutore.manualPutIntReleve(releve, Ressource.ARCHI, Keyboard.readDouble());
		}
		new Tutore(nom, prenom, moyenne, Motivation.getByValue(motiv), abs, releve);
		System.out.println("Tutor� ajout� avec succ�s");
	}


	///////////////////////////////
	// 	   getters et setters    //
	///////////////////////////////
	
	public static ArrayList<Etudiant> getTutores() {
		return tutores;
	}


	public static void setTutores(ArrayList<Etudiant> tutores) {
		Tutore.tutores = tutores;
	}


	public HashMap<Ressource,Double> getNotesRessources() {
		return notesRessources;
	}


	public void setNotesRessources(HashMap<Ressource,Double> notesRessources) {
		this.notesRessources = notesRessources;
	}




}