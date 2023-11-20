/*

Cours : 420-201 - Introduction a la programmation
Groupe : 1
Nom: Gagne
Prenom: Jayke
DA: 2264136
*/

package a23.sim203.tp3.controller;

import a23.sim203.tp3.app.GestionAffichage;
import a23.sim203.tp3.modele.MoteurCalcul;
import a23.sim203.tp3.services.SimulationService;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.ResourceBundle;

public class TableauController implements Initializable {

    MoteurCalcul moteurCalcul;
    private Button button;

    GestionAffichage gestionAffichage;

    public TableauController() {
        button = new Button();
    }

    public void setButton(ActionEvent actionEvent) {

    }

    public void setMoteurCalcul(MoteurCalcul moteurCalcul) {
//        double scale = Double.valueOf(echelleTemporelleTextField.getText());
        this.moteurCalcul = moteurCalcul;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        new TableauController();
    }
}
