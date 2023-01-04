package app;


import java.util.Arrays;
import java.util.LinkedList;

import affectation.Affectation;
import affectation.Data;
import database.Enseignant;
import database.Etudiant;
import database.Ressource;
import database.Tuteur;
import database.Tutore;
import utils.FileUtils;
import utils.GsonUtils;
import utils.Keyboard;
import utils.ObjectNotFoundException;

public class App {

	/////////////////
	//  Attributs  //
	/////////////////
	
	private static Enseignant utilisateur;
	private static boolean dejaAffecte = false;

	
	////////////////////
	// 	 main method  //
	////////////////////

	public static void main(String[] args) {
		initializeTeachers();
		System.out.println("				 				 Application tutorat			       ");
		System.out.println("	 				       créee par Hayar Malak, Maddy De Wancker, Gwendal Margely\n");
		Enseignant.printTeacherList();
		System.out.println("Entrez votre ID");
		boolean done = false;
		while (!done) {
			try {
				setUtilisateur(Enseignant.findTeacherByID(Keyboard.readString().toUpperCase()));
				done = true;
			} catch (ObjectNotFoundException e) {System.out.println("Cet ID ne correspond Ã  aucun professeur");}
		}
		System.out.println("Bienvenue Professeur.e " + utilisateur.getNom());
		Affectation f = new Affectation(utilisateur.getRessourceEnseigne());
		menu(f);
	}
	
	//////////////////
	//   Méthodes   //
	//////////////////
	
	public static void initializeTeachers() {
		new Enseignant("NonGaillard", "Antoine", Ressource.DEV);
		new Enseignant("Chlebowski", "Max", Ressource.MATHS);
		new Enseignant("Baste", "Julien", Ressource.ARCHI);
		new Enseignant("Delecroix", "Fabien", Ressource.DEV);
		new Enseignant("Carle", "Jean", Ressource.WEB);
		new Enseignant("Deletombe", "Marie", Ressource.MATHS);
	}

	public static void menu(Affectation f) {
		System.out.println("Que voulez vous faire ?");
		System.out.println("1 : Affecter des couples tuteurs/tutorés");
		System.out.println("2 : modifier la base de donnÃ©es");
		System.out.println("3 : Exporter la base de donnÃ©es");
		System.out.println("4 : Se déconnecter");
		int key = Keyboard.customReadInt(1,4,"Entrée non valide");
		switch (key) {
		case 1: // affecter les étudiants
			affectationMenu(f);
			break;
		case 2: // alteration des données
			modMenu(f);
			break;
		case 3: // exporter les données
			exportMenu(f);
			System.out.println("Fichiers enregistrés.\nAppuyez sur une touche pour revenir au menu principal");
			Keyboard.readString();
			menu(f);
			break;
		case 4: // se déconnecter
			System.out.println("Etes vous sûr.e de vouloir vous déconnecter ? (oui/non)");
			if (Keyboard.theySaidOui()) {System.out.println("En espérant vous revoir bientôt Professeur.e " + utilisateur.getNom());return;}
			main(null);
		}
	}

