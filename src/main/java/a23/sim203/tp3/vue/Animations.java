package a23.sim203.tp3.vue;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Animations {

    private int frameIndex = 0;
    private final String[] spinner = {"|", "/", "-", "\\"};
    private final Label loadingLabel = new Label("Loading " + spinner[frameIndex]);

    private Rectangle2D screensize = Screen.getPrimary().getBounds();

    private Stage stageSimulation;
    private Stage stageCalculatrice;
    private int secondsElapsed = 0;
    private Timeline timeline;
    private Label timerLabel;

    public void setStages(Stage stageSimulation, Stage stageCalculatrice) {
        this.stageSimulation = stageSimulation;
        this.stageCalculatrice = stageCalculatrice;
    }


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

    public void partageQuatreFenetres(Stage stageToShow) {
        stageCalculatrice.setX(0);
        stageCalculatrice.setY(0);

        stageToShow.setX(0);
        stageToShow.setY(screensize.getHeight() / 2);

        stageCalculatrice.setHeight(screensize.getHeight() / 2);
        stageToShow.setHeight(screensize.getHeight() / 2);

        stageToShow.setWidth(screensize.getWidth() / 2);
    }

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

    public void premiereAnimation(Stage stageAnimation) {

        VBox root = new VBox();

        timerLabel = new Label("00:00");
        timerLabel.setStyle("-fx-font-size: 80; -fx-text-fill: #00ff00;");
        root.setAlignment(Pos.CENTER);
        root.setMaxHeight(50);
        root.setMaxWidth(50);


        root.getChildren().add(timerLabel);

        Scene scene = new Scene(root, 250, 150);

        stageAnimation.setTitle("Chronomètre");
        stageAnimation.setScene(scene);
        stageAnimation.show();
    }

    public void departChronometre() {
        timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), event -> {
                    secondsElapsed++;
                    updateTimerLabel();
                })
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    public void arreterChronometre() {
        if (timeline != null) {
            timeline.stop();
        }
    }

    public void resumeChronometre() {
        timeline.play();
    }

    public void pauseChronometre() {
        timeline.pause();
    }

    private void updateTimerLabel() {
        int minutes = secondsElapsed / 60;
        int seconds = secondsElapsed % 60;
        String formattedTime = String.format("%02d:%02d", minutes, seconds);
        timerLabel.setText(formattedTime);
    }

    public Timeline getTimeline() {
        return timeline;
    }

    public void deuxiemeAnimation(Stage primaryStage){
            StackPane root = new StackPane(loadingLabel);
            root.setAlignment(Pos.CENTER);

            Scene scene = new Scene(root, 300, 200);
            primaryStage.setTitle("Télécharge l'animation");
            primaryStage.setScene(scene);
            primaryStage.show();

            startLoadingAnimation();
        }

        private void startLoadingAnimation() {
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(100), event -> {
                loadingLabel.setText("Télécharge " + spinner[frameIndex]);
                frameIndex = (frameIndex + 1) % spinner.length;
            }));
            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.play();
        }
}
