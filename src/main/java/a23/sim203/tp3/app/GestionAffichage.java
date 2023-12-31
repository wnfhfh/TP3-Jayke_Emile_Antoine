/**
 * Gère l'affichage et les interactions de la calculatrice.
 *
 * @Author Jayke Gagné, Antoine Houde, Émile Roy
 */
package a23.sim203.tp3.app;


import a23.sim203.tp3.controller.CalculatriceController;
import a23.sim203.tp3.controller.SimFenetreController;
import a23.sim203.tp3.modele.MoteurCalcul;
import a23.sim203.tp3.vue.Animations;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.mariuszgromada.math.mxparser.Constant;

import java.io.*;
import java.util.ArrayList;

public class GestionAffichage {
    /**
     * Moteur de calcul associé à l'instance de la classe.
     */
    private MoteurCalcul moteurCalcul;
    /**
     * Chaîne de texte affichée associée à l'instance de la classe.
     */

    private String stringAffiche;
    /**
     * Contrôleur de la calculatrice associé à l'instance de la classe.
     */
    private CalculatriceController calculatriceController;
    /**
     * Stage (scène) associé à l'instance de la classe.
     */
    private Stage stage = new Stage();
    /**
     * Instance de la classe Animations utilisée pour gérer les animations associée à l'instance de la classe.
     */
    private Animations animations = new Animations();

    /**
     * Constructeur de la classe GestionAffichage.
     *
     * @param controller Le contrôleur de la calculatrice.
     */
    public GestionAffichage(CalculatriceController controller) {
        moteurCalcul = new MoteurCalcul();
        stringAffiche = "";
        this.calculatriceController = controller;
        calculatriceController.getListeEquations();

        ajouterEquationsDeBase();
    }

    public Animations getAnimations() {
        return animations;
    }

    /**
     * Ajoute les équations demandées aux listes et au moteur de calcul.
     */
    private void ajouterEquationsDeBase() {
        Enregistreur enregistreur = new Enregistreur(moteurCalcul, this);
        try {
            Enregistreur.EquationsConstantesEtVariables eCET = enregistreur.chargeModele(new File(System.getProperty("user.dir") + "//modeleDeBase.txt"));
            moteurCalcul.setEquationMap(eCET.getEquations());
            moteurCalcul.setConstantMap(eCET.getVariables());
            moteurCalcul.setMapAncienneValeur(eCET.getMapAncienneValeur());
            refreshAffichage();
        } catch (FileNotFoundException e) {
            System.out.println("bug loader modeleDeBase.txt");
        }
    }

    /**
     * Configure le bouton pour afficher un caractère lorsqu'il est cliqué.
     *
     * @param caractere Le caractère a affiché.
     * @param bouton    Le bouton associé.
     */

