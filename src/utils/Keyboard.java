package utils;
import java.util.Scanner;

public class Keyboard{
	private final static Scanner sc = new Scanner(System.in);
	private final static String INVALID_MSG = "Valeur invalide, veuillez réessayer"; 

	// constructeur privée pour éviter une instanciation
	private Keyboard() {
	}
	
	// readString
	public static String readString(){
		return sc.nextLine();
	}

	public static String withoutSpecialCharacters() {
		String entry = "";
		boolean ok = false;
		while (!ok) {
			entry = Keyboard.readString();
			int i = 0;
			boolean specialChar = true;
			while (i < entry.length() && specialChar) {
				if (!Character.isDigit(entry.charAt(i)) && !Character.isLetter(entry.charAt(i)) && Character.isWhitespace(entry.charAt(i))) {
					System.out.println("Nom invalide, réessayez");
					specialChar = false;
				}
				i++;
			}
			ok = specialChar;
		}
		return entry;
	}

	// readInt
	public static int readInt(String invalidMsg){
		int res = 0;
		boolean ok = false;
		while (!ok) {
			try {
				res = Integer.parseInt(sc.nextLine());
				ok = true;
			} catch (NumberFormatException nfe) {
				System.out.println(invalidMsg);
			}
		}
		return res;
	}

	public static int readInt(){
		return readInt(INVALID_MSG);
	}

	public static int customReadInt(int min, int max, String invalidMsg) {
		int res = 0;
		boolean ok = false;
		while (!ok) {
			res = Keyboard.readInt();
			if (res >= min && res <= max) ok = true;
			else {System.out.println(invalidMsg);}
		}
		return res;
	}

	public static int customReadInt(int min, int max) {
		return customReadInt(min,max,INVALID_MSG);
	}

	public static int positiveReadInt() {
		int res = Keyboard.readInt();
		if (res > 0) return res;
		else {System.out.println("la valeur entrée est négative, veuillez réessayer"); return positiveReadInt();}
	}

	// readDouble
	public static double readDouble() {
		String str = readString();
		return Double.parseDouble(str); 
	}	

	// specific cases
	public static boolean theySaidOui() {
		return Keyboard.readString().equalsIgnoreCase("oui");
	}
}