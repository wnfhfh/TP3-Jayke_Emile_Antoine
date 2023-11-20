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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.mariuszgromada.math.mxparser.Constant;

import java.io.IOException;
import java.net.URL;

public class SimFenetreController {


    @FXML
    private Button boutonLancer;
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
        boutonLancer.setDisable(false);
    }

    @FXML
    void boutonLancerOnAction(ActionEvent event) {
        try {
            double scale = Double.parseDouble(echelleTemporelleTextField.getText());
            setMoteurCalcul(moteurCalcul, scale);
            simulationService.setPeriod(Duration.valueOf(dtTextField.getText() + "s"));
            moteurCalcul.getConstanteValeurMap().put("dt_", new Constant("dt_", simulationService.getPeriod().toSeconds() * scale));
            moteurCalcul.ajouteEquation("t_=t_+dt_");
            simulationService.start();
            boutonLancer.setDisable(true);
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("Veuillez entrer des valeurs valides");
            alert.setTitle("Calculateur avancée");
            alert.setContentText("Réessayez plz");
            alert.showAndWait();
        }
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
    @FXML
    public void setMenuItemTableTemps(ActionEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/controller/affichageTableau.fxml"));
        Parent root = null;
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        TableauController controller = fxmlLoader.getController();
        Scene scene = new Scene(root);
        gestionAffichage.getStage().setScene(scene);
        gestionAffichage.getStage().show();
        controller.setMoteurCalcul(moteurCalcul);
    }

    public void setMoteurCalcul(MoteurCalcul moteurCalcul) {
//        double scale = Double.valueOf(echelleTemporelleTextField.getText());
        this.moteurCalcul = moteurCalcul;
        simulationService.setMoteurCalcul(moteurCalcul);
    }

    public void setMoteurCalcul(MoteurCalcul moteurCalcul, double scale) {
//        double scale = Double.valueOf(echelleTemporelleTextField.getText());
        this.moteurCalcul = moteurCalcul;
        simulationService.setMoteurCalculEtScale(moteurCalcul, scale);
    }

    public void setGestionAffichage(GestionAffichage gestionAffichage) {
        this.gestionAffichage = gestionAffichage;
    }

}

