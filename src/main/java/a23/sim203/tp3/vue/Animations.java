package a23.sim203.tp3.vue;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Cette classe gère diverses animations liées à l'interface utilisateur.
 * Elle inclut des méthodes pour organiser les fenêtres, gérer un chronomètre,
 * et effectuer des animations de chargement.
 */
public class Animations {

    private int frameIndex = 0;
    private final String[] spinner = {"|", "/", "-", "\\"};
    private Label loadingLabel = new Label("En attente "); //spinner[frameIndex]

    private Rectangle2D screensize = Screen.getPrimary().getBounds();

    private Stage stageSimulation;
    private Stage stageCalculatrice;
    private int secondsElapsed = 0;
    private Timeline timeline;
    private Label timerLabel;

    /**
     * Définit les stages de simulation et de calculatrice pour être utilisés dans les animations.
     *
     * @param stageSimulation   Le stage de simulation.
     * @param stageCalculatrice Le stage de la calculatrice.
     */
    public void setStages(Stage stageSimulation, Stage stageCalculatrice) {
        this.stageSimulation = stageSimulation;
        this.stageCalculatrice = stageCalculatrice;
    }

    /**
     * Organise les fenêtres en fonction du nombre de fenêtres à afficher et du stage à montrer.
     *
     * @param shownStagesCount Le nombre de fenêtres à afficher.
     * @param stageToShow      Le stage à montrer.
     */
    public void sortLesMethodes(int shownStagesCount, Stage stageToShow) {
        if (shownStagesCount == 3) {
            partageTroisFenetres(stageToShow);
        }
        if (shownStagesCount == 4) {
            partageQuatreFenetres(stageToShow);
        }
        if (shownStagesCount == 2) {
            partageDesFenetresSimulation(stageSimulation, stageCalculatrice);
        }
    }

    /**
     * Organise les positions et dimensions des stages pour afficher trois fenêtres.
     *
     * @param stageToShow Le stage à montrer après l'organisation.
     */
    private void partageTroisFenetres(Stage stageToShow) {
        stageSimulation.setWidth(screensize.getMaxX() / 2);
        stageCalculatrice.setWidth(screensize.getMaxX() / 2);
        stageToShow.setWidth(screensize.getMaxX() / 2);

        stageSimulation.setHeight(screensize.getHeight() / 2);
        stageToShow.setHeight(screensize.getHeight() / 2);

        stageCalculatrice.setX(0);
        stageCalculatrice.setY(0);

        stageSimulation.setX(screensize.getWidth() / 2);
        stageToShow.setX(screensize.getWidth() / 2);
        stageSimulation.setY(0);
        stageToShow.setY(screensize.getHeight() / 2);
    }

    /**
     * Organise les positions et dimensions des stages pour afficher quatre fenêtres.
     *
     * @param stageToShow Le stage à montrer après l'organisation.
     */
    public void partageQuatreFenetres(Stage stageToShow) {
        stageCalculatrice.setX(0);
        stageCalculatrice.setY(0);

        stageToShow.setX(0);
        stageToShow.setY(screensize.getHeight() / 2);

        stageCalculatrice.setHeight(screensize.getHeight() / 2);
        stageToShow.setHeight(screensize.getHeight() / 2);

        stageToShow.setWidth(screensize.getWidth() / 2);
    }

    /**
     * Organise les positions et dimensions des stages de simulation et de calculatrice.
     *
     * @param stageSimulation   Le stage de simulation.
     * @param stageCalculatrice Le stage de la calculatrice.
     */
    public void partageDesFenetresSimulation(Stage stageSimulation, Stage stageCalculatrice) {
        stageSimulation.setWidth(screensize.getMaxX() / 2);
        stageCalculatrice.setWidth(screensize.getMaxX() / 2);

        stageSimulation.setHeight(screensize.getMaxY());
        stageCalculatrice.setHeight(screensize.getMaxY());

        stageCalculatrice.setX(0);
        stageSimulation.setX(stageCalculatrice.getX() + stageCalculatrice.getWidth());

        stageCalculatrice.setY(0);
        stageSimulation.setY(0);
    }

