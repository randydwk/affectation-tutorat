package database;


/**
 * 
 * @author Malak Hayar, Maddy De Wancker, Gwendal Margely
 *
 */
public enum Ressource {
	
	////////////////
	//  Attributs //
	////////////////
	
	MATHS(1), DEV(2), WEB(3), ARCHI(4); 

	protected int id;

	////////////////////
	//  Constructeur  //
	////////////////////
	
	
	private Ressource(int id){
		this.id = id;
	}

	////////////////////
	//     M�thode    //
	////////////////////
	
	public static Ressource getByValue(int value) {
		switch (value) {
		case 1: return MATHS;
		case 2: return DEV;
		case 3: return WEB;
		case 4: return ARCHI;
		}
		return null;
	}
	
	public int getID() {
		return this.id;
	}
}