	private static void affectationMenu(Affectation f) {
		System.out.println("          Menu affectation           \n");
		System.out.println("1 : Affectation manuelle \n2 : Affectation algorithmique\nChoisissez le numÃ©ro correspondant");
		String res = Keyboard.readString();
		int aws = 0;
		switch (res) {
		case "1": 
			System.out.println("            Menu Affectation manuelle            \n");
			f.getBdd().manuallyAssign();
			System.out.println("appuyez sur une touche pour revenir au menu principal");
			Keyboard.readString();
			menu(f);
		case "2": 
			System.out.println("            Menu Affectation algorithmique            \n");
			System.out.println("                      Filtrage                      ");
			if (dejaAffecte) {
				System.out.println("Vous avez déjà  fais une affectation, voulez vous sauvegarder vos résultats avant de procéder Ã  une autre ? Attention : Si vous tapez 'non' , les données seront rénitialisées et vous perdrez vos résultats. (oui/non)");
				if (Keyboard.theySaidOui()) {
					exportMenu(f);
				}
				f.getBdd().resetData();
			}
			System.out.println("Voulez vous filtrer les tuteurs par moyenne ? (oui/non)");
			if (Keyboard.theySaidOui()) {
				System.out.println("Entrez la moyenne minimale (pas en dessous de 0..)"); int min = Keyboard.customReadInt(0, 20);
				f.getBdd().setTuteurs(f.filterByMoyenne(f.getBdd().getTuteurs(), min, 20));
			}
			System.out.println("Voulez vous filtrer les tutorés par moyenne ? (oui/non)");
			if (Keyboard.theySaidOui()) {
				System.out.println("Note : si vous mettez aux tutorés des notes supérieures aux tuteurs, ça risque de donner des résultats bizzares à l'affectation...");
				System.out.println("Entrez la moyenne maximale (pas supérieure a 20..)"); int max = Keyboard.customReadInt(0, 20);
				f.getBdd().setTutores(f.filterByMoyenne(f.getBdd().getTutores(), 0, max));
			}
			System.out.println("Voulez vous gardez les tuteurs par année (oui/non)");
			if (Keyboard.theySaidOui()) {
				System.out.println("Voulez vous gardez que les tuteurs de 3ème année ou de 2ème année ? ('2'/'3')");
				f.getBdd().setTuteurs(f.filterByAnnee(f.getBdd().getTuteurs(), Keyboard.customReadInt(2, 3)));
			}
			System.out.println("Voulez vous filtrer les tuteurs et les tutorés par leur nombre d'absences ? (oui/non)?");
			if (Keyboard.theySaidOui()) {
				System.out.println("Entrez le nombre d'absence maximal");
				aws = Keyboard.positiveReadInt();
				f.getBdd().setTuteurs(f.filterByAbsence(f.getBdd().getTuteurs(), aws));
				f.getBdd().setTutores(f.filterByAbsence(f.getBdd().getTutores(), aws));
			}
			System.out.println("                      		Choix des critères                      ");
			System.out.println("L'affectation se fait par défaut en fonction des moyennes des tuteurs/tutorés. Voulez vous rajouter d'autres critères ? (oui/non)");
			if (Keyboard.theySaidOui()) {
				LinkedList<String> criteres = new LinkedList<>(Arrays.asList("motivation", "absences", "annee"));
				boolean done = false;
				while (!criteres.isEmpty() && !done) {
					System.out.println("Choisissez le critère prioritaire parmis la liste : " + printLinkedList(criteres));
					String choix = Keyboard.readString().toLowerCase();
					if (criteres.contains(choix)) {
						criteres.remove(choix);
						if (choix.equals("motivation")) {
							f.getBdd().setTuteurs(Data.sortByMotivation(f.getBdd().getTuteurs()));
							f.getBdd().setTutores(Data.sortByMotivation(f.getBdd().getTutores()));
							System.out.println("Critère de motivation maintenant inclus");
						} else if (choix.equals("absences")) {
							f.getBdd().setTuteurs(Data.sortByAbsences(f.getBdd().getTuteurs(), aws));
							f.getBdd().setTutores(Data.sortByAbsences(f.getBdd().getTutores(), aws));
							System.out.println("Critère d'absence maintenant inclus");
						} else if (choix.equals("annee")) {
							f.getBdd().setTuteurs(Data.sortByYear(f.getBdd().getTuteurs()));
							f.getBdd().setTutores(Data.sortByYear(f.getBdd().getTutores()));
							System.out.println("Critère d'année d'étude maintenant inclus");}
					} else {System.out.println("Ce critère n'existe pas");}
					if (criteres.isEmpty()) done = true;
					else {
						System.out.println("Voulez encore choisir un critère ? (oui/non)");
						if (!Keyboard.theySaidOui()) done = true;
					}
				}
			}
			f.affectation(App.getUtilisateur().getRessourceEnseigne());
			dejaAffecte = true;
			System.out.println("appuyez sur une touche pour revenir au menu principal\n(PS : Si votre affectation vous convient, n'oubliez pas d'exporter vos donnï¿½es avant de refaire une nouvelle affectation");
			Keyboard.readString();
			menu(f);
		default:
			System.out.println("Entrée non valide, veuillez réessayer"); affectationMenu(f);
		}
	}