    /**
     * Initialise et affiche la première animation du chronomètre dans le stage spécifié.
     *
     * @param stageAnimation Le stage pour afficher l'animation du chronomètre.
     */
    public void premiereAnimation(Stage stageAnimation) {

        VBox root = new VBox();

        timerLabel = new Label("00:00");
        timerLabel.setStyle("-fx-font-size: 80; -fx-text-fill: #00ff00;");
        root.setAlignment(Pos.CENTER);
        root.setMaxHeight(50);
        root.setMaxWidth(50);


        root.getChildren().add(timerLabel);

        Scene scene = new Scene(root, 225, 100);

        stageAnimation.setTitle("Chronomètre");
        stageAnimation.setScene(scene);
        stageAnimation.setX(screensize.getWidth() / 2 - (scene.getWidth() / 2));
        stageAnimation.setY(screensize.getHeight() / 2.2);
        stageAnimation.setAlwaysOnTop(true);
        stageAnimation.show();
    }

    /**
     * Démarre le chronomètre en initialisant la timeline pour mettre à jour le temps écoulé.
     */
    public void departChronometre() {
        timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), event -> {
                    secondsElapsed++;
                    updateTimerLabel();
                })
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
        departLoadingAnimation();
    }

    /**
     * Arrête le chronomètre en stoppant la timeline et en mettant à jour le label du chronomètre.
     */
    public void arreterChronometre() {
        if (timeline != null) {
            timeline.stop();
            updateInitialTimerLabel();
            pauseLoadingAnimation();
        }
    }

    /**
     * Reprend le chronomètre en relançant la timeline et l'animation de chargement.
     */
    public void resumeChronometre() {
        timeline.play();
        departLoadingAnimation();

    }

    /**
     * Met en pause le chronomètre en suspendant la timeline et l'animation de chargement.
     */
    public void pauseChronometre() {
        timeline.pause();
        pauseLoadingAnimation();
    }

    /**
     * Met à jour le label du chronomètre en fonction du temps écoulé.
     */
    private void updateTimerLabel() {
        int minutes = secondsElapsed / 60;
        int seconds = secondsElapsed % 60;
        String formattedTime = String.format("%02d:%02d", minutes, seconds);
        timerLabel.setText(formattedTime);
    }

    /**
     * Réinitialise le label du chronomètre à zéro.
     */
    private void updateInitialTimerLabel() {
        secondsElapsed = 0;
        int minutes = secondsElapsed / 60;
        int seconds = secondsElapsed % 60;
        String formattedTime = String.format("%02d:%02d", minutes, seconds);
        timerLabel.setText(formattedTime);
    }

    public Timeline getTimeline() {
        return timeline;
    }

    /**
     * Initialise et affiche la deuxième animation dans le stage spécifié.
     *
     * @param primaryStage Le stage pour afficher la deuxième animation.
     */
    public void deuxiemeAnimation(Stage primaryStage) {
        StackPane root = new StackPane(loadingLabel);
        root.setAlignment(Pos.CENTER);

        Scene scene = new Scene(root, 200, 100);
        primaryStage.setTitle("Télécharge l'animation");
        primaryStage.setScene(scene);
        primaryStage.setX((screensize.getWidth() / 2) - (scene.getWidth() / 2));
        primaryStage.setY(screensize.getHeight() / 1.6);
        BackgroundFill[] backgroundFills = {
                new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY),
                new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY),
                new BackgroundFill(Color.GREEN, CornerRadii.EMPTY, Insets.EMPTY),
        };
        Background background = new Background(backgroundFills);
        root.setBackground(background);
        primaryStage.show();
    }

    /**
     * Démarre l'animation de chargement en utilisant une timeline pour changer le texte du label à intervalles réguliers.
     */
    public void departLoadingAnimation() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(100), event -> {
            loadingLabel.setText("Calcule " + spinner[frameIndex]);
            frameIndex = (frameIndex + 1) % spinner.length;
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    /**
     * Met en pause l'animation de chargement en modifiant le texte du label et en suspendant la timeline.
     */
    public void pauseLoadingAnimation() {
        loadingLabel.setText("En arrêt" + "/");
        frameIndex = 0;
        timeline.pause();
    }
}
