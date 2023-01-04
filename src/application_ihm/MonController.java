package application_ihm;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

import affectation.Affectation;
import affectation.Data;
import app.App;
import database.Couple;
import database.Critere;
import database.Etudiant;
import database.Motivation;
import database.Ressource;
import database.Tuteur;
import database.Tutore;
import javafx.animation.FadeTransition;
import javafx.beans.value.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.util.Duration;
import utils.*;

public class MonController {

	// Ressource
	@FXML
	private ChoiceBox<Ressource> filter_matiere;

	// Filtres
	@FXML
	private Spinner<Integer> filter_moy_min_tuteur;
	@FXML
	private Spinner<Integer> filter_moy_max_tuteur;
	@FXML
	private Spinner<Integer> filter_moy_min_tutore;
	@FXML
	private Spinner<Integer> filter_moy_max_tutore;
	@FXML
	private Spinner<Integer> filter_abs_tuteur;
	@FXML
	private Spinner<Integer> filter_abs_tutore;
	@FXML
	private RadioButton filter_annee_tous;
	@FXML
	private RadioButton filter_annee_2;
	@FXML
	private RadioButton filter_annee_3;

	// Affectation
	@FXML
	private CheckBox critere_annee;
	@FXML
	private CheckBox critere_motivation;
	@FXML
	private CheckBox critere_absences;
	private ArrayList<Critere> ordreCriteres = new ArrayList<Critere>();
	@FXML
	private Button button_affecter;
	@FXML
	private Button button_lier;
	private ArrayList<Affectation> affectations;
	private Affectation currentAffectation;
	@FXML
	private ListView<Couple> liste_couples;
	private ArrayList<Couple> affectationsManuelles = new ArrayList<Couple>();
	@FXML
	private Button button_aff_export;
	private TextInputDialog tid;

	// Liste Tuteurs
	@FXML
	private ListView<Etudiant> liste_tuteur;
	private Etudiant currentTuteur;
	@FXML
	private Button button_new_tuteur;

	// Liste Tutores
	@FXML
	private ListView<Etudiant> liste_tutore;
	private Etudiant currentTutore;
	@FXML
	private Button button_new_tutore;

	// Infos Tuteurs
	@FXML
	private Button button_remove_tuteur;
	@FXML
	private VBox vbox_tuteur;
	@FXML
	private TextField info_tuteur_prenom;
	@FXML
	private TextField info_tuteur_nom;
	@FXML
	private RadioButton info_tuteur_annee_2;
	@FXML
	private RadioButton info_tuteur_annee_3;
	@FXML
	private ChoiceBox<Ressource> info_tuteur_matiere;
	@FXML
	private Slider info_tuteur_motiv;
	@FXML
	private TextField info_tuteur_moy;
	@FXML
	private Spinner<Integer> info_tuteur_abs;
	private SpinnerValueFactory<Integer> info_tuteur_abs_fact;
	private final static int MAX_ABS = 999;
	private static boolean userAction = true;

	// Infos Tutorés
	@FXML
	private Button button_remove_tutore;
	@FXML
	private VBox vbox_tutore;
	@FXML
	private TextField info_tutore_prenom;
	@FXML
	private TextField info_tutore_nom;
	@FXML
	private Slider info_tutore_motiv;
	@FXML
	private Label label_tutore_res;
	@FXML
	private TextField info_tutore_moy_res;
	@FXML
	private TextField info_tutore_moy;
	@FXML
	private Spinner<Integer> info_tutore_abs;
	private SpinnerValueFactory<Integer> info_tutore_abs_fact;

	public void initialize() {
		System.out.println("Initialisation...");

		initializeAffectations();
		App.initializeTeachers();
		setInterfaceRessource(Ressource.MATHS);

		//-----------//
		// RESSOURCE //
		//-----------//

		filter_matiere.getItems().setAll(Ressource.values());
		filter_matiere.setValue(Ressource.MATHS);

		filter_matiere.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Ressource>() {
			@Override
			public void changed(ObservableValue<? extends Ressource> arg0, Ressource arg1, Ressource arg2) {
				setInterfaceRessource(arg2);
				filtrageTuteur();
				filtrageTutore();
				affectationsManuelles.clear();
				liste_couples.getItems().clear();
				liste_couples.setDisable(true);
				button_aff_export.setDisable(true);
			}
		});

		//---------//
		// TUTEURS //
		//---------//

		// Animation Bordure

