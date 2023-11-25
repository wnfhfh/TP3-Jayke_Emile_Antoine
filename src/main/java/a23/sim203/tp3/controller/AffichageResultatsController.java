package a23.sim203.tp3.controller;

import a23.sim203.tp3.modele.Equation;
import a23.sim203.tp3.modele.MoteurCalcul;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.layout.TilePane;
import org.mariuszgromada.math.mxparser.Constant;

import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;

public class AffichageResultatsController implements Initializable {

    @FXML
    private Button boutonVider;

    @FXML
    private LineChart lineChart;

    @FXML
    private TilePane tilePaneEquations;

    private NumberAxis axeX;
    private NumberAxis axeY;

    private MoteurCalcul moteurCalcul;
    private HashSet<String> toutesEquationsGraphiques;
    private HashSet<XYChart.Series> equationsDejaDansGraphique;

    public void creerLineChart() {
        axeX = new NumberAxis();
        axeY = new NumberAxis();
        lineChart = new LineChart(axeX, axeY);
    }

    public void rafraichirGraphique(String nomEquation, Constant y, double endTime) { //TODO figurer si on se fie a endtime pour calc
        if (dejaDansGraphique(nomEquation) != null) {
            XYChart.Series droiteEquation = new XYChart.Series();
            equationsDejaDansGraphique.add(droiteEquation);
            droiteEquation.setName(nomEquation);
            droiteEquation.getData().add(new XYChart.Data<Double, Double>(endTime, y.getConstantValue()));
            lineChart.getData().add(droiteEquation);
        } else {
            dejaDansGraphique(nomEquation).getData().add(new XYChart.Data<Double, Double>(endTime, y.getConstantValue()));
        }
    }

    private XYChart.Series dejaDansGraphique(String nomEquation) {
        XYChart.Series seriesDejaDansGraphique = null;
        for (XYChart.Series series :
                equationsDejaDansGraphique) {
            if (nomEquation.equals(series.getName())) seriesDejaDansGraphique = series;
        }
        return seriesDejaDansGraphique;
    }

    public HashSet<String> getToutesEquationsGraphiques() {
        return toutesEquationsGraphiques;
    }

    public void ajouterEquationAGraph(String nomEquation) {
        toutesEquationsGraphiques.add(nomEquation);
    }

    @FXML
    public void rafraichirEquationsOnAction(ActionEvent event) {
        for (Equation equation :
                moteurCalcul.getAllEquations()) {
            Button button = new Button(equation.getNom());
            button.setPrefSize(100, 100);
            button.setMinSize(100, 100);
            button.setMaxSize(100, 100);
            button.setOnAction(event1 -> {
                ajouterEquationAGraph(button.getText());
            });
            tilePaneEquations.getChildren().add(button);

        }
        for (Node node :
                tilePaneEquations.getChildren()) {
            Button bouton = (Button) node;
            if (!moteurCalcul.getEquationMap().containsKey(bouton.getText()) || toutesEquationsGraphiques.contains(bouton.getText())) {
                tilePaneEquations.getChildren().remove(node);
            }
        }
    }

    public void setMoteurCalcul(MoteurCalcul moteurCalcul) {
        this.moteurCalcul = moteurCalcul;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        moteurCalcul = new MoteurCalcul();
        toutesEquationsGraphiques = new HashSet<>();
        equationsDejaDansGraphique = new HashSet<>();
        tilePaneEquations.setPadding(new Insets(10, 10, 10, 10));
    }
}
