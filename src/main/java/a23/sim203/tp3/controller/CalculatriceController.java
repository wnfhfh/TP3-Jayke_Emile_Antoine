/**
 * Classe contrôleur pour l'application de calculatrice.
 * Responsable de la gestion des interactions utilisateur, de la gestion
 * des éléments de l'interface graphique (GUI) et de la liaison avec le moteur
 * de calcul sous-jacent.
 *
 * @author Émile Roy, Jayke Gagné, Antoine Houde
 */
package a23.sim203.tp3.controller;

import a23.sim203.tp3.app.GestionAffichage;
import a23.sim203.tp3.modele.MoteurCalcul;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import javax.security.auth.callback.Callback;
import java.net.URL;
import java.util.ResourceBundle;

public class CalculatriceController implements Initializable {

    GestionAffichage gestionAffichage;
    @FXML
    private MenuItem menuItemAPropos;
    @FXML
    private Button bouton0;
    @FXML
    private Button bouton1;
    @FXML
    private Button bouton2;
    @FXML
    private Button bouton3;
    @FXML
    private Button bouton4;
    @FXML
    private Button bouton5;
    @FXML
    private Button bouton6;
    @FXML
    private Button bouton7;
    @FXML
    private Button bouton8;
    @FXML
    private Button bouton9;
    @FXML
    private Button boutonAdditionner;
    @FXML
    private Button boutonAjoute;
    @FXML
    private Button boutonDiviser;
    @FXML
    private Button boutonEffacer;
    @FXML
    private Button boutonEgal;
    @FXML
    private Button boutonMultiplier;
    @FXML
    private Button boutonParentheseD;
    @FXML
    private Button boutonParentheseG;
    @FXML
    private Button boutonPlusMinus;
    @FXML
    private Button boutonPoint;
    @FXML
    private Button boutonReculer;
    @FXML
    private Button boutonSoustraire;
    @FXML
    private Button boutonSupprime;
    @FXML
    private ToggleButton toggleBoutonVariable;
    @FXML
    private ListView<String> listeVariables;

    @FXML
    private GridPane gridPane;
    @FXML
    private ListView<String> listeEquations;
    @FXML
    private ListView<String> listeConstantes;
    @FXML
    private Menu menuAssistance;
    @FXML
    private CheckMenuItem menuItemAssistanceVisuelle;
    @FXML
    private ToggleButton toggleBoutonLire;
    @FXML
    private TextField stringAffiche;    // TODO Changer ID dans SceneBuilder
    @FXML
    private MenuItem MenuItemCalculPasDeTemps;
    @FXML
    private MenuItem MenuItemCharger;
    @FXML
    private MenuItem MenuItemEnregistrer;

    /**
     * Configure les boutons de la calculatrice en associant les caractères
     * et les actions correspondantes à chaque bouton de l'interface graphique.
     * Les actions incluent l'ajout de caractères, le retrait, l'inversion, etc.
     * Les boutons associés aux opérations mathématiques et aux parenthèses sont également configurés.
     */
    @FXML
    void setBoutonsCalculatrice() {
        setBoutonsCaractères();
        gestionAffichage.actionBoutonReculer(boutonReculer);
        gestionAffichage.actionBoutonEffacer(boutonEffacer);
        gestionAffichage.actionBoutonPlusMinus(boutonPlusMinus);
        gestionAffichage.actionBoutonEgal(boutonEgal);
        gestionAffichage.actionBoutonAjoute(boutonAjoute);
        gestionAffichage.actionBoutonSupprime(boutonSupprime);
        gestionAffichage.setMenuItemCalculPasDeTemps(MenuItemCalculPasDeTemps);
        gestionAffichage.setMenuItemEnregistrer(MenuItemEnregistrer);

        gestionAffichage.setMenuItemCharger(MenuItemCharger);

        ToggleGroup toggleGroup = new ToggleGroup();
        gestionAffichage.actionToggleBoutons(toggleBoutonLire, toggleBoutonVariable, toggleGroup);
    }

