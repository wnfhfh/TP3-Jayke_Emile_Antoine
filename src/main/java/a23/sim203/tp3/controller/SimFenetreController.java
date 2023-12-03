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
import javafx.scene.control.*;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.mariuszgromada.math.mxparser.Constant;

import java.io.IOException;
import java.util.Objects;

public class SimFenetreController {


    @FXML
    private Button boutonLancer;
    @FXML
    private Button boutonPause;
    @FXML
    private Button boutonArret;
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

    @FXML
    private ChoiceBox<String> XAccelerationChoiceBox;

    @FXML
    private ChoiceBox<String> XInitialChoiceBox;

    @FXML
    private ChoiceBox<String> XVitesseInitialeChoiceBox;

    @FXML
    private ChoiceBox<String> YAccelerationChoiceBox;

    @FXML
    private ChoiceBox<String> YInitialChoiceBox;

    @FXML
    private ChoiceBox<String> YVitesseInitialeChoiceBox;

    @FXML
    private Circle cercle;
    private int shownStagesCount = 2;


    private SimulationService simulationService = new SimulationService();

    @FXML
    void boutonArreterOnAction(ActionEvent event) {
        gestionAffichage.getAnimations().arreterChronometre();
        simulationService.cancel();
        simulationService.reset();
        cercle.setCenterX(0);
        cercle.setCenterY(0);
        moteurCalcul.getAncienneValeurVariableMap().get("t_").setConstantValue(0);
        boutonLancer.setDisable(false);
        boutonPause.setDisable(false);
    }

    @FXML
    void boutonLancerOnAction(ActionEvent event) {
        if (gestionAffichage.getAnimations().getTimeline() != null) {
            gestionAffichage.getAnimations().resumeChronometre();
        } else {
            gestionAffichage.getAnimations().departChronometre();
        }
        if (boutonPause.disabledProperty().getValue()) {
            simulationService.resumeService();
            boutonPause.setDisable(false);
        } else {
            try {
                double scale = Double.parseDouble(echelleTemporelleTextField.getText());
                setMoteurCalcul(moteurCalcul, scale);
                simulationService.setPeriod(Duration.valueOf(dtTextField.getText() + "s"));
                moteurCalcul.getConstanteValeurMap().put("d_", new Constant("d_", simulationService.getPeriod().toSeconds() * scale));
                simulationService.startService();
                addServiceListener();
                placerCercleIni();
                boutonLancer.setDisable(true);
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setHeaderText("Veuillez entrer des valeurs valides");
                alert.setTitle("Calculateur avancée");
                alert.setContentText("Réessayez plz");
                alert.showAndWait();
            }
        }
    }

