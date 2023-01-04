package database;

/**
 * @author malak Hayar, Maddy De Wancker, Gwendal Margely
 *
 */
public class Personne {
	
	//////////////////
	//   Attributs  //
	//////////////////

	protected String nom;
	protected String prenom;


	//////////////////
	// Constructeur //
	//////////////////
	
	public Personne(String nom, String prenom) {
		this.nom = nom;
		this.prenom = prenom;
	}


	//////////////////
	//   M�thodes   //
	//////////////////
	
	public String getNom() {
		return nom;
	}


	public void setNom(String nom) {
		this.nom = nom;
	}
	
	public String getPrenom() {
		return prenom;
	}


	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}
	
}