    public void setBoutonCaractere(char caractere, Button bouton) {
        actionAssistanceVisuelle(bouton);
        bouton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                stringAffiche = calculatriceController.getStringAfficheTexte();
                stringAffiche += caractere;
                calculatriceController.getStringAffiche().setText(stringAffiche);
            }
        });
    }

    /**
     * Définit le contrôleur de la calculatrice pour cette instance de GestionAffichage.
     *
     * @param calculatriceController Le contrôleur de la calculatrice.
     */
    public void setCalculatriceController(CalculatriceController calculatriceController) {
        this.calculatriceController = calculatriceController;
    }

    /**
     * Associe un événement au bouton pour effacer l'affichage de la calculatrice.
     *
     * @param bouton Le bouton associé à l'action d'effacement.
     */
    public void actionBoutonEffacer(Button bouton) {
        bouton.setOnAction(event -> {
            stringAffiche = "";
            calculatriceController.setStringAffiche(stringAffiche);
        });
    }

    /**
     * Associe un événement au bouton pour reculer d'un caractère dans l'affichage de la calculatrice.
     *
     * @param bouton Le bouton associé à l'action de recul.
     */

    public void actionBoutonReculer(Button bouton) {
        bouton.setOnAction(event -> {
            if (stringAffiche.length() >= 1) {
                stringAffiche = stringAffiche.substring(0, stringAffiche.length() - 1);
                calculatriceController.setStringAffiche(stringAffiche);
            }
        });
    }

    /**
     * Associe un événement au bouton pour changer le signe de la valeur affichée (positif/négatif).
     *
     * @param bouton Le bouton associé à l'action de changement de signe.
     */
    public void actionBoutonPlusMinus(Button bouton) {
        actionAssistanceVisuelle(bouton);
        bouton.setOnAction(event -> {
            if (stringAffiche.charAt(0) == '-') {
                stringAffiche = stringAffiche.substring(1);
                calculatriceController.setStringAffiche(stringAffiche);
            } else {
                stringAffiche = "-" + stringAffiche;
                calculatriceController.setStringAffiche(stringAffiche);
            }
        });
    }

    /**
     * Associe un événement au bouton pour effectuer le calcul de l'expression actuellement affichée.
     *
     * @param bouton Le bouton associé à l'action de calcul.
     */
    public void actionBoutonEgal(Button bouton) {
        actionAssistanceVisuelle(bouton);
        bouton.setOnAction(event -> {
            double reponse = moteurCalcul.calcule(calculatriceController.getStringAfficheTexte().split("=")[0]);
            calculatriceController.setStringAffiche(String.valueOf(reponse));
        });
    }

    /**
     * Crée et affiche une alerte indiquant une expression récursive.
     * Utilisé lorsque l'utilisateur tente d'ajouter une expression récursive.
     */
    public void creerAlerteRecursive() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Expression récursive");
        alert.setTitle("Calculateur avancée");
        alert.setContentText("L'expression saisie ne peut être ajoutée");
        alert.showAndWait();
    }

    /**
     * Associe un événement au bouton pour ajouter une équation à partir de l'expression actuellement affichée.
     *
     * @param bouton Le bouton associé à l'action d'ajout d'équation.
     */
    public void actionBoutonAjoute(Button bouton) {
        bouton.setOnMouseClicked(event -> {
            try {
                moteurCalcul.ajouteEquation(calculatriceController.getStringAfficheTexte());
                calculatriceController.getListeEquations().getItems().add(moteurCalcul.getEquationMap().get((calculatriceController.getStringAfficheTexte()).split("=")[0]).toString());
                calculatriceController.getListeEquations().refresh();
                calculatriceController.getListeVariables().getItems().setAll(moteurCalcul.getToutesLesVariablesString());
            } catch (Exception e) {
                // Affiche une alerte en cas d'équation non valide
                new Alert(Alert.AlertType.ERROR, "Équation non valide").showAndWait();
            }
        });
    }

    /**
     * Associe des événements visuels au bouton pour l'assistance visuelle.
     *
     * @param bouton Le bouton associé à l'assistance visuelle.
     */

    public void actionAssistanceVisuelle(Button bouton) {
        bouton.setOnMouseEntered(event -> {
            boolean isSelected = calculatriceController.getMenuItemAssistanceVisuelle().isSelected();
            if (isSelected) {
                bouton.setStyle("-fx-font-size: 35;");
            }
        });

        bouton.setOnMouseExited(event1 -> {
            boolean isSelected = calculatriceController.getMenuItemAssistanceVisuelle().isSelected();
            if (isSelected) {
                bouton.setStyle("-fx-font-size: 25;");
            }
        });
    }

    /**
     * Associe un événement au bouton pour supprimer une équation sélectionnée dans la liste des équations.
     *
     * @param bouton Le bouton associé à l'action de suppression.
     */
    public void actionBoutonSupprime(Button bouton) {
        bouton.setOnAction(event -> {
            moteurCalcul.getEquationMap().remove(calculatriceController.getListeEquations().getSelectionModel().getSelectedItem().split("=")[0]);
            calculatriceController.getListeEquations().getItems().remove(calculatriceController.getListeEquations().getSelectionModel().getSelectedItem());
            moteurCalcul.retireConstantesInutiles();
        });
    }

    /**
     * Associe des événements aux boutons de bascule pour gérer les actions d'écriture et de lecture.
     *
     * @param toggleBoutonLire     Le bouton de bascule pour l'action de lecture/écriture.
     * @param toggleButtonVariable Le bouton de bascule pour sélectionner la valeur ou la variable.
     * @param toggleGroup          Le groupe de bascules auquel appartiennent les boutons.
     */
    public void actionToggleBoutons(ToggleButton toggleBoutonLire, ToggleButton toggleButtonVariable, ToggleGroup toggleGroup) {
        toggleBoutonLire.setToggleGroup(toggleGroup);
        toggleButtonVariable.setToggleGroup(toggleGroup);

        toggleBoutonLire.setOnMouseClicked(event -> {
            if (toggleBoutonLire.isSelected()) {
                toggleBoutonLire.setText("écrire");
                toggleButtonVariable.setDisable(true);
                gererEcrire();
            } else {
                toggleBoutonLire.setText("lire");
                gererLire(toggleButtonVariable);
                toggleButtonVariable.setDisable(false);
            }
        });

        toggleButtonVariable.setOnMouseClicked(event -> {
            if (toggleButtonVariable.isSelected()) {
                toggleButtonVariable.setText("valeur");
            } else {
                toggleButtonVariable.setText("variable");
            }
        });
    }

    /**
     * Gère l'action de lecture lorsqu'une variable est double-cliquée dans la liste des variables.
     *
     * @param toggleButtonVariable Le bouton de bascule pour sélectionner la valeur ou la variable.
     */
    private void gererLire(ToggleButton toggleButtonVariable) {

        calculatriceController.getListeVariables().setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                String stringAAjouter = calculatriceController.getListeVariables().getSelectionModel().getSelectedItem().split("=")[0];
                if (toggleButtonVariable.getText() == "valeur") {
                    stringAAjouter = String.valueOf((moteurCalcul.getVariableValueMap().get(stringAAjouter)).getConstantValue());
                }
                calculatriceController.setStringAffiche(calculatriceController.getStringAfficheTexte() + stringAAjouter);
            }
        });
    }

    /**
     * Gère l'action d'écriture lorsqu'une variable est double-cliquée dans la liste des variables.
     */
    private void gererEcrire() {
        calculatriceController.getListeVariables().setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                String nomVariableAChanger = calculatriceController.getListeVariables().getSelectionModel().getSelectedItem().split("=")[0];
                try {
                    Constant nouvelleConstante = new Constant(nomVariableAChanger, Double.parseDouble(calculatriceController.getStringAfficheTexte()));
                    moteurCalcul.getVariableValueMap().put(nomVariableAChanger, nouvelleConstante);
                    calculatriceController.getListeVariables().getItems().remove(calculatriceController.getListeVariables().getSelectionModel().getSelectedItem());
                    calculatriceController.getListeVariables().getItems().add(String.valueOf(nouvelleConstante.getConstantName() + " = " + nouvelleConstante.getConstantValue()));
                } catch (Exception e) {
                    System.out.println("veuillez enter une valeur numérique seulement");
                }
            }
        });
    }

    /**
     * Définit l'action associée à un MenuItem spécifié pour ouvrir et charger un modèle à partir d'un fichier texte.
     *
     * @param menuItemCharger Le MenuItem associé à l'action de chargement de fichier.
     */
    public void setMenuItemCharger(MenuItem menuItemCharger) {
        menuItemCharger.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser(); // Crée un sélecteur de fichiers avec un filtre pour les fichiers texte
            fileChooser.getExtensionFilters().add(0, new FileChooser.ExtensionFilter("fichier texte", "*.txt"));
            // Définit le répertoire initial sur le répertoire utilisateur actuel
            fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
            File fichierACharger = fileChooser.showOpenDialog(stage); // Affiche la boîte de dialogue d'ouverture et obtient le fichier sélectionné
            try {
                // Charge le modèle à partir du fichier sélectionné en utilisant la classe Enregistreur
                Enregistreur.EquationsConstantesEtVariables eCET = new Enregistreur(moteurCalcul, this).chargeModele(fichierACharger);
                moteurCalcul.setEquationMap(eCET.getEquations()); // Met à jour le moteur de calcul avec les données chargées
                moteurCalcul.setConstantMap(eCET.getVariables());
                moteurCalcul.setMapAncienneValeur(eCET.getMapAncienneValeur());
                refreshAffichage(); // Rafraîchit l'affichage pour refléter les changements
            } catch (Exception e) {
                // Affiche une alerte en cas d'exception lors du chargement du fichier
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setHeaderText("Fichier non valide");
                alert.setTitle("Calculateur avancée");
                alert.setContentText("Réessayez");
                alert.showAndWait();
                System.out.println(e);
            }
        });
    }

    /**
     * Définit l'action associée à un MenuItem spécifié pour ouvrir une fenêtre de simulation de calcul sans contrainte de temps.
     *
     * @param boutonCalculPasDeTemps Le MenuItem associé à l'action d'ouverture de la fenêtre de simulation.
     */
    public void setMenuItemCalculPasDeTemps(MenuItem boutonCalculPasDeTemps) {
        boutonCalculPasDeTemps.setOnAction(n -> {
            FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("testFenetreSimulation.fxml"));
            Parent root;
            try {
                root = fxmlLoader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            SimFenetreController controller = fxmlLoader.getController();
            controller.setGestionAffichage(this);
            Scene sceneSimulation = new Scene(root);
            Stage stageSimulation = new Stage();
            controller.setStageSimulation(stageSimulation);
            stageSimulation.setScene(sceneSimulation);
            controller.setMoteurCalcul(moteurCalcul);
            controller.setNombreObjets(demanderNombreObjets());
            stageSimulation.show();
            controller.creerGraphique();
            controller.creerTableau();
            animations.partageDesFenetresSimulation(stageSimulation, stage);
            animations.setStages(stageSimulation, this.stage);
            animations.premiereAnimation(new Stage());
            animations.deuxiemeAnimation(new Stage());
        });
    }

    /**
     * Demande à l'utilisateur d'entrer un nombre d'objets et renvoie la valeur saisie en tant qu'entier.
     *
     * @return Le nombre d'objets saisi par l'utilisateur.
     */
    private Integer demanderNombreObjets() {
        TextInputDialog textInputDialog = new TextInputDialog("1");
        textInputDialog.setHeaderText("Entrez le nombre de planètes voulu:");
        textInputDialog.showAndWait();
        try {
            int reponse = Integer.valueOf(textInputDialog.getResult());
            return reponse;
        } catch (Exception e) {
            textInputDialog.close();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("Veuillez entrer un nombre svp :)");
            alert.setTitle("Calculateur avancée");
            alert.setContentText("Réessayez plz");
            alert.showAndWait();
            return demanderNombreObjets();
        }
    }

    /**
     * Rafraîchit l'affichage de la calculatrice en mettant à jour les listes d'équations, de variables et de constantes.
     */
    private void refreshAffichage() {
        calculatriceController.getListeEquations().getItems().addAll(moteurCalcul.getAllEquationsString());
        calculatriceController.getListeVariables().getItems().addAll(moteurCalcul.getToutesLesVariablesString());
        calculatriceController.getListeConstantes().getItems().addAll(moteurCalcul.getToutesLesConstantesString());
    }

    /**
     * Définit l'action associée à un MenuItem spécifié pour enregistrer les équations et constantes dans un fichier texte.
     *
     * @param menuItemEnregistrer Le MenuItem associé à l'action d'enregistrement.
     */
    public void setMenuItemEnregistrer(MenuItem menuItemEnregistrer) {
        menuItemEnregistrer.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(0, new FileChooser.ExtensionFilter("fichier texte", "*.txt"));
            File fichierEnregistrer = fileChooser.showSaveDialog(stage);
            new Enregistreur().enregistreEquation(moteurCalcul.getAllEquationsString(), moteurCalcul.getToutesLesConstantesString(), new ArrayList<Constant>(moteurCalcul.getConstanteValeurMap().values()), fichierEnregistrer);
        });
    }

    /**
     * Définit le stage associé à l'instance de la classe.
     *
     * @param stage Le stage à associer à l'instance.
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Obtient le stage associé à l'instance de la classe.
     *
     * @return Le stage associé à l'instance.
     */
    public Stage getStage() {
        return stage;
    }

    /**
     * Obtient le moteur de calcul associé à l'instance de la classe.
     *
     * @return Le moteur de calcul associé à l'instance.
     */

    public MoteurCalcul getMoteurCalcul() {
        return moteurCalcul;
    }

    public CalculatriceController getCalculatriceController() {
        return calculatriceController;
    }
}
