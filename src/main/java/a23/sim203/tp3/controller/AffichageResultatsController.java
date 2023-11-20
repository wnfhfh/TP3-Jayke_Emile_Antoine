package a23.sim203.tp3.controller;

import a23.sim203.tp3.modele.Equation;
import a23.sim203.tp3.modele.MoteurCalcul;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Button;
import javafx.scene.layout.TilePane;

import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;

public class AffichageResultatsController implements Initializable {

    @FXML
    private Button boutonVider;

    @FXML
    private LineChart lineChart;

    @FXML
    public Button rafraichirEquationsBouton = new Button();

    @FXML
    private TilePane tilePaneEquations;

    NumberAxis axeX;
    NumberAxis axeY;

    MoteurCalcul moteurCalcul;
    HashSet<String> equationsDansGraphique;

    public void creerLineChart() {
        axeX = new NumberAxis();
        axeY = new NumberAxis();
        lineChart = new LineChart(axeX, axeY);
    }

    public void rafraichirGraphique() {

    }

    public void ajouterEquationAGraph(String nomEquation) {

    }

    public void rafraichirEquationsOnAction() {
        for (Equation equation :
                moteurCalcul.getAllEquations()) {
            Button button = new Button(equation.getNom());
            button.setOnAction(event1 -> {
                ajouterEquationAGraph(button.getText());
            });
            tilePaneEquations.getChildren().add(button);
        }
        for (Node node :
                tilePaneEquations.getChildren()) {
            Button bouton = (Button) node;
            if (!moteurCalcul.getEquationMap().containsKey(bouton.getText()) || equationsDansGraphique.contains(bouton.getText())) {
                tilePaneEquations.getChildren().remove(node);
            }
        }
    }

    public void setMoteurCalcul(MoteurCalcul moteurCalcul) {
        this.moteurCalcul = moteurCalcul;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        equationsDansGraphique = new HashSet<>();
    }
}
