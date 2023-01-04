package utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;



public class GsonUtils {
	
	//////////////////
	//   Attributs  //
	//////////////////

	private static GsonBuilder gsonBuilder = new GsonBuilder().serializeNulls().disableHtmlEscaping().enableComplexMapKeySerialization().setLenient();
	private static final String LISTETUTORE = FileUtils.getExportPath() + "listeTutore.json";
	private static final String LISTETUTEUR = FileUtils.getExportPath() + "listeTuteur.json";

	
	//////////////////
	// Constructeur //
	//////////////////
	
	private GsonUtils() { // constructeur private pour éviter une instanciation en dehors
		
	}
	
	//////////////////
	//   Méthodes   //
	//////////////////
	
	public static Gson getGson(boolean pretty) {
		return (pretty) ? gsonBuilder.setPrettyPrinting().create() : gsonBuilder.create();
	}


	public static <E> void serializeObject(ArrayList<E> list, String destFile, boolean pretty) {
		final FileWriter filwri = FileUtils.createFile(destFile);
		final StringBuilder objectsSB = new StringBuilder("");
		objectsSB.append(getGson(pretty).toJson(list));
		try (BufferedWriter writer = new BufferedWriter(filwri)) {
			writer.write(objectsSB.toString());
		} catch (IOException e) {System.out.println("Une erreur s'est produite");e.printStackTrace();}
		try {
			filwri.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static <E> ArrayList<E> deserializeEtudiant(String filePath, Type listType, boolean pretty) { // tuteur/tutore/couple
		ArrayList<E> myStudents = new ArrayList<>();
		//Type listType = new TypeToken<ArrayList<Tutore>>(){}.getType();
		String line = "";
		try (BufferedReader br = new BufferedReader(new FileReader(filePath))){
			if (br.ready() && (line = br.readLine()) != null) {
				myStudents = (getGson(pretty).fromJson(line, listType)); 
			}
			br.close();
		} catch (FileNotFoundException e) { System.out.println("Ce fichier n'existe pas");}
		catch (IOException e) {System.out.println("Erreur D'écriture");}
		return myStudents;
	}


	///////////////////////////////
	// 	   getters et setters    //
	///////////////////////////////
	
	
	public static String getListetutoreDir() {
		return LISTETUTORE;
	}

	public static String getListetuteurDir() {
		return LISTETUTEUR;
	}
}
