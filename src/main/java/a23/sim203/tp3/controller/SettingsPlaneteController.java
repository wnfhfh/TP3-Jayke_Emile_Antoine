package a23.sim203.tp3.controller;

import a23.sim203.tp3.modele.MoteurCalcul;
import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

/**
 * Le contrôleur {@code SettingsPlaneteController} gère les paramètres d'une planète dans une interface utilisateur.
 * Il utilise des composants tels que des ChoiceBox et un TextField pour permettre à l'utilisateur de définir les paramètres de la planète.
 */
public class SettingsPlaneteController {

    @FXML
    private Button boutonOK;

    @FXML
    private ChoiceBox<String> xAccelerationChoiceBox;

    @FXML
    private ChoiceBox<String> xInitialChoiceBox;

    @FXML
    private ChoiceBox<String> xVitesseInitialeChoiceBox;

    @FXML
    private ChoiceBox<String> yAccelerationChoiceBox;

    @FXML
    private TextField titreTextField;

    @FXML
    private ChoiceBox<String> yInitialChoiceBox;

    @FXML
    private ChoiceBox<String> yVitesseInitialeChoiceBox;

    /**
     * Obtient le bouton OK associé à l'instance de la classe.
     *
     * @return Le bouton OK.
     */
    public Button getBoutonOK() {
        return boutonOK;
    }

    /**
     * Obtient la ChoiceBox pour l'accélération en x associée à l'instance de la classe.
     *
     * @return La ChoiceBox pour l'accélération en x.
     */
    public ChoiceBox<String> getxAccelerationChoiceBox() {
        return xAccelerationChoiceBox;
    }

    /**
     * Obtient la ChoiceBox pour la position initiale en x associée à l'instance de la classe.
     *
     * @return La ChoiceBox pour la position initiale en x.
     */
    public ChoiceBox<String> getxInitialChoiceBox() {
        return xInitialChoiceBox;
    }

    /**
     * Obtient la ChoiceBox pour la vitesse initiale en x associée à l'instance de la classe.
     *
     * @return La ChoiceBox pour la vitesse initiale en x.
     */
    public ChoiceBox<String> getxVitesseInitialeChoiceBox() {
        return xVitesseInitialeChoiceBox;
    }

    /**
     * Obtient la ChoiceBox pour l'accélération en y associée à l'instance de la classe.
     *
     * @return La ChoiceBox pour l'accélération en y.
     */
    public ChoiceBox<String> getyAccelerationChoiceBox() {
        return yAccelerationChoiceBox;
    }

    /**
     * Obtient la ChoiceBox pour la position initiale en y associée à l'instance de la classe.
     *
     * @return La ChoiceBox pour la position initiale en y.
     */
    public ChoiceBox<String> getyInitialChoiceBox() {
        return yInitialChoiceBox;
    }

    /**
     * Obtient la ChoiceBox pour la vitesse initiale en y associée à l'instance de la classe.
     *
     * @return La ChoiceBox pour la vitesse initiale en y.
     */
    public ChoiceBox<String> getyVitesseInitialeChoiceBox() {
        return yVitesseInitialeChoiceBox;
    }

    /**
     * Obtient le champ de texte pour le titre associé à l'instance de la classe.
     *
     * @return Le champ de texte pour le titre.
     */
    public TextField getTitreTextField() {
        return titreTextField;
    }

    /**
     * Initialise les ChoiceBoxes avec les équations et constantes du moteur de calcul donné.
     *
     * @param moteurCalcul Le moteur de calcul à partir duquel obtenir les équations et constantes.
     */
    public void setMoteurCalculEtChoiceBoxes(MoteurCalcul moteurCalcul) {
        for (String equationString :
                moteurCalcul.getAllEquationsString()) {
            xAccelerationChoiceBox.getItems().add(equationString);
            yAccelerationChoiceBox.getItems().add(equationString);
        }
        for (String constanteString :
                moteurCalcul.getToutesLesConstantesString()) {
            xInitialChoiceBox.getItems().add(constanteString);
            yInitialChoiceBox.getItems().add(constanteString);
            xAccelerationChoiceBox.getItems().add(constanteString);
            yAccelerationChoiceBox.getItems().add(constanteString);
            xVitesseInitialeChoiceBox.getItems().add(constanteString);
            yVitesseInitialeChoiceBox.getItems().add(constanteString);
        }
    }
}