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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;
import org.mariuszgromada.math.mxparser.Constant;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * Le contrôleur {@code SimFenetreController} gère l'interface utilisateur de la simulation.
 * Il utilise des composants tels que des boutons, des champs de texte, un slider et des éléments graphiques pour permettre à l'utilisateur de contrôler et visualiser la simulation.
 */
public class SimFenetreController {


    @FXML
    private ImageView imageViewBackground;
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
    @FXML
    private TextField mParPxTextField;
    private Stage stageSimulation;
    private Stage stageCalculatrice;
    private Stage stageTableau;
    private Stage stageGraphique;
    MoteurCalcul moteurCalcul;
    GestionAffichage gestionAffichage;
    private AffichageResultatsController affichageResultatsController;
    private TableauController tableauController;
    @FXML
    private AnchorPane anchorPane;
    HashMap<Circle, String[]> paramsAllPlanetes;
    Set<Line> lines;
    private boolean isStopped = false;
    Double mParPx = 1.0;
    private int shownStagesCount = 2;


    private SimulationService simulationService = new SimulationService();

    /**
     * Méthode appelée lors de l'action du bouton "Arrêter" de la simulation.
     * Arrête le chronomètre, annule le service de simulation, réinitialise les valeurs, et réactive les boutons de contrôle.
     *
     * @param event L'événement associé à l'action du bouton "Arrêter".
     */
    @FXML
    void boutonArreterOnAction(ActionEvent event) {
        isStopped = true;
        gestionAffichage.getAnimations().arreterChronometre();
        simulationService.cancel();
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

    /**
     * Méthode appelée lors de l'action du bouton "Lancer" de la simulation.
     * Initialise et lance la simulation avec les paramètres spécifiés par l'utilisateur.
     * Affiche une boîte de dialogue d'erreur en cas d'entrée invalide.
     *
     * @param event L'événement associé à l'action du bouton "Lancer".
     */
    @FXML
    void boutonLancerOnAction(ActionEvent event) {
        simulationService.reset();
        if (boutonPause.disabledProperty().getValue()) {
            simulationService.resumeService();
            boutonPause.setDisable(false);
        } else {
            try {
                isStopped = false;
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
        }
        if (gestionAffichage.getAnimations().getTimeline() != null) {
            gestionAffichage.getAnimations().resumeChronometre();
        } else {
            gestionAffichage.getAnimations().departChronometre();
        }
    }

    /**
     * Place les cercles initiaux des planètes sur l'interface graphique.
     * Les cercles sont positionnés en fonction des coordonnées initiales spécifiées.
     */
    private void placerCerclesIni() {
        try {
            mParPx = Double.valueOf(mParPxTextField.getText());
        } catch (NumberFormatException e) {
            System.out.println("m par px invalide :/");
        }
        for (Circle circle :
                paramsAllPlanetes.keySet()) {
            circle.setFill(Color.BLUE);

            String xInitial = paramsAllPlanetes.get(circle)[0];
            String yInitial = paramsAllPlanetes.get(circle)[1];

            anchorPane.getChildren().add(circle);
            circle.setCenterX(mParPx * (Double.parseDouble(xInitial.split("=")[1])));
            circle.setCenterY(300 - mParPx * (Double.parseDouble(yInitial.split("=")[1])));
        }

    }

    /**
     * Ajoute un écouteur au service de simulation pour gérer les événements réussis.
     * Lorsque la simulation est terminée avec succès, met à jour le graphique, le tableau et rafraîchit la simulation.
     */
    private void addServiceListener() {
        simulationService.setOnSucceeded(event -> {
            if (!isStopped) {
                for (String equationAMettreDansGraphique :
                        affichageResultatsController.getBoutonsCliques()) {
                    affichageResultatsController.rafraichirGraphique(equationAMettreDansGraphique,
                            moteurCalcul.getAncienneValeurVariableMap().get(equationAMettreDansGraphique),
                            moteurCalcul.getAncienneValeurVariableMap().get("t_").getConstantValue());
                }
                tableauController.refreshDonneesTableau();
                rafraichirSimulation();
            }
        });
    }

    /**
     * Rafraîchit la simulation en mettant à jour la position des planètes et en ajoutant des lignes de trajectoire.
     * Vérifie également les limites de l'espace de simulation et arrête la simulation si une limite est dépassée.
     */
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
            Double centerXEnVrai;
            Double centerYEnVrai;
            if (moteurCalcul.getAncienneValeurVariableMap().containsKey(nomXAcceleration)) {
                centerXEnVrai = xInitial + (xVitesseInitiale * temps) + (0.5 * moteurCalcul.getAncienneValeurVariableMap().get(nomXAcceleration.trim()).getConstantValue() * (Math.pow(temps, 2)));
            } else {
                centerXEnVrai = xInitial + (xVitesseInitiale * temps) + (0.5 * moteurCalcul.getConstanteValeurMap().get(nomXAcceleration.trim()).getConstantValue() * (Math.pow(temps, 2)));
            }
            if (moteurCalcul.getAncienneValeurVariableMap().containsKey(nomYAcceleration)) {
                centerYEnVrai = anchorPane.getHeight() - (yInitial + (yVitesseInitiale * temps) + (0.5 * moteurCalcul.getAncienneValeurVariableMap().get(nomYAcceleration.trim()).getConstantValue() * (Math.pow(temps, 2))));
            } else {
                centerYEnVrai = anchorPane.getHeight() - (yInitial + (yVitesseInitiale * temps) + (0.5 * moteurCalcul.getConstanteValeurMap().get(nomYAcceleration.trim()).getConstantValue() * (Math.pow(temps, 2))));
            }
            if (centerXEnVrai > anchorPane.getWidth() || centerXEnVrai < 0 || centerYEnVrai > anchorPane.getHeight() || centerYEnVrai < 0) {
                boutonArreterOnAction(new ActionEvent());
            }
            circle.setCenterX((centerXEnVrai - ancienCenterX) * mParPx + ancienCenterX);
            circle.setCenterY((centerYEnVrai - ancienCenterY) * mParPx + ancienCenterY);
            ajouterLine(circle, ancienCenterX, ancienCenterY);
        }
    }