    private void placerCercleIni() {
        try {
            String xInitial = XInitialChoiceBox.getSelectionModel().getSelectedItem();
            String yInitial = YInitialChoiceBox.getSelectionModel().getSelectedItem();

            cercle.setCenterX(Double.parseDouble(xInitial.split("=")[1]));
            cercle.setCenterY(Double.parseDouble(yInitial.split("=")[1]));
        } catch (NullPointerException e) {
            System.out.println("veuillez remplir les choice box plz");
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
            tableauController.refreshDonneesTableau();
            rafraichirSimulation();
        });
    }

    private void rafraichirSimulation() {
        try {
            String nomXAcceleration = XAccelerationChoiceBox.getSelectionModel().getSelectedItem().split("=")[0];
            String nomYAcceleration = YAccelerationChoiceBox.getSelectionModel().getSelectedItem().split("=")[0];
            Double xInitial = Double.valueOf(XInitialChoiceBox.getSelectionModel().getSelectedItem().split("=")[1]);
            Double yInitial = Double.valueOf(YInitialChoiceBox.getSelectionModel().getSelectedItem().split("=")[1]);
            Double xVitesseInitiale = Double.valueOf(XVitesseInitialeChoiceBox.getSelectionModel().getSelectedItem().split("=")[1]);
            Double yVitesseInitiale = Double.valueOf(YVitesseInitialeChoiceBox.getSelectionModel().getSelectedItem().split("=")[1]);

            Double temps = moteurCalcul.getAncienneValeurVariableMap().get("t_").getConstantValue();
            Double centerX = xInitial + (xVitesseInitiale * temps) + (0.5 * moteurCalcul.getAncienneValeurVariableMap().get(nomXAcceleration).getConstantValue() * (Math.pow(temps, 2)));
            Double centerY = yInitial + (yVitesseInitiale * temps) + (0.5 * moteurCalcul.getAncienneValeurVariableMap().get(nomYAcceleration).getConstantValue() * (Math.pow(temps, 2)));

            cercle.setCenterX(centerX);
            cercle.setCenterY(centerY);
        } catch (NullPointerException e) {
            System.out.println("mettre des données dans tous les champs svp");
        }
        //TODO ajouter des limites au cercle pour pas qu'il s'autoyeet, permettre de mettre autre chose qu'un cercle
    }

    @FXML
    void boutonPauseOnAction(ActionEvent event) {
        boutonPause.setDisable(true);
        boutonLancer.setDisable(false);
        simulationService.pauseService();
        gestionAffichage.getAnimations().pauseChronometre();
    }

    @FXML
    void afficherGraphiqueOnAction(ActionEvent event) {
        stageGraphique.setOnShown(event1 -> {
            shownStagesCount++;
        });
        stageGraphique.setOnCloseRequest(event1 -> {
            shownStagesCount--;
            gestionAffichage.getAnimations().sortLesMethodes(shownStagesCount,stageGraphique);
        });
        stageGraphique.show();
        gestionAffichage.getAnimations().sortLesMethodes(shownStagesCount, stageGraphique);
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
        affichageResultatsController.rafraichirEquations();
        simulationService.setAffichageResultatsController(affichageResultatsController);

        Stage stageGraphique = new Stage();
        this.stageGraphique = stageGraphique;
        stageGraphique.setScene(new Scene(root));
    }

    @FXML
    public void setMenuItemTableTemps(ActionEvent event) {
        stageTableau.setOnShown(event1 -> {
            shownStagesCount++;
        });
        stageTableau.setOnCloseRequest(event1 -> {
            shownStagesCount--;
            gestionAffichage.getAnimations().sortLesMethodes(shownStagesCount,stageTableau);
        });
        tableauController.setMoteurCalcul(moteurCalcul);
        tableauController.ajouterColonnesTableau();
        stageTableau.show();
        gestionAffichage.getAnimations().sortLesMethodes(shownStagesCount, stageTableau);

    }

    public void creerTableau() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("affichageTableau.fxml"));
        Parent root = null;
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        tableauController = fxmlLoader.getController();
        tableauController.setMoteurCalcul(moteurCalcul);
        tableauController.ajouterColonnesTableau();
        simulationService.setTableauController(tableauController);
        Scene scene = new Scene(root);
        stageTableau = new Stage();
        stageTableau.setScene(scene);
    }

    public void setMoteurCalcul(MoteurCalcul moteurCalcul) {
        this.moteurCalcul = moteurCalcul;
        simulationService.setMoteurCalcul(moteurCalcul);
    }

    public void setMoteurCalcul(MoteurCalcul moteurCalcul, double scale) {
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

    public void setChoiceBoxes() {
        for (String equationString :
                moteurCalcul.getAllEquationsString()) {
            XAccelerationChoiceBox.getItems().add(equationString);
            YAccelerationChoiceBox.getItems().add(equationString);
        }
        for (String constanteString :
                moteurCalcul.getToutesLesConstantesString()) {
            XInitialChoiceBox.getItems().add(constanteString);
            YInitialChoiceBox.getItems().add(constanteString);
            XAccelerationChoiceBox.getItems().add(constanteString);
            YAccelerationChoiceBox.getItems().add(constanteString);
            XVitesseInitialeChoiceBox.getItems().add(constanteString);
            YVitesseInitialeChoiceBox.getItems().add(constanteString);
        }
    }
}

