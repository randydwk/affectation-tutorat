package affectation;
import java.util.ArrayList;

import database.*;
import utils.Keyboard;
import utils.ObjectNotFoundException;




public class Data{ 

	/////////////////
	//  Attributs  //
	/////////////////
	
	Ressource ressourceConcerne;
	private  ArrayList<Etudiant> tuteurs;
	private ArrayList<Etudiant> tutores;
	protected ArrayList<Couple> assignedCouples;

	
	//////////////////
	// Constructeur //
	//////////////////
	
	public Data(Ressource ressourceConcerne){
		this.tuteurs = getTutorsOfThisRessource(ressourceConcerne);
		this.tutores = Tutore.getTutores();
		this.assignedCouples = new ArrayList<Couple>();
		this.ressourceConcerne = ressourceConcerne;

	}

	
	//////////////////
	//   Méthodes   //
	//////////////////


	public static ArrayList<Etudiant> getTutorsOfThisRessource(Ressource res){
		ArrayList<Etudiant> newList = new ArrayList<>();
		for (int i = 0; i < Tuteur.getTuteurs().size(); i++) {
			if (((Tuteur) Tuteur.getTuteurs().get(i)).getRessourceEnseigne() == res) {
				newList.add(Tuteur.getTuteurs().get(i));
			}
		}
		return newList;
	}



	public static ArrayList<Etudiant> sortByYear(ArrayList<Etudiant> tuteurs){ 
		ArrayList<Etudiant> sortedTuteurs = new ArrayList<>();
		for (int i = 0; i < tuteurs.size(); i++) {
			if (tuteurs.get(i).getAnnee() == 3) {
				sortedTuteurs.add(tuteurs.get(i));
			}
		}
		for (int j = 0; j < tuteurs.size(); j++) {
			if (tuteurs.get(j).getAnnee() != 3) {
				sortedTuteurs.add(tuteurs.get(j));
			}
		}
		return sortedTuteurs;

	}

	public static ArrayList<Etudiant> sortByMotivation(ArrayList<Etudiant> etudiants){
		ArrayList<Etudiant> sortedEtudiants = new ArrayList<>();
		for (int i = 0; i < etudiants.size(); i++){
			if (etudiants.get(i).getMotivation() == Motivation.ELEVE){
				sortedEtudiants.add(etudiants.get(i));
			}
		}

		for (int i = 0; i < etudiants.size(); i++){
			if (etudiants.get(i).getMotivation() == Motivation.MOYEN){
				sortedEtudiants.add(etudiants.get(i));
			}
		}

		for (int i = 0; i < etudiants.size(); i++){
			if (etudiants.get(i).getMotivation() == Motivation.FAIBLE){
				sortedEtudiants.add(etudiants.get(i));
			}
		}

		return sortedEtudiants;
	}


	public static ArrayList<Etudiant> sortByAbsences(ArrayList<Etudiant> etudiants, int maxAbsence){
		ArrayList<Etudiant> sortedEtudiants = new ArrayList<>();
		int cptAbsences = 0;
		while (cptAbsences <= maxAbsence) {
			for (int i = 0; i < etudiants.size(); i++) {
				if (etudiants.get(i).getNbrAbsences() == cptAbsences) {
					sortedEtudiants.add(etudiants.get(i));
				}
			}
			cptAbsences++;
		}
		return sortedEtudiants;

	}


	public boolean studentAlreadyExists(Etudiant e) {
		return this.getTuteurs().contains(e) || this.getTutores().contains(e);
	}


	public void resetData() {
		this.setTuteurs(Tuteur.getTuteurs());
		this.tuteurs = getTutorsOfThisRessource(ressourceConcerne);
		this.setTutores(Tutore.getTutores());
		this.clearCoupleLists();
	}



	public void clearCoupleLists() {
		this.assignedCouples.clear();
		Couple.setCpt(0);
	}


	public Couple findCoupleByID(String id) throws ObjectNotFoundException  {
		for (Couple c : this.assignedCouples) {
			if (c.getID().equals(id)) return c;
		}
		throw new ObjectNotFoundException();
	}


	public void removeCoupleByID() {
		System.out.println("\nVoulez vous supprimer un couple formé ? (oui/non)");
		if (Keyboard.readString().toLowerCase().equals("oui")) {
			boolean done = false;
			String id = "";
			while (!done) {
				try {
					this.printCouples();
					System.out.println("Entrez l'ID du couple");
					id = Keyboard.readString();
					this.assignedCouples.remove(findCoupleByID(id));
					done = true;
				} catch (ObjectNotFoundException o) {System.out.println("il n'y a pas de couple avec l'ID " + id);}
			}
		}
	}


	public void printCouples() {
		if (!assignedCouples.isEmpty()) {
			System.out.println("Liste des couples affectés");
			for (int i = 0; i < assignedCouples.size(); i++) {
				System.out.println(this.assignedCouples.get(i));
			}
		} else { System.out.println("La liste est vide"); }
	}


	public void manuallyAssign() {
		boolean passed = false;
		Etudiant.printStudentList(this.getTuteurs(), "tuteurs");
		Etudiant.printStudentList(this.getTutores(), "tutores");
		System.out.println("voulez vous affecter manuellement des couples tuteurs/tutorés ? (oui/non)");
		if (Keyboard.theySaidOui()) {
			do {
				try {
					System.out.println("Tapez l'identifiant du tuteur");
					Etudiant tuteur = Etudiant.findStudentByID(tuteurs, Keyboard.readString());
					System.out.println("Tapez l'identifiant du tutoré");
					Etudiant tutore = Etudiant.findStudentByID(tutores, Keyboard.readString());
					this.getAssignedCouples().add(new Couple((Tuteur) tuteur,(Tutore) tutore));
					System.out.println("assignation faite");
					this.getTuteurs().remove(tuteur);
					this.getTutores().remove(tutore);
					passed = true;
				} catch (ObjectNotFoundException e) {System.out.println("Affectation impossible, etudiant(s) non présent dans la base de donnée");}
				System.out.println("Voulez vous encore affecter des tuteurs/tutores ? (tapez 'stop' si non)");
				if(Keyboard.readString().equalsIgnoreCase("stop")) {System.out.println("fin de l'assignation manuelle"); break;}
			} while (!passed);
		} else {
			System.out.println("Pas d'assignation manuelle");
		}
		this.printCouples();
	}
	
	
	///////////////////////////////
	// 	   getters et setters    //
	///////////////////////////////
	
	public ArrayList<Couple> getAssignedCouples() {
		return this.assignedCouples;
	}	

	public void setAssignedCouples(ArrayList<Couple> assignedCouples) {
		this.assignedCouples = assignedCouples;
	}


	public ArrayList<Etudiant> getTuteurs() {
		return this.tuteurs;
	}

	public void setTuteurs(ArrayList<Etudiant> tute) {
		this.tuteurs = tute;
	}

	public ArrayList<Etudiant> getTutores() {
		return this.tutores;
	}

	public void setTutores(ArrayList<Etudiant> tuto) {
		this.tutores = tuto;
	}


}