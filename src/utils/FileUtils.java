package utils;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;



public class FileUtils {
	
	//////////////////
	//   Attribut   //
	//////////////////
	
	private final static String IEPATH = System.getProperty("user.dir") + File.separator +"res" + File.separator;
	
	
	//////////////////
	// Constructeur //
	//////////////////
	
	private FileUtils() { // private pour �viter une instanciation ailleurs
		
	}
	
	//////////////////
	//    M�thode   //
	//////////////////
	
	public static FileWriter createFile(String name) {
		FileWriter fw = null;
		try {
			 fw = new FileWriter(name);
		} catch (IOException e) {System.out.println("Une erreur s'est produite � la cr�ation du fichier");}
		return fw;
	}
	
	public static String getExportPath() {
		return FileUtils.IEPATH;
	}
	
	

	

	
	

	
	
	
	
	
	
}
