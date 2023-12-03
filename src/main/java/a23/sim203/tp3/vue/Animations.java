package a23.sim203.tp3.vue;

import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Animations {

    private Rectangle2D screensize = Screen.getPrimary().getBounds();

    private Stage stageSimulation;
    private Stage stageCalculatrice;

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


}
