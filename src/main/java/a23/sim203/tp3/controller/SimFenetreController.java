package a23.sim203.tp3.controller;

import a23.sim203.tp3.app.GestionAffichage;
import a23.sim203.tp3.modele.MoteurCalcul;
import a23.sim203.tp3.services.SimulationService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.util.Duration;

public class SimFenetreController {

    @FXML
    private TextField dtTextField;
    @FXML
    private TextField echelleTemporelleTextField;

    MoteurCalcul moteurCalcul;

    private SimulationService simulationService = new SimulationService();

    @FXML
    void boutonArreterOnAction(ActionEvent event) {
        simulationService.cancel();
        simulationService.reset();
    }

    @FXML
    void boutonLancerOnAction(ActionEvent event) {
        simulationService.setPeriod(Duration.valueOf(dtTextField.getText()+"s"));
        simulationService.start();
    }

    @FXML
    void boutonPauseOnAction(ActionEvent event) {

    }

    public void setMoteurCalcul(MoteurCalcul moteurCalcul) {
        this.moteurCalcul = moteurCalcul;
        simulationService.setMoteurCalcul(moteurCalcul);
    }
}

