/*

Cours : 420-201 - Introduction a la programmation
Groupe : 1
Nom: Gagne
Prenom: Jayke
DA: 2264136
*/

package a23.sim203.tp3.controller;

import a23.sim203.tp3.app.GestionAffichage;
import a23.sim203.tp3.modele.Equation;
import a23.sim203.tp3.modele.MoteurCalcul;
import a23.sim203.tp3.services.SimulationService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.mariuszgromada.math.mxparser.Constant;

import java.net.URL;
import java.util.Collection;
import java.util.ResourceBundle;
import java.util.Set;

public class TableauController implements Initializable {

    MoteurCalcul moteurCalcul;
    private Button button;

    @FXML
    TableView<Collection<Equation>> tableViewG;
    @FXML
    TableView<Set<String>> tableViewD;

    public TableauController() {
        button = new Button();
    }

    public void tableau(Collection<Equation> equation, Set<String> constant){
        tableViewG.getItems().add(equation);
        tableViewD.getItems().add(constant);
    }

    public void setMoteurCalcul(MoteurCalcul moteurCalcul) {
        this.moteurCalcul = moteurCalcul;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        new TableauController();
    }
}