		FadeTransition fadeBorderTuteur = new FadeTransition(Duration.seconds(0.5),new Button());
		fadeBorderTuteur.currentTimeProperty().addListener(new ChangeListener<Duration>() {

			@Override
			public void changed(ObservableValue<? extends Duration> arg0, Duration arg1, Duration arg2) {
				double width = Math.round((500-(arg2.toMillis()))/500*6);
				vbox_tuteur.setStyle("-fx-background-color:#E0E0E0; -fx-background-radius:24; -fx-border-color:#2cb1ff; -fx-border-radius:21; -fx-border-insets:"+(5-width)+"; -fx-border-width:"+width+";");
			}
		});

		// Prénom

		info_tuteur_prenom.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
				if (userAction) {
					currentTuteur.setPrenom(arg2);
					updateListeTuteurKeepSelection();
					exportTuteur();
				}
			}
		});

		// Nom

		info_tuteur_nom.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
				if (userAction) {
					currentTuteur.setNom(arg2);
					updateListeTuteurKeepSelection();
					exportTuteur();
				}
			}
		});

		// Année

		info_tuteur_annee_2.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> arg0, Boolean arg1, Boolean arg2) {
				if (userAction) {
					currentTuteur.setAnnee(2);
					exportTuteur();
					if (filter_annee_3.isSelected()) {
						filtrageTuteur();
					}
				}
			}
		});

		info_tuteur_annee_3.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> arg0, Boolean arg1, Boolean arg2) {
				if (userAction) {
					currentTuteur.setAnnee(3);
					exportTuteur();
					if (filter_annee_2.isSelected()) {
						filtrageTuteur();
					}
				}
			}
		});

		// Ressource Enseignée

		info_tuteur_matiere.getItems().setAll(Ressource.values());

		info_tuteur_matiere.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Ressource>() {
			@Override
			public void changed(ObservableValue<? extends Ressource> arg0, Ressource arg1, Ressource arg2) {
				if (userAction) {
					((Tuteur) currentTuteur).setRessourceEnseigne(arg2);
					exportTuteur();
					filtrageTuteur();
					tuteurSetDisable(true);
				}
			}
		});

		// Motivation

		info_tuteur_motiv.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
				if (userAction) {
					currentTuteur.setMotivation(Motivation.getByValue(arg2.intValue()));
					exportTuteur();
				}
			}
		});

		// Moyenne

		info_tuteur_moy.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
				if (userAction) {
					try {
						Double note = Double.parseDouble(arg2);
						if (note > 20) note = 20.0;
						currentTuteur.setMoyenne(note);
					} catch (NumberFormatException nfe) {
						System.out.println("Nombre incorrect");
						currentTuteur.setMoyenne(0);
					}
					exportTuteur();
				}
			}
		});

		info_tuteur_moy.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> arg0, Boolean arg1, Boolean arg2) {
				if (!arg2 && (currentTuteur.getMoyenne() > filter_moy_max_tuteur.getValue() || currentTuteur.getMoyenne() < filter_moy_min_tuteur.getValue())) {
					filtrageTuteur();
				}
			}
		});

		// Absences

		info_tuteur_abs_fact = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,MAX_ABS);
		info_tuteur_abs_fact.setValue(0);
		info_tuteur_abs.setValueFactory(info_tuteur_abs_fact);

		info_tuteur_abs.valueProperty().addListener(new ChangeListener<Integer>() {
			@Override
			public void changed(ObservableValue<? extends Integer> arg0, Integer arg1, Integer arg2) {
				if (userAction) {
					currentTuteur.setNbrAbsences(arg2);
					exportTuteur();
					if (arg2 > filter_abs_tuteur.getValue()) {
						filtrageTuteur();
					}
				}
			}
		});

		// Liste des Tuteurs

		liste_tuteur.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				if (currentTuteur != null && info_tuteur_prenom.isDisable()) tuteurSetDisable(false);
				updateInfosTuteur();
			}
		});

		liste_tuteur.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Etudiant>() {

			@Override
			public void changed(ObservableValue<? extends Etudiant> arg0, Etudiant arg1, Etudiant arg2) {
				if (info_tuteur_prenom.isDisable()) tuteurSetDisable(false);

				currentTuteur = liste_tuteur.getSelectionModel().getSelectedItem();
				updateInfosTuteur();
				updateBoutonLier();
				fadeBorderTuteur.stop();
				if (liste_tuteur.getSelectionModel().getSelectedIndex() != -1) fadeBorderTuteur.play();
			}
		});

		// Ajouter / Supprimer des Tuteurs

		button_new_tuteur.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				addTuteur();
			}
		});

		button_remove_tuteur.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				removeTuteur();
			}
		});

		//---------//
		// TUTORES //
		//---------//

		// Animation Bordure

		FadeTransition fadeBorderTutore = new FadeTransition(Duration.seconds(0.5),new Button());
		fadeBorderTutore.currentTimeProperty().addListener(new ChangeListener<Duration>() {

			@Override
			public void changed(ObservableValue<? extends Duration> arg0, Duration arg1, Duration arg2) {
				double width = Math.round((500-(arg2.toMillis()))/500*6);
				vbox_tutore.setStyle("-fx-background-color:#E0E0E0; -fx-background-radius:24; -fx-border-color:#2cb1ff; -fx-border-radius:21; -fx-border-insets:"+(5-width)+"; -fx-border-width:"+width+";");
			}
		});

		// Prénom

		info_tutore_prenom.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
				if (userAction) {
					currentTutore.setPrenom(arg2);
					updateListeTutoreKeepSelection();
					exportTutore();
				}
			}
		});

		// Nom

		info_tutore_nom.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
				if (userAction) {
					currentTutore.setNom(arg2);
					updateListeTutoreKeepSelection();
					exportTutore();
				}
			}
		});

		// Moyenne Ressource

		info_tutore_moy_res.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
				if (userAction) {
					try {
						Double note = Double.parseDouble(arg2);
						Tutore ttr = (Tutore) currentTutore;
						if (note > 20) note = 20.0;
						ttr.getNotesRessources().replace(filter_matiere.getValue(),note);
					} catch (NumberFormatException nfe) {
						System.out.println("Nombre incorrect");
						currentTutore.setMoyenne(0);
					}
					exportTutore();
				}
			}
		});

		// Motivation

		info_tutore_motiv.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
				if (userAction) {
					currentTutore.setMotivation(Motivation.getByValue(arg2.intValue()));
					exportTutore();
				}
			}
		});

		// Moyenne

		info_tutore_moy.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
				if (userAction) {
					try {
						Double note = Double.parseDouble(arg2);
						if (note <= 20) if (note > 20) note = 20.0;
						currentTutore.setMoyenne(note);
					} catch (NumberFormatException nfe) {
						System.out.println("Nombre incorrect");
						currentTutore.setMoyenne(0);
					}
					exportTutore();
				}
			}
		});

		info_tutore_moy.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> arg0, Boolean arg1, Boolean arg2) {
				if (!arg2 && (currentTutore.getMoyenne() > filter_moy_max_tutore.getValue() || currentTutore.getMoyenne() < filter_moy_min_tutore.getValue())) {
					filtrageTutore();
				}
			}
		});

		// Absences

		info_tutore_abs_fact = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,MAX_ABS);
		info_tutore_abs_fact.setValue(0);
		info_tutore_abs.setValueFactory(info_tutore_abs_fact);

		info_tutore_abs.valueProperty().addListener(new ChangeListener<Integer>() {
			@Override
			public void changed(ObservableValue<? extends Integer> arg0, Integer arg1, Integer arg2) {
				if (userAction) {
					currentTutore.setNbrAbsences(arg2);
					exportTutore();
					if (arg2 > filter_abs_tutore.getValue()) {
						filtrageTutore();
					}
				}
			}
		});

		// Liste des Tutorés

		liste_tutore.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Etudiant>() {

			@Override
			public void changed(ObservableValue<? extends Etudiant> arg0, Etudiant arg1, Etudiant arg2) {
				if (info_tutore_prenom.isDisable()) {tutoreSetDisable(false);}
				currentTutore = liste_tutore.getSelectionModel().getSelectedItem();
				updateInfosTutore();
				updateBoutonLier();
				fadeBorderTutore.stop();
				if (liste_tutore.getSelectionModel().getSelectedIndex() != -1) fadeBorderTutore.play();
			}
		});

		// Ajouter / Supprimer des Tutorés

		button_new_tutore.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				addTutore();
			}
		});

		button_remove_tutore.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				removeTutore();
			}
		});

		//---------//
		// FILTRES //
		//---------//

		// Filtre Moyenne Tuteurs

		SpinnerValueFactory<Integer> filter_moy_min_tuteur_fact = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,20);
		filter_moy_min_tuteur_fact.setValue(0);
		filter_moy_min_tuteur.setValueFactory(filter_moy_min_tuteur_fact);

		SpinnerValueFactory<Integer> filter_moy_max_tuteur_fact = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,20);
		filter_moy_max_tuteur_fact.setValue(20);
		filter_moy_max_tuteur.setValueFactory(filter_moy_max_tuteur_fact);

		filter_moy_min_tuteur.valueProperty().addListener(new ChangeListener<Integer>() {
			@Override
			public void changed(ObservableValue<? extends Integer> arg0, Integer arg1, Integer arg2) {
				if (filter_moy_min_tuteur.getValue() > filter_moy_max_tuteur.getValue()) {
					filter_moy_max_tuteur_fact.setValue(filter_moy_min_tuteur.getValue());
				}
				filtrageTuteur();
			}
		});

		filter_moy_max_tuteur.valueProperty().addListener(new ChangeListener<Integer>() {
			@Override
			public void changed(ObservableValue<? extends Integer> arg0, Integer arg1, Integer arg2) {
				if (filter_moy_max_tuteur.getValue() < filter_moy_min_tuteur.getValue()) {
					filter_moy_min_tuteur_fact.setValue(filter_moy_max_tuteur.getValue());
				}
				filtrageTuteur();
			}
		});

		// Filtre Moyenne Tutorés

		SpinnerValueFactory<Integer> filter_moy_min_tutore_fact = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,20);
		filter_moy_min_tutore_fact.setValue(0);
		filter_moy_min_tutore.setValueFactory(filter_moy_min_tutore_fact);

		SpinnerValueFactory<Integer> filter_moy_max_tutore_fact = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,20);
		filter_moy_max_tutore_fact.setValue(20);
		filter_moy_max_tutore.setValueFactory(filter_moy_max_tutore_fact);

		filter_moy_min_tutore.valueProperty().addListener(new ChangeListener<Integer>() {
			@Override
			public void changed(ObservableValue<? extends Integer> arg0, Integer arg1, Integer arg2) {
				if (filter_moy_min_tutore.getValue() > filter_moy_max_tutore.getValue()) {
					filter_moy_max_tutore_fact.setValue(filter_moy_min_tutore.getValue());
				}
				filtrageTutore();
			}
		});

		filter_moy_max_tutore.valueProperty().addListener(new ChangeListener<Integer>() {
			@Override
			public void changed(ObservableValue<? extends Integer> arg0, Integer arg1, Integer arg2) {
				if (filter_moy_max_tutore.getValue() < filter_moy_min_tutore.getValue()) {
					filter_moy_min_tutore_fact.setValue(filter_moy_max_tutore.getValue());
				}
				filtrageTutore();
			}
		});

		// Filtre Absences Tuteurs

		SpinnerValueFactory<Integer> filter_abs_tuteur_fact = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,MAX_ABS);
		filter_abs_tuteur_fact.setValue(MAX_ABS);
		filter_abs_tuteur.setValueFactory(filter_abs_tuteur_fact);

		filter_abs_tuteur.valueProperty().addListener(new ChangeListener<Integer>() {
			@Override
			public void changed(ObservableValue<? extends Integer> arg0, Integer arg1, Integer arg2) {
				filtrageTuteur();
			}
		});

		// Filtre Absences Tutorés

		SpinnerValueFactory<Integer> filter_abs_tutore_fact = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,MAX_ABS);
		filter_abs_tutore_fact.setValue(MAX_ABS);
		filter_abs_tutore.setValueFactory(filter_abs_tutore_fact);

		filter_abs_tutore.valueProperty().addListener(new ChangeListener<Integer>() {
			@Override
			public void changed(ObservableValue<? extends Integer> arg0, Integer arg1, Integer arg2) {
				filtrageTutore();
			}
		});

		// Filtre Année

		filter_annee_tous.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {filtrageTuteur();}
		});

		filter_annee_2.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {filtrageTuteur();}
		});

		filter_annee_3.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {filtrageTuteur();}
		});

		//-------------//
		// AFFECTATION //
		//-------------//

		// Critères d'Affectation

		critere_annee.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				if (critere_annee.isSelected()) {
					ordreCriteres.add(Critere.ANNEE);
				} else {
					ordreCriteres.remove(Critere.ANNEE);
				}
				updateCriteresNumbers();
			}
		});

		critere_motivation.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				if (critere_motivation.isSelected()) {
					ordreCriteres.add(Critere.MOTIVATION);
				} else {
					ordreCriteres.remove(Critere.MOTIVATION);
				}
				updateCriteresNumbers();
			}
		});

		critere_absences.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				if (critere_absences.isSelected()) {
					ordreCriteres.add(Critere.ABSENCES);
				} else {
					ordreCriteres.remove(Critere.ABSENCES);
				}
				updateCriteresNumbers();
			}
		});

		// Bouton d'Affectation

		button_affecter.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				affectationCriteres();
			}
		});

		// Bouton Lier

		button_lier.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				Couple compareCouple = new Couple((Tuteur) currentTuteur, (Tutore) currentTutore);
				if (!currentAffectation.getBdd().getAssignedCouples().contains(compareCouple) &&
						!affectationsManuelles.contains(compareCouple)) { // Lier
					liaison();
				} else { // Délier
					deliaison();
				}
			}
		});

		// Liste Couples

		liste_couples.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				Couple selected = liste_couples.getSelectionModel().getSelectedItem();
				if (selected != null) {
					liste_tuteur.getSelectionModel().select(selected.getTuteurAffecte());
					liste_tutore.getSelectionModel().select(selected.getTutoreAffecte());
					if (info_tutore_prenom.isDisable()) tutoreSetDisable(false);
					if (info_tuteur_prenom.isDisable()) tuteurSetDisable(false);
					currentTuteur = selected.getTuteurAffecte();
					currentTutore = selected.getTutoreAffecte();
					updateInfosTuteur();
					updateInfosTutore();
					fadeBorderTuteur.stop();
					fadeBorderTutore.stop();
					fadeBorderTuteur.play();
					fadeBorderTutore.play();
				}
			}
		});

		// Exporter Affectation

		button_aff_export.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				tid = new TextInputDialog();
				tid.setHeaderText("Entrez un nom de fichier à exporter");
				tid.setTitle("Exporter Affectation (.json)");
				Optional<String> result = tid.showAndWait();

				result.ifPresent(filename -> {
					exportAffectation(filename);
				});
			}
		});

	}

	//----------//
	// MÉTHODES //
	//----------//

	// Ressource

	private void setInterfaceRessource(Ressource res) {
		App.setRessource(res);
		label_tutore_res.setText("Moyenne en "+res);
		currentAffectation = affectations.get(res.getID()-1);
		updateListeTuteur();
		updateListeTutore();
	}

	// Tuteurs

	private void updateInfosTuteur() {
		if (currentTuteur != null) {
			userAction = false;
			info_tuteur_prenom.setText(currentTuteur.getPrenom());
			info_tuteur_nom.setText(currentTuteur.getNom());
			info_tuteur_annee_2.setSelected(currentTuteur.getAnnee()==2);
			info_tuteur_annee_3.setSelected(currentTuteur.getAnnee()==3);
			info_tuteur_matiere.getSelectionModel().select(((Tuteur) currentTuteur).getRessourceEnseigne());
			info_tuteur_motiv.setValue((currentTuteur.getMotivation().getLevel()));
			info_tuteur_moy.setText(currentTuteur.getMoyenne()+"");
			info_tuteur_abs_fact.setValue(currentTuteur.getNbrAbsences());
			userAction = true;
		}
	}

	private void tuteurSetDisable(boolean arg0) {
		info_tuteur_prenom.setDisable(arg0);
		info_tuteur_nom.setDisable(arg0);
		info_tuteur_annee_2.setDisable(arg0);
		info_tuteur_annee_3.setDisable(arg0);
		info_tuteur_matiere.setDisable(arg0);
		info_tuteur_motiv.setDisable(arg0);
		info_tuteur_moy.setDisable(arg0);
		info_tuteur_abs.setDisable(arg0);
	}

	public void initTuteur() {
		currentAffectation.getBdd().setTuteurs(Data.getTutorsOfThisRessource(filter_matiere.getValue()));
		updateListeTuteur();
	}

	public void updateListeTuteur() {
		liste_tuteur.getSelectionModel().clearSelection();
		currentTuteur = null;
		liste_tuteur.getItems().setAll(currentAffectation.getBdd().getTuteurs());
	}

	public void updateListeTuteurKeepSelection() {
		Etudiant tmp = currentTuteur;
		updateListeTuteur();
		liste_tuteur.getSelectionModel().select(tmp);
		currentTuteur = tmp;
	}

	public void addTuteur() {
		Etudiant newTuteur = new Tuteur("Tuteur","Nouveau",2,0,Motivation.MOYEN,0,filter_matiere.getValue());
		currentAffectation.getBdd().getTuteurs().add(newTuteur);
		updateListeTuteur();
		liste_tuteur.getSelectionModel().select(newTuteur);
		currentTuteur = newTuteur;
		exportTuteur();
	}

	public void removeTuteur() {
		// Enlever le tuteur des affectations
		for (int c1 = 0; c1 < currentAffectation.getBdd().getAssignedCouples().size(); c1++) {
			if (currentAffectation.getBdd().getAssignedCouples().get(c1).getTuteurAffecte().equals(currentTuteur)){
				currentAffectation.getBdd().getAssignedCouples().remove(c1);
				c1--;
			}
		}
		for (int c2 = 0; c2 < affectationsManuelles.size(); c2++) {
			if (affectationsManuelles.get(c2).getTuteurAffecte().equals(currentTuteur)){
				affectationsManuelles.remove(c2);
				c2--;
			}
		}
		updateListeCouples();

		// Enlever le tuteur de la liste
		currentAffectation.getBdd().getTuteurs().remove(currentTuteur);
		Tuteur.getTuteurs().remove(currentTuteur);
		filtrageTuteur();
		exportTuteur();
	}

	// Tutorés

	private void updateInfosTutore() {
		if (currentTutore != null) {
			userAction = false;
			info_tutore_prenom.setText(currentTutore.getPrenom());
			info_tutore_nom.setText(currentTutore.getNom());
			info_tutore_motiv.setValue((currentTutore.getMotivation().getLevel()));									    
			info_tutore_moy_res.setText(noteFormat(((Tutore) currentTutore).getNotesRessources().get(App.getUtilisateur().getRessourceEnseigne())));
			info_tutore_moy.setText(currentTutore.getMoyenne()+"");
			info_tutore_abs_fact.setValue(currentTutore.getNbrAbsences());
			userAction = true;
		}
	}

	private void tutoreSetDisable(boolean arg0) {
		info_tutore_prenom.setDisable(arg0);
		info_tutore_nom.setDisable(arg0);
		info_tutore_motiv.setDisable(arg0);
		info_tutore_moy_res.setDisable(arg0);
		info_tutore_moy.setDisable(arg0);
		info_tutore_abs.setDisable(arg0);
	}

	public void initTutore() {
		currentAffectation.getBdd().setTutores(Tutore.getTutores());
		updateListeTutore();
	}

	public void updateListeTutore() {
		liste_tutore.getSelectionModel().clearSelection();
		currentTutore = null;
		liste_tutore.getItems().setAll(currentAffectation.getBdd().getTutores());
	}

	public void updateListeTutoreKeepSelection() {
		Etudiant tmp = currentTutore;
		updateListeTutore();
		liste_tutore.getSelectionModel().select(tmp);
		currentTutore = tmp;
	}

	public void addTutore() {
		HashMap<Ressource,Double> notes = new HashMap<Ressource,Double>();
		for (Ressource r : Ressource.values()) {
			notes.put(r,0.0);
		}

		Etudiant newTutore = new Tutore("Tutoré","Nouveau",0,Motivation.MOYEN,0,notes);
		currentAffectation.getBdd().getTutores().add(newTutore);
		updateListeTutore();
		liste_tutore.getSelectionModel().select(newTutore);
		currentTutore = newTutore;
		exportTutore();
	}

	public void removeTutore() {
		// Enlever le tutoré des affectations
		for (int c1 = 0; c1 < currentAffectation.getBdd().getAssignedCouples().size(); c1++) {
			if (currentAffectation.getBdd().getAssignedCouples().get(c1).getTutoreAffecte().equals(currentTutore)){
				currentAffectation.getBdd().getAssignedCouples().remove(c1);
				c1--;
			}
		}
		for (int c2 = 0; c2 < affectationsManuelles.size(); c2++) {
			if (affectationsManuelles.get(c2).getTutoreAffecte().equals(currentTutore)){
				affectationsManuelles.remove(c2);
				c2--;
			}
		}
		updateListeCouples();

		// Enlever le tutoré de la liste
		currentAffectation.getBdd().getTutores().remove(currentTutore);
		Tutore.getTutores().remove(currentTutore);
		filtrageTutore();
		exportTutore();
	}

	// Filtres

	public void filtrageTuteur() {
		// Réinitialisation des tuteurs
		initTuteur();

		// Filtrage Moyenne
		currentAffectation.getBdd().setTuteurs(currentAffectation.filterByMoyenne(currentAffectation.getBdd().getTuteurs(), filter_moy_min_tuteur.getValue(), filter_moy_max_tuteur.getValue()));

		// Filtrage Absences
		currentAffectation.getBdd().setTuteurs(currentAffectation.filterByAbsence(currentAffectation.getBdd().getTuteurs(), filter_abs_tuteur.getValue()));

		// Filtrage Année
		if (filter_annee_2.isSelected()) currentAffectation.getBdd().setTuteurs(currentAffectation.filterByAnnee(currentAffectation.getBdd().getTuteurs(), 2));
		if (filter_annee_3.isSelected()) currentAffectation.getBdd().setTuteurs(currentAffectation.filterByAnnee(currentAffectation.getBdd().getTuteurs(), 3));

		// Update des tuteurs
		updateListeTuteur();

		// Blocage des infos tuteurs
		tuteurSetDisable(true);
	}

	public void filtrageTutore() {
		// Réinitialisation des tutorés
		initTutore();

		// Filtrage Moyenne
		currentAffectation.getBdd().setTutores(currentAffectation.filterByMoyenne(currentAffectation.getBdd().getTutores(), filter_moy_min_tutore.getValue(), filter_moy_max_tutore.getValue()));

		// Filtrage Absences
		currentAffectation.getBdd().setTutores(currentAffectation.filterByAbsence(currentAffectation.getBdd().getTutores(), filter_abs_tutore.getValue()));

		// Update des tutorés
		updateListeTutore();

		// Blocage des infos tutorés
		tutoreSetDisable(true);
	}

	// Affectations

	private void initializeAffectations() {
		affectations = new ArrayList<Affectation>();
		for (Ressource r : Ressource.values()) {
			affectations.add(new Affectation(r));
		}
		currentAffectation = affectations.get(0);
	}

	public void updateCriteresNumbers() {
		int nbAnnee = ordreCriteres.indexOf(Critere.ANNEE);
		if (nbAnnee != -1) critere_annee.setText((nbAnnee+1)+". Année");
		else critere_annee.setText("Année");

		int nbMotivation = ordreCriteres.indexOf(Critere.MOTIVATION);
		if (nbMotivation != -1) critere_motivation.setText((nbMotivation+1)+". Motivation");
		else critere_motivation.setText("Motivation");

		int nbAbsences = ordreCriteres.indexOf(Critere.ABSENCES);
		if (nbAbsences != -1) critere_absences.setText((nbAbsences+1)+". Absences");
		else critere_absences.setText("Absences");
	}

	public void affectationCriteres() {
		ArrayList<Couple> assignedCouples = currentAffectation.getBdd().getAssignedCouples();
		// Tri par critères et affectation
		assignedCouples.clear();
		for (int i = ordreCriteres.size()-1; i > 0; i--) {
			if (ordreCriteres.get(i) == Critere.ANNEE) {
				currentAffectation.getBdd().setTuteurs(Data.sortByYear(currentAffectation.getBdd().getTuteurs()));
				currentAffectation.getBdd().setTutores(Data.sortByYear(currentAffectation.getBdd().getTutores()));
			} else if (ordreCriteres.get(i) == Critere.MOTIVATION) {
				currentAffectation.getBdd().setTuteurs(Data.sortByMotivation(currentAffectation.getBdd().getTuteurs()));
				currentAffectation.getBdd().setTutores(Data.sortByMotivation(currentAffectation.getBdd().getTutores()));
			} else if (ordreCriteres.get(i) == Critere.ABSENCES) {
				currentAffectation.getBdd().setTuteurs(Data.sortByAbsences(currentAffectation.getBdd().getTuteurs(),filter_abs_tuteur.getValue()));
				currentAffectation.getBdd().setTutores(Data.sortByAbsences(currentAffectation.getBdd().getTutores(),filter_abs_tutore.getValue()));
			}
		}
		currentAffectation.affectation(filter_matiere.getValue());

		// Ne pas prendre en compte les tutorés déjà affectés manuellement
		for (int c1 = 0; c1 < assignedCouples.size(); c1++) {
			for (Couple c2: affectationsManuelles) {
				if (assignedCouples.get(c1).getTutoreAffecte().equals(c2.getTutoreAffecte())) {
					assignedCouples.remove(c1);
					c1--;
				}
			}
		}

		// Récupération des couples formés
		updateListeCouples();
		button_aff_export.setDisable(false);

		// Filtrage
		filtrageTuteur();
		filtrageTutore();
	}

	public void updateListeCouples() {
		liste_couples.getSelectionModel().clearSelection();
		liste_couples.setDisable(false);
		liste_couples.getItems().setAll(currentAffectation.getBdd().getAssignedCouples());
		liste_couples.getItems().addAll(affectationsManuelles);
		button_aff_export.setDisable(false);
	}

	public void updateBoutonLier() {
		if (currentTuteur == null || currentTutore == null) {
			button_lier.setDisable(true);
		} else {
			boolean isManual = false;
			Couple compareCouple = new Couple((Tuteur) currentTuteur, (Tutore) currentTutore);
			int coupleIndex = currentAffectation.getBdd().getAssignedCouples().indexOf(compareCouple);
			if (coupleIndex == -1) {
				coupleIndex = affectationsManuelles.indexOf(compareCouple);
				isManual = true;
			}
			if (coupleIndex == -1) { // Lier
				if (isTutoreAffecte((Tutore) currentTutore)) {
					button_lier.setDisable(true);
					button_lier.setText("Tutoré déjà affecté");
				} else {
					button_lier.setDisable(false);
					button_lier.setText("Lier");
				}
				button_lier.setStyle("-fx-background-color:#147bb8; -fx-background-radius:12;");
				button_lier.setTextFill(Paint.valueOf("ffffff"));
			} else { // Délier
				button_lier.setDisable(false);
				button_lier.setText("Délier");
				button_lier.setStyle("-fx-background-color:#ffffff; -fx-background-radius:12; -fx-border-color:#147bb8; -fx-border-radius:12; -fx-border-width:3;");
				button_lier.setTextFill(Paint.valueOf("147bb8"));
				if (isManual) coupleIndex += currentAffectation.getBdd().getAssignedCouples().size();
				liste_couples.getSelectionModel().select(coupleIndex);
			}
		}
	}

	public void liaison() {
		Couple newLiaison = new Couple((Tuteur) currentTuteur,(Tutore) currentTutore);
		affectationsManuelles.add(newLiaison);
		updateListeCouples();
		liste_couples.getSelectionModel().select(newLiaison);
		updateBoutonLier();
	}

	public void deliaison() {
		liste_couples.getSelectionModel().clearSelection();
		Couple compareCouple = new Couple((Tuteur) currentTuteur, (Tutore) currentTutore);
		int coupleIndex = currentAffectation.getBdd().getAssignedCouples().indexOf(compareCouple);
		if (coupleIndex != -1) {
			currentAffectation.getBdd().getAssignedCouples().remove(coupleIndex);
		} else {
			coupleIndex = affectationsManuelles.indexOf(compareCouple);
			if (coupleIndex != -1) {
				affectationsManuelles.remove(coupleIndex);
			}
		}
		updateListeCouples();
		updateBoutonLier();
	}

	public boolean isTutoreAffecte(Tutore tutore) {
		for (Couple c : currentAffectation.getBdd().getAssignedCouples()) {
			if (c.getTutoreAffecte().equals(tutore)) return true;
		}
		for (Couple c : affectationsManuelles){
			if (c.getTutoreAffecte().equals(tutore)) return true;
		}
		return false;
	}

	// Exportation

	public void exportTuteur() {
		GsonUtils.serializeObject(Tuteur.getTuteurs(), GsonUtils.getListetuteurDir(), false);
	}

	public void exportTutore() {
		GsonUtils.serializeObject(Tutore.getTutores(), GsonUtils.getListetutoreDir(), false);
	}

	public void exportAffectation(String filename) {
		String f = filename;
		if (f.isEmpty()) f = "Sans Titre";
		if (!f.equals("listeTuteur") && !f.equals("listeTutore")) {
			ArrayList<Couple> serialisation = new ArrayList<Couple>();
			serialisation.addAll(currentAffectation.getBdd().getAssignedCouples());
			serialisation.addAll(affectationsManuelles);
			GsonUtils.serializeObject(serialisation, FileUtils.getExportPath() + f + ".json", true);
		}
	}

	// Other

	private String noteFormat(double note) {
		double res = Math.round(note*100);
		return res/100+"";
	}
}