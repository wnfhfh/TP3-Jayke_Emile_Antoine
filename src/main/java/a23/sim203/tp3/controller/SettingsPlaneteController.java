package a23.sim203.tp3.controller;

import a23.sim203.tp3.modele.MoteurCalcul;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

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

    public Button getBoutonOK() {
        return boutonOK;
    }

    public ChoiceBox<String> getxAccelerationChoiceBox() {
        return xAccelerationChoiceBox;
    }

    public ChoiceBox<String> getxInitialChoiceBox() {
        return xInitialChoiceBox;
    }

    public ChoiceBox<String> getxVitesseInitialeChoiceBox() {
        return xVitesseInitialeChoiceBox;
    }

    public ChoiceBox<String> getyAccelerationChoiceBox() {
        return yAccelerationChoiceBox;
    }

    public ChoiceBox<String> getyInitialChoiceBox() {
        return yInitialChoiceBox;
    }

    public ChoiceBox<String> getyVitesseInitialeChoiceBox() {
        return yVitesseInitialeChoiceBox;
    }

    public TextField getTitreTextField() {
        return titreTextField;
    }

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