	private static void modMenu(Affectation f) {
		System.out.println("            Menu modification des donnï¿½es            \n");
		System.out.println("1 : Ajouter un tuteur \n" +
				"2 : Supprimer un tuteur \n" +
				"3 : Ajouter un tutoré \n" +
				"4 : Supprimer un tutoré\n" +
				"5 : Supprimer un couple \n" +
				"6 : Renitialiser les données \n" +
				"7 : Voir la liste de tuteurs en " + App.getUtilisateur().getRessourceEnseigne() + "\n" +
				"8 : Voir la liste totale de tuteurs\n" +
				"9 : Voir la liste des tutorés\n" + 
				"10 : Voir Les couples affectés\n"+
				"tapez le numéro correspondant");
		String res = Keyboard.readString();
		switch (res) {
		case "1":
			Tuteur.addTuteur();
			break;
		case "2":
			Tuteur.setTuteurs(Etudiant.removeManually(Tuteur.getTuteurs(), "tuteur"));
			break;
		case "3":
			Tutore.addTutore();
			break;
		case "4":
			Tuteur.setTuteurs(Etudiant.removeManually(Tuteur.getTuteurs(), "tuteur"));
			break;
		case "5":
			f.getBdd().removeCoupleByID();
			break;
		case "6":
			f.getBdd().resetData();
			System.out.println("données rénitialisées");
			break;
		case "7":
			Etudiant.printStudentList(f.getBdd().getTuteurs(), "tuteur");
			break;
		case "8": 
			Etudiant.printStudentList(Tuteur.getTuteurs(), "tuteur");
			break;
		case "9":
			Etudiant.printStudentList(Tutore.getTutores(), "tutoré");
			break;
		case "10": 
			f.getBdd().printCouples();
			break;
		default :
			System.out.println("Entrée non valide, veuillez réessayer");
			modMenu(f);
		}
		System.out.println("Appuyez sur une touche pour retourner au menu principal");
		Keyboard.readString();
		clearConsole();
		menu(f);
	}

	private static void exportMenu(Affectation f) {
		System.out.println("          Menu exportation des données           \n");
		GsonUtils.serializeObject(Tuteur.getTuteurs(), GsonUtils.getListetuteurDir(), false);
		GsonUtils.serializeObject(Tutore.getTutores(), GsonUtils.getListetutoreDir(), false);
		System.out.println("quel nom voulez vous donner au fichier contenant les couples tuteurs/tutorés ?");
		GsonUtils.serializeObject(f.getBdd().getAssignedCouples(), FileUtils.getExportPath() + Keyboard.withoutSpecialCharacters(), true);
		f.getBdd().resetData();
	}

	private static String printLinkedList(LinkedList<String> myList) {
		String res = " ";
		for (String s : myList) {
			res += s + " , ";
		}
		return res;
	}
	
	

	public static void clearConsole() {
		System.out.print("\033[H\033[2J");
	}
	
	
	///////////////////////////////
	// 	   getters et setters    //
	///////////////////////////////
	
	public static Enseignant getUtilisateur() {
		return utilisateur;
	}

	public static void setUtilisateur(Enseignant utilisateur) {
		App.utilisateur = utilisateur;
	}

	public static void setRessource(Ressource ressource) {
		try {
			setUtilisateur(Enseignant.findTeacherByRessource(ressource));
		} catch (ObjectNotFoundException e) {System.out.println("Ressource inconnue");}
	}


}
