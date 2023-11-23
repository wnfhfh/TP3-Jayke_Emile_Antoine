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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.util.Callback;

import java.net.URL;
import java.util.EventListener;
import java.util.ResourceBundle;

public class TableauController implements Initializable {

    MoteurCalcul moteurCalcul;
    SimFenetreController simFenetreController;
    private Button button;

    GestionAffichage gestionAffichage;

    public TableauController() {
        button = new Button();
    }

    public void list(Button button){

        button.setOnAction(event -> {
            TableView<String> tableView = new TableView<>();
            tableView.getItems().add(moteurCalcul.getAllConstantes().toString());
        });
        simFenetreController.setMenuItemTableTemps(new ActionEvent());
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
