package a23.sim203.tp3.vue;

import a23.sim203.tp3.modele.MoteurCalcul;
import a23.sim203.tp3.services.SimulationService;
import javafx.animation.KeyFrame;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.mariuszgromada.math.mxparser.Constant;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class Animations {
    @FXML
    private Button boutonLancer;

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

        Button playButton = new Button("Play");
        playButton.setDisable(false);
        playButton.setOnAction(event -> departChronomètre());

        Button stopButton = new Button("Stop");
        stopButton.setDisable(false);
        stopButton.setOnAction(event -> arreterChronometre());


        root.getChildren().addAll(timerLabel, playButton, stopButton);

        Scene scene = new Scene(root, 200, 100);

        stageAnimation.setTitle("Chronomètre");
        stageAnimation.setScene(scene);
        stageAnimation.show();
    }

    private void departChronomètre() {
        // Disable the play button once the timer starts
        (timerLabel.getParent().getChildrenUnmodifiable().filtered(node -> node instanceof Button).get(0)).setDisable(true);

        timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), event -> {
                    secondsElapsed++;
                    updateTimerLabel();
                })
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void arreterChronometre() {
        // Enable the play button and disable the stop button when the timer stops
        Button playButton = (Button) timerLabel.getParent().getChildrenUnmodifiable().filtered(node -> node instanceof Button && ((Button) node).getText().equals("Play")).get(0);
        Button stopButton = (Button) timerLabel.getParent().getChildrenUnmodifiable().filtered(node -> node instanceof Button && ((Button) node).getText().equals("Stop")).get(0);

        playButton.setDisable(false);
        stopButton.setDisable(true);

        if (timeline != null) {
            timeline.stop();
        }
    }


    private void updateTimerLabel() {
        int minutes = secondsElapsed / 60;
        int seconds = secondsElapsed % 60;
        String formattedTime = String.format("%02d:%02d", minutes, seconds);
        timerLabel.setText(formattedTime);
    }
}
