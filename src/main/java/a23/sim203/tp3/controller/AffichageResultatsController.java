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

    Double i = 0d;

    @FXML
    private Button boutonVider;

    @FXML
    private LineChart lineChart;

    @FXML
    private TilePane tilePaneEquations;

    final private NumberAxis axeX = new NumberAxis();
    final private NumberAxis axeY = new NumberAxis();

    private MoteurCalcul moteurCalcul;
    private HashSet<String> boutonsCliques;
    private HashSet<XYChart.Series> seriesDejaDansGraphique;


    public void creerLineChart() {
//        XYChart.Series series = new XYChart.Series<>();
//        series.getData().addAll(
//                new XYChart.Data<>(1, 2),
//                new XYChart.Data<>(2, 2),
//                new XYChart.Data<>(3, 2),
//                new XYChart.Data<>(4, 2),
//                new XYChart.Data<>(5, 2));
//        lineChart.getData().add(series);
    }

    public void rafraichirGraphique(String nomEquation, Constant y, double endTime) { //TODO figurer si on se fie a endtime pour calc
        if (dejaDansGraphique(nomEquation) != null) {
            XYChart.Series serieARafraichir = dejaDansGraphique(nomEquation);
            serieARafraichir.setName(nomEquation);
            serieARafraichir.getData().add(new XYChart.Data<Double, Double>(endTime, y.getConstantValue()));
//            lineChart.getData().add(serieARafraichir);
        } else {
            XYChart.Series aAjouter = new XYChart.Series();
            aAjouter.setName(nomEquation);
            seriesDejaDansGraphique.add(aAjouter);
            aAjouter.getData().add(new XYChart.Data<Double, Double>(endTime, y.getConstantValue()));
            lineChart.setCreateSymbols(true);
            lineChart.getData().add(aAjouter);
        }
    }

    private XYChart.Series dejaDansGraphique(String nomEquation) {
        XYChart.Series seriesDejaDansGraphique = null;
        for (XYChart.Series series :
                this.seriesDejaDansGraphique) {
            if (nomEquation.equals(series.getName())) seriesDejaDansGraphique = series;
        }
        return seriesDejaDansGraphique;
    }

    public HashSet<String> getBoutonsCliques() {
        return boutonsCliques;
    }

    public void ajouterEquationAGraph(String nomEquation) {
        boutonsCliques.add(nomEquation);
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
                tilePaneEquations.getChildren().remove(button);
            });
            tilePaneEquations.getChildren().add(button);
        }

        for (Node node :
                tilePaneEquations.getChildren()) {
            Button bouton = (Button) node;
            if (boutonsCliques.contains(bouton.getText())) {
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
        boutonsCliques = new HashSet<>();
        seriesDejaDansGraphique = new HashSet<>();
        tilePaneEquations.setPadding(new Insets(10, 10, 10, 10));
        boutonVider.setOnAction(event -> {
            boutonsCliques.clear();
            lineChart.getData().clear();
            rafraichirEquationsOnAction(event);
        });
    }
}