    private void ajouterLine(Circle circle, Double ancienCenterX, Double ancienCenterY) {
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

    /**
     * Action déclenchée lorsqu'on appuie sur le bouton de pause.
     * Désactive le bouton de pause, active le bouton de lancer, met en pause le service de simulation et le chronomètre.
     */
    @FXML
    void boutonPauseOnAction(ActionEvent event) {
        boutonPause.setDisable(true);
        boutonLancer.setDisable(false);
        simulationService.pauseService();
        gestionAffichage.getAnimations().pauseChronometre();
    }

    /**
     * Action déclenchée lorsqu'on souhaite afficher le graphique.
     * Configure les événements pour la gestion de l'ouverture et de la fermeture de la fenêtre graphique.
     * Affiche ensuite la fenêtre graphique et ajuste les méthodes d'animation en fonction du nombre de fenêtres affichées.
     */
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

    /**
     * Crée le graphique en chargeant la vue "affichageResultats.fxml".
     * Configure le contrôleur d'affichage des résultats en lien avec le moteur de calcul.
     * Initialise le graphique, rafraîchit les équations et configure le service de simulation.
     * Initialise la fenêtre graphique.
     */
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

    /**
     * Action déclenchée lorsqu'on sélectionne l'option de menu pour afficher le tableau des temps.
     * Configure les événements pour la gestion de l'ouverture et de la fermeture de la fenêtre du tableau des temps.
     * Configure le contrôleur du tableau des temps en lien avec le moteur de calcul, ajoute les colonnes au tableau.
     * Affiche ensuite la fenêtre du tableau des temps et ajuste les méthodes d'animation en fonction du nombre de fenêtres affichées.
     */
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

    /**
     * Crée le tableau en chargeant la vue "affichageTableau.fxml".
     * Configure le contrôleur du tableau en lien avec le moteur de calcul et ajoute les colonnes au tableau.
     * Configure le service de simulation avec le contrôleur du tableau.
     * Initialise la scène et la fenêtre du tableau.
     */
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

    /**
     * Définit le moteur de calcul pour la simulation et met à jour le service de simulation avec le nouveau moteur.
     *
     * @param moteurCalcul Le moteur de calcul à utiliser pour la simulation.
     */
    public void setMoteurCalcul(MoteurCalcul moteurCalcul) {
        this.moteurCalcul = moteurCalcul;
        simulationService.setMoteurCalcul(moteurCalcul);
    }

    /**
     * Définit le moteur de calcul pour la simulation avec une échelle spécifiée.
     * Met à jour le service de simulation avec le nouveau moteur et l'échelle.
     *
     * @param moteurCalcul Le moteur de calcul à utiliser pour la simulation.
     * @param scale        L'échelle spécifiée pour la simulation.
     */
    public void setMoteurCalcul(MoteurCalcul moteurCalcul, double scale) {
        this.moteurCalcul = moteurCalcul;
        simulationService.setMoteurCalculEtScale(moteurCalcul, scale);
    }

    /**
     * Définit la gestion d'affichage pour la simulation.
     * Met à jour la référence vers la fenêtre principale de la calculatrice.
     *
     * @param gestionAffichage La gestion d'affichage à utiliser pour la simulation.
     */
    public void setGestionAffichage(GestionAffichage gestionAffichage) {
        this.gestionAffichage = gestionAffichage;
        this.stageCalculatrice = gestionAffichage.getStage();
    }

    /**
     * Définit la scène de simulation associée à cette fenêtre de contrôleur.
     *
     * @param stageSimulation La scène de simulation.
     */
    public void setStageSimulation(Stage stageSimulation) {
        this.stageSimulation = stageSimulation;
    }

    /**
     * Définit le nombre d'objets dans la simulation et initialise les paramètres pour chaque planète.
     *
     * @param nbObjets Le nombre d'objets à initialiser.
     */
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

    @FXML
    void changerFondDeSimOnAction(ActionEvent event) {
        try {
            FileChooser fileChooser = new FileChooser();
            File file = fileChooser.showOpenDialog(new Stage());
            Image image = new Image(file.getAbsolutePath());

            imageViewBackground.setImage(image);
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Veuillez choisir une image valide");
            alert.setTitle("Calculateur avancée");
            alert.setContentText("Réessayez plz");
            alert.showAndWait();
        }
    }
}