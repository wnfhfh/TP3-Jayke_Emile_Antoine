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
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.mariuszgromada.math.mxparser.Constant;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

public class SimFenetreController {

    @FXML
    private Button boutonLancer;
    @FXML
    private Button boutonPause;
    @FXML
    private Button boutonArret;
    @FXML
    private Button boutonTableau;
    @FXML
    private Button boutonGraphique;
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
    private Slider sliderMParPx;

    @FXML
    private AnchorPane anchorPane;
    HashMap<Circle, String[]> paramsAllPlanetes;
    Set<Line> lines;
    private int shownStagesCount = 2;


    private SimulationService simulationService = new SimulationService();

    @FXML
    void boutonArreterOnAction(ActionEvent event) {
        gestionAffichage.getAnimations().arreterChronometre();
        simulationService.cancel();
        simulationService.reset();
        for (Circle circle :
                paramsAllPlanetes.keySet()) {
            circle.setCenterX(0);
            circle.setCenterY(300);
        }
        anchorPane.getChildren().clear();
        lines.clear();
        moteurCalcul.getAncienneValeurVariableMap().get("t_").setConstantValue(0);
        boutonLancer.setDisable(false);
        boutonPause.setDisable(false);
    }

    @FXML
    void boutonLancerOnAction(ActionEvent event) {
        try {
            double scale = Double.parseDouble(echelleTemporelleTextField.getText());
            setMoteurCalcul(moteurCalcul, scale);
            simulationService.setPeriod(Duration.valueOf(dtTextField.getText() + "s"));
            moteurCalcul.getConstanteValeurMap().put("d_", new Constant("d_", simulationService.getPeriod().toSeconds() * scale));
            simulationService.start();
            addServiceListener();
            placerCerclesIni();
            lines = new HashSet<>();
            boutonLancer.setDisable(true);
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            System.out.println(e.getMessage());
            alert.setHeaderText("Veuillez entrer des valeurs valides");
            alert.setTitle("Calculateur avancée");
            alert.setContentText("Réessayez plz");
            alert.showAndWait();
        }
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
                placerCerclesIni();
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

    private void placerCerclesIni() {
        for (Circle circle :
                paramsAllPlanetes.keySet()) {
            circle.setFill(Color.BLUE);

            String xInitial = paramsAllPlanetes.get(circle)[0];
            String yInitial = paramsAllPlanetes.get(circle)[1];

            anchorPane.getChildren().add(circle);
            circle.setCenterX(Double.parseDouble(xInitial.split("=")[1]));
            circle.setCenterY(300 - Double.parseDouble(yInitial.split("=")[1]));
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
        for (Circle circle :
                paramsAllPlanetes.keySet()) {
            Double ancienCenterX = circle.getCenterX();
            Double ancienCenterY = circle.getCenterY();

            Double xInitial = Double.valueOf(paramsAllPlanetes.get(circle)[0].split("=")[1]);
            Double yInitial = Double.valueOf(paramsAllPlanetes.get(circle)[1].split("=")[1]);
            String nomXAcceleration = paramsAllPlanetes.get(circle)[2].split("=")[0];
            String nomYAcceleration = paramsAllPlanetes.get(circle)[3].split("=")[0];
            Double xVitesseInitiale = Double.valueOf(paramsAllPlanetes.get(circle)[4].split("=")[1]);
            Double yVitesseInitiale = Double.valueOf(paramsAllPlanetes.get(circle)[5].split("=")[1]);

            Double temps = moteurCalcul.getAncienneValeurVariableMap().get("t_").getConstantValue();
            Double centerX;
            Double centerY;
            if (moteurCalcul.getAncienneValeurVariableMap().containsKey(nomXAcceleration)) {
                centerX = xInitial + (xVitesseInitiale * temps) + (0.5 * moteurCalcul.getAncienneValeurVariableMap().get(nomXAcceleration.trim()).getConstantValue() * (Math.pow(temps, 2)));
            } else {
                centerX = xInitial + (xVitesseInitiale * temps) + (0.5 * moteurCalcul.getConstanteValeurMap().get(nomXAcceleration.trim()).getConstantValue() * (Math.pow(temps, 2)));
            }
            if (moteurCalcul.getAncienneValeurVariableMap().containsKey(nomYAcceleration)) {
                centerY = anchorPane.getHeight() - (yInitial + (yVitesseInitiale * temps) + (0.5 * moteurCalcul.getAncienneValeurVariableMap().get(nomYAcceleration.trim()).getConstantValue() * (Math.pow(temps, 2))));
            } else {
                centerY = anchorPane.getHeight() - (yInitial + (yVitesseInitiale * temps) + (0.5 * moteurCalcul.getConstanteValeurMap().get(nomYAcceleration.trim()).getConstantValue() * (Math.pow(temps, 2))));
            }
            if (centerX > anchorPane.getWidth() || centerX < 0 || centerY > anchorPane.getHeight() || centerY < 0) {
                boutonArreterOnAction(new ActionEvent());
                break;
            }
            circle.setCenterX(centerX);
            circle.setCenterY(centerY);

            Line line = new Line();
            anchorPane.getChildren().add(line);
            line.setStartX(ancienCenterX);
            line.setStartY(ancienCenterY);
            line.setEndX(circle.getCenterX());
            line.setEndY(circle.getCenterY());
            line.setStroke(Color.BLUE);
            line.setStrokeWidth(4);
            lines.add(line);
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
            boutonGraphique.setDisable(true);
        });
        stageGraphique.setOnCloseRequest(event1 -> {
            shownStagesCount--;
            gestionAffichage.getAnimations().sortLesMethodes(shownStagesCount, stageGraphique);
            boutonGraphique.setDisable(false);
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
            boutonTableau.setDisable(true);
        });
        stageTableau.setOnCloseRequest(event1 -> {
            shownStagesCount--;
            gestionAffichage.getAnimations().sortLesMethodes(shownStagesCount, stageTableau);
            boutonTableau.setDisable(false);
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

    public void setNombreObjets(Integer nbObjets) {
        paramsAllPlanetes = new HashMap<Circle, String[]>();
        for (int i = 0; i < nbObjets; i++) {
            Stage settingsPlaneteStage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("settingsPlanete.fxml"));
            try {
                fxmlLoader.load();
                settingsPlaneteStage.setScene(new Scene(fxmlLoader.getRoot()));
            } catch (IOException e) {
                System.out.println("probleme de load settings planete");
            }
            SettingsPlaneteController controller = fxmlLoader.getController();
            controller.getTitreTextField().setText("Valeurs pour planète " + i);
            controller.getTitreTextField().setDisable(true);
            controller.setMoteurCalculEtChoiceBoxes(moteurCalcul);

            String[] paramsPlanete = new String[6];
            controller.getBoutonOK().setOnAction(event -> {
                paramsPlanete[0] = controller.getxInitialChoiceBox().getSelectionModel().getSelectedItem();
                paramsPlanete[1] = controller.getyInitialChoiceBox().getSelectionModel().getSelectedItem();
                paramsPlanete[2] = controller.getxAccelerationChoiceBox().getSelectionModel().getSelectedItem();
                paramsPlanete[3] = controller.getyAccelerationChoiceBox().getSelectionModel().getSelectedItem();
                paramsPlanete[4] = controller.getxVitesseInitialeChoiceBox().getSelectionModel().getSelectedItem();
                paramsPlanete[5] = controller.getyVitesseInitialeChoiceBox().getSelectionModel().getSelectedItem();
                paramsAllPlanetes.put(new Circle(13), paramsPlanete);
                settingsPlaneteStage.close();
            });
            settingsPlaneteStage.showAndWait();
        }
    }
}

