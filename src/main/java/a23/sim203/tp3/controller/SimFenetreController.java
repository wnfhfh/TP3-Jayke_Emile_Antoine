package a23.sim203.tp3.controller;

import a23.sim203.tp3.app.GestionAffichage;
import a23.sim203.tp3.modele.Equation;
import a23.sim203.tp3.modele.MoteurCalcul;
import a23.sim203.tp3.services.SimulationService;
import a23.sim203.tp3.vue.Animations;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.mariuszgromada.math.mxparser.Constant;

import java.io.IOException;

public class SimFenetreController {


    @FXML
    private Button boutonLancer;
    @FXML
    private TextField dtTextField;
    @FXML
    private TextField echelleTemporelleTextField;

    private Stage stageSimulation;
    private Stage stageCalculatrice;
    private Stage stageTableau;
    private Stage stageGraphique;
    MoteurCalcul moteurCalcul;
    GestionAffichage gestionAffichage;
    private AffichageResultatsController affichageResultatsController;

    private TableauController tableauController;


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
            moteurCalcul.getConstanteValeurMap().put("d_", new Constant("d_", simulationService.getPeriod().toSeconds() * scale));
            moteurCalcul.ajouteEquation("t_=t_+d_");
            moteurCalcul.getAncienneValeurVariableMap().put("t_", new Constant("t_", 0));
            simulationService.start();
            addServiceListener();

            boutonLancer.setDisable(true);
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("Veuillez entrer des valeurs valides");
            alert.setTitle("Calculateur avancée");
            alert.setContentText("Réessayez plz");
            alert.showAndWait();
        }
    }

    private void addServiceListener() {
        simulationService.setOnSucceeded(event -> {

            for (String equationAMettreDansGraphique :
                    affichageResultatsController.getBoutonsCliques()) {
                affichageResultatsController.rafraichirGraphique(equationAMettreDansGraphique,
                        moteurCalcul.getAncienneValeurVariableMap().get(equationAMettreDansGraphique),
                        moteurCalcul.getAncienneValeurVariableMap().get("t_").getConstantValue());
            }

            tableauController.ajouterEquationTableau();
        });
    }

    @FXML
    void boutonPauseOnAction(ActionEvent event) {

    }

    @FXML
    void afficherGraphiqueOnAction(ActionEvent event) {
        stageGraphique.show();
    }

    public void creerGraphique() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("affichageResultats.fxml"));
        Parent root = null;
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        affichageResultatsController = fxmlLoader.getController();
        affichageResultatsController.setMoteurCalcul(moteurCalcul);
        affichageResultatsController.creerLineChart();
        affichageResultatsController.rafraichirEquationsOnAction(new ActionEvent());
        simulationService.setAffichageResultatsController(affichageResultatsController);

        Stage stageGraphique = new Stage();
        this.stageGraphique = stageGraphique;
        stageGraphique.setScene(new Scene(root));
    }

    @FXML
    public void setMenuItemTableTemps(ActionEvent event) {
        stageTableau.show();
        tableauController.setMoteurCalcul(moteurCalcul);
        gestionAffichage.getAnimations().partageDesFenetresTableau(stageSimulation, stageCalculatrice, stageTableau);
    }

    public void creerTableau() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("affichageTableau.fxml"));
        Parent root = null;
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        tableauController = new TableauController();
        fxmlLoader.setController(tableauController);
        tableauController.setMoteurCalcul(moteurCalcul);
        tableauController.ajouterEquationTableau();
        simulationService.setTableauController(tableauController);
        Scene scene = new Scene(root);
        Stage stageTableau = new Stage();
        this.stageTableau = stageTableau;
        stageTableau.setScene(scene);
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
        this.stageCalculatrice = gestionAffichage.getStage();
    }

    public void setStageSimulation(Stage stageSimulation) {
        this.stageSimulation = stageSimulation;
    }
}

