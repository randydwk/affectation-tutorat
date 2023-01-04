package database;

public class Couple{
	
	//////////////////
	//  Attributs   //
	//////////////////

	private Tuteur tuteurAffecte;
	private Tutore tutoreAffecte;
	private static int cpt = 0;
	private final String ID;

	//////////////////
	// Constructeur //
	//////////////////
	
	public Couple(Tuteur tuteur,  Tutore tutore) {
		this.tuteurAffecte = tuteur;
		this.tutoreAffecte = tutore;
		this.ID = "C" + cpt;
		cpt++;
	}
	
	//////////////////
	//   M�thodes   //
	//////////////////
	
	public String toString() {
		return this.tuteurAffecte + " - " + this.tutoreAffecte;
	}
	
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		Couple otherCouple = (Couple) obj;
		return (this.getTuteurAffecte().equals(otherCouple.getTuteurAffecte()) &&
				this.getTutoreAffecte().equals(otherCouple.getTutoreAffecte()));
	}
	
	
	///////////////////////////////
	// 	   getters et setters    //
	///////////////////////////////

	public Tuteur getTuteurAffecte() {
		return tuteurAffecte;
	}

	public void setTuteurAffecte(Tuteur tuteurAffecte) {
		this.tuteurAffecte = tuteurAffecte;
	}

	public Tutore getTutoreAffecte() {
		return tutoreAffecte;
	}

	public void setTutoreAffecte(Tutore tutoreAffecte) {
		this.tutoreAffecte = tutoreAffecte;
	}

	public static int getCpt() {
		return cpt;
	}

	public static void setCpt(int cpt) {
		Couple.cpt = cpt;
	}

	public String getID() {
		return this.ID;
	}
	
}
