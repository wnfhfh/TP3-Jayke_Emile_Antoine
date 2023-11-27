/*

Cours : 420-201 - Introduction a la programmation
Groupe : 1
Nom: Gagne
Prenom: Jayke
DA: 2264136
*/

package a23.sim203.tp3.controller;

import a23.sim203.tp3.modele.Equation;
import a23.sim203.tp3.modele.MoteurCalcul;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.mariuszgromada.math.mxparser.Constant;

import java.net.URL;
import java.util.Collection;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

public class TableauController implements Initializable {

    MoteurCalcul moteurCalcul;
    @FXML
    TableView<String> tableView;

    public TableauController() {
        this.tableView = new TableView<>();
    }

    public void ajouterEquationTableau() {
        for (String s : moteurCalcul.getAllConstantes()) {
            tableView.getItems().add(s);
        }
    }

    public void setMoteurCalcul(MoteurCalcul moteurCalcul) {
        this.moteurCalcul = moteurCalcul;
}

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        moteurCalcul = new MoteurCalcul();
    }
}
