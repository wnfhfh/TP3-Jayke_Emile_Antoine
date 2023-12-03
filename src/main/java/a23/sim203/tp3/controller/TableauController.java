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
import org.mariuszgromada.math.mxparser.Constant;

import java.net.URL;
import java.util.*;

public class TableauController implements Initializable {

    MoteurCalcul moteurCalcul;
    @FXML
    TableView<Map<String, Constant>>tableView;
    @FXML
    private TableColumn<Map, Double> tempsTableColumn;

    public TableauController() {
        this.tableView = new TableView<>();
    }

    public void ajouterColonnesTableau() {
        Set<String> columnNames = new TreeSet<>();

        for (Map<String, Constant> innerMap : moteurCalcul.getHistorique().values()) {
            columnNames.addAll(innerMap.keySet());
        }

        tableView.getColumns().clear();

        for (String columnName : columnNames) {
            TableColumn<Map<String, Constant>, Double> column = new TableColumn<>(columnName);
            column.setCellValueFactory(cellData -> {
                Map<String, Constant> rowData = cellData.getValue();
                Constant constantValue = (rowData != null) ? rowData.get(columnName) : null;
                return new ReadOnlyObjectWrapper<>((constantValue != null) ? constantValue.getConstantValue() : null);
            });

            tableView.getColumns().add(column);
        }

        tableView.refresh();
    }


    public void refreshDonneesTableau() {
      tableView.getItems().add(moteurCalcul.getHistorique().get(moteurCalcul.getPasDeTempsActuelMoinsUn()));
    }


    public void setMoteurCalcul(MoteurCalcul moteurCalcul) {
        this.moteurCalcul = moteurCalcul;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        moteurCalcul = new MoteurCalcul();
    }
}
