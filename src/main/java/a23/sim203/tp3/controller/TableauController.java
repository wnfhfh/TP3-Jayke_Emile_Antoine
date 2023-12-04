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

/**
 * Contrôleur responsable de la gestion du tableau dans l'interface utilisateur.
 */
public class TableauController implements Initializable {
    /**
     * Moteur de calcul associé à ce contrôleur.
     */
    MoteurCalcul moteurCalcul;
    /**
     * Tableau JavaFX affiché dans l'interface utilisateur.
     */
    @FXML
    TableView<Map<String, Constant>> tableView;

    /**
     * Constructeur par défaut qui initialise le tableau.
     */
    public TableauController() {
        this.tableView = new TableView<>();
    }

    /**
     * Ajoute les colonnes au tableau en fonction des données du moteur de calcul.
     */

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

    /**
     * Rafraîchit les données du tableau avec les dernières valeurs du moteur de calcul.
     */
    public void refreshDonneesTableau() {
        tableView.getItems().add(moteurCalcul.getHistorique().get(moteurCalcul.getPasDeTempsActuelMoinsUn()));
    }

    /**
     * Définit le moteur de calcul associé à ce contrôleur.
     *
     * @param moteurCalcul Le moteur de calcul à associer.
     */
    public void setMoteurCalcul(MoteurCalcul moteurCalcul) {
        this.moteurCalcul = moteurCalcul;
    }

    /**
     * Initialise le moteur de calcul avec une instance par défaut.
     *
     * @param location  L'emplacement du fichier FXML.
     * @param resources Les ressources à utiliser.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        moteurCalcul = new MoteurCalcul();
    }
}
