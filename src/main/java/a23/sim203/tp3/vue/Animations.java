package a23.sim203.tp3.vue;

import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Animations {

    private Rectangle2D screensize = Screen.getPrimary().getBounds();

    public void partageDesFenetresSimulation(Stage stageSimulation, Stage stageCalculatrice){
        stageSimulation.setWidth(screensize.getMaxX()/2);
        stageCalculatrice.setWidth(screensize.getMaxX()/2);

        stageSimulation.setHeight(screensize.getMaxY());
        stageCalculatrice.setHeight(screensize.getMaxY());

        stageCalculatrice.setX(0);
        stageSimulation.setX(stageCalculatrice.getX()+stageCalculatrice.getWidth());

        stageCalculatrice.setY(0);
        stageSimulation.setY(0);
    }

    public void partageDesFenetresTableau(Stage stageSimulation, Stage stageCalculatrice, Stage stageTableau){
        stageSimulation.setWidth(screensize.getMaxX()/2);
        stageCalculatrice.setWidth(screensize.getMaxX()/2);
        stageTableau.setWidth(screensize.getMaxX()/2);

        stageSimulation.setHeight(screensize.getHeight()/2);
        stageTableau.setHeight(screensize.getHeight()/2);

        stageCalculatrice.setX(0);
        stageCalculatrice.setY(0);

        stageSimulation.setX(screensize.getWidth()/2);
        stageTableau.setX(screensize.getWidth()/2);
        stageSimulation.setY(0);
        stageTableau.setY(screensize.getHeight()/2);
    }

    public void partageDesFenetresGraphique(Stage stageSimulation, Stage stageCalculatrice, Stage stageGraphique){

    }


}
