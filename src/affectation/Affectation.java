package affectation;

import java.util.ArrayList;
import java.util.List;

import database.Couple;
import database.Etudiant;
import database.Ressource;
import database.Tuteur;
import database.Tutore;
import fr.ulille.but.sae2_02.graphes.*;

public class Affectation {

	//////////////////
	//   Attributs  //
	//////////////////

	private GrapheNonOrienteValue<Etudiant> graph;
	protected Data bdd;


	//////////////////
	// Constructeur //
	//////////////////

	public Affectation(Ressource res){
		this.graph = new GrapheNonOrienteValue<>();
		this.setBdd(new Data(res));
	}


	//////////////////
	//   Méthodes   //
	//////////////////


	public void defineSommets() {
		int maxSize;
		if (this.bdd.getTuteurs().size() > this.getBdd().getTutores().size()) {
			maxSize = this.getBdd().getTutores().size();
		} else maxSize = this.getBdd().getTuteurs().size();

		for (int i = 0; i < maxSize; i++) {
			graph.ajouterSommet(this.getBdd().getTuteurs().get(i));	
		}
		for (int j = 0; j < maxSize; j++) {
			graph.ajouterSommet(this.getBdd().getTutores().get(j));
		}
	}



	public void defineAretes(Ressource res) {
		int i, j;
		for (i = 0; i <  this.getBdd().getTuteurs().size() ; i++) {
			double arete = 0;
			for (j = 0; j <  this.getBdd().getTuteurs().size(); j++) {
				arete = arete - (((Tutore) this.getBdd().getTutores().get(i)).getNotesRessources().get(res) - this.getBdd().getTuteurs().get(j).getMoyenne());
				graph.ajouterArete(this.getBdd().getTuteurs().get(i), this.getBdd().getTutores().get(j), arete);
			}
		}
	}

	// Retourne une plus petite ArrayList oï¿½ on copie des elements d'une plus grandes Arraylist en parametres dans une intervalle donnee
	public static ArrayList<Etudiant> copyOfRangeArrayList(ArrayList<Etudiant> ours, int from, int to) {
		if (to > ours.size()) to = ours.size(); // on se protège du nullPointerExceptions :)
		ArrayList<Etudiant> created = new ArrayList<>();
		for (int i = from; i < to; i++) {
			created.add(ours.get(i));
		}
		return created;
	}



	public ArrayList<Etudiant> filterByMoyenne(ArrayList<Etudiant> etudiants ,int minMoyenne, int maxMoyennne){
		ArrayList<Etudiant> finalList = new ArrayList<Etudiant>();
		for (int i = 0; i < etudiants.size(); i++) {
			if (etudiants.get(i).getMoyenne() >= minMoyenne && etudiants.get(i).getMoyenne() <= maxMoyennne) {
				finalList.add(etudiants.get(i));
			}
		}
		return finalList;
	}

	public ArrayList<Etudiant> filterByAbsence(ArrayList<Etudiant> etudiants, int maxAbsences){
		ArrayList<Etudiant> finalList = new ArrayList<Etudiant>();
		for (int i = 0; i < etudiants.size(); i++) {
			if (etudiants.get(i).getNbrAbsences() <= maxAbsences) {
				finalList.add(etudiants.get(i));
			}
		}
		return finalList;
	}

	public ArrayList<Etudiant> filterByAnnee(ArrayList<Etudiant> tuteurs,int annee){
		ArrayList<Etudiant> finalList = new ArrayList<Etudiant>();
		for (int i = 0; i < tuteurs.size(); i++) {

			if (tuteurs.get(i).getAnnee() == annee) {
				finalList.add(tuteurs.get(i));
			}
		}
		return finalList;
	}


	public void affectation(Ressource ress) {
		boolean finished = false;
		if (Etudiant.thereIsNoStudents(this.getBdd().getTuteurs()) || Etudiant.thereIsNoStudents(this.getBdd().getTuteurs())) {
			System.out.println("Pas d'etudiants affectables");
			return;
		}
		CalculAffectation<Etudiant> calcul;
		int i = 1;
		do {
			this.getBdd().setTuteurs(copyOfRangeArrayList(this.getBdd().getTuteurs(), 0, this.getBdd().getTutores().size()));
			ArrayList<Etudiant> temp_tutores = copyOfRangeArrayList(this.getBdd().getTutores(), 0, this.getBdd().getTuteurs().size());
			this.graph = new GrapheNonOrienteValue<>();
			this.defineSommets();
			this.defineAretes(ress);
			calcul = new CalculAffectation<Etudiant>(this.graph, this.getBdd().getTuteurs(), temp_tutores);
			List<Arete<Etudiant>> res = calcul.getAffectation();
			this.getcoupleTutores(res); // assignation : done
			System.out.println("----------------\n affectation " + i + "\n----------------\n" + "\n\n" + res);
			System.out.println();
			if (this.getBdd().getTutores().size() > this.getBdd().getTuteurs().size()) {
				this.getBdd().setTutores(copyOfRangeArrayList(this.getBdd().getTutores(), this.getBdd().getTuteurs().size(), this.getBdd().getTutores().size()));
				temp_tutores = this.getBdd().getTutores();
				this.getBdd().setTuteurs(copyOfRangeArrayList(this.getBdd().getTuteurs(), 0, this.getBdd().getTutores().size()));
			} else finished = true;
			i++;
		}while (!finished);


	}


	public void getcoupleTutores(List<Arete<Etudiant>> res) {
		for (Arete<Etudiant> a : res) {
			this.getBdd().assignedCouples.add(new Couple((Tuteur) a.getExtremite1(), (Tutore) a.getExtremite2()));
		}
	}

	///////////////////////////////
	// 	   getters et setters    //
	///////////////////////////////
	
	public Data getBdd() {
		return bdd;
	}

	public void setBdd(Data bdd) {
		this.bdd = bdd;
	}



}
