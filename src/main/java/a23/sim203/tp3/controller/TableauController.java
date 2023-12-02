/*

Cours : 420-201 - Introduction a la programmation
Groupe : 1
Nom: Gagne
Prenom: Jayke
DA: 2264136
*/

package a23.sim203.tp3.controller;

import a23.sim203.tp3.modele.MoteurCalcul;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.mariuszgromada.math.mxparser.Constant;

import java.net.URL;
import java.util.*;

public class TableauController implements Initializable {

    MoteurCalcul moteurCalcul;
    @FXML
    TableView<Map.Entry<String, Map<String, Double>>> tableView;
    @FXML
    private TableColumn<Map, Double> tempsTableColumn;

    public TableauController() {
        this.tableView = new TableView<>();

    }

    public void ajouterColonnesTableau() {
        Set<String> nomsColonnes = new TreeSet<>();
        for (Map<String, Constant> innerMap : moteurCalcul.getHistorique().values()) {
            nomsColonnes.addAll(innerMap.keySet());
        }

        String[] tableauNomsColonnes = nomsColonnes.toArray(new String[0]);

        for (String nomColonne : tableauNomsColonnes) {
            TableColumn<Map.Entry<String, Map<String, Double>>, Double> colonne = new TableColumn<>(nomColonne);
            colonne.setCellValueFactory(cellData -> {
                Map<String, Double> rowData = cellData.getValue().getValue();
                if (rowData != null && rowData.containsKey(nomColonne)) {
                    return new ReadOnlyObjectWrapper<>(rowData.get(nomColonne));
                } else {
                    return null;
                }
            });
            tableView.getColumns().add(colonne);
            tableView.refresh();
        }
    }

    public void refreshDonneesTableau() {
        Map<String, Double> nouvelleDonnee = new HashMap<>();
        nouvelleDonnee.put(moteurCalcul.getPasDeTempsActuelMoinsUn().toString(), ((Constant) moteurCalcul.getHistorique().get(moteurCalcul.getPasDeTempsActuelMoinsUn())).getConstantValue());
        tableView.getItems().add(Map.entry(moteurCalcul.getPasDeTempsActuelMoinsUn().toString(), nouvelleDonnee));
    }


    public void setMoteurCalcul(MoteurCalcul moteurCalcul) {
        this.moteurCalcul = moteurCalcul;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        moteurCalcul = new MoteurCalcul();
    }
}
