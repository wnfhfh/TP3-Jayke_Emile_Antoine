package a23.sim203.tp3.controller;

import a23.sim203.tp3.app.GestionAffichage;
import a23.sim203.tp3.controller.affichageResultatsController;
import a23.sim203.tp3.modele.MoteurCalcul;
import a23.sim203.tp3.services.SimulationService;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class SimFenetreController {

    @FXML
    private TextField dtTextField;
    @FXML
    private TextField echelleTemporelleTextField;

    MoteurCalcul moteurCalcul;

    GestionAffichage gestionAffichage;

    private SimulationService simulationService = new SimulationService();

    @FXML
    void boutonArreterOnAction(ActionEvent event) {
        simulationService.cancel();
        simulationService.reset();
    }

    @FXML
    void boutonLancerOnAction(ActionEvent event) {
        simulationService.setPeriod(Duration.valueOf(dtTextField.getText() + "s"));
        simulationService.start();
    }

    @FXML
    void boutonPauseOnAction(ActionEvent event) {

    }

    @FXML
    void boutonVoirGraphiqueOnAction(ActionEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("affichageResultats.fxml"));
        Parent root = null;
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        affichageResultatsController controller = fxmlLoader.getController();
        controller.setMoteurCalcul(moteurCalcul);
        Scene sceneGraph = new Scene(root);
        gestionAffichage.getStage().setScene(sceneGraph);
        gestionAffichage.getStage().show();
    }

    public void setMoteurCalcul(MoteurCalcul moteurCalcul) {
        this.moteurCalcul = moteurCalcul;
        simulationService.setMoteurCalcul(moteurCalcul);
    }

    public void setGestionAffichage(GestionAffichage gestionAffichage) {
        this.gestionAffichage = gestionAffichage;
    }
}