    private void setBoutonsCaractères() {
        gestionAffichage.setBoutonCaractere('0', bouton0);
        gestionAffichage.setBoutonCaractere('1', bouton1);
        gestionAffichage.setBoutonCaractere('2', bouton2);
        gestionAffichage.setBoutonCaractere('3', bouton3);
        gestionAffichage.setBoutonCaractere('4', bouton4);
        gestionAffichage.setBoutonCaractere('5', bouton5);
        gestionAffichage.setBoutonCaractere('6', bouton6);
        gestionAffichage.setBoutonCaractere('7', bouton7);
        gestionAffichage.setBoutonCaractere('8', bouton8);
        gestionAffichage.setBoutonCaractere('9', bouton9);
        gestionAffichage.setBoutonCaractere('+', boutonAdditionner);
        gestionAffichage.setBoutonCaractere('-', boutonSoustraire);
        gestionAffichage.setBoutonCaractere('/', boutonDiviser);
        gestionAffichage.setBoutonCaractere('*', boutonMultiplier);
        gestionAffichage.setBoutonCaractere('(', boutonParentheseG);
        gestionAffichage.setBoutonCaractere(')', boutonParentheseD);
        gestionAffichage.setBoutonCaractere('.', boutonPoint);
    }

    /**
     * Crée et configure l'action associée à l'élément de menu "À propos".
     * Lorsque l'élément de menu est déclenché, une boîte de dialogue d'information
     * est affichée avec des détails sur le cours de programmation et les auteurs du programme.
     */
    private void createMenuAPropos() {
        menuItemAPropos.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Cours de programmation 203 en SIM\n" +
                    "Cégep Limoilou A23\n" +
                    "par: Émile Roy, Jayke Gagné et Antoine Houde :)");
            alert.setHeaderText("SIM 203");
            alert.setTitle("Calculateur Avancé");
            alert.show();
        });
    }

    /**
     * Définit le texte affiché dans le champ de texte de l'interface graphique.
     *
     * @param stringAAfficher Le texte à afficher dans le champ de texte.
     */
    public void setStringAffiche(String stringAAfficher) {
        stringAffiche.setText(stringAAfficher);
    }

    /**
     * Récupère l'instance de la classe GestionAffichage associée à ce contrôleur.
     *
     * @return L'instance de la classe GestionAffichage.
     */
    public GestionAffichage getGestionAffichage() {
        return this.gestionAffichage;
    }

    /**
     * Récupère la liste des variables de l'interface graphique.
     *
     * @return La liste des variables sous forme de ListView<String>.
     */
    public ListView<String> getListeVariables() {
        return listeVariables;
    }

    /**
     * Récupère la liste des équations de l'interface graphique.
     *
     * @return La liste des équations sous forme de ListView<String>.
     */
    public ListView<String> getListeEquations() {
        return listeEquations;
    }

    /**
     * Récupère le champ de texte de l'interface graphique utilisé pour afficher du texte.
     *
     * @return Le champ de texte sous forme de TextField.
     */
    public TextField getStringAffiche() {
        return stringAffiche;
    }

    public void setGestionAffichage(GestionAffichage gestionAffichage) {
        this.gestionAffichage = gestionAffichage;
    }

    /**
     * Récupère le texte actuellement affiché dans le champ de texte de l'interface graphique.
     *
     * @return Le texte actuellement affiché dans le champ de texte.
     */
    public String getStringAfficheTexte() {
        return stringAffiche.getText();
    }

    /**
     * Récupère l'élément de menu associé à l'assistance visuelle dans l'interface graphique.
     *
     * @return L'élément de menu de type CheckMenuItem associé à l'assistance visuelle.
     */
    public CheckMenuItem getMenuItemAssistanceVisuelle() {
        return menuItemAssistanceVisuelle;
    }

    public ListView<String> getListeConstantes() {
        return listeConstantes;
    }

    /**
     * Initialise le contrôleur, crée les instances nécessaires,
     * configure les éléments de l'interface graphique et gère les initialisations.
     *
     * @param location  L'emplacement utilisé pour résoudre les chemins relatifs
     *                  pour l'objet racine, ou null si l'emplacement n'est pas connu.
     * @param resources Les ressources utilisées pour localiser l'objet racine,
     *                  ou null si l'objet racine n'a pas été localisé.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gestionAffichage = new GestionAffichage(this);
        setBoutonsCalculatrice();
        createMenuAPropos();
    }
}
