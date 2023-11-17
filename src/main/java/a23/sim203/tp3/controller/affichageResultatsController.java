package a23.sim203.tp3.controller;

import a23.sim203.tp3.modele.MoteurCalcul;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

public class affichageResultatsController implements Initializable {

    @FXML
    private Button boutonVider;

    @FXML
    private LineChart lineChart;

    NumberAxis axeX;
    NumberAxis axeY;

    MoteurCalcul moteurCalcul;

    private void creerLineChart() {
        axeX = new NumberAxis();
        axeY = new NumberAxis();
        lineChart = new LineChart(axeX, axeY);
        
    }

    public void setMoteurCalcul(MoteurCalcul moteurCalcul) {
        this.moteurCalcul = moteurCalcul;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        creerLineChart();
    }
}
