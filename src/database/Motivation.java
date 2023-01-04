package database;


/**
 * 
 * @author malak hayar, Gwendal Margely, Maddy De Wancker
 *
 */
public enum Motivation {
	
	//////////////////
	//  Attributs   //
	//////////////////

	FAIBLE(1),
	MOYEN(2),
	ELEVE(3);

	protected int level;

	//////////////////
	// Constructeur //
	//////////////////
	
	private Motivation(int level){
		this.level = level;
	}


	//////////////////
	//   Méthodes   //
	//////////////////

	
	public static Motivation getByValue(int value) {
		switch (value) {
		case 1: return FAIBLE;
		case 2: return MOYEN;
		case 3: return ELEVE;
		}
		return null;
	}
	
	public int getLevel() {
		return this.level;
	}
}